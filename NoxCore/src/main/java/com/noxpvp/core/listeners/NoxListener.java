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

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.noxpvp.core.NoxPlugin;

public abstract class NoxListener<T extends NoxPlugin> implements Listener {
	private T plugin;
	private boolean isRegistered;

	public NoxListener(T plugin) {
		this.plugin = plugin;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public T getPlugin() {
		return this.plugin;
	}

	public void register() {
		if (isRegistered)
			return;
		plugin.register(this);
		isRegistered = true;
	}

	public void unregister() {
		HandlerList.unregisterAll(this);
		isRegistered = false;
	}
}
