package com.noxpvp.mmo.manager;

import java.util.UUID;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.manager.BaseManager;
import com.noxpvp.mmo.AbilityCycler;
import com.noxpvp.mmo.NoxMMO;

public class AbilityCyclerManager extends BaseManager<AbilityCycler> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static final Class<AbilityCycler>	saveType	= AbilityCycler.class;
	private static final String					saveFolder	= "ability-cyclers";
	private static AbilityCyclerManager			instance;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public AbilityCyclerManager() {
		super(saveType, saveFolder, false);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static AbilityCyclerManager getInstance() {
		return instance != null ? instance : (instance = new AbilityCyclerManager());
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public FileConfiguration getConfig() {
		return getConfig("ability-cyclers.yml");
	}
	
	public AbilityCycler getCycler(UUID persistentID) {
		return get(persistentID.toString(), persistentID.toString());
	}
	
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}
	
	public void load() {
		// Nothing to load
	}
	
}
