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

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.AbilityContainer;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;

public interface IPlayerClass extends AbilityContainer<PlayerAbility>,
		MenuItemRepresentable, ConfigurationSerializable, IExperienceHolder {
	
	/**
	 * Checks if player can use this class.
	 * 
	 * @return
	 */
	public boolean canUseClass();
	
	/**
	 * Retrieves the unmodified unmerged coloring of the armour.
	 * 
	 * @return Color armour color object.
	 */
	public Color getBaseArmourColor();
	
	/**
	 * Retrieves the color associated with chat and other chat related
	 * messages.
	 * 
	 * @return ChatColor
	 */
	public ChatColor getColor();
	
	/**
	 * Gets the name of this class, prefixed with the class color
	 * 
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Returns a list of the {@link #getDescription()}
	 * 
	 * @return list<String> lore
	 */
	public List<String> getLore();
	
	/**
	 * Gets the custom max health for the user of this class
	 * 
	 * @return
	 */
	public double getMaxHealth();
	
	/**
	 * Gets the MMO player user of this class
	 * 
	 * @return
	 */
	public MMOPlayer getMMOPlayer();
	
	/**
	 * Gets the name of this class
	 * 
	 * @return
	 */
	public String getName();
	
	public OfflinePlayer getPlayer();
	
	/**
	 * Retrieves the color used for coloring armour.
	 * 
	 * @return Color armour color object.
	 */
	public Color getRBGColor();
	
	/**
	 * Tells whether or not this is a primary class. Meaning the player
	 * sets this as their main display class. <br>
	 * If this is not a primary class. It is set as a skill type class
	 * (Skill type? not sure of yet.)
	 * 
	 * @return true if primary and false other wise.
	 */
	public boolean isPrimaryClass();
	
	public void setPlayer(OfflinePlayer player);
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Special Color Blending techniques
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static enum BLEND_MODE {
		AVERAGE, ADDITIVE, SUBTRACTIVE, MULTIPLE, OVERRIDE, CANCEL;
		
		public Color blend(Color first, Color second) {
			if (equals(OVERRIDE))
				return second;
			else if (equals(CANCEL))
				return first;
			
			final int r = first.getRed(), g = first.getGreen(), b = first.getBlue();
			final int r2 = second.getRed(), g2 = second.getGreen(), b2 = second
					.getBlue();
			
			if (equals(ADDITIVE))
				return Color.fromRGB(Math.min(r + r2, 255), Math.min(g + g2, 255),
						Math.min(b + b2, 255));
			else if (equals(AVERAGE))
				return Color.fromRGB((r + r2) / 2, (g + g2) / 2, (b + b2) / 2);
			
			return null; // FIXME: Finish
		}
	}
	
}
