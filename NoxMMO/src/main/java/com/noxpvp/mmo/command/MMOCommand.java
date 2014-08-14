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

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.gui.NoxMMOMenu;

public class MMOCommand extends BaseCommand {
	
	public static final String	COMMAND_NAME	= "mmo";
	
	public MMOCommand() {
		super(COMMAND_NAME, true);
	}
	
	public String[] getFlags() {
		return blankStringArray;
	}
	
	@Override
	public CommandResult execute(CommandContext context) {
		new NoxMMOMenu(context.getPlayer()).show();
		
		return new CommandResult(this, true);
	}
	
	@Override
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}
	
}
