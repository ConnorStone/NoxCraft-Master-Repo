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
 * To use this software with any different license terms you must get prior explicit written permission from the copyright hers.
 */

package com.noxpvp.mmo.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.filtering.Filter;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class PlayerClassUtil {
	
	public static final String			LOG_MODULE_NAME	= "PlayerClass";
	
	private static ModuleLogger			log;
	
	private static Filter<IPlayerClass>	changedFilter	= new Filter<IPlayerClass>() {
															
															public boolean isFiltered(
																	IPlayerClass element) {
																if (element
																		.getExp() >= 1)
																	return true;
																else if (element
																		.getLevel() >= 1)
																	return true;
																return false;
															}
														};
	
	private static Filter<IPlayerClass>	canUseFilter	= new Filter<IPlayerClass>() {
															
															public boolean isFiltered(
																	IPlayerClass element) {
																return element
																		.canUseClass();
															}
														};
	
	public static Collection<IPlayerClass> getAllClasses(MMOPlayer player) {
		return player.getPlayerClasses();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Utilities: getAllClasses
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static Collection<IPlayerClass> getAllClasses(UUID playerID) {
		return getAllClasses(MMOPlayerManager.getInstance().getPlayer(playerID));
	}
	
	public static Collection<IPlayerClass> getModifiedClasses(
			MMOPlayer player) {
		return getFiltered(player.getPlayerClasses(), changedFilter);
	}
	
	public static Collection<IPlayerClass> getModifiedClasses(
			OfflinePlayer player) {
		return getModifiedClasses(player.getUniqueId());
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Utilities: getModifiedClasses
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static Collection<IPlayerClass> getModifiedClasses(
			UUID playerID) {
		return getModifiedClasses(MMOPlayerManager.getInstance().getPlayer(playerID));
	}
	
	public static Collection<IPlayerClass> getUsableClasses(
			OfflinePlayer player) {
		return getUsableClasses(player.getUniqueId());
	}
	
	public static Collection<IPlayerClass> getUsableClasses(UUID playerID) {
		return (Collection<IPlayerClass>) getUsableClasses(MMOPlayerManager
				.getInstance().getPlayer(playerID));
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Utilities: getAllowedClasses
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static void init() {
		log = NoxMMO.getInstance().getModuleLogger(LOG_MODULE_NAME);
	}
	
	private static <T extends IPlayerClass> Collection<T> getFiltered(
			Collection<T> values, Filter<IPlayerClass> filter) {
		try {
			final Iterator<T> iterator = values.iterator();
			while (iterator.hasNext())
				if (!filter.isFiltered(iterator.next())) {
					iterator.remove();
				}
		} catch (final UnsupportedOperationException e) {
			// Ignore and retry with a new mutable version.
			return getFiltered(new ArrayList<T>(values), filter);
		}
		return values;
	}
	
	private static Collection<? extends IPlayerClass> getUsableClasses(
			MMOPlayer player) {
		return getFiltered(player.getPlayerClasses(), canUseFilter);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Internal Utilities: Filtering
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public Collection<? extends IPlayerClass> getAllClasses(OfflinePlayer player) {
		return getAllClasses(player.getUniqueId());
	}
	
}
