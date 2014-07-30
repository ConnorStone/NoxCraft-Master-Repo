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

package com.noxpvp.homes.limits;

import com.noxpvp.homes.managers.HomeLimitManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

@SerializableAs("HomeLimit")
@DelegateDeserialization(BaseHomeLimit.class)
public class BackupHomeLimit implements HomeLimit {

	private final UUID uuid;
	private Map<String, Object> data;

	protected BackupHomeLimit(Map<String, Object> data) {

		UUID id;
		try { id = UUID.fromString(data.get("uuid").toString()); } catch (Exception e) {
			id = UUID.randomUUID();
			if (e instanceof NullPointerException) {
				log(Level.INFO, "No ID present for data. Generating new UUID");
			} else if (e instanceof IllegalArgumentException) {
				log(Level.WARNING, "UUID for limit was malformed... Generating new UUID");
			}
			data.put("uuid", id);
		}
		this.uuid = id;
		this.data = data;
	}

	public boolean canLimit(OfflinePlayer player) {
		return false;
	}

	public void log(Level level, String msg) {
		HomeLimitManager.getInstance().log(level, "BackupHomeLimit: " + msg);
	}

	public UUID getPersistentID() {
		return uuid;
	}

	public String getPersistenceNode() {
		return "";
	}

	/**
	 * Returns the emergency backup.
	 * @return
	 */
	public Map<String, Object> serialize() { return data; }

	public boolean isPastLimit(Player player) { return false; }

	public boolean isPastLimit(UUID uuid) { return false; }

	public int getLimit() { return 0; }

	public int getPriority() { return 0; }

	public boolean isCumulative() { return false; }
}
