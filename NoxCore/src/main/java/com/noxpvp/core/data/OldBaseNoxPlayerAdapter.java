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

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.proxies.ProxyBase;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.manager.old.CorePlayerManager;

public abstract class OldBaseNoxPlayerAdapter extends ProxyBase<OldNoxPlayer> implements OldNoxPlayerAdapter {

	public OldBaseNoxPlayerAdapter(OldNoxPlayerAdapter player) {
		this(player.getPlayerName());
	}

	public OldBaseNoxPlayerAdapter(OfflinePlayer player) {
		this(player.getName());
	}

	public OldBaseNoxPlayerAdapter(String name) {
		super(getNoxPlayer(name));
	}

	private static OldNoxPlayer getNoxPlayer(String name) {
		return CorePlayerManager.getInstance().getPlayer(name);
	}

	public void deleteCoreBox() {
		getProxyBase().deleteCoreBox();
	}

	public void removeCoolDown(String name) {
		getProxyBase().removeCoolDown(name);
	}

	public boolean hasCoreBox() {
		return getProxyBase().hasCoreBox();
	}

	public UUID getUUID(boolean autoUpdate) {
		return getProxyBase().getUUID(autoUpdate);
	}

	public void updatePersistantData() {
		getProxyBase().updatePersistantData();
	}

	public boolean isFirstLoad() {
		return getProxyBase().isFirstLoad();
	}

	public void setFirstLoad(boolean isFirstLoad) {
		getProxyBase().setFirstLoad(isFirstLoad);
	}

	public final OldNoxPlayer getNoxPlayer() {
		return getProxyBase();
	}

	public boolean hasFirstLoaded() {
		return getProxyBase().hasFirstLoaded();
	}

	public final ConfigurationNode getPersistantData() {
		return getProxyBase().getPersistantData();
	}

	public void setPersistantData(ConfigurationNode persistant_data) {
		getProxyBase().setPersistantData(persistant_data);
	}

	public final ConfigurationNode getTempData() {
		return getProxyBase().getTempData();
	}

	/**
	 * @deprecated calls {@link #getName()}. Use that instead for less processing.
	 */
	public final String getPlayerName() {
		return getName();
	}

	public final OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getName());
	}

	public final boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}

	public final Player getPlayer() {
		if (isOnline())
			return (Player) getOfflinePlayer();
		else
			return null;
	}

	public final String getFullName() {
		return getNoxPlayer().getFullName();
	}

	public String toString() {
		return getProxyBase().toString();
	}

	public int hashCode() {
		return getProxyBase().hashCode();
	}

	public boolean equals(Object o) {
		return getProxyBase().equals(o);
	}

	public CoreBoard getCoreBoard() {
		return getProxyBase().getCoreBoard();
	}

	public CoreBar getCoreBar() {
		return getProxyBase().getCoreBar();
	}

	public boolean hasCoreBox(CoreBox box) {
		return getProxyBase().hasCoreBox(box);
	}

	public void setCoreBox(CoreBox box) {
		getProxyBase().setCoreBox(box);
	}

	public boolean addCoolDown(String name, int seconds, boolean coreBoardTimer) {
		return getProxyBase().addCoolDown(name, seconds, coreBoardTimer);
	}

	public void decrementVote() {
		getProxyBase().decrementVote();
	}

	public List<CoolDown> getCoolDowns() {
		return getProxyBase().getCoolDowns();
	}
	
	public long getCooldownTimeRemaining(String name) {
		return getProxyBase().getCooldownTimeRemaining(name);
	}
	
	public String getReadableRemainingCDTime(String name) {
		return getProxyBase().getReadableRemainingCDTime(name);
	}

	public long getFirstJoin() {
		return getProxyBase().getFirstJoin();
	}

	public void setFirstJoin(long value) {
		getProxyBase().setFirstJoin(value);
	}

	public long getFirstJoin(boolean cached) {
		return getProxyBase().getFirstJoin(cached);
	}

	public Location getLastDeathLocation() {
		return getProxyBase().getLastDeathLocation();
	}

	public void setLastDeathLocation(Location loc) {
		getProxyBase().setLastDeathLocation(loc);
	}

	public long getLastDeathTS() {
		return getProxyBase().getLastDeathTS();
	}

	public void setLastDeathTS(long stamp) {
		getProxyBase().setLastDeathTS(stamp);
	}

	public long getLastJoin() {
		return getProxyBase().getLastJoin();
	}

	public long getLastJoin(boolean cached) {
		return getProxyBase().getLastJoin(cached);
	}

	public SafeLocation getLastLocation() {
		return getProxyBase().getLastLocation();
	}

	public World getLastWorld() {
		return getProxyBase().getLastWorld();
	}

	public String getLastWorldName() {
		return getProxyBase().getLastWorldName();
	}

	public Double getMoney() {
		return getProxyBase().getMoney();
	}

	public Double getMoney(String worldName) {
		return getProxyBase().getMoney(worldName);
	}

	public String getName() {
		return getProxyBase().getName();
	}

	public final void setName(String name) {
		getProxyBase().setName(name);
	}

	public int getVotes() {
		return getProxyBase().getVotes();
	}

	public void setVotes(int amount) {
		getProxyBase().setVotes(amount);
	}

	public boolean hasPermission(String permNode) {
		return getProxyBase().hasPermission(permNode);
	}

	public boolean hasPermissions(String... permissions) {
		return getProxyBase().hasPermissions(permissions);
	}

	public void incrementVote() {
		getProxyBase().incrementVote();
	}

	public boolean isCooldownActive(String name) {
		return getProxyBase().isCooldownActive(name);
	}

	public boolean isCooldownExpired(String name) {
		return getProxyBase().isCooldownExpired(name);
	}

	public void superLoad(boolean overwrite) {
		getProxyBase().load(overwrite);
	}

	public void removeCooldDown(String name) {
		getProxyBase().removeCoolDown(name);
	}

	public final void superSave() {
		getProxyBase().save();
	}

	public final void superLoad() {
		getProxyBase().load();
	}

	public final UUID getUUID() {
		return getProxyBase().getUUID();
	}

	public final String getUID() {
		return getProxyBase().getUID();
	}

	/**
	 * All data must be set in here. Do not save to file from here though.
	 */
	public abstract void save();

	public abstract void load();

	public void load(boolean overwrite) {
		getProxyBase().load(overwrite);
	}

	public void saveLastLocation() {
		getProxyBase().saveLastLocation();
	}

	public void saveLastLocation(Player player) {
		getProxyBase().saveLastLocation(player);
	}

	public void setFirstJoin() {
		getProxyBase().setFirstJoin();
	}

	public void setLastDeath(PlayerDeathEvent event) {
		getProxyBase().setLastDeath(event);
	}

	public void setLastDeathTS() {
		getProxyBase().setLastDeathTS();
	}

	public void setLastKill(Entity entity) {
		getProxyBase().setLastKill(entity);
	}

	public void setLastKillTS() {
		getProxyBase().setLastKillTS();
	}

	public void setLastKillTS(long stamp) {
		getProxyBase().setLastKillTS(stamp);
	}

	public void saveToManager() {
		CorePlayerManager.getInstance().savePlayer(getNoxPlayer());
	}
}
