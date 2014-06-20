package com.noxpvp.core.manager;

import com.noxpvp.core.data.NoxPlayer;

public class CorePlayerManager extends BasePlayerManager<NoxPlayer>{

	private static CorePlayerManager instance;

	private CorePlayerManager() {
		super(NoxPlayer.class, "playerdata");
	}

	public static CorePlayerManager getInstance() {
		if (instance == null)
			instance = new CorePlayerManager();
		return instance;
	}
}
