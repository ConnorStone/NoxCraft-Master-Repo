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

import com.noxpvp.mmo.abilities.internal.RangedPlayerAbility;
import org.bukkit.entity.Player;

import static com.noxpvp.mmo.abilities.BaseRangedAbility.RangedAbilityResult;

public class PostRangedPlayerAbilityEvent<T extends RangedPlayerAbility> extends PostPlayerAbilityEvent<T> {
	private double range;

	public PostRangedPlayerAbilityEvent(T ability, RangedAbilityResult<T> result, Player player) {
		super(ability, result, player);
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getRange() {
		return range;
	}

	@Override
	public T getAbility() {
		return super.getAbility();
	}

	@Override
	public RangedAbilityResult<T> getResult() {
		return (RangedAbilityResult<T>) super.getResult();
	}

}
