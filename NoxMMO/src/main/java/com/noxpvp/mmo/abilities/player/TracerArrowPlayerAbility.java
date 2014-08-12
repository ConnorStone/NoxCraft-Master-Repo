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

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static com.noxpvp.mmo.abilities.BaseTargetedAbility.TargetedAbilityResult;

public class TracerArrowPlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "tracer-arrow";
	private static final String ABILITY_NAME = "Tracer Arrow";
	static Map<String, TracerArrowPlayerAbility> abilityCue = new HashMap<String, TracerArrowPlayerAbility>();

	public TracerArrowPlayerAbility(Player player) {
		super(ABILITY_NAME, player, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
	}

	/**
	 * Runs the event-side execution of this ability
	 *
	 * @param player Player, normally arrow shooter from a projectile hit event
	 * @return boolean If the execution ran successfully
	 */
	public static boolean eventExecute(Player player, final Arrow arrow) {
		MMOPlayer mmoP = MMOPlayerManager.getInstance().getPlayer(player);
		String name = player.getName();

		if (abilityCue.containsKey(name))
			return false;

		final LivingEntity target = mmoP.getTarget();

		Bukkit.getScheduler().runTaskTimer(NoxMMO.getInstance(), new BukkitRunnable() {

			private Vector arrowLoc = arrow.getLocation().toVector();
			private Vector los;

			public void safeCancel() {
				try {
					cancel();
				} catch (IllegalStateException e) {
				}
			}

			public void run() {
				if (arrow == null || !arrow.isValid() || target == null) {
					safeCancel();
					return;
				}

				los = target.getLocation().toVector().subtract(arrowLoc);
				arrow.setVelocity(los);
			}
		}, 0, 1);
		return true;
	}

	/**
	 * @return boolean - If the ability executed successfully
	 */
	public TargetedAbilityResult<TracerArrowPlayerAbility> execute() {
		if (!mayExecute())
			return new TargetedAbilityResult<TracerArrowPlayerAbility>(this, false);

		final String pName = getPlayer().getName();

		TracerArrowPlayerAbility.abilityCue.put(pName, this);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {

			public void run() {

				if (TracerArrowPlayerAbility.abilityCue.containsKey(pName))
					TracerArrowPlayerAbility.abilityCue.remove(pName);

			}
		}, 120);

		return new TargetedAbilityResult<TracerArrowPlayerAbility>(this, true);
	}

}
