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
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.logging.Level;

public class NoxPlayer implements PluginPlayer, Persistent {

	//~~~~~~~~~~~
	//Logging
	//~~~~~~~~~~~

	private static ModuleLogger log;

	static {
		log = CorePlayerManager.getInstance().getModuleLogger("NoxPlayer");
	}

	public static ModuleLogger getModuleLogger(String... module) {
		return log.getModule(module);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private final UUID playerUUID;
	private WeakHashMap<String, CoolDown> cd_cache;
	private List<CoolDown> cds;
	private ConfigurationNode temp_data;
	private CoreBar coreBar;
	private CoreBoard coreBoard;
	private CorePlayerStats stats;
	private Reference<CoreBox> coreBox;
	private String lastFormattedName;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public NoxPlayer(Player player) {
		this(player.getUniqueId());
	}

	public NoxPlayer(UUID playerUUID) {
		this.playerUUID = playerUUID;

		//Cool-Downs
		cds = new ArrayList<CoolDown>();
		cd_cache = new WeakHashMap<String, CoolDown>();

		//Temp Data
		temp_data = new ConfigurationNode();

		this.stats = new CorePlayerStats(getPersistentID());
		//Player stats
//		this.stats = new PlayerStats(getPersistentID());
	}

	public NoxPlayer(Map<String, Object> data) {
		Validate.isTrue(data.containsKey("uuid"), "Not a valid data structure. Missing uuid entry!");

		this.playerUUID = UUID.fromString(data.get("uuid").toString());

		//Setup CoolDowns
		try { this.cds = (List<CoolDown>) data.get("cool-downs"); }
		catch (Exception e) {
			if (e instanceof ClassCastException) {
				log(Level.SEVERE, "Data has been corrupted for player \"" + getPlayerUUID() + "\". Failed to deserialize a list of cooldowns.");
				log(Level.WARNING, "Cooldowns has been erased as a result.");
			} else if (e instanceof NullPointerException) {
				log(Level.FINE, "There is no cooldowns for the player \"" + getPlayerUUID() + "\". Not entirely sure if data corruption is the cause.");
			}

			this.cds = new ArrayList<CoolDown>();
		}

		this.stats = (data.containsKey("stats") ? (data.get("stats") instanceof CorePlayerStats ? (CorePlayerStats) data.get("stats") : new CorePlayerStats(this.playerUUID)) : new CorePlayerStats(this.playerUUID));
		if (data.containsKey("stats") && this.stats != data.get("stats")) log(Level.SEVERE, "Data for player stats may have been wiped. Not the same instance as the data map.");

		this.temp_data = new ConfigurationNode();

		updateCoolDownCache();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Internal Methods.
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private String getMainGroup() {
		String[] groups = VaultAdapter.permission.getPlayerGroups(getPlayer());
		LinkedList<String> groupList = new LinkedList<String>();//: put local group list here

		if (groups.length < 0) return null;

		int ind = 100;
		String finalGroup = null;

		for (String group : groups) {
			if (groupList.indexOf(group) < ind) {
				ind = groupList.indexOf(group);
				finalGroup = group;
			}
		}

		return finalGroup;
	}

	private void updateCoolDownCache() {
		cd_cache.clear();

		Iterator<CoolDown> iterator = cds.iterator();
		while (iterator.hasNext()) {
			CoolDown cd = iterator.next();
			if (cd.expired())
				iterator.remove();
			else
				cd_cache.put(cd.getName(), cd);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods.
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void log(Level level, String msg) {
		log.log(level, msg);
	}

	public NoxCore getPlugin() { return NoxCore.getInstance(); }

	public UUID getPlayerUUID() { return getPersistentID(); }


	public CorePlayerStats getStats() {
		return stats;
	}

	public String getFullName() {
		StringBuilder text = new StringBuilder();
		text.append(VaultAdapter.chat.getGroupPrefix(getStats().getLastWorldName(), getMainGroup()) + getPlayer().getName());

		String v = getLastFormattedName();

		if (!isOnline())
			return v;

		String v2 = VaultAdapter.GroupUtils.getFormattedPlayerName(getPlayer());

		this.lastFormattedName = v2;

		return v2;
	}

	/**
	 * {@inheritDoc}
	 * <hr/>
	 * <p>If the player is not online. It will return the last known username of the player.</p>
	 *
	 * @return current player name or last known one. If known was ever known it will return null.
	 */
	public String getPlayerName() {
		String name = null;
		if (isOnline()) name = ((Player)getOfflinePlayer()).getName();
		if (name == null) name = getStats().getLastIGN();

		return name;
	}

	public boolean isOnline() { return PlayerUtils.isOnline(getPlayerUUID()); }


	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: GUI
	//~~~~~~~~~~~~~~~~~~~~~~~~~

	public CoreBoard getCoreBoard() {
		return coreBoard;
	}

	public CoreBar getCoreBar() {
		return coreBar;
	}

//	-------- CoreBox --------

	public boolean hasCoreBox() {
		return coreBox != null && coreBox.get() != null;
	}

	public boolean hasCoreBox(CoreBox box) {
		return coreBox != null && coreBox.get() == box;
	}

	public void setCoreBox(CoreBox box) {
		if (hasCoreBox())
			deleteCoreBox();

		coreBox = new WeakReference<CoreBox>(box);
	}

	public void deleteCoreBox() {
		if (hasCoreBox() && isOnline())
			getPlayer().closeInventory();

		coreBox = null;
	}


	//----------------------------------------
	//Instanced Methods: CoolDowns System
	//----------------------------------------

	/**
	 * Retrieves an unmodifiable view of the CoolDown List.
	 *
	 * @return CoolDowns
	 */
	public List<CoolDown> getCoolDowns() {
		return Collections.unmodifiableList(cds);
	}

	public CoolDown getCoolDown(String name) {
		if (hasCoolDown(name)) return cd_cache.get(name);
		else return null;
	}

	/**
	 * Tells whether or not the cooldown with the specified name is currently still active.
	 *
	 * @param name cooldown name
	 * @return true if still active and false if expired or does not exist.
	 */
	public boolean isCooling(String name) {
		return !hasCoolDown(name) || !cd_cache.get(name).expired();
	}

	/**
	 * Tells whether a cooldown exists or not.
	 *
	 * @param name cooldown name
	 * @return true if cooldown exists even if its expired. false otherwise.
	 */
	public boolean hasCoolDown(String name) {
		return cd_cache.containsKey(name);
	}

	/**
	 * Adds a new cooldown with the specified name and length.
	 * <p>Adding true to coreTimer param will make it display a timer on user screen through the {@link com.noxpvp.core.gui.CoreBoard} api.</p>
	 *
	 * @param name cooldown name
	 * @param length length of cooldown.
	 * @param coreTimer display as a timer on user screen?
	 * @return true if successfully made and false otherwise.
	 */
	public boolean addCoolDown(String name, CoolDown.Time length, boolean coreTimer) {
		CoolDown cd = addCoolDown(name, length);
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

			} catch (IllegalArgumentException e) {
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

	/**
	 * Adds a new cooldown with the specified name and length.
	 *
	 * <p>To display a render of the CoolDown. Use {@link #addCoolDown(String, com.noxpvp.core.gui.CoolDown.Time, boolean)} with a true param on the final param.</p>
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
			} else return null;
		}
		CoolDown cd = new CoolDown(name, length);

		cds.add(cd);
		cd_cache.put(cd.getName(), cd);

		return cd;
	}

	public void removeCoolDown(String name) {
		if (!cd_cache.containsKey(name))
			return;

		CoolDown cd = cd_cache.get(name);

		cds.remove(cd);
		cd_cache.remove(name);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced: Helpers
	//~~~~~~~~~~~~~~~~~~~~~~~~~~

	public OfflinePlayer getOfflinePlayer() {
		return PlayerUtils.getOfflinePlayer(getPlayerUUID());
	}

	public Player getPlayer() {
		if (isOnline()) return (Player) getOfflinePlayer();
		else return null;
	}

	/**
	 * @deprecated Use {@link #getPlayerName()} instead. This is here to help merge new system.
	 *
	 * @see #getPlayerName()
	 * @return {@link #getPlayerName()} result.
	 */
	@Deprecated
	@Temporary
	public String getName() {
		return getPlayerName();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("uuid", getPersistentID().toString());
		data.put("cool-downs", cds);
		data.put("stats", stats);

		return data;
	}

	public UUID getPersistentID() {
		return playerUUID;
	}

	public String getPersistenceNode() {
		return "NoxPlayer";
	}

	public String getLastFormattedName() {
		return lastFormattedName;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Deprecated Migratory Code.
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Deprecated
	/**
	 * @deprecated Please use {@link #isCooling(String)} instead.
	 */
	public boolean isCooldownActive(String name) {
		return isCooling(name);
	}

	/**
	 *
	 * @deprecated Please use {@link #getCoolDown(String)} along with {@link com.noxpvp.core.gui.CoolDown#getReadableTimeLeft()}
	 * @param name cooldown name
	 * @return readable cooldown time.
	 */
	@Deprecated
	public String getReadableRemainingCDTime(String name) {
		return getCoolDown(name).getReadableTimeLeft();
	}

}
