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

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.events.EntityAbilityPreExcuteEvent;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.locale.MMOLocale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseEntityAbility extends BaseAbility implements IEntityAbility {
	private Reference<Entity> entityRef;
	private MasterListener masterListener;
	
	private CoolDown.Time cd;
	private double damage;
	private List<Entity> effectedEntities;

	public BaseEntityAbility(final String name, Entity ref) {
		super(name);
		entityRef = new WeakReference<Entity>(ref);

		this.masterListener = NoxMMO.getInstance().getMasterListener();
		this.cd = new CoolDown.Time().seconds(5);
		this.damage = 0;
		this.effectedEntities = new ArrayList<Entity>();
	}
	
	public void fixEntityRef(Entity e) {
		this.entityRef = new WeakReference<Entity>(e);
	}

	public Entity getEntity() {
		return entityRef.get();
	}

	public boolean isValid() {
		return getEntity() != null;
	}

	/**
	 * Returns is the Entity of this ability is null, thus if the execute method will start
	 *
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		Entity entity = getEntity();
		if (entity == null || (!(entity instanceof Player) && (entity.isDead() || !entity.isValid())))
			return false;

		if (this instanceof PVPAbility && !TownyUtil.isPVP(entity)) {
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

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	public CoolDown.Time getCD() {
		return cd;
	}
	
	public void setCD(CoolDown.Time cd) {
		this.cd = cd;
	}

	/**
	 * @deprecated Please use the new Time Objects.
	 * @param i seconds
	 */
	@Deprecated
	public void setCD(int i) {
		setCD(new CoolDown.Time().seconds(i));
	}
	
	public void addEffectedEntity(Entity e) {
		effectedEntities.add(e);
	}
	
	public List<Entity> getEffectedEntities() {
		return effectedEntities;
	}
	
	public void clearEffected() {
		effectedEntities.clear();
	}

}
