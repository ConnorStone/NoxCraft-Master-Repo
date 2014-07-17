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

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.DamageUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class DamageListener extends NoxListener<NoxMMO> {

	CorePlayerManager pm;

	public DamageListener(NoxMMO mmo) {
		super(mmo);

		this.pm = CorePlayerManager.getInstance();
	}

	public DamageListener() {
		this(NoxMMO.getInstance());
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent event) {

//		Entity e = event.getEntity();

		LivingEntity livingDamaged = DamageUtil.getDamagedEntity(event),
				livingAttacker = DamageUtil.getAttackingLivingEntity(event);

		Player playerDamaged = DamageUtil.getDamagedPlayer(event),
				playerAttacker = DamageUtil.getAttackingPlayer(event);

		if (playerAttacker != null) {

			if (livingDamaged != null) {
				MMOPlayerManager.getInstance().getPlayer(playerAttacker).setTarget(livingDamaged);

				if (event.getDamage() > 0) {
					if (getPlugin().getMMOConfig().get("effect.damage.blood", Boolean.class, Boolean.TRUE)) {
						StaticEffects.BloodEffect(livingDamaged, getPlugin());
					}
					if (getPlugin().getMMOConfig().get("effect.damage.damage-particle", Boolean.class, Boolean.TRUE)) {
						StaticEffects.DamageAmountParticle(livingDamaged, event.getDamage());
					}
				}
			}
		}
	}
}
