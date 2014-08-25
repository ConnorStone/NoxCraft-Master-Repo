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

package com.noxpvp.core.internal;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.AsyncTask;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.events.CooldownExpireEvent;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.manager.CorePlayerManager;

public class CooldownHandler extends AsyncTask {
	
	private final Map<UUID, List<CoolDown>>	cds;
	private final NoxCore					core;
	private final long						delay;
	
	public CooldownHandler() {
		cds = new MapMaker().concurrencyLevel(2).makeMap();
		core = NoxCore.getInstance();
		delay = core.getCoreConfig().get("cd-handler-delay", (long) 500);
	}
	
	public synchronized void loadPlayer(NoxPlayer player) {
		cds.put(player.getPlayerUUID(), player.getCoolDowns());
	}
	
	public void loadPlayer(Player player) {
		loadPlayer(CorePlayerManager.getInstance().getPlayer(player));
	}
	
	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p/>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 * 
	 * @see Thread#run()
	 */
	public void run() {
		for (final Map.Entry<UUID, List<CoolDown>> entry : cds.entrySet()) {
			final UUID uuid = entry.getKey();
			for (final CoolDown cd : entry.getValue())
				if (cd.expired()) {
					CommonUtil.nextTick(new Runnable() {
						
						public void run() {
							final Player p = Bukkit.getPlayer(uuid);
							if (p != null) {
								CommonUtil.callEvent(new CooldownExpireEvent(p, cd
										.getName()));
							}
						}
					});
				}
		}
		
		sleep(delay);
	}
}
