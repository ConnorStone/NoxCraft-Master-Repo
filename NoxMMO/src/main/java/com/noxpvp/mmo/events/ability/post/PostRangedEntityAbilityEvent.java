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

package com.noxpvp.mmo.events.ability.post;

import com.noxpvp.mmo.abilities.internal.RangedEntityAbility;
import com.noxpvp.mmo.events.internal.IRangedEntityAbilityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

import static com.noxpvp.mmo.abilities.BaseRangedAbility.RangedAbilityResult;

public class PostRangedEntityAbilityEvent<T extends RangedEntityAbility> extends PostEntityAbilityEvent<T> implements IRangedEntityAbilityEvent<T> {

	private static HandlerList handlers = new HandlerList();
	private double range;

	public PostRangedEntityAbilityEvent(T ability, RangedAbilityResult<T> result,  Entity entity) {
		super(ability, result, entity);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public RangedAbilityResult<T> getResult() {
		return (RangedAbilityResult<T>) super.getResult();
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	@Override
	public T getAbility() {
		return super.getAbility();
	}
}
