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

package com.noxpvp.mmo.events.ability;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.events.internal.IEntityAbilityEvent;

public class EntityAbilityEvent<T extends BaseEntityAbility> extends
		BaseAbilityEvent<T> implements IEntityAbilityEvent<T> {
	
	private static HandlerList	handlers	= new HandlerList();
	private final Entity		entity;
	
	public EntityAbilityEvent(T ability, Entity entity) {
		super(ability);
		this.entity = entity;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
