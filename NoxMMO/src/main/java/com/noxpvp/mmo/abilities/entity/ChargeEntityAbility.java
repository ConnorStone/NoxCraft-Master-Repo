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

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class ChargeEntityAbility extends BaseEntityAbility {

	public static final String ABILITY_NAME = "Charge";
	public static final String PERM_NODE = "charge";
	private double forwardMultiplier;

	public ChargeEntityAbility(Entity ent, double forwardMultiplier) {
		super(ABILITY_NAME, ent);
		this.forwardMultiplier = forwardMultiplier;
	}

	public ChargeEntityAbility(Entity ent) {
		this(ent, 10);
	}

	@Override
	public String getDescription() {
		return "You charge forward with great speed";
	}

	/**
	 * Gets the current multiplier for the ability's forward velocity
	 *
	 * @return double The forward multiplier
	 */
	public double getForwardMultiplier() {
		return forwardMultiplier;
	}

	/**
	 * Sets the multiplier for this ability's forward velocity
	 *
	 * @param forwardMultiplier The multiplier to set
	 * @return ChargeAbility This instance
	 */
	public ChargeEntityAbility setForwardMultiplier(double forwardMultiplier) {
		this.forwardMultiplier = forwardMultiplier;
		return this;
	}

	public AbilityResult<ChargeEntityAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<ChargeEntityAbility>(this, false);

		Entity e = getEntity();

		Vector newVelocity = e.getLocation().getDirection();

		newVelocity.setY(0).multiply(forwardMultiplier);

		new ParticleRunner(ParticleType.largesmoke, e, true, 0, 6, 10).start(0, 1);

		e.setVelocity(newVelocity);
		return new AbilityResult<ChargeEntityAbility>(this, true);
	}

}
