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

package com.noxpvp.mmo.gui;

import com.noxpvp.core.gui.CoreBox;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PartyManagerMenu extends CoreBox {
	
	public static final String MENU_NAME = "Party Manager";
	public static final int size = 2;
	
	public PartyManagerMenu(Player p) {
		this(p, null);
	}
	
	public PartyManagerMenu(Player p, CoreBox backbutton) {
		super(p, MENU_NAME, size, backbutton);
	}
	
	@Override
	protected PartyManagerMenu clone() {
		return new PartyManagerMenu(getPlayer(), this.getBackButton());
	}
	
	@Override
	public ItemStack getIdentifiableItem() {
		ItemStack item = super.getIdentifiableItem();
		ItemMeta meta = item.getItemMeta();
		
		meta.setLore(Arrays.asList(ChatColor.AQUA + "Under Development"));
		item.setItemMeta(meta);
		
		return item;
	}

}
