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

package com.noxpvp.core.localization;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.utils.gui.MessageUtil;

public class GlobalLocale extends NoxLocale {
	public static GlobalLocale HELP_HEADER;
	/**
	 * Argument 1: Plugin Name<br>
	 * Argument 2: Command Name (Context label preferred)
	 */
	public static GlobalLocale COMMAND_HELP_HEADER;
	public static GlobalLocale HEADER_CLOSE;
	public static GlobalLocale FAILED_PERMISSION;
	public static GlobalLocale FAILED_PERMISSION_VERBOSE;
	public static GlobalLocale COMMAND_SUCCESS;
	public static GlobalLocale COMMAND_FAILED;
	public static GlobalLocale ERROR_NULL;
	public static GlobalLocale CONSOLE_NEEDPLAYER;
	public static GlobalLocale CONSOLE_ONLYPLAYER;
	public static GlobalLocale CHAT_COLOR_GLOBAL;
	static {
		//Help Dialogs
		HELP_HEADER = new GlobalLocale("help.header", "&6[======~*~======([&c%0%&6])======~*~======]");
		COMMAND_HELP_HEADER = new GlobalLocale("help.command-header", "&6[======~*~======([&c%0% - %1%&6])======~*~======]");
		HEADER_CLOSE = new GlobalLocale("heap.header.close", "&6[======~*~========~.*.~========~*~======]");

		//Permissions
		FAILED_PERMISSION = new GlobalLocale("permission.denied.default", "&cNope&r: &e%0%");
		FAILED_PERMISSION_VERBOSE = new GlobalLocale("permission.denied.verbose", FAILED_PERMISSION.getDefault().replace("%0", "%0% >> Node '%1%'"));

		//Commands
		COMMAND_SUCCESS = new GlobalLocale("command.successful", "&aSuccessfully executed command: %0%");
		COMMAND_FAILED = new GlobalLocale("command.failed", "&cFailed to execute command: %0%");

		//ERRORS
		ERROR_NULL = new GlobalLocale("error.null", "&cA null pointer error occured: &c%0%");

		//CONSOLE
		CONSOLE_NEEDPLAYER = new GlobalLocale("console.needplayer", "This command requires a player: %0%");
		CONSOLE_ONLYPLAYER = new GlobalLocale("console.onlyplayer", "This command can only be run by a player.");


	}

	public GlobalLocale(String name, String def) {
		super(name, def);
	}

	@Override
	public String get(String... args) {
		return MessageUtil.parseColor(NoxCore.getInstance().getGlobalLocale(getName(), args));
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxCore.getInstance();
	}
}
