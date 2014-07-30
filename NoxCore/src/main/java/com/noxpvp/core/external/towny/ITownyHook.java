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

public interface ITownyHook {
	public boolean isTownyEnabled();

	public boolean isUsingTowny(World world);

	public boolean isUsingTowny(String worldNamed);

	public boolean isClaimedLand(LocationAbstract location);

	public boolean isClaimedLand(Location location);

	public boolean isWild(LocationAbstract location);

	public boolean isWild(Location location);

	public boolean isOwnLand(Player p);

	public boolean isOwnLand(Player p, LocationAbstract location);

	public boolean isOwnLand(Player p, Location location);

	public boolean isTownMember(Player player, Location location);

	public boolean isTownMember(Player player, String townName);

	public boolean isTownMember(String playerName, String townName);

	public boolean isPVP(Entity entity);

	public boolean isPVP(LocationAbstract loc);

	public boolean isPVP(Location loc);

	public boolean isAlly(Player player, Entity entity);

	public boolean isAlly(Player player, LocationAbstract location);

	public boolean isAlly(Player player, Location Location);

	public boolean isAlly(Player player, Player otherPlayer);

	public boolean isNationMember(Player player, Entity entity);

	public boolean isNationMember(Player player, LocationAbstract location);

	public boolean isNationMember(Player player, Location location);

	public boolean isNationMember(Player player, Player otherPlayer);
}
