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

package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.noxpvp.core.data.OldNoxPlayer;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public interface IPlayerAbility extends IEntityAbility {

	/**
	 * Retrieves the player that uses this ability.
	 *
	 * @return Player object that holds this ability
	 */
	public Player getPlayer();

	/**
	 * The entity is actually the return of getPlayer()
	 *
	 * @see #getPlayer()
	 */
	public Entity getEntity();

	/**
	 * Retrieves the NoxPlayer core player implementation for this ability.
	 * <br/>
	 * This method should use getPlayer() within to retrieve this object if possible.
	 *
	 * @return NoxPlayer object.
	 */
	public OldNoxPlayer getNoxPlayer();

	/**
	 * Retrieves the master listener to register and unregister handlers for this ability.
	 *
	 * @return
	 */
	public MasterListener getMasterListener();

	/**
	 * <b>Helper method<b>
	 * <p/>
	 * Used to register handlers with the master listener
	 *
	 * @param handler
	 */
	public void registerHandler(BaseMMOEventHandler<? extends Event> handler);

	/**
	 * <b>Helper method<b>
	 * <p/>
	 * Used to unregister handlers from the master listener
	 *
	 * @param handler
	 */
	public void unregisterHandler(BaseMMOEventHandler<? extends Event> handler);
}
