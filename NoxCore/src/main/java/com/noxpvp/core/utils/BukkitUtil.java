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

package com.noxpvp.core.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;

public class BukkitUtil {

	/**
	 * Safely gets all online players.
	 * <p>This was made to help with build dependant issues with the new update from {@code Player[] to Collection<? extends Player>}</p>
	 * @return {@code Collection<? extends Player>} of online players.
	 */
	public static Collection<? extends Player> getOnlinePlayers() {
		try {
			if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).getReturnType() == Collection.class)
				return ((Collection<? extends Player>)Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
			else
				return Lists.newArrayList((Player[]) Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
		}
		catch (NoSuchMethodException ex){} // can never happen
		catch (InvocationTargetException ex){} // can also never happen
		catch (IllegalAccessException ex){} // can still never happen

		return Collections.emptyList();
	}

	/**
	 * Retrieves the amount of players on the server.
	 * @return int value of a count of online players.
	 */
	public static int getOnlinePlayerCount() {
		return getOnlinePlayers().size();
	}
}
