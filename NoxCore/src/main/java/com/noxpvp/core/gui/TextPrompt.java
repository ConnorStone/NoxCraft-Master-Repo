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

package com.noxpvp.core.gui;

import com.bergerkiller.bukkit.common.utils.LogicUtil;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.listeners.NoxPacketListener;

public abstract class TextPrompt extends NoxPacketListener {

	private CommonPacket packet;
	private Player p;
	
	public TextPrompt(Player p) {
		super(PacketType.IN_UPDATE_SIGN);
		
		this.p = p;
		this.packet = new CommonPacket(PacketType.OUT_OPEN_SIGN_EDITOR);
		
	}
	
	public void show() {
		register();
		PacketUtil.sendPacket(p, packet, false);
	}
	
	public void onPacketReceive(PacketReceiveEvent event) {
		unRegister();
		String[] ret = event.getPacket().read(PacketType.IN_UPDATE_SIGN.lines);
		
		if (LogicUtil.nullOrEmpty(ret))
			onReturn(null);
		
		onReturn(ret);
	}
	
	public void onPacketSend(PacketSendEvent event) {
		return;
	}
	
	public abstract void onReturn(String[] lines);

}
