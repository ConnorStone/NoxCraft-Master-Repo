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

package com.noxpvp.mmo.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class HealthBar {
	private static Map<String, HealthBar> bars;
	private static Scoreboard sb;
	private static Objective ob;
	private static BaseMMOEventHandler<EntityRegainHealthEvent> healHandler;
	private static BaseMMOEventHandler<EntityDamageEvent> damageHandler;
	
	static {
		bars = new HashMap<String, HealthBar>();
		sb = Bukkit.getScoreboardManager().getMainScoreboard();
		ob = (ob = sb.getObjective("HealthBar")) != null? ob : sb.registerNewObjective("HealthBar", "dummy");
		ob.setDisplayName(ChatColor.RED.toString() + "❤");
		ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		healHandler = new BaseMMOEventHandler<EntityRegainHealthEvent>(
				new StringBuilder("HealthBar").append("EntityRegainHealthEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<EntityRegainHealthEvent> getEventType() {
				return EntityRegainHealthEvent.class;
			}
			
			public String getEventName() {
				return "EntityRegainHealthEvent";
			}
			
			public void execute(EntityRegainHealthEvent event) {
				HealthBar b;
				Entity e = event.getEntity();
				String key = (String) (e instanceof Player? ((Player) e).getName() : Integer.toString(e.getEntityId()));
				
				if ((b = bars.get(key)) == null || !(e instanceof LivingEntity))
					return;
				
				updateBar(b);
			}
		};
		
		damageHandler = new BaseMMOEventHandler<EntityDamageEvent>(
				new StringBuilder().append("HealthBar").append("EntityDamageEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<EntityDamageEvent> getEventType() {
				return EntityDamageEvent.class;
			}
			
			public String getEventName() {
				return "EntityDamageEvent";
			}
			
			public void execute(EntityDamageEvent event) {
				HealthBar b;
				Entity e = event.getEntity();
				String key = (String) (e instanceof Player? ((Player) e).getName() : Integer.toString(e.getEntityId()));
				
				if ((b = bars.get(key)) == null || !(e instanceof LivingEntity))
					return;
				
				updateBar(b);
			}
		};
		
		NoxMMO.getInstance().getMasterListener().registerHandler(healHandler);
		NoxMMO.getInstance().getMasterListener().registerHandler(damageHandler);
	}
	
	public static HealthBar getHealthBar(Player p) {
		HealthBar b = bars.get(p.getName());
		if (b == null)
			return null;
		
		return b;
	}
	
	public static void updateBar(final HealthBar b) {
		CommonUtil.nextTick(new Runnable() {
			
			public void run() {
				b.update();
				
			}
		});
	}
	
	private LivingEntity e;
	
	public HealthBar(LivingEntity e) {
		HealthBar b;
		if ((b = bars.get(Integer.toString(e.getEntityId()))) != null && b.e.isValid()) {
			b.update();
			return;
		}
		
		this.e = e;
		bars.put(Integer.toString(e.getEntityId()), this);
		update();
	}
	
	public HealthBar(Player p) {
		HealthBar b;
		if ((b = bars.get(p.getName())) != null && b.e.isValid()) {
			b.update();
			return;
		}
		
		this.e = p;
		bars.put(p.getName(), this);
		update();
	}
	
	public void update() {
		String key = e instanceof Player? ((Player) e).getName() : Integer.toString(e.getEntityId());
		Score s;
		
		if ((s = ob.getScore(key)) != null) {
			s.setScore((int) e.getHealth());
		}
		
	}

}
