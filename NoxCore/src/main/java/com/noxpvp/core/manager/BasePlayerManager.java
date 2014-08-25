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

import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.Persistent;
import com.noxpvp.core.data.PluginPlayer;
import com.noxpvp.core.events.uuid.NoxUUIDFoundEvent;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.reflection.NoxSafeConstructor;
import com.noxpvp.core.utils.BukkitUtil;

public abstract class BasePlayerManager<T extends PluginPlayer> extends
		BaseManager<T> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Cached Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final NoxSafeConstructor<T>		uuidConstructor,
											offlinePlayerConstructor;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Fields Automated
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final PlayerManagerAutoSaver	saver;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public BasePlayerManager(Class<T> type, String saveFolderPath) {
		super(type, saveFolderPath);
		
		uuidConstructor = new NoxSafeConstructor<T>(getTypeClass(), UUID.class);
		offlinePlayerConstructor = new NoxSafeConstructor<T>(getTypeClass(),
				OfflinePlayer.class);
		
		saver = new PlayerManagerAutoSaver();
		saver.register();
	}
	
	public BasePlayerManager(Class<T> type, String saveFolderPath,
			boolean useNoxFolder) {
		super(type, saveFolderPath, useNoxFolder);
		
		uuidConstructor = new NoxSafeConstructor<T>(getTypeClass(), UUID.class);
		offlinePlayerConstructor = new NoxSafeConstructor<T>(getTypeClass(),
				OfflinePlayer.class);
		
		saver = new PlayerManagerAutoSaver();
		saver.register();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tells whether or not the player is online.
	 * 
	 * <p>
	 * <b>Warning this will accept any Persistent object however UUID's may
	 * not match.</b>
	 * </p>
	 * <p>
	 * <b><i>It was intended to be used against any object that uses a
	 * player ID as its id!</i></b>
	 * </p>
	 * <p>
	 * Using anything other than those such objects may produce extremely
	 * undesirable behaviour
	 * </p>
	 * 
	 * @see #isOnline(java.util.UUID)
	 * @param object
	 *            Used to grab the player by the specified object's UUID
	 * @return true if online false otherwise.
	 */
	public static boolean isOnline(Persistent object) {
		return isOnline(object.getPersistentID());
	}
	
	/**
	 * Tells whether or not the player is online.
	 * 
	 * @see org.bukkit.Bukkit#getOfflinePlayer(java.util.UUID)
	 * @see org.bukkit.OfflinePlayer#isOnline()
	 * 
	 * @param uuid
	 *            uuid of the player being checked.
	 * @return true if online false otherwise.
	 */
	public static boolean isOnline(UUID uuid) {
		return Bukkit.getOfflinePlayer(uuid).isOnline();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~
	
	public T getPlayer(OfflinePlayer player) {
		return get(player.getUniqueId());
	}
	
	public PluginPlayer<?> getPlayer(PluginPlayer<?> p) {
		return getPlayer(p.getPlayerUUID());
	}
	
	public T getPlayer(UUID playerUID) {
		return get(playerUID);
	}
	
	public void load() {
		for (final Player p : BukkitUtil.getOnlinePlayers()) {
			load(p);
		}
	}
	
	public T load(OfflinePlayer player) {
		final T ret = super.load(player.getUniqueId());
		if (ret == null)
			return construct(player);
		checkConstruct(ret);
		
		if (ret != null) {
			loadedCache.put(ret.getPersistenceNode(), ret);
		}
		
		return ret;
	}
	
	public T load(UUID playerID) {
		T ret;
		if (Bukkit.getOfflinePlayer(playerID).isOnline()) {
			ret = load(Bukkit.getPlayer(playerID)); // If player is online.
													// We would rather use
													// player functions to
													// auto update data.
		} else {
			ret = super.load(playerID);
		}
		
		if (ret == null) {
			ret = construct(playerID);
		}
		checkConstruct(ret);
		
		if (ret != null) {
			loadedCache.put(ret.getPersistenceNode(), ret);
		}
		
		return ret;
	}
	
	public T loadPlayer(OfflinePlayer player) {
		return load(player);
	}
	
	public T loadPlayer(UUID playerUUID) {
		return load(playerUUID);
	}
	
	public void save(OfflinePlayer player) {
		save(player.getUniqueId());
	}
	
	public void save(UUID id) {
		if (isLoaded(id.toString())) {
			save(getIfLoaded(id.toString()));
		}
		
	}
	
	public void savePlayer(OfflinePlayer player) {
		save(player);
	}
	
	public void savePlayer(UUID playerUUID) {
		save(playerUUID);
	}
	
	public void unloadAndSave(UUID id) {
		if (isLoaded(id.toString())) {
			unloadAndSave(getIfLoaded(id.toString()));
		}
	}
	
	private void checkConstruct(T ret) {
		if (ret == null) {
			log(Level.SEVERE,
					"No constructors where found for the PluginPlayer. "
							+
							"\nAny class using PluginPlayer must follow the guidelines set in javadoc!"
							+
							"\n\n"
							+ CommonUtil.getPluginByClass(this.getClass()).getName()
							+ "Is not following the guidelines their implementation in: "
							+
							"\n\t" + this.getClass().getName());
		}
	}
	
	private T construct(OfflinePlayer player) {
		Validate.notNull(player);
		if (offlinePlayerConstructor.isValid())
			return offlinePlayerConstructor.newInstance(player);
		return construct(player.getUniqueId());
	}
	
	private T construct(UUID uuid) {
		Validate.notNull(uuid);
		if (uuidConstructor.isValid())
			return uuidConstructor.newInstance(uuid);
		return null;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Internal Classes
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private class PlayerManagerAutoSaver extends NoxListener {
		
		public PlayerManagerAutoSaver() {
			super(BasePlayerManager.this.getPlugin());
		}
		
		// TODO: Add a timer that throws save events for players while
		// online.
		
		@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
		public void onLogoutEvent(PlayerQuitEvent event) {
			unloadAndSave(event.getPlayer().getUniqueId());
		}
		
		@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
		public void onSafeUUID(NoxUUIDFoundEvent event) {
			BasePlayerManager.this.load(event.getUUID());
		}
	}
}
