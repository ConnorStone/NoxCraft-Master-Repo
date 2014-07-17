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

import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.player.CriticalHitPlayerAbility;
import com.noxpvp.mmo.abilities.player.LeapPlayerAbility;
import com.noxpvp.mmo.classes.internal.ClassTier;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.PlayerClass;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AxesBasherClassTier extends ClassTier {

	public static final String TIER_NAME = "Basher";
	private static volatile String dName = "Basher";
	private final double maxHealth;
	private Map<String, Ability> abilities = new HashMap<String, Ability>();

	public AxesBasherClassTier(PlayerClass retainer) {
		super(retainer, TIER_NAME, 1);

		//abilities.put("Iron Grip", /* HERP */)
		abilities.put(LeapPlayerAbility.ABILITY_NAME.toLowerCase(), new LeapPlayerAbility(retainer.getPlayer()));
		abilities.put(CriticalHitPlayerAbility.ABILITY_NAME.toLowerCase(), new CriticalHitPlayerAbility(retainer.getPlayer()));

		this.maxHealth = 22;
	}

	public String getDisplayName() {
		return getRetainingClass().getColor() + AxesBasherClassTier.dName;
	}

	public void setDisplayName(String displayName) {
		AxesBasherClassTier.dName = displayName;
	}

	public List<String> getLore() {
		return Collections.emptyList();//XXX implement soon
	}

	public double getMaxHealth() {
		return this.maxHealth;
	}

	public int getMaxExp(int level) {
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
