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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public abstract class BaseHelix extends BukkitRunnable implements IHelix {

	protected static final Map<Integer, double[]> lookup = new HashMap<Integer, double[]>();

	static {
		generateLookup();
	}
	private Location loc;
	private Vector direction;
	private double widthGain;
	private double forwardGain;
	private int speed;
	private int time;
	private int verticalTicker;
	private int horizontalTicker;
	private boolean render;

	public BaseHelix(Location loc, int time) {
		this.loc = loc;
		this.direction = loc.getDirection();
		this.time = time;

		this.widthGain = 1;
		this.forwardGain = 1;
		this.speed = 2;

		this.render = true;

	}

	protected static void generateLookup() {

		for (int i = 0; i < 360; i++) {
			double[] velocity = new double[2];

			velocity[0] = Math.sin(Math.toRadians(i));
			velocity[1] = Math.cos(Math.toRadians(i));

			lookup.put(i, velocity);
		}
	}

	public int verticalTicker() {
		if (verticalTicker < 90)
			verticalTicker += 1;

		return verticalTicker;
	}

	public int horizontalTicker() {
		return horizontalTicker = (int) ((horizontalTicker + 22.5) % 360);
	}

	public void render(int maxRuns) {
		int i = 0;
		while (i++ < maxRuns && render)
			run();
	}

	public void renderBeforeStart(int maxRuns, int delay) {
		render(maxRuns);
		start(delay);
	}

	public void start(int delay) {
		this.runTaskTimer(getPlugin(), delay, speed);

		Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {

			public void run() {
				try {
					BaseHelix.this.onStop();
					BaseHelix.this.cancel();
					return;
				} catch (IllegalStateException e) {
				}

			}
		}, time);

		onStart();
	}

	public void run() {

		double radius = (BaseHelix.lookup.get(verticalTicker())[0] * widthGain) + Math.abs(direction.getY());
		int horizontal = horizontalTicker();

		double yawX = direction.getX(), yawZ = direction.getZ();

		Vector v = new Vector(
				((radius * BaseHelix.lookup.get(horizontal)[0]) * yawZ),
				(radius * BaseHelix.lookup.get(horizontal)[1]),
				((radius * BaseHelix.lookup.get(horizontal)[0]) * (yawX * -1)));

		this.loc.add(v);
		this.loc.add(direction.clone().multiply(forwardGain));

		onRun();
	}

	public Location getLocation() {
		return this.loc.clone();
	}

	public void setLocation(Location loc) {
		this.loc = loc;
	}

	public Vector getDirection() {
		return this.direction.clone();
	}

	public void setDirection(Vector direction) {
		this.direction = direction;
	}

	public int getSpeed() {
		return this.speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public double getRadiusGain() {
		return widthGain;
	}

	public void setRadiusGain(double gain) {
		this.widthGain = gain;

	}

	public double getForwardGain() {
		return forwardGain;
	}

	public void setForwardGain(double gain) {
		this.forwardGain = gain;

	}

	public boolean getCanRender() {
		return this.render;
	}

	public void setCanRender(boolean can) {
		this.render = can;
	}

}
