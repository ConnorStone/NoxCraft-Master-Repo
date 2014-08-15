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

package com.noxpvp.mmo.filters;

import com.bergerkiller.bukkit.common.filtering.Filter;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerClassFilter implements Filter<Player> {
	
	private final List<String>	classIds;
	
	private boolean				inverse	= false;
	
	public PlayerClassFilter(String... ids) {
		classIds = new ArrayList<String>();
		Collections.addAll(classIds, ids);
	}
	
	private static MMOPlayer getMMOPlayer(Player player) {
		return MMOPlayerManager.getInstance().getPlayer(player);
	}
	
	/**
	 * Sets the internal inversion value for the output of isFiltered
	 * 
	 * @param invert
	 *            inverse value to set to.
	 * @return self instance for chaining.
	 */
	public PlayerClassFilter invertFilter(boolean invert) {
		inverse = invert;
		return this;
	}
	
	public boolean isFiltered(Player player) {
		final MMOPlayer mPlayer = getMMOPlayer(player);
		
		final PlayerClass mainClass = mPlayer.getPrimaryClass();
		final PlayerClass subClass = mPlayer.getSecondaryClass();
		
		if (classIds.contains(mainClass.getName())
				|| classIds.contains(mainClass.getName()))
			return !inverse;
		if (classIds.contains(subClass.getName())
				|| classIds.contains(subClass.getName()))
			return !inverse;
		
		return inverse;
	}
	
	public boolean isInverse() {
		return inverse;
	}
}
