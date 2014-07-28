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

package com.noxpvp.core.internal;

import com.noxpvp.core.gui.CoolDown;

public interface IHeated {

	/**
	 * Sets the cooldown variable to be used in this ability
	 *
	 * @param coolDown
	 */
	void setCD(CoolDown.Time coolDown);

	/**
	 * Gets the cooldown variable to be used in this ability
	 *
	 * @return Time entry
	 */
	public CoolDown.Time getCD();
}
