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

package com.noxpvp.mmo.classes.internal;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.mmo.abilities.internal.Ability;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class ClassTier implements IClassTier, MenuItemRepresentable {

	private final String name;
	private final int tierLevel;
	private PlayerClass retainer;
	private ItemStack identifingItem;
	protected Map<String, Ability> abilities = new HashMap<String, Ability>();

	public ClassTier(PlayerClass retainer, String name, int tierLevel) {
		this.name = name;
		this.tierLevel = tierLevel;
		this.retainer = retainer;
	}

	public String getPermission() {
		return StringUtil.join(".", "nox", "class", retainer.getName(), "tier", Integer.toString(getTierLevel()));
	}
	
	public final String getName() {
		return name;
	}
	
	public ItemStack getIdentifiableItem() {
		boolean classCanUse = getRetainingClass().canUseTier(getTierLevel());
		ItemMeta meta;
		
		if (identifingItem == null) {
			identifingItem = new ItemStack(Material.STONE, getTierLevel());
			
			meta = identifingItem.getItemMeta();
			meta.setDisplayName(new MessageBuilder().gold(ChatColor.BOLD + "Tier: ")
					.append(getRetainingClass().getColor() + getName()).toString());
			
			meta.setLore(getLore());
			
			identifingItem.setItemMeta(meta);
		}
		
		meta = identifingItem.getItemMeta();
		if (classCanUse) {
			identifingItem.setType(Material.ENCHANTED_BOOK);
		} else {
			identifingItem.setType(Material.BOOK_AND_QUILL);
			
			meta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Tier: "
					+ getRetainingClass().getColor() + getName());
			
			List<String> lore = new ArrayList<String>();
			for (String cur : getLore()) lore.add(ChatColor.MAGIC + cur);
			
			meta.setLore(lore);
		}
		identifingItem.setItemMeta(meta);
		
		
		return identifingItem.clone();
	}

	public final PlayerClass getRetainingClass() {
		return retainer;
	}
	
	public boolean canUseLevel(int level) {
		return level <= getMaxLevel();
	}
	
	public boolean canUse() {
		if (retainer.getPlayer() != null)
			return retainer.getPlayer().hasPermission(getPermission());
		return false;
	}
	
	public void addExp(int amount) {
		setExp(getExp() + amount);
	}

	public final int getMaxExp() {
		return getMaxExp(getLevel());
	}

	public final Player getPlayer() {
		return getRetainingClass().getPlayer();
	}

	public final int getTotalExp() {
		int ret = 0;
		for (int i = 1; i < getMaxLevel(); i++)
			ret += getExp(i);

		return ret;
	}

	//TODO: Test class made for this method. This is a complex method...
	public final void setTotalExp(int amount) {
		Map<Integer, Integer> expCaps = new HashMap<Integer, Integer>();

		for (int i = 1; i < getMaxLevel(); i++) {
			expCaps.put(i, getMaxExp(i));
		}
		int i = 1;
		int originalLevel = getLevel();
		while (i < getMaxLevel()) {
			setLevel(i);
			int max = getMaxExp();

			if ((i + 1) >= getMaxLevel()) {
				setExp(amount);
				break;
			}

			if (amount > max) {
				amount -= max;
				setExp(max);
			} else {
				setExp(amount);
				break;
			}
			i++;
		}
		setLevel(originalLevel);
	}

	public final int getTierLevel() {
		return tierLevel;
	}

	public final int getExp() {
		return getExp(getLevel());
	}

	public final boolean hasExp(int amount) {
		return getExp() >= amount;
	}

	public final boolean hasLevel(int level) {
		return getLevel() >= level;
	}

	public final boolean hasReachedExpCap() {
		return getExp() >= getMaxExp();
	}

	public final boolean hasTotalExp(int amount) {
		return getTotalExp() >= amount;
	}

	public void removeExp(int amount) {
		setExp(getExp() - amount);
	}

	public void update() {
		setTotalExp(getTotalExp());
	}

	public final Collection<Ability> getAbilities() {
		return Collections.unmodifiableCollection(getAbilitiesMap().values());
	}

	/**
	 * {@inheritDoc}
	 * <b>If overridden you must call super!</b>
	 * @return map of serialized data.
	 */
	public Map<String, Object> serialize() {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		ret.put("exp", getTotalExp());
		
		return ret;
	}

	/**
	 * Load custom data to configs.
	 *
	 * @param data map of serialized data.
	 */
	protected abstract void load(Map<String, Object> data);

	/**
	 * Save custom data to configs.
	 *
	 * @param data map of serialized data.
	 */
	protected abstract void save(Map<String, Object> data);
	
	public final void onLoad(Map<String, Object> data) {
		setTotalExp((Integer) data.get("exp")); //Currently that is all there is needed! NO REALLY!
		load(data);
	}


	public Ability getAbility(String identity) {
		if (hasAbility(identity)) return getAbilitiesMap().get(identity);
		return null;
	}

	public boolean hasAbility(String identity) {
		return getAbilitiesMap().containsKey(identity);
	}

	public Map<String, Ability> getAbilitiesMap() {
		return abilities;
	}

	public void onSave(Map<String, Object> data) {
		save(data);
		data.put("exp", getTotalExp());

	}
}
