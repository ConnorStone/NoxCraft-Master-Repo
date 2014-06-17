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

package com.noxpvp.core.old_manager;

import java.util.Map;

import com.noxpvp.core.utils.UUIDUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.data.OldNoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;

public abstract class BasePlayerManager<T extends NoxPlayerAdapter> implements IPlayerManager<T> {
	private Map<String, T> uid_players;
	private Map<String, T> name_players;

	private CorePlayerManager pm = null;

	private Class<T> typeClass;

	public BasePlayerManager(Class<T> t) {
		this.typeClass = t;
		this.uid_players = craftNewStorage();
		this.name_players = craftNewStorage();
		CorePlayerManager.addManager(this);
	}

	protected T craftNew(NoxPlayerAdapter adapter) {
		return craftNew(adapter.getNoxPlayer());
	}

	public void loadPlayer(OldNoxPlayer player) {
		getPlayer(player).load();
	}

	protected abstract T craftNew(OldNoxPlayer adapter);

	/**
	 * Required to grab objects.
	 *
	 * @param name of player.
	 * @return Object.
	 */
	protected T craftNew(String name) {
		return craftNew(getCorePlayerManager().getPlayer(name));
	}

	/**
	 * Required to store player data.
	 *
	 * @return
	 */
	protected abstract Map<String, T> craftNewStorage();

	/**
	 * Should never be called in the constructor of the core manager.
	 *
	 * @return PlayerManager from NoxCore
	 */
	protected CorePlayerManager getCorePlayerManager() {
		if (pm == null)
			pm = CorePlayerManager.getInstance();
		return pm;
	}

	public T[] getLoadedPlayers() {
		return LogicUtil.toArray(uid_players.values(), typeClass);
	}

	public T getPlayer(OldNoxPlayer oldNoxPlayer) {
		T player = null;
		String name = oldNoxPlayer.getIdentity();
		if (isLoaded(name))
			if (UUIDUtil.isUUID(name))
				player = uid_players.get(name);
			else
				player = name_players.get(name);
		else {
			player = craftNew(oldNoxPlayer);
			player.load();
			if (UUIDUtil.isUUID(name))
				uid_players.put(name, player);
			else
				name_players.put(name, player);
		}
		return player;
	}

	public T getPlayer(NoxPlayerAdapter adapt) { //TODO: remove duplicate code ID(gp1)
		return getPlayer(adapt.getNoxPlayer());
	}

	public final T getPlayer(OfflinePlayer player) {
		if (player == null)
			return null;

		if (player instanceof Player) return getPlayer(player.getUniqueId().toString());
		else if (LogicUtil.nullOrEmpty(player.getName())) return null;
		else return getPlayer(player.getName());
	}

	public final T getPlayer(String name) { //TODO: remove duplicate code ID(gp1)
		Validate.notNull(name);

		T player = null;
		if (isLoaded(name))
			if (UUIDUtil.isUUID(name)) player = uid_players.get(name);
			else player = name_players.get(name);
		else {
			player = craftNew(name);
			if (UUIDUtil.isUUID(name)) uid_players.put(name, player);
			else name_players.put(name, player);
			loadPlayer(player);
		}
		return player;
	}

	protected Map<String, T> getPlayerMap() {
		return this.uid_players;
	}

	public final boolean isLoaded(OfflinePlayer player) {
		return isLoaded(player.getName());
	}

	public final boolean isLoaded(String name) {
		if (UUIDUtil.isUUID(name)) return uid_players.containsKey(name);
		else return name_players.containsKey(name);
	}

	public void load() {
		for (Player p : Bukkit.getOnlinePlayers())
			loadPlayer(p);
	}

	public void loadPlayer(OfflinePlayer player) {
		loadPlayer(getPlayer(player));
	}

	public void loadPlayer(String name) {
		loadPlayer(getPlayer(name));
	}

	public void loadPlayer(T player) {
		player.load();
	}

	public void save() {
		for (T p : getLoadedPlayers())
			savePlayer(p);
	}

	public void savePlayer(OldNoxPlayer player) {
		getPlayer(player).save();
	}

	public final void savePlayer(OfflinePlayer player) {
		savePlayer(player.getName());
	}

	public final void savePlayer(String name) {
		if (isLoaded(name))
			savePlayer(getPlayer(name));
	}

	public void savePlayer(T player) {
		player.save();
	}

	/**
	 * Unload and save player.
	 *
	 * @param player offlinePlayer object
	 * @see #unloadAndSavePlayer(String)
	 * @see #unloadPlayer(String)
	 * @see #savePlayer(String)
	 */
	public final void unloadAndSavePlayer(OfflinePlayer player) {
		unloadAndSavePlayer(player.getName());
	}

	/**
	 * Unload and save player.
	 *
	 * @param name the name
	 * @see #unloadPlayer(String)
	 * @see #savePlayer(String)
	 */
	public final void unloadAndSavePlayer(String name) {
		savePlayer(name);
		unloadPlayer(name);
	}

	/**
	 * Unload if player is offline.
	 * <br/>
	 * <b> WARNING IF THERE ARE PLUGINS THAT ARE IMPROPERLY CACHING THIS OBJECT. IT WILL NEVER TRUELY UNLOAD</b>
	 *
	 * @param name of the player
	 * @return true, if it unloads the player from memory.
	 */
	public boolean unloadIfOffline(String name) {
		if (isLoaded(name))
			if (getPlayer(name).getPlayer() == null || !getPlayer(name).getPlayer().isOnline()) {
				unloadAndSavePlayer(name);
				return true;
			}
		return false;
	}

	public final void unloadPlayer(OfflinePlayer player) {
		unloadPlayer(player.getName());
	}

	public void unloadPlayer(String name) {
		if (isLoaded(name))
			uid_players.remove(name);
	}
}
