package com.noxpvp.mmo.vortex;

import java.util.ArrayDeque;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.core.NoxCore;
import com.noxpvp.mmo.NoxMMO;

public abstract class BaseVortex extends BukkitRunnable implements IVortex {

	protected final static HashMap<Integer, Double[]> lookup = new HashMap<Integer, Double[]>();
	private ArrayDeque<BaseVortexEntity> entities;
	
	private Player user;
	private Location currentLocation;
	private int maxEntityAmount;
	private int time;
	
	private double width, height;
	
	private int taskId;
	private int speed;
	
	private static void generateLookup() {
		if(lookup.size() != 0) {
			return;
		}
		
		for(int i = 0 ; i < 360 ; i++) {
			Double[] data = new Double[2];
			data[0] = Math.sin(Math.toRadians(i));
			data[1] = Math.cos(Math.toRadians(i));
			lookup.put(i, data);
		}
	}
	
	public boolean checkValid() {
		if (user == null || !user.isOnline() || !user.isValid())
			return false;
		
		return true;
	}
	
	public Player getUser() {
		return this.user;
	}
	
	public void addEntity(BaseVortexEntity entity) {
		if (entity == null || entities.contains(entity))
			return;
		
		checkListSize();
		entities.add(entity);
		
	}
	
	public ArrayDeque<BaseVortexEntity> getEntities() {
		return this.entities;
	}
	
	public void clearEntities() {
		for (BaseVortexEntity e : entities) {
			e.remove();
		}
		entities.clear();
	}
	
	public void setLocation(Location loc) {
		this.currentLocation = loc;
	}
	
	public Location getLocation() {
		return this.currentLocation.clone();
	}
	
	public void setSpeed(int speedInTicks) {
		this.speed = speedInTicks;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setMaxSize(int size) {
		this.maxEntityAmount = size;
	}
	
	public int getMaxSize() {
		return this.maxEntityAmount;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public void setHeightGain(double height) {
		this.height = height;
	}
	
	public double getHeightGain() {
		return this.height;
	}
	
	public BaseVortex(Player user, Location loc, int time) {
		entities = new ArrayDeque<BaseVortexEntity>();
		
		this.user = user;
		this.currentLocation = loc;
		this.maxEntityAmount = 50;
		this.time = time;
		this.speed = 5;
		
		generateLookup();
	}
	
	public void start() {
		this.taskId = runTaskTimer(NoxMMO.getInstance(), 0, speed).getTaskId();
		
		Bukkit.getScheduler().runTaskLater(NoxCore.getInstance(), new Runnable() {
			
			public void run() {
				BaseVortex.this.stop();
				
			}
		}, time);
	}
	
	public void stop() {
		Bukkit.getScheduler().cancelTask(taskId);
		clearEntities();
	}

	public void run() {
		if (!checkValid())
			stop();
		
		onRun();
	}
	
	// Removes the oldest block if the list goes over the limit.
	private void checkListSize() {
		while(entities.size() >= maxEntityAmount) {
			BaseVortexEntity ve = entities.getFirst();
			entities.remove(ve);
			ve.getEntity().removeMetadata(BaseVortexEntity.uniqueMetaKey, NoxMMO.getInstance());
			ve.remove();
		}
	}
	
}
