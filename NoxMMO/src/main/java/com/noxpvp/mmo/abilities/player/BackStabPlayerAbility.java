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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;

/**
 * @author NoxPVP
 */
public class BackStabPlayerAbility extends BasePlayerAbility implements IPassiveAbility<EntityDamageByEntityEvent>, PVPAbility {

	public static final String PERM_NODE = "backstab";
	public static final String ABILITY_NAME = "BackStab";

	private LivingEntity target;
	private float damagePercent;

	private double accuracy;

	/**
	 * @param player The Player type user for this ability instance
	 */
	public BackStabPlayerAbility(Player player) {
		super(ABILITY_NAME, player);

		this.damagePercent = 150;
		this.accuracy = 20;
	}

	/**
	 * @return double the currently set accuracy required for being behind the target
	 */
	public double getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy double value in degrees of accuracy required. 0 = exactly behind, 20 (Default) = 20 degrees to either side of target
	 * @return BackStabAbility This instance, used for chaining
	 */
	public BackStabPlayerAbility setAccuracy(double accuracy) {
		this.accuracy = accuracy;
		return this;
	}

	/**
	 * @return Entity The currently set target for this ability
	 */
	public LivingEntity getTarget() {
		return target;
	}

	/**
	 * @param target The LivingEntity type target for this ability instance
	 * @return BackStabAbility This instance, used for chaining
	 */
	public BackStabPlayerAbility setTarget(LivingEntity target) {
		this.target = target;
		return this;
	}

	/**
	 * @return double The currently set damage percent. 100% = normal damage.
	 */
	public float getDamagePercent() {
		return damagePercent;
	}

	/**
	 * @param damagePercent double percent value for damage modifier. 100% = normal damage
	 */
	public void setDamagePercent(float damagePercent) {
		this.damagePercent = damagePercent;
	}

	public AbilityResult execute(EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return new AbilityResult(this, false);

		LivingEntity t = getTarget();
		Player p = getPlayer();

		Location pLoc = p.getLocation();
		Location tLoc = t.getLocation();
		double tYaw = tLoc.getYaw();
		double pYaw = pLoc.getYaw();

		if (!(pYaw <= (tYaw + accuracy)) && (pYaw >= (tYaw - accuracy)))
			return new AbilityResult(this, false);

		MMOPlayer player = MMOPlayerManager.getInstance().getPlayer(p);
		if (player == null)
			return new AbilityResult(this, false);

		IPlayerClass clazz = player.getPrimaryClass();

		float chance = (clazz.getLevel() + clazz.getTotalLevel()) / 10;//up to 40% at max 400 total levels
		if ((Math.random() * 100) > chance)
			return new AbilityResult(this, false);

		if (pLoc.distance(tLoc) < .35)//prevent if inside the target
			return new AbilityResult(this, false);

		event.setDamage(event.getDamage() * damagePercent);

		return new AbilityResult(this, true);
	}

	public AbilityResult execute() {
		return new AbilityResult(this, true);
	}

}
