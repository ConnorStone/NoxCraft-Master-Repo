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

package com.noxpvp.core.data;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;

/**
 * Using this interface you are required to implement a constructor with the
 * argument OldNoxPlayer as part of the implementation. <br />
 * <p/>
 * After doing so. It will allow the core to automatically give you objects of
 * desired choice.<br />
 * <p/>
 * Failure to do so if you register your object as a player object. Will result
 * in it not automatically being converted when supplied.
 */
public interface NoxPlayerAdapter {

	/**
	 * Retrieves the player object that this adaptor uses for data.
	 *
	 * @return OldNoxPlayer instance
	 */
	public OldNoxPlayer getNoxPlayer();

	/**
	 * Retrieves the player represented by this object.
	 *
	 * @return Player
	 */
	public Player getPlayer();

	public boolean hasFirstLoaded();

	public String getPlayerName();

	public void load();

	public String getUID();

	public UUID getUUID();

	public ConfigurationNode getPersistantData();

	public ConfigurationNode getTempData();

	/**
	 * All data must be set in here. Do not save to file from here though.
	 */
	public void save();

	public void saveToManager();
}
