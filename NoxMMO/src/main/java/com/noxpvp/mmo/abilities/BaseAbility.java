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

package com.noxpvp.mmo.abilities;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.locale.MMOLocale;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAbility implements Ability {
	private final String name;
	private ItemStack identifingItem;

	public BaseAbility(final String name) {
		this.name = name;
	}

	/**
	 * This implementation returns {@link MMOLocale#ABIL_DISPLAY_NAME} using method {@link MMOLocale#get(String...)} with the parameters as [getName(), getName()]
	 * <hr/>
	 * {@inheritDoc}
	 */
	public String getDisplayName() {
		return MMOLocale.ABIL_DISPLAY_NAME.get(getName(), "");
	}

	public String getDisplayName(ChatColor color) {
		return color + getDisplayName();
	}

	public String getName() {
		return name;
	}
	
	public String getUniqueId() {
		return getName() + "-" + hashCode();
	}

	public String getDescription() {
		return "\"Cryptic message here\"";
	}

	public List<String> getLore(int linesLength) {
		return getLore(ChatColor.WHITE, linesLength);
	}

	public List<String> getLore(ChatColor color, int linesLength) {
		List<String> ret = new ArrayList<String>();

		for (String cur : MessageUtil.convertStringForLore(getDescription(), linesLength)) {
			ret.add(color + cur);
		}

		return ret;
	}
	
	public ItemStack getIdentifiableItem(boolean canUse) {
		ItemStack item = getIdentifiableItem();
		item.setType(canUse? Material.BLAZE_POWDER : Material.SULPHUR);
		
		if (!canUse) {
			ItemMeta meta = item.getItemMeta();
			MessageBuilder obfuscated = new MessageBuilder();
			
			for (String s : getDescription().split(" ")) {
				if (RandomUtils.nextFloat() > .2)
					obfuscated.gold("").magic(s);
				else
					obfuscated.gold(s);
				
				obfuscated.append(' ');
			}
			
			meta.setLore(MessageUtil.convertStringForLore(obfuscated.toString(), 40));
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
	public ItemStack getIdentifiableItem() {
		if (identifingItem == null) {
			identifingItem = new ItemStack(Material.BLAZE_POWDER);

			ItemMeta meta = identifingItem.getItemMeta();
			
			meta.setDisplayName(new MessageBuilder().gold(ChatColor.BOLD + "Ability: ").red(getName()).toString());
			meta.setLore(getLore(ChatColor.GOLD, 28));
			
			identifingItem.setItemMeta(meta);
		}
		
		return identifingItem.clone();
	}

	public boolean mayExecute() {
		return true;
	}
}
