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

package com.noxpvp.mmo.classes.tiers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.entity.ChargeEntityAbility;
import com.noxpvp.mmo.abilities.player.ChargePlayerAbility;
import com.noxpvp.mmo.classes.internal.ClassTier;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.PlayerClass;

public class AxesBerserkerClassTier extends ClassTier {

	public static final String TIER_NAME = "Berserker";

	private static volatile String dName = "Berserker";
	private final double maxHealth;
	private Map<String, Ability> abilities = new HashMap<String, Ability>();

	public AxesBerserkerClassTier(PlayerClass retainer) {
		super(retainer, TIER_NAME, 3);

		abilities.putAll(retainer.getTier(getTierLevel() - 1).getAbilityMap());
		abilities.put(ChargePlayerAbility.ABILITY_NAME.toLowerCase(), new ChargePlayerAbility(getPlayer(), 3));

		this.maxHealth = 26;
	}

	public String getDisplayName() {
		return getRetainingClass().getColor() + AxesBerserkerClassTier.dName;
	}

	public void setDisplayName(String displayName) {
		AxesBerserkerClassTier.dName = displayName;
	}

	public List<String> getLore() {
		return Collections.emptyList();
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public int getMaxExp(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNeededExp() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNeededExp(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getExp(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLevel(int level) {
		// TODO Auto-generated method stub

	}

	public int getMaxLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setExp(int amount) {
		// TODO Auto-generated method stub

	}

	public Map<String, Ability> getAbilityMap() {
		return Collections.unmodifiableMap(abilities);
	}

	public ExperienceType[] getExpTypes() {
		return ExperienceType.COMBAT;
	}

	@Override
	protected void load(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}

}
