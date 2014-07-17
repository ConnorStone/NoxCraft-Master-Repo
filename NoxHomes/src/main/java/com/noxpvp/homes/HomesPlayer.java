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

package com.noxpvp.homes;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.data.player.BasePluginPlayer;
import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.homes.homes.HomesContainer;
import com.noxpvp.homes.limits.HomeLimit;
import com.noxpvp.homes.limits.HomeLimitContainer;
import com.noxpvp.homes.locale.HomeLocale;
import com.noxpvp.homes.managers.HomeLimitManager;
import com.noxpvp.homes.managers.HomesPlayerManager;
import com.noxpvp.homes.permissions.HomePermission;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

import static com.noxpvp.core.localization.GlobalLocale.COMMAND_FAILED;
import static com.noxpvp.homes.locale.HomeLocale.*;
import static com.noxpvp.homes.permissions.HomePermission.*;

public class HomesPlayer extends BasePluginPlayer<NoxHomes> implements HomesContainer, HomeLimitContainer {

	//~~~~~~~~~~~~
	//Logging
	//~~~~~~~~~~~~
	private static ModuleLogger log;

	static {
		log = HomesPlayerManager.getInstance().getModuleLogger("HomesPlayer");
	}

	//~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~
	private List<BaseHome> homes;

	//An entirely self updating cache of homes. On home deletion and addition it should self update.
	//      It should be rare for full resyncs.
	private Map<String, BaseHome> home_cache = new MapMaker().weakValues().makeMap();

	//~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~

	//Primary Constructor
	public HomesPlayer(UUID playerUUID) {
		super(playerUUID);
	}

	public HomesPlayer(Player player) {
		this(player.getUniqueId());
	}

