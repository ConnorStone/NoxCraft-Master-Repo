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

public class WrapperPlayServerMapChunkBulk extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.MAP_CHUNK_BULK;
    
    public WrapperPlayServerMapChunkBulk() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerMapChunkBulk(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the size of the data field.
     * @return The current Data length
    */
    public int getDataLength() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the size of the data field.
     * @param value - new value.
    */
    public void setDataLength(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve whether or not the chunk data contains a light nibble array. 
     * <p>
     * This is true in the main world, false in the end + nether.
     * @return The current Sky light sent
    */
    public boolean getSkyLightSent() {
        return handle.getSpecificModifier(boolean.class).read(0);
    }
    
    /**
     * Set whether or not the chunk data contains a light nibble array. 
     * <p>
     * This is true in the main world, false in the end + nether.
     * @param value - new value.
    */
    public void setSkyLightSent(boolean value) {
        handle.getSpecificModifier(boolean.class).write(0, value);
    }
    
    /**
     * Retrieve the x coordinates for each chunk segment.
     * @return Chunk x coordindates.
    */
    public int[] getChunksX() {
        return handle.getIntegerArrays().read(0);
    }
    
    /**
     * Set the x coordinates for each chunk segment.
     * @param value - new value.
    */
    public void setChunksX(int[] value) {
        handle.getIntegerArrays().write(0, value);
    }
    
    /**
     * Retrieve the y coordinates for each chunk segment.
     * @return Chunk y coordindates.
    */
    public int[] getChunksY() {
        return handle.getIntegerArrays().read(1);
    }
    
    /**
     * Set the y coordinates for each chunk segment.
     * @param value - new value.
    */
    public void setChunksY(int[] value) {
        handle.getIntegerArrays().write(1, value);
    }
    
    /**
     * Retrieve the chunk mask for each chunk segment.
     * @return Chunk x coordindates.
    */
    public int[] getChunksMask() {
        return handle.getIntegerArrays().read(2);
    }
    
    /**
     * Set the chunk mask for each chunk segment.
     * @param value - new value.
    */
    public void setChunksMask(int[] value) {
        handle.getIntegerArrays().write(2, value);
    }
    
    /**
     * Retrieve the mask for the extra data in each chunk segment.
     * @return Chunk x coordindates.
    */
    public int[] getChunksExtraMask() {
        return handle.getIntegerArrays().read(3);
    }
    
    /**
     * Set the mask for the extra data in each chunk segment.
     * @param value - new value.
    */
    public void setChunksExtraMask(int[] value) {
        handle.getIntegerArrays().write(3, value);
    }
    
    /**
     * Retrieve the inflated buffer for each chunk segment.
     * @return Array of each chunk data array.
    */
    public byte[][] getChunksInflatedBuffers() {
        return handle.getSpecificModifier(byte[][].class).read(0);
    }
    
    /**
     * Set the inflated buffer for each chunk segment.
     * @param value - new value.
    */
    public void setChunksExtraMask(byte[][] value) {
        handle.getSpecificModifier(byte[][].class).write(0, value);
    }
    
    /**
     * Retrieve the uncompressed chunks data that will be compressed and sent.
     * @return The uncompressed chunks data.
     */
    public byte[] getUncompressedData() {
    	return handle.getByteArrays().read(1);
    }
    
    /**
     * Set the uncompressed chunks data that will be compressed and sent.
     * @param value - the uncompressed data.
     */
    public void setUncompressedData(byte[] value) {
    	handle.getByteArrays().write(1, value);
    }
}


