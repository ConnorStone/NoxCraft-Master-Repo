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

package com.noxpvp.mmo.abilities.internal;

import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.mmo.abilities.AbilityResult;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * <b>IF this is not a dynamic ability. <br/> With changing names. Please specify a static getName() method. </b>
 *
 * @author Chris
 */
public interface Ability extends MenuItemRepresentable {

	/**
	 * Retrieves the ability name.
	 *
	 * @return Ability Name
	 */
	public String getName();
	
	public String getUniqueId();

	/**
	 * Retrieves the description of the ability.
	 * <p> Mainly used for command information purpose.
	 *
	 * @return description of the ability.
	 */
	public String getDescription();

	/**
	 * Retrieves the description of the ability for use as itemmeta.
	 * <p> Mainly used for itemmeta and other uses of List<String>.
	 *
	 * @return Description of the ability;
	 */
	public List<String> getLore(int lineLength);

	/**
	 * Retrieves the description of the ability for use as itemmeta in the specified color.
	 * <p> Mainly used for itemmeta and other uses of List<String>.
	 *
	 * @return Description of the ability;
	 */
	public List<String> getLore(ChatColor color, int lineLength);

	/**
	 * Same as {@link MenuItemRepresentable#getIdentifiableItem()} but with special additions depending on if the player can use it 
	 * 
	 * @param canUse
	 * @return
	 */
	public ItemStack getIdentifiableItem(boolean canUse);
	
	/**
	 * Executes an ability
	 *
	 * @return AbilityResult
	 */
	public AbilityResult execute();

	/**
	 * Retrieves the display name locale of this ability.
	 *
	 * @return Colored String Value
	 */
	public String getDisplayName();

	/**
	 * The same as {@link #getDisplayName()} but prefixed with a specific color
	 *
	 * @param color
	 * @return
	 */
	public String getDisplayName(ChatColor color);

	/**
	 * Tells whether or not an ability should be allowed to be executed.
	 *
	 * @return true if allowed and false if not.
	 */
	public boolean mayExecute();
}
