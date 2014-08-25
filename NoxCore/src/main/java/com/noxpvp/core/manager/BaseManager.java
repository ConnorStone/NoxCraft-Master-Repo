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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.Persistent;

public abstract class BaseManager<T extends Persistent> implements IManager<T> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private ModuleLogger		logger;
	
	private Class<T>			typeClass;
	
	protected final String		saveFolder;
	private final boolean		useNoxFolder;
	protected File				folder;
	
	protected Map<String, T>	loadedCache	= new HashMap<String, T>();
	
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
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public T get(String persistentId, String persistenceNode) {
		T it;
		if ((it = loadedCache.get(persistentId)) != null)
			return it;
		else
			return load(persistentId, persistenceNode);
	}
	
	public FileConfiguration getConfig(String path) {
		final File config = new File(getFile(), path);
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
	
	public FileConfiguration getConfig(T object) {
		final FileConfiguration ret = new FileConfiguration(new File(getFile(),
				object.getPersistentID()));
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
	
	public Map<String, T> getLoadedMap() {
		return Collections.unmodifiableMap(loadedCache);
	}
	
	public List<T> getLoadedValues() {
		return Collections.unmodifiableList(new ArrayList<T>(loadedCache.values()));
	}
	
	public Class<T> getTypeClass() {
		return typeClass;
	}
	
	public boolean isLoaded(String persistentId) {
		return loadedCache.containsKey(persistentId);
	}
	
	public boolean isLoaded(T object) {
		return loadedCache.containsValue(object);
	}
	
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
	
	public void save(T object) {
		final FileConfiguration datafile = getConfig(object.getPersistentID()
				+ ".yml");
		datafile.set(object.getPersistenceNode(), object);
		
		datafile.save();
	}
	
	public void saveAll() {
		for (final T loaded : loadedCache.values()) {
			save(loaded);
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
		final Iterator<T> iterator = getLoadedValues().iterator();
		while (iterator.hasNext()) {
			final T cur = iterator.next();
			unloadAndSave(cur);
		}
		
	}
	
	protected T getIfLoaded(String persistenceId) {
		T object;
		if ((object = loadedCache.get(persistenceId)) != null)
			return object;
		
		return null;
		
	}
	
	protected ModuleLogger getLogger() {
		return logger;
	}
	
	protected ModuleLogger getModuleLogger(String... modulePath) {
		return logger.getModule(modulePath);
	}
	
	protected T load(String persistentId, String persistenceNode) {
		final T created = getConfig(persistentId + ".yml").get(persistenceNode,
				typeClass);
		
		if (created != null) {
			loadedCache.put(persistentId, created);
		}
		
		return created;
	}
	
	protected void load(T object) {
		loadedCache.put(object.getPersistentID(), object);
	}
	
}
