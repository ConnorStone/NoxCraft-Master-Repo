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

import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.core.gui.CoreBoxRegion;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.classes.internal.ClassTier;
import com.noxpvp.mmo.classes.internal.IClassTier;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class InnerClassMenu extends CoreBox {

	public static final String MENU_NAME = "Class";
	private static final int size = 27;

	private PlayerClass clazz;

	public InnerClassMenu(final Player p, PlayerClass clazz, CoreBox backButton) {
		super(p, clazz.getDisplayName() + " " + MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size, backButton);

		this.clazz = clazz;

		Inventory box = getBox();

		box.setItem(0, clazz.getIdentifiableItem());

		CoreBoxRegion tiers = new CoreBoxRegion(this, new Vector(0, 0, 2), 0, 7),
				abilities = new CoreBoxRegion(this, new Vector(2, 0, 0), 1, 9);

		final MMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(p);

		for (Map.Entry<Integer, IClassTier> tier : clazz.getTiers()) {
			if (!(tier.getValue() instanceof ClassTier))
				continue;

			ClassTier t = (ClassTier) tier.getValue();

			boolean canuse = clazz.canUseTier(tier.getKey());
//			boolean locked = t.getLevel() >= t.getMaxLevel()? true : false;//TODO When adding exp
			boolean hasTier = mmoPlayer.getPrimaryClass().getTier() == t ||
					mmoPlayer.getSecondaryClass().getTier() == t;

			ItemStack item = t.getIdentifiableItem();
			if (hasTier)
				item.setType(Material.SKULL);

			ItemMeta meta = item.getItemMeta();
//			String name = meta.getDisplayName() + " | " + (locked? ChatColor.DARK_RED + "LOCKED" : (canuse? ChatColor.GREEN + "OPEN" : ChatColor.RED + "NOT AVAILIBLE"));//TODO When adding exp
			String name = meta.getDisplayName() + " " + (canuse ? ChatColor.GREEN + "[OPEN" : ChatColor.RED + "[NOT AVAILIBLE") + "]";
			meta.setDisplayName(name);

			item.setItemMeta(meta);

			tiers.add(new ClassMenuItem(this, item, clazz, t.getTierLevel()) {

				public boolean onClick(InventoryClickEvent click) {
					PlayerClass clazz = InnerClassMenu.this.getPlayerClass();
					clazz.setCurrentTier(getTier());
					
					if (!clazz.canUseClass()) {
						MessageUtil.sendLocale(p, MMOLocale.CLASS_LOCKED, clazz.getDisplayName(), "Insufficient Permissions");
						return false;
					}

					mmoPlayer.setClass(clazz);

					StaticEffects.SmokeScreen(p.getEyeLocation(), 2);
					hide();
					return true;
				}
			});

		}
		
		List<Ability> used = new ArrayList<Ability>();
		for (Entry<Integer, IClassTier> t : clazz.getTiers()) {
			List<Ability> abs = new ArrayList<Ability>(t.getValue().getAbilities());
			abs.removeAll(used);
			
			for (Ability ab : abs) {
				used.add(ab);
				ItemStack item = ab.getIdentifiableItem(clazz.getAbilities().contains(ab));
				
				abilities.add(new CoreBoxItem(this, item) {
					public boolean onClick(InventoryClickEvent click) {
						return false;
					}
				});
				
			}
		}
	
	}

	@Override
	protected InnerClassMenu clone() {
		return new InnerClassMenu(getPlayer(), getPlayerClass(), this.getBackButton());
	}

	public PlayerClass getPlayerClass() {
		return clazz;
	}

	public abstract class ClassMenuItem extends CoreBoxItem {

		private int tier;
		private PlayerClass clazz;

		public ClassMenuItem(InnerClassMenu parent, ItemStack item, PlayerClass clazz, int tier) {
			super(parent, item);

			this.clazz = clazz;
			this.tier = tier;
		}

		public PlayerClass getPlayerClass() {
			return clazz;
		}

		public int getTier() {
			return tier;
		}
	}
	
	public ItemStack getIdentifiableItem() {
		return clazz.getIdentifiableItem();
	}

}
