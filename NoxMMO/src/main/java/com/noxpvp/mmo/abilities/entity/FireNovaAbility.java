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

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

/**
 * @author NoxPVP
 *
 */
public class FireNovaAbility extends BaseEntityAbility{
	
	public static final String ABILITY_NAME = "Fire Nova";
	public static final String PERM_NODE = "fire-nova";
	
	private int range;
	private int tickSpeed;
	private Material blockType = Material.FIRE;
	private boolean burnTallGrass;
	private FirenovaAnimation animation;
	
	/**
	 * 
	 * 
	 * @return Integer The currently set nova range emitted out from player
	 */
	public int getRange() {return this.range;}
	
	/**
	 * 
	 * 
	 * @param range Integer range that the fire nova will emitt from player
	 * @return FireNovaAbility This instance, used for chaining
	 */
	public FireNovaAbility setRange(int range) {this.range = range; return this;}
	
	/**
	 * 
	 * 
	 * @return Integer Currently set tick delay between fire rings
	 */
	public int getTickSpeed() {return tickSpeed;}
	
	/**
	 * 
	 * 
	 * @param tickSpeed Integer tick delay between fire ring creations/removal
	 * @return FireNovaAbility This instance, used for chaining
	 */
	public FireNovaAbility setTickSpeed(int tickSpeed) {this.tickSpeed = tickSpeed; return this;}
	
	/**
	 * 
	 * 
	 * @return Material Currently set fire Material (Default Material.FIRE)
	 */
	public Material getBlockType() {return blockType;}
	
	/**
	 * 
	 * 
	 * @param blockType The Material type to use for fire blocks (Default Material.FIRE)
	 * @return FireNovaAbility This instance, used for chaining
	 */
	public FireNovaAbility setBlockType(Material blockType) {this.blockType = blockType; return this;}
	
	/**
	 * 
	 * 
	 * @return Boolean If the fire Rings will burn any tall grass it their way, or go around them
	 */
	public boolean isBurnTallGrass() {return burnTallGrass;}
	
	/**
	 * 
	 * 
	 * @param burnTallGrass Boolean if the fire rings should burn tall grass is their way
	 * @return
	 */
	public FireNovaAbility setBurnTallGrass(boolean burnTallGrass) {this.burnTallGrass = burnTallGrass; return this;}
	
	/**
	 * 
	 * 
	 * @param entity The Entity type user for this ability instance (Also to fire ring center location)
	 */
	public FireNovaAbility(Entity entity){super(ABILITY_NAME, entity);}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		animation = new FirenovaAnimation();
		animation.start();
		
		return true;
	}
	
	private class FirenovaAnimation extends BukkitRunnable {
		private Entity e;
		private int i;
		private Block center;
		private HashSet<Block> fireBlocks;
		
		/**
		 * 
		 * The actual Fire Rings animation, handle with care...or evil
		 */
		public FirenovaAnimation() {
			this.e = getEntity();
			this.i = 0;
			this.center = e.getLocation().getBlock();
			this.fireBlocks = new HashSet<Block>();
			
		}
		
		public void safeCancel() {try { cancel(); } catch (IllegalStateException e) {}	}

		public void start() {
			if (e instanceof LivingEntity)
				((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, (10 * 20), 1));
			
			runTaskTimer(NoxMMO.getInstance(), 0, tickSpeed);
		}
		
		public void run() {
			// remove old fire blocks
			for (Block block : fireBlocks) {
				if (block.getType() == blockType) {
					block.setType(Material.AIR);
				}
			}
			fireBlocks.clear();

			i += 1;
			if (i <= range) {
				// set next ring on fire
				int bx = center.getX();
				int y = center.getY();
				int bz = center.getZ();
				for (int x = bx - i; x <= bx + i; x++) {
					for (int z = bz - i; z <= bz + i; z++) {
						if (Math.abs(x-bx) == i || Math.abs(z-bz) == i) {
							Block b = center.getWorld().getBlockAt(x, y, z);
							if (b.getType() == Material.AIR || (burnTallGrass && b.getType() == Material.LONG_GRASS)) {
								Block under = b.getRelative(BlockFace.DOWN);
								if (under.getType() == Material.AIR || (burnTallGrass && under.getType() == Material.LONG_GRASS)) {
									b = under;
								}
								b.setType(blockType);
								fireBlocks.add(b);
							} else if (b.getRelative(BlockFace.UP).getType() == Material.AIR || (burnTallGrass && b.getRelative(BlockFace.UP).getType() == Material.LONG_GRASS)) {
								b = b.getRelative(BlockFace.UP);
								b.setType(blockType);
								fireBlocks.add(b);
							}
						}
					}
				}
			} else if (i > range+1) {
				safeCancel();
				return;
			}
		}
	}
	

}
