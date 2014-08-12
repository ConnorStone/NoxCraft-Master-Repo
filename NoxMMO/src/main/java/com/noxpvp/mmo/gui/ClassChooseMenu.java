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

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.core.gui.CoreBoxRegion;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassChooseMenu extends CoreBox {
	
	public static final String	MENU_NAME		= "Class Selection";
	
	private static final String	MENU_MAIN_COLOR	= MMOLocale.GUI_MENU_NAME_COLOR
														.get();
	private static final int	size			= 18;
	
	public ClassChooseMenu(Player p, @Nullable CoreBox backButton) {
		super(p, MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size, backButton);
		
		final Inventory box = getBox();
		
		final List<PlayerClass> availableClasses = (List<PlayerClass>) PlayerClassUtil
				.getUsableClasses(p);
		
		final ItemStack primarySign = new ItemStack(Material.SIGN);
		final ItemStack secondarySign = new ItemStack(Material.SIGN);
		
		final ItemMeta pMeta = primarySign.getItemMeta();
		final ItemMeta sMeta = secondarySign.getItemMeta();
		
		final String lorePrefix = ChatColor.GREEN.toString() + ChatColor.ITALIC;
		
		pMeta.setDisplayName(MENU_MAIN_COLOR + "Pick a primary class");
		sMeta.setDisplayName(MENU_MAIN_COLOR + "Pick a Secondary class");
		
		pMeta.setLore(Arrays.asList(lorePrefix + "Click an item on this row",
				lorePrefix + "to select a primary class"));
		sMeta.setLore(Arrays.asList(lorePrefix + "Click an item on this row",
				lorePrefix + "to select a secondary class"));
		
		primarySign.setItemMeta(pMeta);
		secondarySign.setItemMeta(sMeta);
		
		box.setItem(0, primarySign);
		box.setItem(9, secondarySign);
		
		final CoreBoxRegion primarys = new CoreBoxRegion(this, new Vector(0, 0, 2),
				0, 7), secondarys = new CoreBoxRegion(this, new Vector(1, 0, 2), 0,
				7);
		
		for (final PlayerClass clazz : availableClasses) {
			
			final ClassChooseMenuItem boxItem = new ClassChooseMenuItem(this, clazz
					.getIdentifiableItem(), clazz) {
				
				public boolean onClick(InventoryClickEvent click) {
					
					if (getPlayerClass().isPrimaryClass()) {
						new InnerClassMenu(getPlayer(), getPlayerClass(),
								ClassChooseMenu.this).show();
						
						return true;
					} else {
						MMOPlayer mmoPlayer;
						if ((mmoPlayer = MMOPlayerManager.getInstance().getPlayer(
								getPlayer())) != null) {
							mmoPlayer.setSecondaryClass(getPlayerClass());
							hide();
							
							return true;
						}
						
						return false;
					}
					
				}
			};
			
			if (clazz.isPrimaryClass()) {
				primarys.add(boxItem);
				
			} else {
				secondarys.add(boxItem);
			}
		}
	}
	
	@Override
	public ItemStack getIdentifiableItem() {
		final ItemStack item = super.getIdentifiableItem();
		item.setType(Material.DIAMOND_CHESTPLATE);
		
		return item;
	}
	
	@Override
	protected ClassChooseMenu clone() {
		return new ClassChooseMenu(getPlayer(), null);
		
	}
	
	public abstract class ClassChooseMenuItem extends CoreBoxItem {
		
		private final PlayerClass	pClass;
		
		public ClassChooseMenuItem(ClassChooseMenu parent, ItemStack item,
				PlayerClass clazz) {
			super(parent, item);
			
			pClass = clazz;
		}
		
		public PlayerClass getPlayerClass() {
			return pClass;
		}
		
	}
	
}
