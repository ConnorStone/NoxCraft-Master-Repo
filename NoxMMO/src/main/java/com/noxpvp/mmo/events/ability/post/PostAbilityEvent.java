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

import org.bukkit.event.HandlerList;

import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.internal.TieredAbility;

public class PostAbilityEvent<T extends TieredAbility> extends PostBaseAbilityEvent<T> {

	private static HandlerList handlers = new HandlerList();

	private AbilityResult<T> result;

	public PostAbilityEvent(T ability, AbilityResult<T> result) {
		super(ability);
		this.result = result;
	}

	public T getAbility() {
		return super.getAbility();
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public AbilityResult<T> getResult() {
		return result;
	}
}
