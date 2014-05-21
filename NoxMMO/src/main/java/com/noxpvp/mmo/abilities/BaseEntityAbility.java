package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.events.EntityAbilityPreExcuteEvent;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.locale.MMOLocale;

public abstract class BaseEntityAbility extends BaseAbility implements IEntityAbility {
	private Reference<Entity> entityRef;
	private MasterListener masterListener;
	private double damage;
	
	public BaseEntityAbility(final String name, Entity ref)
	{
		super(name);
		entityRef = new SoftReference<Entity>(ref);
		
		this.masterListener = NoxMMO.getInstance().getMasterListener();
	}
	
	public Entity getEntity() {
		return entityRef.get();
	}
	
	public boolean isValid() { return getEntity() != null; }
	
	/**
	 * Returns is the Entity of this ability is null, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		Entity entity = getEntity();
		if (entity == null || entity.isDead() || !entity.isValid())
			return false;
		
		if (this instanceof IPVPAbility && !TownyUtil.isPVP(entity)) {
			if (entity instanceof CommandSender)
				MessageUtil.sendLocale((CommandSender) entity, MMOLocale.ABIL_NO_PVP, getName());
			
			return false;
		}
		
		return super.mayExecute();
	}
	
	public boolean isCancelled() {
		return CommonUtil.callEvent(new EntityAbilityPreExcuteEvent(getEntity(), this)).isCancelled();
	}
	
	public MasterListener getMasterListener() {
		return masterListener;
	}
	
	public void registerHandler(BaseMMOEventHandler<? extends Event> handler) {
		masterListener.registerHandler(handler);
	}
	
	public void unregisterHandler(BaseMMOEventHandler<? extends Event> handler) {
		masterListener.unregisterHandler(handler);
		
	}
	
	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	public double getDamage() {
		return damage;
	}
	
}
