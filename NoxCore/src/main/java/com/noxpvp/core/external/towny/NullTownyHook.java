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

package com.noxpvp.core.external.towny;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.bases.mutable.LocationAbstract;

public class NullTownyHook implements ITownyHook {

	public boolean isTownyEnabled() {
		return false;
	}

	public boolean isUsingTowny(World world) {
		return false;
	}

	public boolean isUsingTowny(String worldNamed) {
		return false;
	}

	public boolean isClaimedLand(LocationAbstract location) {
		return false;
	}

	public boolean isClaimedLand(Location location) {
		return false;
	}

	public boolean isWild(LocationAbstract location) {
		return true;
	}

	public boolean isWild(Location location) {
		return true;
	}

	public boolean isOwnLand(Player p) {
		return false;
	}

	public boolean isOwnLand(Player p, LocationAbstract location) {
		return false;
	}

	public boolean isOwnLand(Player p, Location location) {
		return false;
	}

	public boolean isTownMember(Player player, Location location) {
		return false;
	}

	public boolean isTownMember(Player player, String townName) {
		return false;
	}

	public boolean isTownMember(String playerName, String townName) {
		return false;
	}

	public boolean isPVP(Entity entity) {
		return isPVP(entity.getLocation());
	}

	public boolean isPVP(LocationAbstract loc) {
		return isPVP(loc.toLocation());
	}

	public boolean isPVP(Location loc) {
		return loc.getWorld().getPVP();
	}

	public boolean isAlly(Player player, Entity entity) {
		return false;
	}

	public boolean isAlly(Player player, LocationAbstract location) {
		return false;
	}

	public boolean isAlly(Player player, Location Location) {
		return false;
	}

	public boolean isAlly(Player player, Player otherPlayer) {
		return false;
	}

	public boolean isNationMember(Player player, Entity entity) {
		return false;
	}

	public boolean isNationMember(Player player, LocationAbstract location) {
		return false;
	}

	public boolean isNationMember(Player player, Location location) {
		return false;
	}

	public boolean isNationMember(Player player, Player otherPlayer) {
		return false;
	}

}
