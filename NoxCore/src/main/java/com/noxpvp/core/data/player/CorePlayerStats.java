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

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.data.NoxPlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;
import java.util.logging.Level;

@SerializableAs("PlayerStats")
public class CorePlayerStats extends PlayerStats {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization Keys
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static final String IPS_KEY = "logged-ips";
	private static final String USED_IGN_KEY = "used-igns";
	private static final String LAST_DEATH_KEY = "last.death";
	private static final String LAST_KILL_UUID_KEY = "last.kill.uuid";
	private static final String LAST_KILL_TYPE_KEY = "last.kill.type";
	private static final String LAST_WORLD_NAME_KEY = "last.world.name";
	private static final String LAST_WORLD_UUID_KEY = "last.world.uuid";

	//~~~~~~~~~~~~~
	//Logging
	//~~~~~~~~~~~~~

	private ModuleLogger log;

	static {
		NoxPlayer.getModuleLogger("stats");
	}

	public void log(Level level, String msg) {
		log.log(level, msg);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private DeathEntry lastDeath;
	private List<String> loggedIps = new ArrayList<String>();
	private List<String> usedIGNs = new ArrayList<String>();
	private UUID lastKillUUID;
	private EntityType lastKillType;
	private String lastWorldName;
	private UUID lastWorldUUID;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public CorePlayerStats(Map<String, Object> data) {
		super(data);

		if (data.containsKey(IPS_KEY)) loggedIps = (List<String>) Conversion.toList.convertSpecial(data.get(IPS_KEY), String.class, new ArrayList<String>());
		if (data.containsKey(USED_IGN_KEY)) usedIGNs = (List<String>) Conversion.toList.convertSpecial(data.get(USED_IGN_KEY), String.class, new ArrayList<String>());

		lastKillType = ((data.containsKey(LAST_KILL_TYPE_KEY) && data.get(LAST_KILL_TYPE_KEY) != null) ? EntityType.valueOf(data.get(LAST_KILL_TYPE_KEY).toString()) : null);
		lastDeath = ((data.containsKey(LAST_DEATH_KEY) && data.get(LAST_DEATH_KEY) != null) ? (data.get(LAST_DEATH_KEY) instanceof DeathEntry ? (DeathEntry) data.get(LAST_DEATH_KEY) : null) : null);
		lastKillUUID = ((data.containsKey(LAST_KILL_UUID_KEY) && data.get(LAST_KILL_UUID_KEY) != null) ? UUID.fromString(data.get(LAST_KILL_UUID_KEY).toString()) : null);
		lastWorldName = ((data.containsKey(LAST_WORLD_NAME_KEY) && data.get(LAST_WORLD_NAME_KEY) != null) ? data.get(LAST_WORLD_NAME_KEY).toString() : null);
		lastWorldUUID = ((data.containsKey(LAST_WORLD_UUID_KEY) && data.get(LAST_WORLD_UUID_KEY) != null) ? UUID.fromString(data.get(LAST_WORLD_UUID_KEY).toString()) : null);
	}

	public CorePlayerStats(Player player) {
		super(player);
	}

	public CorePlayerStats(UUID uuid) {
		super(uuid);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods:
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public List<String> getUsedIPs() {
		return Collections.unmodifiableList(loggedIps);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Last World Info
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public String getLastWorldName() {
		//Self update.
		if (isOnline()) updateWorld();
		return this.lastWorldName;
	}

	public UUID getLastWorldUUID() {
		if (isOnline()) updateWorld();
		return this.lastWorldUUID;
	}

	private void updateWorld() {
		Player p = getOfflinePlayer().getPlayer();
		if (p != null) {
			this.lastWorldName = p.getWorld().getName();
			this.lastWorldUUID = p.getWorld().getUID();
		}
	}

	//~~~~~~~~~~~~~~~~
	//IGN'S
	//~~~~~~~~~~~~~~~~

	public List<String> getUsedIGNs() { return Collections.unmodifiableList(usedIGNs); }

	public String getLastIGN() {
		if (usedIGNs.size() > 0) return usedIGNs.get(0);
		else return null;
	}

	public void updateLastPlayerInfo(OfflinePlayer player) { //TODO: Add function and move this code to it. updateLastConnectionData()
		Validate.isTrue(getPersistentID().equals(player.getUniqueId()), "Player does not match the ID of this stats handler. This is not the same player!");

		String ip = player.getPlayer().getAddress().getAddress().getHostAddress();
		if (player.isOnline()) {
			if (this.loggedIps.contains(ip)) this.loggedIps.remove(ip);
			this.loggedIps.add(0, ip);
		}
		if (!LogicUtil.nullOrEmpty(player.getName())) addLastIGN(player.getName());
	}

	protected void addLastIGN(String name) {
		Validate.notNull(name);

		final String ign = getLastIGN();
		if (ign == null || !ign.equals(name)) usedIGNs.add(0, name);
	}

	//~~~~~~~~~~~~~~~~
	//Kills
	//~~~~~~~~~~~~~~~~

	public void setLastKill(LivingEntity lastKill) {
		this.lastKillType = lastKill.getType();
		this.lastKillUUID = lastKill.getUniqueId();
	}

	public UUID getLastKillUUID() {
		return this.lastKillUUID;
	}

	public EntityType getLastKillType() {
		return lastKillType;
	}

	//~~~~~~~~~~~~~~~~
	//Deaths
	//~~~~~~~~~~~~~~~~

	public DeathEntry getLastDeath() {
		return lastDeath;
	}

	public void setLastDeath(PlayerDeathEvent e) {

	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Helper Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPersistentID());
	}

	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}

	public World getLastWorld() {
		if (isOnline()) updateWorld();

		return (this.lastWorldUUID != null ? Bukkit.getWorld(this.lastWorldUUID) : Bukkit.getWorld(this.lastWorldName));
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = super.serialize();

		data.put(IPS_KEY, getUsedIPs());
		data.put(LAST_DEATH_KEY, getLastDeath());
		data.put(USED_IGN_KEY, getUsedIGNs());
		data.put(LAST_KILL_UUID_KEY, getLastKillUUID());
		data.put(LAST_KILL_TYPE_KEY, getLastKillType());
		data.put(LAST_WORLD_NAME_KEY, getLastWorldName());
		data.put(LAST_WORLD_UUID_KEY, getLastWorldUUID().toString());

		return data;
	}

	public String getType() {
		return "Core";
	}

}
