package com.noxpvp.mmo.classes.internal;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public abstract class ExperienceHolder implements IExperienceHolder,
		ConfigurationSerializable {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static final ExperienceFormula	DEFAULT_FORMULA			= new ExperienceFormula(
																			2.5,
																			1.0,
																			2000);
	
	// Serializers start
	public static final String				SERIALIZE_CURRENT_EXP	= "current-exp";
	// Serializers end
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private int								currentLevel;
	private double							exp;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public ExperienceHolder() {
		
		exp = 0;
		currentLevel = 0;
		
	}
	
	public ExperienceHolder(Map<String, Object> data) {
		Object getter;
		
		if ((getter = data.get(SERIALIZE_CURRENT_EXP)) != null
				&& getter instanceof Number) {
			exp = (Double) getter;
		} else {
			exp = 0;
		}
		
		currentLevel = 0;
		
		double tempXP = exp;
		while (tempXP >= getExpToLevel() && currentLevel < getMaxLevel()) {
			tempXP -= getExpToLevel();
			currentLevel++;
		}
		
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void addExp(double amount) {
		while (getExp() + amount >= getMaxExp()) {
			amount = -getExpToLevel();
			incrementLevel();
			
			setExp(0);
		}
		
		addExp(amount);
	}
	
	public void addLevels(int amount) {
		
		// Don't add any levels above max
		currentLevel = Math.min(getMaxLevel(), currentLevel + amount);
	}
	
	public double getExp() {
		return exp;
	}
	
	public double getExpToLevel() {
		
		// Never return below 0
		return Math.max(0, getMaxExp() - getExp());
	}
	
	public int getLevel() {
		return currentLevel;
	}
	
	public double getMaxExp() {
		return getFormula().getExperienceNeeded(getLevel());
	}
	
	public void incrementLevel() {
		
		// Only increment up to max level
		if (getLevel() < getMaxLevel()) {
			setLevel(getLevel() + 1);
		}
	}
	
	public void removeExp(int amount) {
		
		// Don't let exp go below 0
		exp = Math.max(0, exp - amount);
	}
	
	public Map<String, Object> serialize() {
		final Map<String, Object> data = new HashMap<String, Object>();
		
		if (exp > 0) {
			data.put(SERIALIZE_CURRENT_EXP, exp);
		}
		
		return data;
	}
	
	public void setExp(int amount) {
		exp = Math.max(getMaxExp(), amount);
	}
	
	public void setLevel(int amount) {
		// Set only if within max level
		if (getLevel() + amount <= getMaxLevel()) {
			currentLevel = getLevel() + amount;
		}
	}
}
