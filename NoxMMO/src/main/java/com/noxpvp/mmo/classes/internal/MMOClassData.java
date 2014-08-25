package com.noxpvp.mmo.classes.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.noxpvp.core.commands.SafeNullPointerException;

public class MMOClassData implements ConfigurationSerializable {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static final String	SERIALIZE_PLAYER_UUID	= "player-uuid";
	private static final String	SERIALIZE_CONFIG_PATH	= "class-config-name";
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private UUID				playerUUID;
	private String				configFilePath;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public MMOClassData(Map<String, Object> data) {
		
		Object getter = null;
		
		if ((getter = data.get(SERIALIZE_PLAYER_UUID)) != null
				&& getter instanceof String) {
			playerUUID = UUID.fromString((String) getter);
		} else
			throw new SafeNullPointerException(
					"Could not get player uuid while loading");
		
		if ((getter = data.get(SERIALIZE_CONFIG_PATH)) != null
				&& getter instanceof String) {
			configFilePath = (String) getter;
		} else
			throw new SafeNullPointerException(
					"Could not get class config path while loading");
		
	}
	
	public MMOClassData(UUID player, ClassConfig config) {
		super();
		
		playerUUID = player;
		configFilePath = config.getFileConfig().getName();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public String getClassConfigPath() {
		return configFilePath;
	}
	
	public UUID getPlayerUUID() {
		return playerUUID;
	}
	
	public Map<String, Object> serialize() {
		final Map<String, Object> data = new HashMap<String, Object>();
		
		data.put(SERIALIZE_PLAYER_UUID, playerUUID.toString());
		data.put(SERIALIZE_CONFIG_PATH, configFilePath);
		
		return data;
	}
	
}
