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

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class CurseAbility extends BaseTargetedPlayerAbility{
	
	private final static String ABILITY_NAME = "Curse";
	public final static String PERM_NODE = "curse";
	
	private int duration;
	private int lethality;
	private double range;
	
	/**
	 * Gets the currently set duration in ticks of the curse effect
	 * 
	 * @return Integer Duration of the curse effect
	 */
	public int getDuration() {return duration;}
	
	/**
	 * Sets the duration in ticks of the curse effect
	 * 
	 * @param lethality The duration of the curse effect
	 * @return CurseAbility This instance
	 */
	public CurseAbility setDuration(int duration) {this.duration = duration; return this;}

	/**
	 * Gets the currently set amplifier for the curse effect
	 * 
	 * @return Integer Amplifier for the curse effect
	 */
	public int getLethality() {return lethality;}
	
	/**
	 * Sets the amplifier of the curse effect
	 * 
	 * @param duration The amplifier of the curse effect
	 * @return CurseAbility This instance
	 */
	public CurseAbility setLethality(int lethality) {this.lethality = lethality; return this;}
	
	/**
	 * Gets the currently set range for this ability instance
	 * 
	 * @return range The max distance the target can be from the ability user
	 */
	public double getRange() {return range;}
	
	/**
	 * Sets the range of this ability instance
	 * 
	 * @param range The max distance to allow the target to be from the ability user
	 * @return CurseAbility This instance
	 */
	public CurseAbility setRange(double range) {this.range = range; return this;}
	
	public CurseAbility(Player player){
		
		super(ABILITY_NAME, player, PlayerManager.getInstance().getPlayer(player).getTarget());
		
		this.duration = 100;
		this.lethality = 1;
		this.range = 5;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		LivingEntity t = getTarget();
		
		if (t.getLocation().distance(getPlayer().getLocation()) > range)
			return false;
		
		t.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, lethality));
		t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, lethality));
		
		new ParticleRunner("angryVillager", t, false, 0, 1, 1).start(0);
		
		return true;
	}
}
