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

package com.noxpvp.mmo;

import java.util.HashMap;
import java.util.Map;

import com.noxpvp.core.data.OldNoxPlayer;
import com.noxpvp.core.manager.old.BasePlayerManager;

public class MMOPlayerManager extends BasePlayerManager<MMOPlayer> {
	private static MMOPlayerManager instance;

	private MMOPlayerManager() {
		super(MMOPlayer.class);
	}

	public static MMOPlayerManager getInstance() {
		if (instance == null)
			instance = new MMOPlayerManager();

		return instance;
	}

	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

	@Override
	protected MMOPlayer craftNew(OldNoxPlayer noxPlayer) {
		return new MMOPlayer(noxPlayer);
	}

	@Override
	protected Map<String, MMOPlayer> craftNewStorage() {
		return new HashMap<String, MMOPlayer>();
	}

}
