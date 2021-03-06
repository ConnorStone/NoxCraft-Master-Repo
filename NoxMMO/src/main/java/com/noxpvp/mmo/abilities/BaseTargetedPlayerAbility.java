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

package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public abstract class BaseTargetedPlayerAbility extends BasePlayerAbility implements TargetedPlayerAbility{
	private Reference<LivingEntity> target_ref;
	
	public BaseTargetedPlayerAbility(String name, Player player, LivingEntity target){
		super(name, player);
		
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	public BaseTargetedPlayerAbility(String name, Player player){
		this(name, player, null);
	}
	
	public LivingEntity getTarget() {
		return (target_ref == null) ? null : target_ref.get();
	}
	
	public void setTarget(LivingEntity target){
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	public double getDistance(){
		if (getPlayer() != null)
			return getDistance(getPlayer().getLocation());
		
		return -1;
	}
	
	public double getDistance(Location loc) {
		if (getTarget() != null && loc != null)
			return getTarget().getLocation().distance(loc);
		
		return -1;
	}
	
	/**
	 * Returns is the player or target of this ability is null, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return (getPlayer() != null && getTarget() != null);
	}
}
