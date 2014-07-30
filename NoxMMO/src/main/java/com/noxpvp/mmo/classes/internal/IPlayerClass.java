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


package com.noxpvp.mmo.classes.internal;

import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.abilities.AbilityContainer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public interface IPlayerClass extends MenuItemRepresentable, ConfigurationSerializable, AbilityContainer<Ability> {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Identity
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * A unique id for classes. <br/><br/>
	 * <b>DO NOT INCLUDE TIER DATA INTO THIS!</b>
	 *
	 * @return
	 */
	public String getUniqueID();

	/**
	 * Gets the name of this class
	 * 
	 * @return
	 */
	public String getName();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Descriptors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the current display name of the class. <br/>
	 * <br/>
	 * Majority of time this will reflect whatever the current tier is.
	 *
	 * @return DisplayName String value of class that is displayed to the player.
	 */
	public String getDisplayName();

	/**
	 * Gets the name of this class, prefixed with the class color
	 * 
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Same as @link {@link #getDescription()} but with given color
	 * 
	 * @param color
	 * @return {@link String} colored description
	 */
	public String getDescription(ChatColor color);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Lores
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns a list of the {@link #getDescription()}
	 * 
	 * @return list<String> lore
	 */
	public List<String> getLore();
	
	/**
	 * Return the same as @link {@link #getLore()} split to given length for chat/item lore
	 * 
	 * @param lineLength
	 * @return {@link List<String>} The
	 */
	public List<String> getLore(int lineLength);
	
	/**
	 * The same as {@link #getLore(int)}, with each string in the list prefixed with the given color
	 * 
	 * @param color
	 * @param lineLength
	 * @return
	 */
	public List<String> getLore(ChatColor color, int lineLength);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Informational
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tells whether or not this is a primary class. Meaning the player sets this as their main display class. <br>
	 * If this is not a primary class. It is set as a skill type class (Skill type? not sure of yet.)
	 *
	 * @return true if primary and false other wise.
	 */
	public boolean isPrimaryClass();

	public ExperienceType[] getExpTypes();

	/**
	 * This defines the classes description and selection system.
	 *
	 * @return ClassType
	 */
	public ClassType getPrimaryClassType();

	/**
	 * This defines the classes description and selection system. <br/>
	 * This will include anything that is partial to it.
	 *
	 * @return ClassType
	 */
	public ClassType[] getSubClassTypes();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Tiers
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Retrieves the current tier level.
	 *
	 * @return currentTierLevel int value
	 */
	public int getCurrentTierLevel();

	public void setCurrentTier(IClassTier tier); //FIXME: Javadocs

	public void setCurrentTier(int tierLevel);

	/**
	 * Retrieves the tier number for the one without maxed out exp.
	 *
	 * @return highest tier without capped exp.
	 */
	public int getLatestTier();


	/**
	 * Tells the highest tier the player is allowed to use.
	 *
	 * @return highestTier
	 */
	public int getHighestAllowedTier();

	/**
	 * Retrieves the highest possible tier level for this class. <br/><br/>
	 * This includes stuff not accesable to the specific player. This is interal max tier count checks.
	 *
	 * @return highestTier
	 */
	public int getHighestPossibleTier();

	/**
	 * Tells whether or not that tier level exists.
	 *
	 * @param level the level to check for.
	 * @return true if exists and false other wise.
	 */
	public boolean hasTier(int level);

	/**
	 *
	 * Returns the current tier selected.
	 *
	 * @return currentTier IClassTier object.
	 */
	public IClassTier getTier();

	/**
	 * Grabs the specified tier object.
	 * <br/>
	 * May return null if non existant.
	 *
	 * @param tier to grab
	 * @return tier IClassTier object and will return if non existant.
	 */
	public IClassTier getTier(int tier);

	/**
	 * Returns all tiers that are possible to get even if locked from user.
	 *
	 * @return tiers A set of Entries of <Integer, IClassTier>
	 */
	public Set<Entry<Integer, IClassTier>> getTiers();


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Level Data
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Retrieve the current level of the current tier of the class.
	 *
	 * @return currentLevel integer value of current level.
	 */
	public int getLevel();

	/**
	 * Retrieve the current level of the specified tier of the class.
	 *
	 * @param tier to retrieve values from.
	 * @return level integer value of current level.
	 */
	public int getLevel(int tier);

	/**
	 * Retrieves the max level for the current tier of the class.
	 *
	 * @return maxLeve integer value of max level allowed.
	 */
	public int getMaxLevel();

	/**
	 * Retrieves the max level for the current tier of the class.
	 *
	 * @param tier to retrieve values from.
	 * @return integer value of max level allowed.
	 */
	public int getMaxLevel(int tier);

	/**
	 * Retrieves the combined total of all tiers levels put together.
	 *
	 * @return totalLevel
	 */
	public int getTotalLevel();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//EXP Data
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//TODO: Javadocs.
	public void setExp(int tier, int amount);

	public void addExp(int amount);

	public void addExp(int tier, int amount);

	public void removeExp(int amount);

	public void removeExp(int tier, int amount);

	/**
	 * Retrieves the current tiers exp value.
	 *
	 * @return currentExp integer value of how much exp the tier has.
	 */
	public int getExp();

	public void setExp(int amount);

	/**
	 * Retrieves the exp for the specified tier.
	 *
	 * @param tier to retrieve values from.
	 * @return exp integer value of how much exp the tier has.
	 */
	public int getExp(int tier);

	/**
	 * Gets the amount of exp needed to reach the next level in the current tier
	 *
	 * @return exp Integer value needed to level
	 */
	public int getExpToLevel();

	/**
	 * Retrieves all exp from all tiers combined.
	 *
	 * @return
	 */
	public int getTotalExp();

	/**
	 * Retrieve the max experience allowed in the current tier.
	 *
	 * @return maxExp integer value of max experience allowed.
	 */
	public int getMaxExp();

	/**
	 * Retrieves the max experience allowed in the selected tier.
	 *
	 * @param tier to retrieve values from.
	 * @return maxExp integer value of max experience allowed.
	 */
	public int getMaxExp(int tier);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Coloring
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Retrieves the color associated with chat and other chat related messages. (COLOR CODES RELATED)
	 *
	 * @return ChatColor
	 */
	public ChatColor getColor();

	/**
	 * Retrieves the color used for coloring armour.
	 *
	 * @return Color armour color  object.
	 */
	public Color getRBGColor();



	/**
	 * Retrieves the unmodified unmerged coloring of the armour.
	 *
	 * @return Color armour color object.
	 */
	public Color getBaseArmourColor();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Restrictions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks if player can use this class.
	 *
	 * @return
	 */
	public boolean canUseClass();

	/**
	 * Retrieves whether or not a player can use specified tier.
	 *
	 * @param tier to check for permission
	 * @return true if allowed and false otherwise.
	 */
	public boolean canUseTier(int tier);



	//TODO: Should we use this?
//	/**
//	 * Sets the users current level on specified tier.
//	 * @param level int object to set.
//	 */
//	public void setLevel(int level);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Special Color Blending techniques
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static enum BLEND_MODE {
		AVERAGE, ADDITIVE, SUBTRACTIVE, MULTIPLE, OVERRIDE, CANCEL;

		public Color blend(Color first, Color second) {
			if (this.equals(OVERRIDE))
				return second;
			else if (this.equals(CANCEL))
				return first;

			int r = first.getRed(), g = first.getGreen(), b = first.getBlue();
			int r2 = second.getRed(), g2 = second.getGreen(), b2 = second.getBlue();

			if (this.equals(ADDITIVE))
				return Color.fromRGB(Math.min((r + r2), 255), Math.min((g + g2), 255), Math.min((b + b2), 255));
			else if (this.equals(AVERAGE))
				return Color.fromRGB(((r+r2)/2), ((g+g2)/2), ((b+b2)/2));

			return null; //FIXME: Finish
		}
	}

	//TODO: Plan out more.
//	public void onConfigLoad();
//	public void onConfigSave();

//	public String[] getAllAbilities();
//	public String[] getAllowedAbilities(); //TODO: Better Name- that better?
}
