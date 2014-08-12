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

package com.noxpvp.mmo.util;

import org.bukkit.ChatColor;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.utils.NoxMessageBuilder;
import com.noxpvp.mmo.classes.internal.IPlayerClass;

public class NoxMMOMessageBuilder extends NoxMessageBuilder {
	
	public NoxMMOMessageBuilder(NoxPlugin plugin) {
		super(plugin);
	}
	
	public NoxMMOMessageBuilder(NoxPlugin plugin, boolean withHeader) {
		super(plugin, withHeader);
	}
	
	public NoxMMOMessageBuilder withClassInfo(IPlayerClass clazz) {
		
		gold(ChatColor.BOLD + "Name: ").append(clazz.getColor()).append(
				clazz.getName()).newLine();
		gold(ChatColor.BOLD + "About: ");
		
		for (final String lore : clazz.getLore()) {
			append(lore).newLine();
		}
		
		return this;
	}
	
}
