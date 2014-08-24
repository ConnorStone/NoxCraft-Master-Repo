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
	public void unloadAndSaveAll() {
		for (final ClassConfig cc : getLoadeds().values()) {
			cc.getFileConfig().save();
			unload(cc);
		}
		
	}
	
	@Override
	protected ClassConfig load(ClassConfig object) {
		return loadedCache.put(object.getPersistenceNode(), object);
	}
	
}
