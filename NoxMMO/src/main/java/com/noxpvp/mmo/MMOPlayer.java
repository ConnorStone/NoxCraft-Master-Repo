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
import com.noxpvp.core.commands.SafeNullPointerException;
import com.noxpvp.core.data.player.BasePluginPlayer;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.mmo.abilities.AbilityContainer;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.classes.PlayerClassContainer;
import com.noxpvp.mmo.classes.internal.DummyClass;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.util.PlayerClassUtil;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Level;

import static com.noxpvp.mmo.util.PlayerClassUtil.getAllChangedPlayerClasses;

@SerializableAs("MMOPlayer")
public class MMOPlayer extends BasePluginPlayer<NoxMMO> implements MenuItemRepresentable, PlayerClassContainer, AbilityContainer<Ability> {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constant Keys
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final String CLASSES_KEY = "classes";
	public static final String CYCLERS_KEY = "ability-cyclers";
	public static final String CURRENT_PRIMARY_CLASS_KEY = "current-primary-class";
	public static final String CURRENT_SECONDARY_CLASS_KEY = "current-secondary-class";

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

	private List<PlayerClass> all_classes = new ArrayList<PlayerClass>();
	private Map<String, PlayerClass> classCache = new HashMap<String, PlayerClass>();
	private IPlayerClass currentPrimaryClass = DummyClass.PRIMARY, currentSecondaryClass = DummyClass.SECONDARY;
	private List<AbilityCycler> cyclers = new ArrayList<AbilityCycler>();

	private ItemStack identifyingItem;
	private LivingEntity target;
	private boolean classesInitialized = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public MMOPlayer(UUID playerUUID) {
		super(playerUUID);
	}

	public MMOPlayer(Player player) {
		super(player);
	}

