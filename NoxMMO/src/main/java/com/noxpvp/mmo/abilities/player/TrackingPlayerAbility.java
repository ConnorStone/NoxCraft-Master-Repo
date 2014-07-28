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

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.runnables.DespawnRunnable;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.noxpvp.mmo.abilities.BaseRangedAbility.RangedAbilityResult;

/**
 * @author NoxPVP
 */
public class TrackingPlayerAbility extends BaseRangedPlayerAbility {

	public static final String ABILITY_NAME = "Tracking";
	public static final String PERM_NODE = "tracking";

	private boolean isInvis;
	private boolean hasSpeed;
	private int speedAmp;
	private Player it;
	private int duration;

	/**
	 * @param player The player that this tracker should be spawned from/ability user
	 */
	public TrackingPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);

		this.duration = 100;
		this.hasSpeed = true;
		this.isInvis = true;
		setRange(50);
		this.speedAmp = 1;
	}

	/**
	 * @return Boolean If tracker will be invisible
	 */
	public boolean isInvis() {
		return isInvis;
	}

	/**
	 * @param isInvis Boolean if tracker should be invisible
	 * @return TrackingAbility This instance used for chaining
	 */
	public TrackingPlayerAbility setInvis(boolean isInvis) {
		this.isInvis = isInvis;
		return this;
	}

	/**
	 * @return Boolean If tracker will have speed effect applied
	 */
	public boolean isHasSpeed() {
		return hasSpeed;
	}

	/**
	 * @param hasSpeed Boolean if tracker should have speed effect applied
	 * @return TrackingAbility This instance used for chaining
	 */
	public TrackingPlayerAbility setHasSpeed(boolean hasSpeed) {
		this.hasSpeed = hasSpeed;
		return this;
	}

	/**
	 * @return Integer The amplifier set for speed effect used on tracker (Returns null if setSpeedAmp has not been used)
	 */
	public int getSpeedAmp() {
		return speedAmp;
	}

	/**
	 * @param speedAmp Integer amplifier that will be used for trackers speed effect
	 */
	public void setSpeedAmp(int speedAmp) {
		this.speedAmp = speedAmp;
	}

	/**
	 * @return Integer The current duration set for tracker (Returns null is setDuration has not been used)
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration Integer ticks that the tracker should last
	 * @return TrackingAbility This instance, used for chaining
	 */
	public TrackingPlayerAbility setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * @return Boolean If execution has ended successfully
	 */
	public RangedAbilityResult execute() {
		if (!mayExecute())
			return new RangedAbilityResult<TrackingPlayerAbility>(this, false);

		Player p = getPlayer();
		double radius = getRange();

		for (Entity it : p.getNearbyEntities(radius, radius, radius)) {
			if (!(it instanceof Player))
				continue;

			this.it = (Player) it;
		}

		if (it == null)
			return new RangedAbilityResult<TrackingPlayerAbility>(this, false, MMOLocale.ABIL_NO_TARGET.get());

		Monster tracker = (Monster) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);

		tracker.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (duration * 5), 15));

		if (hasSpeed)
			tracker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (duration * 5), 1));
		if (isInvis)
			tracker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (duration * 5), 1));

		tracker.setTarget(it);

		new ParticleRunner(ParticleType.flame, it, true, 0, 0, 4).start(0, 5);
		new DespawnRunnable(tracker).runTaskLater(NoxMMO.getInstance(), duration);

		return new RangedAbilityResult<TrackingPlayerAbility>(this, true);
	}

}
