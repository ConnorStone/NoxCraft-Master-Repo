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

package com.noxpvp.core.logging;

import java.io.File;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.utils.TimeUtils;

public class PVELog extends NoxListener<NoxCore> {

	private static final String PVELog = "PVELOG";
	//	private static FileHandler handler;
//	private static Formatter formatter;
	private static ModuleLogger log;
//	private static final String newLine = System.lineSeparator();

	public PVELog(NoxCore core) {
		super(core);
	}

	public static void init() {
		final NoxCore core = NoxCore.getInstance();
//		File file = core.getDataFile("logs/" + PVELog);

		if (log == null)
			log = core.getModuleLogger(PVELog);

//		log.setUseParentHandlers(false);
//		
//		if (handler != null)
//			handler.close();
//
//		handler = null;
//		
//		formatter = new Formatter() {
//			
//			@Override
//			public String format(LogRecord record) {
//				return null;
//			}
//			
//			
//		};
		//TODO: FINISH LOGS

	}

	/**
	 * Gets the player PVELog file.
	 * <b>INTERNAL METHOD</b> Best not to use this!
	 *
	 * @param uuid The players unique id
	 * @return the players log file
	 */
	private File getPlayerPVELog(UUID uuid) {
		return new File(PVELog + File.separatorChar + uuid.toString() + ".yml");
	}

	public void log(EntityDamageEvent event) {
		Player p;

		if (true/*!(event.getEntity() instanceof Player) || (p = (Player) event.getEntity()) == null*/)
			return;

		StringBuilder log = new StringBuilder();

		String time, player, damager, damage, loc;
		UUID uuid;

		uuid = p.getUniqueId();
		time = TimeUtils.getReadableMillisTime(System.currentTimeMillis());
		player = p.getName();


		{
			EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent) ((event instanceof EntityDamageByEntityEvent) ? event : null);
			damager = event2 == null ? event.getCause().name() : (event2.getDamager() instanceof Player) ? ((Player) event2.getDamager()).getName() : event2.getDamager().getType().name();
		}

		damage = event.getDamage() + " / " + p.getHealth();

		{
			Location pLoc = p.getLocation();
			loc = "X= " + (float) pLoc.getX() + ", Y= " + (float) pLoc.getY() + ", Z= " + (float) pLoc.getZ();
		}

		log.append(player + " hit for " + damage + " by " + damager + ". At " + loc);

		this.log.info(log.toString());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		log(e);
	}
}
