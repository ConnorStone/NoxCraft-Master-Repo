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
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import static com.noxpvp.mmo.abilities.BaseTargetedAbility.TargetedAbilityResult;

public class SoothePlayerAbility extends BaseTargetedPlayerAbility {

	public static final String ABILITY_NAME = "Soothe";
	public static final String PERM_NODE = "soothe";

	private double healAmount;

	/**
	 * Constructs a new Soothe Ability with the provided player as the user
	 *
	 * @param player The ability's user
	 */
	public SoothePlayerAbility(Player player) {
		super(ABILITY_NAME, player, MMOPlayerManager.getInstance().getPlayer(player).getTarget());

		this.healAmount = 8;
	}

	/**
	 * Gets the amount of health that will be given to the target
	 *
	 * @return Double The amount to heal the target
	 */
	public double getHealAmount() {
		return healAmount;
	}

	/**
	 * Sets the amount of health to give to the target
	 *
	 * @param healAmount The amount to heal the target
	 * @return SootheAbility This instance
	 */
	public SoothePlayerAbility setHealAmount(double healAmount) {
		this.healAmount = healAmount;
		return this;
	}

	public TargetedAbilityResult<SoothePlayerAbility> execute() {
		if (!mayExecute())
			return new TargetedAbilityResult<SoothePlayerAbility>(this, false);

		LivingEntity t = getTarget();
		double ha = t.getHealth() + getHealAmount();

		t.setHealth(ha > t.getMaxHealth() ? t.getMaxHealth() : ha);

		return new TargetedAbilityResult<SoothePlayerAbility>(this, true);
	}

}
