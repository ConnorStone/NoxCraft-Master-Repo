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

package com.noxpvp.mmo.command;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.command.subcommands.ClassInfoCommand;
import com.noxpvp.mmo.command.subcommands.ClassListCommand;
import com.noxpvp.mmo.command.subcommands.ClassSwitchCommand;

public class ClassCommand extends BaseCommand {
	
	public static final String	  COMMAND_NAME	= "class";
	
	private static final String[]	flags	   = new String[] {};
	
	public ClassCommand() {
		super(COMMAND_NAME, true);
		
		registerSubCommands(
		        new ClassSwitchCommand(),
		        new ClassInfoCommand(),
		        new ClassListCommand());
	}
	
	public String[] getFlags() {
		return flags;
	}
	
	@Override
	public CommandResult execute(CommandContext context)
	        throws NoPermissionException {
		return new CommandResult(this, false);
	}
	
	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
}
