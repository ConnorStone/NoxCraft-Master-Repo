package com.noxpvp.mmo.abilities;

import com.noxpvp.mmo.abilities.internal.EntityAbility;

public abstract class EntityAbilityTier<T extends EntityAbility<? extends EntityAbilityTier>>
		extends BaseAbilityTier<T> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final BaseEntityAbility	ability;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public EntityAbilityTier(BaseEntityAbility ab, int level) {
		super(level);
		
		ability = ab;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public BaseEntityAbility getAbility() {
		return ability;
	}
	
}
