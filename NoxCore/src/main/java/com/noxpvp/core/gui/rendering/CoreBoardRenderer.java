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

package com.noxpvp.core.gui.rendering;

import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.runners.RenderRunner;

public abstract class CoreBoardRenderer extends RenderRunner implements ICoreBoardRenderer {

	private CoreBoard board;

	public CoreBoardRenderer(CoreBoard board) {
		this.board = board;
	}

	public final void render() {
		updateRender();
		board.updateScores();
	}

	protected abstract void updateRender();

	public final void startRender() {
		board.show();
	}

	public final void stopRender() {
		board.hide();
	}



}
