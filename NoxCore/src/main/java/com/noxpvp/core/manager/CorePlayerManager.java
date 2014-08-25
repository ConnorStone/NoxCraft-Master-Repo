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

import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.OfflinePlayer;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;

public class CorePlayerManager extends BasePlayerManager<NoxPlayer> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~
	// Instance
	// ~~~~~~~~~~~~~~~~~~~~~~
	
	private static CorePlayerManager	instance;	// Instance of manager.
													
	private CorePlayerManager() {
		super(NoxPlayer.class, "playerdata");
		
	}
	
	public static CorePlayerManager getInstance() {
		if (instance == null) {
			instance = new CorePlayerManager();
		}
		return instance;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~
	// Logging
	// ~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public ModuleLogger getModuleLogger(String... moduleName) {
		return super.getModuleLogger(moduleName);
	} // protected -> public
	
	// ~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~
	
	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~
	// Instanced methods
	// ~~~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public NoxPlayer load(OfflinePlayer player) {
		final NoxPlayer ret = super.load(player);
		
		ret.getStats().updateLastPlayerInfo(player);
		
		return ret;
	}
	
	@Override
	public void log(Level level, String msg) {
		getLogger().log(level, msg);
	}
	
	@Override
	public void save(OfflinePlayer player) {
		final NoxPlayer np = getIfLoaded(player.getUniqueId().toString());
		if (np == null)
			return;
		
		np.getStats().updateLastPlayerInfo(player);
		
		super.save(player);
	}
	
	@Override
	public void unloadAndSave(UUID id) { // protected -> public
		super.unloadAndSave(id);
	}
	
}
