package com.noxpvp.core.data.player;

import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.noxpvp.core.SafeLocation;
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

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private DeathEntry lastDeath;
	private List<String> loggedIps = new ArrayList<String>();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public CorePlayerStats(Map<String, Object> data) {
		super(data);

		if (data.containsKey(IPS_KEY)) loggedIps = (List<String>) Conversion.toList.convertSpecial(data.get(IPS_KEY), String.class, new ArrayList<String>());
		if (data.containsKey(LAST_DEATH_KEY)) lastDeath = (DeathEntry) data.get(LAST_DEATH_KEY);
	}

	public CorePlayerStats(Player player) {
		super(player);
	}

	public CorePlayerStats(UUID uuid) {
		super(uuid);
	}

	public List<String> getUsedIPs() {
		return Collections.unmodifiableList(loggedIps);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = super.serialize();

		data.put(IPS_KEY, getUsedIPs());
		data.put(LAST_DEATH_KEY, getLastDeath());

		return data;
	}

	public String getType() {
		return "Core";
	}

	public DeathEntry getLastDeath() {
		return lastDeath;
	}
}
