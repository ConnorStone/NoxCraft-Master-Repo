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

package com.noxpvp.core.data.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.conversion.type.ConversionTypes;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.data.NoxPlayer;

@SerializableAs("PlayerStats")
public class CorePlayerStats extends PlayerStats {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Serialization Keys
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static final String	IPS_KEY				= "logged-ips";
	private static final String	USED_IGN_KEY		= "used-igns";
	private static final String	LAST_DEATH_KEY		= "last.death";
	private static final String	LAST_KILL_UUID_KEY	= "last.kill.uuid";
	private static final String	LAST_KILL_TYPE_KEY	= "last.kill.type";
	private static final String	LAST_WORLD_NAME_KEY	= "last.world.name";
	private static final String	LAST_WORLD_UUID_KEY	= "last.world.uuid";
	
	// ~~~~~~~~~~~~~
	// Logging
	// ~~~~~~~~~~~~~
	
	private ModuleLogger		log;
	
	static {
		NoxPlayer.getModuleLogger("stats");
	}
	
	private DeathEntry			lastDeath;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private List<String>		loggedIps			= new ArrayList<String>();
	private List<String>		usedIGNs			= new ArrayList<String>();
	private UUID				lastKillUUID;
	private EntityType			lastKillType;
	private String				lastWorldName;
	private UUID				lastWorldUUID;
	
	public CorePlayerStats(Map<String, Object> data) {
		super(data);
		
		if (data.containsKey(IPS_KEY)) {
			loggedIps = (List<String>) ConversionTypes.toList.convertSpecial(data
					.get(IPS_KEY), String.class, new ArrayList<String>());
		}
		if (data.containsKey(USED_IGN_KEY)) {
			usedIGNs = (List<String>) ConversionTypes.toList.convertSpecial(data
					.get(USED_IGN_KEY), String.class, new ArrayList<String>());
		}
		
		lastKillType = data.containsKey(LAST_KILL_TYPE_KEY)
				&& data.get(LAST_KILL_TYPE_KEY) != null ? EntityType.valueOf(data
				.get(LAST_KILL_TYPE_KEY).toString()) : null;
		lastDeath = data.containsKey(LAST_DEATH_KEY)
				&& data.get(LAST_DEATH_KEY) != null ? data.get(LAST_DEATH_KEY) instanceof DeathEntry ? (DeathEntry) data
				.get(LAST_DEATH_KEY)
				: null
				: null;
		lastKillUUID = data.containsKey(LAST_KILL_UUID_KEY)
				&& data.get(LAST_KILL_UUID_KEY) != null ? UUID.fromString(data.get(
				LAST_KILL_UUID_KEY).toString()) : null;
		lastWorldName = data.containsKey(LAST_WORLD_NAME_KEY)
				&& data.get(LAST_WORLD_NAME_KEY) != null ? data.get(
				LAST_WORLD_NAME_KEY).toString() : null;
		lastWorldUUID = data.containsKey(LAST_WORLD_UUID_KEY)
				&& data.get(LAST_WORLD_UUID_KEY) != null ? UUID.fromString(data.get(
				LAST_WORLD_UUID_KEY).toString()) : null;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public CorePlayerStats(Player player) {
		super(player);
	}
	
	public CorePlayerStats(UUID uuid) {
		super(uuid);
	}
	
	public DeathEntry getLastDeath() {
		return lastDeath;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods:
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public String getLastIGN() {
		if (usedIGNs.size() > 0)
			return usedIGNs.get(0);
		else
			return null;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods: Last World Info
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public EntityType getLastKillType() {
		return lastKillType;
	}
	
	public UUID getLastKillUUID() {
		return lastKillUUID;
	}
	
	public World getLastWorld() {
		if (isOnline()) {
			updateWorld();
		}
		
		return lastWorldUUID != null ? Bukkit.getWorld(lastWorldUUID) : Bukkit
				.getWorld(lastWorldName);
	}
	
	// ~~~~~~~~~~~~~~~~
	// IGN'S
	// ~~~~~~~~~~~~~~~~
	
	public String getLastWorldName() {
		// Self update.
		if (isOnline()) {
			updateWorld();
		}
		return lastWorldName;
	}
	
	public UUID getLastWorldUUID() {
		if (isOnline()) {
			updateWorld();
		}
		return lastWorldUUID;
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPersistentID());
	}
	
	@Override
	public String getType() {
		return "Core";
	}
	
	// ~~~~~~~~~~~~~~~~
	// Kills
	// ~~~~~~~~~~~~~~~~
	
	public List<String> getUsedIGNs() {
		return Collections.unmodifiableList(usedIGNs);
	}
	
	public List<String> getUsedIPs() {
		return Collections.unmodifiableList(loggedIps);
	}
	
	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}
	
	// ~~~~~~~~~~~~~~~~
	// Deaths
	// ~~~~~~~~~~~~~~~~
	
	public void log(Level level, String msg) {
		log.log(level, msg);
	}
	
	@Override
	public Map<String, Object> serialize() {
		final Map<String, Object> data = super.serialize();
		
		data.put(IPS_KEY, getUsedIPs());
		data.put(LAST_DEATH_KEY, getLastDeath());
		data.put(USED_IGN_KEY, getUsedIGNs());
		data.put(LAST_KILL_UUID_KEY, getLastKillUUID());
		data.put(LAST_KILL_TYPE_KEY, getLastKillType());
		data.put(LAST_WORLD_NAME_KEY, getLastWorldName());
		data.put(LAST_WORLD_UUID_KEY,
				getLastWorldUUID() != null ? getLastWorldUUID().toString() : null);
		
		return data;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods: Helper Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void setLastDeath(PlayerDeathEvent e) {
		
	}
	
	public void setLastKill(LivingEntity lastKill) {
		lastKillType = lastKill.getType();
		lastKillUUID = lastKill.getUniqueId();
	}
	
	public void updateLastPlayerInfo(OfflinePlayer player) { // TODO: Add
																// function
																// and move
																// this
																// code to
																// it.
																// updateLastConnectionData()
		Validate.isTrue(getPersistentID().equals(player.getUniqueId().toString()),
				"Player does not match the ID of this stats handler. This is not the same player!");
		
		final String ip = player.getPlayer().getAddress().getAddress()
				.getHostAddress();
		if (player.isOnline()) {
			if (loggedIps.contains(ip)) {
				loggedIps.remove(ip);
			}
			loggedIps.add(0, ip);
		}
		if (!LogicUtil.nullOrEmpty(player.getName())) {
			addLastIGN(player.getName());
		}
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods: Serialization
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private void updateWorld() {
		final Player p = getOfflinePlayer().getPlayer();
		if (p != null) {
			lastWorldName = p.getWorld().getName();
			lastWorldUUID = p.getWorld().getUID();
		}
	}
	
	protected void addLastIGN(String name) {
		Validate.notNull(name);
		
		final String ign = getLastIGN();
		if (ign == null || !ign.equals(name)) {
			usedIGNs.add(0, name);
		}
	}
	
}
