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

package com.noxpvp.mmo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.MapMaker;
import com.noxpvp.core.data.OldBaseNoxPlayerAdapter;
import com.noxpvp.core.data.OldNoxPlayerAdapter;
import com.noxpvp.mmo.util.PlayerClassUtil;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.OldPersistant;
import com.noxpvp.core.annotation.Temporary;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.abilities.IPlayerAbility;
import com.noxpvp.mmo.classes.internal.DummyClass;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.classes.internal.PlayerClass;

public class OldMMOPlayer extends OldBaseNoxPlayerAdapter implements OldPersistant, MenuItemRepresentable {

	/** Not recommended for use **/
	private static final String PRIMARY_CLASS_NODE = "current.primary-class";
	private static final String PRIMARY_PLAYERCLASS_NODE = PRIMARY_CLASS_NODE + ".class";
	private static final String PRIMARY_TIER_NODE = PRIMARY_CLASS_NODE + ".tier";

	private static final String SECONDARY_CLASS_NODE = "current.secondary-class";
	private static final String SECONDARY_PLAYERCLASS_NODE = PRIMARY_CLASS_NODE + ".class";
	private static final String SECONDARY_TIER_NODE = PRIMARY_CLASS_NODE + ".tier";
	private static final String TARGET_NODE = "current.target";

	private static final String ABILITY_CYCLERS_NODE = "cyclers";
	private static final String PLAYER_CLASSES_NODE = "class-data";
	private IPlayerClass primaryClass = DummyClass.PRIMARY, secondaryClass = DummyClass.SECONDARY;
	private LivingEntity target;
	private ItemStack identifiableItem;

	private Map<String, PlayerClass> classes = new MapMaker().weakValues().concurrencyLevel(4).makeMap();

	public OldMMOPlayer(OfflinePlayer player) {
		super(player);
	}

	public OldMMOPlayer(String name) {
		super(name);
	}

	public OldMMOPlayer(OldNoxPlayerAdapter player) {
		super(player);
	}
	
	public ItemStack getIdentifiableItem() {
		if (identifiableItem == null) {
			identifiableItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			
			ItemMeta meta = identifiableItem.getItemMeta();
			
			meta.setDisplayName(new MessageBuilder().gold("Player: ").yellow(getFullName()).toString());
			meta.setLore(Arrays.asList("other info here"));
			
			identifiableItem.setItemMeta(meta);
		}
		
		return identifiableItem.clone();
	}

	@Temporary
	public List<Ability> getAllAbilities() {

		List<Ability> ret = new ArrayList<Ability>();
		if (getPrimaryClass() != null)
			ret.addAll(getPrimaryClass().getAbilities());
		if (getSecondaryClass() != null)
			ret.addAll(getSecondaryClass().getAbilities());

		return Collections.unmodifiableList(ret);
	}

	@Temporary
	public Map<String, Ability> getAllMappedAbilities() {
		Map<String, Ability> ret = new HashMap<String, Ability>();

		if (getPrimaryClass() != null)
			ret.putAll(getPrimaryClass().getAbilityMap());
		if (getSecondaryClass() != null)
			ret.putAll(getSecondaryClass().getAbilityMap());

		return Collections.unmodifiableMap(ret);
	}

	/**
	 * Retrieves a modifiable map that persists in the player object containing all class data.
	 * @return
	 */
	public Map<String, PlayerClass> getClassMap() {
		return classes;
	}

	public List<PlayerClass> getClasses() {
		return new ArrayList<PlayerClass>(getClassMap().values());
	}

	/*
	 * This will return a usable list object.
	 * You may add and remove from it and it should store it automatically!
	 * So long as the jvm considers it like a pointer to the real object. And not two objects.
	 *
	 * //TODO: Test to make sure data is storing properly..
	 */
	public List<AbilityCycler> getAbilityCyclers() {
		return getPersistantData().getList(ABILITY_CYCLERS_NODE, AbilityCycler.class, new ArrayList<AbilityCycler>());
	}

	public IPlayerClass getPrimaryClass() {
		return primaryClass;
	}

	public void setPrimaryClass(IPlayerClass c) {
		this.primaryClass = c;
		
		if (c.getTier() != null)
			getPlayer().setMaxHealth(primaryClass.getTier().getMaxHealth());
	}

	public IPlayerClass getSecondaryClass() {
		return secondaryClass;
	}

	public void setSecondaryClass(IPlayerClass c) {
		this.secondaryClass = c;
	}

