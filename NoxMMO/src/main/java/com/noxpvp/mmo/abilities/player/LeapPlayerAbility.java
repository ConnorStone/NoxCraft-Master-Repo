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
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.ShockWaveAnimation;

public class LeapPlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Leap";
	public static final String PERM_NODE = "leap";
	private double multiplier;

	public LeapPlayerAbility(OfflinePlayer p, double multiplier) {
		super(ABILITY_NAME, p);
		this.multiplier = multiplier;
		
		setCD(new CoolDown.Time().seconds(5));
	}

	public LeapPlayerAbility(OfflinePlayer p) {
		this(p, 2D);
	}

	@Override
	public String getDescription() {
		return "You leap forward in your current direction";
	}

	/**
	 * Gets the current multiplier for the ability's forward velocity
	 *
	 * @return double The forward multiplier
	 */
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * Sets the multiplier for this ability's forward velocity
	 *
	 * @param forwardMultiplier The multiplier to set
	 * @return LeapAbility This instance
	 */
	public LeapPlayerAbility setMultiplier(double forwardMultiplier) {
		this.multiplier = forwardMultiplier;
		return this;
	}

	public AbilityResult<LeapPlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<LeapPlayerAbility>(this, false);

		Player p = getPlayer();
		Location pLoc = p.getLocation();
		Vector newVelocity = pLoc.getDirection();
		newVelocity.multiply(multiplier);

		// if going up a reasonable amount
		if (newVelocity.getY() > .65) {
			new ParticleRunner(ParticleType.cloud, pLoc.clone().add(0, 2, 0), true, 0, 50, 1).start(0);
			new ShockWaveAnimation(pLoc, 1, 2, true).start(0);
		}

		//reset fall distance on use
		p.setFallDistance(0);
		p.setVelocity(newVelocity);
		return new AbilityResult<LeapPlayerAbility>(this, true);
	}

}
