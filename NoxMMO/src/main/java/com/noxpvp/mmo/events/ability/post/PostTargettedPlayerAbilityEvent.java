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

import com.noxpvp.mmo.abilities.internal.TargetedPlayerAbility;
import com.noxpvp.mmo.events.internal.ITargetedPlayerAbilityEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import static com.noxpvp.mmo.abilities.BaseTargetedAbility.TargetedAbilityResult;

public class PostTargettedPlayerAbilityEvent<T extends TargetedPlayerAbility> extends PostRangedPlayerAbilityEvent<T> implements ITargetedPlayerAbilityEvent<T> {

	public PostTargettedPlayerAbilityEvent(T ability, TargetedAbilityResult<T> result,  Player player) {
		super(ability, result, player);
	}

	public LivingEntity getTarget() {
		return getAbility().getTarget();
	}

	public double getDistance() {
		return getTarget().getLocation().distance(getPlayer().getLocation());
	}

	@Override
	public T getAbility() {
		return super.getAbility();
	}

	@Override
	public TargetedAbilityResult<T> getResult() {
		return (TargetedAbilityResult<T>) super.getResult();
	}
}
