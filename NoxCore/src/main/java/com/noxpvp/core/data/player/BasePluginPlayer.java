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

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.Persistent;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.PluginPlayer;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.PlayerUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BasePluginPlayer<T extends NoxPlugin> implements PluginPlayer<T>, Persistent {

	//~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~
	private UUID uuid; //Player UUID

	//~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~

	public BasePluginPlayer(UUID playerUUID) {
		Validate.notNull(playerUUID);

		this.uuid = playerUUID;
	}

	public BasePluginPlayer(Player player) {
		this(player != null ? player.getUniqueId() : null);
	}

	public BasePluginPlayer(Map<String, Object> data) {
		Validate.isTrue(data.containsKey("uuid"), "Not a valid data structure. Missing uuid entry!");

		this.uuid = UUID.fromString(data.get("uuid").toString());
	}

	//~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~

	public final boolean isOnline() { return PlayerUtils.isOnline(getPlayerUUID()); }


	public final String getPlayerName() {
		return CorePlayerManager.getInstance().getPlayer(getPlayerUUID()).getPlayerName();
	}

	public final OfflinePlayer getOfflinePlayer() {
		return PlayerUtils.getOfflinePlayer(getPlayerUUID());
	}

	public final Player getPlayer() {
		return getOfflinePlayer().getPlayer();
	}

	public final NoxPlayer getNoxPlayer() {
		return CorePlayerManager.getInstance().getPlayer(getPlayerUUID());
	}

	//~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~

	public final UUID getPlayerUUID() { return uuid; }

	public final UUID getPersistentID() { return getPlayerUUID(); }

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("uuid", getPlayerUUID());

		return data;
	}


}
