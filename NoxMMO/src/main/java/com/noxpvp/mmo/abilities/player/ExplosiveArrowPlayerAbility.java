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

import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.locale.MMOLocale;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.ArrayList;
import java.util.List;

public class ExplosiveArrowPlayerAbility extends BasePlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "explosive-arrow";
	private static final String ABILITY_NAME = "Explosive Arrow";
	List<Arrow> arrows;
	private BaseMMOEventHandler<ProjectileHitEvent> hitHandler;
	private BaseMMOEventHandler<ProjectileLaunchEvent> launchHandler;
	private boolean isActive, isFiring, isSingleShotMode;
	private float power;

	public ExplosiveArrowPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);

		hitHandler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public void execute(ProjectileHitEvent event) {
				if (event.getEntityType() != EntityType.ARROW)
					return;

				Arrow a = (Arrow) event.getEntity();

				Location loc = a.getLocation();
				if (!arrows.contains(a))
					return;

				a.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, false, false);
				arrows.remove(a);
				a.remove();

				if (arrows.isEmpty())
					setActive(false);
			}

			public Class<ProjectileHitEvent> getEventType() {
				return ProjectileHitEvent.class;
			}

			public String getEventName() {
				return "ProjectileHitEvent";
			}
		};

		launchHandler = new BaseMMOEventHandler<ProjectileLaunchEvent>(new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileLaunchEvent").toString(), EventPriority.MONITOR, 1) {


			public boolean ignoreCancelled() {
				return true;
			}

			public Class<ProjectileLaunchEvent> getEventType() {
				return ProjectileLaunchEvent.class;
			}

			public String getEventName() {
				return "ProjectileLaunchEvent";
			}

			public void execute(ProjectileLaunchEvent event) {
				Arrow a = (event.getEntity() instanceof Arrow ? (Arrow) event.getEntity() : null);

				if (a == null)
					return;

				if (a.getShooter().equals(getPlayer()) && isFiring()) {
					arrows.add(a);
					if (isSingleShotMode())
						setFiring(false);
				}

				if (!arrows.isEmpty())
					setActive(true);

			}
		};


		this.arrows = new ArrayList<Arrow>();
		this.isActive = false;
		this.isFiring = false;
		this.isSingleShotMode = true;
		this.power = 4f;
	}

	public boolean isFiring() {
		return this.isFiring;
	}

	public ExplosiveArrowPlayerAbility setFiring(boolean firing) {
		boolean changed = this.isFiring != firing;
		this.isFiring = firing;

		if (changed)
			if (firing)
				registerHandler(launchHandler);
			else
				unregisterHandler(hitHandler);

		return this;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public ExplosiveArrowPlayerAbility setActive(boolean active) {
		boolean changed = this.isActive != active;
		this.isActive = active;

		if (changed)
			if (active)
				registerHandler(hitHandler);
			else
				unregisterHandler(hitHandler);

		return this;
	}

	public boolean isSingleShotMode() {
		return this.isSingleShotMode;
	}

	public ExplosiveArrowPlayerAbility setSingleShotMode(boolean singleMode) {
		this.isSingleShotMode = singleMode;
		return this;
	}

	/**
	 * Gets the current power of the explosion
	 *
	 * @return Float The power
	 */
	public float getPower() {
		return power;
	}

	/**
	 * Sets the power of the explosion
	 *
	 * @param power The power
	 * @return NetArrowAbility This instance
	 */
	public ExplosiveArrowPlayerAbility setPower(float power) {
		this.power = power;
		return this;
	}

	public AbilityResult<ExplosiveArrowPlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<ExplosiveArrowPlayerAbility>(this, false);

		if (!isActive() && !isFiring()) {
			setFiring(true);
			return new AbilityResult<ExplosiveArrowPlayerAbility>(this, true, MMOLocale.ABIL_ACTIVATED.get(getName()));
		} else {
			return new AbilityResult<ExplosiveArrowPlayerAbility>(this, false, MMOLocale.ABIL_ALREADY_ACTIVE.get(getName()));
		}
	}

}
