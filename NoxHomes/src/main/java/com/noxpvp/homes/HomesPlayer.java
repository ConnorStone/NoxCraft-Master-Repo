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

package com.noxpvp.homes;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.noxpvp.core.Persistent;
import com.noxpvp.core.data.PluginPlayer;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.PlayerUtils;
import com.noxpvp.homes.managers.HomesPlayerManager;
import com.noxpvp.homes.tp.BaseHome;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class HomesPlayer implements PluginPlayer<NoxHomes> ,Persistent {

	//~~~~~~~~~~~~
	//Logging
	//~~~~~~~~~~~~
	private static ModuleLogger log;

	static {
		log = HomesPlayerManager.getInstance().getModuleLogger("HomesPlayer");
	}

	//~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~

	public HomesPlayer(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	public HomesPlayer(Player player) {
		this(player.getUniqueId());
	}

	public HomesPlayer(Map<String, Object> data) {
		Validate.isTrue(data.containsKey("uuid"), "Not a valid data structure. Missing uuid entry!");

		this.playerUUID = UUID.fromString(data.get("uuid").toString());

		//Setup Homes
		try { this.homes =(List<BaseHome>) data.get("homes"); }
		catch (Exception e) {
			if (e instanceof ClassCastException) {
				log(Level.SEVERE, "Data has been corrupted for player \"" + getPlayerUUID() + "\". Failed to deserialize a list of homes.");
				log(Level.WARNING, "Homes may have been erased as a result.");
			} else if (e instanceof NullPointerException) {
				log(Level.FINE, "There is no Homes for the player \"" + getPlayerUUID() + "\". Not entirely sure if data corruption is the cause.");
			}
			this.homes = new ArrayList<BaseHome>();

		}
	}

	//~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~
	private UUID playerUUID;
	private List<BaseHome> homes;

	//~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Implements
	//~~~~~~~~~~~~~~~~~~~~~

	public boolean isOnline() {
		return PlayerUtils.isOnline(getPlayerUUID());
	}

	public UUID getPlayerUUID() {
		return getPersistentID();
	}

	//Helper
	public String getPlayerName() {
		return CorePlayerManager.getInstance().getPlayer(getPlayerUUID()).getPlayerName();
	}

	//Helper
	public OfflinePlayer getOfflinePlayer() {
		return PlayerUtils.getOfflinePlayer(getPlayerUUID());
	}

	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}


	//~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Homes
	//~~~~~~~~~~~~~~~~~~~~~

	public List<BaseHome> getHomes() {
		return Collections.unmodifiableList(homes);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("uuid", getPlayerUUID());
		data.put("homes", getHomes());

		return data;
	}

	public void log(Level level, String msg) {

	}

	public UUID getPersistentID() {
		return playerUUID;
	}

	public String getPersistenceNode() {
		return "HomesPlayer";
	}


}
