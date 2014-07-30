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

package com.noxpvp.core.listeners;

import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.NoxPlugin;

public abstract class NoxPacketListener implements PacketListener {

	private PacketType[] listeningTypes;

	public NoxPacketListener(PacketType... packetTypes) {

		this.listeningTypes = packetTypes;
	}

	public abstract NoxPlugin getPlugin();

	public void register() {
		PacketUtil.addPacketListener(getPlugin(), this, listeningTypes);
	}

	public void unRegister() {
		PacketUtil.addPacketListener(getPlugin(), this, listeningTypes);
	}

}
