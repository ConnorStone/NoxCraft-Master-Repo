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

import org.bukkit.OfflinePlayer;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.VanishPlayerRunnable;

public class VanishPlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Cloak";
	public static final String PERM_NODE = "cloak";

	private double range;
	private int time;
	private int hideFreq;

	/**
	 * @param player The user of the ability instance
	 */
	public VanishPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);

		this.range = 75;
		this.hideFreq = 20;
		this.time = 60;
	}

	/**
	 * @return double The currently set amount of distance to check for players to hide
	 */
	public double getRange() {
		return range;
	}

	/**
	 * @param range The distance to check for players to hide
	 * @return VanishAbility This instance, used for chaining
	 */
	public VanishPlayerAbility setRange(double range) {
		this.range = range;
		return this;
	}

	/**
	 * @return Integer The currently set amount of seconds to keep the player vanished
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time The amount of seconds to keep the player vanished
	 * @return VanishAbility This instance, used for chaining
	 */
	public VanishPlayerAbility setTime(int time) {
		this.time = time;
		return this;
	}

	/**
	 * @return Integer The currently set amount of delay between checks for players to hidef
	 */
	public int getHideFreq() {
		return hideFreq;
	}

	/**
	 * @param hideFreq The amount of time to wait between checks for players to hide
	 */
	public VanishPlayerAbility setHideFreq(int hideFreq) {
		this.hideFreq = hideFreq;
		return this;
	}

	/**
	 * @return boolean If this ability executed successfully
	 */
	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		new VanishPlayerRunnable(getPlayer(), range, (time * (20 / hideFreq))).
				runTaskTimer(NoxMMO.getInstance(), 0, hideFreq);

		return new AbilityResult(this, true);
	}

}
