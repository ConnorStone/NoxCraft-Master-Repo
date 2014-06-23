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

package com.noxpvp.noxchat;

import java.util.HashMap;
import java.util.Map;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.data.OldNoxPlayer;
import com.noxpvp.core.manager.old.BasePlayerManager;

public class PlayerManager extends BasePlayerManager<OldChatPlayer> {
	private static PlayerManager instance;
	private static ModuleLogger log;

	private PlayerManager() {
		super(OldChatPlayer.class);
	}

	public static PlayerManager getInstance() {
		if (instance == null)
			instance = new PlayerManager();
		if (log == null)
			log = instance.getPlugin().getModuleLogger("PlayerManager");

		return instance;
	}

	@Override
	protected OldChatPlayer craftNew(String name) {
		return new OldChatPlayer(name);
	}

	@Override
	protected Map<String, OldChatPlayer> craftNewStorage() {
		return new HashMap<String, OldChatPlayer>();
	}

	public NoxPlugin getPlugin() {
		return NoxChat.getInstance();
	}

	public void loadPlayer(OldNoxPlayer player) {
		getPlayer(player.getName());
	}

	@Override
	protected OldChatPlayer craftNew(OldNoxPlayer player) {
		return new OldChatPlayer(player);
	}

}
