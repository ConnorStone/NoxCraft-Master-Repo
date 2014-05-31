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

public class WrapperPlayClientHeldItemSlot extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.HELD_ITEM_SLOT;
    
    public WrapperPlayClientHeldItemSlot() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientHeldItemSlot(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the slot which the player has selected (0-8).
     * @return The current Slot
    */
    public short getSlot() {
        return handle.getIntegers().read(0).shortValue();
    }
    
    /**
     * Set the slot which the player has selected (0-8).
     * @param value - new value.
    */
    public void setSlot(short value) {
        handle.getIntegers().write(0, (int) value);
    }
}