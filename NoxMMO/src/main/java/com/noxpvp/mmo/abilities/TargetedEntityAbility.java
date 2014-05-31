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

import org.bukkit.entity.LivingEntity;

public interface TargetedEntityAbility extends EntityAbility, TargetedAbility{
	
	/**
	 * Gets the target involved in this targeted ability instance
	 * 
	 * @return LivingEntity The target entity
	 */
	public LivingEntity getTarget();
	
	/**
	 * Sets the target involved in this targeted ability instance
	 * 
	 * @param target The target living entity
	 */
	public void setTarget(LivingEntity target);
	
	/**
	 * Gets the distance from target to the entity involved in this instance
	 * 
	 * @return double The distance. Will return -1 if the target is null
	 */
	public double getDistance();

}
