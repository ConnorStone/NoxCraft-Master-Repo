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

package com.noxpvp.core.effect.vortex;

import java.util.ArrayDeque;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxPlugin;

public interface IVortex {

	public void start();

	public void stop();
	
	public void onStop();

	public void onRun();

	public Player getUser();

	public Location getLocation();

	public void setLocation(Location loc);

	public int getSpeed();

	public void setSpeed(int speedInTicks);

	public int getMaxSize();

	public void setMaxSize(int size);

	public double getWidth();

	public void setWidth(double width);

	public double getHeightGain();

	public void setHeightGain(double height);

	public void addEntity(BaseVortexEntity entity);

	public ArrayDeque<? extends BaseVortexEntity> getEntities();

	public void clearEntities();

	public NoxPlugin getPlugin();

}
