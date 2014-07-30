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

import com.noxpvp.mmo.abilities.player.HammerOfThorPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.MassDestructionPlayerAbility;
import com.noxpvp.mmo.classes.internal.ClassTier;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.PlayerClass;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AxesWarlordClassTier extends ClassTier {

	public static final String TIER_NAME = "Warlord";

	private static volatile String dName = "Warlord";
	private final double maxHealth;

	public AxesWarlordClassTier(PlayerClass retainer) {
		super(retainer, TIER_NAME, 4);

		abilities.putAll(retainer.getTier(getTierLevel() - 1).getAbilitiesMap());
		abilities.put(HammerOfThorPlayerAbility.ABILITY_NAME.toLowerCase(), new HammerOfThorPlayerAbility(retainer.getOfflinePlayer()));
		abilities.put(MassDestructionPlayerAbility.ABILITY_NAME.toLowerCase(), new MassDestructionPlayerAbility(retainer.getOfflinePlayer()));

		this.maxHealth = 28;
	}

	public String getDisplayName() {
		return getRetainingClass().getColor() + AxesWarlordClassTier.dName;
	}

	public void setDisplayName(String displayName) {
		AxesWarlordClassTier.dName = displayName;
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

	public ExperienceType[] getExpTypes() {
		return ExperienceType.COMBAT;
	}

	@Override
	protected void load(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}

	protected void save(Map<String, Object> data) {

	}
}
