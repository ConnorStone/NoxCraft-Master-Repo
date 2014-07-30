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

public abstract class BaseCorkScrew extends BaseHelix {

	private int horizontalTicker;
	private double heightGain;
	private double heightLimit;
	private double origHeight;

	public BaseCorkScrew(Location loc, int time, double heightLimit) {
		super(loc, time);

		this.heightLimit = heightLimit;
		this.heightGain = 0.3;
		this.origHeight = loc.getY();
	}

	@Override
	public void run() {
		if ((getLocation().getY() - origHeight) > heightLimit) {
			setCanRender(false);
			return;
		}

		double radius = BaseHelix.lookup.get(verticalTicker())[0] * getRadiusGain();
		int horizontal = horizontalTicker();

		Vector v = new Vector(
				(radius * BaseHelix.lookup.get(horizontal)[0]),
				heightGain,
				(radius * BaseHelix.lookup.get(horizontal)[1]));

		setLocation(getLocation().add(v));

		onRun();
	}

	public double getHeightGain() {
		return this.heightGain;
	}

	public void setHeightGain(double gain) {
		this.heightGain = gain;
	}

	public int verticalTicker() {
		return 90;
	}

	public int horizontalTicker() {
		return horizontalTicker = (int) ((horizontalTicker + 22.5) % 360);
	}


}
