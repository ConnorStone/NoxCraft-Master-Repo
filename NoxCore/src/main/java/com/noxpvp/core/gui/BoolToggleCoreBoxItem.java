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

package com.noxpvp.core.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BoolToggleCoreBoxItem extends ToggleCoreBoxItem {

	private boolean onOff;

	public BoolToggleCoreBoxItem(CoreBox parent, ItemStack item) {
		this(parent, item, false);
	}

	public BoolToggleCoreBoxItem(CoreBox parent, ItemStack item,
			boolean startState) {
		super(parent, item);
		this.onOff = startState;
	}

	public final void onToggle(InventoryClickEvent click) {
		if (canToggle(click)) {
			onOff =! onOff;
			handleToggle(click);
		}
	}

	public final boolean getState() {
		return onOff;
	}

	/**
	 * Defines whether or not you can toggle the item based on the click event.<br/>
	 * <p>
	 *     Default's to true: Must override to add functionality
	 * </p>
	 * @return true if allowed, False otherwise
	 */
	public boolean canToggle(InventoryClickEvent click) {
		return true;
	}

	public abstract void handleToggle(InventoryClickEvent click);
}
