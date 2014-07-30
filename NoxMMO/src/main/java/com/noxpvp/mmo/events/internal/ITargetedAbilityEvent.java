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

package com.noxpvp.mmo.events.internal;

import com.noxpvp.mmo.abilities.internal.TargetedAbility;
import org.bukkit.entity.LivingEntity;

public interface ITargetedAbilityEvent<T extends TargetedAbility> {
	/**
	 * Gets the {@link com.noxpvp.mmo.abilities.internal.TargetedAbility} associated with this event
	 *
	 * @return {@link com.noxpvp.mmo.abilities.internal.TargetedAbility} The Ability
	 */
	public T getAbility();

	/**
	 * Gets the target {@link org.bukkit.entity.LivingEntity} involved in this event
	 *
	 * @return {@link org.bukkit.entity.LivingEntity} The target
	 */
	public LivingEntity getTarget();

	/**
	 * Gets the distance from target to the entity involved in this instance
	 *
	 * @return double The distance. Will return -1 if the target is null
	 */
	public double getDistance();

}
