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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.internal.IHeated;
import com.noxpvp.core.packet.NoxPacketUtil;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.runnables.BlockTimerRunnable;

/**
 * @author NoxPVP
 */
public class HookShotPlayerAbility extends BasePlayerAbility implements IHeated {
	
	public static final String						ABILITY_NAME	= "Hook Shot";
	public static final String						PERM_NODE		= "hook-shot";
	
	private final ItemStack							pullRegent		= new ItemStack(
																			Material.STRING,
																			1);
	private final ItemStack							shootRegent		= new ItemStack(
																			Material.ARROW,
																			1);
	
	private BaseMMOEventHandler<ProjectileHitEvent>	hitHandler;
	
	private int										blockTime;
	private Material								holdingBlockType;
	private int										batId;
	private Arrow									arrow;
	private boolean									active			= false;
	
	/**
	 * @param player
	 *            Player used for the ability
	 */
	public HookShotPlayerAbility(OfflinePlayer player, int cooldown, double range) {
		super(ABILITY_NAME, player);
		
		setCD(new CoolDown.Time().seconds(cooldown));
		setRange(range);
		blockTime = 20 * 10;
		holdingBlockType = Material.GLASS;
		batId = NoxPacketUtil.getNewEntityId(1);
		
		hitHandler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(ABILITY_NAME).append(player.getName())
						.append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL, 1) {
			
			public void execute(ProjectileHitEvent event) {
				if (!active) {
					unregisterHandler(this);
					return;
				}
				
				if (!arrow.equals(event.getEntity()))
					return;
				
				Player p;
				
				if ((p = HookShotPlayerAbility.this.getPlayer()) == null)
					return;
				
				NoxPacketUtil.spawnFakeEntity(EntityType.BAT, batId, arrow
						.getLocation().add(0, -1, 0), true);
				NoxPacketUtil.spawnRope(p.getEntityId(), batId);
				StaticEffects.PlaySound(p, Sound.ARROW_HIT);
				
			}
			
			public String getEventName() {
				return "ProjectileHitEvent";
			}
			
			public Class<ProjectileHitEvent> getEventType() {
				return ProjectileHitEvent.class;
			}
			
			public boolean ignoreCancelled() {
				return true;
			}
		};
	}
	
	public HookShotPlayerAbility(Player p) {
		this(p, 5, 50);
	}
	
	public HookShotPlayerAbility(Player p, double range) {
		this(p, 5, range);
	}
	
	public HookShotPlayerAbility(Player p, int cd) {
		this(p, cd, 50);
	}
	
	@Override
	public AbilityResult<HookShotPlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<HookShotPlayerAbility>(this, false);
		
		if (active && arrow != null && arrow.isOnGround())
			return eventExecute();
		else if (!active) {
			final Player p = getPlayer();
			final PlayerInventory inv = p.getInventory();
			
			if (!inv.containsAtLeast(shootRegent, shootRegent.getAmount()))
				return new AbilityResult<HookShotPlayerAbility>(this, false,
						MMOLocale.ABIL_NOT_ENOUGH_REGENT.get(
								Integer.toString(shootRegent.getAmount()),
								shootRegent.getType().name().toLowerCase()));
			
			inv.removeItem(shootRegent);
			p.updateInventory();
			
			arrow = p.launchProjectile(Arrow.class);
			arrow.setVelocity(p.getLocation().getDirection().multiply(2));
			setActive(true);
			
			return new AbilityResult<HookShotPlayerAbility>(this, false,
					"&6You throw your hook!");
		}
		if (arrow != null) {
			arrow.remove();
		}
		arrow = null;
		setActive(false);
		return new AbilityResult<HookShotPlayerAbility>(this, false);
	}
	
	/**
	 * @return Integer Amount of seconds the supporting block will last
	 *         before removal
	 */
	public int getBlockTime() {
		return blockTime;
	}
	
	public double getDistance() {
		
		return getPlayer().getLocation().distance(arrow.getLocation());
	}
	
	/**
	 * @return Material Type of block used to support player
	 */
	public Material getHoldingBlockType() {
		return holdingBlockType;
	}
	
	public boolean hasLOS() {
		final Location loc = getPlayer().getLocation();
		loc.add(loc.getDirection().setY(0).normalize());
		
		return LineOfSightUtil.getTargetBlock(loc, (int) Math.floor(getDistance()),
				Material.AIR) == null;
	}
	
	/**
	 * @param blockTime
	 *            Time before the supporting block is removed
	 * @return HookShotAbility This instance used for chaining
	 */
	public HookShotPlayerAbility setBlockTime(int blockTime) {
		this.blockTime = blockTime;
		return this;
	}
	
	/**
	 * @param block
	 *            Material type that should be used to support the player
	 * @return HookShotAbility This instance used for chaining
	 */
	public HookShotPlayerAbility setHoldingBlockType(Material block) {
		holdingBlockType = block;
		return this;
	}
	
	private AbilityResult eventExecute() {
		final Block hBlock = arrow.getLocation().getBlock();
		final Player p = getPlayer();
		final Inventory inv = p.getInventory();
		NoxPacketUtil.removeEntity(batId);
		
		if (arrow.isDead() || !arrow.isValid() || !arrow.isOnGround()) {
			arrow.remove();
			arrow = null;
			setActive(false);
			
			return new AbilityResult<HookShotPlayerAbility>(this, false,
					"&cYou have no hook to pull to!");
		}
		
		final Material typeSame = hBlock.getType(), typeAbove = hBlock.getRelative(
				BlockFace.UP).getType();
		if (typeSame != Material.AIR || typeAbove != Material.AIR) {
			arrow.remove();
			setActive(false);
			return new AbilityResult<HookShotPlayerAbility>(this, false,
					"&cThe hook area didn't have enough space to pull!");
		}
		
		if (getDistance() > getRange()) {
			arrow.remove();
			setActive(false);
			
			return new AbilityResult<HookShotPlayerAbility>(this, false,
					MMOLocale.ABIL_RANGED_TOO_FAR.get(Double.toString(getRange())));
		}
		
		// if (!hasLOS()) {
		// arrow.remove();
		// setActive(false);
		//
		// return new AbilityResult(this, false,
		// MMOLocale.ABIL_NO_LOS.get(getName()));
		// }
		
		if (!inv.containsAtLeast(pullRegent, pullRegent.getAmount())) {
			arrow.remove();
			setActive(false);
			
			return new AbilityResult<HookShotPlayerAbility>(this, false,
					MMOLocale.ABIL_NOT_ENOUGH_REGENT.get(
							Integer.toString(pullRegent.getAmount()), pullRegent
									.getType().name().toLowerCase()));
		}
		
		inv.removeItem(pullRegent);
		p.updateInventory();
		
		final Block blockBelow = hBlock.getRelative(BlockFace.DOWN);
		if (blockBelow.getType() == Material.AIR) {
			hBlock.getRelative(BlockFace.DOWN).setType(holdingBlockType);
			new BlockTimerRunnable(blockBelow, Material.AIR, holdingBlockType)
					.runTaskLater(NoxMMO.getInstance(), blockTime);
		}
		
		p.setFallDistance(0);
		p.teleport(hBlock.getLocation().add(.50, .1, .50), TeleportCause.PLUGIN);
		arrow.remove();
		
		setActive(false);
		return new AbilityResult<HookShotPlayerAbility>(this, true,
				"&6You pull to the hook");
	}
	
	private void setActive(boolean active) {
		final boolean changed = this.active != active;
		this.active = active;
		
		if (changed)
			if (active) {
				registerHandler(hitHandler);
			} else {
				unregisterHandler(hitHandler);
			}
	}
	
}
