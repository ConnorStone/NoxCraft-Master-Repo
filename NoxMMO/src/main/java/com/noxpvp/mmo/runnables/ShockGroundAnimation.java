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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class ShockGroundAnimation extends BukkitRunnable {
	private static MetadataValue shockMeta = new FixedMetadataValue(NoxMMO.getInstance(), "noland");
	private static BaseMMOEventHandler<EntityChangeBlockEvent> landHandler = new BaseMMOEventHandler<EntityChangeBlockEvent>(
			new StringBuilder("ShockGroundAnimation").toString(),
			EventPriority.HIGHEST, 1) {

		public boolean ignoreCancelled() {
			return true;
		}

		public Class<EntityChangeBlockEvent> getEventType() {
			return EntityChangeBlockEvent.class;
		}

		public String getEventName() {
			return "EntityChangeBlockEvent";
		}

		public void execute(EntityChangeBlockEvent event) {

			Entity e = event.getEntity();
			if (!(e instanceof FallingBlock))
				return;

			if (!e.hasMetadata("ShockGround"))
				return;

			e.remove();
			event.setCancelled(true);

		}
	};

	static {
		NoxMMO.getInstance().getMasterListener().registerHandler(landHandler);
	}

	private int shockRange;
	private Vector shockVelo;
	private float percentThrown;
	private Block center;
	private List<Material> flowers;

	/**
	 * @param shockCenter   Location for center of shock
	 * @param shockRange    Distance of wave from center
	 * @param shockVelo     Height velocity to throw blocks
	 * @param percentThrown Decimal value of chance to throw blocks (1.0 = all / 0.8 = 80% of blocks)
	 * @param delay         Delay in ticks before starting
	 */
	public ShockGroundAnimation(Location shockCenter, int shockRange, double shockVelo, float percentThrown, int delay) {
		this.center = shockCenter.getBlock();
		this.shockRange = shockRange;
		this.shockVelo = new Vector().zero();
		this.percentThrown = percentThrown;
		this.flowers = Arrays.asList(Material.LONG_GRASS, Material.RED_ROSE, Material.YELLOW_FLOWER, Material.CROPS, Material.DEAD_BUSH, Material.VINE, Material.SAPLING);

	}

	private boolean isThrowable(Material type) {
		switch (type) {
			case AIR:
			case SAPLING:
			case POWERED_RAIL:
			case DETECTOR_RAIL:
			case TORCH:
			case FIRE:
			case REDSTONE_WIRE:
			case CROPS:
			case LADDER:
			case RAILS:
			case LEVER:
			case REDSTONE_TORCH_OFF:
			case REDSTONE_TORCH_ON:
			case STONE_BUTTON:
			case SUGAR_CANE_BLOCK:
			case PORTAL:
			case DIODE_BLOCK_OFF:
			case DIODE_BLOCK_ON:
			case PUMPKIN_STEM:
			case MELON_STEM:
			case VINE:
			case WATER_LILY:
			case NETHER_WARTS:
			case ENDER_PORTAL:
			case COCOA:
			case TRIPWIRE_HOOK:
			case TRIPWIRE:
			case FLOWER_POT:
			case CARROT:
			case POTATO:
			case WOOD_BUTTON:
			case SKULL:
			case REDSTONE_COMPARATOR_OFF:
			case REDSTONE_COMPARATOR_ON:
			case ACTIVATOR_RAIL:
			case CARPET:
			case LEAVES:
				return false;
			default:
				return true;
		}
	}

	public void start(int delay) {
		runTaskLater(NoxMMO.getInstance(), delay);
	}

	public void run() {

		int bx = center.getX();
		int y = center.getY();
		int bz = center.getZ();

		for (int x = bx - shockRange; x <= bx + shockRange; x++) {
			for (int z = bz - shockRange; z <= bz + shockRange; z++) {
				Block b = center.getWorld().getBlockAt(x, y + 3, z);

				while (!isThrowable(b.getType()) && b.getLocation().getY() >= (center.getY() - 3)) {
					b = b.getRelative(BlockFace.DOWN);
				}

				if (Math.random() > percentThrown) continue;

				if (flowers.contains(b.getType())) {
					b.breakNaturally();
				}
				if (b.getType() == Material.GRASS) {
					b.setType(Material.DIRT);
				}

				final FallingBlock nb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());

				nb.setVelocity(shockVelo.setY(RandomUtils.nextDouble() + 0.45));
				nb.setMetadata("ShockGround", shockMeta);
				nb.setDropItem(false);
			}
		}
	}
}
