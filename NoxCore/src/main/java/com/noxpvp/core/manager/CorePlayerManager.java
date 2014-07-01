package com.noxpvp.core.manager;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Level;

public class CorePlayerManager extends BasePlayerManager<NoxPlayer>{

	//~~~~~~~~~~~~~~~~~~~~~~
	//Instance
	//~~~~~~~~~~~~~~~~~~~~~~

	public static CorePlayerManager getInstance() {
		if (instance == null) instance = new CorePlayerManager();
		return instance;
	}

	private static CorePlayerManager instance; //Instance of manager.

	//~~~~~~~~~~~~~~~~~~~~~~
	//Logging
	//~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public ModuleLogger getModuleLogger(String... moduleName) { return super.getModuleLogger(moduleName);} //protected -> public

	public void log(Level level, String msg) {
		getLogger().log(level, msg);
	}

	//~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~

	private CorePlayerManager() {
		super(NoxPlayer.class, "playerdata");

	}

	//~~~~~~~~~~~~~~~~~~~~~~
	//Instanced methods
	//~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public void unloadAndSave(UUID id) { //protected -> public
		super.unloadAndSave(id);
	}

	@Override
	public NoxPlayer load(Player player) {
		NoxPlayer ret = super.load(player);

		ret.getStats().addLastIGN(player);

		return ret;
	}

	@Override
	public void save(Player player) {
		NoxPlayer np = getIfLoaded(player.getUniqueId());
		if (np == null) return;

		np.getStats().addLastIGN(player);

		super.save(player);
	}

}
