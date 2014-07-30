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

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

import java.util.HashSet;

/**
 * @author NoxPVP
 */
public class DemoralizingRoarEntityAbility extends BaseEntityAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Demoralizing Roar";
	public static final String PERM_NODE = "demoralizing-roar";

	private HashSet<Creature> creatures = null;
	private int range;

	/**
	 * @param entity Entity type user of this ability (Usually a wolf)
	 */
	public DemoralizingRoarEntityAbility(Entity entity) {
		super(ABILITY_NAME, entity);
		creatures = new HashSet<Creature>();
	}

	public AbilityResult<DemoralizingRoarEntityAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<DemoralizingRoarEntityAbility>(this, false);

		Entity e = getEntity();

		for (Entity it : e.getNearbyEntities(range, range, range)) {
			if (!(it instanceof Creature || creatures.contains(it))) continue;

			for (Entity itTwo : it.getNearbyEntities(range, range, range)) {
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
		
		return new AbilityResult<DemoralizingRoarEntityAbility>(this, !creatures.isEmpty());

	}

	/**
	 * @return Integer Currently set range to look for targets
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @param range Integer range to look that this ability should look for targets
	 * @return DemoralizingRoarAbility This instance, used for chaining
	 */
	public DemoralizingRoarEntityAbility setRange(int range) {
		this.range = range;
		return this;
	}

}
