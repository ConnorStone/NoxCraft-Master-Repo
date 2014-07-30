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

package com.noxpvp.core.runners;

import java.util.ArrayList;
import java.util.List;

import com.noxpvp.core.data.Pair;
import com.noxpvp.core.utils.BlockStateUtils;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.java.JavaPlugin;

import com.bergerkiller.bukkit.common.Task;

//TODO: TestRun
public class BlockChangerRunnable extends Task {

	//First should be the one to check for if checking to match.
	//Second should be to change to.
	private List<Pair<BlockState>> states;
	private boolean checkCurrent = true;
//	private List<Pair<BlockState>> pending; //For the placement safety system.
	private long restoreInterval;
	private int restoreRate;
	private boolean isRandom = false;
	public BlockChangerRunnable(JavaPlugin plugin) {
		super(plugin);
	}

	public BlockChangerRunnable(JavaPlugin plugin, List<BlockState> states, List<BlockState> oldStates, boolean random, boolean checkCurrent, int restoreRate, long restoreInterval) {
		super(plugin);
		if (states.size() != oldStates.size())
			throw new IllegalArgumentException("States and old states do not match. You must specify equal number for each.");
		this.states = new ArrayList<Pair<BlockState>>();
		for (int i = 0; i < states.size(); i++)
			if (oldStates.get(i).getBlock().equals(states.get(i).getBlock()))
				this.states.add(new Pair<BlockState>(oldStates.get(i), states.get(i)));
		setRestoreRate(restoreRate);
		setRestoreInterval(restoreInterval);
	}

	public BlockChangerRunnable setUsingRandom(boolean random) {
		this.isRandom = random;
		return this;
	}

	public boolean isRandom() {
		return this.isRandom;
	}

	public long getRestoreInterval() {
		return this.restoreInterval;
	}

	public BlockChangerRunnable setRestoreInterval(long ticks) {
		this.restoreInterval = ticks;
		return this;
	}

	public int getRestoreRate() {
		return this.restoreRate;
	}

	public BlockChangerRunnable setRestoreRate(int amount) {
		this.restoreRate = amount;
		return this;
	}

	private boolean isSame(BlockState previousState) {
		return BlockStateUtils.blockStatesPerfectMatch(previousState, previousState.getBlock().getState());
	}

	private void clean() {
		if (!checkCurrent)
			return;

		List<Pair<BlockState>> pending = new ArrayList<Pair<BlockState>>();
		for (Pair<BlockState> pair : states)
			if (!isSame(pair.first))
				pending.add(pair);

		states.removeAll(pending);
		pending = null;
	}

	public void run() {
		clean();

		if (getRestoreRate() >= states.size())
			for (Pair<BlockState> pair : states)
				pair.second.update(true, false); //TODO: Add safecode for fixing stuff that can break the moment it is spawned in air. Such as redstone!
		else {
			if (!isRandom())
				for (int i = 0; i < getRestoreRate(); i++)
					states.remove(0).second.update(true, false);
			else {
				int i = 0;
				while (i < getRestoreRate()) {
					int next = RandomUtils.nextInt(states.size() - 1);
					states.remove(next).second.update(true, false);

					i++;
				}
			}
		}

		if (states.size() > 0)
			start(restoreInterval);
	}
}
