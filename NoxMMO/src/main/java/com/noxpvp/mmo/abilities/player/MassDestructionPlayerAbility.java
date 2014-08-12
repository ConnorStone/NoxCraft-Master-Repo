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

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.internal.DamagingAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.ExpandingDamageRunnable;
import com.noxpvp.mmo.runnables.SetVelocityRunnable;
import com.noxpvp.mmo.runnables.ShockWaveAnimation;

/**
 * @author NoxPVP
 */
public class MassDestructionPlayerAbility extends BasePlayerAbility implements
		PVPAbility, DamagingAbility {
	
	public static final String						ABILITY_NAME	= "Mass Destruction";
	public static final String						PERM_NODE		= "mass-destruction";
	
	private BaseMMOEventHandler<EntityDamageEvent>	handler;
	private double									hVelo			= 1.5;
	private boolean									isActive;
	private double									damage;
	
	public MassDestructionPlayerAbility(OfflinePlayer p) {
		this(p, 10);
	}
	
	/**
	 * @param p
	 *            The Player type user for this instance
	 */
	public MassDestructionPlayerAbility(OfflinePlayer p, double range) {
		super(ABILITY_NAME, p, range);
		
		isActive = false;
		setDamage(5);
		setCD(new CoolDown.Time().seconds(45));
		
		handler = new BaseMMOEventHandler<EntityDamageEvent>(
				new StringBuilder().append(p.getName()).append(ABILITY_NAME).append(
						"EntityDamageEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			public void execute(EntityDamageEvent event) {
				if (event.getCause() != DamageCause.FALL
						|| event.getEntityType() != EntityType.PLAYER)
					return;
				
				final Player p = (Player) event.getEntity();
				
				if (p.getName() == MassDestructionPlayerAbility.this.getPlayer()
						.getName()
						&& isActive) {
					event.setCancelled(true);
					MassDestructionPlayerAbility.this
							.eventExecute(event.getDamage());
				}
			}
			
			public String getEventName() {
				return "EntityDamageEvent";
			}
			
			public Class<EntityDamageEvent> getEventType() {
				return EntityDamageEvent.class;
			}
			
			public boolean ignoreCancelled() {
				return false;
			}
		};
		
	}
	
	public void eventExecute(double d) {
		
		final Player p = getPlayer();
		final Location pLoc = p.getLocation();
		
		final int range = (int) Math.ceil(getRange());
		
		new ParticleRunner(ParticleType.largeexplode, pLoc.clone().add(0, 1, 0),
				true, 10, 3, 1).start(0);
		new ShockWaveAnimation(pLoc, 1, range, 0.35, true).start(0);
		new ExpandingDamageRunnable(p, pLoc, d, range, 1).start(0);
		
		setActive(false);
		
	}
	
	@Override
	public AbilityResult<MassDestructionPlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<MassDestructionPlayerAbility>(this, false);
		
		final NoxMMO instance = NoxMMO.getInstance();
		
		final Player p = getPlayer();
		
		final Vector up = p.getLocation().getDirection();
		up.setY(gethVelo());
		final Vector down = p.getLocation().getDirection();
		down.setY(-gethVelo() * 2);
		
		final SetVelocityRunnable shootUp = new SetVelocityRunnable(getPlayer(), up);
		final SetVelocityRunnable shootDown = new SetVelocityRunnable(getPlayer(),
				down);
		
		shootUp.runTask(instance);
		shootDown.runTaskLater(instance, 20);
		
		setActive(true);
		
		return new AbilityResult<MassDestructionPlayerAbility>(this, true);
	}
	
	public double getDamage() {
		return damage;
	}
	
	@Override
	public String getDescription() {
		return "You leap high into the air causing the ground to shake when you land, dealing out your fall damage"
				+ " to all enemys within " + getRange() + " blocks";
	}
	
	/**
	 * @return Double The current set velocity used for the player
	 *         upwards/downwards effect
	 */
	public double gethVelo() {
		return hVelo;
	}
	
	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	/**
	 * @param velo
	 *            Double velocity value for player upwards/downwards effect
	 * @return MassDestructionAbility This instance, used for chaining
	 */
	public MassDestructionPlayerAbility sethVelo(double velo) {
		hVelo = velo;
		return this;
	}
	
	private MassDestructionPlayerAbility setActive(boolean active) {
		final boolean changed = isActive != active;
		isActive = active;
		
		if (changed)
			if (active) {
				registerHandler(handler);
			} else {
				unregisterHandler(handler);
			}
		
		return this;
	}
}
