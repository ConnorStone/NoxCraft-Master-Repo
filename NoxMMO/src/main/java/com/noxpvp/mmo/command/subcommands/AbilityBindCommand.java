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

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.Task;
import com.dsh105.holoapi.util.StringUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.gui.QuestionBox;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.AbilityCycler;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.internal.PassiveAbility;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.abilities.internal.SilentAbility;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class AbilityBindCommand extends BaseCommand {
	
	public static final String	  COMMAND_NAME	= "bind";
	private static final String[]	flags	   = new String[] { "h", "help",
	                                           "o", "overwrite" };
	
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
		// final int slot = player.getInventory().getHeldItemSlot();
		// //TODO: Implement item replacement?
		
		final AbilityCycler cycler = AbilityCycler.getCycler(context.getPlayer());
		final boolean exists = cycler != null;
		
		final boolean singleItem = context.hasArgument(0);
		
		final String arg;
		if (singleItem) {
			arg = StringUtil.join(context.getArguments(), " ");
		} else {
			arg = null;
		}
		
		final boolean safe = context.hasFlag("o") || context.hasFlag("overwrite")
		        || !exists;
		final Task t = new Task(NoxMMO.getInstance()) {
			
			public void run() {
				AbilityCycler cycler;
				if (exists) {
					cycler = AbilityCycler.getCycler(player);
				} else {
					cycler = new AbilityCycler(!singleItem ?
					        mmoPlayer.getAbilities() :
					        new ArrayList<PlayerAbility>(), player, currentItem);
				}
				
				cycler.getList().clear();
				final List<PlayerAbility> abs = new ArrayList<PlayerAbility>();
				if (singleItem) {
					for (final PlayerAbility a : mmoPlayer.getAbilities())
						if (a.getName().equalsIgnoreCase(arg)) {
							abs.add(a);
							break;
						}
					
					if (abs.isEmpty()) {
						MessageUtil
						        .sendLocale(player, MMOLocale.ABIL_NOT_FOUND, arg);
						return;
					}
				} else {
					for (final PlayerAbility a : mmoPlayer.getAbilities())
						if (!(a instanceof SilentAbility)
						        && !(a instanceof PassiveAbility<?>)) {
							abs.add(a);
						}
					
				}
				
				cycler.addAll(abs);
				mmoPlayer.getAbilityCyclers().add(cycler);
			}
		};
		
		if (!safe && exists) {
			new QuestionBox(context.getPlayer(), "Bind All Abilities?") {
				
				@Override
				public void onConfirm() {
					t.start();
					hide();
				}
				
				@Override
				public void onDeny() {
					hide();
				}
			}.show();
		} else if (safe) {
			t.start();
		}
		
		return new CommandResult(this, true);
	}
	
	public String[] getFlags() {
		return flags;
	}
	
	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
	
}
