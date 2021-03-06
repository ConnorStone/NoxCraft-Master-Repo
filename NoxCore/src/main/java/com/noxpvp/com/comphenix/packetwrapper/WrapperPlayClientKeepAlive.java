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

package com.noxpvp.com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

/**
 * This is the client's response to {@link WrapperPlayServerKeepAlive}.
 */
public class WrapperPlayClientKeepAlive extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.KEEP_ALIVE;
    
    public WrapperPlayClientKeepAlive() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientKeepAlive(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the server-generated random ID.
     * @return The current keep-alive ID
    */
    public int getKeepAliveId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the server-generated random ID.
     * @param value - new ID.
    */
    public void setKeepAliveId(int value) {
        handle.getIntegers().write(0, value);
    }
}


