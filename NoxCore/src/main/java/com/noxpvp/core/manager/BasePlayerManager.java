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

package com.noxpvp.core.manager;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistent;
import com.noxpvp.core.utils.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class BasePlayerManager<T extends Persistent> extends BaseManager<T> {
	public BasePlayerManager(Class<T> type, String saveFolderPath) {
		super(type, saveFolderPath);
	}

	public BasePlayerManager(Class<T> type, String saveFolderPath, boolean useNoxFolder) {
		super(type, saveFolderPath, useNoxFolder);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~

	public void save(OfflinePlayer player) {
		super.save(player.getUniqueId());
	}

	@Override
	public void save(UUID id) {
		if (Bukkit.getOfflinePlayer(id).isOnline()) save(Bukkit.getPlayer(id));
		else super.save(id);
	}

	public T load(OfflinePlayer player) {
		return super.load(player.getUniqueId());
	}

	@Override
	public T load(UUID playerID) {
		if (Bukkit.getOfflinePlayer(playerID).isOnline()) return load(Bukkit.getPlayer(playerID)); //If player is online. We would rather use player functions to auto update data.
		else return super.load(playerID);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Required Implementations
	//~~~~~~~~~~~~~~~~~~~~~~~~

	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}

	public void load() {
		for (Player p : BukkitUtil.getOnlinePlayers())
			load(p);
	}

	public T getPlayer(UUID playerUID) { return get(playerUID); }

	public T getPlayer(OfflinePlayer player) {
		return get(player.getUniqueId());
	}

	//~~~~~~~~~~~~~~~~~~
	//Instanced: Helpers
	//~~~~~~~~~~~~~~~~~~

	public T loadPlayer(OfflinePlayer player) { return load(player); }

	public T loadPlayer(UUID playerUUID) { return load(playerUUID); }

	public void savePlayer(UUID playerUUID) { save(playerUUID);}

	public void savePlayer(OfflinePlayer player) { save(player); }

	//~~~~~~~~~~~~~~~~~~~~~~~
	//Static Methods
	//~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tells whether or not the player is online.
	 *
	 * <p><b>Warning this will accept any Persistent object however UUID's may not match.</b></p>
	 * <p><b><i>It was intended to be used against any object that uses a player ID as its id!</i></b></p>
	 * <p>Using anything other than those such objects may produce extremely undesirable behaviour</p>
	 *
	 * @see #isOnline(java.util.UUID)
	 * @param object Used to grab the player by the specified object's UUID
	 * @return true if online false otherwise.
	 */
	public static boolean isOnline(Persistent object) { return isOnline(object.getPersistentID()); }

	/**
	 * Tells whether or not the player is online.
	 *
	 * @see org.bukkit.Bukkit#getOfflinePlayer(java.util.UUID)
	 * @see org.bukkit.OfflinePlayer#isOnline()
	 *
	 * @param uuid uuid of the player being checked.
	 * @return true if online false otherwise.
	 */
	public static boolean isOnline(UUID uuid) { return Bukkit.getOfflinePlayer(uuid).isOnline(); }
}
