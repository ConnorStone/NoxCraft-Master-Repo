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

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.data.player.BasePluginPlayer;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.AbilityContainer;
import com.noxpvp.mmo.classes.PlayerClassContainer;
import com.noxpvp.mmo.classes.internal.DummyClass;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class MMOPlayer extends BasePluginPlayer<NoxMMO> implements MenuItemRepresentable, PlayerClassContainer, AbilityContainer<Ability> {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Static Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static ModuleLogger logger;

	static {
		logger = MMOPlayerManager.getInstance().getModuleLogger("MMOPlayer");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private List<PlayerClass> all_classes;
	private Map<String, PlayerClass> classCache;
	private IPlayerClass currentPrimaryClass = DummyClass.PRIMARY, currentSecondaryClass = DummyClass.SECONDARY;

	private ItemStack identifyingItem;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public MMOPlayer(UUID playerUUID) {
		super(playerUUID);
	}

	public MMOPlayer(Player player) {
		super(player);
	}

	public MMOPlayer(Map<String, Object> data) {
		super(data);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Experience
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void addExp(ExperienceType pvp, int floor) {

	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Classes
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public IPlayerClass getPrimaryClass() {
		return null;
	}

	public IPlayerClass getSecondaryClass() {
		return null;
	}

	public void setClass(PlayerClass clazz) {

	}

	public void setSecondaryClass(PlayerClass playerClass) {

	}

	public void setPrimaryClass(PlayerClass playerClass) {

	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Targetting
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void setTarget(LivingEntity livingEntity) {

	}

	public LivingEntity getTarget() {
		return null;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: AbilitiesContainer
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Ability getAbility(String identity) {
		return null;
	}

	public boolean hasAbility(String identity) {
		return false;
	}

	public Collection<Ability> getAbilities() {
		return null;
	}

	public Map getAbilitiesMap() {
		return null;
	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: PlayerClassContainer
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public boolean hasPlayerClass(String identifier) {
		return false;
	}

	public PlayerClass getPlayerClass(String identifier) {
		return null;
	}

	public List<PlayerClass> getPlayerClasses() {
		return null;
	}

	public boolean addPlayerClass(IPlayerClass clazz) {
		return false;
	}

	public boolean removePlayerClass(String identifier) {
		return false;
	}

	public boolean removePlayerClass(IPlayerClass clazz) {
		return false;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: MenuItemRepresentable
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public ItemStack getIdentifiableItem() {
		return this.identifyingItem;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: PluginPlayer
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void log(Level level, String msg) {
		logger.log(level, msg);
	}

	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = super.serialize();

		return data;
	}

	public String getPersistenceNode() {
		return "MMOPlayer";
	}

	public Map<String, PlayerClass> getClassMap() {
		return classCache;
	}

	public List<AbilityCycler> getAbilityCyclers() {
		return null;
	}
}
