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

package com.noxpvp.mmo.renderers;

import java.util.ArrayList;
import java.util.List;

import com.noxpvp.mmo.abilities.internal.TieredAbility;
import org.bukkit.ChatColor;

import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.gui.rendering.ICoreBoardRenderer;
import com.noxpvp.mmo.AbilityCycler;

public class CoreBoardACRenderer extends BaseAbilityCyclerRenderer implements ICoreBoardRenderer {
	private CoreBoard board;
	private List<String> entries;
	private String currentEntry, nextEntry, prevEntry;

	public CoreBoardACRenderer(AbilityCycler cycler) {
		this(cycler, new CoreBoard("Abilities", cycler.getPlayer()));
		entries = new ArrayList<String>();

		for (TieredAbility ability : cycler.getList())
			entries.add(ability.getName());
	}

	public CoreBoardACRenderer(AbilityCycler cycler, CoreBoard board) {
		super(cycler);
		this.board = board;
	}

	//Warning: This is slightly intensive if constantly being updated.
	private void update() {
		board.clear();
		for (String entry : entries)
			board.addEntry(entry,  entry, (entry.equals(currentEntry)? ChatColor.YELLOW: entry.equals(nextEntry) || entry.equals(prevEntry)? ChatColor.AQUA: null));
	}

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekNext()} method.
	 * @deprecated Please use {@link #renderCurrent()}
	 */
	@Override
	@Deprecated
	public void renderNext() {
		renderCurrent();
	}

	/**
	 * Renders a view provided by the {@link com.noxpvp.mmo.AbilityCycler#peekPrevious()} method.
	 * @deprecated Please use {@link #renderCurrent()}
	 */
	@Override
	@Deprecated
	public void renderPrevious() {
		renderCurrent();
	}

	/**
	 * Renders a view provided by the current through {@link com.noxpvp.mmo.AbilityCycler#current()}.
	 */
	@Override
	public void renderCurrent() {
		update();
	}

	public void startRender() {
		board.show();
	}

	public void stopRender() {
		board.hide();
	}
}