	public MMOPlayer(final Map<String, Object> data) {
		super(data);

//		MMOPlayerManager.getInstance().loadObject(this); //I know this is bad practice.. Temp fix for stuff.

		setupClasses(data);
		setupCyclers(data);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Internal Constructor Shortcuts
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void setupClasses(Map<String, Object> data) {
		try {
			if (data.containsKey(CLASSES_KEY)) {
				if (data.get(CLASSES_KEY) == null) throw new SafeNullPointerException();
				Collection<?> r = (Collection<?>) data.get(CLASSES_KEY);
				int failedCasts = 0;
				for (Object o : r) {
					try {
						getAllClasses().add((PlayerClass) o);
					} catch (ClassCastException e) {
						failedCasts++;
					}
				}

				if (failedCasts > 0)
					log(Level.WARNING, "During class data importing " + failedCasts + " failed to be casted to PlayerClass objects." +
							System.lineSeparator() + "Player: " + getPlayerUUID().toString());
			}
		} catch (Throwable t) {
			boolean throwTrace = false;
			if (t instanceof SafeNullPointerException) {
				log(Level.WARNING, "Class data did not exist properly. It is possible it was never created.");
				throwTrace = false;
			} else if (t instanceof NullPointerException) {
				log(Level.SEVERE, "There was a NPE within MMOPlayer during loading of class data." +
						System.lineSeparator() + " What follows is a stacktrace of the issue.");
				throwTrace = true;
			} else if (t instanceof UnsupportedOperationException) {
				log(Level.SEVERE, "Class data in MMOPlayer is immutable!!! Error occured within constructors.");
				throwTrace = true;
			}

			if (throwTrace) t.printStackTrace();
		}

		if (data.containsKey(CURRENT_PRIMARY_CLASS_KEY) && data.get(CURRENT_PRIMARY_CLASS_KEY) != null) setPrimaryClass(getPlayerClass(data.get(CURRENT_PRIMARY_CLASS_KEY).toString()));
		if (data.containsKey(CURRENT_SECONDARY_CLASS_KEY) && data.get(CURRENT_SECONDARY_CLASS_KEY) != null) setSecondaryClass(getPlayerClass(data.get(CURRENT_SECONDARY_CLASS_KEY).toString()));;

	}

	private void updateClassCache() {
		for (PlayerClass c : all_classes)
			if (c != null)
				classCache.put(c.getUniqueID(), c);
	}

	private void setupCyclers(Map<String, Object> data) {
		try {
			if (data.containsKey(CYCLERS_KEY)) {
				if (data.get(CYCLERS_KEY) == null) throw new SafeNullPointerException();
				Collection<?> r = (Collection<?>) data.get(CYCLERS_KEY);
				int failedCasts = 0;
				for (Object o : r) {
					try {
						cyclers.add((AbilityCycler) o);
					} catch (ClassCastException e) {
						failedCasts++;
					}
				}

				if (failedCasts > 0)
					log(Level.WARNING, "During class data importing " + failedCasts + " failed to be casted to AbilityCycler objects." +
							System.lineSeparator() + "Player: " + getPlayerUUID().toString());
			}
		} catch (Throwable t) {
			boolean throwTrace = true;
			if (t instanceof SafeNullPointerException) {
				log(Level.WARNING, "AbilityCycler data did not exist properly. It is possible it was never created.");
				throwTrace = false;
			} else if (t instanceof NullPointerException) {
				log(Level.SEVERE, "There was a NPE within MMOPlayer during loading of AbilityCycler data." +
						System.lineSeparator() + " What follows is a stacktrace of the issue.");
				throwTrace = true;
			} else if (t instanceof UnsupportedOperationException) {
				log(Level.SEVERE, "Cyclers data in MMOPlayer is immutable!!! Error occured within constructors.");
				throwTrace = true;
			}

			if (throwTrace) t.printStackTrace();
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Internal
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected List<PlayerClass> getAllClasses() {
		if ((all_classes == null || all_classes.isEmpty() || classesInitialized) && MMOPlayerManager.getInstance().isLoaded(this)) {
			all_classes = PlayerClassUtil.getAllPlayerClasses(getPlayerUUID());
			updateClassCache();
			classesInitialized = true;
		}
		return all_classes;
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
		return currentPrimaryClass;
	}

	public IPlayerClass getSecondaryClass() {
		return currentSecondaryClass;
	}

	public void setClass(@Nullable PlayerClass clazz) {
		if (clazz.isPrimaryClass()) setPrimaryClass(clazz);
		else setSecondaryClass(clazz);
	}

	public void setSecondaryClass(PlayerClass playerClass) {
		if (playerClass != null) this.currentSecondaryClass = playerClass;
		else this.currentPrimaryClass = DummyClass.SECONDARY;
	}

	public void setPrimaryClass(PlayerClass playerClass) {
		if (playerClass != null) {
			this.currentPrimaryClass = playerClass;
			playerClass.setHealth(); //TODO: add more checks on resetting health as it can leak.
			//Leak meaning that if the health was modified in another plugin it will reset to that instead.
		}
		else {
			this.currentPrimaryClass = DummyClass.PRIMARY;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Targetting
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void setTarget(LivingEntity target) {
		this.target = target;
	}

	public LivingEntity getTarget() {
		return target;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: AbilityCyclers
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public List<AbilityCycler> getAbilityCyclers() {
		return cyclers;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: AbilitiesContainer
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Ability getAbility(String identity) {
		return getAbilitiesMap().get(identity);
	}

	public boolean hasAbility(String identity) {
		return getAbilitiesMap().containsKey(identity);
	}

	public Collection<Ability> getAbilities() {
		List<Ability> ret = new ArrayList<Ability>();

		ret.addAll(getPrimaryClass().getAbilities());
		ret.addAll(getSecondaryClass().getAbilities());

		return Collections.unmodifiableCollection(ret);
	}

	public Map<String, Ability> getAbilitiesMap() {
		Map<String, Ability> ret = new HashMap<String, Ability>();

		ret.putAll(getPrimaryClass().getAbilitiesMap());
		ret.putAll(getSecondaryClass().getAbilitiesMap());

		return Collections.unmodifiableMap(ret);
	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: PlayerClassContainer
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public boolean hasPlayerClass(String identifier) {
		return getClassMap().containsKey(identifier);
	}

	public PlayerClass getPlayerClass(String identifier) {
		return getClassMap().get(identifier);
	}

	public List<PlayerClass> getPlayerClasses() {
		return Collections.unmodifiableList(getAllClasses());
	}

	public boolean addPlayerClass(IPlayerClass clazz) {
		if (clazz instanceof PlayerClass) return addPlayerClass((PlayerClass) clazz);
		return false;
	}

	public boolean addPlayerClass(PlayerClass clazz) {
		classCache.put(clazz.getUniqueID(), clazz);
		if (getAllClasses().contains(clazz)) getAllClasses().remove(clazz);
		return getAllClasses().add(clazz);
	}

	public boolean removePlayerClass(String identifier) {
		return false;
	}

	public boolean removePlayerClass(IPlayerClass clazz) {
		return false;
	}

	public Map<String, PlayerClass> getClassMap() {
		return classCache;
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

		data.put(CLASSES_KEY, getAllChangedPlayerClasses(getPlayerUUID()));

		if (!(getPrimaryClass() instanceof DummyClass)) data.put(CURRENT_PRIMARY_CLASS_KEY, getPrimaryClass().getUniqueID());
		if (!(getSecondaryClass() instanceof DummyClass)) data.put(CURRENT_SECONDARY_CLASS_KEY, getSecondaryClass().getUniqueID());

		return data;
	}

	public String getPersistenceNode() {
		return "MMOPlayer";
	}
}
