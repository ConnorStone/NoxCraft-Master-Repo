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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import com.bergerkiller.bukkit.common.utils.PlayerUtil;

public class PlayerUtils extends PlayerUtil {

	//~~~~~~~~~~~~~~~~~~~~~
	//Item Logic
	//~~~~~~~~~~~~~~~~~~~~~

	public static boolean hasAtleast(Player p, ItemStack item) {
		return hasAtleast(p.getInventory(), item, item.getAmount());
	}

	public static boolean hasAtleast(Player p, ItemStack item, int amount) {
		return hasAtleast(p.getInventory(), item, amount);
	}

	public static boolean hasAtleast(Inventory inv, ItemStack item, int amount) {
		Material type = item.getType();
		int totals = 0;

		for (ItemStack s : inv.getContents()) {
			if (s == null || s.getType() != type) continue;

			totals += s.getAmount();

		}

		return totals >= amount;

	}

	//~~~~~~~~~~~~~~~~~~~~~~~
	//Global Helpers
	//~~~~~~~~~~~~~~~~~~~~~~~
	public static boolean isOnline(UUID uuid) {
		return isOnline(getOfflinePlayer(uuid));
	}

	public static boolean isOnline(OfflinePlayer player) {
		return player.isOnline();
	}

	public static OfflinePlayer getOfflinePlayer(UUID uuid) {
		return Bukkit.getOfflinePlayer(uuid);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~
	//Sight Logic
	//~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * @author burgerkiller - https://forums.bukkit.org/threads/determine-whether-the-player-can-see-a-mob.56129/
	 */
	public static class LineOfSightUtil {

		public static Block getTargetBlock(Location from, int distance, Material... transparentTypes) {
			if (transparentTypes == null || transparentTypes.length == 0) {
				return getTargetBlock(from, distance, (Set<Material>) null);
			} else {
				Set<Material> types = new HashSet<Material>(transparentTypes.length);
				for (Material b : transparentTypes) types.add(b);
				return getTargetBlock(from, distance, types);
			}
		}

		public static Block getTargetBlock(Location from, int distance, Set<Material> transparentTypes) {
			BlockIterator itr = new BlockIterator(from, 0, distance);
			while (itr.hasNext()) {
				Block block = itr.next();
				Material type = block.getType();
				if (transparentTypes == null) {
					if (type == Material.AIR) continue;
				} else if (transparentTypes.contains(type)) {
					continue;
				}
				return block;
			}
			return null;
		}

		public static Block getTargetBlock(LivingEntity from, int distance, Set<Material> transparentTypes) {
			return getTargetBlock(from.getEyeLocation(), distance, transparentTypes);
		}

		public static Block getTargetBlock(LivingEntity from, int distance, Material... transparentTypes) {
			return getTargetBlock(from.getEyeLocation(), distance, transparentTypes);
		}

		public static Location getTargetBlockLocation(LivingEntity from, int distance, Set<Material> transparentTypes) {
			Block b = getTargetBlock(from.getEyeLocation(), distance, transparentTypes);
			if (b == null)
				return from.getLocation();

			return b.getLocation();
		}

		public static Location getTargetBlockLocation(LivingEntity from, int distance, Material... transparentTypes) {
			Block b = getTargetBlock(from.getEyeLocation(), distance, transparentTypes);
			if (b == null)
				return null;

			return b.getLocation();
		}

		public static boolean hasLineOfSight(LivingEntity from, Location to, Set<Material> transparentTypes) {
			return getTargetBlock(from, (int) Math.ceil(from.getLocation().distance(to)), transparentTypes) == null;
		}

		public static boolean hasLineOfSight(LivingEntity from, Location to, Material... transparentTypes) {
			return getTargetBlock(from, (int) Math.ceil(from.getLocation().distance(to)), transparentTypes) == null;
		}

	}

}
