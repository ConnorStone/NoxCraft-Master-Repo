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

package com.noxpvp.mmo.abilities.internal;

import com.noxpvp.core.internal.IHeated;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.listeners.IMMOHandlerHolder;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface PlayerAbility extends Ability, IHeated, IMMOHandlerHolder {

	/**
	 * Retrieves the player that uses this ability.
	 *
	 * @return Player object that holds this ability
	 */
	public Player getPlayer();

	/**
	 * Retrieves the OfflinePlayer object associated with this ability.
	 *
	 * @return OfflinePlayer object.
	 */
	public OfflinePlayer getOfflinePlayer();

	/**
	 * Tells whether or not the player is online.
	 * @return true if online, false otherwise
	 */
	public boolean isOnline();

	/**
	 * Retrieves the player UUID
	 * @return UUID
	 */
	public UUID getPlayerUUID();

	/**
	 * Retrieves the MMOPlayer
	 * <br/>
	 * This method should use getOfflinePlayer() within to retrieve this object if possible.
	 *
	 * @return MMOPlayer object.
	 */
	public MMOPlayer getMMOPlayer();

	/**
	 * Checks if the user has permission to use this ability.
	 *
	 * @return True if allow, False otherwise.
	 */
	public boolean hasPermission();

	//TODO: Discuess whether or not we would like to add a method for fetching NoxPlayer in interfaces...
}
