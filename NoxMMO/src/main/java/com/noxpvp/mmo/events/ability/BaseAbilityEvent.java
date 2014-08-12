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

import com.noxpvp.mmo.abilities.internal.TieredAbility;
import com.noxpvp.mmo.events.internal.IAbilityEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class BaseAbilityEvent<T extends TieredAbility> extends Event implements IAbilityEvent<T>, Cancellable {
	private final T ability;
	private boolean isCancelled;

	public BaseAbilityEvent(T ability) {
		this.ability = ability;
	}

	public T getAbility() {
		return ability;
	}

	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}

	public boolean isCancelled() {
		return isCancelled;
	}
}
