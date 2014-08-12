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

package com.noxpvp.mmo.abilities.internal;

import org.bukkit.entity.Entity;

import com.noxpvp.core.internal.IHeated;
import com.noxpvp.mmo.abilities.EntityAbilityTier;
import com.noxpvp.mmo.listeners.IMMOHandlerHolder;

public interface EntityAbility<T extends EntityAbilityTier<? extends EntityAbility>>
		extends TieredAbility<T>, IHeated, IMMOHandlerHolder {
	
	/**
	 * Retrieves the entity responsible for this ability.
	 * 
	 * @return Entity object from bukkit.
	 */
	public Entity getEntity();
	
	// public void addEffectedEntity(Entity e);
	//
	// public List<Entity> getEffectedEntities();
	//
	// public void clearEffected();
}
