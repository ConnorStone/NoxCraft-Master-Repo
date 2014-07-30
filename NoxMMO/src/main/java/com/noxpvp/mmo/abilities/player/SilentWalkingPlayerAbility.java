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

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class SilentWalkingPlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Silent Walking";
	public static final String PERM_NODE = "silent-walk";

	private CommonPacket packet;

	public SilentWalkingPlayerAbility(OfflinePlayer p, CommonPacket packet) {
		super(ABILITY_NAME, p);

		this.packet = packet;
	}

	public AbilityResult<SilentWalkingPlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<SilentWalkingPlayerAbility>(this, false);

		if (!packet.read(PacketType.OUT_NAMED_SOUND_EFFECT.soundName).contains("step."))
			return new AbilityResult<SilentWalkingPlayerAbility>(this, false);

		Player hearing = getPlayer();
		Location loc = new Location(hearing.getWorld(),
				packet.read(PacketType.OUT_NAMED_SOUND_EFFECT.x) / 8,
				packet.read(PacketType.OUT_NAMED_SOUND_EFFECT.y) / 8,
				packet.read(PacketType.OUT_NAMED_SOUND_EFFECT.z) / 8);

		double lowestDistance = 100;
		Player closest = null;

		for (Player p : PlayerUtil.getNearbyPlayers(hearing, 100)) {
			if (p.equals(hearing)) continue;

			double d = p.getLocation().distance(loc);
			if (d < lowestDistance) {
				lowestDistance = d;
				closest = p;
			}
		}

		if (closest == null || lowestDistance > 1) {
			return new AbilityResult<SilentWalkingPlayerAbility>(this, false);
		}

		return new AbilityResult<SilentWalkingPlayerAbility>(this, VaultAdapter.permission.has(closest, NoxMMO.PERM_NODE + ".ability." + SilentWalkingPlayerAbility.PERM_NODE));

	}

}
