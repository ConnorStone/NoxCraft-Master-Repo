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

import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.internal.IHeated;
import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.abilities.internal.SilentAbility;
import com.noxpvp.mmo.handlers.MMOEventHandler;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public abstract class BasePlayerAbility<T extends PlayerAbilityTier<? extends PlayerAbility>>
		extends BaseTieredAbility<T> implements PlayerAbility<T> {
	
	private final UUID				playerUUID;
	private final MasterListener	masterListener;
	
	private CoolDown.Time			cd;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public BasePlayerAbility(final String name, OfflinePlayer player) {
		super(name);
		Validate.notNull(player);
		playerUUID = player.getUniqueId();
		masterListener = NoxMMO.getInstance().getMasterListener();
		cd = new CoolDown.Time();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public CoolDown.Time getCD() {
		return cd;
	}
	
	public MasterListener getMasterListener() {
		return NoxMMO.getInstance().getMasterListener();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods: Implements
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public MMOPlayer getMMOPlayer() {
		return MMOPlayerManager.getInstance().getPlayer(getOfflinePlayer());
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPlayerUUID());
	}
	
	public Player getPlayer() {
		return isOnline() ? getOfflinePlayer().getPlayer() : null;
	}
	
	public UUID getPlayerUUID() {
		return playerUUID;
	}
	
	/**
	 * Recommended to override if you want to add dynamic perm node
	 * support.
	 * 
	 * @return true if allowed or false if not OR if could not retrieve
	 *         NoxPlayer object.
	 */
	public boolean hasPermission() {
		final MMOPlayer p = getMMOPlayer();
		if (p == null)
			return false;
		
		return VaultAdapter.PermUtils.hasPermission(p, NoxMMO.PERM_NODE
				+ ".ability." + getName().replaceAll(" ", "-").toLowerCase());
	}
	
	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}
	
	/**
	 * Returns is the player of this ability is null and has the
	 * permission, thus if the execute method will start This also checks
	 * for if the ability is an instance of a {@link PVPAbility} which
	 * marks anything that should not be used in Non PVP Zones. This will
	 * prevent usage in such zones.
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	@Override
	public boolean mayExecute() {
		final Player player = getPlayer();
		
		if (player == null || !player.isOnline() || !hasPermission())
			return false;
		
		NoxPlayer p;
		
		if (this instanceof PVPAbility && !TownyUtil.isPVP(player)) {
			MessageUtil.sendLocale(player, MMOLocale.ABIL_NO_PVP, getName());
			return false;
		}
		
		if (this instanceof IHeated
				&& (p = MMOPlayerManager.getInstance().getPlayer(player)
						.getNoxPlayer()).isCooling(getName())) {
			if (!(this instanceof SilentAbility)) {
				MessageUtil.sendLocale(player, MMOLocale.ABIL_ON_COOLDOWN,
						getName(), p.getReadableRemainingCDTime(getName()));
			}
			
			return false;
		}
		
		return super.mayExecute();
	}
	
	public void registerHandler(MMOEventHandler<? extends Event> handler) {
		getMasterListener().registerHandler(handler);
	}
	
	public void setCD(CoolDown.Time coolDown) {
		cd = coolDown;
	}
	
	public void unregisterHandler(MMOEventHandler<? extends Event> handler) {
		getMasterListener().unregisterHandler(handler);
	}
}
