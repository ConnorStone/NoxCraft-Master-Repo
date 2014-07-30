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

import com.bergerkiller.bukkit.common.collections.InterpolatedMap;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.classes.internal.ClassTier;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class DynamicClassTier extends ClassTier {

	private String displayName;
	private Map<Integer, Integer> expMap = new HashMap<Integer, Integer>();
	private ExperienceType[] expTypes = new ExperienceType[0];

	private int level, maxLevel;
	private InterpolatedMap levelToExpMap = new InterpolatedMap();
	private List<String> lore = new ArrayList<String>();

	private String permNode = "";
	private boolean useLevelPerms = false;

	/**
	 * @param name
	 * @param tierLevel current tier level
	 * @param maxLevel  Maximum allowed level.
	 */
	public DynamicClassTier(PlayerClass retainer, String name, int tierLevel, @Nullable Integer maxLevel) {
		super(retainer, name, tierLevel);

		if (maxLevel == null || maxLevel < 0)
			this.maxLevel = Integer.MAX_VALUE;
	}

	private static int round(double value) {
		int ret = 0;

		ret = MathUtil.floor(value);

		return ret;
	}

	@Override
	public boolean canUseLevel(int level) {
		if (super.canUseLevel(level)) {
			Player p = getPlayer();
			if (p != null) {
				if (useLevelPerms && !LogicUtil.nullOrEmpty(permNode))
					return NoxMMO.getInstance().getPermissionHandler().hasPermission(p, permNode);
				else
					return true;
			} else if (useLevelPerms)
				return false;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Load custom data to configs.
	 *
	 * @param data map of serialized data.
	 */
	@Override
	protected void load(Map<String, Object> data) {

	}

	protected void save(Map<String, Object> data) {

	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getExp(int level) {
		return expMap.get(level);
	}

	public ExperienceType[] getExpTypes() {
		return expTypes;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public double getMaxHealth() {
		return 20;
	}

	public int getMaxExp(int level) {
		return round(levelToExpMap.get(level));
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public int getNeededExp() {
		return getNeededExp(getLevel() + 1);
	}

	public int getNeededExp(int level) {

		return 0;
	}

	/**
	 * Loads the tiers configuration. <br>
	 * <p/>
	 * The node you supply must already be in the tiers section.
	 *
	 * @param node the node to traverse for settings.
	 */
	public void loadTierConfig(ConfigurationNode node) {
		ConfigurationNode eNode = node.getNode("exp-levels");
		for (String lvl : eNode.getKeys())
			if (ParseUtil.isNumeric(lvl)) {
				int l = ParseUtil.parseInt(lvl, -1);
				if (l < 0)
					continue;

				levelToExpMap.put(l, eNode.get(lvl, levelToExpMap.get(l)));
			}


	}

	/**
	 * Saves the tiers configuration. <br>
	 * <p/>
	 * The node you supply must already be in the tiers section.
	 *
	 * @param node preselected node.
	 */
	public void saveTierConfig(ConfigurationNode node) {

	}

	public void setExp(int amount) {
		boolean maxed = amount > getMaxExp();
		if (maxed) {
			amount -= getMaxExp();
			expMap.put(getLevel(), getMaxExp());
			setExp(amount);
		}

		if (amount <= 0) return;

		expMap.put(getLevel(), amount);
	}

	public Map<String, Ability> getAbilityMap() {
		return Collections.emptyMap();
	}
}
