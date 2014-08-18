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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.manager.MMOPlayerManager;

@SuppressWarnings("unused")
public class PlayerInteractListener extends NoxListener<NoxMMO> {
	
	MMOPlayerManager	pm;
	
	public PlayerInteractListener() {
		this(NoxMMO.getInstance());
		
	}
	
	public PlayerInteractListener(NoxMMO mmo) {
		super(mmo);
		
		pm = MMOPlayerManager.getInstance();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent e) {
		
		final Player p = e.getPlayer();
		
		// debug===========================================
		if (p.getItemInHand().getType() != Material.STICK)
			return;
		
	}
	
}
