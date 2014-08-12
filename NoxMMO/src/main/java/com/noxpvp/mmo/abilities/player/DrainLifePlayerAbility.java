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

import java.util.ArrayDeque;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.internal.DamagingAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class DrainLifePlayerAbility extends BasePlayerAbility implements
		DamagingAbility {
	
	public static final String	ABILITY_NAME	= "Drain Life";
	public static final String	PERM_NODE		= "drain-life";
	
	private final int			time;
	private final int			period;
	private double				damage;
	
	public DrainLifePlayerAbility(Player p) {
		this(p, 10);
	}
	
	public DrainLifePlayerAbility(Player p, double range) {
		super(ABILITY_NAME, p, range, MMOPlayerManager.getInstance().getPlayer(p)
				.getTarget());
		
		time = 20 * 10;
		period = 20;
		setDamage(1);
	}
	
	@Override
	public TargetedAbilityResult<DrainLifePlayerAbility> execute() {
		if (!mayExecute())
			return new TargetedAbilityResult<DrainLifePlayerAbility>(this, false);
		
		new DrainingLifePipe(getPlayer(), getTarget(), time).start(0);
		
		return new TargetedAbilityResult<DrainLifePlayerAbility>(this, true);
		
	}
	
	public double getDamage() {
		return damage;
	}
	
	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	private class DrainingLifePipe extends BukkitRunnable {
		
		private final ArrayDeque<heart>	ents;
		private final Player			player;
		private final LivingEntity		target;
		
		private Location				pLoc, tLoc;
		
		private int						counter;
		private final int				time;
		private final int				speed;
		
		public DrainingLifePipe(Player p, LivingEntity target, int time) {
			player = p;
			pLoc = p.getLocation();
			this.target = target;
			tLoc = target.getLocation();
			ents = new ArrayDeque<heart>();
			this.time = time;
			counter = 0;
			speed = 2;
		}
		
		public boolean mayRun() {
			return getTarget() != null && getDistance() <= getRange();
		}
		
		public Hologram newHeart() {
			return HoloAPI.getManager().createSimpleHologram(tLoc, 2,
					ChatColor.RED + "♥");
		}
		
		public void run() {
			if (!mayRun()) {
				stop();
			}
			
			pLoc = player.getEyeLocation().add(0, -1, 0);
			tLoc = target.getLocation().add(0, .50, 0);
			
			if (counter % period == 0 && target.getHealth() > getDamage()
					&& player.getHealth() < player.getMaxHealth()) {
				target.setHealth(Math.max(target.getHealth() - getDamage(), 0));
				player.setHealth(Math.min(player.getHealth() + getDamage(), player
						.getMaxHealth()));
			}
			
			ents.add(new heart(tLoc));
			
			while (ents.size() > 50) {
				final heart first = ents.getFirst();
				ents.remove(first);
				first.remove();
			}
			
			for (final heart heart : ents) {
				if (heart.loc.distance(player.getLocation()) < 1) {
					heart.remove();
					continue;
				}
				
				final Vector dir = pLoc.toVector().subtract(heart.loc.toVector())
						.normalize().multiply(0.5);
				heart.tick(dir);
			}
			
			counter += speed;
			
		}
		
		public void start(int delay) {
			runTaskTimer(NoxMMO.getInstance(), delay, speed);
			
			Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
				
				public void run() {
					stop();
				}
			}, time);
		}
		
		public void stop() {
			for (final heart h : ents) {
				h.remove();
			}
			
			ents.clear();
			
			try {
				cancel();
				return;
			} catch (final IllegalStateException e) {
			}
		}
		
		private class heart {
			
			Location	loc;
			Hologram	h;
			
			public heart(Location loc) {
				this.loc = loc;
				h = newHeart();
			}
			
			public void remove() {
				h.clearAllPlayerViews();
				ents.remove(h);
			}
			
			public void tick(Vector direction) {
				loc.add(direction);
				new ParticleRunner(ParticleType.happyVillager, loc, false, 0, 1, 1)
						.start(0);
				h.move(loc);
			}
		}
		
	}
	
}
