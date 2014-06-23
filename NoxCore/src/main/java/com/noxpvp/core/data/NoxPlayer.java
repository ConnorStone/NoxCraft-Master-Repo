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

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.Persistent;
import com.noxpvp.core.data.player.CorePlayerStats;
import com.noxpvp.core.data.player.PlayerStats;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.gui.CoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class NoxPlayer implements PluginPlayer, Persistent {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private final UUID playerUUID;
	private WeakHashMap<String, CoolDown> cd_cache;
	private List<CoolDown> cds;
	private ConfigurationNode temp_data;
	private CoreBoard coreBoard;
	private CorePlayerStats stats;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public NoxPlayer(Player player) {
		this(player.getUniqueId());
	}

	public NoxPlayer(UUID playerUUID) {
		this.playerUUID = playerUUID;

		//Cool-Downs
		cds = new ArrayList<CoolDown>();
		cd_cache = new WeakHashMap<String, CoolDown>();

		//Temp Data
		temp_data = new ConfigurationNode();

		//Player stats
//		this.stats = new PlayerStats(getPersistantID());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Internal Methods.
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void updateCoolDownCache() {
		cd_cache.clear();

		Iterator<CoolDown> iterator = cds.iterator();
		while (iterator.hasNext()) {
			CoolDown cd = iterator.next();
			if (cd.expired())
				iterator.remove();
			else
				cd_cache.put(cd.getName(), cd);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods.
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public NoxCore getPlugin() { return NoxCore.getInstance(); }

	public UUID getPlayerUUID() { return getPersistantID(); }


	public CorePlayerStats getStats() {
		return stats;
	}

	/**
	 * {@inheritDoc}
	 * <hr/>
	 * <p>If the player is not online. It will return the last known username of the player.</p>
	 *
	 * @return current player name or last known one. If known was ever known it will return null.
	 */
	public String getPlayerName() {
		String name = null;
		if (isOnline()) name = ((Player)getOfflinePlayer()).getName();
		List<String> names = getStats().getUsedIGNs();
		if (name == null && names.size() > 0) name = names.get(0);

		return name;
	}

	public boolean isOnline() { return Bukkit.getOfflinePlayer(getPlayerUUID()).isOnline(); }

	public CoreBoard getCoreBoard() {
		return coreBoard;
	}

	public UUID getPersistantID() {
		return playerUUID;
	}

	public String getPersistanceNode() {
		return "NoxPlayer";
	}

	//----------------------------------------
	//Instanced Methods: CoolDowns System
	//----------------------------------------

	/**
	 * Retrieves an unmodifiable view of the CoolDown List.
	 *
	 * @return CoolDowns
	 */
	public List<CoolDown> getCoolDowns() {
		return Collections.unmodifiableList(cds);
	}

	/**
	 * Tells whether or not the cooldown with the specified name is currently still active.
	 *
	 * @param name cooldown name
	 * @return true if still active and false if expired or does not exist.
	 */
	public boolean isCooling(String name) {
		return !hasCoolDown(name) || !cd_cache.get(name).expired();
	}

	/**
	 * Tells whether a cooldown exists or not.
	 *
	 * @param name cooldown name
	 * @return true if cooldown exists even if its expired. false otherwise.
	 */
	public boolean hasCoolDown(String name) {
		return cd_cache.containsKey(name);
	}

	/**
	 * Adds a new cooldown with the specified name and length.
	 * <p>Adding true to coreTimer param will make it display a timer on user screen through the {@link com.noxpvp.core.gui.CoreBoard} api.</p>
	 *
	 * @param name cooldown name
	 * @param length length of cooldown.
	 * @param coreTimer display as a timer on user screen?
	 * @return true if successfully made and false otherwise.
	 */
	public boolean addCoolDown(String name, CoolDown.Time length, boolean coreTimer) {
		CoolDown cd = addCoolDown(name, length);
		if (cd != null && coreTimer) {
			ChatColor cdNameColor, cdCDColor;
			try {
				cdNameColor = ChatColor.valueOf(getPlugin().getCoreConfig().get(
						"gui.coreboard.cooldowns.name-color",
						String.class,
						"&e"));
				cdCDColor = ChatColor.valueOf(getPlugin().getCoreConfig().get(
						"gui.coreboard.cooldowns.time-color",
						String.class,
						"&a"));

			} catch (IllegalArgumentException e) {
				cdNameColor = ChatColor.YELLOW;
				cdCDColor = ChatColor.GREEN;
			}

			getCoreBoard().addTimer(
					name,
					name,
					length,
					cdNameColor,
					cdCDColor);
		}

		return cd != null;
	}

	/**
	 * Adds a new cooldown with the specified name and length.
	 *
	 * <p>To display a render of the CoolDown. Use {@link #addCoolDown(String, com.noxpvp.core.gui.CoolDown.Time, boolean)} with a true param on the final param.</p>
	 *
	 * @param name
	 * @param length
	 * @return
	 */
	public CoolDown addCoolDown(String name, CoolDown.Time length) {
		if (cd_cache.containsKey(name)) {
			CoolDown cd;
			if ((cd = cd_cache.get(name)) != null && cd.expired()) {
				cd_cache.remove(name);
				cds.remove(cd);
			} else return null;
		}
		CoolDown cd = new CoolDown(name, length);

		cds.add(cd);
		cd_cache.put(cd.getName(), cd);

		return cd;
	}

	public void removeCoolDown(String name) {
		if (!cd_cache.containsKey(name))
			return;

		CoolDown cd = cd_cache.get(name);

		cds.remove(cd);
		cd_cache.remove(name);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced: Helpers
	//~~~~~~~~~~~~~~~~~~~~~~~~~~

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPlayerUUID());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("uuid", getPersistantID().toString());
		data.put("cool-downs", cds);
		data.put(stats.getPersistanceNode(), stats);

		return data;
	}

	public static NoxPlayer valueOf(Map<String, Object> data) {
		return new NoxPlayer(UUID.fromString((String) data.get("uuid")));
	}
}
