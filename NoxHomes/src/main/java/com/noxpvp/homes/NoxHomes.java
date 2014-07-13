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

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.collections.StringMap;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.noxpvp.core.MasterReloader;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.Command;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.reloader.BaseReloader;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.StaticCleaner;
import com.noxpvp.homes.commands.*;
import com.noxpvp.homes.limits.BackupHomeLimit;
import com.noxpvp.homes.limits.BaseHomeLimit;
import com.noxpvp.homes.limits.GroupHomeLimit;
import com.noxpvp.homes.locale.HomeLocale;
import com.noxpvp.homes.managers.old.HomeLimitManager;
import com.noxpvp.homes.managers.old.HomesPlayerManager;
import com.noxpvp.homes.permissions.HomePermission;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.logging.Level;

public class NoxHomes extends NoxPlugin {

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

	private static final Class<? extends ConfigurationSerializable>[] serializables = new Class[]{
			BaseHome.class, NamedHome.class, DefaultHome.class, //Homes
			BaseHomeLimit.class, BackupHomeLimit.class, GroupHomeLimit.class //Limits
	};

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
		addPermissions(HomePermission.class);
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
		return serializables;
	}
}
