package com.noxpvp.core;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.UUID;

public interface Persistent extends ConfigurationSerializable {

	/**
	 * Returns a Unique persistant ID for this data.
	 * @return UUID object
	 */
	public UUID getPersistantID();

	/**
	 * Returns a path node for the data storage.
	 * <p>When implementing if not implementing you must return getPerstantID().toString() it should use the UUID as the path instead!</p>
	 *
	 * @return String path that must never be null or empty.
	 */
	public String getPersistanceNode();
}
