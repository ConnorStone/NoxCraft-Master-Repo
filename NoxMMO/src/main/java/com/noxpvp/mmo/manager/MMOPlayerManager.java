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

import java.util.logging.Level;

import org.bukkit.OfflinePlayer;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.manager.BasePlayerManager;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;

public class MMOPlayerManager extends BasePlayerManager<MMOPlayer> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static MMOPlayerManager			instance;
	
	private static final String				saveFolder	= "playerdata";
	private static final Class<MMOPlayer>	saveType	= MMOPlayer.class;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private MMOPlayerManager() {
		super(saveType, saveFolder);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static MMOPlayerManager getInstance() {
		if (instance == null) {
			instance = new MMOPlayerManager();
		}
		return instance;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public ModuleLogger getModuleLogger(String... moduleName) {
		return super.getModuleLogger(moduleName);
	}
	
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
	
	@Override
	public void log(Level level, String msg) {
		getLogger().log(level, msg);
	}
	
	@Override
	public void save(OfflinePlayer player) {
		final MMOPlayer np = getIfLoaded(player.getUniqueId().toString());
		if (np == null)
			return;
		
		super.save(player);
	}
	
}
