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

import com.noxpvp.mmo.abilities.internal.Ability;

public interface IRangedAbilityEvent<T extends Ability> extends IAbilityEvent<T> {

	/**
	 * Gets the {@link com.noxpvp.mmo.abilities.internal.RangedAbility} associated with this event
	 *
	 * @return {@link com.noxpvp.mmo.abilities.internal.RangedAbility} The Ability
	 */
	public T getAbility();

	/**
	 * Retrieves the range for the ability.
	 * @return double value of distance.
	 */
	public double getRange();

	/**
	 * Set the range of the ability.
	 * @param range double value of distance
	 */
	public void setRange(double range);
}
