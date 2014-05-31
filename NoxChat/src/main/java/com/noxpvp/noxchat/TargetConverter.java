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

package com.noxpvp.noxchat;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.conversion.BasicConverter;

public class TargetConverter extends BasicConverter<Targetable> {

	public TargetConverter() {
		super(Targetable.class);
	}

	@Override
	protected Targetable convertSpecial(Object value, Class<?> valueType, Targetable def) {
		if (value instanceof Player)
			return PlayerManager.getInstance().getPlayer((Player) value);

		return def;
	}

	@Override
	public boolean isRegisterSupported() {
		return false;
	}
}
