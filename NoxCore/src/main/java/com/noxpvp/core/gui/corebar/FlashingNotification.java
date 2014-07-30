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

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.manager.CorePlayerManager;

public class FlashingNotification extends BukkitRunnable {

	private static final int runPeriod = 2;

	private Player p;
	private CoreBar bar;

	private Cycler<Character> colors;
	private StringBuilder text;
	private String newInsert;

	private int displayTicks;
	private int runs;

	public FlashingNotification(Player p, String text) {
		this(p, text, 500);
	}

	public FlashingNotification(Player p, String text, int displayTicks) {
		this(p, text, displayTicks, true);
	}

	public FlashingNotification(Player p, String text, int displayTicks, boolean canBeOverridden) {

		this.p = p;
		this.bar = CorePlayerManager.getInstance().getPlayer(p).getCoreBar();
		if (!bar.setLock(this, canBeOverridden))
			return;

		colors = new Cycler<Character>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'));
		this.text = new StringBuilder(text);

		this.displayTicks = canBeOverridden ? displayTicks : (displayTicks <= 0 ? 500 : displayTicks);
		this.runs = 0;

		bar.getCurrentEntry().update(100F, text);
		this.runTaskTimer(NoxCore.getInstance(), 0, runPeriod);
	}

	private boolean checkValid() {
		return !(!bar.hasLock(this) || (displayTicks != 0 && ((runs * runPeriod) > displayTicks)) || p == null || !p.isOnline());
	}

	public void run() {
		if (!checkValid()) {
			safeCancel();
			return;
		}

		if (runs++ > 0)
			text.delete(0, newInsert.length());

		newInsert = ChatColor.COLOR_CHAR + colors.next().toString();
		text.insert(0, newInsert);

		bar.getCurrentEntry().update(text.toString());

	}

	public void safeCancel() {
		try {
			bar.removeIfLockedBy(this);
			cancel();
		} catch (IllegalStateException e) {
		}

	}

}