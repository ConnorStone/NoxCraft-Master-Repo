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

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.CommonUtil;

/**
 * @author NoxPVP
 */
public class HealRunnable extends BukkitRunnable {

	private double health;
	private LivingEntity e;
	private int runsLimit;
	private int runs = 0;

	/**
	 * @param entity       Living entity to heal
	 * @param healthAmount double amount to heal target
	 * @param runsLimit    the amount of times to run, if used as a tasktimer
	 */
	public HealRunnable(LivingEntity entity, double healthAmount, int runsLimit) {
		this.e = entity;
		this.health = healthAmount;
		this.runsLimit = runsLimit;
	}

	public void safeCancel() {
		try {
			cancel();
		} catch (IllegalStateException e) {
		}
	}

	public void run() {
		if (runs++ >= runsLimit) {
			safeCancel();
			return;
		}

		double ha = e.getHealth() + health;
		ha = ha > e.getMaxHealth() ? e.getMaxHealth() : ha;

		if (CommonUtil.callEvent(new EntityRegainHealthEvent(e, ha - e.getHealth(), RegainReason.CUSTOM)).isCancelled())
			return;

		e.setHealth(ha);
	}

}
