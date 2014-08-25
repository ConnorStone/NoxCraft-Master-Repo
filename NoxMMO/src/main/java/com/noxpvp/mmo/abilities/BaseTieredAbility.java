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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.abilities.internal.TieredAbility;
import com.noxpvp.mmo.locale.MMOLocale;

public abstract class BaseTieredAbility<T extends BaseAbilityTier<?>> implements
		TieredAbility<T> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final String	name;
	private int				currentTier;
	private final Set<T>	tiers;
	private ItemStack		identifingItem;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public BaseTieredAbility(final String name) {
		this.name = name;
		this.currentTier = 1;
		
		this.tiers = new HashSet<T>();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public boolean addTier(T tier) {
		final Iterator<T> iter = tiers.iterator();
		
		while (iter.hasNext()) {
			final T t = iter.next();
			if (t.getLevel() == tier.getLevel()) {
				iter.remove();
			}
		}
		
		return tiers.add(tier);
	};
	
	public AbilityResult<?> execute() {
		return execute(currentTier);
	}
	
	public AbilityResult<?> execute(int tierLevel) {
		Validate.isTrue(hasTier(tierLevel), "This ability does not have a level "
				+ tierLevel + " tier!");
		
		return getTier(tierLevel).execute();
	}
	
	public int getCurrentTier() {
		return currentTier;
	}
	
	public String getDescription() {
		return "\"Cryptic message here\"";
	}
	
	/**
	 * This implementation returns {@link MMOLocale#ABIL_DISPLAY_NAME}
	 * using method {@link MMOLocale#get(String...)} with the parameters as
	 * [getName(), getName()]
	 * <hr/>
	 * {@inheritDoc}
	 */
	public String getDisplayName() {
		return MMOLocale.ABIL_DISPLAY_NAME.get(getName(), "");
	}
	
	public String getDisplayName(ChatColor color) {
		return color + getDisplayName();
	}
	
	public ItemStack getIdentifiableItem() {
		if (identifingItem == null) {
			identifingItem = new ItemStack(Material.BLAZE_POWDER);
			
			final ItemMeta meta = identifingItem.getItemMeta();
			
			meta.setDisplayName(new MessageBuilder().gold(
					ChatColor.BOLD + "Ability: ").red(getName()).toString());
			meta.setLore(getLore(ChatColor.GOLD, 28));
			
			identifingItem.setItemMeta(meta);
		}
		
		return identifingItem.clone();
	}
	
	public ItemStack getIdentifiableItem(boolean canUse) {
		final ItemStack item = getIdentifiableItem();
		item.setType(canUse ? Material.BLAZE_POWDER : Material.SULPHUR);
		
		if (!canUse) {
			final ItemMeta meta = item.getItemMeta();
			final MessageBuilder obfuscated = new MessageBuilder();
			
			for (final String s : getDescription().split(" ")) {
				if (RandomUtils.nextFloat() > .2) {
					obfuscated.gold("").magic(s);
				} else {
					obfuscated.gold(s);
				}
				
				obfuscated.append(' ');
			}
			
			meta.setLore(MessageUtil.splitToList(obfuscated.toString(), 30));
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
	public List<String> getLore(ChatColor color, int linesLength) {
		final List<String> ret = new ArrayList<String>();
		
		for (final String cur : MessageUtil.splitToList(getDescription(),
				linesLength)) {
			ret.add(color + cur);
		}
		
		return ret;
	}
	
	public List<String> getLore(int linesLength) {
		return getLore(ChatColor.WHITE, linesLength);
	}
	
	public String getName() {
		return name;
	}
	
	public T getTier(int level) {
		for (final T tier : tiers)
			if (tier.getLevel() == level)
				return tier;
		
		return null;
	}
	
	public Set<T> getTiers() {
		return Collections.unmodifiableSet(tiers);
	}
	
	public String getUniqueId() {
		return getName() + "-" + hashCode();
	}
	
	public boolean hasTier(int level) {
		return getTier(level) != null;
	}
	
	public boolean mayExecute() {
		return true;
	}
	
	public boolean removeTier(int level) {
		final Iterator<T> iter = tiers.iterator();
		
		while (iter.hasNext()) {
			final T tier = iter.next();
			if (tier.getLevel() == level) {
				iter.remove();
				return true;
			}
		}
		
		return false;
	}
	
	public void setCurrentTier(int tierlevel) {
		Validate.isTrue(tierlevel > 0, "Tier level must be more that 0. -> "
				+ tierlevel + " <-");
		
		this.currentTier = tierlevel;
	}
}
