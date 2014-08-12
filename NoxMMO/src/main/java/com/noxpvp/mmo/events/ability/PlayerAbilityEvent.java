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

package com.noxpvp.mmo.events.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.events.internal.IPlayerAbilityEvent;

public class PlayerAbilityEvent<T extends BasePlayerAbility> extends
		BaseAbilityEvent<T> implements IPlayerAbilityEvent<T> {
	
	private static HandlerList	handlers	= new HandlerList();
	private final Player		player;
	
	public PlayerAbilityEvent(T ability, Player player) {
		super(ability);
		this.player = player;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Player getPlayer() {
		return player;
	}
}
