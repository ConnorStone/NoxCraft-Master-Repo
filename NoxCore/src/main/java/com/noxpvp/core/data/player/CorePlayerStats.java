package com.noxpvp.core.data.player;

import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.utils.UUIDUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class CorePlayerStats extends PlayerStats {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization Keys
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static final String IPS_KEY = "logged-ips";
	private static final String LAST_DEATH_KEY = "last-death";
	private static final String USED_IGN_KEY = "used-igns";
	private static final String LAST_KILL_UUID_KEY = "last-kill-uuid";
	private static final String LAST_KILL_TYPE_KEY = "last-kill-type";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private DeathEntry lastDeath;
	private List<String> loggedIps = new ArrayList<String>();
	private List<String> usedIGNs = new ArrayList<String>();
	private UUID lastKillUUID;
	private EntityType lastKillType;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public CorePlayerStats(Map<String, Object> data) {
		super(data);

		if (data.containsKey(IPS_KEY)) loggedIps = (List<String>) Conversion.toList.convertSpecial(data.get(IPS_KEY), String.class, new ArrayList<String>());
		if (data.containsKey(LAST_DEATH_KEY)) lastDeath = (DeathEntry) data.get(LAST_DEATH_KEY);
		if (data.containsKey(USED_IGN_KEY)) usedIGNs = (List<String>) Conversion.toList.convertSpecial(data.get(USED_IGN_KEY), String.class, new ArrayList<String>());
		if (data.containsKey(LAST_KILL_TYPE_KEY)) lastKillType = EntityType.valueOf(data.get(LAST_KILL_TYPE_KEY).toString());
		if (data.containsKey(LAST_KILL_UUID_KEY)) lastKillUUID = UUID.fromString(data.get(LAST_KILL_UUID_KEY).toString());
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


	//~~~~~~~~~~~~~~~~
	//IGN'S
	//~~~~~~~~~~~~~~~~

	public List<String> getUsedIGNs() { return Collections.unmodifiableList(usedIGNs); }

	public String getLastIGN() {
		if (usedIGNs.size() > 0) return usedIGNs.get(0);
		else return null;
	}

	public void addLastIGN(Player player) {
		Validate.isTrue(getPersistantID().equals(player.getUniqueId()), "Player does not match the ID of this stats handler. This is not the same player!");

		addLastIGN(player.getName());
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

		return data;
	}

	public String getType() {
		return "Core";
	}

	public void setLastDeath(PlayerDeathEvent e) {

	}
}
