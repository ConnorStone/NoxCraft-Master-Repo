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

package com.noxpvp.mmo.listeners;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.Material;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.OldMMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.classes.internal.ExperienceType;

public class ExperienceListener extends NoxListener<NoxMMO> {

	private final MMOPlayerManager pm;

	private final FileConfiguration expFile;
	private final ConfigurationNode expNode;
	private final ConfigurationNode matNode;
	private final int killExp;
	private final int miningExp;
	private final int excavationExp;
	private final int smeltingExp;
	private Map<String, Integer> cachedMultipliers;
	private List<Material> exc = new ArrayList<Material>();
	private List<Material> mine = new ArrayList<Material>();

	public ExperienceListener() {
		this(NoxMMO.getInstance());
	}

	public ExperienceListener(NoxMMO mmo) {
		super(mmo);

		this.pm = mmo.getPlayerManager();

		this.expFile = mmo.getExperienceConfig();
		this.expNode = expFile.getNode("experience");
		this.matNode = expFile.getNode("materials");

		this.killExp = expNode.get("amounts.base.kill", 100);
		this.miningExp = expNode.get("amounts.base.mining", 100);
		this.excavationExp = expNode.get("amounts.base.excavation", 100);
		this.smeltingExp = expNode.get("amounts.base.smelting", 100);

		this.cachedMultipliers = this.initializePresets();
		if (this.cachedMultipliers == null) this.cachedMultipliers = new HashMap<String, Integer>();
	}

	private static List<String> readLines(String fileName) {
		Scanner scanner = null;
		List<String> lines = new ArrayList<String>();
		try {
			InputStream i = NoxMMO.getInstance().getResource(fileName);
			if (i == null)
				return lines;

			scanner = new Scanner(i);

			while (scanner.hasNextLine())
				lines.add(scanner.nextLine());
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
		}

		return lines;
	}

	private Map<String, Integer> initializePresets() {
		Map<String, Integer> cache = new HashMap<String, Integer>();
		List<String> defs = new ArrayList<String>();

		Class<LivingEntity> living = LivingEntity.class;
		Class<Tameable> tamable = Tameable.class;

		/*
		 * Entity Killing / Taming
		 */
		for (EntityType type : EntityType.values()) {
			if (type == null || type.getEntityClass() == null)
				continue;
			else if (living.isAssignableFrom(type.getEntityClass()))
				defs.add("multipliers.kills." + type.name());
			else if (tamable.isAssignableFrom(type.getEntityClass()))
				defs.add("multipliers.taming." + type.name());

		}

		List<String> c = matNode.get("mining", new ArrayList<String>());
		List<String> c2 = matNode.get("excavation", new ArrayList<String>());

		List<String> d = readLines("defaultMining.txt");
		List<String> d2 = readLines("defaultExcavation.txt");

		boolean changes = false;
		for (String i : d)
			if (!c.contains(i)) {
				c.add(i);
				changes = true;
			}

		for (String i : d2)
			if (!c2.contains(i)) {
				c2.add(i);
				changes = true;
			}

		if (changes) {
			matNode.set("mining", c);
			matNode.set("excavation", c2);
		}

		mine.clear();
		for (Iterator<String> iterator = c.iterator(); iterator.hasNext(); ) {
			String i = iterator.next();
			Material m = Material.valueOf(i);
			if (m == null)
				m = Material.getMaterial(i);
			if (m == null) {
				changes = true;
				iterator.remove();
				continue;
			}

			mine.add(m);
		}

		exc.clear();
		for (Iterator<String> iterator = c2.iterator(); iterator.hasNext(); ) {
			String i = iterator.next();
			try {
				Material m = Material.valueOf(i);
				if (m == null)
					m = Material.getMaterial(i);
				if (m == null) {
					changes = true;
					iterator.remove();
					continue;
				}

				exc.add(m);
			} catch (IllegalArgumentException e) {

			}
		}

		if (changes)
			expFile.save();

		for (String value : defs)
			cache.put(value, expNode.get("amounts." + value, 1));

		return cache;
	}

	public int getExpBoost(Player player) {
		if (VaultAdapter.permission.has(player, NoxMMO.PERM_NODE + ".exp.booster.5"))
			return 4;
		else if (VaultAdapter.permission.has(player, NoxMMO.PERM_NODE + ".exp.booster.4"))
			return 3;
		else if (VaultAdapter.permission.has(player, NoxMMO.PERM_NODE + ".exp.booster.3"))
			return 2;
		else if (VaultAdapter.permission.has(player, NoxMMO.PERM_NODE + ".exp.booster.2"))
			return 1;
		else return 0;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onKill(EntityDeathEvent event) {
		LivingEntity killed = event.getEntity();

		EntityDamageByEntityEvent last = (EntityDamageByEntityEvent) ((killed.getLastDamageCause() instanceof EntityDamageByEntityEvent) ? killed.getLastDamageCause() : null);
		if (last == null) return;

		Player attacker = (Player) (last.getDamager() instanceof Player ? last.getDamager() : null);
		if (attacker == null) return;

		OldMMOPlayer mmoPlayer = pm.getPlayer(attacker);
		if (mmoPlayer == null) return;

		double multiplier = cachedMultipliers.get(killed.getType().name());

		mmoPlayer.addExp(ExperienceType.PVP, MathUtil.floor(killExp * multiplier));

	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent event) {
		Player breaker = event.getPlayer();

		OldMMOPlayer player = pm.getPlayer(breaker);
		if (player == null) return;

		double xpMulti = cachedMultipliers.get(event.getBlock().getType().name());
		if (xpMulti != 0)
			xpMulti = +getExpBoost(breaker);

		if (mine.contains(event.getBlock().getType()))
			player.addExp(ExperienceType.MINING, MathUtil.floor(miningExp * xpMulti));
		else if (exc.contains(event.getBlock().getType()))
			player.addExp(ExperienceType.EXCAVATION, MathUtil.floor(excavationExp * xpMulti));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSmelt(FurnaceExtractEvent event) {
		if (event.getExpToDrop() <= 0) return;

		Player smelter = event.getPlayer();

		OldMMOPlayer player = pm.getPlayer(smelter);

		if (player == null) return;
		double xpMulti = cachedMultipliers.get(event.getItemType().name());
		if (xpMulti != 0)
			xpMulti = +getExpBoost(smelter);

		if (xpMulti != 0)
			xpMulti = +getExpBoost(smelter);

		player.addExp(ExperienceType.SMELTING, MathUtil.floor(smeltingExp * xpMulti));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onTame(EntityTameEvent event) {
		AnimalTamer tamer = event.getOwner();

		Player player = null;
		if (!(tamer instanceof Player) || (player = (Player) tamer) == null)
			return;

		OldMMOPlayer mmoPlayer;
		if ((mmoPlayer = pm.getPlayer(player)) == null) return;

		double xpMulti = cachedMultipliers.get(event.getEntityType().name());
		if (xpMulti != 0)
			xpMulti = +getExpBoost(player);

		mmoPlayer.addExp(ExperienceType.TAMING, MathUtil.floor(miningExp * xpMulti));
	}
}
