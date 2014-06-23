package com.noxpvp.core.manager;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class BasePlayerManager<T extends Persistent> extends BaseManager<T> {
	public BasePlayerManager(Class<T> type, String saveFolderPath) {
		super(type, saveFolderPath);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~

	public T load(Player player) {
		return load(player.getUniqueId());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Required Implementations
	//~~~~~~~~~~~~~~~~~~~~~~~~

	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}

	public void load() {
		for (Player p : Bukkit.getOnlinePlayers())
			load(p);
	}

	public T getPlayer(Player player) {
		return get(player.getUniqueId());
	}

	//~~~~~~~~~~~~~~~~~~
	//Instanced: Helpers
	//~~~~~~~~~~~~~~~~~~

	public T loadPlayer(Player player) { return load(player); }

	public T loadPlayer(UUID playerUUID) { return load(playerUUID); }

	//~~~~~~~~~~~~~~~~~~~~~~~
	//Static Methods
	//~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tells whether or not the player is online.
	 *
	 * <p><b>Warning this will accept any Persistent object however UUID's may not match.</b></p>
	 * <p><b><i>It was intended to be used against any object that uses a player ID as its id!</i></b></p>
	 * <p>Using anything other than those such objects may produce extremely undesirable behaviour</p>
	 *
	 * @see #isOnline(java.util.UUID)
	 * @param object Used to grab the player by the specified object's UUID
	 * @return true if online false otherwise.
	 */
	public static boolean isOnline(Persistent object) { return isOnline(object.getPersistantID()); }

	/**
	 * Tells whether or not the player is online.
	 *
	 * @see org.bukkit.Bukkit#getOfflinePlayer(java.util.UUID)
	 * @see org.bukkit.OfflinePlayer#isOnline()
	 *
	 * @param uuid uuid of the player being checked.
	 * @return true if online false otherwise.
	 */
	public static boolean isOnline(UUID uuid) { return Bukkit.getOfflinePlayer(uuid).isOnline(); }
}
