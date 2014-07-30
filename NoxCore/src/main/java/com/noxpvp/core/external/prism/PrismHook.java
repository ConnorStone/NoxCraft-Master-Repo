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

import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actions.GenericAction;
import me.botsko.prism.events.PrismCustomPlayerActionEvent;
import me.botsko.prism.exceptions.InvalidActionException;

import com.noxpvp.core.NoxPlugin;

public class PrismHook implements IPrismHook {
	
	private Prism prism;
	private NoxPlugin plugin;
	
	public PrismHook (Prism prism, NoxPlugin plugin) {
		this.prism = prism;
		this.plugin = plugin;
	}

	public boolean isPrismEnabled() {
		return prism != null && prism.isEnabled();
	}
	
	public void registerActionType(NoxPlugin plugin, ActionType type) {
		if (isPrismEnabled())
			try {
				Prism.getActionRegistry().registerCustomAction(plugin, type);
			} catch (InvalidActionException e) {
				plugin.log(Level.WARNING, "Could not register custom ActionType in prism: " + e.getMessage());
				e.printStackTrace();
			}
	}
	
	public void registerCustomActionHandler(NoxPlugin plugin, Class<? extends GenericAction> clazz) {
		if (isPrismEnabled())
			try {
				Prism.getHandlerRegistry().registerCustomHandler(plugin, clazz);
			} catch (InvalidActionException e) {
				plugin.log(Level.WARNING, "Could nat register a custom Handler in prism: " + e.getMessage());
				e.printStackTrace();
			}
		
	}
	
	public void callCustomPrismEvent(PrismCustomPlayerActionEvent event) {
		if (isPrismEnabled())
			Bukkit.getPluginManager().callEvent(event);
	}

}
