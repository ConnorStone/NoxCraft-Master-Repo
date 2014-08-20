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

package com.noxpvp.mmo.renderers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.attribute.NbtFactory;
import com.comphenix.attribute.NbtFactory.NbtCompound;
import com.comphenix.packetwrapper.WrapperPlayServerSetSlot;
import com.noxpvp.mmo.AbilityCycler;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class ItemDisplayACRenderer extends BaseAbilityCyclerRenderer {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public ItemDisplayACRenderer(AbilityCycler cycler) {
		super(cycler);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Renders a view provided by the current through
	 * {@link com.noxpvp.mmo.AbilityCycler#current()}.
	 */
	@Override
	public void renderCurrent() {
		final AbilityCycler cycler = getCycler();
		if (cycler == null)
			return;
		
		final Player player = cycler.getPlayer();
		
		final PlayerAbility curAB = cycler.getCurrentAB();
		final String curABName = cycler.current();
		
		if (cycler == null || curAB == null || player == null)
			return;
		
		// Get list of all items, and show which is used with an arrow
		final String[] others = new String[cycler.getList().size()];
		final MMOPlayer mmop = MMOPlayerManager.getInstance().getPlayer(player);
		int i = 0;
		
		for (final String a : cycler.getList()) {
			if (curABName == a) {
				continue;
			}
			
			others[i++] = (curAB.getName().equals(curABName) ? ChatColor.GREEN + ">"
					: " ")
					+ curAB.getName();
		}
		
		// Set fake NBT data on item, and send update packet for that item
		final ItemStack item = NbtFactory.getCraftItemStack(getCycler().getPlayer()
				.getItemInHand().clone());
		
		final NbtCompound tag = NbtFactory.fromItemTag(item);
		tag.putPath("display.Name", ChatColor.GREEN + curAB.getName());
		tag.putPath("display.Lore", NbtFactory.createList((Object[]) others));
		
		// Send update packet
		getNewUpdate(item, (short) cycler.getSlotOfItem(item)).sendPacket(
				getCycler().getPlayer());
		
	}
	
	/**
	 * Renders a view provided by the
	 * {@link com.noxpvp.mmo.AbilityCycler#peekNext()} method.
	 */
	@Override
	public void renderNext() {
		
	}
	
	/**
	 * Renders a view provided by the
	 * {@link com.noxpvp.mmo.AbilityCycler#peekPrevious()} method.
	 */
	@Override
	public void renderPrevious() {
		
	}
	
	private WrapperPlayServerSetSlot getNewUpdate(ItemStack s, short slot) {
		final WrapperPlayServerSetSlot p = new WrapperPlayServerSetSlot();
		
		p.setSlotData(s);
		p.setSlot(slot);
		
		return p;
	}
}
