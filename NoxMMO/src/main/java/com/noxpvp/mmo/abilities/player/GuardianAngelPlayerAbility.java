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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.DespawnRunnable;
import com.noxpvp.mmo.runnables.HealRunnable;
import com.noxpvp.mmo.runnables.SetVelocityRunnable;

/**
 * @author NoxPVP
 */
public class GuardianAngelPlayerAbility extends BasePlayerAbility {

	public static final String PERM_NODE = "guardian-angel";
	public static final String ABILITY_NAME = "Guardian Angel";

	private static List<Villager> guardians = new ArrayList<Villager>();

	private BaseMMOEventHandler<PlayerInteractEntityEvent> noTradeHandler;

	private Villager guardian;
	private int range;
	private double healAmount;

	private boolean active;

	/**
	 * @param player The Player type user for this ability instance
	 */
	public GuardianAngelPlayerAbility(Player player) {
		super(ABILITY_NAME, player);

		this.noTradeHandler = new BaseMMOEventHandler<PlayerInteractEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("PlayerInteractEntityEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public Class<PlayerInteractEntityEvent> getEventType() {
				return PlayerInteractEntityEvent.class;
			}

			public String getEventName() {
				return "PlayerInteractEntityEvent";
			}

			public void execute(PlayerInteractEntityEvent event) {
				if (guardians.isEmpty()) {
					setActive(false);
					return;
				}

				Entity it = event.getRightClicked();
				if (it instanceof Villager && guardians.contains(it))
					event.setCancelled(true);

				return;
			}
		};

		this.healAmount = 6;
		this.range = 6;
	}

	public void setActive(boolean active) {
		boolean changed = this.active != active;
		this.active = active;

		if (changed)
			if (active)
				registerHandler(noTradeHandler);
			else
				unregisterHandler(noTradeHandler);
	}

	/**
	 * @return Integer Currently set range for the angel heal range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @param range The Integer range the angel should look for player to heal
	 * @return GuardianAngelAbility This instance, used for chaining
	 */
	public GuardianAngelPlayerAbility setRange(int range) {
		this.range = range;
		return this;
	}

	/**
	 * @return Integer The currently set amount to heal players
	 */
	public double getHealAmount() {
		return healAmount;
	}

	/**
	 * @param healAmount double amount that the angel should heal players
	 * @return GuardianAngelAbility This instance, used for chaining
	 */
	public GuardianAngelPlayerAbility setHealAmount(double healAmount) {
		this.healAmount = healAmount;
		return this;
	}

	private Villager craftNewGuardian(Location loc) {
		Villager v = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);

		v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 50, true), true);
		v.setProfession(Profession.PRIEST);
		v.setCustomNameVisible(true);
		v.setCustomName(ChatColor.GREEN + "Guardian Angel");
		v.setNoDamageTicks(Short.MAX_VALUE);

		return v;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		final NoxMMO instance = NoxMMO.getInstance();
		Player p = getPlayer();

		this.guardian = craftNewGuardian(LineOfSightUtil.getTargetBlockLocation(p, 10, (Set<Material>) null).add(0, 1, 0));

		for (Entity it : guardian.getNearbyEntities(range, range, range)) {
			if (!(it instanceof Player) || (it == guardian)) continue;

			LivingEntity e = (LivingEntity) it;

			new HealRunnable(e, 1, (int) healAmount).runTaskTimer(instance, 40, 5);

		}

		guardians.add(guardian);
		Bukkit.getScheduler().runTaskLater(instance, new Runnable() {

			public void run() {
				new ParticleRunner(ParticleType.happyVillager, guardian, true, 0f, 10, 5).start(0, 1);
				new SetVelocityRunnable(guardian, guardian.getVelocity().clone().setY(3)).runTask(instance);
				new DespawnRunnable(guardian).runTaskLater(instance, 5);

				Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
					public void run() {
						guardians.remove(guardian);
					}
				}, 10);
			}
		}, 95);

		setActive(true);
		return new AbilityResult(this, true);
	}

}
