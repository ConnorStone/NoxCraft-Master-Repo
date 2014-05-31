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

import java.util.HashSet;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

/**
 * @author NoxPVP
 *
 */
public class DemoralizingRoarAbility extends BaseEntityAbility{
	
	public static final String ABILITY_NAME = "Demoralizing Roar";
	public static final String PERM_NODE = "demoralizing-roar";
	
	private HashSet<Creature> creatures = null;
	private int range;
	
	/**
	 * 
	 * 
	 * @param entity Entity type user of this ability (Usually a wolf)
	 */
	public DemoralizingRoarAbility(Entity entity){
		super(ABILITY_NAME, entity);
		creatures = new HashSet<Creature>();
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Entity e = getEntity();
		
		for (Entity it : e.getNearbyEntities(range, range, range)){
			if (!(it instanceof Creature || creatures.contains(it))) continue;
			
			for (Entity itTwo : it.getNearbyEntities(range, range, range)){
				if (!(itTwo instanceof Creature || creatures.contains(it))) continue;
				
				((Creature) it).setTarget((Creature) itTwo);
				((Creature) itTwo).setTarget((Creature) it);
				
				new ParticleRunner(ParticleType.angryVillager, it, false, 0, 1, 1).start(0);
				new ParticleRunner(ParticleType.angryVillager, itTwo, false, 0, 1, 1).start(0);
				
				creatures.add((Creature) it);
				creatures.add((Creature) itTwo);
				break;
			}
		}
		if (creatures.isEmpty())
			return false;
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @return Integer Currently set range to look for targets
	 */
	public int getRange() {return range;}

	/**
	 * 
	 * 
	 * @param range Integer range to look that this ability should look for targets
	 * @return DemoralizingRoarAbility This instance, used for chaining
	 */
	public DemoralizingRoarAbility setRange(int range) {this.range = range; return this;}
	
}
