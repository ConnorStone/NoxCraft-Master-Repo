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

package com.noxpvp.core.manager;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.logging.Level;

public class CorePlayerManager extends BasePlayerManager<NoxPlayer>{

	//~~~~~~~~~~~~~~~~~~~~~~
	//Instance
	//~~~~~~~~~~~~~~~~~~~~~~

	public static CorePlayerManager getInstance() {
		if (instance == null) instance = new CorePlayerManager();
		return instance;
	}

	private static CorePlayerManager instance; //Instance of manager.

	//~~~~~~~~~~~~~~~~~~~~~~
	//Logging
	//~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public ModuleLogger getModuleLogger(String... moduleName) { return super.getModuleLogger(moduleName);} //protected -> public

	public void log(Level level, String msg) {
		getLogger().log(level, msg);
	}

	//~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~

	private CorePlayerManager() {
		super(NoxPlayer.class, "playerdata");

	}

	//~~~~~~~~~~~~~~~~~~~~~~
	//Instanced methods
	//~~~~~~~~~~~~~~~~~~~~~~


	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}

	@Override
	public void unloadAndSave(UUID id) { //protected -> public
		super.unloadAndSave(id);
	}

	@Override
	public NoxPlayer load(OfflinePlayer player) {
		NoxPlayer ret = super.load(player);

		ret.getStats().updateLastPlayerInfo(player);

		return ret;
	}

	@Override
	public void save(OfflinePlayer player) {
		NoxPlayer np = getIfLoaded(player.getUniqueId());
		if (np == null) return;

		np.getStats().updateLastPlayerInfo(player);

		super.save(player);
	}

}
