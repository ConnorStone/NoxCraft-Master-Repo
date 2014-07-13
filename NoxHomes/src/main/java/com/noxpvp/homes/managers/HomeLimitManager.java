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

package com.noxpvp.homes.managers;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.data.PluginPlayer;
import com.noxpvp.core.manager.BaseManager;
import com.noxpvp.core.utils.PlayerUtils;
import com.noxpvp.core.utils.UUIDUtil;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.limits.HomeLimit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class HomeLimitManager extends BaseManager<HomeLimit> {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Static Constant Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static Comparator<HomeLimit> sorter = new Comparator<HomeLimit>() {
		public int compare(HomeLimit o1, HomeLimit o2) {
			if (o1.equals(o2)) return 0;
			else if (o1.isCumulative() && !o2.isCumulative()) return -1;
			else if (!o1.isCumulative() && o2.isCumulative()) return 1;
			else return sgn(o1.getPriority(), o2.getPriority());
		}

		private int sgn(int a, int b) {
			if (a > b) return 1;
			else if (a < b) return -1;
			else return 0;
		}
	};

	//~~~~~~~~~~~~~~~~~~~~
	//Instance
	//~~~~~~~~~~~~~~~~~~~~

	private static HomeLimitManager instance; //Limiter Instance.

	public static HomeLimitManager getInstance() {
		if (instance == null) instance = new HomeLimitManager();
		return instance;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private boolean useSuperPerms;

	//~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~

	private HomeLimitManager() {
		super(HomeLimit.class, "", false);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~

	public boolean getUseSuperPerms() {
		return useSuperPerms;
	}

	public void setUseSuperPerms(boolean useSuperPerms) {
		this.useSuperPerms = useSuperPerms;
	}

	public FileConfiguration getConfig() {
		return getConfig("limits.yml");
	}

	@Override
	public ModuleLogger getModuleLogger(String... path) { return super.getModuleLogger(path); } //protected -> public

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods -> Limits: getLimits()
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public List<HomeLimit> getApplicableLimits(PluginPlayer<?> player) {
		return getApplicableLimits(player.getOfflinePlayer());
	}

	public List<HomeLimit> getApplicableLimits(UUID playerUUID) {
		return getApplicableLimits(PlayerUtils.getOfflinePlayer(playerUUID));
	}

	public List<HomeLimit> getApplicableLimits(OfflinePlayer player) {
		List<HomeLimit> limits = getLimits();
		filterLimits(player, limits); //Filter our limits to only applicable stuff.

		return limits;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods -> Limits: Internals
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void filterLimits(OfflinePlayer player, List<HomeLimit> limits) {
		try {
			Iterator<HomeLimit> iterator = limits.iterator();
			while (iterator.hasNext()) if (!iterator.next().canLimit(player)) iterator.remove();
		} catch (UnsupportedOperationException e) {
			log(Level.SEVERE, "filterLimits was fed a unmodifiable list...", e);
		} catch (Exception e) {
			log(Level.SEVERE, "An unexpected error occurred while filtering limits.", e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods -> Limits: getLimit()
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public List<HomeLimit> getLimits() {
		ArrayList<HomeLimit> homeLimits = new ArrayList<HomeLimit>(loadedCache.values());
		Collections.sort(homeLimits, sorter);

		return homeLimits;
	}

	public int getLimit(PluginPlayer<?> player) {
		return getLimit(player.getOfflinePlayer());
	}

	public int getLimit(UUID playerUUID) {
		return getLimit(PlayerUtils.getOfflinePlayer(playerUUID));
	}

	public int getLimit(OfflinePlayer player) {
		int ret = 0;
		HomeLimit base = null;
		for (HomeLimit limit : getApplicableLimits(player)) {
			if (limit.isCumulative())
				ret += limit.getLimit();
			else if (base == null || base.getPriority() < limit.getPriority()) base = limit;
		}

		/*
		 * If cumulative plus base is below 0 than result to base if base is above 0.
		 *
		 * If the base value equals -1 than return Short.MAX_VALUE as a fake infinity.
		 *
		 */
		if (ret + base.getLimit() < 0) return (base.getLimit() == -1) ? Short.MAX_VALUE : base.getLimit();

		return ret + base.getLimit();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Overrides
	//~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public File getFile() {
		if (folder == null) {
			folder = new File(getPlugin().getDataFolder(), saveFolder);
			folder.mkdirs();
		}
		return folder;
	}

	@Override
	protected HomeLimit load(UUID path) {
		return load(path, path.toString());
	}

	@Override
	protected HomeLimit load(UUID path, String node) {
		HomeLimit created = getConfig().get(node, HomeLimit.class);

		if (created != null) loadedCache.put(created.getPersistentID(), created);

		return created;
	}

	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}

	public void load() {
		getConfig().load();

		loadedCache.clear();
		for (String key : getConfig().getKeys())
			if (UUIDUtil.isUUID(key)) load(UUIDUtil.toUUID(key));
			else if (key.equals("settings")) continue; //We can skip settings.
			else log(Level.WARNING, key + " is not a valid UUID to load limits from!");

		ConfigurationNode settings = getConfig().getNode("settings");

		this.useSuperPerms = settings.get("use-superPerms", true);
	}
}
