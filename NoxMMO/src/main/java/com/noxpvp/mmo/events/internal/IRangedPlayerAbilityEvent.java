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

import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.abilities.internal.RangedPlayerAbility;

public interface IRangedPlayerAbilityEvent<T extends RangedPlayerAbility> extends IPlayerAbilityEvent<T>, IRangedAbilityEvent<T> {

	/**
	 * Gets the {@link com.noxpvp.mmo.abilities.internal.RangedPlayerAbility} associated with this event
	 *
	 * @return {@link BaseRangedPlayerAbility} The ability
	 */
	public T getAbility();
}
