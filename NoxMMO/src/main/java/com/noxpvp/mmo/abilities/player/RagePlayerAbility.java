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

package com.noxpvp.mmo.abilities.player;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class RagePlayerAbility extends BasePlayerAbility implements PVPAbility {
	
	public static final String								ABILITY_NAME	= "Rage";
	public static final String								PERM_NODE		= "rage";
	
	private boolean											isActive		= false;
	private BaseMMOEventHandler<EntityDamageByEntityEvent>	handler;
	
	public RagePlayerAbility(@Nonnull OfflinePlayer player) {
		this(player, 5);
	}
	
	/**
	 * @param player
	 *            The user of the ability instance
	 */
	public RagePlayerAbility(@Nonnull OfflinePlayer player, double range) {
		super(ABILITY_NAME, player, range);
		
		setCD(new CoolDown.Time().seconds(75));
		
		handler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME)
						.append("EntityDamageByEntityEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			public void execute(EntityDamageByEntityEvent event) {
				if (!isActive) {
					unregisterHandler(this);
					return;
				}
				
				if (!event.getDamager().equals(RagePlayerAbility.this.getPlayer()))
					return;
				
				final double damage = event.getDamage();
				final double range = getRange();
				
				final Player attacker = (Player) event.getDamager();
				
				for (final Entity it : getPlayer().getNearbyEntities(range, range,
						range)) {
					if (!(it instanceof Damageable) || it.equals(attacker)) {
						continue;
					}
					
					final double newDamage = damage - damage / 4;
					((Damageable) it).damage(newDamage);
					StaticEffects.SkullBreak((LivingEntity) it);
					
					if (it instanceof Player) {
						final MMOPlayer mp = MMOPlayerManager.getInstance()
								.getPlayer(getPlayer());
						final NoxPlayer np = mp.getNoxPlayer();
						MessageUtil.sendLocale((Player) it,
								MMOLocale.ABIL_HIT_ATTACKER_DAMAGED,
								np.getFullName(), getDisplayName(), String.format(
										"%.2f", newDamage));
						
					}
					
				}
			}
			
			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}
			
			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}
			
			public boolean ignoreCancelled() {
				return true;
			}
		};
		
	}
	
	@Override
	public AbilityResult<RagePlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<RagePlayerAbility>(this, false);
		
		final IPlayerClass pClass = MMOPlayerManager.getInstance().getPlayer(
				getPlayer()).getPrimaryClass();
		
		final int length = Math.max(20 * 5, 20 * pClass.getTotalLevel() / 16);
		
		setActive(true);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				RagePlayerAbility.this.setActive(false);
				MessageUtil.sendLocale(getPlayer(), MMOLocale.ABIL_DEACTIVATED,
						getName());
			}
		}, length);
		
		return new AbilityResult<RagePlayerAbility>(this, true,
				MMOLocale.ABIL_ACTIVATED.get(getName()));
	}
	
	@Override
	public String getDescription() {
		return "Your axe becomes ingulfed with your own rage! Dealing 75% initial "
				+ "damage to all enemies surrounding anything you hit";
	}
	
	public void setActive(boolean isActive) {
		final boolean changed = this.isActive == isActive;
		this.isActive = isActive;
		
		if (changed)
			if (this.isActive) {
				registerHandler(handler);
			} else {
				unregisterHandler(handler);
			}
	}
	
}
