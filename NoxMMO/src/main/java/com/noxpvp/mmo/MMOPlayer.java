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

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.data.player.BasePluginPlayer;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.mmo.abilities.AbilityContainer;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.classes.PlayerClassContainer;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.manager.AbilityCyclerManager;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.util.PlayerClassUtil;

@SerializableAs("MMOPlayer")
public class MMOPlayer extends BasePluginPlayer<NoxMMO> implements
		MenuItemRepresentable, PlayerClassContainer,
		AbilityContainer<PlayerAbility> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Init
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	static {
		logger = MMOPlayerManager.getInstance().getModuleLogger("MMOPlayer");
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static final String			SERIALIZE_ALL_CLASSES				= "classes";
	public static final String			SERIALIZE_ABLILTY_CYCLERS			= "ability-cyclers";
	public static final String			SERIALIZE_CURRENT_PRIMARY_CLASS		= "current-primary-class";
	public static final String			SERIALIZE_CURRENT_SECONDARY_CLASS	= "current-secondary-class";
	private static ModuleLogger			logger;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private Set<IPlayerClass>			allClasses;
	private final Set<AbilityCycler>	cyclers;
	private String						currentPrimaryClass;
	private String						currentSecondaryClass;
	
	private ItemStack					identifyingItem;
	private WeakReference<LivingEntity>	targetRef;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public MMOPlayer(final Map<String, Object> data) {
		super(data);
		
		setupClasses(data);
		
		List<String> keys;
		Object getter;
		
		if ((getter = data.get(SERIALIZE_ABLILTY_CYCLERS)) != null
				&& getter instanceof List) {
			
			keys = (List<String>) getter;
			cyclers = new HashSet<AbilityCycler>();
			final AbilityCyclerManager acm = AbilityCyclerManager.getInstance();
			
			for (final String key : keys) {
				final UUID id = UUID.fromString(key);
				
				if (key != null) {
					cyclers.add(acm.getCycler(id));
				}
			}
			
		} else {
			cyclers = new HashSet<AbilityCycler>();
		}
		
	}
	
	public MMOPlayer(Player player) {
		this(player.getUniqueId());
	}
	
	public MMOPlayer(UUID playerUUID) {
		super(playerUUID);
		
		allClasses = new HashSet<IPlayerClass>();
		cyclers = new HashSet<AbilityCycler>();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public boolean addPlayerClass(IPlayerClass clazz) {
		if (clazz instanceof PlayerClass)
			return addPlayerClass((PlayerClass) clazz);
		return false;
	}
	
	public boolean addPlayerClass(PlayerClass clazz) {
		if (!allClasses.contains(clazz))
			return allClasses.add(clazz);
		
		return false;
	}
	
	public Set<PlayerAbility> getAbilities() {
		final Set<PlayerAbility> ret = new HashSet<PlayerAbility>();
		
		if (getPrimaryClass() != null) {
			for (final PlayerAbility a : getPrimaryClass().getAbilities()) {
				ret.add(a);
			}
		}
		
		if (getSecondaryClass() != null) {
			for (final PlayerAbility a : getSecondaryClass().getAbilities()) {
				ret.add(a);
			}
		}
		
		return ret;
	}
	
	public PlayerAbility getAbility(String name) {
		for (final PlayerAbility a : getAbilities())
			if (a.getName().equalsIgnoreCase(name))
				return a;
		
		return null;
	}
	
	public Set<AbilityCycler> getAbilityCyclers() {
		return cyclers;
	}
	
	public ItemStack getIdentifiableItem() {
		return identifyingItem;
	}
	
	public String getPersistenceNode() {
		return "MMOPlayer";
	}
	
	public IPlayerClass getPlayerClass(String name) {
		for (final IPlayerClass c : allClasses)
			if (c.getName().equalsIgnoreCase(name))
				return c;
		
		return null;
	}
	
	public Set<IPlayerClass> getPlayerClasses() {
		return Collections.unmodifiableSet(allClasses);
	}
	
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
	
	public IPlayerClass getPrimaryClass() {
		return getPlayerClass(currentPrimaryClass);
	}
	
	public IPlayerClass getSecondaryClass() {
		return getPlayerClass(currentSecondaryClass);
	}
	
	public LivingEntity getTarget() {
		return targetRef.get();
	}
	
	public boolean hasAbility(String name) {
		return getAbility(name) != null;
	}
	
	public boolean hasPlayerClass(String name) {
		for (final IPlayerClass c : allClasses)
			if (c.getName().equalsIgnoreCase(name))
				return true;
		
		return false;
	}
	
	public void log(Level level, String msg) {
		logger.log(level, msg);
	}
	
	@Override
	public Map<String, Object> serialize() {
		final Map<String, Object> data = super.serialize();
		
		final Set<String> d = new HashSet<String>();
		
		if (getAbilityCyclers() != null) {
			for (final AbilityCycler cycler : getAbilityCyclers()) {
				d.add(cycler.getPersistentID().toString());
			}
			
			data.put(SERIALIZE_ABLILTY_CYCLERS, d);
		}
		
		if (allClasses.size() > 0) {
			data.put(SERIALIZE_ALL_CLASSES, PlayerClassUtil.getModifiedClasses(this));
		} else
			// return early if no classes are set or changed
			return data;
		
		if (getPrimaryClass() != null) {
			data.put(SERIALIZE_CURRENT_PRIMARY_CLASS, getPrimaryClass().getName());
		}
		
		if (getSecondaryClass() != null) {
			data.put(SERIALIZE_CURRENT_SECONDARY_CLASS, getSecondaryClass()
					.getName());
		}
		
		return data;
	}
	
	public void setClass(@Nullable PlayerClass clazz) {
		if (clazz.isPrimaryClass()) {
			setPrimaryClass(clazz);
		} else {
			setSecondaryClass(clazz);
		}
	}
	
	public void setPrimaryClass(IPlayerClass newPrimary) {
		if (newPrimary != null) {
			currentPrimaryClass = newPrimary.getName();
		}
	}
	
	public void setSecondaryClass(IPlayerClass newSecondary) {
		if (newSecondary != null) {
			currentSecondaryClass = newSecondary.getName();
		}
	}
	
	public void setTarget(LivingEntity target) {
		targetRef = new WeakReference<LivingEntity>(target);
	}
	
	private void setupClasses(Map<String, Object> data) {
		Object getter;
		
		if ((getter = data.get(SERIALIZE_ALL_CLASSES)) != null
				&& getter instanceof Collection) {
			allClasses = new HashSet<IPlayerClass>((Collection<IPlayerClass>) data);
		} else {
			allClasses = new HashSet<IPlayerClass>();
		}
		
		if (data.containsKey(SERIALIZE_CURRENT_PRIMARY_CLASS)
				&& data.get(SERIALIZE_CURRENT_PRIMARY_CLASS) != null) {
			setPrimaryClass(getPlayerClass(data.get(SERIALIZE_CURRENT_PRIMARY_CLASS)
					.toString()));
		}
		if (data.containsKey(SERIALIZE_CURRENT_SECONDARY_CLASS)
				&& data.get(SERIALIZE_CURRENT_SECONDARY_CLASS) != null) {
			setSecondaryClass(getPlayerClass(data.get(
					SERIALIZE_CURRENT_SECONDARY_CLASS)
					.toString()));
		}
	}
}
