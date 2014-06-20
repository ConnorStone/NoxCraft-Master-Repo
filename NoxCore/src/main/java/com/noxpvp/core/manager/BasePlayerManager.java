package com.noxpvp.core.manager;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class BasePlayerManager<T extends Persistant> extends BaseManager<T> {
	public BasePlayerManager(Class<T> type, String saveFolderPath) {
		super(type, saveFolderPath);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~

	public T getPlayer(Player player) {
		return get(player.getUniqueId());
	}

	public void load(Player player) {
		load(player.getUniqueId());
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
}
