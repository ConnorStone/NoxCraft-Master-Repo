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
import java.lang.ref.WeakReference;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.internal.EntityAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.handlers.MMOEventHandler;
import com.noxpvp.mmo.listeners.IMMOHandlerHolder;
import com.noxpvp.mmo.locale.MMOLocale;

public abstract class BaseEntityAbility<T extends EntityAbilityTier<? extends BaseEntityAbility>>
		extends BaseTieredAbility<T> implements EntityAbility<T>, IMMOHandlerHolder {
	
	private Reference<Entity>		entityRef;
	private final MasterListener	masterListener;
	
	private CoolDown.Time			cd;
	
	public BaseEntityAbility(final String name, Entity ref) {
		super(name);
		Validate.notNull(ref);
		entityRef = new WeakReference<Entity>(ref);
		
		masterListener = NoxMMO.getInstance().getMasterListener();
		cd = new CoolDown.Time().seconds(5);
	}
	
	public void fixEntityRef(Entity e) {
		entityRef = new WeakReference<Entity>(e);
	}
	
	public CoolDown.Time getCD() {
		return cd;
	}
	
	public Entity getEntity() {
		return entityRef.get();
	}
	
	public MasterListener getMasterListener() {
		return masterListener;
	}
	
	public boolean isValid() {
		return getEntity() != null;
	}
	
	/**
	 * Returns is the Entity of this ability is null, thus if the execute
	 * method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	@Override
	public boolean mayExecute() {
		final Entity entity = getEntity();
		
		if (entity == null || !(entity instanceof Player)
				&& (entity.isDead() || !entity.isValid()))
			return false;
		
		if (this instanceof PVPAbility && !TownyUtil.isPVP(entity)) {
			if (entity instanceof CommandSender) {
				MessageUtil.sendLocale((CommandSender) entity,
						MMOLocale.ABIL_NO_PVP, getName());
			}
			
			return false;
		}
		
		return super.mayExecute();
	}
	
	public void registerHandler(MMOEventHandler<? extends Event> handler) {
		masterListener.registerHandler(handler);
	}
	
	public void setCD(CoolDown.Time cd) {
		this.cd = cd;
	}
	
	/**
	 * @deprecated Please use the new Time Objects.
	 * @param i
	 *            seconds
	 */
	@Deprecated
	public void setCD(int i) {
		setCD(new CoolDown.Time().seconds(i));
	}
	
	public void unregisterHandler(MMOEventHandler<? extends Event> handler) {
		masterListener.unregisterHandler(handler);
	}
}
