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

package com.noxpvp.mmo.abilities.ranged;

import com.noxpvp.mmo.abilities.PVPAbility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.OldMMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.locale.MMOLocale;

public class RagePlayerAbility extends BaseRangedPlayerAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Rage";
	public static final String PERM_NODE = "rage";
	
	private boolean isActive = false;
	private BaseMMOEventHandler<EntityDamageByEntityEvent> handler;
	
	public void setActive(boolean isActive) {
		boolean changed = this.isActive == isActive;
		this.isActive = isActive;
		
		if (changed)
			if (this.isActive)
				registerHandler(handler);
			else
				unregisterHandler(handler);
	}

	/**
	 * @param player The user of the ability instance
	 */
	public RagePlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range);
		
		setCD(75);

		this.handler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageByEntityEvent").toString(),
				EventPriority.MONITOR, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}

			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}

			public void execute(EntityDamageByEntityEvent event) {
				if (!RagePlayerAbility.this.isActive) {
					unregisterHandler(this);
					return;
				}
				
				if (!event.getDamager().equals(RagePlayerAbility.this.getPlayer()))
					return;

				double damage = event.getDamage();
				double range = getRange();
				
				Player attacker = (Player) event.getDamager();

				for (Entity it : getPlayer().getNearbyEntities(range, range, range)) {
					if (!(it instanceof Damageable) || it.equals(attacker)) continue;

					double newDamage = damage - (damage / 4);
					((Damageable) it).damage(newDamage);
					StaticEffects.SkullBreak((LivingEntity) it);
					
					if (it instanceof Player) {
						OldMMOPlayer mp = MMOPlayerManager.getInstance().getPlayer(getPlayer());
						MessageUtil.sendLocale((Player) it, MMOLocale.ABIL_HIT_ATTACKER_DAMAGED,
								mp.getFullName(), getDisplayName(), String.format("%.2f", newDamage));
						
					}
					
				}
			}
		};

	}

	public RagePlayerAbility(Player player) {
		this(player, 5);
	}

	@Override
	public String getDescription() {
		return "Your axe becomes ingulfed with your own rage! Dealing 75% initial "
				+ "damage to all enemies surrounding anything you hit";
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		IPlayerClass pClass = MMOPlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();

		int length = Math.max(20 * 5, (20 * (pClass.getTotalLevel())) / 16);

		setActive(true);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				RagePlayerAbility.this.setActive(false);
				MessageUtil.sendLocale(getPlayer(), MMOLocale.ABIL_DEACTIVATED, getName());
			}
		}, length);

		return new AbilityResult(this, true, MMOLocale.ABIL_ACTIVATED.get(getName()));
	}

}
