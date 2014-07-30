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

package com.noxpvp.mmo.handlers.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.GenericMMOListener;

public class PlayerPickupItemListener extends GenericMMOListener<PlayerPickupItemEvent> {

	public PlayerPickupItemListener(NoxMMO plugin) {
		super(plugin, PlayerPickupItemEvent.class);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onLowest(PlayerPickupItemEvent event) {
		onEventLowest(event);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onLow(PlayerPickupItemEvent event) {
		onEventLow(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onNormal(PlayerPickupItemEvent event) {
		onEventNormal(event);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onHigh(PlayerPickupItemEvent event) {
		onEventHigh(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onHighest(PlayerPickupItemEvent event) {
		onEventHighest(event);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onMonitor(PlayerPickupItemEvent event) {
		onEventMonitor(event);
	}
}
