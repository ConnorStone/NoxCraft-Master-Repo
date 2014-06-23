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

package com.noxpvp.core.manager.old;

import com.noxpvp.core.OldPersistant;
import com.noxpvp.core.data.OldNoxPlayerAdapter;
import org.bukkit.OfflinePlayer;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.data.OldNoxPlayer;

public interface IPlayerManager<T extends OldNoxPlayerAdapter> extends OldPersistant {
	public T[] getLoadedPlayers();

	/**
	 * Gets the player. <br><br> Best implementation is to get players name and then use {@link #getPlayer(String)}
	 *
	 * @param player the player
	 * @return the player
	 */
	public T getPlayer(OfflinePlayer player);

	/**
	 * Gets the player with the specified name.
	 *
	 * @param name of player
	 * @return the player
	 */
	public T getPlayer(String name);

	public T getPlayer(OldNoxPlayer player);

	public NoxPlugin getPlugin();

	public void savePlayer(OldNoxPlayer player);

	public void savePlayer(T player);

	public void savePlayer(String name);

	public void savePlayer(OfflinePlayer player);

	public boolean isLoaded(String name);

	public boolean isLoaded(OfflinePlayer player);

	public void unloadPlayer(String name);

	public void unloadPlayer(OfflinePlayer player);

	public void loadPlayer(OldNoxPlayer player);

	public void loadPlayer(T player);

	public void loadPlayer(String name);

	public void loadPlayer(OfflinePlayer player);
}
