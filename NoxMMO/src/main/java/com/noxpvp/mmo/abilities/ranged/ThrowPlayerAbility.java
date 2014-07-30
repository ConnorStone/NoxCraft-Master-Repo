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

package com.noxpvp.mmo.abilities.ranged;

import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.runnables.ExpandingDamageRunnable;
import com.noxpvp.mmo.runnables.ShockWaveAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import static com.noxpvp.mmo.abilities.BaseRangedAbility.RangedAbilityResult;

public class ThrowPlayerAbility extends BaseRangedPlayerAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Grapple Throw";
	public static final String PERM_NODE = "grapple-throw";
	
	private int maxTargets;
	private int pushDelay;
	
	/**
	 * @param player The Player type user object for this ability instance
	 */
	public ThrowPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);

		setRange(8);
		setCD(new CoolDown.Time().seconds(15));
		this.maxTargets = 10;
		this.pushDelay = 20;
	}

	@Override
	public String getDescription() {
		return "You grab all nearby enemys within " + String.format("%.2f", getRange()) + " blocks and throw them high into the air";
	}

	/**
	 * @return Integer The max amount of targets this ability can find
	 */
	public int getMaxTargets() {
		return maxTargets;
	}

	/**
	 * @param maxTargets Integer amount of targets this ability is allowed to target at max
	 * @return GrappleThrowAbility This instance, used for chaining
	 */
	public ThrowPlayerAbility setMaxTargets(int maxTargets) {
		this.maxTargets = maxTargets;
		return this;
	}

	/**
	 * @return Integer The currently set tick delay before the targets a thrown/shock wave runs
	 */
	public int getPushDelay() {
		return pushDelay;
	}

	/**
	 * @param pushDelay Integer amount of ticks to wait before throwing the target(s) and executing the shockwave
	 * @return GrappleThrowAbility This instance, used for chaining
	 */
	public ThrowPlayerAbility setPushDelay(int pushDelay) {
		this.pushDelay = pushDelay;
		return this;
	}

	public RangedAbilityResult<ThrowPlayerAbility> execute() {
		if (!mayExecute())
			return new RangedAbilityResult<ThrowPlayerAbility>(this, false);

		Player p = getPlayer();
		final Location pLoc = p.getLocation();

		int i = 0;
		double range = getRange();
		for (Entity it : p.getNearbyEntities(range, range, range)) {
			if (i >= maxTargets) break;

			if (!(it instanceof LivingEntity) || it == p)
				continue;

			if (!LineOfSightUtil.hasLineOfSight(p, it.getLocation(), Material.AIR))
				continue;

			i++;
			final Damageable e = (Damageable) it;
			final Location itLoc = it.getLocation();

//			addEffectedEntity(e);
			e.setVelocity((pLoc.toVector().subtract(itLoc.toVector()).multiply(0.4)));

			Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
				public void run() {
					Location pLoc = getPlayer().getLocation();
					e.teleport(pLoc);
					e.setVelocity(pLoc.getDirection().multiply(3).setY(1));
				}
			}, pushDelay);
		}

		if (i > 0) {
			new ExpandingDamageRunnable(p, pLoc, 4, (int) range, 2).start(pushDelay);
			new ShockWaveAnimation(pLoc, 2, (int) range, true).start(pushDelay);
			return new RangedAbilityResult<ThrowPlayerAbility>(this, true);
			
		} else return new RangedAbilityResult<ThrowPlayerAbility>(this, false);
	}

}
