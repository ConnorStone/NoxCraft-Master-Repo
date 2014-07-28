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

package com.noxpvp.mmo.abilities.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutMultiBlockChange;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.comphenix.packetwrapper.BlockChangeArray;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class MinersHelmetEntityAbility extends BaseEntityAbility {

	public static final String ABILITY_NAME = "Miner's Helmet";
	public static final String PERM_NODE = "miners-helmet";

	private int duration;
	private int speed;

	public MinersHelmetEntityAbility(Entity e) {
		super(ABILITY_NAME, e);

		this.duration = 120;
		this.speed = 10;
	}

	/**
	 * Get the duration is seconds
	 *
	 * @return Integer seconds
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the duration in seconds for this ability
	 *
	 * @param duration
	 * @return MinersHelmetAbility This instance
	 */
	public MinersHelmetEntityAbility setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * Gets the speed in ticks for the block changer checks
	 *
	 * @return Integer Speed in ticks
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed in ticks for the block changer checks
	 *
	 * @param speed
	 * @return MinersHelmetAbility This instance
	 */
	public MinersHelmetEntityAbility setSpeed(int speed) {
		this.speed = speed;
		return this;
	}

	public AbilityResult<MinersHelmetEntityAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<MinersHelmetEntityAbility>(this, false);

		new MinersHelmet(getEntity(), duration * (20 / speed)).runTaskTimer(NoxMMO.getInstance(), 0, speed);

		return new AbilityResult<MinersHelmetEntityAbility>(this, true);
	}

	private class MinersHelmet extends BukkitRunnable {

		CommonPacket commonFakeBlock;
		NMSPacketPlayOutMultiBlockChange fakeBlock;
		private Entity e;
		private int runs;
		private int runsLimit;
		private Block b;
		private Material oldBlock;
		private Location last;

		public MinersHelmet(Entity e, int runsLimit) {
			this.e = e;

			this.runs = 0;

			this.runsLimit = runsLimit;
			this.last = e.getLocation();

			commonFakeBlock = new CommonPacket(PacketType.OUT_MULTI_BLOCK_CHANGE);
			fakeBlock = new NMSPacketPlayOutMultiBlockChange();
		}

		public void safeCancel() {
			try {
				cancel();
			} catch (IllegalStateException e) {
			}
		}

		public void run() {

			Location loc = e.getLocation();

			if (runs > 0) {
				if (!((int) loc.getX() != (int) last.getX() || (int) loc.getZ() != (int) last.getZ() || (int) loc.getY() - 1 != (int) last.getY())) {
					runs++;
					return;
				}

				updateBlock(last, oldBlock);
			}

			if (runs > runsLimit) {
				updateBlock(last, oldBlock);
				safeCancel();
				return;
			}

			if (e.isOnGround()) {
				loc.setY(loc.getY() - 1);
				b = e.getWorld().getBlockAt(loc);

				last = loc;
				oldBlock = b.getType();

				updateBlock(loc, Material.GLOWSTONE);

			} else {
				runs++;
				return;
			}

			runs++;
		}

		private void updateBlock(Location loc, Material BlockType) {

			try {

				commonFakeBlock = new CommonPacket(PacketType.OUT_MULTI_BLOCK_CHANGE);
				fakeBlock = new NMSPacketPlayOutMultiBlockChange();

				BlockChangeArray change = new BlockChangeArray(1);

				change.getBlockChange(0).
						setLocation(loc).
						setBlockID(BlockType.getId());

				commonFakeBlock.write(fakeBlock.chunk, new IntVector2(last.getChunk()));
				commonFakeBlock.write(fakeBlock.blockCount, 1);
				commonFakeBlock.write(fakeBlock.blockData, change.toByteArray());

				PacketUtil.broadcastPacket(commonFakeBlock, false);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
