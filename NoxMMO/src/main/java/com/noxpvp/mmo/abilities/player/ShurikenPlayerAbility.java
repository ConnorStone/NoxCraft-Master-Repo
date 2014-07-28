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

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.noxpvp.core.packet.NoxPacketUtil;
import com.noxpvp.core.utils.DamageUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class ShurikenPlayerAbility extends BasePlayerAbility implements PVPAbility {
	public static final String ABILITY_NAME = "Shuriken";
	public static final String PERM_NODE = "shuriken";
	private static final FixedMetadataValue trackingMeta = new FixedMetadataValue(NoxMMO.getInstance(), "Shuriken");
	private BaseMMOEventHandler<ProjectileHitEvent> hitHandler;
	private BaseMMOEventHandler<EntityDamageByEntityEvent> hitEntityHandler;

	private double distanceVelo;
	private double damageMultiplier;
	private boolean active;

	/**
	 * @param player       The user of this ability instance
	 * @param distanceVelo double multiplier of the users direction used as a velocity
	 */
	public ShurikenPlayerAbility(OfflinePlayer player, double distanceVelo, double damageMultiplier) {
		super(ABILITY_NAME, player);

		this.distanceVelo = distanceVelo;
		this.damageMultiplier = damageMultiplier;

		this.hitEntityHandler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageByEntityEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}

			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}

			public void execute(EntityDamageByEntityEvent event) {
				Projectile arrow = DamageUtil.getAttackingProjectile(event);

				if (arrow == null || !arrow.hasMetadata("Shuriken"))
					return;

				event.setDamage(event.getDamage() * getDamageMultiplier());
				arrow.remove();

				return;
			}
		};

		this.hitHandler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public Class<ProjectileHitEvent> getEventType() {
				return ProjectileHitEvent.class;
			}

			public String getEventName() {
				return "ProjectileHitEvent";
			}

			public void execute(ProjectileHitEvent event) {
				Projectile arrow = event.getEntity();

				if (arrow == null || !arrow.hasMetadata("Shuriken"))
					return;

				arrow.remove();

				return;
			}
		};
	}

	/**
	 * @param player The user of this ability instance
	 */
	public ShurikenPlayerAbility(Player player) {
		this(player, 1.5, 2);
	}

	private void setActive(boolean active) {
		boolean changed = this.active != active;
		this.active = active;

		if (changed)
			if (active) {
				registerHandler(hitEntityHandler);
				registerHandler(hitHandler);
			} else {
				unregisterHandler(hitEntityHandler);
				unregisterHandler(hitHandler);
			}

		return;
	}

	/**
	 * @return distanceVelo The currently set multiplier for the users direction used as a velocity
	 */
	public double getDistanceVelo() {
		return distanceVelo;
	}

	/**
	 * @param distanceVelo double multiplier of the users direction used as a velocity
	 * @return ShurikenAbility This instance, used for chaining
	 */
	public ShurikenPlayerAbility setDistanceVelo(double distanceVelo) {
		this.distanceVelo = distanceVelo;
		return this;
	}

	public double getDamageMultiplier() {
		return damageMultiplier;
	}

	public ShurikenPlayerAbility setDamageMultiplier(double damageMultiplier) {
		this.damageMultiplier = damageMultiplier;
		return this;
	}

	/**
	 * @return boolean If this ability executed successfully
	 */
	public AbilityResult<ShurikenPlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<ShurikenPlayerAbility>(this, false);

		Player p = getPlayer();

		Arrow a = p.launchProjectile(Arrow.class);
		a.setMetadata("Shuriken", trackingMeta);
		a.setVelocity(p.getLocation().getDirection().multiply(distanceVelo));

		ItemStack shuriken = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = shuriken.getItemMeta();
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
		shuriken.setItemMeta(meta);

		NoxPacketUtil.disguiseArrow(a, shuriken);

		setActive(true);
		return new AbilityResult<ShurikenPlayerAbility>(this, true);
	}

}
