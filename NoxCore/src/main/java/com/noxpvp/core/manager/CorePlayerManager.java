package com.noxpvp.core.manager;

import com.noxpvp.core.data.NoxPlayer;
import org.bukkit.entity.Player;

public class CorePlayerManager extends BasePlayerManager<NoxPlayer>{

	//~~~~~~~~~~~~~~~~~~~~~~
	//Instance
	//~~~~~~~~~~~~~~~~~~~~~~

	public static CorePlayerManager getInstance() {
		if (instance == null)
			instance = new CorePlayerManager();
		return instance;
	}

	private static CorePlayerManager instance; //Instance of manager.

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
