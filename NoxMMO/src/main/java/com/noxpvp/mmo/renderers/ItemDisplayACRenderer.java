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

import com.noxpvp.mmo.abilities.internal.TieredAbility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.attribute.NbtFactory;
import com.comphenix.attribute.NbtFactory.NbtCompound;
import com.comphenix.packetwrapper.WrapperPlayServerSetSlot;
import com.noxpvp.mmo.AbilityCycler;

public class ItemDisplayACRenderer extends BaseAbilityCyclerRenderer {

	public ItemDisplayACRenderer(AbilityCycler cycler) {
		super(cycler);
	}
	
	private WrapperPlayServerSetSlot getNewUpdate(ItemStack s, short slot) {
		WrapperPlayServerSetSlot p = new WrapperPlayServerSetSlot();
		
		p.setSlotData(s);
		p.setSlot(slot);
		
		return p;
	}

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekNext()} method.
	 */
	@Override
	public void renderNext() {
	}

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekPrevious()} method.
	 */
	@Override
	public void renderPrevious() {

	}

	/**
	 * Renders a view provided by the current through {@link com.noxpvp.mmo.AbilityCycler#current()}.
	 */
	@Override
	public void renderCurrent() {
		AbilityCycler cycler = getCycler();
		if (cycler == null) return;
		final Player player = cycler.getPlayer();

		TieredAbility cur = cycler != null? cycler.current() : null;
		if (cycler == null || cur == null || player == null)
			return;
		
		//Get list of all items, and show which is used with an arrow
		String[] others = new String[cycler.getList().size()];
		int i = 0;
		for (TieredAbility a : cycler.getList())
			others[i++] = (a.equals(cur)? ChatColor.GREEN + ">" : " ") + a.getDisplayName(ChatColor.RED);
		
		//Set fake NBT data on item, and send update packet for that item
		ItemStack item = NbtFactory.getCraftItemStack(getCycler().getPlayer().getItemInHand().clone());
		NbtCompound tag = NbtFactory.fromItemTag(item);
		tag.putPath("display.Name", cur.getDisplayName(ChatColor.GOLD));
		tag.putPath("display.Lore", NbtFactory.createList(others));
		
		//Send update packet
		getNewUpdate(item, (short) cycler.getLastSlot()).sendPacket(getCycler().getPlayer());

	}
}
