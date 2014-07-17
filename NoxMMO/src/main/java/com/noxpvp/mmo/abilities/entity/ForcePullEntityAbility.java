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

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.PVPAbility;

public class ForcePullEntityAbility extends BaseEntityAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Force Pull";
	public static final String PERM_NODE = "force-pull";

	private double range;
	private int maxTargets;

	public ForcePullEntityAbility(Entity ent) {
		super(ABILITY_NAME, ent);

		this.range = 8;
		this.maxTargets = 4;
	}

	public double getRange() {
		return range;
	}

	public ForcePullEntityAbility setRange(double range) {
		this.range = range;
		return this;
	}

	public int getMaxTargets() {
		return maxTargets;
	}

	public ForcePullEntityAbility setMaxTargets(int maxTargets) {
		this.maxTargets = maxTargets;
		return this;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Entity e = getEntity();

		Location eLoc = e.getLocation();
		int i = 0;
		for (Entity it : e.getNearbyEntities(range, range, range)) {
			if (!(it instanceof LivingEntity))
				continue;

			it.setVelocity(eLoc.subtract(it.getLocation()).toVector());

			if (i++ >= maxTargets)
				break;
		}
		
		return new AbilityResult(this, i > 0);
	}

}
