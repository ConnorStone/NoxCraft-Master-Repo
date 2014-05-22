package com.noxpvp.mmo.abilities.entity;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.ShockWaveAnimation;

public class LeapPlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Leap";
	public static final String PERM_NODE = "leap";
	private double multiplier;

	public LeapPlayerAbility(Player p, double multiplier) {
		super(ABILITY_NAME, p);
		this.multiplier = multiplier;
	}

	public LeapPlayerAbility(Player ent) {
		this(ent, 2D);
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

	public boolean execute() {
		if (!mayExecute())
			return false;

		Player p = getPlayer();
		Location pLoc = p.getLocation();
		Vector newVelocity = pLoc.getDirection();
		newVelocity.multiply(multiplier);

		// if going up a reasonable amount
		if (newVelocity.getY() > .75) {
			new ParticleRunner(ParticleType.cloud, pLoc.clone().add(0, 2, 0), true, 0, 50, 1).start(0);
			new ShockWaveAnimation(pLoc, 1, 2, true).start(0);
		}

		//reset fall distance on use
		p.setFallDistance(0);
		p.setVelocity(newVelocity);
		return true;
	}

}
