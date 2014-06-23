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

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.core.gui.CoreBoxRegion;
import com.noxpvp.mmo.OldMMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.locale.MMOLocale;

public class NoxMMOMenu extends CoreBox {

	public static final int size = 9;
	public static final String MENU_NAME = "NoxMMO";
	
	public NoxMMOMenu(Player p) {
		this(p, null);
	}
	
	public NoxMMOMenu(Player p, CoreBox backButton) {
		super(p, MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size, backButton);	
		
		Inventory box = getBox();
		OldMMOPlayer mp = MMOPlayerManager.getInstance().getPlayer(p);
		
		//Set up items
		CoreBoxRegion links = new CoreBoxRegion(this, new Vector(0, 0, 3), 0, 3);

		box.setItem(0, mp.getIdentifiableItem());
		box.setItem(1, mp.getPrimaryClass().getIdentifiableItem());
		
		//Class switch
		final ClassChooseMenu ccm = new ClassChooseMenu(p, NoxMMOMenu.this);
		links.add(new CoreBoxItem(this, ccm.getIdentifiableItem()) {
			public boolean onClick(InventoryClickEvent click) {
				ccm.show();
				return true;
			}
		});
		
		//Party manager
		final PartyManagerMenu pmm = new PartyManagerMenu(p, this);
		links.add(new CoreBoxItem(this, pmm.getIdentifiableItem()) {
			
			public boolean onClick(InventoryClickEvent click) {
				return false;
			}
		});
		
		//TODO More
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new NoxMMOMenu(getPlayer(), this.getBackButton());
	}
	
	@Override
	public ItemStack getIdentifiableItem() {
		return super.getIdentifiableItem();
	}
}
