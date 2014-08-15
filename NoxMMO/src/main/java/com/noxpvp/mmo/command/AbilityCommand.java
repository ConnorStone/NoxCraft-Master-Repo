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
import com.noxpvp.mmo.command.subcommands.AbilityBindCommand;
import com.noxpvp.mmo.command.subcommands.AbilityInfoCommand;
import com.noxpvp.mmo.command.subcommands.AbilityListCommand;

public class AbilityCommand extends BaseCommand {
	
	public static final String	  COMMAND_NAME	= "ability";
	
	private static final String[]	flags	   = new String[] {};
	
	public AbilityCommand() {
		super(COMMAND_NAME, true);
		registerSubCommand(new AbilityBindCommand());
		registerSubCommand(new AbilityInfoCommand());
		registerSubCommand(new AbilityListCommand());
	}
	
	@Override
	public CommandResult execute(CommandContext context)
	        throws NoPermissionException {
		
		return new CommandResult(this, false);
	}
	
	public String[] getFlags() {
		return flags;
	}
	
	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
	
}
