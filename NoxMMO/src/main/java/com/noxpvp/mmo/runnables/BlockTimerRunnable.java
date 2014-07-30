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

package com.noxpvp.mmo.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 */
public class BlockTimerRunnable extends BukkitRunnable {

	private Material newType;
	private Material oldType;
	private List<Block> blocks;

	/**
	 * @param block   The block to change
	 * @param newType The material the block should be changed to
	 * @param oldType The material the block should be changed from. If the block given is not this type it will 
	 * not be changed as something else in the world has changed it already. (set this to null for no checking)
	 */
	public BlockTimerRunnable(Block block, Material newType, Material oldType) {
		this.newType = newType;
		this.oldType = oldType;
		this.blocks = new ArrayList<Block>();
		this.blocks.add(block);
	}

	/**
	 * @param blocks  A list of blocks to change
	 * @param newType The material the block should be changed to
	 * @param oldType The material the block should be changed from. If the block given is not this type it will not be changed as something else is the world has changed it already. (set this to null for no checking)
	 */
	public BlockTimerRunnable(List<Block> blocks, Material newType, Material oldType) {
		this.newType = newType;
		this.oldType = oldType;
		this.blocks = blocks;
	}

	public void safeCancel() {
		try {
			cancel();
		} catch (IllegalStateException e) {
		}
	}

	public void run() {
		for (Block b : this.blocks) {
			if (b == null) continue;
			if (oldType != null && b.getType() != oldType) continue;

			b.setType(newType);
		}
	}
}
