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

package com.noxpvp.core.data;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import org.apache.commons.lang.Validate;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistent;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.annotation.Temporary;
import com.noxpvp.core.data.player.CorePlayerStats;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.PlayerUtils;

@SerializableAs("NoxPlayer")
public class NoxPlayer implements PluginPlayer<NoxCore>, Persistent {
	
	// ~~~~~~~~~~~
	// Logging
	// ~~~~~~~~~~~
	
	private static ModuleLogger					log;
	
	static {
		log = CorePlayerManager.getInstance().getModuleLogger("NoxPlayer");
	}

	public static final String persistanceNode = "NoxPlayer";

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private final UUID playerUUID;

	private final WeakHashMap<String, CoolDown> cd_cache = new WeakHashMap<String, CoolDown>();
	private       List<CoolDown>     cds;
	private final ConfigurationNode  temp_data;
	private       CoreBar            coreBar;
	private       CoreBoard          coreBoard;
	private       CorePlayerStats    stats;
	private       Reference<CoreBox> coreBox;
	private       String             lastFormattedName;

	public NoxPlayer(Map<String, Object> data) {
		Validate.isTrue(data.containsKey("uuid"),
				"Not a valid data structure. Missing uuid entry!");

		playerUUID = UUID.fromString(data.get("uuid").toString());

		// Setup CoolDowns
		try {
			cds = (List<CoolDown>) data.get("cool-downs");
		} catch (final Exception e) {
			if (e instanceof ClassCastException) {
				log(Level.SEVERE, "Data has been corrupted for player \""
						+ getPlayerUUID()
						+ "\". Failed to deserialize a list of cooldowns.");
				log(Level.WARNING, "Cooldowns has been erased as a result.");
			} else if (e instanceof NullPointerException) {
				log(Level.FINE, "There is no cooldowns for the player \""
						+ getPlayerUUID()
						+ "\". Not entirely sure if data corruption is the cause.");
			}

			cds = new ArrayList<CoolDown>();
		}

		stats = data.containsKey("stats") ?
				data.get("stats") instanceof CorePlayerStats ? (CorePlayerStats) data
						.get("stats")
						: new CorePlayerStats(playerUUID)
				:
				new CorePlayerStats(playerUUID);
		if (data.containsKey("stats") && stats != data.get("stats")) {
			log(Level.SEVERE,
					"Data for player stats may have been wiped. Not the same instance as the data map.");
		}

		temp_data = new ConfigurationNode();

		updateCoolDownCache();
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public NoxPlayer(Player player) {
		this(player.getUniqueId());
	}

	public NoxPlayer(UUID playerUUID) {
		this.playerUUID = playerUUID;

		// Cool-Downs
		cds = new ArrayList<CoolDown>();

		// Temp Data
		temp_data = new ConfigurationNode();

		stats = new CorePlayerStats(getPlayerUUID());
		// Player stats
		// this.stats = new PlayerStats(getPersistentID());
	}

	public static ModuleLogger getModuleLogger(String... module) {
		return log.getModule(module);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Internal Methods.
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds a new cooldown with the specified name and length.
	 *
	 * <p>
	 * To display a render of the CoolDown. Use
	 * {@link #addCoolDown(String, com.noxpvp.core.gui.CoolDown.Time, boolean)}
	 * with a true param on the final param.
	 * </p>
	 * 
	 * @param name
	 * @param length
	 * @return
	 */
	public CoolDown addCoolDown(String name, CoolDown.Time length) {
		if (cd_cache.containsKey(name)) {
			CoolDown cd;
			if ((cd = cd_cache.get(name)) != null && cd.expired()) {
				cd_cache.remove(name);
				cds.remove(cd);
			} else
				return null;
		}
		final CoolDown cd = new CoolDown(name, length);
		
		cds.add(cd);
		cd_cache.put(cd.getName(), cd);
		
		return cd;
	}
	
	/**
	 * Adds a new cooldown with the specified name and length.
	 * <p>
	 * Adding true to coreTimer param will make it display a timer on user
	 * screen through the {@link com.noxpvp.core.gui.CoreBoard} api.
	 * </p>
	 * 
	 * @param name
	 *            cooldown name
	 * @param length
	 *            length of cooldown.
	 * @param coreTimer
	 *            display as a timer on user screen?
	 * @return true if successfully made and false otherwise.
	 */
	public boolean addCoolDown(String name, CoolDown.Time length, boolean coreTimer) {
		final CoolDown cd = addCoolDown(name, length);
		if (cd != null && coreTimer) {
			ChatColor cdNameColor, cdCDColor;
			try {
				cdNameColor = ChatColor.valueOf(getPlugin().getCoreConfig().get(
						"gui.coreboard.cooldowns.name-color",
						String.class,
						"&e"));
				cdCDColor = ChatColor.valueOf(getPlugin().getCoreConfig().get(
						"gui.coreboard.cooldowns.time-color",
						String.class,
						"&a"));
				
			} catch (final IllegalArgumentException e) {
				cdNameColor = ChatColor.YELLOW;
				cdCDColor = ChatColor.GREEN;
			}
			
			getCoreBoard().addTimer(
					name,
					name,
					length,
					cdNameColor,
					cdCDColor);
		}
		
		return cd != null;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods.
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void deleteCoreBox() {
		if (hasCoreBox() && isOnline()) {
			getPlayer().closeInventory();
		}
		
		coreBox = null;
	}
	
	public CoolDown getCoolDown(String name) {
		if (hasCoolDown(name))
			return cd_cache.get(name);
		else
			return null;
	}
	
	/**
	 * Retrieves an unmodifiable view of the CoolDown List.
	 * 
	 * @return CoolDowns
	 */
	public List<CoolDown> getCoolDowns() {
		return Collections.unmodifiableList(cds);
	}
	
	public CoreBar getCoreBar() {
		if (coreBar == null)
			return coreBar = new CoreBar(NoxCore.getInstance(), getPlayer());
		return coreBar;
	}
	
	public CoreBoard getCoreBoard() {
		return coreBoard;
	}
	
	public String getFullName() {
		final StringBuilder text = new StringBuilder();
		text.append(VaultAdapter.chat.getGroupPrefix(getStats().getLastWorldName(),
				getMainGroup())
				+ getPlayer().getName());
		
		final String v = getLastFormattedName();
		
		if (!isOnline())
			return v;
		
		final String v2 = VaultAdapter.GroupUtils
				.getFormattedPlayerName(getPlayer());
		
		lastFormattedName = v2;
		
		return v2;
	}
	
	public String getLastFormattedName() {
		return lastFormattedName;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced Methods: GUI
	// ~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * @deprecated Use {@link #getPlayerName()} instead. This is here to
	 *             help merge new system.
	 * 
	 * @see #getPlayerName()
	 * @return {@link #getPlayerName()} result.
	 */
	@Deprecated
	@Temporary
	public String getName() {
		return getPlayerName();
	}
	
	public NoxPlayer getNoxPlayer() {
		return this;
	}
	
	// -------- CoreBox --------
	
	public OfflinePlayer getOfflinePlayer() {
		return PlayerUtils.getOfflinePlayer(getPlayerUUID());
	}
	
	public String getPersistenceNode() {
		return persistanceNode;
	}
	
	public String getPersistentID() {
		return playerUUID.toString();
	}
	
	public Player getPlayer() {
		if (isOnline())
			return (Player) getOfflinePlayer();
		else
			return null;
	}
	
	// ----------------------------------------
	// Instanced Methods: CoolDowns System
	// ----------------------------------------
	
	/**
	 * {@inheritDoc}
	 * <hr/>
	 * <p>
	 * If the player is not online. It will return the last known username
	 * of the player.
	 * </p>
	 * 
	 * @return current player name or last known one. If known was ever
	 *         known it will return null.
	 */
	public String getPlayerName() {
		String name = null;
		if (isOnline()) {
			name = ((Player) getOfflinePlayer()).getName();
		}
		if (name == null) {
			name = getStats().getLastIGN();
		}
		
		return name;
	}
	
	public UUID getPlayerUUID() {
		return playerUUID;
	}
	
	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}
	
	/**
	 * 
	 * @deprecated Please use {@link #getCoolDown(String)} along with
	 *             {@link com.noxpvp.core.gui.CoolDown#getReadableTimeLeft()}
	 * @param name
	 *            cooldown name
	 * @return readable cooldown time.
	 */
	@Deprecated
	public String getReadableRemainingCDTime(String name) {
		final CoolDown cd = getCoolDown(name);
		if (cd != null)
			return cd.getReadableTimeLeft();
		return "";
	}
	
	public CorePlayerStats getStats() {
		if (stats == null) {
			stats = new CorePlayerStats(getPlayerUUID());
		}
		return stats;
	}
	
	/**
	 * Tells whether a cooldown exists or not.
	 * 
	 * @param name
	 *            cooldown name
	 * @return true if cooldown exists even if its expired. false
	 *         otherwise.
	 */
	public boolean hasCoolDown(String name) {
		return cd_cache.containsKey(name);
	}
	
	public boolean hasCoreBox() {
		return coreBox != null && coreBox.get() != null;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instanced: Helpers
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public boolean hasCoreBox(CoreBox box) {
		return coreBox != null && coreBox.get() == box;
	}
	
	@Deprecated
	/**
	 * @deprecated Please use {@link #isCooling(String)} instead.
	 */
	public boolean isCooldownActive(String name) {
		return isCooling(name);
	}
	
	/**
	 * Tells whether or not the cooldown with the specified name is
	 * currently still active.
	 * 
	 * @param name
	 *            cooldown name
	 * @return true if still active and false if expired or does not exist.
	 */
	public boolean isCooling(String name) {
		return !hasCoolDown(name) || !cd_cache.get(name).expired();
	}
	
	public boolean isOnline() {
		return PlayerUtils.isOnline(getPlayerUUID());
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Serialization
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void log(Level level, String msg) {
		log.log(level, msg);
	}
	
	public void removeCoolDown(String name) {
		if (!cd_cache.containsKey(name))
			return;
		
		final CoolDown cd = cd_cache.get(name);
		
		cds.remove(cd);
		cd_cache.remove(name);
	}
	
	public Map<String, Object> serialize() {
		final Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("uuid", getPersistentID().toString());
		data.put("cool-downs", cds);
		data.put("stats", stats);
		
		return data;
	}
	
	public void setCoreBox(CoreBox box) {
		if (hasCoreBox()) {
			deleteCoreBox();
		}
		
		coreBox = new WeakReference<CoreBox>(box);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Deprecated Migratory Code.
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private String getMainGroup() {
		final String[] groups = VaultAdapter.permission.getPlayerGroups(getPlayer());
		final LinkedList<String> groupList = new LinkedList<String>();// :
																		// put
																		// local
																		// group
																		// list
																		// here
		
		if (groups.length < 0)
			return null;
		
		int ind = 100;
		String finalGroup = null;
		
		for (final String group : groups) {
			if (groupList.indexOf(group) < ind) {
				ind = groupList.indexOf(group);
				finalGroup = group;
			}
		}
		
		return finalGroup;
	}
	
	private void updateCoolDownCache() {
		cd_cache.clear();
		
		final Iterator<CoolDown> iterator = cds.iterator();
		while (iterator.hasNext()) {
			final CoolDown cd = iterator.next();
			if (cd.expired()) {
				iterator.remove();
			} else {
				cd_cache.put(cd.getName(), cd);
			}
		}
	}
	
}
