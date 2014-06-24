package com.noxpvp.core.data.player;

import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.noxpvp.core.SafeLocation;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class CorePlayerStats extends PlayerStats {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization Keys
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static final String IPS_KEY = "logged-ips";
	private static final String LAST_DEATH_KEY = "last-death";
	private static final String USED_IGN_KEY = "used-igns";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private DeathEntry lastDeath;
	private List<String> loggedIps = new ArrayList<String>();
	private List<String> usedIGNs = new ArrayList<String>();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public CorePlayerStats(Map<String, Object> data) {
		super(data);

		if (data.containsKey(IPS_KEY)) loggedIps = (List<String>) Conversion.toList.convertSpecial(data.get(IPS_KEY), String.class, new ArrayList<String>());
		if (data.containsKey(LAST_DEATH_KEY)) lastDeath = (DeathEntry) data.get(LAST_DEATH_KEY);
		if (data.containsKey(USED_IGN_KEY)) usedIGNs = (List<String>) Conversion.toList.convertSpecial(data.get(USED_IGN_KEY), String.class, new ArrayList<String>());
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

		return data;
	}

	public String getType() {
		return "Core";
	}
}
