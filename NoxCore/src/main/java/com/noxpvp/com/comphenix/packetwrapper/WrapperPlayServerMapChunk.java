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

public class WrapperPlayServerMapChunk extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.MAP_CHUNK;
    
    public WrapperPlayServerMapChunk() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerMapChunk(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve chunk X Coordinate (multiply by 16 to get the true X).
     * @return The current X
    */
    public int getChunkX() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set chunk X Coordinate (multiply by 16 to get the true X).
     * @param value - new value.
    */
    public void setChunkX(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve chunk Z Coordinate (multiply by 16 to get the true Z).
     * @return The current Z
    */
    public int getChunkZ() {
        return handle.getIntegers().read(1);
    }
    
    /**
     * Set chunk Z Coordinate (multiply by 16 to get the true Z).
     * @param value - new value.
    */
    public void setChunkZ(int value) {
        handle.getIntegers().write(1, value);
    }
    
    /**
     * Retrieve whether or not the packet represents all sections in this vertical column. 
     * <p>
     * The primary bit map specifies exactly which sections are included, and which are air.
     * @return The current Ground-up continuous
    */
    public boolean getGroundUpContinuous() {
        return handle.getSpecificModifier(boolean.class).read(0);
    }
    
    /**
     * Set whether or not this packet represents all sections in this vertical column. 
     * <p>
     * The primary bit map specifies exactly which sections are included, and which are air.
     * @param value - new value.
    */
    public void setGroundUpContinuous(boolean value) {
        handle.getSpecificModifier(boolean.class).write(0, value);
    }
    
    /**
     * Retrieve a bitmask indicating which 16x16x16 section is stored in the compressed data.
     * @return The current Primary bit map
    */
    public short getPrimaryBitMap() {
        return handle.getIntegers().read(2).shortValue();
    }
    
    /**
     * Set a bitmask indicating which 16x16x16 section is stored in the compressed data.
     * @param value - new value.
    */
    public void setPrimaryBitMap(short value) {
        handle.getIntegers().write(2, (int) value);
    }
    
    /**
     * Retrieve a bitmask similar to {@link #getPrimaryBitMap()}, but this is used exclusively for the 'add' portion of the payload.
     * @return The current add bit map
    */
    public short getAddBitMap() {
        return handle.getIntegers().read(3).shortValue();
    }
    
    /**
     * Set a bitmask similar to {@link #getPrimaryBitMap()}, which is used exclusively for the 'add' portion of the payload.
     * @param value - new value.
    */
    public void setAddBitMap(short value) {
        handle.getIntegers().write(3, (int) value);
    }
    
    /**
     * Retrieve size of compressed chunk data.
     * @return The current Compressed size
    */
    public int getCompressedSize() {
        return handle.getIntegers().read(4);
    }
    
    /**
     * Set size of compressed chunk data..
     * @param value - new value.
    */
    public void setCompressedSize(int value) {
        handle.getIntegers().write(4, value);
    }
    
    /**
     * Retrieve the chunk data that is compressed using ZLib Deflate function.
     * <p>
     * This is the data that will be transmitted to the client.
     * @return The current Compressed data
    */
    public byte[] getCompressedData() {
        return (byte[]) handle.getByteArrays().read(0);
    }
    
    /**
     * Set the chunk data that has been compressed using ZLib Deflate function.
     * <p>
     * This is the data that will be transmitted to the client.
     * @param value - new value.
    */
    public void setCompressedData(byte[] value) {
        handle.getByteArrays().write(0, (byte[]) value);
    }
    
    /**
     * Retrieve the chunk data that is compressed using ZLib Deflate function.
     * <p>
     * This will not be transmitted to the client.
     * @return The current Compressed data
    */
    public byte[] getUncompressedData() {
        return (byte[]) handle.getByteArrays().read(1);
    }
    
    /**
     * Set the originally uncompressed chunk data.
     * <p>
     * This will not be transmitted to the client.
     * @param value - new value.
    */
    public void setUncompressedData(byte[] value) {
        handle.getByteArrays().write(1, (byte[]) value);
    }
}


