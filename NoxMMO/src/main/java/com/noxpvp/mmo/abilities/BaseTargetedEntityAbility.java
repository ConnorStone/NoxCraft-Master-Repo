package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public abstract class BaseTargetedEntityAbility extends BaseEntityAbility implements TargetedEntityAbility{
	private Reference<LivingEntity> target_ref;
	
	public BaseTargetedEntityAbility(String name, Entity entity, LivingEntity target){
		super(name, entity);
		
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	public BaseTargetedEntityAbility(String name, Entity entity){
		this(name, entity, null);
	}
	
	public LivingEntity getTarget() {
		return (target_ref == null) ? null : target_ref.get();
	}
	
	public void setTarget(LivingEntity target){
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	public double getDistance(){
		Location l = null;
		if (getTarget() != null)
			l = getTarget().getLocation();
		if (l != null)
			return getDistance(l);
		else
			return -1;
	}
}
