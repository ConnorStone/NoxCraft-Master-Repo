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

package com.noxpvp.core.external.prism;

import org.bukkit.entity.Player;

import com.noxpvp.core.NoxPlugin;

import me.botsko.prism.events.PrismCustomPlayerActionEvent;

public class BaseNoxPrismEvent extends PrismCustomPlayerActionEvent implements INoxPrismEvent {

	private NoxPlugin plugin;
	
	/**
	 * 
	 * @param plugin
	 * @param action_type_name
	 * @param player
	 * @param builtArgBuilder see {@link NoxPrismEventArgBuilder}
	 */
	public BaseNoxPrismEvent(NoxPlugin plugin, String action_type_name,
			Player player, String builtArgBuilder) {
		
		super(plugin, action_type_name, player, builtArgBuilder);
		
		this.plugin = plugin;
	}

	public NoxPlugin getPlugin() {
		return plugin;
	}

}
