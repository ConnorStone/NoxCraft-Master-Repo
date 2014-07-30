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

import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.events.internal.IAbilityEvent;
import org.bukkit.event.Event;

public abstract class PostBaseAbilityEvent<T extends Ability> extends Event implements IAbilityEvent<T>{
	private final T ability;

	public PostBaseAbilityEvent(T ability) {
		this.ability = ability;
	}

	@Override
	public T getAbility() {
		return ability;
	}

	public abstract AbilityResult<T> getResult();
}
