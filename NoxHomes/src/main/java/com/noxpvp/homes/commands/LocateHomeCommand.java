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

package com.noxpvp.homes.commands;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.homes.NoxHomes;


public class LocateHomeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "locatehome";

	private NoxHomes plugin;

	public LocateHomeCommand() {
		super(COMMAND_NAME, false);
		this.plugin = NoxHomes.getInstance();
	}

	public String[] getHelp() {
		return new String[]{"THIS COMMAND IS NOT IMPLEMENTED"};
	}

	public CommandResult execute(CommandContext context) {
		return new CommandResult(this, false);
	}

	public String[] getFlags() {
		return new String[]{"h", "help", "p", "player"};
	}

	@Override
	public NoxPlugin getPlugin() {
		return plugin;
	}

}
