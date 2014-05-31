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

package com.noxpvp.core.commands;

import com.noxpvp.core.NoxCore;

public class PlayerOptionCommand extends BaseCommand {

	public static final String COMMAND_NAME = "options";

	public PlayerOptionCommand() {
		super(COMMAND_NAME, true);
	}

	@Override
	public CommandResult execute(CommandContext context) throws NoPermissionException {
		return new CommandResult(this, false);
	}

	@Override
	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}

	@Override
	public String[] getFlags() {
		return new String[] {"h", "help"};
	}

	@Override
	public int getMaxArguments() {
		return -1;
	}
}
