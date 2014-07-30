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

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.packet.PacketSounds;
import com.noxpvp.mmo.NoxMMO;

public class HealListener extends NoxListener<NoxMMO> {

	public HealListener(NoxMMO plugin) {
		super(plugin);

	}

	public HealListener() {
		this(NoxMMO.getInstance());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onHeal(EntityRegainHealthEvent event) {

		double amount = event.getAmount();
		final LivingEntity healed;

		//return unless a living entity
		if (!(event.getEntity() instanceof LivingEntity) || (healed = (LivingEntity) event.getEntity()) == null)
			return;

		{
			//figure out if the entity was healed, and if it was too much
			double damage = healed.getMaxHealth() - healed.getHealth();
			amount = damage < amount ? damage : amount;
		}

		//not actually gaining health
		if (amount <= 0)
			return;

		if (getPlugin().getMMOConfig().get("effect.heal.heart", Boolean.class, Boolean.TRUE)) {
			StaticEffects.HeartEffect(healed, amount, getPlugin());
		}

		if (getPlugin().getMMOConfig().get("effect.heal.heal-amount-particle", Boolean.class, Boolean.TRUE)) {
			StaticEffects.HealAmountParticle(healed, amount);
		}

		if (healed instanceof Player && event.getRegainReason() == RegainReason.CUSTOM)
			for (int i = 0; i < amount; i++) {
				Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {

					public void run() {
						StaticEffects.playSound((Player) healed, PacketSounds.LiquidLavaPop, 2, 3);
					}
				}, i);
			}
	}

}
