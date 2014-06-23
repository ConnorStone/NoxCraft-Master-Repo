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

package com.noxpvp.mmo.abilities.player;

import com.noxpvp.mmo.OldMMOPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;

public class AutoToolPlayerAbilities {

	public static class AutoSword extends BasePlayerAbility implements IPassiveAbility<EntityDamageByEntityEvent> {

		public static final String ABILITY_NAME = "Auto Sword";
		public static final String PERM_NODE = "auto-sword";

		public AutoSword(Player player) {
			super(ABILITY_NAME, player);
		}

		public AbilityResult execute(EntityDamageByEntityEvent event) {
			if (!mayExecute())
				return new AbilityResult(this, false);

			Player p = getPlayer();
			Entity e = event.getEntity();

			OldMMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(p);

			int fireTicks = 50;

			IPlayerClass clazz = mmoPlayer.getPrimaryClass();

			fireTicks = (int) (((mmoPlayer != null) && clazz != null) ? clazz.getTotalLevel() / 2.5 : fireTicks);

			e.setFireTicks(fireTicks);

			return new AbilityResult(this, true);
		}

		public AbilityResult execute() {
			return new AbilityResult(this, true);
		}
	}

	public static class AutoTool extends BasePlayerAbility implements IPassiveAbility<BlockBreakEvent> {

		public static final String ABILITY_NAME = "Auto Tool";
		public static final String PERM_NODE = "auto-tool";

		public AutoTool(Player player) {
			super(ABILITY_NAME, player);
		}

		private void breakBlockBurned(Block block, Material type, int amount, short data) {
			block.setType(Material.AIR);
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(type, amount, data));
		}

		public AbilityResult execute() {
			return new AbilityResult(this, true);
		}

		public AbilityResult execute(BlockBreakEvent event) {
			if (!mayExecute())
				return new AbilityResult(this, false);

			Block block = event.getBlock();
			Material tool = getPlayer().getItemInHand().getType();

			switch (tool) {

				case GOLD_PICKAXE:
					switch (block.getType()) {
						case IRON_ORE:
							event.setCancelled(true);
							breakBlockBurned(block, Material.IRON_INGOT, 1, (short) 0);

							break;
						case GOLD_ORE:
							event.setCancelled(true);
							breakBlockBurned(block, Material.GOLD_INGOT, 1, (short) 0);

							break;
						case COBBLESTONE:
							event.setCancelled(true);
							breakBlockBurned(block, Material.STONE, 1, (short) 0);

							break;
						case NETHERRACK:
							event.setCancelled(true);
							breakBlockBurned(block, Material.NETHER_BRICK_ITEM, 1, (short) 0);

							break;
						default:
							return new AbilityResult(this, false);

					}

					break;
				case GOLD_AXE:
					switch (block.getType()) {

						case LOG:
							event.setCancelled(true);
							breakBlockBurned(block, Material.COAL, 1, (short) 0);

							break;
						case CACTUS:
							event.setCancelled(true);
							breakBlockBurned(block, Material.INK_SACK, 1, (short) 2);

							break;
						default:
							return new AbilityResult(this, false);

					}

					break;
				case GOLD_SPADE:
					switch (block.getType()) {

						case SAND:
							event.setCancelled(true);
							breakBlockBurned(block, Material.GLASS, 1, (short) 0);

							break;
						case CLAY:
							event.setCancelled(true);
							breakBlockBurned(block, Material.CLAY_BRICK, 3, (short) 0);

							break;
						default:
							return new AbilityResult(this, false);

					}

					break;
				default:
					return new AbilityResult(this, false);
			}

			return new AbilityResult(this, true);
		}
	}

	public static class AutoArmor extends BasePlayerAbility implements IPassiveAbility<EntityDamageEvent> {

		public static final String ABILITY_NAME = "Auto Armor";
		public static final String PERM_NODE = "auto-armor";

		public AutoArmor(Player player) {
			super(ABILITY_NAME, player);
		}

		public AbilityResult execute(EntityDamageEvent event) {
			return execute();
		}

		public AbilityResult execute() {
			Player p = getPlayer();

			if (p.getEquipment().getHelmet().getType() != Material.GOLD_HELMET)
				return new AbilityResult(this, false);

			p.setRemainingAir(p.getMaximumAir());

			return new AbilityResult(this, true);
		}
	}

}
