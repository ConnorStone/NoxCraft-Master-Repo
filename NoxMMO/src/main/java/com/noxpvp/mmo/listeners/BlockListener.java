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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.player.AutoToolPlayerAbilities.AutoTool;

public class BlockListener extends NoxListener<NoxMMO> {

	public BlockListener(NoxMMO mmo) {
		super(mmo);
	}

	public BlockListener() {
		this(NoxMMO.getInstance());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e) {
		if (VaultAdapter.permission.has(e.getPlayer(), NoxMMO.PERM_NODE + ".ability." + AutoTool.PERM_NODE)) {
			new AutoTool(e.getPlayer()).execute(e);
		}
	}


//	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
//	public void onPlace(BlockPlaceEvent e) {
//		
//	}

}
