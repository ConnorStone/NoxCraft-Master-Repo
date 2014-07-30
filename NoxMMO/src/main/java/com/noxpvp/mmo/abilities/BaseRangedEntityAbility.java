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

import com.noxpvp.mmo.abilities.internal.RangedEntityAbility;
import org.bukkit.entity.Entity;

public abstract class BaseRangedEntityAbility extends BaseEntityAbility implements RangedEntityAbility {
	private double range;

	public BaseRangedEntityAbility(String name, Entity ref) {
		this(name, ref, 0);
	}

	public BaseRangedEntityAbility(String name, Entity ref, double range) {
		super(name, ref);

		this.range = range;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

}
