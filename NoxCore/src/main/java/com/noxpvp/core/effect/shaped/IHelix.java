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
import org.bukkit.util.Vector;

import com.noxpvp.core.NoxPlugin;

public interface IHelix {

	public NoxPlugin getPlugin();

	public void render(int maxRuns);

	public void start(int delay);

	public void onStart();

	public void onStop();

	public void run();

	public void onRun();

	public Location getLocation();

	public void setLocation(Location loc);

	public Vector getDirection();

	public void setDirection(Vector direction);

	public double getRadiusGain();

	public void setRadiusGain(double gain);

	public double getForwardGain();

	public void setForwardGain(double gain);

	public boolean getCanRender();

	public void setCanRender(boolean can);
}
