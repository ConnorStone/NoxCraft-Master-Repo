package com.noxpvp.mmo.manager;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.manager.BaseManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityConfig;

public class AbilityConfigManager extends BaseManager<AbilityConfig> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static AbilityConfigManager			instance;
	
	private static final String					saveFolder	= "abilities";
	private static final Class<AbilityConfig>	saveType	= AbilityConfig.class;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public AbilityConfigManager() {
		super(saveType, saveFolder, false);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static void setup() {
		if (instance == null) {
			instance = new AbilityConfigManager();
		}
		
		instance.load();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}
	
	public void load() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void unloadAndSaveAll() {
		for (final AbilityConfig ac : getLoadedMap().values()) {
			ac.getFileConfig().save();
			unload(ac);
		}
		
	}
	
}
