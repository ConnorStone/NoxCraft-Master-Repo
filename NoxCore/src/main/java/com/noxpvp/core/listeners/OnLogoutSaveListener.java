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

package com.noxpvp.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.manager.CorePlayerManager;

public class OnLogoutSaveListener extends NoxListener<NoxCore> {

	public OnLogoutSaveListener(NoxCore plugin) {
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onLogoutEvent(PlayerQuitEvent event) {
		CorePlayerManager.getInstance().unloadAndSave(event.getPlayer().getUniqueId());
	}
}
