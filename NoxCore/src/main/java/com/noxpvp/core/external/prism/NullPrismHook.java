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

import com.noxpvp.core.NoxPlugin;

import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actions.GenericAction;
import me.botsko.prism.events.PrismCustomPlayerActionEvent;

public class NullPrismHook implements IPrismHook {

	public boolean isPrismEnabled() {
		return false;
	}

	public void registerActionType(NoxPlugin plugin, ActionType type) {
		return;
	}
	
	public void registerCustomActionHandler(NoxPlugin plugin, Class<? extends GenericAction> clazz) {
		return;
	}
	
	public void callCustomPrismEvent(PrismCustomPlayerActionEvent event) {
		return;
	}

}
