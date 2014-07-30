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

package com.noxpvp.core.utils;

import org.bukkit.Bukkit;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actions.GenericAction;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.external.prism.BaseNoxPrismEvent;
import com.noxpvp.core.external.prism.IPrismHook;
import com.noxpvp.core.external.prism.NullPrismHook;
import com.noxpvp.core.external.prism.PrismHook;

public class PrismUtil {
	private static ModuleLogger log;
	
	private static Prism prism;
	private static IPrismHook hook = new NullPrismHook();

	static {
		setup();
	}
	
	public static void setup() {
		if (log == null)
			log = NoxCore.getInstance().getModuleLogger("PrismUtil");

		prism = NoxCore.getInstance().getPrism();
		
		if (prism == null) {
			prism = (Prism) Bukkit.getPluginManager().getPlugin("Prism");
		}
		
		if (prism != null)
			hook = new PrismHook(prism, NoxCore.getInstance());
		else log.severe("Prism is null and could not be found");
	}
	
	public static boolean isPrismActive() {
		return hook.isPrismEnabled();
	}
	
	public static void registerActionType(NoxPlugin plugin, ActionType type) {
		if (!isPrismActive())
			return;
		
		hook.registerActionType(plugin, type);
	}
	
	public static void registerCustomActionHandler(NoxPlugin plugin, Class<? extends GenericAction> clazz) {
		if (!isPrismActive())
			return;
		
		hook.registerCustomActionHandler(plugin, clazz);
	}
	
	public static void callCustomPrismEvent(BaseNoxPrismEvent event) {
		if (!isPrismActive())
			return;
		
		hook.callCustomPrismEvent(event);
	}
}
