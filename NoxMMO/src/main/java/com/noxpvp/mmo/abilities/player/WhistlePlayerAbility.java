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

package com.noxpvp.mmo.abilities.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class WhistlePlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Whistle";
	public static final String PERM_NODE = "whistle";

	private int range;

	public WhistlePlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);
		this.range = 15;
	}

	/**
	 * @return Integer The currently set range for this ability instance (Default is 15)
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @param range The range to check for nearby targets  (Default is 15)
	 * @return WhistleAbility This instance, used for chaining
	 */
	public WhistlePlayerAbility setRange(int range) {
		this.range = range;
		return this;
	}

	/**
	 * @return boolean If the ability has executed successfully
	 */
	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player p = getPlayer();

		for (Entity it : p.getNearbyEntities(range, range, range)) {
			if (!(it instanceof Wolf)) continue;
			if (((Wolf) it).getOwner() != p) continue;

			Wolf n = (Wolf) it;

			if (n.isSitting()) n.setSitting(false);
			else if (!n.isSitting()) n.setSitting(true);
		}

		return new AbilityResult(this, false);
	}

}
