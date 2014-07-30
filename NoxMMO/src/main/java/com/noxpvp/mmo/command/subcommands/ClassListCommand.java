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

import java.util.ArrayList;
import java.util.List;

import com.noxpvp.mmo.util.PlayerClassUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.util.NoxMMOMessageBuilder;

public class ClassListCommand extends BaseCommand {

	public static final String COMMAND_NAME = "list";
	
	public ClassListCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return blankStringArray;
	}

	public int getMaxArguments() {
		return 0;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {
		
		NoxMMOMessageBuilder mb = new NoxMMOMessageBuilder(getPlugin(), true);
		Player p = context.getPlayer();
		
		for (PlayerClass clazz : PlayerClassUtil.getAllowedPlayerClasses(p)) {
			mb.yellow("Primary classes").gold(": ");
			
			List<String> names = new ArrayList<String>();
			if (clazz.isPrimaryClass())
				names.add(clazz.getDisplayName());
			
			mb.append(StringUtil.join(ChatColor.GOLD + ", ", names));
		}
		
		mb.newLine().newLine();
		for (PlayerClass clazz : PlayerClassUtil.getAllowedPlayerClasses(p)) {
			mb.yellow("Secondary classes").gold(": ");
			
			List<String> names = new ArrayList<String>();
			if (!clazz.isPrimaryClass())
				names.add(clazz.getDisplayName());
			
			mb.append(StringUtil.join(ChatColor.GOLD + ", ", names));
		}
		
		mb.headerClose(true);
		mb.send(p);
		
		return new CommandResult(this, true);
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}

}
