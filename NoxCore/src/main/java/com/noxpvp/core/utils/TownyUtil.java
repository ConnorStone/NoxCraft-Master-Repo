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

package com.noxpvp.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.bases.mutable.LocationAbstract;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.external.towny.*;
import com.palmergames.bukkit.towny.Towny;

public class TownyUtil {
	private static ModuleLogger log;

	private static Towny towny;
	private static ITownyHook hook = new NullTownyHook();

	static {
		setup();
	}

	/**
	 * Should not instance this object. Meant to be static.
	 */
	public TownyUtil() {
		super();
	}

	public static void setup() {
		if (log == null)
			log = NoxCore.getInstance().getModuleLogger("TownyUtil");

		Plugin plugin = Bukkit.getPluginManager().getPlugin("Towny");
		if (plugin instanceof Towny)
			towny = (Towny) plugin;
		else if (plugin != null)
			log.severe("NoxCore is outdated. Towny no longer of the class type Towny?");
		else
			log.info("Towny was not found.");

		if (towny != null)
			hook = new TownyHook(towny);
	}

	public static boolean isTownyEnabled() {
		return hook.isTownyEnabled();
	}

	public static boolean isUsingTowny(World world) {
		return hook.isUsingTowny(world);
	}

	public static boolean isUsingTowny(String worldNamed) {
		return hook.isUsingTowny(worldNamed);
	}

	public static boolean isClaimedLand(LocationAbstract location) {
		return hook.isClaimedLand(location);
	}

	public static boolean isClaimedLand(Location location) {
		return hook.isClaimedLand(location);
	}

	public static boolean isWild(LocationAbstract location) {
		return hook.isWild(location);
	}

	public static boolean isWild(Location location) {
		return hook.isWild(location);
	}

	public static boolean isOwnLand(Player p) {
		return hook.isOwnLand(p);
	}

	public static boolean isOwnLand(Player p, LocationAbstract location) {
		return hook.isOwnLand(p, location);
	}

	public static boolean isOwnLand(Player p, Location location) {
		return hook.isOwnLand(p, location);
	}

	public static boolean isTownMember(Player player, Location location) {
		return hook.isTownMember(player, location);
	}

	public static boolean isTownMember(Player player, String townName) {
		return hook.isTownMember(player, townName);
	}

	public static boolean isTownMember(String playerName, String townName) {
		return hook.isTownMember(playerName, townName);
	}

	public static boolean isPVP(Entity entity) {
		return hook.isPVP(entity);
	}

	public static boolean isPVP(LocationAbstract loc) {
		return hook.isPVP(loc);
	}

	public static boolean isPVP(Location loc) {
		return hook.isPVP(loc);
	}

	public static boolean isAlly(Player player, Entity entity) {
		return hook.isAlly(player, entity);
	}

	public static boolean isAlly(Player player, LocationAbstract location) {
		return hook.isAlly(player, location);
	}

	public static boolean isAlly(Player player, Location Location) {
		return hook.isAlly(player, Location);
	}

	public static boolean isAlly(Player player, Player otherPlayer) {
		return hook.isAlly(player, otherPlayer);
	}

	public static boolean isNationMember(Player player, Entity entity) {
		return hook.isNationMember(player, entity);
	}

	public static boolean isNationMember(Player player, LocationAbstract location) {
		return hook.isNationMember(player, location);
	}

	public static boolean isNationMember(Player player, Location location) {
		return hook.isNationMember(player, location);
	}

	public static boolean isNationMember(Player player, Player otherPlayer) {
		return hook.isNationMember(player, otherPlayer);
	}
	
	/*
	 * is ally land(block / loc)
	 */
}
