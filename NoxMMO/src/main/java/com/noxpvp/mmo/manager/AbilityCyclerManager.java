package com.noxpvp.mmo.manager;

import java.util.UUID;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.manager.BaseManager;
import com.noxpvp.mmo.AbilityCycler;
import com.noxpvp.mmo.NoxMMO;

public class AbilityCyclerManager extends BaseManager<AbilityCycler> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static AbilityCyclerManager			instance;
	
	private static final Class<AbilityCycler>	saveType	= AbilityCycler.class;
	private static final String					saveFolder	= "ability-cyclers";
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public AbilityCyclerManager() {
		super(saveType, saveFolder);
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
	
	public AbilityCycler getCycler(UUID persistentID) {
		return get(persistentID);
	}
	
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}
	
	public void load() {
		
	}
}
