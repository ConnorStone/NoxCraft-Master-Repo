package com.noxpvp.core.manager;


import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.Persistent;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public abstract class BaseManager<T extends Persistent> implements IManager<T> {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instance Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private ModuleLogger logger;
	private Class<T> typeClass;
	private String saveFolder;
	private File folder;
	private Map<UUID, T> loadedCache;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public BaseManager(Class<T> type, String saveFolderPath) {
		this.typeClass = type;
		this.saveFolder = saveFolderPath;
		this.loadedCache = new HashMap<UUID, T>();

		logger = new ModuleLogger(getPlugin(), typeClass.getName() + "Manager");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Logging Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Log a message, with no arguments.
	 * <p>
	 * If the logger is currently enabled for the given message
	 * level then the given message is forwarded to all the
	 * registered output Handler objects.
	 * <p>
	 * @param   level   One of the message level identifiers, e.g., SEVERE
	 * @param   msg     The string message (or a key in the message catalog)
	 */
	public void log(Level level, String msg) {
		logger.log(level, msg);
	}

	/**
	 * Log a message, with one object parameter.
	 * <p>
	 * If the logger is currently enabled for the given message
	 * level then a corresponding LogRecord is created and forwarded
	 * to all the registered output Handler objects.
	 * <p>
	 * @param   level   One of the message level identifiers, e.g., SEVERE
	 * @param   msg     The string message (or a key in the message catalog)
	 * @param   param1  parameter to the message
	 */
	public void log(Level level, String msg, Object param1) {
		logger.log(level, msg, param1);
	}

	/**
	 * Log a message, with an array of object arguments.
	 * <p>
	 * If the logger is currently enabled for the given message
	 * level then a corresponding LogRecord is created and forwarded
	 * to all the registered output Handler objects.
	 * <p>
	 * @param   level   One of the message level identifiers, e.g., SEVERE
	 * @param   msg     The string message (or a key in the message catalog)
	 * @param   params  array of parameters to the message
	 */
	public void log(Level level, String msg, Object[] params) {
		logger.log(level, msg, params);
	}

	/**
	 * Log a message, with associated Throwable information.
	 * <p>
	 * If the logger is currently enabled for the given message
	 * level then the given arguments are stored in a LogRecord
	 * which is forwarded to all registered output handlers.
	 * <p>
	 * Note that the thrown argument is stored in the LogRecord thrown
	 * property, rather than the LogRecord parameters property.  Thus is it
	 * processed specially by output Formatters and is not treated
	 * as a formatting parameter to the LogRecord message property.
	 * <p>
	 * @param   level   One of the message level identifiers, e.g., SEVERE
	 * @param   msg     The string message (or a key in the message catalog)
	 * @param   thrown  Throwable associated with log message.
	 */
	public void log(Level level, String msg, Throwable thrown) {
		logger.log(level, msg, thrown);
	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instance Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public File getFile() {
		if (folder == null) {
			folder = new File(getPlugin().getNoxPluginFolder(), saveFolder);
			folder.mkdirs();
		}
		return folder;
	}

	public FileConfiguration getConfig(String name) {
		File config = new File(getFile(), name);
		if (!config.exists())
			try {
				config.createNewFile();
			} catch (IOException e) {
			}

		FileConfiguration ret = new FileConfiguration(config);
		ret.load();

		return ret;
	}

	public Map<UUID, T> getLoadeds() {
		return Collections.unmodifiableMap(loadedCache);
	}

	protected T getIfLoaded(UUID id) {
		T object;
		if ((object = loadedCache.get(id)) != null)
			return object;

		return null;
	}

	protected T get(UUID arg) {
		T it;
		if ((it = loadedCache.get(arg)) != null)
			return it;
		else
			return load(arg);
	}

	public boolean isLoaded(UUID id) {
		return loadedCache.containsKey(id);
	}

	public boolean isLoaded(T object) {
		return loadedCache.containsValue(object);
	}

	protected boolean isLoaded(String key) {
		return loadedCache.containsKey(key);
	}

	protected T load(UUID path) {
		return load(path, path.toString());
	}

	protected T load(UUID path, String node) {
		T created = getConfig(path.toString() + ".yml").get(node, typeClass);

		if (created != null)
			loadedCache.put(created.getPersistentID(), created);

		return created;
	}

	protected T load(T object) {
		return load(object.getPersistentID());
	}

	public void loadObject(T object) {
		if (!loadedCache.containsKey(object.getPersistentID()))
			loadedCache.put(object.getPersistentID(), object);
	}

	protected void save(T object, UUID id) {
		FileConfiguration datafile = getConfig(id.toString() + ".yml");
		datafile.set(object.getPersistenceNode(), object);

		datafile.save();
	}

	public void save(UUID id) {
		T object = getIfLoaded(id);
		if (object != null) save(object);
	}

	public void save(T object) {
		save(object, object.getPersistentID());
	}

	public void unload(T object) {
		loadedCache.remove(object.getPersistentID());
	}

	protected void unload(UUID arg) {
		T object;
		if ((object = getIfLoaded(arg)) != null)
			unload(object);
	}

	protected void unloadAndSave(UUID id) {
		T object = null;
		if ((object = getIfLoaded(id)) != null) unloadAndSave(object);
	}

	public void unloadAndSave(T object) {
		if (isLoaded(object)) {
			save(object);
			unload(object);
		}
	}

	public void unloadAndSaveAll() {
		for (T loaded : loadedCache.values())
			unloadAndSave(loaded);
	}

	public void save() {
		for (T loaded : loadedCache.values()) {
			save(loaded);
		}
	}

}
