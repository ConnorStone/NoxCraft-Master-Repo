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


package com.noxpvp.core.gui;

import me.confuser.barapi.BarAPI;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.ObjectLock;

public class CoreBar {

	public static String separater;
	public static String color;
	public final Player p;
	private final Entry currentEntry = new Entry();
	private ObjectLock lock;

	/**
	 * @param p - The player to give this bar to
	 */
	public CoreBar(NoxCore core, Player p) {
		this.p = p;

		lock = new ObjectLock(null, true);

		if (color == null || separater == null) {
			separater = core.getCoreConfig().get("gui.corebar.separater", String.class, "  ||  ");
			color = core.getCoreConfig().get("gui.corebar.default-color", String.class, ChatColor.GREEN.toString());
		}

	}

	public boolean isChangeable() {
		return (lock == null || lock.lock == null || lock.canUnlock);
	}

	public boolean hasLock(Object lock) {
		return (this.lock != null && this.lock.lock != null && this.lock.lock.equals(lock));
	}

	public boolean setLock(Object lock, boolean canUnlock) {
		if (isChangeable()) {
			this.lock = new ObjectLock(lock, canUnlock);
			return true;
		}

		return false;
	}

	public void removeIfLockedBy(Object lock) {
		if (this.lock != null && lock != null && this.lock.lock != null && this.lock.lock.equals(lock)) {
			this.lock = null;

			currentEntry.update(0, "");
			currentEntry.hide();
		}

	}

	public Entry getCurrentEntry() {
		return this.currentEntry;
	}

	public class Entry {
		float percentFilled;
		String text;

		public Entry() {
			this.percentFilled = 100F;
		}

		public void update(float percentFilled, String text) {
			this.percentFilled = percentFilled;
			this.text = text;

			BarAPI.setMessage(p, text, percentFilled);
		}

		public void update(LivingEntity health, String text) {
			update((float) (health.getHealth() / health.getMaxHealth()) * 100, text);
		}

		public void update(String text) {
			this.update(percentFilled, text);

		}

		public void update(float percentFilled) {
			update(percentFilled, text);
		}

		public void update(LivingEntity health) {
			update(health, text);
		}

		public void hide() {
			update("");
			BarAPI.removeBar(p);
		}

	}

}
