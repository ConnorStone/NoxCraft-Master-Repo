package com.noxpvp.core;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.UUID;
import java.util.logging.Level;

public interface Persistent extends ConfigurationSerializable {

	/**
	 * Log a message to the current logger.
	 * <p>Implementation is non specific. However the minimal required level is plugin level. Do not log straight to bukkit.</p>
	 * @param level of message.
	 * @param msg message to log.
	 */
	public void log(Level level, String msg);

	/**
	 * Returns a Unique persistent ID for this data.
	 * @return UUID object
	 */
	public UUID getPersistantID();

	/**
	 * Returns a path node for the data storage.
	 * <p>When implementing if not implementing you must return getPersistentID().toString() it should use the UUID as the path instead!</p>
	 *
	 * @return String path that must never be null or empty.
	 */
	public String getPersistanceNode();
}
