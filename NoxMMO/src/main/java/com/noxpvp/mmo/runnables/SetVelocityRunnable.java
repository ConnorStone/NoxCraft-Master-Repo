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

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * @author NoxPVP
 */
public class SetVelocityRunnable extends BukkitRunnable {

	private Entity e;
	private Vector velo;

	/**
	 * @param entity  Entity to give the new velocity
	 * @param newVelo Velocity to set
	 */
	public SetVelocityRunnable(Entity entity, Vector newVelo) {
		this.e = entity;
		this.velo = newVelo;
	}

	public void run() {
		e.setVelocity(velo);
	}
}
