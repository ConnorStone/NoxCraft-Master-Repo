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

package com.noxpvp.mmo.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.mmo.abilities.player.ChargePlayerAbility;
import com.noxpvp.mmo.abilities.player.LeapPlayerAbility;
import com.noxpvp.mmo.abilities.player.MassDestructionPlayerAbility;
import com.noxpvp.mmo.abilities.player.RagePlayerAbility;
import com.noxpvp.mmo.abilities.player.WallopPlayerAbility;
import com.noxpvp.mmo.classes.internal.ClassType;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.PlayerClass;

public class WarlordPlayerClass extends PlayerClass {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static final String	className	= "Warlord";
	private static final Color	armourColor	= Color.RED;
	private static final int	maxLevel	= 50;
	private static final double	maxHealth	= 20D;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public WarlordPlayerClass(UUID playerUUID) {
		super(className, playerUUID, DEFAULT_FORMULA);
		
		addNewAbilities();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public Color getBaseArmourColor() {
		return armourColor;
	}
	
	public ChatColor getColor() {
		return ChatColor.RED;
	}
	
	public String getDescription() {
		return "Needs description";
	}
	
	public ExperienceType[] getExpTypes() {
		return null;
	}
	
	@Override
	public ItemStack getIdentifiableItem() {
		final ItemStack s = super.getIdentifiableItem();
		s.setType(Material.DIAMOND_AXE);
		
		return s;
	}
	
	public double getMaxHealth() {
		return maxHealth;
	}
	
	@Override
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public ClassType getPrimaryClassType() {
		return ClassType.Axes;
	}
	
	public Color getRBGColor() {
		return Color.fromRGB(215, 0, 0);
	}
	
	public ClassType[] getSubClassTypes() {
		return new ClassType[0];
	}
	
	public boolean isPrimaryClass() {
		return true;
	}
	
	private void addNewAbilities() {
		final Player p = getPlayer();
		
		// Wallop
		{
			final Map<Integer, Integer> wallopLevels = new HashMap<Integer, Integer>();
			wallopLevels.put(1, 1);
			wallopLevels.put(10, 2);
			wallopLevels.put(20, 3);
			wallopLevels.put(30, 4);
			addAbility(new WallopPlayerAbility(p), wallopLevels);
		}
		
		// Charge
		{
			final Map<Integer, Integer> chargeLevels = new HashMap<Integer, Integer>();
			chargeLevels.put(7, 1);
			chargeLevels.put(17, 2);
			chargeLevels.put(27, 3);
			chargeLevels.put(37, 4);
			addAbility(new ChargePlayerAbility(p), chargeLevels);
		}
		
		// Mass destruction
		{
			final Map<Integer, Integer> massdestructionLevels = new HashMap<Integer, Integer>();
			massdestructionLevels.put(14, 1);
			massdestructionLevels.put(24, 2);
			massdestructionLevels.put(34, 3);
			massdestructionLevels.put(44, 4);
			addAbility(new MassDestructionPlayerAbility(p), massdestructionLevels);
		}
		
		// Leap
		{
			final Map<Integer, Integer> leapLevels = new HashMap<Integer, Integer>();
			leapLevels.put(21, 1);
			leapLevels.put(31, 2);
			leapLevels.put(41, 3);
			leapLevels.put(51, 4);
			addAbility(new LeapPlayerAbility(p), leapLevels);
		}
		
		// Rage
		{
			final Map<Integer, Integer> rageLevels = new HashMap<Integer, Integer>();
			rageLevels.put(28, 1);
			rageLevels.put(38, 2);
			rageLevels.put(48, 3);
			rageLevels.put(58, 4);
			addAbility(new RagePlayerAbility(p), rageLevels);
		}
	}
	
}
