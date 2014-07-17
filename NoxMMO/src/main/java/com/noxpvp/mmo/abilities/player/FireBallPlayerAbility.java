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
import org.bukkit.entity.*;
import org.bukkit.projectiles.ProjectileSource;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.effect.shaped.BaseHelix;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;

/**
 * @author NoxPVP
 */
public class FireBallPlayerAbility extends BasePlayerAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Fire Ball";
	public static final String PERM_NODE = "fire-ball";

	private double power;

	/**
	 * @param e The Entity type user of this ability instance
	 */
	public FireBallPlayerAbility(Player p) {
		super(ABILITY_NAME, p);

		this.power = 3;
	}

	/**
	 * @return double current power set for fireball launch
	 */
	public double getPower() {
		return this.power;
	}

	/**
	 * @param power The double power to shoot the fireball at
	 *              (THIS MUST BE 1.0 OR ABOVE)
	 * @return FireBallAbility This instance, used for chaining
	 */
	public FireBallPlayerAbility setPower(double power) {
		this.power = power;
		return this;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Entity e = getEntity();
		Location loc = (e instanceof LivingEntity) ? ((LivingEntity) e).getEyeLocation() : e.getLocation();
		Projectile fireBall = (Projectile) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);

		fireBall.setVelocity(loc.getDirection().normalize().multiply(getPower()));
		fireBall.setShooter((ProjectileSource) e);

		new FireBallHelix(e, 60).render(250);

		return new AbilityResult(this, true);
	}

	private class FireBallHelix extends BaseHelix {

		public FireBallHelix(Entity user, int time) {
			super((user instanceof LivingEntity) ?
					((LivingEntity) user).getEyeLocation().add(user.getLocation().getDirection().normalize()) :
					user.getLocation().add(user.getLocation().getDirection().normalize()), time);

			setForwardGain(0.07);
			setRadiusGain(0.5);
			setSpeed(1);

		}

		public NoxPlugin getPlugin() {
			return NoxMMO.getInstance();
		}

		public void onStart() {
			return;
		}

		public void onRun() {
			new ParticleRunner(ParticleType.flame, getLocation(), false, 0, 1, 1).start(0);

		}

		public void onStop() {
			return;
		}
	}

}
