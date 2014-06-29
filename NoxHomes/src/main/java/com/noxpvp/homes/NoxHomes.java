/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.homes;

import java.util.logging.Level;

import com.noxpvp.homes.managers.old.HomeLimitManager;
import com.noxpvp.homes.managers.old.HomesPlayerManager;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.collections.StringMap;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.MasterReloader;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.Command;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.BaseReloader;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.StaticCleaner;
import com.noxpvp.homes.commands.DeleteHomeCommand;
import com.noxpvp.homes.commands.HomeAdminCommand;
import com.noxpvp.homes.commands.HomeAdminImportCommand;
import com.noxpvp.homes.commands.HomeCommand;
import com.noxpvp.homes.commands.HomeListCommand;
import com.noxpvp.homes.commands.SetHomeCommand;
import com.noxpvp.homes.locale.HomeLocale;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;

public class NoxHomes extends NoxPlugin {

	public static final String HOMES_NODE = "nox.homes";
	/**
	 * <b> BE SURE TO HAVE THOSE CLASSES IMPLEMENT <u>Command</u> OR YOU RISK CRASH!</b>
	 */
	@SuppressWarnings("unchecked")
	private static final Class<Command>[] commands = (Class<Command>[]) new Class<?>[]{
			DeleteHomeCommand.class,
			HomeAdminCommand.class,
			HomeCommand.class,
			HomeListCommand.class,
//		LocateHomeCommand.class,
			SetHomeCommand.class
	};
	private static NoxHomes instance;
	NoxCore core;
	private PermissionHandler permHandler;

	public static NoxHomes getInstance() {
		return instance;
	}

	private static void setInstance(NoxHomes homes) {
		instance = homes;
	}

	@Override
	public void disable() {
		saveConfig();

		getHomeManager().save();
		getLimitsManager().save();

		String[] internals = {};
		Class<?>[] classes = {
				HomesPlayerManager.class,
				HomeLimitManager.class,
		};

		new StaticCleaner(this, getClassLoader(), internals, classes).resetAll();

		setInstance(null);
	}

	@Override
	public void enable() {
		if (instance != null) {
			log(Level.SEVERE, "Instance already running of NoxHomes!");
			log(Level.SEVERE, "Self Disabling new instance.");
			setEnabled(false);
			return;
		}

		Common.loadClasses("com.noxpvp.homes.locale.HomeLocale");
		permHandler = new PermissionHandler(this);

		instance = this;
		core = NoxCore.getInstance();

		commandExecs = new StringMap<Command>();

		getLimitsManager().load();
		getHomeManager().load();

		Reloader r = new BaseReloader(MasterReloader.getInstance(), "NoxHomes") {
			public boolean reload() {
				reloadConfig();
				return true;
			}
		};

		r.addModule(new BaseReloader(r, "homes") {
			public boolean reload() {
				getHomeManager().load();
				return true;
			}
		});

		r.addModule(new BaseReloader(r, "limits") {
			public boolean reload() {
				getLimitsManager().load();
				return true;
			}
		});

		MasterReloader.getInstance().addModule(r);
		registerAllCommands();

	}

	public NoxCore getCore() {
		return core;
	}

	public HomesPlayerManager getHomeManager() {
		return HomesPlayerManager.getInstance();

	}

	@Override
	public void permissions() {
		addPermission(
				new NoxPermission(this, "homes.*", "All of NoxHomes permissions Including admin.", PermissionDefault.FALSE,
						new NoxPermission(this, "homes.admin.*", "All NoxHome's Admin Permissions", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "history"), "Ability to read Home History.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "wipe", "*"), "Permission to wipe all data.", PermissionDefault.OP,
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "wipe", "history"), "Permission to wipe history.", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "wipe", "homes"), "Permission to wipe homes data.", PermissionDefault.OP)
								),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "load"), "Permission to reload plugin.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "save"), "Permission to force save.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", HomeAdminImportCommand.PERM_NODE), "Permission to migrate save data to and from plugin.", PermissionDefault.OP)
						),
						new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "*"), "Allowed to set home of everything including others.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, DefaultHome.PERM_NODE), "Allowed to set default home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, NamedHome.PERM_NODE), "Allowed to set named homes.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", NoxHomes.HOMES_NODE, SetHomeCommand.PERM_NODE, "other-towns"), "Allows setting homes in other towns you don't belong to.", PermissionDefault.FALSE),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "others", "*"), "Allowed to set any type of other peoples homes.", PermissionDefault.FALSE,
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "others", DefaultHome.PERM_NODE), "Allowed to set other peoples default homes.", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "others", NamedHome.PERM_NODE), "Allowed to set other peoples named homes.", PermissionDefault.OP)
								)
						),
						new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "*"), "Allowed to warp to any type of home. Including others.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "default"), "Allowed to warp to your default home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "named"), "Allowed to warp to your named home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "*"), "Allowed to warp to others homes. Without invitiation.", PermissionDefault.FALSE,
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "default"), "Allowed to warp to other peoples default home's.", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "named"), "Allowed to warp to other peoples named home's", PermissionDefault.OP)
								)
						),
						new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "*"), "Allowed to delete any and all homes.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "default"), "Allowed to delete your default home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "named"), "Allowed to delete your named home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "*"), "Allowed to delete any type of other people's homes.", PermissionDefault.FALSE,
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "default"), "Allowed to delete other peoples default home's", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "named"), "Allowed to delete other peoples named home's", PermissionDefault.OP)
								)
						),
