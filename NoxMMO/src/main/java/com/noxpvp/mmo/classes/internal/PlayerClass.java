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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.SafeNullPointerException;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.AbilityContainer;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;

@SerializableAs("PlayerClass")
public abstract class PlayerClass extends Experientable implements IPlayerClass,
		MenuItemRepresentable, ConfigurationSerializable,
		AbilityContainer<PlayerAbility> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// Debug and errors
	protected static final String						LOG_MODULE_NAME			= "PlayerClass";
	protected static ModuleLogger						pcLog;
	
	// Serializers start
	private static final String							SERIALIZE_NAME			= "name";
	private static final String							SERIALIZE_PLAYER_UUID	= "player-id";
	// Serializers end
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final String								name;
	private UUID										playerUUID;
	
	private final Set<PlayerAbility>					abilitySet;
	
	// Ability id - <required level - tier level>
	private final Map<String, Map<Integer, Integer>>	levelsToTier;
	
	private ItemStack									identifyingItem;
	private double										maxHealth;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public PlayerClass(Map<String, Object> data) {
		super(data);
		
		Object getter;
		
		if ((getter = data.get(SERIALIZE_NAME)) != null && getter instanceof String) {
			name = (String) getter;
		} else {
			name = "";
		}
		
		if ((getter = data.get(SERIALIZE_PLAYER_UUID)) != null
				&& getter instanceof String) {
			playerUUID = UUID.fromString((String) getter);
		} else
			throw new SafeNullPointerException(
					"Could not get player uuid while loading");
		
		abilitySet = new HashSet<PlayerAbility>();
		levelsToTier = new HashMap<String, Map<Integer, Integer>>();
		
	}
	
	public PlayerClass(String name, UUID playerUUID, ExperienceFormula formula) {
		super(formula);
		
		Validate.notNull(name, "The name of class must not be null!");
		Validate.notNull(playerUUID, "Player should not be null!");
		
		this.name = name;
		this.playerUUID = playerUUID;
		
		abilitySet = new HashSet<PlayerAbility>();
		levelsToTier = new HashMap<String, Map<Integer, Integer>>();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void addAbility(PlayerAbility ab, Map<Integer, Integer> levelsToTierMap) {
		abilitySet.add(ab);
		levelsToTier.put(ab.getUniqueId(), levelsToTierMap);
	}
	
	public boolean canUseClass() {
		if (getPlayer() != null)
			return getPlayer().hasPermission(getPermission());
		
		return false;
	}
	
	public Set<PlayerAbility> getAbilities() {
		return Collections.unmodifiableSet(abilitySet);
	};
	
	public PlayerAbility getAbility(String name) {
		for (final PlayerAbility a : abilitySet)
			if (a.getName().equalsIgnoreCase(name))
				return a;
		
		return null;
	}
	
	public PlayerAbility getAbility(UUID identity) {
		for (final PlayerAbility a : abilitySet)
			if (a.getUniqueId().equals(identity))
				return a;
		
		return null;
	}
	
	public Set<PlayerAbility> getAbilitySet() {
		return Collections.unmodifiableSet(abilitySet);
	}
	
	public String getDisplayName() {
		return getColor() + getName();
	}
	
	public ItemStack getIdentifiableItem() {
		if (identifyingItem == null) {
			identifyingItem = new ItemStack(Material.BOOK_AND_QUILL);
			
			final ItemMeta meta = identifyingItem.getItemMeta();
			meta.setDisplayName(new MessageBuilder()
					.gold(ChatColor.BOLD + "Class: ")
					.append(getColor() + getName()).toString());
			
			final List<String> lore = getLore(30);
			
			lore.add(new
					MessageBuilder().gold("Current Level: ").yellow(getLevel())
							.white("/").yellow(getMaxLevel()).toString());
			lore.add(new
					MessageBuilder().gold("Exp: ").yellow(getExp()).white("/")
							.yellow(getMaxExp()).toString());
			lore.add(new
					MessageBuilder().gold("Exp To Level: ").yellow(getExpToLevel())
							.toString());
			
			meta.setLore(lore);
			
			identifyingItem.setItemMeta(meta);
		}
		
		return identifyingItem.clone();
	}
	
	public List<String> getLore() {
		return MessageUtil.convertStringForLore(getDescription(), 30);
	}
	
	public List<String> getLore(int lineLength) {
		return MessageUtil.convertStringForLore(getDescription(), lineLength);
	}
	
	public MMOPlayer getMMOPlayer() {
		return MMOPlayerManager.getInstance().getPlayer(getPlayerUUID());
	}
	
	public final String getName() {
		return name;
	}
	
	public final OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPlayerUUID());
	}
	
	public String getPermission() {
		return StringUtil.join(".", "nox", "class", getName());
	}
	
	public final Player getPlayer() {
		if (isOnline())
			return getOfflinePlayer().getPlayer();
		return null;
	}
	
	public final String getPlayerName() {
		if (isOnline())
			return getPlayer().getName();
		return getOfflinePlayer().getName();
	}
	
	public final UUID getPlayerUUID() {
		return playerUUID;
	}
	
	public boolean hasAbility(String name) {
		return getAbility(name) != null;
	}
	
	public boolean isOnline() {
		return getMMOPlayer().isOnline();
	}
	
	public void resetHealth() {
		if (!isOnline())
			return;
		final Player p = getPlayer();
		
		p.setMaxHealth(maxHealth);
	}
	
	@Override
	public Map<String, Object> serialize() {
		final Map<String, Object> ret = super.serialize();
		
		ret.put(SERIALIZE_NAME, getName());
		ret.put(SERIALIZE_PLAYER_UUID, getPlayerUUID().toString());
		
		return ret;
	}
	
	public void setPlayer(OfflinePlayer player) {
		Validate.notNull(player);
		
		playerUUID = player.getUniqueId();
	}
	
}
