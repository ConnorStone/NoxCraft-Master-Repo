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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.gui.ColoredStringScroller;

public class ScrollingText extends BukkitRunnable {

	private static final int runPeriod = 3;

	private Player p;
	private CoreBar bar;

	private ColoredStringScroller text;

	private int displayTicks;
	private int runs;

	public ScrollingText(Player p, String text, int displayTicks) {
		this(p, text, displayTicks, true);
	}

	public ScrollingText(Player p, String text, int displayTicks, boolean canBeOverridden) {

		this.p = p;
		this.bar = CorePlayerManager.getInstance().getPlayer(p).getCoreBar();
		if (!bar.setLock(this, canBeOverridden))
			return;

		{
			int left = (62 - text.length());
			StringBuilder addon = new StringBuilder();

			while (addon.length() < left)
				addon.append(' ');

			text = (addon.toString() + ChatColor.RESET) + text;
		}

		this.text = new ColoredStringScroller(text);

		this.displayTicks = canBeOverridden ? displayTicks : (displayTicks <= 0 ? 500 : displayTicks);
		this.runs = 0;

		bar.getCurrentEntry().update(100F, text.substring(0, text.length()));

		this.runTaskTimer(NoxCore.getInstance(), 0, runPeriod);
	}

	public boolean checkValid() {
		return !(!bar.hasLock(this) || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) ||
				p == null || !p.isOnline() || !p.isValid());

	}

	public void run() {
		if (!checkValid()) {
			safeCancel();
			return;
		}

		bar.getCurrentEntry().update(100F, text.scroll().substring(0, 64));

	}

	public void safeCancel() {
		try {
			bar.removeIfLockedBy(this);
			cancel();
		} catch (IllegalStateException e) {
		}
	}

}