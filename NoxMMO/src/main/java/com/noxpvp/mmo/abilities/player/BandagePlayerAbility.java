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
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class BandagePlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Bandage";
	public static final String PERM_NODE = "bandage";
	private int delay;

	public BandagePlayerAbility(OfflinePlayer p) {
		super(ABILITY_NAME, p);

		this.delay = 10 * 20;
	}

	public int getDelay() {
		return delay;
	}

	public BandagePlayerAbility setDelay(int delay) {
		this.delay = delay;
		return this;
	}

	public AbilityResult<BandagePlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<BandagePlayerAbility>(this, false);

		Player p = getPlayer();

		p.setHealth(p.getMaxHealth());

		DamageRunnable wearOff = new DamageRunnable(p, p, (p.getMaxHealth() / 10), 10);
		wearOff.runTaskTimer(NoxMMO.getInstance(), delay, 15);

		return new AbilityResult<BandagePlayerAbility>(this, true);
	}

}
