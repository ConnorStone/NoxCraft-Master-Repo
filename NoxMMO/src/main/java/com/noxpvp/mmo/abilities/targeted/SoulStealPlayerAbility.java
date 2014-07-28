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
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.noxpvp.mmo.abilities.BaseTargetedAbility.TargetedAbilityResult;

/**
 * @author NoxPVP
 */
public class SoulStealPlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "soulsteal";
	public static final String ABILITY_NAME = "SoulSteal";
	private int duration;

	/**
	 * @param player The Player type user for this ability instance
	 */
	public SoulStealPlayerAbility(Player player) {
		super(ABILITY_NAME, player, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
	}

	/**
	 * @return Integer The currently set duration for the blindness effect
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration The duration is ticks for the blindness effect to last
	 * @return SoulStealAbility This instance, used for chaining
	 */
	public SoulStealPlayerAbility setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * @return Boolean If the ability has successfully executed
	 */
	public TargetedAbilityResult<SoulStealPlayerAbility> execute() {
		if (!mayExecute())
			return new TargetedAbilityResult<SoulStealPlayerAbility>(this, false);

		getTarget().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));

		return new TargetedAbilityResult<SoulStealPlayerAbility>(this, true);
	}

}
