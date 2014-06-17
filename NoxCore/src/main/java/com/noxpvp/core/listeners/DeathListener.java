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

import com.noxpvp.core.data.OldNoxPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.manager.old.CorePlayerManager;

public class DeathListener extends NoxListener<NoxCore> {
	private CorePlayerManager pm;

	public DeathListener() {
		this(NoxCore.getInstance());
	}

	public DeathListener(NoxCore core) {
		super(core);
		this.pm = CorePlayerManager.getInstance();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDeath(PlayerDeathEvent e) {
		OldNoxPlayer player = pm.getPlayer(e.getEntity());
		if (player == null)
			return;

		player.setLastDeath(e);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent e) {
		Player p = null;
		OldNoxPlayer np = null;
		EntityDamageEvent ede = e.getEntity().getLastDamageCause();
		if (ede instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) ede;
			if (edbe.getDamager() instanceof Projectile) {
				if (((Projectile) edbe.getDamager()).getShooter() instanceof Player) {
					p = (Player) ((Projectile) edbe.getDamager()).getShooter();
					if ((np = pm.getPlayer(p)) != null)
						np.setLastKill(e.getEntity());
				}
			}
		}
	}
}
