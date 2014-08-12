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

import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static com.noxpvp.mmo.abilities.BaseTargetedAbility.TargetedAbilityResult;

/**
 * @author NoxPVP
 */
public class ShadowStepPlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "shadow-step";
	public static final String ABILITY_NAME = "Shadow Step";

	/**
	 * @param player the Player type user for this ability instance
	 */
	public ShadowStepPlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());

	}

	public TargetedAbilityResult<ShadowStepPlayerAbility> execute() {
		if (!mayExecute())
			return new TargetedAbilityResult<ShadowStepPlayerAbility>(this, false);

		Location targetLoc = getTarget().getLocation();
		Vector facing = targetLoc.getDirection().setY(0).multiply(-1);
		Location loc = targetLoc.toVector().add(facing).toLocation(targetLoc.getWorld());
		loc.setPitch(0);
		loc.setYaw(targetLoc.getYaw());


		Block b = loc.getBlock();
		if (!(!b.getType().isSolid() || b.getRelative(BlockFace.UP).getType().isSolid())) {
			return new TargetedAbilityResult<ShadowStepPlayerAbility>(this, false);
		}

		getPlayer().teleport(loc);

		return new TargetedAbilityResult<ShadowStepPlayerAbility>(this, true);
	}

}
