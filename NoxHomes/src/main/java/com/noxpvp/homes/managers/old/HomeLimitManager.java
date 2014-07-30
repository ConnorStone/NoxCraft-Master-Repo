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

package com.noxpvp.homes.managers.old;

import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.OldHomesPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.ManuelPersistent;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.OldNoxPlayerAdapter;

public class HomeLimitManager implements ManuelPersistent {
	private static HomeLimitManager instance;
	private static boolean cumulativeLimits = false;
	private static boolean superPerms = true;
	private FileConfiguration config;

	private HomeLimitManager() {
		this(NoxHomes.getInstance());
	}

	private HomeLimitManager(NoxHomes plugin) {
		config = new FileConfiguration(plugin.getDataFile("limits.yml"));
	}

	/**
	 * @return the superPerms
	 */
	public static boolean isSuperPerms() {
		return superPerms;
	}

	/**
	 * @param superPerms the superPerms to set
	 */
	public static void setSuperPerms(boolean superPerms) {
		HomeLimitManager.superPerms = superPerms;
	}

	public static HomeLimitManager getInstance() {
		if (instance == null)
			instance = new HomeLimitManager();

		return instance;
	}

	public static HomeLimitManager getInstance(NoxHomes homes) {
		if (instance == null)
			instance = new HomeLimitManager(homes);

		return instance;
	}

	public static boolean usingCumulativeLimits() {
		return cumulativeLimits;
	}

	public static void setCumulativeLimits(boolean use) {
		cumulativeLimits = use;
	}

	private static OldHomesPlayer getNoxPlayer(OldNoxPlayerAdapter adapt) {
		if (adapt instanceof OldHomesPlayer)
			return (OldHomesPlayer) adapt;
		else
			return HomesPlayerManager.getInstance().getPlayer(adapt);
	}

	private static OldHomesPlayer getNoxPlayer(String name) {
		return HomesPlayerManager.getInstance().getPlayer(name);
	}

	public boolean inRange(OfflinePlayer player, int count) {
		return inRange(player.getName(), count);
	}

	public boolean inRange(OldNoxPlayerAdapter noxplayer, int count) {
		int limit;
		limit = 0;

		OldHomesPlayer p = getNoxPlayer(noxplayer);

		limit = getLimit(p);

		if (limit <= -1)
			return true;

		return (count <= limit);
	}

	public boolean inRange(String playerName, int count) {
		return inRange(getNoxPlayer(playerName), count);
	}

	public int getLimit(String playerName) {
		return getLimit(getNoxPlayer(playerName));
	}

	public int getLimit(OldNoxPlayerAdapter p) {
		OldHomesPlayer nPlayer = getNoxPlayer(p);
		String player = nPlayer.getName();

		World world = nPlayer.getLastWorld();

		int limit = 0;

		if (!config.getNode("player").getKeys().contains(player)) {
			for (String node : config.getNode("group").getKeys())
				if (superPerms || !VaultAdapter.permission.hasGroupSupport()) {
					if (VaultAdapter.permission.has(world, player, "group." + node))
						if (cumulativeLimits)
							limit += config.get("group." + node, int.class);
						else
							limit = (limit <= config.get("group." + node, int.class) ? config.get("group." + node, int.class) : limit);
				} else if (VaultAdapter.permission.playerInGroup(world, player, node)) {
					if (cumulativeLimits)
						limit += config.get("group." + node, int.class);
					else
						limit = (limit <= config.get("group." + node, int.class) ? config.get("group." + node, int.class) : limit);
				}
		} else
			limit = config.get("player." + player, limit);

		return limit;
	}

	public boolean canAddHome(String playerName) {
		return canAddHome(getNoxPlayer(playerName));
	}

	public boolean canAddHome(OldHomesPlayer player) {
		return inRange(player, player.getHomeCount() + 1);
	}

	public void save() {
		config.set("cumulativeLimits", usingCumulativeLimits());
		config.set("superPerms", isSuperPerms());
		config.save();
	}

	public void load() {
		if (!config.exists())
			genDefault();

		config.load();
		setSuperPerms(config.get("superPerms", isSuperPerms()));
		setCumulativeLimits(config.get("cumulativeLimits", cumulativeLimits));
	}

	private void genDefault() {
		config.setHeader("This file is generated to give you an idea for syntax of setting limits.");
		config.addHeader("There are two root nodes. \"group\" and \"player\"");
		config.addHeader("Use those nodes to add limits. Below is default example.");
		config.addHeader("");
		config.addHeader("Values below 0 are infinite homes!");
		ConfigurationNode node = config.getNode("group");
		node.set("peasant", 1);
		node.set("vip", 2);
		node.set("sponsor", 3);
		node.set("king", 4);
		node.set("imperial", 5);

		node = config.getNode("player");
		node.set("coaster3000", -1);

		save();
	}
}
