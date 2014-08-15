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

package com.noxpvp.mmo.command.subcommands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.localization.GlobalLocale;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.gui.ClassChooseMenu;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassSwitchCommand extends BaseCommand {
	
	public static final String	  COMMAND_NAME	= "switch";
	
	private static final String[]	flags	   = new String[] { "nogui" };
	
	public ClassSwitchCommand() {
		super(COMMAND_NAME, true);
	}
	
	public String[] getFlags() {
		return flags;
	}
	
	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder(GlobalLocale.HELP_HEADER.get(
		        "NoxMMO : Classes", COMMAND_NAME));
		mb.newLine().green("Flags").newLine().setIndent(1);
		boolean first = true;
		mb.red("");
		for (String s : getFlags()) {
			mb.append(s);
			if (first) {
				first = false;
				mb.setSeparator(", ");
			}
		}
		mb.setIndent(0).setSeparator("");
		return mb.lines();
	}
	
	public int getMaxArguments() {
		return 2;
	}
	
	@Override
	public CommandResult execute(CommandContext context)
	        throws NoPermissionException {
		if (true || !context.hasFlag("nogui")) {
			new ClassChooseMenu(context.getPlayer(), null).show();
			return new CommandResult(this, true);
		}
		
		return new CommandResult(this, true);
	}
	
	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
	
}
