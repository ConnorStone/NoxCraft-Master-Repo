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
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.util.PlayerClassUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerClassIdentifierFilter implements Filter<Player> {

	private List<String> classIds;

	private boolean inverse = false;

	/**
	 * Sets the internal inversion value for the output of isFiltered
	 *
	 * @param invert inverse value to set to.
	 * @return self instance for chaining.
	 */
	public PlayerClassIdentifierFilter invertFilter(boolean invert) {
		inverse = invert;
		return this;
	}

	public boolean isInverse() {
		return inverse;
	}

	public PlayerClassIdentifierFilter(String... ids) {
		classIds = new ArrayList<String>();
		for (String id : ids) {
			if (PlayerClassUtil.hasClass(id))
				classIds.add(id);
		}
	}

	public boolean isFiltered(Player player) {
		MMOPlayer mPlayer = getMMOPlayer(player);

		IPlayerClass mainClass = mPlayer.getPrimaryClass();
		IPlayerClass subClass = mPlayer.getSecondaryClass();

		if (classIds.contains(mainClass.getUniqueID()) || classIds.contains(mainClass.getName()))
			return !inverse;
		if (classIds.contains(subClass.getUniqueID()) || classIds.contains(subClass.getName()))
			return !inverse;

		return inverse;
	}

	private static MMOPlayer getMMOPlayer(Player player) {
		return MMOPlayerManager.getInstance().getPlayer(player);
	}
}
