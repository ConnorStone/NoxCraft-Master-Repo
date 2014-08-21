/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.core.manager;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.Persistent;

public abstract class BaseManager<T extends Persistent> implements IManager<T> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private ModuleLogger	logger;
	
	private Class<T>		typeClass;
	
	protected final String	saveFolder;
	private final boolean	useNoxFolder;
	protected File			folder;
	
	protected Map<UUID, T>	loadedCache	= new HashMap<UUID, T>();
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public BaseManager(Class<T> type, String saveFolderPath) {
		this(type, saveFolderPath, true);
	}
	
	public BaseManager(Class<T> type, String saveFolderPath, boolean useNoxFolder) {
		this.typeClass = type;
		this.saveFolder = saveFolderPath;
		this.useNoxFolder = useNoxFolder;
		
		logger = new ModuleLogger(getPlugin(), getClass().getSimpleName());
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Logging Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public FileConfiguration getConfig(String name) {
		final File config = new File(getFile(), name);
		if (!config.exists()) {
			try {
				config.createNewFile();
			} catch (final IOException e) {
			}
		}
		
		final FileConfiguration ret = new FileConfiguration(config);
		ret.load();
		
		return ret;
	}
	
	public File getFile() {
		if (folder == null) {
			folder = new File(useNoxFolder ? getPlugin().getNoxPluginFolder()
					: getPlugin().getDataFolder(), saveFolder);
			folder.mkdirs();
		}
		return folder;
	}
	
	public Map<UUID, T> getLoadeds() {
		return Collections.unmodifiableMap(loadedCache);
	}
	
	public Class<T> getTypeClass() {
		return typeClass;
	}
	
	public boolean isLoaded(T object) {
		return loadedCache.containsValue(object);
	}
	
	public boolean isLoaded(UUID id) {
		return loadedCache.containsKey(id);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void loadObject(T object) {
		if (!loadedCache.containsKey(object.getPersistentID())) {
			loadedCache.put(object.getPersistentID(), object);
		}
	}
	
	/**
	 * Log a message, with no arguments.
	 * <p>
	 * If the logger is currently enabled for the given message level then
	 * the given message is forwarded to all the registered output Handler
	 * objects.
	 * <p>
	 * 
	 * @param level
	 *            One of the message level identifiers, e.g., SEVERE
	 * @param msg
	 *            The string message (or a key in the message catalog)
	 */
	public void log(Level level, String msg) {
		logger.log(level, msg);
	}
	
	/**
	 * Log a message, with one object parameter.
	 * <p>
	 * If the logger is currently enabled for the given message level then
	 * a corresponding LogRecord is created and forwarded to all the
	 * registered output Handler objects.
	 * <p>
	 * 
	 * @param level
	 *            One of the message level identifiers, e.g., SEVERE
	 * @param msg
	 *            The string message (or a key in the message catalog)
	 * @param param1
	 *            parameter to the message
	 */
	public void log(Level level, String msg, Object param1) {
		logger.log(level, msg, param1);
	}
	
	/**
	 * Log a message, with an array of object arguments.
	 * <p>
	 * If the logger is currently enabled for the given message level then
	 * a corresponding LogRecord is created and forwarded to all the
	 * registered output Handler objects.
	 * <p>
	 * 
	 * @param level
	 *            One of the message level identifiers, e.g., SEVERE
	 * @param msg
	 *            The string message (or a key in the message catalog)
	 * @param params
	 *            array of parameters to the message
	 */
	public void log(Level level, String msg, Object[] params) {
		logger.log(level, msg, params);
	}
	
	/**
	 * Log a message, with associated Throwable information.
	 * <p>
	 * If the logger is currently enabled for the given message level then
	 * the given arguments are stored in a LogRecord which is forwarded to
	 * all registered output handlers.
	 * <p>
	 * Note that the thrown argument is stored in the LogRecord thrown
	 * property, rather than the LogRecord parameters property. Thus is it
	 * processed specially by output Formatters and is not treated as a
	 * formatting parameter to the LogRecord message property.
	 * <p>
	 * 
	 * @param level
	 *            One of the message level identifiers, e.g., SEVERE
	 * @param msg
	 *            The string message (or a key in the message catalog)
	 * @param thrown
	 *            Throwable associated with log message.
	 */
	public void log(Level level, String msg, Throwable thrown) {
		logger.log(level, msg, thrown);
	}
	
	public void save() {
		for (final T loaded : loadedCache.values()) {
			save(loaded);
		}
	}
	
	public void save(T object) {
		save(object, object.getPersistentID());
	}
	
	public void save(UUID id) {
		final T object = getIfLoaded(id);
		if (object != null) {
			save(object);
		}
	}
	
	public void unload(T object) {
		loadedCache.remove(object.getPersistentID());
	}
	
	public void unloadAndSave(T object) {
		if (isLoaded(object)) {
			save(object);
			unload(object);
		}
	}
	
	public void unloadAndSaveAll() {
		for (final T loaded : loadedCache.values()) {
			unloadAndSave(loaded);
		}
	}
	
	protected T get(UUID arg) {
		T it;
		if ((it = loadedCache.get(arg)) != null)
			return it;
		else
			return load(arg);
	}
	
	protected T getIfLoaded(UUID id) {
		T object;
		if ((object = loadedCache.get(id)) != null)
			return object;
		
		return null;
	}
	
	protected ModuleLogger getLogger() {
		return logger;
	}
	
	protected ModuleLogger getModuleLogger(String... modulePath) {
		return logger.getModule(modulePath);
	}
	
	protected boolean isLoaded(String key) {
		return loadedCache.containsKey(key);
	}
	
	protected T load(T object) {
		return load(object.getPersistentID());
	}
	
	protected T load(UUID path) {
		return load(path, path.toString());
	}
	
	protected T load(UUID path, String node) {
		final T created = getConfig(path.toString() + ".yml").get(node, typeClass);
		
		if (created != null) {
			loadedCache.put(created.getPersistentID(), created);
		}
		
		return created;
	}
	
	protected void save(T object, UUID id) {
		final FileConfiguration datafile = getConfig(id.toString() + ".yml");
		datafile.set(object.getPersistenceNode(), object);
		
		datafile.save();
	}
	
	protected void unload(UUID arg) {
		T object;
		if ((object = getIfLoaded(arg)) != null) {
			unload(object);
		}
	}
	
	protected void unloadAndSave(UUID id) {
		T object = null;
		if ((object = getIfLoaded(id)) != null) {
			unloadAndSave(object);
		}
	}
	
}
