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

package com.noxpvp.mmo.abilities;

import org.apache.commons.lang.Validate;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.mmo.abilities.internal.TieredAbility;

public class AbilityResult<T extends TieredAbility<?>> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final T		ability;
	final boolean		successful;
	private String[]	messages;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public AbilityResult(T ability, boolean success) {
		this(ability, success, null);
	}
	
	public AbilityResult(T ability, boolean success, String... messages) {
		Validate.notNull(ability, "Ability cannot be null!");
		
		this.ability = ability;
		this.successful = success;
		this.messages = LogicUtil.fixNull(messages, new String[0]);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public T getAbility() {
		return ability;
	}
	
	public String[] getMessages() {
		return messages;
	}
	
	public boolean hasMessages() {
		return getMessages().length > 0;
	}
	
	public boolean isSuccessful() {
		return successful;
	}
}
