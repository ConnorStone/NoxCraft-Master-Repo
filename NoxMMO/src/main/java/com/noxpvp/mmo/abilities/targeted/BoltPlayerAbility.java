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

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.internal.DamagingAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;

import static com.noxpvp.mmo.abilities.BaseTargetedAbility.TargetedAbilityResult;

public class BoltPlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility, DamagingAbility {

	public static final String PERM_NODE = "bolt";
	private static final String ABILITY_NAME = "Bolt";
	private double damage;

	/**
	 * Constructs a new BoltAbility instance with the specified player as the ability user
	 *
	 * @param player - The player to use as the abilities user
	 */
	public BoltPlayerAbility(Player player) {
		this(player, 10);
	}

	/**
	 * Constructs a new BoltAbility instance with the specified player as the ability user and specified range
	 *
	 * @param player - The player to use as the abilities user
	 * @param range  - The max distance away from the user that a target can be
	 */
	public BoltPlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());

		setDamage(8);
	}

	public TargetedAbilityResult<BoltPlayerAbility> execute() {
		if (!mayExecute())
			return new TargetedAbilityResult<BoltPlayerAbility>(this, false);

		Player p = getPlayer();
		LivingEntity t = getTarget();

		t.getWorld().strikeLightningEffect(t.getLocation());
		t.damage(getDamage(), p);

		return new TargetedAbilityResult<BoltPlayerAbility>(this, true);
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getDamage() {
		return damage;
	}
}
