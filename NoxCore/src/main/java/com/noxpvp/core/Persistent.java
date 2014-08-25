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

package com.noxpvp.core;

import java.util.logging.Level;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface Persistent extends ConfigurationSerializable {
	
	/**
	 * Returns a path node for the data storage.
	 * <p>
	 * When implementing if not implementing you must return
	 * getPersistentID().toString() it should use the UUID as the path
	 * instead!
	 * </p>
	 * 
	 * @return String path that must never be null or empty.
	 */
	public String getPersistenceNode();
	
	/**
	 * Returns a Unique persistent ID for this data.
	 * 
	 * @return String id
	 */
	public String getPersistentID();
	
	/**
	 * Log a message to the current logger.
	 * <p>
	 * Implementation is non specific. However the minimal required level
	 * is plugin level. Do not log straight to bukkit.
	 * </p>
	 * 
	 * @param level
	 *            of message.
	 * @param msg
	 *            message to log.
	 */
	public void log(Level level, String msg);
}
