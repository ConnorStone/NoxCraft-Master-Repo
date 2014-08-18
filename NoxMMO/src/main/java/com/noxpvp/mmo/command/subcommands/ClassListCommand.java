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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.util.NoxMMOMessageBuilder;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassListCommand extends BaseCommand {
	
	public static final String	COMMAND_NAME	= "list";
	
	public ClassListCommand() {
		super(COMMAND_NAME, true);
	}
	
	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {
		
		final NoxMMOMessageBuilder mb = new NoxMMOMessageBuilder(getPlugin(), true);
		final Player p = context.getPlayer();
		
		for (final IPlayerClass clazz : PlayerClassUtil.getUsableClasses(p)) {
			mb.yellow("Primary classes").gold(": ");
			
			final List<String> names = new ArrayList<String>();
			if (clazz.isPrimaryClass()) {
				names.add(clazz.getName());
			}
			
			mb.append(StringUtil.join(ChatColor.GOLD + ", ", names));
		}
		
		mb.newLine().newLine();
		for (final IPlayerClass clazz : PlayerClassUtil.getUsableClasses(p)) {
			mb.yellow("Secondary classes").gold(": ");
			
			final List<String> names = new ArrayList<String>();
			if (!clazz.isPrimaryClass()) {
				names.add(clazz.getName());
			}
			
			mb.append(StringUtil.join(ChatColor.GOLD + ", ", names));
		}
		
		mb.headerClose(true);
		mb.send(p);
		
		return new CommandResult(this, true);
	}
	
	public String[] getFlags() {
		return blankStringArray;
	}
	
	@Override
	public int getMaxArguments() {
		return 0;
	}
	
	@Override
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}
	
}
