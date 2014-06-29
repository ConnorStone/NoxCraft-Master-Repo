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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.OldHomesPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.data.OldNoxPlayer;
import com.noxpvp.core.manager.old.BasePlayerManager;
import com.noxpvp.homes.tp.BaseHome;

public class HomesPlayerManager extends BasePlayerManager<OldHomesPlayer> { //FIXME: Javadocs
	private static HomesPlayerManager instance;

	private HomesPlayerManager() {
		super(OldHomesPlayer.class);
	}

	public static HomesPlayerManager getInstance() {
		if (instance == null)
			instance = new HomesPlayerManager();

		return instance;
	}

	public void addHome(BaseHome home) {
		String owner = home.getOwner();
		getPlayer(owner).addHome(home);
	}

	public void addHomes(List<BaseHome> homes) {
		if (LogicUtil.nullOrEmpty(homes))
			return;

		for (BaseHome home : homes)
			addHome(home);
	}

	/**
	 * Erases all home data on every player.
	 */
	public void clear() {

		com.noxpvp.core.manager.old.CorePlayerManager pm = com.noxpvp.core.manager.old.CorePlayerManager.getInstance();
		List<String> names = pm.getAllPlayerNames();

		for (OldHomesPlayer player : getPlayerMap().values()) {
			if (!pm.isLoaded(player.getName())) {
				names.add(player.getName());
				continue;
			}
			player.setHomes(null);
		}

		names.removeAll(new ArrayList<String>(getPlayerMap().keySet()));
		for (String name : names) {
			boolean notMem = !pm.isLoaded(name);
			OldNoxPlayer np = pm.getPlayer(name);
			np.getPersistantData().remove("homes");
			np.save();

			if (notMem)
				pm.unloadAndSavePlayer(name);
		}
	}

	@Override
	protected OldHomesPlayer craftNew(OldNoxPlayer noxPlayer) {
		return new OldHomesPlayer(noxPlayer);
	}

	@Override
	protected OldHomesPlayer craftNew(String name) {
		return new OldHomesPlayer(name);
	}

	@Override
	protected Map<String, OldHomesPlayer> craftNewStorage() {
		return new HashMap<String, OldHomesPlayer>();
	}

	public BaseHome getHome(OldHomesPlayer player, String homeName) {
		return player.getHome(homeName);
	}

	/**
	 * Retrieves the named home of the named owner
	 *
	 * @param owner name
	 * @param name  The name of the home or null for default home
	 * @return home of type BaseHome or null if none exist with that name.
	 */
	public BaseHome getHome(String owner, String name) {
		return getHome(getPlayer(owner), name);
	}

	public List<BaseHome> getHomes(OfflinePlayer player) {
		return getHomes(player.getName());
	}

	public List<BaseHome> getHomes(String player) {
		return getPlayer(player).getHomes();
	}

	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}

	public boolean hasData(OfflinePlayer player) {
		return hasData(player.getName());
	}

	public boolean hasData(String player) {
		return getPlayer(player).hasHomes();
	}

	public void load() {
		getPlayerMap().clear();
		for (Player player : Bukkit.getOnlinePlayers())
			getPlayer(player);
	}

	public void loadPlayer(OldNoxPlayer player) {
		getPlayer(player).load();
	}

	public boolean removeHome(BaseHome home) {
		String owner = home.getOwner();
		return getPlayer(owner).removeHome(home);
	}

	public void save() {
		for (OldHomesPlayer player : getPlayerMap().values())
			player.save();
	}

}