	public void setClass(String c) {
		if (!PlayerClassUtil.hasClass(c))

		setClass(PlayerClassUtil.getClass(c, this));
	}

	public void setClass(IPlayerClass c) {
		if (c.isPrimaryClass())
			setPrimaryClass(c);
		else
			setSecondaryClass(c);

		superSave();
	}

	public void setPrimaryClass(String c) {
		if (LogicUtil.nullOrEmpty(c))
			setClass(DummyClass.PRIMARY);

		PlayerClass clazz;
		if ((clazz = PlayerClassUtil.getClass(c, this)) != null)
			setClass(clazz);

		return;
	}

	public void setSecondaryClass(String c) {
		if (LogicUtil.nullOrEmpty(c))
			setClass(DummyClass.SECONDARY);

		PlayerClass clazz;
		if ((clazz = PlayerClassUtil.getClass(c, this)) != null)
			setClass(clazz);

		return;
	}

	public LivingEntity getTarget() {
		return target;
	}

	public void setTarget(LivingEntity entity) {
		this.target = entity;
	}

	public IPlayerAbility[] getPlayerAbilities() {
		throw new NotImplementedException();
	}

	public IPassiveAbility<?>[] getPassiveAbilities() {
		throw new NotImplementedException();
	}

	public void addExp(ExperienceType type, int amount) {
		throw new NotImplementedException();
		
		/*
		 * If successful, do this
		 */
//		Player p = getMMOPlayer();
//		p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10, 0);
	}

	public void load() {
		ConfigurationNode node = getPersistantData();
		setPrimaryClass(node.get(PRIMARY_CLASS_NODE, ""));
		getPrimaryClass().setCurrentTier(node.get(PRIMARY_TIER_NODE, Integer.class, 1));

		setSecondaryClass(node.get(SECONDARY_CLASS_NODE, ""));
		getSecondaryClass().setCurrentTier(node.get(SECONDARY_TIER_NODE, Integer.class, 1));
		
		/*
		 * Separate the scope Just in case we want to use the variables again.
		 */
		{
			String uid = node.get(TARGET_NODE + ".uuid", String.class);
			String worldName = node.get(TARGET_NODE + ".world", String.class);

			// check for possible null world name before getting world, as bukkit will freak out
			World world = worldName == null ? null : Bukkit.getWorld(worldName);

			if (!LogicUtil.nullOrEmpty(uid) && world != null)
				setTarget(EntityUtil.getEntity(world, UUID.fromString(uid), LivingEntity.class));
		}

		node.set(PRIMARY_PLAYERCLASS_NODE, getPrimaryClass().getUniqueID());
		node.set(PRIMARY_TIER_NODE, getPrimaryClass().getCurrentTierLevel());

		node.set(SECONDARY_PLAYERCLASS_NODE, getSecondaryClass().getUniqueID());
		node.set(SECONDARY_TIER_NODE, getSecondaryClass().getCurrentTierLevel());

		List<PlayerClass> classes = node.getList(PLAYER_CLASSES_NODE, PlayerClass.class, new ArrayList<PlayerClass>());
		for (PlayerClass c : classes)
			this.classes.put(c.getUniqueID(), c);
	}

	/**
	 * Warning this does not save to file. You must call saveToManager() method after using this method.
	 * DO NOT USE saveToManager() in this method. It will infinite loop. Or you can use saveToManager() without this... It will work just fine.
	 */
	public void save() {
		ConfigurationNode node = getPersistantData();
		if (getPrimaryClass() == null)
			node.remove(PRIMARY_CLASS_NODE);
		else {
			node.set(PRIMARY_PLAYERCLASS_NODE, getPrimaryClass().getUniqueID());
			node.set(PRIMARY_TIER_NODE, getPrimaryClass().getCurrentTierLevel());
		}

		if (getSecondaryClass() == null)
			node.remove(SECONDARY_CLASS_NODE);
		else {
			node.set(SECONDARY_PLAYERCLASS_NODE, getSecondaryClass().getUniqueID());
			node.set(SECONDARY_TIER_NODE, getSecondaryClass().getCurrentTierLevel());
		}

		if (getTarget() == null)
			node.remove(TARGET_NODE);
		else {
			node.set(TARGET_NODE + ".world", getTarget().getWorld().getName());
			node.set(TARGET_NODE + ".uuid", getTarget().getUniqueId().toString());
		}

		node.set(PLAYER_CLASSES_NODE, PlayerClassUtil.getAllChangedPlayerClasses(this));
	}
}
