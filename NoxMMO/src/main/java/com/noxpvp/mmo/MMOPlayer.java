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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
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
import com.noxpvp.mmo.classes.internal.ClassConfig;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.classes.internal.MMOClassData;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.manager.AbilityCyclerManager;
import com.noxpvp.mmo.manager.ClassConfigManager;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.util.PlayerClassUtil;

@SerializableAs("MMOPlayer")
public class MMOPlayer extends BasePluginPlayer<NoxMMO> implements
		MenuItemRepresentable, PlayerClassContainer,
		AbilityContainer<PlayerAbility>, AbilityCyclerContainer {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Init
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	static {
		logger = MMOPlayerManager.getInstance().getModuleLogger("MMOPlayer");
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static final String			SERIALIZE_ALL_CLASSES				= "class-data";
	public static final String			SERIALIZE_ABLILTY_CYCLERS			= "ability-cyclers";
	public static final String			SERIALIZE_CURRENT_PRIMARY_CLASS		= "current-primary-class-name";
	public static final String			SERIALIZE_CURRENT_SECONDARY_CLASS	= "current-secondary-class-name";
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
				
				AbilityCycler ac = null;
				if (id != null && (ac = acm.getCycler(id)) != null) {
					cyclers.add(ac);
					AbilityCycler.register(ac);
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
		
		addUnusedClasses();
		
		MMOPlayerManager.getInstance().loadObject(this);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void addAbilityCycler(AbilityCycler cycler) {
		if (cycler == null || !cycler.getMMOPlayer().equals(this))
			return;
		
		cyclers.add(cycler);
		
	}
	
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
	
	public AbilityCycler getCycler(ItemStack item) {
		if (item == null)
			return null;
		
		for (final AbilityCycler ac : cyclers)
			if (ac.isCycleItem(item))
				return ac;
		
		return null;
	}
	
	public AbilityCycler getCycler(UUID id) {
		if (id == null)
			return null;
		
		for (final AbilityCycler ac : cyclers)
			if (ac.getPersistentID().equals(id))
				return ac;
		
		return null;
	}
	
	public ItemStack getIdentifiableItem() {
		return identifyingItem;
	}
	
	public String getPersistenceNode() {
		return "MMOPlayer";
	}
	
	public IPlayerClass getPlayerClass(String fileName) {
		for (final IPlayerClass c : allClasses)
			if (c.getFileName().equalsIgnoreCase(fileName))
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
	
	public boolean hasAbilityCycler(ItemStack item) {
		return getCycler(item) != null;
	}
	
	public boolean hasAbilityCycler(UUID id) {
		return getCycler(id) != null;
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
			final Collection<IPlayerClass> modified = PlayerClassUtil
					.getModifiedClasses(this);
			
			if (!modified.isEmpty()) {
				
				final List<MMOClassData> saveData = new ArrayList<MMOClassData>();
				
				for (final IPlayerClass pc : modified) {
					saveData.add(pc.getClassData());
				}
				
				data.put(SERIALIZE_ALL_CLASSES, saveData);
				
			}
		}
		
		if (getPrimaryClass() != null) {
			data.put(SERIALIZE_CURRENT_PRIMARY_CLASS, getPrimaryClass()
					.getFileName());
		}
		
		if (getSecondaryClass() != null) {
			data.put(SERIALIZE_CURRENT_SECONDARY_CLASS, getSecondaryClass()
					.getFileName());
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
			Bukkit.broadcastMessage(newPrimary.getFileName());
			currentPrimaryClass = newPrimary.getFileName();
		}
	}
	
	public void setSecondaryClass(IPlayerClass newSecondary) {
		if (newSecondary != null) {
			currentSecondaryClass = newSecondary.getFileName();
		}
	}
	
	public void setTarget(LivingEntity target) {
		targetRef = new WeakReference<LivingEntity>(target);
	}
	
	private void addUnusedClasses() {
		for (final ClassConfig cfg : ClassConfigManager.getInstance()
				.getLoadedValues()) {
			
			if (!hasPlayerClass(cfg.getFileConfig().getName())) {
				addPlayerClass(new PlayerClass(cfg, new MMOClassData(
						getPlayerUUID(), cfg)));
			}
			
		}
	}
	
	private void setupClasses(Map<String, Object> data) {
		Object getter;
		
		List<MMOClassData> classDataList;
		if ((getter = data.get(SERIALIZE_ALL_CLASSES)) != null
				&& getter instanceof Collection) {
			classDataList = new ArrayList<MMOClassData>(
					(Collection<MMOClassData>) getter);
			
			final ClassConfigManager ccm = ClassConfigManager.getInstance();
			allClasses = new HashSet<IPlayerClass>();
			
			for (final MMOClassData cd : classDataList) {
				
				final ClassConfig cc = ccm.getClassConfig(cd.getClassConfigPath());
				if (cc == null) {
					continue;
				}
				
				addPlayerClass(new PlayerClass(cc, cd));
			}
			
		} else {
			allClasses = new HashSet<IPlayerClass>();
		}
		
		addUnusedClasses();
		
		if ((getter = data.get(SERIALIZE_CURRENT_PRIMARY_CLASS)) != null
				&& getter instanceof String) {
			final IPlayerClass clazz = getPlayerClass((String) getter);
			
			if (clazz == null)
				return;
			
			setPrimaryClass(clazz);
		}
		
		if ((getter = data.get(SERIALIZE_CURRENT_SECONDARY_CLASS)) != null
				&& getter instanceof String) {
			final IPlayerClass clazz = getPlayerClass((String) getter);
			
			if (clazz == null)
				return;
			
			setSecondaryClass(clazz);
		}
		
	}
}
