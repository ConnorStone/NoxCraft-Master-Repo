package com.noxpvp.core.manager;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CorePlayerManager extends BaseManager<NoxPlayer>{

	private static CorePlayerManager instance;

	private CorePlayerManager() {
		super(NoxPlayer.class, "playerdata");
	}

	public static CorePlayerManager getInstance() {
		if (instance == null)
			instance = new CorePlayerManager();
		return instance;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~

	public NoxPlayer getPlayer(Player player) {
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
