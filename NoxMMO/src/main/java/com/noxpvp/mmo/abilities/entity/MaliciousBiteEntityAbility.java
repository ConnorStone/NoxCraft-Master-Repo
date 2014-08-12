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

package com.noxpvp.mmo.abilities.entity;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.abilities.internal.PassiveAbility;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class MaliciousBiteEntityAbility extends BaseEntityAbility implements
		PassiveAbility<EntityDamageByEntityEvent>, PVPAbility {
	
	public static final String	ABILITY_NAME	= "Malicious bite";
	public static final String	PERM_NODE		= "malicious-bite";
	
	public MaliciousBiteEntityAbility(Entity entity) {
		super(ABILITY_NAME, entity);
	}
	
	@Override
	public AbilityResult execute() {
		return new AbilityResult<MaliciousBiteEntityAbility>(this, true);
	}
	
	public AbilityResult<MaliciousBiteEntityAbility> execute(
			EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return new AbilityResult<MaliciousBiteEntityAbility>(this, false);
		
		if (!(getEntity() instanceof Tameable))
			return new AbilityResult<MaliciousBiteEntityAbility>(this, false);
		
		final AnimalTamer a = ((Tameable) getEntity()).getOwner();
		
		if (a == null || !(a instanceof Player))
			return new AbilityResult<MaliciousBiteEntityAbility>(this, false);
		
		final Player o = (Player) a;
		
		final PlayerClass pClass = MMOPlayerManager.getInstance().getPlayer(o)
				.getPrimaryClass();
		
		return new AbilityResult<MaliciousBiteEntityAbility>(this, RandomUtils
				.nextFloat() < pClass.getLevel() / 1000);
	}
	
}
