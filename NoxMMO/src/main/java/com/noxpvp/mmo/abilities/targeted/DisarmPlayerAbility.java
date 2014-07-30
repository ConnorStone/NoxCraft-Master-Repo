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

package com.noxpvp.mmo.abilities.targeted;

import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.noxpvp.mmo.abilities.BaseTargetedAbility.TargetedAbilityResult;

/**
 * @author NoxPVP
 */
public class DisarmPlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility {

	private static final String ABILITY_NAME = "Disarm";
	public static final String PERM_NODE = "disarm";
	
	private List<LivingEntity> disarmed = new ArrayList<LivingEntity>();

	public DisarmPlayerAbility(Player player) {
		this(player, 10);
	}

	public DisarmPlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
	}

	public TargetedAbilityResult<DisarmPlayerAbility> execute() {
		if (!mayExecute())
			return new TargetedAbilityResult<DisarmPlayerAbility>(this, false);

		disarmed.add(getTarget());

		return new TargetedAbilityResult<DisarmPlayerAbility>(this, true);
	}

}
