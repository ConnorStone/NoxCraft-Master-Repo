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

package com.noxpvp.mmo.abilities.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;

public class WisdomEntityAbility extends BaseEntityAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Wisdom";
	public static final String PERM_NODE = "wisdom";
	private NoxMMO mmo;
	private int duration;
	private int amplifier;
	public WisdomEntityAbility(Entity e) {
		super(ABILITY_NAME, e);

		this.mmo = NoxMMO.getInstance();
		this.duration = 10;
		this.amplifier = 2;
	}

	@Override
	public String getDescription() {
		return "You gain a sudden burst of wisdom, increasing your attack strength by " + (((getAmplifier() + 1) * 130)  - 100) + " % for " + getDuration() + " seconds";
	}

	/**
	 * Gets the duration is seconds
	 *
	 * @return Integer The duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the duration
	 *
	 * @param duration in seconds
	 * @return WisdomAbility This instance
	 */
	public WisdomEntityAbility setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * Gets the amplifier
	 *
	 * @return Integer The amplifier
	 */
	public int getAmplifier() {
		return amplifier;
	}

	/**
	 * Sets the amplifier
	 *
	 * @param amplifier
	 * @return WisdomAbility This instance
	 */
	public WisdomEntityAbility setAmplifier(int amplifier) {
		this.amplifier = amplifier;
		return this;
	}

	public AbilityResult<WisdomEntityAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<WisdomEntityAbility>(this, false);

		final LivingEntity e = (LivingEntity) (getEntity() instanceof LivingEntity ? getEntity() : null);

		if (e == null) return new AbilityResult<WisdomEntityAbility>(this, false);

		final Location loc = e.getLocation().clone();
		e.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 45, -100), true);
		e.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 45, -100), true);

		new ParticleRunner(ParticleType.enchantmenttable, e, false, 2F, 150, 1).start(10);
		new ParticleRunner(ParticleType.fireworksSpark, e, false, 0.3F, 50, 1).start(45);
		new ParticleRunner(ParticleType.flame, e, false, 0.05F, 50, 1).start(45);


		Bukkit.getScheduler().runTaskLater(mmo, new Runnable() {

			public void run() {
				e.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * getDuration(), getAmplifier()), true);
				e.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * getDuration(), getAmplifier()), true);
				e.teleport(loc);
			}
		}, 45);

		return new AbilityResult<WisdomEntityAbility>(this, true);
	}

}