	public HomesPlayer(Map<String, Object> data) {
		super(data);

		//Setup Homes
		try { this.homes =(List<BaseHome>) data.get("homes"); }
		catch (Exception e) {
			if (e instanceof ClassCastException) {
				log(Level.SEVERE, "Data has been corrupted for player \"" + getPlayerUUID() + "\". Failed to deserialize a list of homes.");
				log(Level.WARNING, "Homes may have been erased as a result.");
			} else if (e instanceof NullPointerException) {
				log(Level.FINE, "There is no Homes for the player \"" + getPlayerUUID() + "\". Not entirely sure if data corruption is the cause.");
			}

			this.homes = new ArrayList<BaseHome>(); //Always makes a new list just in case the list is completely non existent.
		}

		updateCache();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Internals
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void updateCache() {
		home_cache.clear();

		for (BaseHome home : getHomes())
			home_cache.put(home.getName(), home);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Implements
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}

	public void log(Level level, String msg) {
		log.log(level, msg);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Homes
	//~~~~~~~~~~~~~~~~~~~~~~~~

	public BaseHome getHome(String name) {
		if (!hasHome(name)) return null;
		return this.home_cache.get(name);
	}

	public boolean hasHome(String name) {
		return home_cache.containsKey(name);
	}


	//Internally used.
	private boolean hasHome(BaseHome home) {
		return hasHome(home.getName());
	}

	public boolean addHome(BaseHome home) {
		return this.homes.add(home);
	}

	public boolean removeHome(String name) {
		if (!hasHome(name)) return false;

		return removeHome(getHome(name));
	}

	public boolean removeHome(BaseHome home) {
		boolean ret = this.homes.remove(home);

		if (ret) home_cache.remove(home.getName());

		return ret;
	}

	public boolean tryAddHome(Player sender, BaseHome home) throws NoPermissionException {
		final boolean isOwner = sender.getUniqueId().equals(getPlayerUUID());
		final boolean isDefaultHome = home instanceof DefaultHome;
		final String perm = ((isOwner) ? ((isDefaultHome) ? DEL_OWN_DEFAULT : DEL_OWN_NAMED) : ((isDefaultHome) ? DEL_OTHERS_DEFAULT : DEL_OTHERS_NAMED)).getName();
		final boolean hasPerm = VaultAdapter.permission.has(sender, perm);
		final boolean exists = hasHome(home);
		final boolean limitExceeded = getHomeLimit() != Short.MAX_VALUE && getHomeCount() >= getHomeLimit();
		final HomeLocale successLocale = (isOwner) ? HomeLocale.SETHOME_OWN : HomeLocale.SETHOME_OTHERS, delHome = (isOwner) ? DELHOME_OWN : DELHOME_OTHERS;

		if (!hasPerm) {
			throw new NoPermissionException(sender, perm, new StringBuilder().append("Cannot create ").append((isOwner) ? "a " : "other's ").append(isDefaultHome ? "Default" : "Named").append(" Home").append((isOwner)?"!":"s!").toString());
		}

		if (limitExceeded) {
			COMMAND_FAILED.message(sender, "You have the maximum amount of homes allowed. \n You have " + getHomeCount() + "/" + getHomeLimit());
			return false;
		}

		if (!exists || (exists && tryRemoveHome(sender, home))) {
			SafeLocation loc = new SafeLocation(home.getLocation());

			String townperm = HomePermission.SET_OTHER_TOWN.getName();
			if (TownyUtil.isClaimedLand(loc) && !TownyUtil.isOwnLand(sender, loc) && !getPlugin().getPermissionHandler().hasPermission(sender, townperm)) {
				HomeLocale.BAD_LOCATION.message(sender, "You are not allowed to set home in other towns.");
				return false;
			}

			boolean success = addHome(home);


			if (success) successLocale.message(sender, home.getOwner(), home.getName(), String.format(
					"x=%1$.1f y=%2$.1f z=%3$.1f on world \"%4$s\"", loc.getX(), loc.getY(), loc.getZ(), loc.getWorldName()
			));

			else COMMAND_FAILED.message(sender, "Failed to Create home.");

			return success;
		}

		return false;
	}

	public boolean tryRemoveHome(CommandSender sender, String home) throws NoPermissionException  {
		if (sender instanceof Player) return tryRemoveHome((Player) sender, home);
		else return tryRemoveHome(sender, getHome(home));
	}

	public boolean tryRemoveHome(Player sender, String home) throws NoPermissionException  {
		return tryRemoveHome(sender, getHome(home));
	}

	public boolean tryRemoveHome(CommandSender sender, BaseHome home) throws NoPermissionException  {
		if (sender instanceof Player) return tryRemoveHome((Player) sender, home);
		else {
			final boolean isOwner = false;
			final boolean hasHome = home != null && hasHome(home);
			final boolean isDefaultHome = home instanceof DefaultHome;
			final String perm = ((isOwner) ? ((isDefaultHome) ? DEL_OWN_DEFAULT : DEL_OWN_NAMED) : ((isDefaultHome) ? DEL_OTHERS_DEFAULT : DEL_OTHERS_NAMED)).getName();
			final boolean hasPerm = NoxHomes.getInstance().getPermissionHandler().hasPermission(sender, perm);
			final HomeLocale successLocale = (isOwner) ? DELHOME_OWN : DELHOME_OTHERS;

			if (!hasPerm) {
				throw new NoPermissionException(sender, perm, new StringBuilder().append("Cannot remove ").append((isOwner)? "your own ": "Other's ").append("Homes!").toString());
			} else if (!hasHome) {
				NON_EXISTENT.message(sender, (isOwner ? "Your" : home.getOwner() + "'s"), home.getName());
				return false;
			}

			boolean success;

			success = removeHome(home);

			if (success) successLocale.message(sender, (isOwner ? "Your" : home.getOwner() + "'s"), home.getName());
			else COMMAND_FAILED.message(sender, "Failed to remove home.");

			return success;
		}
	} //FIXME: Optimize and remove redudant code.

	public boolean tryRemoveHome(Player sender, BaseHome home) throws NoPermissionException  {
		final boolean isOwner = sender.getUniqueId().equals(getPlayerUUID());
		final boolean hasHome = home != null && hasHome(home);
		final boolean isDefaultHome = home instanceof DefaultHome;
		final String perm = ((isOwner) ? ((isDefaultHome) ? DEL_OWN_DEFAULT : DEL_OWN_NAMED) : ((isDefaultHome) ? DEL_OTHERS_DEFAULT : DEL_OTHERS_NAMED)).getName();
		final boolean hasPerm = NoxHomes.getInstance().getPermissionHandler().hasPermission(sender, perm);
		final HomeLocale successLocale = (isOwner) ? DELHOME_OWN : DELHOME_OTHERS;

		if (!hasPerm) {
			throw new NoPermissionException(sender, perm, new StringBuilder().append("Cannot remove ").append((isOwner)? "your own ": "Other's ").append("Homes!").toString());
		} else if (!hasHome) {
			NON_EXISTENT.message(sender, (isOwner ? "Your" : home.getOwner() + "'s"), home.getName());
			return false;
		}

		boolean success;

		success = removeHome(home);

		if (success) successLocale.message(sender, (isOwner ? "Your" : home.getOwner() + "'s"), home.getName());
		else COMMAND_FAILED.message(sender, "Failed to remove home.");

		return success;
	} //FIXME: Optimize and remove redudant code.

	public List<BaseHome> getHomes() {
		return Collections.unmodifiableList(homes);
	}

	public int getHomeCount() {
		return getHomes().size();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Home Limits
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/*--------------------------
	 *- Infinity Loop Warning -!
	 *--------------------------
	 *
	 * Warning: This gets called from HomeLimitManager.getLimit(HomesPlayer)
	 * Be careful when rearranging code execution.
	 */
	public List<HomeLimit> getApplicableLimits() {
		return HomeLimitManager.getInstance().getApplicableLimits(this);
	}

	public int getHomeLimit() {
		return HomeLimitManager.getInstance().getLimit(this);
	}

	public void setLimit(int limit) {
		throw new NotImplementedException("This has not been implemented. setLimit(int)");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("uuid", getPlayerUUID());
		data.put("homes", getHomes());

		return data;
	}

	public String getPersistenceNode() {
		return "HomesPlayer";
	}

}
