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

package com.noxpvp.mmo.listeners;

import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.handlers.MMOEventHandler;
import org.bukkit.event.Event;

/**
 * Used to provide helper methods for registering handlers.
 */
public interface IMMOHandlerHolder {

	/**
	 * <b>Helper method<b>
	 * <p/>
	 * Used to register handlers with the master listener
	 *
	 * @param handler
	 */
	public void registerHandler(MMOEventHandler<? extends Event> handler);

	/**
	 * <b>Helper method<b>
	 * <p/>
	 * Used to unregister handlers from the master listener
	 *
	 * @param handler
	 */
	public void unregisterHandler(MMOEventHandler<? extends Event> handler);

	/**
	 * Retrieves the master listener to register and unregister handlers for this ability.
	 *
	 * @return MasterListener
	 */
	public MasterListener getMasterListener();
}
