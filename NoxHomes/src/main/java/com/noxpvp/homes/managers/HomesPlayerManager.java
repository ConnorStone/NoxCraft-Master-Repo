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

package com.noxpvp.homes.managers;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.manager.BasePlayerManager;
import com.noxpvp.homes.HomesPlayer;
import com.noxpvp.homes.NoxHomes;

import java.util.logging.Level;

public class HomesPlayerManager extends BasePlayerManager<HomesPlayer> {

	//~~~~~~~~~~~~~~~~~~~~~~
	//Instance
	//~~~~~~~~~~~~~~~~~~~~~~

	private static HomesPlayerManager instance; //Instance of manager.

	public static HomesPlayerManager getInstance() {
		if (instance == null) instance = new HomesPlayerManager();
		return instance;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private HomesPlayerManager() {
		super(HomesPlayer.class, "playerdata");
	}

	//~~~~~~~~~~~~~~~~~~~~~~
	//Logging
	//~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public ModuleLogger getModuleLogger(String... moduleName) { return super.getModuleLogger(moduleName); } //protected -> public
}
