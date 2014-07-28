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

package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

/**
 * @author NoxPVP
 */
public class DashEntityAbility extends BaseEntityAbility {

	public static final String ABILITY_NAME = "Dash";
	public static final String PERM_NODE = "dash";

	private int s = 2;
	private int d = (20 * 6);

	/**
	 * @param entity Entity to apply ability on
	 */
	public DashEntityAbility(LivingEntity entity) {
		super(ABILITY_NAME, entity);
	}

	/**
	 * @return Currently set speed amplifier
	 */
	public int getS() {
		return s;
	}

	/**
	 * @param s Speed effect amplifier (same as minecraft potions)
	 */
	public void setS(int s) {
		this.s = s;
	}

	/**
	 * @return Currently set duration time
	 */
	public int getD() {
		return d;
	}

	/**
	 * @param d Tick amount of speed duration (default is 6 seconds or 20*6)
	 */
	public void setD(int d) {
		this.d = d;
	}

	public AbilityResult<DashEntityAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<DashEntityAbility>(this, false);

		((LivingEntity) getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, d, s, true));

		return new AbilityResult<DashEntityAbility>(this, true);
	}

}
