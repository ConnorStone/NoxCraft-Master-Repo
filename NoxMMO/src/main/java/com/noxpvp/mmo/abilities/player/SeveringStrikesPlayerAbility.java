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
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.abilities.internal.PassiveAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class SeveringStrikesPlayerAbility extends BasePlayerAbility implements PassiveAbility<EntityDamageByEntityEvent>, PVPAbility {

	public static final String ABILITY_NAME = "Severing Strikes";
	public static final String PERM_NODE = "severing-strikes";

	private int bleed;

	/**
	 * @param player The user of the ability instance
	 */
	public SeveringStrikesPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);

	}

	public AbilityResult<SeveringStrikesPlayerAbility> execute() {
		return new AbilityResult<SeveringStrikesPlayerAbility>(this, true);
	}

	public AbilityResult<SeveringStrikesPlayerAbility> execute(EntityDamageByEntityEvent event) {
		if (!mayExecute() || event.getDamager() != getPlayer())
			return new AbilityResult<SeveringStrikesPlayerAbility>(this, false);

		Entity damaged = event.getEntity();
		Player p = getPlayer();

		if (!(damaged instanceof Damageable))
			return new AbilityResult<SeveringStrikesPlayerAbility>(this, false);

		int levels = MMOPlayerManager.getInstance().getPlayer(p).getPrimaryClass().getTotalLevel();
		this.bleed = (20 * levels) / 16;

		new DamageRunnable((Damageable) damaged, p, 1 * (1 + ((bleed / 20) / 6)), (bleed / 20) / 3).runTaskTimer(NoxMMO.getInstance(), 30, 30);

		return new AbilityResult<SeveringStrikesPlayerAbility>(this, true);
	}

}
