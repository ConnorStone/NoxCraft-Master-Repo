package com.noxpvp.mmo.classes.internal;

import java.util.HashMap;
import java.util.Map;

public class ExperienceFormula {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final Map<Integer, Double>	expNeededPerLevel;
	
	private final double				exponent;
	private final double				multiplier;
	private final double				base;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public ExperienceFormula(double exponent, double multiplier, double base) {
		this.exponent = exponent;
		this.multiplier = multiplier;
		this.base = base;
		
		expNeededPerLevel = new HashMap<Integer, Double>();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public double getExperienceNeeded(int level) {
		if (!expNeededPerLevel.containsKey(level)) {
			final double exp = multiplier * (Math.pow(level, exponent) + base);
			expNeededPerLevel.put(level, exp);
		}
		
		return expNeededPerLevel.get(level);
	}
	
}
