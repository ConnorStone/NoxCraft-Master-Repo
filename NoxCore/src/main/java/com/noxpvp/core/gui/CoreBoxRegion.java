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

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CoreBoxRegion {

	private static final int colums = 9;
	private final Vector a, b;
	private final CoreBox parent;
	private int rows;

	public CoreBoxRegion(CoreBox parent, Vector topLeft, int height, int width) {
		this.parent = parent;
		this.rows = parent.getBox().getSize();

		if (height > rows)
			throw new IllegalArgumentException("Cannot use height larger than box rows");
		if (width > 9)
			throw new IllegalArgumentException("Cannot use width larger than 9");

		this.a = topLeft;
		this.b = new Vector(a.getX() + height, 0, a.getZ() + width);
		
		if (a.getX() > rows)
			throw new IllegalArgumentException("a - x is too large");
		
	}

	private int getSlotFromCoord(Vector coord) {
		int x = (int) coord.getX();
		int z = (int) coord.getZ();

		return ((x * colums) + z);
	}

	public boolean add(CoreBoxItem item) {

		Inventory box = parent.getBox();
		ItemStack tempItem;
		int tempSlot;

		for (int curX = (int) a.getX(); curX <= b.getX(); curX++)
			for (int curZ = (int) a.getZ(); curZ <= b.getZ(); curZ++) {

				if ((tempItem = box.getItem((tempSlot = getSlotFromCoord(new Vector(curX, 0, curZ))))) != null &&
						tempItem.getType() != Material.AIR)
					continue;


				return parent.addMenuItem(tempSlot, item);
			}

		return false;
	}
}