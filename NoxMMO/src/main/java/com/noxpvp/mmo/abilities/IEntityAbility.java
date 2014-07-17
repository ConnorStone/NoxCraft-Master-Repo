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

import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.internal.IHeated;
import org.bukkit.entity.Entity;

import java.util.List;

public interface IEntityAbility extends Ability, IHeated {
	
	/**
	 * Retrieves the entity responsible for this ability.
	 *
	 * @return Entity object from bukkit.
	 */
	public Entity getEntity();

	/**
	 * Gets the damage variable stored in this ability, 0 if it has not been set
	 *
	 * @return {@link Double} damage amount
	 */
	public double getDamage();

	/**
	 * Sets a damage variable to be used in this ability
	 *
	 * @param damage
	 */
	public void setDamage(double damage);
	
	public void addEffectedEntity(Entity e);
	
	public List<Entity> getEffectedEntities();
	
	public void clearEffected();
	
	/**
	 * Gets the cooldown seconds variable to be used in this ability
	 * 
	 * @return seconds
	 */
	public CoolDown.Time getCD();
	
	/**
	 * Sets the cooldown seconds variable to be used in this ability
	 * 
	 * @param cd
	 */
	public void setCD(CoolDown.Time cd);
	
}
