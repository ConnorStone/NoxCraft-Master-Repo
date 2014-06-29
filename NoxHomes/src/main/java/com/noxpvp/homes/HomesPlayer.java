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
import com.noxpvp.core.data.player.BasePluginPlayer;
import com.noxpvp.homes.managers.HomesPlayerManager;
import com.noxpvp.homes.tp.BaseHome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class HomesPlayer extends BasePluginPlayer<NoxHomes> {

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

	//Primary Constructor
	public HomesPlayer(UUID playerUUID) {
		super(playerUUID);
	}

	public HomesPlayer(Player player) {
		this(player.getUniqueId());
	}

	public HomesPlayer(Map<String, Object> data) {
		super(data);

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
	private List<BaseHome> homes;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Implements
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}

	public void log(Level level, String msg) {
		log.log(level, msg);
	}


	}

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

	public String getPersistenceNode() {
		return "HomesPlayer";
	}
}
