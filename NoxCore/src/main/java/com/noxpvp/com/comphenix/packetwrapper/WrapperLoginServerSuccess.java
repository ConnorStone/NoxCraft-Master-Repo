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
import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class WrapperLoginServerSuccess extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Login.Server.SUCCESS;
    
    public WrapperLoginServerSuccess() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperLoginServerSuccess(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the UUID and player name of the connected client.
     * @return The current client profile.
    */
    public WrappedGameProfile getProfile() {
        return handle.getGameProfiles().read(0);
    }
    
    /**
     * Set the UUID and player name of the connected client as a game profile.
     * @param value - new profile.
    */
    public void setProfile(WrappedGameProfile value) {
        handle.getGameProfiles().write(0, value);
    }
}


