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

package com.noxpvp.mmo.manager;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.manager.BasePlayerManager;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.logging.Level;

public class MMOPlayerManager extends BasePlayerManager<MMOPlayer> {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instance
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static MMOPlayerManager instance;

	public static MMOPlayerManager getInstance() {
		if (instance == null) instance = new MMOPlayerManager();
		return instance;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private MMOPlayerManager() {
		super(MMOPlayer.class, "playerdata");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Logging
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public ModuleLogger getModuleLogger(String... moduleName) { return super.getModuleLogger(moduleName);} //protected -> public

	public void log(Level level, String msg) {
		getLogger().log(level, msg);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

	@Override
	public void unloadAndSave(UUID id) {
		super.unloadAndSave(id);
	} //protected -> public

	@Override
	public void save(OfflinePlayer player) {
		MMOPlayer np = getIfLoaded(player.getUniqueId());
		if (np == null) return;

		super.save(player);
	}
}
