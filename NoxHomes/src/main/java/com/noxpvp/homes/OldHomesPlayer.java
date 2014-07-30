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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.noxpvp.core.data.OldBaseNoxPlayerAdapter;
import com.noxpvp.core.data.OldNoxPlayerAdapter;
import org.bukkit.OfflinePlayer;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.core.ManuelPersistent;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;

public class OldHomesPlayer extends OldBaseNoxPlayerAdapter implements ManuelPersistent {

	/**
	 * Instantiates a new homes playerRef.
	 *
	 * @param playerName the name of the playerRef.
	 */
	public OldHomesPlayer(String playerName) {
		super(playerName);
	}

	/**
	 * Instantiates a new homes playerRef.
	 *
	 * @param player an OfflinePlayer.
	 */
	public OldHomesPlayer(OfflinePlayer player) {
		super(player);
	}

	public OldHomesPlayer(OldNoxPlayerAdapter player) {
		super(player);
	}

	/**
	 * Gets the list of homes.
	 *
	 * @return homes List
	 */
	public List<BaseHome> getHomes() {
		List<BaseHome> homes = new ArrayList<BaseHome>();
		ConfigurationNode data = getPersistantData().getNode("homes");
		for (String node : data.getKeys())
			homes.add(data.get(node, BaseHome.class));
		return Collections.unmodifiableList(homes);
	}

	/**
	 * Sets the homes.
	 * <br/>
	 * Saves data after completion.
	 *
	 * @param list the replacement list of homes.
	 */
	public final void setHomes(List<BaseHome> list) {
		if (list == null)
			list = new ArrayList<BaseHome>();
		ConfigurationNode node = getPersistantData().getNode("homes");

		node.clear();
		for (BaseHome home : list)
			node.set(home.getName(), home);

		saveToManager();
	}

	public int getHomeCount() {
		return getPersistantData().getNode("homes").getKeys().size();
	}

	/**
	 * Gets the home names.
	 *
	 * @return the home names
	 */
	public List<String> getHomeNames() {
		List<String> names = new ArrayList<String>();
		for (BaseHome home : getHomes())
			names.add(home.getName());
		return Collections.unmodifiableList(names);
	}

	/**
	 * Checks for homes.
	 *
	 * @return true, if successful
	 */
	public boolean hasHomes() {
		return getHomeCount() > 0;
	}

	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#load()
	 */
	public void load() {
		//Data is already preloaded.
	}

	/**
	 * Adds the home to data.
	 * <br/>
	 * Saves data after completion.
	 *
	 * @param home of type BaseHome to add.
	 */
	public void addHome(BaseHome home) {
		ConfigurationNode node = getPersistantData().getNode("homes");
		node.set(home.getName(), home);
		saveToManager();
	}

	/**
	 * Removes the home from data.
	 * <br/>
	 * Saves data after completion.
	 *
	 * @param home to remove.
	 * @return true if successful.
	 */
	public boolean removeHome(BaseHome home) {
		ConfigurationNode node = getPersistantData().getNode("homes");

		node.remove(home.getName());
		saveToManager();
		return !node.contains(home.getName());
	}

	/**
	 * Gets the specified home.
	 *
	 * @param name of the home
	 * @return the home
	 */
	public BaseHome getHome(String name) {
		if (name == null)
			return getHome(DefaultHome.PERM_NODE);

		return getPersistantData().get("homes." + name, BaseHome.class);
	}

	@Override
	public void save() {
		//Data is automatically saved on edit...
	}

}
