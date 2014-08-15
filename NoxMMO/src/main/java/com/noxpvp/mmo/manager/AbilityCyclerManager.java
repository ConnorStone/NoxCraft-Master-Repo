package com.noxpvp.mmo.manager;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.manager.BaseManager;
import com.noxpvp.core.utils.UUIDUtil;
import com.noxpvp.mmo.AbilityCycler;
import com.noxpvp.mmo.NoxMMO;

import java.util.UUID;

public class AbilityCyclerManager extends BaseManager<AbilityCycler> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static final Class<AbilityCycler>	saveType	= AbilityCycler.class;
	private static final String	              saveFolder	= "ability-cyclers";
	private static AbilityCyclerManager	      instance;
	
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
	
	public FileConfiguration getConfig() {
		return getConfig("ability-cyclers.yml");
	}
	
	public AbilityCycler getCycler(UUID persistentID) {
		return get(persistentID);
	}
	
	@Override
	protected AbilityCycler load(UUID path, String node) {
		AbilityCycler created = getConfig().get(node, saveType);
		
		if (created != null)
			loadedCache.put(created.getPersistentID(), created);
		
		return created;
	}
	
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}
	
	public void load() {
		for (String key : getConfig().getKeys())
			load(key);
	}

	private AbilityCycler load(String key) {
		if (UUIDUtil.isUUID(key))
			return load(UUIDUtil.toUUID(key));
		return null;
	}
}
