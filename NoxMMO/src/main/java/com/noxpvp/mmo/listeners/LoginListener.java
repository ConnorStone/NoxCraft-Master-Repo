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

package com.noxpvp.mmo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.gui.HealthBar;

public class LoginListener extends NoxListener<NoxMMO> {

	public LoginListener(NoxMMO plugin) {
		super(plugin);
	}

	public LoginListener() {
		super(NoxMMO.getInstance());
	}

	public void onLogin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();

		new HealthBar(player);
	}

}
