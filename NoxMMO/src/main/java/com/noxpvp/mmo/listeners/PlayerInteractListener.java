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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.bergerkiller.bukkit.common.controller.EntityController;
import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.wrappers.DamageSource;
import com.noxpvp.core.gui.PlayerPermManager;
import com.noxpvp.core.gui.QuestionBox;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.entity.FireNovaEntityAbility;
import com.noxpvp.mmo.abilities.entity.WisdomEntityAbility;
import com.noxpvp.mmo.abilities.player.ChargePlayerAbility;
import com.noxpvp.mmo.abilities.player.FireBallPlayerAbility;
import com.noxpvp.mmo.abilities.player.FireSpinPlayerAbility;
import com.noxpvp.mmo.abilities.player.GuardianAngelPlayerAbility;
import com.noxpvp.mmo.abilities.player.LeapPlayerAbility;
import com.noxpvp.mmo.abilities.player.RejuvenationPlayerAbility;
import com.noxpvp.mmo.abilities.player.TornadoPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.HookShotPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.MassDestructionPlayerAbility;
import com.noxpvp.mmo.abilities.ranged.ThrowPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.BoltPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.DrainLifePlayerAbility;
import com.noxpvp.mmo.abilities.targeted.MortalWoundPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.SoothePlayerAbility;
import com.noxpvp.mmo.abilities.targeted.TargetPlayerAbility;
import com.noxpvp.mmo.gui.HealthBar;

@SuppressWarnings("unused")
public class PlayerInteractListener extends NoxListener<NoxMMO> {

	MMOPlayerManager pm;

	public PlayerInteractListener(NoxMMO mmo) {
		super(mmo);

		this.pm = MMOPlayerManager.getInstance();
	}

	public PlayerInteractListener() {
		this(NoxMMO.getInstance());
	}

	private static Map<String, BasePlayerAbility> abs = new HashMap<String, BasePlayerAbility>();
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent e) {

		final Player p = e.getPlayer();
//		MMOPlayer player = pm.getPlayer((p = e.getPlayer()));
//		if (player == null) return;

//		new TargetPlayerAbility(p).execute(e);//TODO make default range configized || passiveness

		//debug===========================================
		if (p.getItemInHand().getType() != Material.STICK)
			return;
		
	}

}
