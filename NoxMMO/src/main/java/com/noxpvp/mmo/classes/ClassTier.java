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

package com.noxpvp.mmo.classes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;

public abstract class ClassTier implements IClassTier {

	private final String name;
	private final int tierLevel;
	private PlayerClass retainer;
	
	public ClassTier(PlayerClass retainer, String name, int tierLevel) {
		this.name = name;
		this.tierLevel = tierLevel;
		this.retainer = retainer;
	}
	
	public void addExp(int amount) {
		setExp(getExp() + amount);
	}
	
	public boolean canUseLevel(int level) {
		return level <= getMaxLevel();
	}
	
	public final PlayerClass getAssociatedClass() { return retainer; }
	
	public final int getMaxExp() {
		return getMaxExp(getLevel());
	}
	
	public final String getName() {
		return name;
	}
	
	public final Player getPlayer() { return getAssociatedClass().getPlayer(); }
	
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
	
	public final int getTotalExp() {
		int ret = 0;
		for (int i = 1; i < getMaxLevel(); i ++)
			ret = getExp(i);
		
		return ret;
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
	
	/**
	 * Load custom data to configs.
	 * @param node Node is already at location to where data is saved. <br/>
	 */
	public abstract void load(ConfigurationNode node);
	
	public final void onLoad(ConfigurationNode node) {
		node = node.getNode("tier." + getTierLevel());
		setTotalExp(node.get("total-exp", 0));
		load(node);
	}
	
	public final void onSave(ConfigurationNode node) {
		node = node.getNode("tier." + getTierLevel());
		node.set("total-exp", getTotalExp());
		save(node);
	}
	public void removeExp(int amount) {
		setExp(getExp() - amount);
	}
	
	/**
	 * Save custom data to configs.
	 * @param node Node is already at location to where data is saved. <br/>
	 */
	public abstract void save(ConfigurationNode node);
	
	public void update() {
		setTotalExp(getTotalExp());
	}
}
