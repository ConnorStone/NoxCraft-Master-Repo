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

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.utils.UUIDUtil;
import org.apache.commons.lang.IllegalClassException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.OldNoxPlayer;
import com.noxpvp.core.internal.IHeated;
import com.noxpvp.core.manager.old.CorePlayerManager;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.events.PlayerAbilityPreExecuteEvent;
import com.noxpvp.mmo.locale.MMOLocale;

import java.util.logging.Level;

public abstract class BasePlayerAbility extends BaseEntityAbility implements IPlayerAbility {
	private static MMOPlayerManager pm = MMOPlayerManager.getInstance();
	
	public BasePlayerAbility(final String name, Player player) {
		super(name, player);
	}

	public void fixPlayer(Object ob )
	{
		if (ob instanceof Player) fixEntityRef((Player)ob);
		else if (UUIDUtil.isUUID(ob)) fixEntityRef(Bukkit.getPlayer(UUIDUtil.toUUID(ob)));
		else if (LogicUtil.nullOrEmpty((String) ((ob == null)?null:ob.toString()))) {
			NoxMMO.getInstance().log(Level.INFO, "It is not recommended to grab players by name due to uuid changes. " +
							System.lineSeparator() + "BasePlayerAbility got a non uuid object for fixPlayer(object)");
			fixEntityRef(Bukkit.getPlayer(ob.toString()));
		}
	}

	public Player getPlayer() {
		if (!(getEntity() instanceof Player))
			throw new IllegalStateException("Internal Data was tampered with..", new IllegalClassException(Player.class, Entity.class));
		
		Player p = (Player) getEntity();
		if (p == null || !p.isValid())
			fixPlayer(p.getName());
		
		return (Player) getEntity();
	}

	public OldNoxPlayer getNoxPlayer() {
		if (isValid())
			return CorePlayerManager.getInstance().getPlayer(getPlayer());
		return null;
	}

	/**
	 * Returns is the player of this ability is null and has the permission, thus if the execute method will start
	 *
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		Player player = getPlayer();
		if (player == null || !player.isOnline() || !hasPermission())
			return false;
		
		MMOPlayer p;
		if (this instanceof IHeated && (p = pm.getPlayer(player)).isCooldownActive(getName())) {
			if (!(this instanceof SilentAbility))
				MessageUtil.sendLocale(player, MMOLocale.ABIL_ON_COOLDOWN, getName(), p.getReadableRemainingCDTime(getName()));
			
			return false;
		}

		return super.mayExecute();
	}

	@Override
	public boolean isCancelled() {
		return CommonUtil.callEvent(new PlayerAbilityPreExecuteEvent(getPlayer(), this)).isCancelled();
	}

	/**
	 * Recommended to override if you want to add dynamic perm node support.
	 *
	 * @return true if allowed or false if not OR if could not retrieve NoxPlayer object.
	 */
	public boolean hasPermission() {
		OldNoxPlayer p = getNoxPlayer();
		if (p == null)
			return false;

		return VaultAdapter.PermUtils.hasPermission(p, (NoxMMO.PERM_NODE + ".ability." + getName().replaceAll(" ", "-").toLowerCase()));
	}

}
