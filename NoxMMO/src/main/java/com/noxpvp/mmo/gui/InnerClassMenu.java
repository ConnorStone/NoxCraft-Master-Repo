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
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class InnerClassMenu extends CoreBox {
	
	public static final String	MENU_NAME	= "Class";
	private static final int	size		= 18;
	
	private IPlayerClass		clazz;
	
	public InnerClassMenu(final Player p, IPlayerClass clazz, CoreBox backButton) {
		super(p, clazz.getName() + " " + MMOLocale.GUI_MENU_NAME_COLOR.get()
				+ MENU_NAME, size, backButton);
		
		this.clazz = clazz;
		
		final Inventory box = getBox();
		
		box.setItem(0, clazz.getIdentifiableItem());
		
		final CoreBoxRegion abilities = new CoreBoxRegion(this, new Vector(1, 0, 0),
				1, 9);
		
		final MMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(p);
		
		for (final PlayerAbility ab : mmoPlayer.getAbilities()) {
			final ItemStack item = ab.getIdentifiableItem(clazz.getAbilities()
					.contains(ab));
			
			abilities.add(new CoreBoxItem(this, item) {
				
				public boolean onClick(InventoryClickEvent click) {
					return false;
				}
			});
			
		}
		
	}
	
	@Override
	public ItemStack getIdentifiableItem() {
		return clazz.getIdentifiableItem();
	}
	
	public IPlayerClass getPlayerClass() {
		return clazz;
	}
	
	@Override
	protected InnerClassMenu clone() {
		return new InnerClassMenu(getPlayer(), getPlayerClass(), getBackButton());
	}
	
	public abstract class ClassMenuItem extends CoreBoxItem {
		
		private final IPlayerClass	clazz;
		
		public ClassMenuItem(InnerClassMenu parent, ItemStack item,
				IPlayerClass clazz) {
			super(parent, item);
			
			this.clazz = clazz;
		}
		
		public IPlayerClass getPlayerClass() {
			return clazz;
		}
	}
	
}
