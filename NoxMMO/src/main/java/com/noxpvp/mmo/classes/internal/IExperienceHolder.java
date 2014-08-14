package com.noxpvp.mmo.classes.internal;

public interface IExperienceHolder {
	
	public void addExp(double amount);
	
	public void addLevels(int amount);
	
	public double getExp();
	
	public double getExpToLevel();
	
	public int getLevel();
	
	public double getMaxExp();
	
	public int getMaxLevel();
	
	public void incrementLevel();
	
	public void removeExp(int amount);
	
	public void setExp(int amount);
	
	public void setLevel(int amount);
	
}
