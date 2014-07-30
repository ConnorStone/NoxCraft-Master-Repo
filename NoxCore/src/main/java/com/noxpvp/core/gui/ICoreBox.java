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

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface ICoreBox {

	/**
	 * Gets the name of this corebox
	 *
	 * @return String name
	 */
	public String getName();

	/**
	 * Gets the inventory of this box
	 *
	 * @return Inventory
	 */
	public Inventory getBox();
	
	/**
	 * Get the previous box before this one, if any
	 * 
	 * @return {@link ICoreBox} previous menu
	 */
	public ICoreBox getBackButton();

	/**
	 * Gets the player this box was created for
	 *
	 * @return Player the player
	 */
	public Player getPlayer();

	/**
	 * Shows the player the non-editable box
	 */
	public void show();

	/**
	 * Closes the inventory of the player has it open
	 */
	public void hide();

	/**
	 * Adds a given item to the menu and the given slot
	 *
	 * @param slot
	 * @param item
	 * @return True if the item was successfully added, false otherwise
	 */
	public boolean addMenuItem(int slot, CoreBoxItem item);

	/**
	 * Removes a given item from the menu
	 *
	 * @param item
	 * @return True if successful, false otherwise
	 */
	public boolean removeMenuItem(CoreBoxItem item);

	/**
	 * Removes any {@link CoreBoxItem} in this menu at the given slot
	 *
	 * @param slot
	 */
	public void removeMenuItem(int slot);

	/**
	 * Gets the menu item (if any) from this slot in the menu
	 *
	 * @param slot
	 * @return {@link CoreBoxItem} that was found, null if the slot had no item
	 */
	public CoreBoxItem getMenuItem(int slot);

	/**
	 * Handles the code of what should go on if a player clicks on an item in the box
	 *
	 * @param event
	 */
	public void clickHandler(InventoryClickEvent event);

	/**
	 * Handles the code of what should go on if a player closes the box
	 *
	 * @param event
	 */
	public void closeHandler(InventoryCloseEvent event);

	public interface ICoreBoxItem {

		/**
		 * Gets the CoreBox that this item is contained in
		 *
		 * @return CoreBox
		 */
		public CoreBox getParentBox();

		/**
		 * Gets the contained itemstack of this box item
		 *
		 * @return ItemStack
		 */
		public ItemStack getItem();

		/**
		 * Handles the code of what should go on if a player clicks this item, not necessary for most implementations
		 *
		 * @param click
		 */
		public boolean onClick(InventoryClickEvent click);

	}

}
