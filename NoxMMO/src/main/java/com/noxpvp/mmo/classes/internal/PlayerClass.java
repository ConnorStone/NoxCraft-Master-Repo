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

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.classes.DynamicClassTier;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.util.PlayerClassUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionDefault;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;

/**
 * When Implementing you must use all of the following constructors and params.
 * <br/>
 * Bold signifies that internal mechanisms depend heavily on and <b>MUST</b> be implemented.
 * <ol>
 * <li><b>(String playerUUID)</b></li>
 * </ol>
 * <br/><br/>
 * You must implement the following:<br/>
 * <ul>
 * <li> public static final String variable of "uniqueID";</li>
 * <li> public static final String variable of "className";</li>
 * </ul>
 *
 * <b>YOU MUST ADD @DelegateDeserialization annotations to all implementing classes of this type.</b>
 *
 */
@SerializableAs("PlayerClass")
public abstract class PlayerClass implements IPlayerClass, MenuItemRepresentable {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final String LOG_MODULE_NAME = "PlayerClass";
	private static final String DYNAMIC_TIER_PATH = "dynamic.tiers";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Static Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	//Debug and errors
	protected static ModuleLogger pcLog;

	@SuppressWarnings("rawtypes")
	private static List<Class> registeredClasses;
	private double lastHealth;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Static Initializer
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//THIS MUST BE STARTED ON ENABLE!
	@SuppressWarnings("rawtypes")
	public static void init() {
		pcLog = NoxMMO.getInstance().getModuleLogger(LOG_MODULE_NAME);
		registeredClasses = new ArrayList<Class>();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//Identification
	private final String uid;
	protected ModuleLogger log;
	
	//Tiers
	protected Map<Integer, IClassTier> tiers;
	private int cTierLevel = 1;
	private String name;
	private ItemStack identifyingItem;
	
	//Player Data
	private UUID playerUUID;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public PlayerClass(@Nonnull String uid,
	                   @Nonnull String name,
	                   @Nonnull UUID player) {
		Validate.notNull(name, "The name of class must not be null!");
		Validate.notNull(uid, "The UID of class must not be null!");
		Validate.notNull(player, "Player should not be null!");

		this.uid = uid;
		this.name = name;

		this.playerUUID = player;

		log = pcLog.getModule(String.valueOf(this.playerUUID));

		this.tiers = craftClassTiers();
		this.tiers.putAll(craftDynamicTiers());

		checkAndRegisterClass(this);

		checkTierCount();
		if (MMOPlayerManager.getInstance().isLoaded(getPlayerUUID())) {
			//Place anything requiring the MMOPlayer object to be available here.
			setCurrentTier(getCurrentTierLevel());
		} else {
			log.fine("Player class loaded before MMOPlayer");
		}

		NoxMMO mmo = NoxMMO.getInstance();
		for (IClassTier tier : tiers.values()) {
			mmo.addPermission(new NoxPermission(NoxMMO.getInstance(),
					StringUtil.join(".", NoxMMO.PERM_NODE, "class", tier.getRetainingClass().getName(), "tier", Integer.toString(tier.getTierLevel())),
					"Allows the usage of the " + tier.getName() + " tier in the " + tier.getRetainingClass().getName() + " class",
					PermissionDefault.OP));
		}

	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructor Internal Registration
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static void checkAndRegisterClass(PlayerClass playerClass) {
		if (registeredClasses.contains(playerClass.getClass()))
			return;

		PlayerClassUtil.registerPlayerClass(playerClass.getClass());
		registeredClasses.add(playerClass.getClass());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Identity
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public final String getUniqueID() {
		return uid;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: EXP
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void setExp(int tier, int amount) {
		getTier(tier).setExp(amount);
	}

	public final void removeExp(int amount) {
		removeExp(getCurrentTierLevel(), amount);
	}

	public void removeExp(int tier, int amount) {
		getTier(tier).removeExp(amount);
	}

	public final void addExp(int amount) {
		getTier().addExp(amount);
	}

	public void addExp(int tier, int amount) {
		getTier(tier).addExp(amount);
	}

	public final int getExp() {
		return getExp(getCurrentTierLevel());
	}

	public final void setExp(int amount) {
		setExp(getCurrentTierLevel(), amount);
	}

	public int getExp(int tier) {
		return getTier(tier).getExp();
	}

	public int getExpToLevel() {
		return Math.max(0, getMaxExp() - getExp());
	}

	public final int getMaxExp() {
		return getMaxExp(getCurrentTierLevel());
	}

	public int getMaxExp(int tier) {
		return getTier(tier).getMaxExp();
	}

	/**
	 * {@inheritDoc} <br/><br/>
	 * <b>Warning. This is calculated every run!</b>
	 *
	 * @see com.noxpvp.mmo.classes.internal.IPlayerClass#getTotalExp()
	 */
	public int getTotalExp() {
		int value = 0;
		for (Entry<Integer, IClassTier> tier : getTiers())
			if (canUseTier(tier.getKey()))
				value += tier.getValue().getExp();
		return value;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Leveling
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public final int getLevel() {
		return getLevel(getCurrentTierLevel());
	}

	public int getLevel(int tier) {
		return getTier(tier).getLevel();
	}

	public final int getMaxLevel() {
		return getMaxLevel(getCurrentTierLevel());
	}

	public int getMaxLevel(int tier) {
		return getTier().getMaxLevel();
	}

	public int getTotalLevel() {
		int ret = 0;
		for (IClassTier tier : tiers.values())
			ret += tier.getLevel();
		return ret;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Restrictions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public String getPermission() {
		return StringUtil.join(".", "nox", "class", getName());
	}

	public boolean canUseClass() {
		if (getPlayer() != null)
			return getPlayer().hasPermission(getPermission());
		return false;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Internal Tier Checking
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void checkTierCount() {
		if (tiers.size() != getHighestPossibleTier()) {
			pcLog.warning("Possible missing tiers for classes found.");
			log.severe("Tier count mismatch. Current tier count is " + tiers.size() + " when supposed to be " + getHighestPossibleTier());

			if (tiers.size() < getHighestPossibleTier()) {
				int h = tiers.size() - 1;
				int i = h + 1;
				while (i < getHighestPossibleTier()) {
					ClassTier dumb = new DummyClassTier(this, i);
					tiers.put(i, dumb);
					i++;
				}
			}


			//TODO: Message about missing tiers... URRRRGGGHHHh

			//IF DUMMIES USED
//			log.severe("Dummy tiers were used for missing tier objects") //FIXME: Better debug message.
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Tiers
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private Map<Integer, IClassTier> craftDynamicTiers(ConfigurationNode node) {
		Map<Integer, IClassTier> nTiers = new HashMap<Integer, IClassTier>();

		if (node != null) {
			node = node.getNode(DYNAMIC_TIER_PATH);
			for (ConfigurationNode t : node.getNodes()) {
				int tl = ParseUtil.parseInt(t.getName(), -1);
				if (tl < 0) {
					pcLog.warning("Tier level must be greater than 0. One of the dynamic tiers is invalid.");
					continue;
				}

				if (!t.contains("name")) {
					pcLog.severe("One of the nodes for dynamic tiers in a player class is missing a name value.");
					continue;
				}

				int maxLvl = t.get("max-level", -1);
				DynamicClassTier ti = new DynamicClassTier(this, t.get("name", String.class), tl, maxLvl);
				ti.loadTierConfig(t);
				nTiers.put(tl, ti);
			}

		}

		return nTiers;
	}

	protected abstract Map<Integer, IClassTier> craftClassTiers();

	protected final Map<Integer, IClassTier> craftDynamicTiers() {
		return craftDynamicTiers(getClassConfig());
	}

	public final int getCurrentTierLevel() {
		return cTierLevel;
	}

	public int getHighestAllowedTier() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLatestTier() {
		// TODO Auto-generated method stub
		return 0;
	}

	public final IClassTier getTier() {
		return getTier(getCurrentTierLevel());
	}

	public final IClassTier getTier(int level) {
		if (hasTier(level))
			return tiers.get(level);
		return null;
	}

	public final boolean hasTier(int level) {
		return tiers.containsKey(level);
	}

	public void setCurrentTier(IClassTier tier) {
		setCurrentTier(tier.getLevel());
	}

	public void setCurrentTier(int tierLevel) {
		cTierLevel = tierLevel;
	}

	public void resetHealth() {
		if (!isOnline()) return;
		Player p = getPlayer();

		p.setMaxHealth(this.lastHealth);
	}

	public void setHealth() {
		if (!isOnline()) return;
		Player p = getPlayer();

		this.lastHealth = p.getMaxHealth();
		p.setMaxHealth(getTier().getMaxHealth());
	}

	protected final Map<Integer, IClassTier> _getTierMap() {
		return tiers;
	}

	public final Set<Entry<Integer, IClassTier>> getTiers() {
		return Collections.unmodifiableSet(tiers.entrySet());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Abilities
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


	public Ability getAbility(String identity) {
		return getTier().getAbility(identity);
	}

	public boolean hasAbility(String identity) {
		return getTier().hasAbility(identity);
	}

	public Collection<Ability> getAbilities() {
		return getAbilitiesMap().values();
	}

	public Map<String, Ability> getAbilitiesMap() {
		return getTier().getAbilitiesMap();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Class Config
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected abstract FileConfiguration getClassConfig();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Descriptors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public final String getName() {
		return name;
	}

	public String getDisplayName() {
		return getColor() + getName();
	}

	public String getDescription(ChatColor color) {
		return color + getDescription();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Lore
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public List<String> getLore() {
		return getLore(30);
	}

	public List<String> getLore(int lineLength) {
		return MessageUtil.convertStringForLore(getDescription(), lineLength);
	}

	public List<String> getLore(ChatColor color, int lineLength) {
		List<String> ret = new ArrayList<String>();
		for (String lore : getLore(lineLength))
			ret.add(color + lore);

		return ret;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: MenuItemRepresentable
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public ItemStack getIdentifiableItem() {
		if (identifyingItem == null) {
			identifyingItem = new ItemStack(Material.BOOK_AND_QUILL);

			ItemMeta meta = identifyingItem.getItemMeta();
			meta.setDisplayName(new MessageBuilder().gold(ChatColor.BOLD + "Class: ")
					.append(getColor() + getName()).toString());

			List<String> lore = getLore(ChatColor.GOLD, 28);

			lore.add(new MessageBuilder().gold("Current Tier: ").yellow(getCurrentTierLevel()).toString());
			
			//TODO EXP
//			lore.add(new MessageBuilder().gold("Current Level: ").yellow(getLevel()).white("/").yellow(getMaxLevel()).toString());
//			lore.add(new MessageBuilder().gold("Exp: ").yellow(getExp()).white("/").yellow(getMaxExp()).toString());
//			lore.add(new MessageBuilder().gold("Exp To Level: ").yellow(getExpToLevel()).toString());

			meta.setLore(lore);

			identifyingItem.setItemMeta(meta);
		}

		return identifyingItem.clone();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Player Accessors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public final OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPlayerUUID());
	}

	public final Player getPlayer() {
		if (isOnline()) return getOfflinePlayer().getPlayer();
		return null;
	}

	public boolean isOnline() {
		return getMMOPlayer().isOnline();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Player Identity
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public MMOPlayer getMMOPlayer() { return MMOPlayerManager.getInstance().getPlayer(getPlayerUUID()); }

	public final UUID getPlayerUUID() {
		return playerUUID;
	}

	public final String getPlayerName() {
		if (isOnline()) return getPlayer().getName();
		return getOfflinePlayer().getName();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Map<String, Object> serialize() {
		Map<String, Object> ret = new HashMap<String, Object>();

		//Class retention...
		ret.put("class.uuid", getUniqueID());
		ret.put("class.name", getName());

		ret.put("player-ident", getPlayerUUID().toString());

		ret.put("current.tier", getCurrentTierLevel());
		ret.put("tiers", mapSerializedTiers());

		return ret;
	}

	private Map<Integer, Map<String, Object>> mapSerializedTiers() {
		Map<Integer, Map<String, Object>> root = new HashMap<Integer, Map<String, Object>>();

		for (int i = 1; i < getHighestPossibleTier(); i++) {
			Map<String, Object> tier = new HashMap<String, Object>();
			getTier(i).onSave(tier);

			root.put(i, tier);
		}

		return root;
	}

	public static PlayerClass valueOf(Map<String, Object> data) {
		Validate.isTrue(data.containsKey("class.uuid"), "Data does not contain a unique class id! Cannot possibly cast the class to the appropriate handler.");

		return PlayerClassUtil.safeConstructClass(data);
	}

	/**
	 * Strictly used for deserializing data.
	 * @param data
	 */
	public final void onLoad(Map<String, Object> data) {
		if (data.containsKey("current.tier")) setCurrentTier(Conversion.toInt.convertZero(data.get("current.tier")));
		else setCurrentTier(0);

		try {
			Map<Integer, Map<String, Object>> tierData = (Map<Integer, Map<String, Object>>) data.get("tiers");
			for (int k : tierData.keySet())
				if (hasTier(k)) getTier(k).onLoad(tierData.get(k));
		} catch (Throwable t) {

		}

		load(data);
	}

	protected abstract void load(Map<String, Object> data);
}
