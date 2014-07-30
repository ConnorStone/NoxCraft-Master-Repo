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

package com.noxpvp.core.utils;

import java.util.logging.Level;

import org.bukkit.block.Beacon;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Furnace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.bergerkiller.bukkit.common.filtering.Filter;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.Pair;

public class BlockStateUtils {

	public static boolean blockStatesMatch(BlockState origin, BlockState current, Filter<Pair<BlockState>>... filters) {
		Pair<BlockState> states = new Pair<BlockState>(origin, current);

		for (Filter<Pair<BlockState>> filter : filters)
			if (!filter.isFiltered(states))
				return false;
		return true;
	}

	public static boolean blockStatesPerfectMatch(BlockState origin, BlockState current) {
		if (!origin.getBlock().equals(current.getBlock()))
			return false;

		if (!origin.getType().equals(current.getType()))
			return false;

		if (origin instanceof Chest) { // No need to check other state for instance since they both match types!
			if (!isChestMatch((Chest) origin, (Chest) current))
				return false;
		} else if (origin instanceof Beacon) {
			if (!isBeaconMatch((Beacon) origin, (Beacon) current))
				return false;
		} else if (origin instanceof BrewingStand) {
			if (!isBrewingStandMatch((BrewingStand) origin, (BrewingStand) current))
				return false;
//		} else if (state1 instanceof Dispenser) {
//			if (!isInventoryHolderMatch((InventoryHolder)state1, (InventoryHolder) state2))
//				return false;
//		} else if (state1 instanceof Dropper) {
//			if (!isDropperMatch((Dropper)state1, (Dropper) state2))
//				return false;
//		} else if (state1 instanceof Hopper) {
//			if (!isHopperMatch((Hopper)state1, (Hopper)state2))
//				return false;
		} else if (origin instanceof Furnace) {
			if (!isFurnaceMatch((Furnace) origin, (Furnace) current))
				return false;
		} else if (origin instanceof InventoryHolder) { ///Must come after Furnace (As its also an inventory holder!)
			if (!isInventoryHolderMatch((InventoryHolder) origin, (InventoryHolder) current))
				return false;
		} else if (origin instanceof CommandBlock) {
			if (!isCommandBlockMatch((CommandBlock) origin, (CommandBlock) current))
				return false;
		} else if (origin instanceof CreatureSpawner) {
			if (!isSpawnerMatch((CreatureSpawner) origin, (CreatureSpawner) current))
				return false;
		} else if (origin instanceof Jukebox) {
			if (!isJukeboxMatch((Jukebox) origin, (Jukebox) current))
				return false;
		} else if (origin instanceof NoteBlock) {
			if (!isNoteBlockMatch((NoteBlock) origin, (NoteBlock) current))
				return false;
		} else if (origin instanceof Sign) {
			if (!isSignMatch((Sign) origin, (Sign) current))
				return false;
		} else if (origin instanceof Skull) {
			if (!isSkullMatch((Skull) origin, (Skull) current))
				return false;
		}

		return true;
	}

	private static boolean isSkullMatch(Skull skull1, Skull skull2) {
		if (!skull1.getSkullType().equals(skull2.getSkullType()))
			return false;

		if (!skull1.getRotation().equals(skull2.getRotation()))
			return false;

		return skull1.getOwner().equals(skull2.getOwner());

	}

	private static boolean isSignMatch(Sign sign1, Sign sign2) {
		for (int i = 0; i < 3; i++)
			if (!sign1.getLine(i).equals(sign2.getLine(i)))
				return false;

		return true;
	}

	private static boolean isNoteBlockMatch(NoteBlock note1, NoteBlock note2) {
		return note1.getNote().equals(note2.getNote());

	}

	private static boolean isJukeboxMatch(Jukebox juke1, Jukebox juke2) {
		return juke1.getPlaying().equals(juke2.getPlaying());

	}

//	private static boolean isHopperMatch(Hopper hopper1, Hopper hopper2) {
//		return isInventoryHolderMatch(hopper1, hopper2);
//	}
//	
//	private static boolean isDropperMatch(Dropper dropper1, Dropper dropper2) {
//		return isInventoryHolderMatch(dropper1, dropper2);
//	}

	private static boolean isFurnaceMatch(Furnace furnace1, Furnace furnace2) {
		if (furnace1.getBurnTime() != furnace2.getBurnTime())
			return false;
		if (furnace1.getCookTime() != furnace2.getCookTime())
			return false;

		return isInventoryHolderMatch(furnace1, furnace2);
	}

	private static boolean isInventoryHolderMatch(InventoryHolder inventory1, InventoryHolder inventory2) {
		Inventory inv1 = inventory1.getInventory(), inv2 = inventory2.getInventory();

		if (inv1.getSize() != inv2.getSize())
			return false;

		if (!inv1.getName().equals(inv2.getName()))
			return false;

		if (!inv1.getTitle().equals(inv2.getTitle()))
			return false;

		for (int i = 0; i < inv1.getSize(); i++)
			if (!inv1.getItem(i).equals(inv2.getItem(i)))
				return false;

		return true;
	}

	private static boolean isSpawnerMatch(CreatureSpawner spawner1, CreatureSpawner spawner2) {
		if (!spawner1.getSpawnedType().equals(spawner2.getSpawnedType()))
			return false;

		if (!spawner1.getCreatureTypeName().equals(spawner2.getCreatureTypeName()))
			return false;

		return spawner1.getDelay() == spawner2.getDelay();

	}

	private static boolean isCommandBlockMatch(CommandBlock cmdBlock1, CommandBlock cmdBlock2) {
		if (!cmdBlock1.getName().equals(cmdBlock2.getName()))
			return false;

		return cmdBlock1.getCommand().equals(cmdBlock2.getCommand());

	}

	private static boolean isBrewingStandMatch(BrewingStand stand1, BrewingStand stand2) {
		//TODO: Determine if we wanna match items brewing and such
		return true;
	}

	private static boolean isBeaconMatch(Beacon beacon1, Beacon beacon2) {
		Inventory rInv1 = beacon1.getInventory(), rInv2 = beacon2.getInventory();

		try {
			BeaconInventory inv1 = (BeaconInventory) rInv1, inv2 = (BeaconInventory) rInv2;

			if (!inv1.getItem().equals(inv2.getItem()))
				return false;

		} catch (ClassCastException e) {
			NoxCore.getInstance().log(Level.SEVERE, "Bukkit is not using their own api for beacons! SUBMIT BUT REPORT TO THEM! \n Defaulting to Plain inventory Code!");
			return isInventoryHolderMatch(beacon1, beacon2);
		}
		return true;
	}

	private static boolean isChestMatch(Chest chest1, Chest chest2) {
		return isInventoryHolderMatch(chest1, chest2);
	}
}
