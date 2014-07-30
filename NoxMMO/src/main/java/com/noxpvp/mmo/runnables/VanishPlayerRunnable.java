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
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.PlayerUtil;

public class VanishPlayerRunnable extends BukkitRunnable {
	private Player vanisher;
	private int runs;
	private double range;
	private int runsLimit;

	/**
	 * @param vanisher The player to keep vanished
	 * @param range    The distance to look for other players
	 * @param runs     The amount of time to run
	 */
	public VanishPlayerRunnable(Player vanisher, double range, int runs) {
		this.runs = 0;
		this.range = range;
		this.runsLimit = runs;
	}

	public void safeCancel() {
		try {
			cancel();
		} catch (IllegalStateException e) {
		}
	}

	public void run() {
		if (this.runs++ >= runsLimit) {
			for (Player it : vanisher.getServer().getOnlinePlayers())
				it.showPlayer(vanisher);

			safeCancel();
			return;
		}

		for (Entity it : PlayerUtil.getNearbyPlayers(vanisher, range))
			((Player) it).hidePlayer(vanisher);
	}

}
