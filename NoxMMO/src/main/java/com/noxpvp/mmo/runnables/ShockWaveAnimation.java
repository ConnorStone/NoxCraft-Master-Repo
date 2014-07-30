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

/**
 * @author NoxPVP
 */
public class ShockWaveAnimation extends BukkitRunnable {

	private static MetadataValue shockMeta = new FixedMetadataValue(NoxMMO.getInstance(), "noland");
	private static BaseMMOEventHandler<EntityChangeBlockEvent> landHandler = new BaseMMOEventHandler<EntityChangeBlockEvent>(
			new StringBuilder("ShockWaveAnimation").toString(),
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

			if (!e.hasMetadata("ShockWave"))
				return;

			e.remove();
			event.setCancelled(true);

		}
	};

	static {
		NoxMMO.getInstance().getMasterListener().registerHandler(landHandler);
	}

	private Block center;
	private int shockSpeed;
	private int shockRange;
	private Vector shockVelo;
	private boolean isCircle;

	private int i;
	private List<Material> flowers;


	/**
	 * @param shockCenter Location the shockwave will start from
	 * @param shockSpeed  The delay in ticks between each ring
	 * @param shockRange  the range from the location the shockwave will extend
	 * @param circle      If the wave should be a circle
	 */
	public ShockWaveAnimation(Location shockCenter, int shockSpeed, int shockRange, boolean circle) {
		this(shockCenter, shockSpeed, shockRange, 0.35, circle);
	}

	/**
	 * @param shockCenter Location the shockwave will start from
	 * @param shockSpeed  The delay in ticks between each ring
	 * @param shockRange  the range from the location the shockwave will extend
	 * @param shockVelo   the velocity height of the shockwave blocks
	 */
	public ShockWaveAnimation(Location shockCenter, int shockSpeed, int shockRange, double shockVelo) {
		this(shockCenter, shockSpeed, shockRange, shockVelo, false);
	}

	/**
	 * @param shockCenter Location the shockwave will start from
	 * @param shockSpeed  The delay in ticks between each ring
	 * @param shockRange  the range from the location the shockwave will extend
	 * @param shockVelo   the velocity height of the shockwave blocks
	 * @param isCircle    If the animation is circular, other wise square
	 */
	public ShockWaveAnimation(Location shockCenter, int shockSpeed, int shockRange, double shockVelo, boolean isCircle) {
		this.center = shockCenter.getBlock().getRelative(BlockFace.DOWN);
		this.shockSpeed = shockSpeed;
		this.shockRange = shockRange;
		this.shockVelo = new Vector().setY(shockVelo);
		this.isCircle = isCircle;

		this.i = 0;
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

	public void safeCancel() {
		try {
			cancel();
		} catch (IllegalStateException e) {
		}
	}

	public void start(int delay) {
		runTaskTimer(NoxMMO.getInstance(), delay, shockSpeed);
	}

	public void run() {

		i += 1;
		if (i <= shockRange) {
			// do next ring
			int bx = center.getX();
			int y = center.getY();
			int bz = center.getZ();

			for (int x = bx - i; x <= bx + i; x++) {
				for (int z = bz - i; z <= bz + i; z++) {
					Block b = null;

					if (isCircle) {
						b = center.getWorld().getBlockAt(x, y + 3, z);

						double dis;
						if ((dis = Math.abs(b.getLocation().distance(center.getLocation().clone().add(0, 3, 0)))) > (i + .50) || dis < (i - .50))
							continue;

					} else if (Math.abs(x - bx) == i || Math.abs(z - bz) == i) {
						b = center.getWorld().getBlockAt(x, y + 3, z);//+3 - max height above location
					}

					if (b == null) continue;

					Material type = b.getType();
					while (!isThrowable(type) && b.getLocation().getY() > (center.getY() - 4)) {
						b = b.getRelative(BlockFace.DOWN);
						type = b.getType();
					}

					final FallingBlock nb = b.getWorld().spawnFallingBlock(b.getLocation(), type == Material.GRASS ? Material.DIRT : type, b.getData());

					nb.setVelocity(shockVelo);
					nb.setMetadata("ShockWave", shockMeta);
					nb.setDropItem(false);

				}
			}

		} else {
			safeCancel();
			return;
		}
	}
}
