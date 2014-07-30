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

package com.noxpvp.mmo.abilities.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.runnables.HealRunnable;

/**
 * @author NoxPVP
 */
public class RejuvenationPlayerAbility extends BasePlayerAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Rejuvenation";
	public static final String PERM_NODE = "rejuvenation";

	private double healthPerHeal;
	private int heals;
	private int delayBetweenHeals;

	/**
	 * Rejuvenation Heal-Over-Time Ability
	 *
	 * @param player The Player type user for this ability instance
	 */
	public RejuvenationPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);

		this.healthPerHeal = 2;
		this.heals = 6;
		this.delayBetweenHeals = 15;
	}

	/**
	 * @return Integer The currently set amount of health the player will receive each heal
	 */
	public double getHealthPerHeal() {
		return healthPerHeal;
	}

	/**
	 * @param healthPerHeal The amount of health the player should receive every heal
	 * @return RejuvenationAbility This instance, used for chaining
	 */
	public RejuvenationPlayerAbility setHealthPerHeal(double healthPerHeal) {
		this.healthPerHeal = healthPerHeal;
		return this;
	}

	/**
	 * @return Integer The amount of time the player will be healed with the set healthPerHeal
	 */
	public int getHeals() {
		return heals;
	}

	/**
	 * @param heals The Integer amount of time a player should be healed
	 * @return RejuvenationAbility this instance, used for chaining
	 */
	public RejuvenationPlayerAbility setHeals(int heals) {
		this.heals = heals;
		return this;
	}

	/**
	 * @return Integer the amount of delay in ticks  between the the set amount of heals
	 */
	public int getDelayBetweenHeals() {
		return delayBetweenHeals;
	}

	/**
	 * @param delayBetweenHeals The Integer amount of tick delay between heals
	 * @return RejuvenationAbility This instance, used for chaining
	 */
	public RejuvenationPlayerAbility setDelayBetweenHeals(int delayBetweenHeals) {
		this.delayBetweenHeals = delayBetweenHeals;
		return this;
	}

	public AbilityResult<RejuvenationPlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<RejuvenationPlayerAbility>(this, false);

		Player p = getPlayer();

		new HealRunnable(p, healthPerHeal, heals).runTaskTimer(NoxMMO.getInstance(), 0, delayBetweenHeals);

		return new AbilityResult<RejuvenationPlayerAbility>(this, true);
	}

}
