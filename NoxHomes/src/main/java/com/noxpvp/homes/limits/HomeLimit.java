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

package com.noxpvp.homes.limits;

import com.noxpvp.core.Persistent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface HomeLimit extends Persistent {

	/**
	 * Tell whether or not this limiter applies to this player.
	 * @param player to check
	 * @return true if being limited by this limiter else false.
	 */
	public boolean canLimit(OfflinePlayer player);

	/**
	 * Tells whether or not the player has passed the allowed limit.
	 * <p>By definition its best to route this to {@link #isPastLimit(java.util.UUID)} with the {@link org.bukkit.entity.Player#getUniqueId()} return value</p>
	 * @param player player to check limit
	 * @return true if past limit or false otherwise.
	 */
	public boolean isPastLimit(Player player);

	/**
	 * Tells whether or not the player has passed the allowed limit.
	 * @param uuid uuid of player to check limit
	 * @return true if past limit or false otherwise.
	 */
	public boolean isPastLimit(UUID uuid);

	/**
	 * Returns the home limit.
	 * @return int value of how many homes you can have max.
	 */
	public int getLimit();

	/**
	 * Retrieves the priority level of this limiter.
	 * @return int level of priority. Lower is executed sooner.
	 */
	public int getPriority();

	/**
	 * Retrieves whether or not this limit can be added on top of others.
	 * @return true if cumulative else false.
	 */
	public boolean isCumulative();
}
