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

import com.noxpvp.mmo.abilities.internal.RangedAbility;
import com.noxpvp.mmo.abilities.player.TrackingPlayerAbility;

public abstract class BaseRangedAbility extends BaseAbility implements RangedAbility {

	private double range;

	public BaseRangedAbility(String name) {
		this(name, 0);
	}

	public BaseRangedAbility(String name, double range) {
		super(name);

		this.range = range;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public static class RangedAbilityResult<T extends RangedAbility> extends AbilityResult<T> {

		private final double range;

		public RangedAbilityResult(T ability, double range, boolean success) {
			this(ability, range, success, null);
		}

		public RangedAbilityResult(T ability, double range,  boolean success, String... messages) {
			super(ability, success, messages);
			this.range = range;
		}

		public RangedAbilityResult(T ability, boolean success) {
			this(ability, ability.getRange(), success);
		}

		public RangedAbilityResult(T ability, boolean success, String... messages) {
			this(ability, ability.getRange(), success, messages);
		}

		public double getRange() {
			return range;
		}
	}

}
