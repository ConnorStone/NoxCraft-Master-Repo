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

package com.noxpvp.mmo.abilities.targeted;

import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.internal.DamagingAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

import static com.noxpvp.mmo.abilities.BaseTargetedAbility.TargetedAbilityResult;

public class MortalWoundPlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility, DamagingAbility {

	public static final String ABILITY_NAME = "Mortal Wound";
	public static final String PERM_NODE = "mortal-wound";
	private int duration;
	private int amplifier;
	private double damage;

	public MortalWoundPlayerAbility(Player player) {
		this(player, 10);
	}

	public MortalWoundPlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, MMOPlayerManager.getInstance().getPlayer(player).getTarget());

		setDamage(8);
		setRange(range);
		setCD(new CoolDown.Time().seconds(45));
		this.duration = (20 * 4);
		this.amplifier = 2;
	}

	@Override
	public String getDescription() {
		return "Bypass all your targets powers and hit them as the mortal they are, causing slowness and posion";
	}

	/**
	 * Gets the duration in ticks set for this ability
	 *
	 * @return Integer Tick length duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the duration in ticks set for this ability
	 *
	 * @param duration Duration in ticks
	 * @return MortalWoundAbility This instance
	 */
	public MortalWoundPlayerAbility setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * Gets the amplifier for the slowness and poison in this ability
	 *
	 * @return Integer Amplifier
	 */
	public int getAmplifier() {
		return amplifier;
	}

	/**
	 * Sets the amplifier for the slowness and poison effects in this ability
	 *
	 * @param amplifier The amplifier
	 * @return MortalWoundAbility This instance
	 */
	public MortalWoundPlayerAbility setAmplifier(int amplifier) {
		this.amplifier = amplifier;
		return this;
	}

	public TargetedAbilityResult<MortalWoundPlayerAbility> execute() {
		if (!mayExecute())
			return new TargetedAbilityResult<MortalWoundPlayerAbility>(this, false);

		LivingEntity t = getTarget();
		Player p = getPlayer();

		t.damage(getDamage(), p);
		t.addPotionEffects(Arrays.asList(
				new PotionEffect(PotionEffectType.POISON, duration, amplifier),
				new PotionEffect(PotionEffectType.SLOW, duration, amplifier)));

		return new TargetedAbilityResult<MortalWoundPlayerAbility>(this, true);
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}
}
