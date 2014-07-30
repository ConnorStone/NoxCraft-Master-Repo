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

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.manager.CorePlayerManager;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;
import java.util.UUID;

@SerializableAs("HomeLimit")
@DelegateDeserialization(BaseHomeLimit.class)
public class GroupHomeLimit extends BaseHomeLimit {

	private final String groupName;
	private final String permNode;

	public GroupHomeLimit(UUID uuid, String groupName, int limit) {
		super(uuid, limit);
		this.groupName = groupName;

		this.permNode = "group." + this.groupName;
	}

	public GroupHomeLimit(Map<String, Object> data) {
		super(data);
		Validate.isTrue(data.containsKey("group-id"), "Data does not contain a group id!");
		this.groupName = data.get("group-id").toString();

		this.permNode = "group." + this.groupName;
	}

	public boolean canLimit(OfflinePlayer player) {
		if (VaultAdapter.permission.hasGroupSupport()) {
			if (player.isOnline()) return VaultAdapter.permission.playerInGroup(player.getPlayer(), getGroupID());
			else return VaultAdapter.permission.playerInGroup(CorePlayerManager.getInstance().getPlayer(player.getUniqueId()).getStats().getLastWorld(), player.getUniqueId(), getGroupID());
		} else {
			if (player.isOnline()) return VaultAdapter.permission.has(player.getPlayer(), permNode);
			else return VaultAdapter.permission.playerHas(CorePlayerManager.getInstance().getPlayer(player.getUniqueId()).getStats().getLastWorld(), player.getUniqueId(), getGroupID());
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~~

	public String getPersistenceNode() {
		return "groups";
	}

	public Map<String, Object> serialize() {
		Map<String, Object> data = super.serialize();

		data.put("group-id", getGroupID());

		return data;
	}

	public String getGroupID() {
		return groupName;
	}
}
