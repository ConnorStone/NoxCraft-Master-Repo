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

package com.noxpvp.core.gui.corebar;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.google.common.collect.Sets;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;

public class LivingEntityTracker extends BukkitRunnable {
	public static final String separater = "  ";
	public static final String color = ChatColor.GREEN.toString();
	private static final int runPeriod = 10;
	private LivingEntity e;
	private Player p;
	private CoreBar bar;

	private double distance;
	private int maxDistance;

	private boolean ignoreLOS;
	private Set<Material> transparents;

	private String text;
	private String stringDist;

	private int displayTicks;
	private int runs;

	public LivingEntityTracker(
			Player p,
			LivingEntity e,
			String text) {
		this(p, e, text, 25);
	}

	public LivingEntityTracker(
			Player p,
			LivingEntity e,
			String text,
			int maxDistance) {
		this(p, e, text, maxDistance, false);
	}

	public LivingEntityTracker(
			Player p,
			LivingEntity e,
			String text,
			int maxDistance,
			boolean ignoreLOS) {
		this(p, e, text, maxDistance, ignoreLOS, 300);
	}

	public LivingEntityTracker(
			Player p,
			LivingEntity e,
			String text,
			int maxDistance,
			boolean ignoreLOS,
			int displayTicks) {
		this(p, e, text, maxDistance, ignoreLOS, displayTicks, true);
	}

	public LivingEntityTracker(Player p, LivingEntity e, String text, int maxDistance, boolean ignoreLOS, int displayTicks, boolean canBeOverridden) {

		this.e = e;
		this.p = p;
		this.bar = CorePlayerManager.getInstance().getPlayer(p).getCoreBar();
		if (!bar.setLock(this, canBeOverridden))
			return;

		this.distance = p.getLocation().distance(e.getLocation());
		this.maxDistance = maxDistance;

		this.ignoreLOS = ignoreLOS;
		this.transparents = Sets.newHashSet();
		for (Material cur : Material.values()) {
			if (!MaterialUtil.ISSOLID.get(cur))
				continue;

			transparents.add(cur);
		}

		this.text = color + text;

		this.displayTicks = canBeOverridden ? displayTicks : (displayTicks <= 0 ? 500 : displayTicks);
		this.runs = 0;

		this.runTaskTimer(NoxCore.getInstance(), 0, runPeriod);
	}

	public boolean checkValid() {
		return !(e == null || p == null ||
				!bar.hasLock(this) || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) ||
				!e.isValid() || !p.isValid());

	}

	public void run() {

		if (!checkValid()) {
			safeCancel();
			return;
		}

		distance = p.getLocation().distance(e.getLocation());

		Block saw = LineOfSightUtil.getTargetBlock(p, 50, (Material) null);
		if (distance > maxDistance || (!ignoreLOS && (saw != null && !saw.getType().isTransparent()))) {
			safeCancel();
			return;
		}

		stringDist = color + separater + String.format("%.1f", distance);

		bar.getCurrentEntry().update(e, text.toString() + stringDist);

	}

	public void safeCancel() {
		try {
			bar.removeIfLockedBy(this);
			cancel();
		} catch (IllegalStateException e) {
		}
	}

}