package com.noxpvp.mmo.abilities;

import java.util.Map;
import java.util.logging.Level;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.Persistent;

public class AbilityConfig implements Persistent {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static ModuleLogger		log							= new ModuleLogger(
																		"AbilityConfig");
	
	// Data Pathss -- DO NOT EDIT
	private static final String		PATH_ABILITY_NAME			= "ability.name";
	private static final String		PATH_ABILITY_DESCRIPTION	= "ability.description";
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final FileConfiguration	config;
	
	private final String			abilityName;
	private final String			abilityDescription;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public AbilityConfig(FileConfiguration config) {
		this.config = config;
		
		config.setHeader(PATH_ABILITY_NAME, "The name used in-game for this ability");
		abilityName = config.get(PATH_ABILITY_NAME, String.class,
				"NO_ABILITY_NAME_DEFINED");
		
		config.setHeader(PATH_ABILITY_DESCRIPTION,
				"The description used in-game and shown to players");
		abilityDescription = config.get(PATH_ABILITY_DESCRIPTION, String.class,
				"NO_ABILITY_DESCRIPTION_DEFINED");
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static boolean isAbilityConfig(FileConfiguration fc) {
		fc.load();
		
		return fc.get(PATH_ABILITY_NAME, String.class) != null;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public String getAbilityDescription() {
		return abilityDescription;
	};
	
	public String getAbilityName() {
		return abilityName;
	}
	
	public FileConfiguration getFileConfig() {
		return config;
	}
	
	public String getPersistenceNode() {
		return config.getName();
	}
	
	public String getPersistentID() {
		return config.getName();
	}
	
	public void log(Level level, String msg) {
		log.log(level, msg);
	}
	
	public Map<String, Object> serialize() {
		return null;
	}
	
}
