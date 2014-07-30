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

package com.noxpvp.core.effect.shaped;

import org.bukkit.Location;
import org.bukkit.Sound;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;

public class LevelUpGlimmer extends BaseCorkScrew {

	public LevelUpGlimmer(Location loc, int time, double heightLimit) {
		super(loc.add(-1, 0, 0), time, heightLimit);

		setSpeed(1);
		setRadiusGain(0.5);
		setHeightGain(0.05);
	}

	public NoxPlugin getPlugin() {
		return NoxCore.getInstance();
	}

	public void onStart() {
		getLocation().getWorld().playSound(getLocation(), Sound.LEVEL_UP, 3, 0);
	}

	public void onRun() {
		new ParticleRunner(ParticleType.happyVillager, getLocation(), false, 0, 1, 1).start(0);
		new ParticleRunner(ParticleType.fireworksSpark, getLocation(), false, 0, 10, 1).start(0);
		new ParticleRunner(ParticleType.cloud, getLocation(), true, 0, 10, 1).start(0);

	}

	public void onStop() {
		return;
	}

}
