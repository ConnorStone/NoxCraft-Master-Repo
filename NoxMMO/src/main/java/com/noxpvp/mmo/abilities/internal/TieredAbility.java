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

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BaseAbilityTier;

/**
 * <b>IF this is not a dynamic ability. <br/>
 * With changing names. Please specify a static getName() method. </b>
 * 
 * @author Chris
 */
public interface TieredAbility<T extends BaseAbilityTier<? extends TieredAbility>>
		extends MenuItemRepresentable {
	
	/**
	 * Add a tier to this ability, and removes any tier already in this
	 * ability with the same tier level
	 * 
	 * @param tier
	 * @return True if the ability was successfuly added, otherwise false
	 */
	public boolean addTier(T tier);
	
	/**
	 * Executes this ability at the current tier level, as provided by
	 * {@link #getCurrentTier()}
	 * 
	 * @return
	 */
	public AbilityResult<?> execute();
	
	/**
	 * Executes this ability at the given tier level
	 * 
	 * @param tierLevel
	 * @return
	 */
	public AbilityResult<?> execute(int tierLevel);
	
	/**
	 * Returns the current tier level that this ability will use when
	 * executing. Defaults to 1, can be set through
	 * {@link #setCurrentTier(int)}
	 * 
	 * @return
	 */
	public int getCurrentTier();
	
	/**
	 * Retrieves the description of the ability.
	 * <p>
	 * Mainly used for command information purpose.
	 * 
	 * @return description of the ability.
	 */
	public String getDescription();
	
	/**
	 * Retrieves the display name locale of this ability.
	 * 
	 * @return Colored String Value
	 */
	public String getDisplayName();
	
	/**
	 * The same as {@link #getDisplayName()} but prefixed with a specific
	 * color
	 * 
	 * @param color
	 * @return
	 */
	public String getDisplayName(ChatColor color);
	
	/**
	 * Same as {@link MenuItemRepresentable#getIdentifiableItem()} but with
	 * special additions depending on if the player can use it
	 * 
	 * @param canUse
	 * @return
	 */
	public ItemStack getIdentifiableItem(boolean canUse);
	
	/**
	 * Retrieves the description of the ability for use as itemmeta in the
	 * specified color.
	 * <p>
	 * Mainly used for itemmeta and other uses of List<String>.
	 * 
	 * @return Description of the ability;
	 */
	public List<String> getLore(ChatColor color, int lineLength);
	
	/**
	 * Retrieves the description of the ability for use as itemmeta.
	 * <p>
	 * Mainly used for itemmeta and other uses of List<String>.
	 * 
	 * @return Description of the ability;
	 */
	public List<String> getLore(int lineLength);
	
	/**
	 * Retrieves the ability name.
	 * 
	 * @return Ability Name
	 */
	public String getName();
	
	/**
	 * Gets the tier (if any) in this ability with thi given level
	 * 
	 * @param level
	 * @return
	 */
	public T getTier(int level);
	
	/**
	 * Returns a set of the tiers currently apart of this ability
	 * 
	 * @return
	 */
	public Set<T> getTiers();
	
	/**
	 * Returns a unique, <b>Non persistent</b> id for this ability
	 * 
	 * @return
	 */
	public String getUniqueId();
	
	/**
	 * Checks if this ability currently has a tier at the given tier level.
	 * 
	 * @param level
	 * @return True if has tier at given level, otherwise false.
	 */
	public boolean hasTier(int level);
	
	/**
	 * Tells whether or not an ability should be allowed to be executed.
	 * 
	 * @return true if allowed and false if not.
	 */
	public boolean mayExecute();
	
	/**
	 * Removes the tier in this ability at the given level, if any.
	 * 
	 * @param level
	 * @return True if the tiers in the ability were changed by this
	 *         operation, otherwise false.
	 */
	public boolean removeTier(int level);
	
	/**
	 * Sets the current tier level for this ability to use
	 * 
	 * @param tierlevel
	 */
	public void setCurrentTier(int tierlevel);
	
}
