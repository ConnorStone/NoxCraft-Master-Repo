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

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.mmo.classes.internal.ClassType;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.IClassTier;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.classes.tiers.AxesBasherClassTier;
import com.noxpvp.mmo.classes.tiers.AxesBerserkerClassTier;
import com.noxpvp.mmo.classes.tiers.AxesChampionClassTier;
import com.noxpvp.mmo.classes.tiers.AxesWarlordClassTier;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@DelegateDeserialization(PlayerClass.class)
public class AxesPlayerClass extends PlayerClass {

	public static final String className = "Axes";

	public static final String uniqueID = "f8c26f34fc36427ab92e94090b146db1";    //RANDOMLY GENERATED DO NOT CHANGE!

	public AxesPlayerClass(Player player) {
		super(uniqueID, className, player);
	}

	public AxesPlayerClass(UUID playerIdentifier, Player player) {
		super(uniqueID, className, playerIdentifier, player);
	}

	public AxesPlayerClass(UUID playerIdentifier) {
		super(uniqueID, className, playerIdentifier);
	}

	public String getDescription() {
		return "Needs description";
	}
	
	public boolean isPrimaryClass() {
		return true;
	}

	public ClassType getPrimaryClassType() {
		return ClassType.Axes;
	}

	public ClassType[] getSubClassTypes() {
		return new ClassType[0];
	}

	public int getHighestPossibleTier() {
		return 4;
	}

	@Override
	public ItemStack getIdentifiableItem() {
		ItemStack s = super.getIdentifiableItem();
		s.setType(Material.DIAMOND_AXE);
		
		return s;
	}

	public Color getRBGColor() {
		return Color.fromRGB(215, 0, 0);
	}

	public Color getBaseArmourColor() {
		return ((LeatherArmorMeta) new ItemStack(Material.LEATHER_HELMET).getItemMeta()).getColor(); //TODO: Move this to a variable. Also change the implementation. This was meant to have a base color for class then the getColor adds onto it!
	}

	public ChatColor getColor() {
		return ChatColor.RED;
	}

	public ExperienceType[] getExpTypes() {
		return getTier().getExpTypes();
	}

	public boolean canUseTier(int tier) {
		return true;//TODO this
	}

	@Override
	protected void load(Map<String, Object> data) {
		//Nothing extra to store just yet...
	}

	@Override
	protected Map<Integer, IClassTier> craftClassTiers() {
		this.tiers = new HashMap<Integer, IClassTier>();

		tiers.put(1, new AxesBasherClassTier(this));
		tiers.put(2, new AxesChampionClassTier(this));
		tiers.put(3, new AxesBerserkerClassTier(this));
		tiers.put(4, new AxesWarlordClassTier(this));

		return this.tiers;
	}

	@Override
	protected FileConfiguration getClassConfig() {
		// TODO Auto-generated method stub
		return null;
	}}
