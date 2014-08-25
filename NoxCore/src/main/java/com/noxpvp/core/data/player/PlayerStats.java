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

package com.noxpvp.core.data.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import com.noxpvp.core.Persistent;

@SerializableAs("PlayerStats")
public abstract class PlayerStats implements Persistent {
	
	private final UUID	uuid;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~
	
	public PlayerStats(Map<String, Object> data) {
		this(UUID.fromString((String) data.get("uuid")));
	}
	
	public PlayerStats(Player player) {
		this(player.getUniqueId());
	}
	
	public PlayerStats(UUID uuid) {
		this.uuid = uuid;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 * <hr/>
	 * <p>
	 * This specific implementation takes the {@link #getType()} and
	 * appends {@literal "-stats"} as a path.
	 * </p>
	 */
	public final String getPersistenceNode() {
		return getType() + "-stats";
	}
	
	public String getPersistentID() {
		return uuid.toString();
	}
	
	// Required base implementation.
	public abstract String getType();
	
	public Map<String, Object> serialize() {
		final Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("uuid", getPersistentID().toString());
		
		return data;
	}
}
