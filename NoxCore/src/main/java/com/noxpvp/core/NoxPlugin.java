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

package com.noxpvp.core;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.bergerkiller.bukkit.common.collections.StringMap;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.Command;
import com.noxpvp.core.commands.Command.CommandResult;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.commands.SafeNullPointerException;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.localization.GlobalLocale;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.CommandUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Level;

public abstract class NoxPlugin extends PluginBase {

	protected StringMap<Command> commandExecs = new StringMap<Command>();
	protected StringMap<String> commandAliases = new StringMap<String>();
	private File noxPluginFolder;

	public File getNoxPluginFolder() {
		if (noxPluginFolder == null && getDataFolder() != null) noxPluginFolder = new File(getDataFolder().getParentFile(), "Nox");
		if (!noxPluginFolder.mkdirs() && (!noxPluginFolder.exists() || noxPluginFolder.isFile())) log(Level.SEVERE, "Could not make the nox plugin folder!");

		return noxPluginFolder;
	}

	public void addPermissions(Class<? extends NoxPermission> permDefaults) {
		for (NoxPermission def : CommonUtil.getClassConstants(permDefaults))
			this.addPermission(def);
	}

	public void addPermission(NoxPermission perm) {
		NoxCore.getInstance().addPermission(perm);
	}

	public void addPermissions(NoxPermission... perms) {
		NoxCore.getInstance().addPermissions(perms);
	}

	@Override
	public boolean command(CommandSender sender, String command, String[] args) {
		String argLine = StringUtil.join(" ", args);
		CommandContext context = CommandUtil.parseCommand(sender, argLine);

		String cmdString = command;
		if (commandAliases.containsKeyLower(cmdString))
			cmdString = commandAliases.getLower(cmdString);

		if (commandExecs.containsKeyLower(cmdString)) {
			Command cmd = commandExecs.getLower(cmdString);
			if (cmd == null)
				throw new NullPointerException("Command execution class was null!");
			try {
				CommandResult result = cmd.executeCommand(context);

				if (!result.success)
					result.executer.displayHelp(context);
				MessageUtil.sendMessage(context.getSender(), MessageUtil.parseColor(result.extraMessages));

				return true;
			} catch (NoPermissionException e) {
				MessageUtil.sendLocale(sender, GlobalLocale.FAILED_PERMISSION_VERBOSE, e.getMessage(), e.getPermission());
			} catch (NullPointerException e) {
				MessageUtil.sendLocale(sender, GlobalLocale.ERROR_NULL, e.getMessage());
				if (!(e instanceof SafeNullPointerException))
					e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public abstract NoxCore getCore();

	public String getGlobalLocale(String path, String... args) {
		NoxCore c;
		if ((c = getCore()) == null)
			throw new IllegalStateException("This plugin depends on NoxCore! Its not loaded!");
		return c.getGlobalLocale(path, args);
	}

	/**
	 * Gets a localization configuration node
	 *
	 * @param path of the node to get
	 * @return Localization configuration node
	 */
	public ConfigurationNode getGlobalLocalizationNode(String path) {
		NoxCore c;
		if ((c = getCore()) == null)
			throw new IllegalStateException("This plugin depends on NoxCore! Its not loaded!");

		return c.getGlobalLocalizationNode(path);
	}

	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}

	public abstract PermissionHandler getPermissionHandler();

	public abstract Class<? extends ConfigurationSerializable>[] getSerialiables();

	public void registerCommand(Command runner) {
		if (runner == null)
			throw new IllegalArgumentException("Command Runner must not be null!");

		if (commandExecs.containsKey(runner.getName().toLowerCase(Locale.ENGLISH)))
			log(Level.WARNING, "Command - " + runner.getName() + " failed to register");

		commandExecs.put(runner.getName().toLowerCase(Locale.ENGLISH), runner);
		PluginCommand cmd = Bukkit.getPluginCommand(runner.getName().toLowerCase());
		try {
			cmd.setExecutor(this);
		} catch (NullPointerException e) {
			if (cmd == null && runner != null)
				log(Level.SEVERE, "Developer Error: Never put the command in plugins.yml\n"
						+ "\tCommand Name:" + runner.getName());
			else if (runner == null)
				log(Level.SEVERE, "Command runner was null!");
			else {
				log(Level.SEVERE, "Command Runner Failed to register. Null Pointer Exception. \n"
						+ "\tRunnerName: " + runner.getName() + "\n"
						+ "\tRunner Owner: " + CommonUtil.getPluginByClass(runner.getClass()).getName());
			}
			e.printStackTrace();
			return;
		}

		if (!LogicUtil.nullOrEmpty(cmd.getAliases()))
			for (String alias : cmd.getAliases())
				commandAliases.putLower(alias, cmd.getName());
	}

	public void registerCommands(Collection<Command> runners) {
		for (Command runner : runners)
			registerCommand(runner);
	}

	public void registerCommands(Command... runners) {
		for (Command runner : runners)
			registerCommand(runner);
	}

	public final void register(Reloader reloader) {
		getMasterReloader().addModule(reloader);
	}

	/**
	 * @return Core Master Reloader
	 */
	public final MasterReloader getMasterReloader() {
		return MasterReloader.getInstance();
	}
}
