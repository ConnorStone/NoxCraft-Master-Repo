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

package com.noxpvp.mmo.abilities.player;

import com.noxpvp.mmo.abilities.PVPAbility;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

//FIXME not made yet

public class JoustPlayerAbility extends BasePlayerAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Joust";
	public static final String PERM_NODE = "joust";

	private EntityDamageByEntityEvent event;

	public JoustPlayerAbility(Player p, EntityDamageByEntityEvent event) {
		super(ABILITY_NAME, p);

		this.event = event;
	}

	public AbilityResult execute() {

		throw new NotImplementedException();

//		OldMMOPlayer mmoP;
//		PlayerClass clazz = (mmoP = PlayerManager.getInstance().getPlayer(getPlayer())) != null? mmoP.getPrimaryClass() : null;
//		
//		if (clazz == null) return false;
//		
//		event.setDamage(event.getDamage() + (((clazz.getTotalLevel() * 100) + clazz.getLevel()) / 75));
//		
//		return true;
	}

}
