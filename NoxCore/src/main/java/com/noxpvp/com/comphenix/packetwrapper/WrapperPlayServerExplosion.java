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
import java.util.List;

import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.ChunkPosition;

public class WrapperPlayServerExplosion extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.EXPLOSION;
    
    public WrapperPlayServerExplosion() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerExplosion(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the x coordinate of the center of the explosion.
     * @return The current X
    */
    public double getX() {
        return handle.getDoubles().read(0);
    }
    
    /**
     * Set the x coordinate of the center of the explosion.
     * @param value - new value.
    */
    public void setX(double value) {
        handle.getDoubles().write(0, value);
    }
    
    /**
     * Retrieve the y coordinate of the center of the explosion.
     * @return The current Y
    */
    public double getY() {
        return handle.getDoubles().read(1);
    }
    
    /**
     * Set the y coordinate of the center of the explosion.
     * @param value - new value.
    */
    public void setY(double value) {
        handle.getDoubles().write(1, value);
    }
    
    /**
     * Retrieve the z coordinate of the center of the explosion.
     * @return The current Z
    */
    public double getZ() {
        return handle.getDoubles().read(2);
    }
    
    /**
     * Set the z coordinate of the center of the explosion.
     * @param value - new value.
    */
    public void setZ(double value) {
        handle.getDoubles().write(2, value);
    }
    
    /**
     * Retrieve the explosion radius.
     * <p>
     * Note: Currently unused in the client.
     * @return The current Radius
    */
    public float getRadius() {
        return handle.getFloat().read(0);
    }
    
    /**
     * Set the explosion radius.
     * <p>
     * Note: Currently unused in the client.
     * @param value - new value.
    */
    public void setRadius(float value) {
        handle.getFloat().write(0, value);
    }
    
    /**
     * Retrieve the absolute coordinates of each affected block in the explosion.
     * @return The current records.
    */
    public List<ChunkPosition> getRecords() {
        return handle.getPositionCollectionModifier().read(0);
    }
    
    /**
     * Set the absolute coordinates of each affected block in the explosion.
     * @param value - new value.
    */
    public void setRecords(List<ChunkPosition> value) {
        handle.getPositionCollectionModifier().write(0, value);
    }
    
    /**
     * Retrieve x velocity of the player being pushed by the explosion.
     * @return The current Player Motion X
    */
    public float getPlayerMotionX() {
        return handle.getFloat().read(0);
    }
    
    /**
     * Set x velocity of the player being pushed by the explosion.
     * @param value - new value.
    */
    public void setPlayerMotionX(float value) {
        handle.getFloat().write(0, value);
    }
    
    /**
     * Retrieve y velocity of the player being pushed by the explosion.
     * @return The current Player Motion Y
    */
    public float getPlayerMotionY() {
        return handle.getFloat().read(1);
    }
    
    /**
     * Set y velocity of the player being pushed by the explosion.
     * @param value - new value.
    */
    public void setPlayerMotionY(float value) {
        handle.getFloat().write(1, value);
    }
    
    /**
     * Retrieve z velocity of the player being pushed by the explosion.
     * @return The current Player Motion Z
    */
    public float getPlayerMotionZ() {
        return handle.getFloat().read(2);
    }
    
    /**
     * Set z velocity of the player being pushed by the explosion.
     * @param value - new value.
    */
    public void setPlayerMotionZ(float value) {
        handle.getFloat().write(2, value);
    }
    
    /**
     * Retrieve velocity of the player being pushed by the explosion.
     * @return New velocity.
     */
    public Vector getPlayerMotion() {
    	return new Vector(getPlayerMotionX(), getPlayerMotionY(), getPlayerMotionZ());
    }
    
    /**
     * Set the velocity of the player being pushed by the explosion.
     * @param motion - new velocity.
     */
    public void setPlayerMotion(Vector motion) {
    	setPlayerMotionX((float) motion.getX());
    	setPlayerMotionY((float) motion.getY());
    	setPlayerMotionZ((float) motion.getZ());
    }
}
