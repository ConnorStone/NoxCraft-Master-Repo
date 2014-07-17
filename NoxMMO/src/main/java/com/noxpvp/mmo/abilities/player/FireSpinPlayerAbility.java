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
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.effect.vortex.BaseVortex;
import com.noxpvp.core.effect.vortex.BaseVortexEntity;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class FireSpinPlayerAbility extends BasePlayerAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Fire Spin";
	public static final String PERM_NODE = "fire-spin";
	
	private BaseMMOEventHandler<EntityChangeBlockEvent> handler;
	private int time;
	private boolean active;
	private FireSpinPlayerAbility.FireSpinVortex vortex;
	
	private void setActive(boolean active) {
		boolean changed = this.active != active;
		this.active = active;
		
		if (changed) {
			if (active)
				registerHandler(handler);
			else
				unregisterHandler(handler);
		}
	}

	public FireSpinPlayerAbility(Player p) {
		super(ABILITY_NAME, p);

		this.time = 20 * 10;
		setCD(25);
		
		this.handler = new BaseMMOEventHandler<EntityChangeBlockEvent>(
				new StringBuilder().append(getName()).append(p.getName()).append("EntityChangeBlockEvent").toString(),
				EventPriority.NORMAL, 1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<EntityChangeBlockEvent> getEventType() {
				return EntityChangeBlockEvent.class;
			}
			
			public String getEventName() {
				return "EntityChangeBlockEvent";
			}
			
			public void execute(EntityChangeBlockEvent event) {
				if (!(event.getEntity() instanceof FallingBlock))
					return;
				
				if (vortex != null && event.getEntity().getMetadata(BaseVortexEntity.uniqueMetaKey).contains(vortex.hashCode()))
					event.setCancelled(true);
			}
		};
	}

	@Override
	public String getDescription() {
		return "Surrounds the user with a powerful ring of spinning fire, Scorching anyone in your path";
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		setActive(true);
		this.vortex = new FireSpinVortex(getPlayer(), time);
		this.vortex.start();
		
		getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, time, 0, true));
		
		return new AbilityResult(this, true);

	}

	private class FireSpinVortex extends BaseVortex {

		public FireSpinVortex(Player user, int time) {
			super(user, user.getLocation(), time);

			setWidth(1.5);
			setHeightGain(0.17);
			setMaxSize(50);
			setSpeed(3);

			for (int i = 0; i < 50; i++) {
				addEntity(new FireSpinVortexEntity(this));
			}
		}

		public NoxPlugin getPlugin() {
			return NoxMMO.getInstance();
		}

		public void onRun() {
			setLocation((getUser().getLocation()));

			// Spawns 10 fire per run
			for (int i = 0; i < 5; i++) {
				addEntity(new FireSpinVortexEntity(this));

			}

			// Make all entities in the list spin, and set all the things on fire
			ArrayDeque<BaseVortexEntity> que = new ArrayDeque<BaseVortexEntity>();

			for (BaseVortexEntity ve : getEntities()) {
				if (ve == null)
					continue;

				HashSet<? extends BaseVortexEntity> new_entities = ve.tick();
				if (new_entities == null || new_entities.size() < 1)
					continue;

				for (BaseVortexEntity temp : new_entities) {
					que.add(temp);
				}
			}

			// Add the new entities, if any
			for (BaseVortexEntity vb : que) {
				addEntity(vb);
			}
		}
		
		@Override
		public void onStop() {
			setActive(false);
		}

	}

	private class FireSpinVortexEntity extends BaseVortexEntity {

		public FireSpinVortexEntity(FireSpinVortex parent) {
			super(parent, parent.getLocation(),
					parent.getLocation().getWorld().spawnFallingBlock(parent.getLocation(), Material.FIRE, (byte) 0));

			new ParticleRunner(ParticleType.dripLava, getEntity(), false, 0, 1, 0).start(0, parent.getSpeed() + 2);
		}

		public FireSpinVortexEntity(BaseVortex parent, Location loc, Entity base) {
			super(parent, loc, base);

		}

		public boolean onRemove() {
			return getEntity() instanceof FallingBlock;

		}

		public boolean onCreation() {
			return true;
		}

		public HashSet<? extends BaseVortexEntity> onTick() {
			Player user = getParent().getUser();

			for (Entity it : getEntity().getNearbyEntities(1, 1, 1)) {
				if (!(it instanceof Damageable))
					continue;

				if (isVortexEntity(it) || it.equals(user))
					continue;

				if (it.getFireTicks() < 50)
					it.setFireTicks(100);
			}

			return null;

		}

	}

}
