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

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.AbilityCycler;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.gui.AbilityBindMenu;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class AbilityBindCommand extends BaseCommand {
	
	public static final String	COMMAND_NAME	= "bind";
	
	public AbilityBindCommand() {
		super(COMMAND_NAME, true);
	}
	
	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {
		final MMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(
				context.getPlayer());
		
		final Player player = context.getPlayer();
		final ItemStack currentItem = player.getItemInHand();
		
		if (currentItem == null || currentItem.getType() == Material.AIR)
			return new CommandResult(this, false, "todo: add no item in hand locale");
		
		AbilityCycler cycler = null;
		if ((cycler = mmoPlayer.getCycler(currentItem)) == null) {
			cycler = new AbilityCycler(Collections.EMPTY_LIST, mmoPlayer
					.getPlayerUUID());
			
			player.setItemInHand(cycler.setCycleItem(currentItem));
			
			mmoPlayer.addAbilityCycler(cycler);
			AbilityCycler.register(cycler);
		}
		
		new AbilityBindMenu(player, cycler).show();
		
		return new CommandResult(this, true);
	}
	
	public String[] getFlags() {
		return blankStringArray;
	}
	
	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
	
}
