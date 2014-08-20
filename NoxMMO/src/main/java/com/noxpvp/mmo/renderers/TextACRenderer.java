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

import org.bukkit.entity.Player;

import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.AbilityCycler;

public class TextACRenderer extends BaseAbilityCyclerRenderer {
	
	public TextACRenderer(AbilityCycler cycler) {
		super(cycler);
	}
	
	@Override
	public void renderCurrent() {
		if (getCycler() != null) {
			final Player p = getCycler().getPlayer();
			if (p != null) {
				MessageUtil.sendMessage(p, MessageUtil.parseColor(getCycler()
						.getCurrentAB().getName()));
			}
		}
	}
	
	@Override
	public void renderNext() {
		if (getCycler() != null && getCycler().getPlayer() != null) {
			MessageUtil.sendMessage(getCycler().getPlayer(), MessageUtil
					.parseColor(getCycler().peekNextAB().getName()));
		}
	}
	
	@Override
	public void renderPrevious() {
		if (getCycler() != null && getCycler().getPlayer() != null) {
			MessageUtil.sendMessage(getCycler().getPlayer(), MessageUtil
					.parseColor(getCycler().peekPreviousAB().getName()));
		}
		
	}
}
