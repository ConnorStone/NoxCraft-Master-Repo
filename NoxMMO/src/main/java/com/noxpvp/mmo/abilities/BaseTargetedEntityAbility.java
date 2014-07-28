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

package com.noxpvp.mmo.abilities;

import com.noxpvp.mmo.abilities.internal.TargetedEntityAbility;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

public abstract class BaseTargetedEntityAbility extends BaseRangedEntityAbility implements TargetedEntityAbility {
	private Reference<LivingEntity> target_ref;

	public BaseTargetedEntityAbility(String name, Entity entity, double range, LivingEntity target) {
		super(name, entity, range);

		this.target_ref = new SoftReference<LivingEntity>(target);
	}

	/**
	 * creates a new targeted entity ability with 0 range
	 *
	 * @param name
	 * @param entity
	 * @param target
	 */
	public BaseTargetedEntityAbility(String name, Entity entity, LivingEntity target) {
		this(name, entity, 0, target);
	}

	public LivingEntity getTarget() {
		return (target_ref == null) ? null : target_ref.get();
	}

	public void setTarget(LivingEntity target) {
		this.target_ref = new SoftReference<LivingEntity>(target);
	}

	public double getDistance() {
		if (getEntity() != null)
			return getDistance(getEntity().getLocation());

		return -1;
	}

	public double getDistance(Location loc) {
		if (getTarget() != null && loc != null)
			return getTarget().getLocation().distance(loc);

		return -1;
	}

	/**
	 * Returns is the entity or target of this ability is null, thus if the execute method will start
	 *
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return super.mayExecute() && (getTarget() != null && (getDistance() <= getRange()));
	}

}
