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

package com.noxpvp.mmo.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.noxpvp.mmo.NoxMMO;

public class ExpandingDamageRunnable extends BukkitRunnable {
	private final LivingEntity a;
	private final Location loc;
	private final double d;
	private final int range;
	private long speed;

	private List<Damageable> damaged;
	private int runs;

	public ExpandingDamageRunnable(LivingEntity attacker, Location loc, double damage, int range, int speed) {
		this.a = attacker;
		this.loc = loc;
		this.d = damage;
		this.range = range;

		this.damaged = new ArrayList<Damageable>();
		this.runs = 0;
	}

	public void safeCancel() {
		try {
			cancel();
		} catch (IllegalStateException e) {
		}
	}

	public void start(int delay) {
		runTaskTimer(NoxMMO.getInstance(), delay, speed);
	}

	public void run() {
		if (runs++ > range) {
			safeCancel();
			return;
		}

		for (Entity e : WorldUtil.getNearbyEntities(loc, runs, runs, runs)) {
			if (e instanceof Damageable && !damaged.contains((Damageable) e)) {
				if (e == a) continue;

				Damageable it = (Damageable) e;

				it.damage(d, a);
				damaged.add(it);
			}

		}
	}
}
