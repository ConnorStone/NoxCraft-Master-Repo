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

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 */
public class DespawnRunnable extends BukkitRunnable {

	private List<Entity> entitys;

	/**
	 * @param entity The entity to mark to be removed
	 */
	public DespawnRunnable(Entity entity) {
		this.entitys = new ArrayList<Entity>();
		this.entitys.add(entity);
	}

	/**
	 * @param entitys A list of entities to mark to be removed.
	 */
	public DespawnRunnable(List<Entity> entitys) {
		this.entitys = entitys;
	}

	public void run() {
		for (Entity e : this.entitys) {
			if (e == null) continue;

			e.remove();
		}
	}

}
