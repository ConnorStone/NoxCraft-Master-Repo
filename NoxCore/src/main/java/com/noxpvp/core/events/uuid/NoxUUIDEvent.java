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

package com.noxpvp.core.events.uuid;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NoxUUIDEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final String username;
	private final UUID uuid;

	public NoxUUIDEvent(String username, UUID uuid) {
		super(!Bukkit.isPrimaryThread());
		this.username = username;
		this.uuid = uuid;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public UUID getUUID() {
		return uuid;
	}

}
