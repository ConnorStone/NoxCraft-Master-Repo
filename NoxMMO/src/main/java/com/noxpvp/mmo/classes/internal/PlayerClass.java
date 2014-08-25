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
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.AbilityContainer;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.abilities.player.WallopPlayerAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;

@SerializableAs("PlayerClass")
public class PlayerClass extends ExperienceHolder implements IPlayerClass,
		MenuItemRepresentable, AbilityContainer<PlayerAbility> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// Debug and errors
	protected static final String				LOG_MODULE_NAME	= "PlayerClass";
	protected static ModuleLogger				pcLog;
	
	// Serializers start
	// Serializers end
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final ClassConfig					config;
	private final MMOClassData					data;
	private final Map<String, PlayerAbility>	abilities;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public PlayerClass(ClassConfig config, MMOClassData data) {
		this.config = config;
		this.data = data;
		
		abilities = new HashMap<String, PlayerAbility>();
		
		final WallopPlayerAbility wpa = new WallopPlayerAbility(getOfflinePlayer());
		abilities.put(wpa.getName(), wpa);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public boolean canUseClass() {
		if (getPlayer() != null)
			return getPlayer().hasPermission(getPermission());
		
		return false;
	}
	
	public Set<PlayerAbility> getAbilities() {
		return Collections.unmodifiableSet(new HashSet<PlayerAbility>(abilities
				.values()));
	}
	
	public PlayerAbility getAbility(String identity) {
		return abilities.get(identity);
	}
	
	public Map<String, PlayerAbility> getAbilityMap() {
		return Collections.unmodifiableMap(abilities);
	}
	
	public Color getArmourColor() {
		return config.getArmourColor();
	}
	
	public MMOClassData getClassData() {
		return data;
	}
	
	public ChatColor getColor() {
		return ChatColor.WHITE;
	}
	
	public String getDescription() {
		return config.getClassDescription();
	}
	
	public String getFileName() {
		return config.getPersistentID();
	}
	
	public ExperienceFormula getFormula() {
		return config.getExpFormula();
	}
	
	public ItemStack getIdentifiableItem() {
		final ItemStack identifyingItem = new ItemStack(config.getItemBadge());
		
		final ItemMeta meta = identifyingItem.getItemMeta();
		meta.setDisplayName(new MessageBuilder()
				.gold(ChatColor.BOLD + "Class: ")
				.append(getColor() + getName()).toString());
		
		final List<String> lore = MessageUtil.splitToList(getDescription(), 30);
		
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
		
		return identifyingItem;
	}
	
	public double getMaxHealth() {
		return config.getMaxHealth();
	}
	
	public int getMaxLevel() {
		return config.getMaxLevel();
	}
	
	public MMOPlayer getMMOPlayer() {
		return MMOPlayerManager.getInstance().getPlayer(getPlayerUUID());
	}
	
	public final String getName() {
		return config.getClassName();
	}
	
	public final OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPlayerUUID());
	}
	
	public String getPermission() {
		return StringUtil.join(".", "nox", "class", getName());
	}
	
	public String getPersistenceNode() {
		return config.getPersistenceNode();
	}
	
	public String getPersistentID() {
		return config.getPersistentID();
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
		return data.getPlayerUUID();
	}
	
	public boolean hasAbility(String identity) {
		return abilities.containsKey(identity);
	}
	
	public boolean isOnline() {
		return getMMOPlayer().isOnline();
	}
	
	public boolean isPrimaryClass() {
		return config.isPrimaryClass();
	}
	
	public void log(Level level, String msg) {
		log(level, msg);
	}
	
	public void resetHealth() {
		if (!isOnline())
			return;
		
		final Player p = getPlayer();
		
		p.setMaxHealth(getMaxHealth());
	}
	
	private void updateAbilityLevels() {
		for (final PlayerAbility ab : getAbilities()) {
			final Map<Integer, Integer> map = levelsToTier.get(ab.getName());
			if (map == null) {
				continue;
			}
			
			boolean set = false;
			for (int lv = getLevel(); lv > 0; lv--) {
				if (!map.containsKey(lv)) {
					continue;
				}
				
				int tier = 0;
				if ((tier = map.get(lv)) > 0) {
					ab.setCurrentTier(tier);
					set = true;
					break;
				}
			}
			
			if (!set) {
				ab.setCurrentTier(1);
			}
			
		}
		
	}
	
}
