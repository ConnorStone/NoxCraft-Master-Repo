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

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.inventory.InventoryType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerOpenWindow extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.OPEN_WINDOW;
    
    // Convert between inventory types and the ID
    private static List<InventoryType> inventoryByID = Arrays.asList(
        InventoryType.CHEST,
        InventoryType.WORKBENCH,
        InventoryType.FURNACE,
        InventoryType.DISPENSER,
        InventoryType.ENCHANTING,
        InventoryType.BREWING,
        InventoryType.MERCHANT,
        InventoryType.BEACON,
        InventoryType.ANVIL
    );
    
    public WrapperPlayServerOpenWindow() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerOpenWindow(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve a unique id number for the window to be displayed. 
     * <p>
     * Notchian server implementation is a counter, starting at 1.
     * @return The current Window id
    */
    public byte getWindowId() {
        return (byte) handle.getIntegers().read(0).byteValue();
    }
    
    /**
     * Set a unique id number for the window to be displayed. Notchian server implementation is a counter, starting at 1..
     * @param value - new value.
    */
    public void setWindowId(byte value) {
        handle.getIntegers().write(0, (int) value);
    }
    
    /**
     * Retrieve the window type to use for display. 
     * @return The current inventory type
    */
    public InventoryType getInventoryType() {
    	int id = handle.getIntegers().read(1);
    	
    	if (id >= 0 && id <= inventoryByID.size())
    		return inventoryByID.get(id);
    	else
    		throw new IllegalArgumentException("Cannot find inventory type " + id);
    }
    
    /**
     * Set the window type to use for display. 
     * @param value - new value.
    */
    public void setInventoryType(InventoryType value) {
    	int id = inventoryByID.indexOf(value);
    	
    	if (id > 0)
    		handle.getIntegers().write(1, id);
    	else
    		throw new IllegalArgumentException("Cannot find the ID of " + value);
    }
    
    /**
     * Retrieve the title of the window..
     * @return The current Window title
    */
    public String getWindowTitle() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set the title of the window..
     * @param value - new value.
    */
    public void setWindowTitle(String value) {
        handle.getStrings().write(0, value);
    }
    
    /**
     * Retrieve number of slots in the window.
     * <p>
     * This excludes the number of slots in the player inventory.
     * @return The current Number of Slots
    */
    public byte getNumberOfSlots() {
        return handle.getIntegers().read(2).byteValue();
    }
    
    /**
     * Set number of slots in the window
     * <p>
     * This excludes the number of slots in the player inventory.
     * @param value - new value.
    */
    public void setNumberOfSlots(byte value) {
        handle.getIntegers().write(2, (int) value);
    }
    
    /**
     * Set whether or not the title will be used as is.
     * <p>
     * If false, the client will look up a string like "window.minecart". If true, the client uses what the server provides. 
     * @param value - new value.
    */
    public void setTitleExact(boolean value) {
        handle.getSpecificModifier(boolean.class).write(0, value);
    }
    
    /**
     * Retrieve whether or not the title will be used as is.
     * <p>
     * If false, the client will look up a string like "window.minecart". If true, the client uses what the server provides. 
     * @return TRUE if it is, FALSE otherwise.
    */
    public boolean isTitleExact() {
        return handle.getSpecificModifier(boolean.class).read(0);
    }
    
    /**
     * Retrieve the entity horse's entity ID.
     * <p>
     * Only sent when window type is equal to 11 
     * @return The unknown field.
    */
    public int getEntityId() {
        return handle.getIntegers().read(3);
    }
    
    /**
     * Set the entity horse's entity ID.
     * <p>
     * Only sent when window type is equal to 11 
     * @param value - new value of the unknown field.
    */
    public void setEntityId(int value) {
        handle.getIntegers().write(3, value);
    }
}


