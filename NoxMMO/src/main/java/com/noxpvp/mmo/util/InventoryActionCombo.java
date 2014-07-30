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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.event.inventory.InventoryAction;

/**
 * Specially made enum to figure out Inventory Actions without extra code in EVERY method.
 */
public enum InventoryActionCombo {//you cant pick up everything...
	ANY_PLACE(InventoryAction.PLACE_ALL, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME),
	ANY_PICKUP(InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME);


	private List<InventoryAction> actions;

	private InventoryActionCombo(InventoryAction... actions) {
		this(Arrays.asList(actions));
	}

	private InventoryActionCombo(List<InventoryAction> actions) {
		this.actions = actions;
	}

	private InventoryActionCombo(List<InventoryAction> actions, InventoryAction... actions2) {
		this(actions);
		for (InventoryAction action : actions2)
			this.actions.add(action);
	}

	private InventoryActionCombo(InventoryActionCombo combo, InventoryAction... actions2) {
		this(new ArrayList<InventoryAction>(combo.actions));
		for (InventoryAction action : actions2)
			this.actions.add(action);
	}

	private InventoryActionCombo(InventoryActionCombo[] combos, InventoryAction... actions2) {
		this(actions2);
		for (InventoryActionCombo combo : combos)
			this.actions.addAll(combo.actions);
	}

	public boolean contains(InventoryAction action) {
		return actions.contains(action);
	}
}
