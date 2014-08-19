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

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;

public class BukkitUtil {
	
	public static boolean	useOnlineWorkaround	= true;
	
	/**
	 * Safely gets all online players.
	 * <p>
	 * This was made to help with build dependant issues with the new
	 * update from {@code Player[] to Collection<? extends Player>}
	 * </p>
	 * 
	 * @return {@code Collection<? extends Player>} of online players.
	 */
	public static Collection<? extends Player> getOnlinePlayers() {
		if (true || useOnlineWorkaround) {
			try {
				if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0])
				        .getReturnType() == Collection.class)
					return ((Collection<? extends Player>) Bukkit.class.getMethod(
					        "getOnlinePlayers", new Class<?>[0]).invoke(null,
					        new Object[0]));
				else
					return Lists.newArrayList((Player[]) Bukkit.class.getMethod(
					        "getOnlinePlayers", new Class<?>[0]).invoke(null,
					        new Object[0]));
			} catch (NoSuchMethodException ex) {
			} // can never happen
			catch (InvocationTargetException ex) {
			} // can also never happen
			catch (IllegalAccessException ex) {
			} // can still never happen
		} else {
			// return Bukkit.getOnlinePlayers(); //FIXME: Check for spiggot
			// compat.
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Retrieves the amount of players on the server.
	 * 
	 * @return int value of a count of online players.
	 */
	public static int getOnlinePlayerCount() {
		return getOnlinePlayers().size();
	}
	
	/**
	 * Wrapping method to
	 * {@link Bukkit#createInventory(org.bukkit.inventory.InventoryHolder, org.bukkit.event.inventory.InventoryType)}
	 * 
	 * @param inventoryHolder
	 * @param inventoryType
	 * @return Inventory
	 */
	public static Inventory createInventory(
	        InventoryHolder inventoryHolder,
	        InventoryType inventoryType) {
		return Bukkit.createInventory(inventoryHolder, inventoryType);
	}

	/**
	 * Wrapping method to
	 * {@link Bukkit#createInventory(org.bukkit.inventory.InventoryHolder, org.bukkit.event.inventory.InventoryType, String)}
	 * @param inventoryHolder
	 * @param inventoryType
	 * @param title
	 * @return Inventory
	 */
	public static Inventory createInventory(
	        InventoryHolder inventoryHolder,
	        InventoryType inventoryType,
	        String title) {
		return Bukkit.createInventory(inventoryHolder, inventoryType, title);
	}

	/**
	 * Wrapping method to
	 * {@link Bukkit#createInventory(org.bukkit.inventory.InventoryHolder, int)}
	 * <br/>
	 * <br/>
	 * This will safely create an inventory. If the size is incorrect.
	 * It shall round the size up to the next valid size.
	 *
	 * @param inventoryHolder
	 * @param size
	 * @return Inventory or null if failed.
	 */
	public static Inventory createInventory(
	        InventoryHolder inventoryHolder, int size) {
		try {
			return Bukkit
					.createInventory(inventoryHolder, fixInventorySize(size));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Wrapping method to
	 * {@link Bukkit#createInventory(org.bukkit.inventory.InventoryHolder, int, String)}
	 * @param inventoryHolder
	 * @param size
	 * @param title
	 * @return Inventory or null if failed.
	 */
	public static Inventory createInventory(
	        InventoryHolder inventoryHolder, int size,
	        String title) {
		try {
			return Bukkit
					.createInventory(inventoryHolder, fixInventorySize(size), title);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	private static int fixInventorySize(int size) {
		if (size % 9 != 0)
			return (size - (size%9)) + 9; //Braces are to make this more readable.
		return size;
	}
}
