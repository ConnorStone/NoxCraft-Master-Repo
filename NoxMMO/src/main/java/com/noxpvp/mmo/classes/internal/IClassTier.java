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

import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.abilities.AbilityContainer;

import java.util.List;
import java.util.Map;

public interface IClassTier extends AbilityContainer<Ability> {

	public int getTierLevel();

	public String getName();

	public boolean canUse();

	/**
	 * This is not the bukkit api and is not included with configuration serialization.
	 *
	 * This is due to the way player class serialization works. This class has this method to manually deserialize stuff into class tiers.
	 *
	 * @return
	 */
	public Map<String, Object> serialize();

	public PlayerClass getRetainingClass();

	public String getDisplayName();

	public void setDisplayName(String displayName);

	public List<String> getLore();

	public double getMaxHealth();

	public int getMaxExp();

	public int getMaxExp(int level);

	public int getNeededExp();

	public int getNeededExp(int level);

	public int getExp();

	public void setExp(int amount);

	public int getExp(int level);

	public int getTotalExp();

	public void setTotalExp(int amount);

	public int getLevel();

	public void setLevel(int level);

	public boolean canUseLevel(int level);

	public int getMaxLevel();

	public void addExp(int amount);

	public void removeExp(int amount);

	public ExperienceType[] getExpTypes();

	/**
	 * Dematerializes data from serialized data map.
	 */
	public void onLoad(Map<String, Object> data);

	/**
	 * Used to cleanup data if max exp per level changes and such. <br/>
	 * This is a very intensive operation and must be used sparingly. <br/>
	 * <br/>
	 * This method should include usage of the setTotalExp(int value);
	 * <br/>
	 * This will allow auto fixing values when manually setting total exp. Thus cleanup can include that set.
	 */
	public void update();

	String getPermission();

	public void onSave(Map<String, Object> tier);
}
