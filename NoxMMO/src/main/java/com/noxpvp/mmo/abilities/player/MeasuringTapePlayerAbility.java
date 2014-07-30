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

import com.noxpvp.core.packet.NoxPacketUtil;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.UnregisterMMOHandlerRunnable;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class MeasuringTapePlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Measuring Tape";
	public static final String PERM_NODE = "measuring-tape";

	private BaseMMOEventHandler<PlayerInteractEvent> handler;

	private Block[] blocks;
	private Block b;
	private boolean firstDone;

	public MeasuringTapePlayerAbility(final OfflinePlayer player) {
		super(ABILITY_NAME, player);

		this.handler = new BaseMMOEventHandler<PlayerInteractEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("PlayerInteractEvent").toString(),
				EventPriority.MONITOR, 1) {

			public boolean ignoreCancelled() {
				return false;
			}

			public void execute(PlayerInteractEvent event) {
				if (!mayExecute()) {
					unregisterHandler(this);
					return;
				}
				if (!event.getPlayer().equals(player.getPlayer()))
					return;


				if (!firstDone) {
					b = LineOfSightUtil.getTargetBlock(event.getPlayer(), 10, (Set<Material>) null);
					if (b == null)
						return;

					blocks[0] = b;
					NoxPacketUtil.fakeBlock(10, Material.WOOL, b.getLocation());

					firstDone = true;
					return;
				} else {
					b = LineOfSightUtil.getTargetBlock(event.getPlayer(), 10, (Set<Material>) null);
					if (b == null)
						return;

					blocks[1] = b;
					NoxPacketUtil.fakeBlock(10, Material.WOOL, b.getLocation());

					unregisterHandler(this);

					return;
				}

			}

			public Class<PlayerInteractEvent> getEventType() {
				return PlayerInteractEvent.class;
			}

			public String getEventName() {
				return "PlayerInteractEvent";
			}
		};

		this.blocks = new Block[2];
	}

	public AbilityResult<MeasuringTapePlayerAbility> execute() {
		if (!mayExecute())
			new AbilityResult<MeasuringTapePlayerAbility>(this, false);

		registerHandler(handler);
		new UnregisterMMOHandlerRunnable(handler).runTaskLater(NoxMMO.getInstance(), 20 * 120);
		return new AbilityResult<MeasuringTapePlayerAbility>(this, true);

	}

}
