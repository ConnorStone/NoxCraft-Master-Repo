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

package com.noxpvp.core.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.noxpvp.core.data.NoxPlayerAdapter;

@Deprecated
public class NoxPlayerDataEvent extends Event {
	private final NoxPlayerAdapter player;
	
	private boolean shouldHonorCore;
	
	@Deprecated
	public NoxPlayerDataEvent(NoxPlayerAdapter player, boolean honorCore)
	{
		this.player = player;
		this.shouldHonorCore = honorCore;
	}

	@Deprecated
	public OfflinePlayer getBukkitPlayer() {
		return player.getNoxPlayer().getOfflinePlayer();
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Deprecated
	public NoxPlayerAdapter getPlayer() {
		return player;
	}

	@Deprecated
	public boolean shouldHonorCore() { return shouldHonorCore; }

	@Deprecated
	private static final HandlerList handlers = new HandlerList();

	@Deprecated
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
