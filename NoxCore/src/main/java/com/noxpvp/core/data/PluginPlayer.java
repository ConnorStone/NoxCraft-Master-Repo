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

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.Persistent;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * When Implementing it is required to have the following constructors available.
 * <br/>
 * Bold represents required. Others are optional.
 * <ul>
 *     <li><b>{@link java.util.UUID}</b></li>
 *     <li>{@link org.bukkit.OfflinePlayer}</li>
 * </ul>
 * @param <T>
 */
public interface PluginPlayer <T extends NoxPlugin> extends Persistent {
	public T getPlugin();

	public boolean isOnline();

	public UUID getPlayerUUID();

	/**
	 * Retrieves the player's name.
	 * <p>By Definition this should be returning the current player name. However this is not promised.</p>
	 * @return String Player's Name.
	 */
	public String getPlayerName();

	public OfflinePlayer getOfflinePlayer();

	public NoxPlayer getNoxPlayer();
}
