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

package com.noxpvp.homes.tp;

import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.homes.NoxHomes;

public class DefaultHome extends BaseHome {
	public static final String PERM_NODE = "default";

	public DefaultHome(Player player) {
		super(player);
	}

	public DefaultHome(String owner, Entity e) {
		super(owner, e);
	}

	public DefaultHome(String owner, Location location) {
		super(owner, location);
	}

	public static DefaultHome deserialize(Map<String, Object> data) {
		try {
			String owner = data.get("owner").toString();
//			SafeLocation warpPoint = SafeLocation.deserialize((Map<String, Object>) data.get("warpPoint"));
			SafeLocation warpPoint = (SafeLocation) data.get("warpPoint");
			String name = data.get("name").toString();

			if (name.equalsIgnoreCase(DefaultHome.PERM_NODE))
				return new DefaultHome(owner, warpPoint.toLocation());
		} catch (ClassCastException e) {
			NoxHomes plugin = NoxHomes.getInstance();

			plugin.log(Level.SEVERE, "Severe error occured during deserialization of Home class from config.");
			plugin.handle(e);
		} catch (Throwable e) {
			NoxHomes plugin = NoxHomes.getInstance();

			plugin.log(Level.SEVERE, "Something happened on trying to make new basehome off of config value.");
			NoxCore.getInstance().handle(e);
		}
		return null;
	}

	@Override
	protected String getNode() {
		return super.getNode() + '.' + PERM_NODE;
	}

	@Override
	protected String getOtherNode() {
		return super.getOtherNode() + '.' + PERM_NODE;
	}

	@Override
	public String getName() {
		return PERM_NODE;
	}

	@Override
	public Map<String, Object> serialize() {
		return super.serialize();
	}
}
