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

public class WrapperPlayServerAbilities extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ABILITIES;
    
    public WrapperPlayServerAbilities() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerAbilities(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve whether or not the current player is in creative mode.
     * @return Creative mode.
    */
    public boolean isCreativeMode() {
        return handle.getSpecificModifier(boolean.class).read(0);
    }
    
    /**
     * Set whether or not the current player is in creative mode.
     * @param value - new value.
    */
    public void setCreativeMode(boolean value) {
        handle.getSpecificModifier(boolean.class).write(0, value);
    }
    
    /**
     * Retrieve whether or not the current player is flying.
     * @return Creative mode.
    */
    public boolean isFlying() {
        return handle.getSpecificModifier(boolean.class).read(1);
    }
    
    /**
     * Set whether or not the current player is flying.
     * @param value - new value.
    */
    public void setFlying(boolean value) {
        handle.getSpecificModifier(boolean.class).write(1, value);
    }
    
    /**
     * Retrieve whether or not the current player is allowed to fly.
     * @return Creative mode.
    */
    public boolean isFlyingAllowed() {
        return handle.getSpecificModifier(boolean.class).read(2);
    }
    
    /**
     * Set whether or not the current player is allowed to fly.
     * @param value - new value.
    */
    public void setFlyingAllowed(boolean value) {
        handle.getSpecificModifier(boolean.class).write(2, value);
    }
    
    /**
     * Retrieve whether or not the current player is in god mode.
     * @return Creative mode.
    */
    public boolean isGodMode() {
        return handle.getSpecificModifier(boolean.class).read(3);
    }
    
    /**
     * Set whether or not the current player is in god mode.
     * @param value - new value.
    */
    public void setGodMode(boolean value) {
        handle.getSpecificModifier(boolean.class).write(3, value);
    }
    
    /**
     * Retrieve the current flying speed.
     * @return The current Flying speed
    */
    public float getFlyingSpeed() {
        return handle.getFloat().read(0);
    }
    
    /**
     * Set the current flying speed.
     * @param value - new value.
    */
    public void setFlyingSpeed(float value) {
        handle.getFloat().write(0, value);
    }
    
    /**
     * Retrieve the current walking speed.
     * @return The current walking speed
    */
    public float getWalkingSpeed() {
        return handle.getFloat().read(1);
    }
    
    /**
     * Set the current walking speed.
     * @param value - walking value.
    */
    public void setWalkingSpeed(float value) {
        handle.getFloat().write(1, value);
    }
}
