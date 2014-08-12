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

import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.noxpvp.mmo.abilities.BaseTargetedAbility.TargetedAbilityResult;

public class CursePlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "curse";
	private static final String ABILITY_NAME = "Curse";
	private int duration;
	private int lethality;

	public CursePlayerAbility(Player player) {
		this(player, 15);
	}

	public CursePlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());

		setCD(new CoolDown.Time().seconds(35));
		this.duration = 100;
		this.lethality = 1;
	}

	/**
	 * Gets the currently set duration in ticks of the curse effect.
	 *
	 * @return Integer Duration of the curse effect.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the duration in ticks of the curse effect.
	 *
	 * @param duration The duration of the curse effect.
	 * @return CurseAbility This instance
	 */
	public CursePlayerAbility setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * Gets the currently set amplifier for the curse effect.
	 *
	 * @return Integer Amplifier for the curse effect.
	 */
	public int getLethality() {
		return lethality;
	}

	/**
	 * Sets the amplifier of the curse effect.
	 *
	 * @param lethality The amplifier of the curse effect.
	 * @return CurseAbility This instance
	 */
	public CursePlayerAbility setLethality(int lethality) {
		this.lethality = lethality;
		return this;
	}

	public TargetedAbilityResult<CursePlayerAbility> execute() {
		if (!mayExecute())
			return new TargetedAbilityResult<CursePlayerAbility>(this, false);

		LivingEntity t = getTarget();

		t.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, duration, lethality));
		t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, lethality));

		new ParticleRunner(ParticleType.angryVillager, t, false, 0, 1, 1).start(0);

		return new TargetedAbilityResult<CursePlayerAbility>(this, true);
	}
}
