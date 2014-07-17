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

package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.*;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class CallOfTheWildPlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Call Of the Wild";
	public static final String PERM_NODE = "wolf-call";

	private double range;
	private boolean cancelIfNearby;
	private EntityType spawnType;

	public CallOfTheWildPlayerAbility(Player player) {
		super(ABILITY_NAME, player);

		this.cancelIfNearby = true;
		this.range = 50;
		this.spawnType = EntityType.WOLF;
	}

	/**
	 * @return double The currently set range to search for nearby entitys
	 */
	public double getRange() {
		return range;
	}

	/**
	 * @param range the distance to search for nearby entitys
	 * @return CallOfTheWildAbility This instance, used for chaining
	 */
	public CallOfTheWildPlayerAbility setRange(double range) {
		this.range = range;
		return this;
	}

	/**
	 * @return boolean If the ability is set to check for nearby entitys before executing
	 */
	public boolean isCancelIfNearby() {
		return cancelIfNearby;
	}

	/**
	 * @param cancelIfNearby If the ability should check for nearby entitys before executing
	 * @return CallOfTheWildAbility This instance, used for chaining
	 */
	public CallOfTheWildPlayerAbility setCancelIfNearby(boolean cancelIfNearby) {
		this.cancelIfNearby = cancelIfNearby;
		return this;
	}

	/**
	 * @return EntityType The type of entity set to spawn
	 */
	public EntityType getSpawnType() {
		return spawnType;
	}

	/**
	 * @param spawnType The type of entity to spawn
	 * @return CallOfTheWildAbility This instance, used for chaining
	 */
	public CallOfTheWildPlayerAbility setSpawnType(EntityType spawnType) {
		this.spawnType = spawnType;
		return this;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player p = getPlayer();
		boolean canSpawn = true;

		if (cancelIfNearby) {
			for (Entity it : p.getNearbyEntities(range, range, range)) {
				if (!(it instanceof Wolf)) continue;

				canSpawn = false;
				break;
			}
		}
		
		if (!canSpawn)
			return new AbilityResult(this, false, "&cThere is already a wolf nearby!");

		Entity e = p.getWorld().spawnEntity(p.getLocation(), spawnType);

		if (e instanceof Tameable)
			((Tameable) e).setOwner(p);

		return new AbilityResult(this, true);
	}

}