//						new NoxPermission(this, "homes.invite.*", "Allowed to set invites to any home of anyone.", PermissionDefault.FALSE,
//								new NoxPermission(this, "homes.invite.default", "Allowed to invite others into your default home.", PermissionDefault.OP),
//								new NoxPermission(this, "homes.invite.named", "Allowed to invite others into your named home.", PermissionDefault.OP),
//								new NoxPermission(this, "homes.invite.others.*", "Allowed to invite others into other people's homes.", PermissionDefault.FALSE,
//										new NoxPermission(this, "homes.invite.others.default", "Allowed to invite others into other people's default home.", PermissionDefault.OP),
//										new NoxPermission(this, "homes.invite.others.named", "Allowed to invite others into other people's named home's." , PermissionDefault.OP)
//								)
//						),
						new NoxPermission(this, "homes.locate.*", "Allow you to locate any and all homes.", PermissionDefault.FALSE,
								new NoxPermission(this, "homes.locate.default.*", "Allows you to retrieve coords and set compass to your default home.", PermissionDefault.OP,
										new NoxPermission(this, "homes.locate.default.compass", "Allows you to use a compass to locate your home.", PermissionDefault.OP),
										new NoxPermission(this, "homes.locate.default.coords", "Allows retrieval and display of the coords of your home.", PermissionDefault.OP)
								),
								new NoxPermission(this, "homes.locate.named.*", "Allows you to retrieve coords and set compass to your named home.", PermissionDefault.OP,
										new NoxPermission(this, "homes.locate.named.compass", "Allows you to use a compass to locate your home.", PermissionDefault.OP),
										new NoxPermission(this, "homes.locate.named.coords", "Allows retrieval and display of the coords of your home.", PermissionDefault.OP)
								),
								new NoxPermission(this, "homes.locate.others.*", "Allows you to locate any type of other people's homes.", PermissionDefault.FALSE,
										new NoxPermission(this, "homes.locate.others.default.*", "Allows you to locate other's Default homes.", PermissionDefault.OP,
												new NoxPermission(this, "homes.locate.others.default.compass", "Allows you to locate others Default home with a compass.", PermissionDefault.OP),
												new NoxPermission(this, "homes.locate.others.default.compass", "Allows you to locate others Default home with coords.", PermissionDefault.OP)
										),
										new NoxPermission(this, "homes.locate.others.named.*", "Allows you to locate others Named homes.", PermissionDefault.OP,
												new NoxPermission(this, "homes.locate.others.named.compass", "Allows you to locate others Named home with a compass.", PermissionDefault.OP),
												new NoxPermission(this, "homes.locate.others.named.coords", "Allows you to locate others Named home with coords.", PermissionDefault.OP)
										)
								)
						),
						new NoxPermission(this, StringUtil.join(".", "homes", HomeListCommand.LIST_PERM_NODE, "*"), "Allows to list any home of any person.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.join(".", "homes", HomeListCommand.LIST_PERM_NODE, "own"), "List your own homes.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", "homes", HomeListCommand.LIST_PERM_NODE, "others"), "List other people's homes.", PermissionDefault.OP)
						)
				)
		);
	}

	@Override
	public void localization() {
		loadLocales(HomeLocale.class);
	}

	private void registerAllCommands() {
		for (Class<Command> cls : commands) {
			SafeConstructor<Command> cons = new SafeConstructor<Command>(cls, new Class[0]);
			Command rn = cons.newInstance();
			if (rn != null)
				registerCommand(rn);
		}
	}

	public HomeLimitManager getLimitsManager() {
		return HomeLimitManager.getInstance(this);
	}

	@Override
	public PermissionHandler getPermissionHandler() {
		return permHandler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends ConfigurationSerializable>[] getSerialiables() {
		return new Class[]{BaseHome.class, NamedHome.class, DefaultHome.class};
	}
}
