package com.noxpvp.mmo.manager;

import java.io.File;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.manager.BaseManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.ClassConfig;

public class ClassConfigManager extends BaseManager<ClassConfig> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static ClassConfigManager		instance;
	
	private static final String				saveFolder	= "classes";
	private static final Class<ClassConfig>	saveType	= ClassConfig.class;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public ClassConfigManager() {
		super(saveType, saveFolder, false);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static ClassConfigManager getInstance() {
		return instance != null ? instance : (instance = new ClassConfigManager());
	}
	
	public static void setup() {
		if (instance == null) {
			instance = new ClassConfigManager();
		}
		
		instance.load();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public ClassConfig getClassConfig(String persistentId) {
		if (isLoaded(persistentId))
			return loadedCache.get(persistentId);
		else {
			load(new ClassConfig(getConfig(persistentId)));
			return getClassConfig(persistentId);
		}
	}
	
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}
	
	public void load() {
		for (final File f : getFile().listFiles()) {
			if (!f.getName().endsWith(".yml")) {
				continue;
			}
			
			final FileConfiguration fc = new FileConfiguration(f);
			fc.load();
			
			if (ClassConfig.isClassConfig(fc)) {
				load(new ClassConfig(fc));
			}
			
		}
		
	}
	
	@Override
	public void load(ClassConfig object) {
		loadObject(object);
	}
	
	@Override
	public void save(ClassConfig object) {
		object.getFileConfig().save();
	}
	
}
