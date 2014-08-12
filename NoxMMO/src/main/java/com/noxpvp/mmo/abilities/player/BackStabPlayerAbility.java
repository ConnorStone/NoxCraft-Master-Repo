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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.abilities.internal.PassiveAbility;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;

/**
 * @author NoxPVP
 */
public class BackStabPlayerAbility extends BasePlayerAbility implements
		PassiveAbility<EntityDamageByEntityEvent>, PVPAbility {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static final String	PERM_NODE		= "backstab";
	public static final String	ABILITY_NAME	= "BackStab";
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private LivingEntity		target;
	private float				damagePercent;
	
	private double				accuracy;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * @param player
	 *            The Player type user for this ability instance
	 */
	public BackStabPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);
		
		damagePercent = 150;
		accuracy = 20;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public AbilityResult execute() {
		return new AbilityResult<BackStabPlayerAbility>(this, true);
	}
	
	public AbilityResult<BackStabPlayerAbility> execute(
			EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return new AbilityResult<BackStabPlayerAbility>(this, false);
		
		final LivingEntity t = getTarget();
		final Player p = getPlayer();
		
		final Location pLoc = p.getLocation();
		final Location tLoc = t.getLocation();
		final double tYaw = tLoc.getYaw();
		final double pYaw = pLoc.getYaw();
		
		if (!(pYaw <= tYaw + accuracy) && pYaw >= tYaw - accuracy)
			return new AbilityResult<BackStabPlayerAbility>(this, false);
		
		final MMOPlayer player = MMOPlayerManager.getInstance().getPlayer(p);
		if (player == null)
			return new AbilityResult<BackStabPlayerAbility>(this, false);
		
		final PlayerClass clazz = player.getPrimaryClass();
		
		final float chance = clazz.getLevel() / 10;
		
		if (Math.random() * 100 > chance)
			return new AbilityResult<BackStabPlayerAbility>(this, false);
		
		if (pLoc.distance(tLoc) < .35)// prevent if inside the target
			return new AbilityResult<BackStabPlayerAbility>(this, false);
		
		event.setDamage(event.getDamage() * damagePercent);
		
		return new AbilityResult<BackStabPlayerAbility>(this, true);
	}
	
	/**
	 * @return double the currently set accuracy required for being behind
	 *         the target
	 */
	public double getAccuracy() {
		return accuracy;
	}
	
	/**
	 * @return double The currently set damage percent. 100% = normal
	 *         damage.
	 */
	public float getDamagePercent() {
		return damagePercent;
	}
	
	/**
	 * @return Entity The currently set target for this ability
	 */
	public LivingEntity getTarget() {
		return target;
	}
	
	/**
	 * @param accuracy
	 *            double value in degrees of accuracy required. 0 = exactly
	 *            behind, 20 (Default) = 20 degrees to either side of
	 *            target
	 * @return BackStabAbility This instance, used for chaining
	 */
	public BackStabPlayerAbility setAccuracy(double accuracy) {
		this.accuracy = accuracy;
		return this;
	}
	
	/**
	 * @param damagePercent
	 *            double percent value for damage modifier. 100% = normal
	 *            damage
	 */
	public void setDamagePercent(float damagePercent) {
		this.damagePercent = damagePercent;
	}
	
	/**
	 * @param target
	 *            The LivingEntity type target for this ability instance
	 * @return BackStabAbility This instance, used for chaining
	 */
	public BackStabPlayerAbility setTarget(LivingEntity target) {
		this.target = target;
		return this;
	}
	
}
