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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class PlayerInteractListener extends NoxListener<NoxMMO> {
	
	MMOPlayerManager	pm;
	
	public PlayerInteractListener() {
		this(NoxMMO.getInstance());
		
	}
	
	public PlayerInteractListener(NoxMMO mmo) {
		super(mmo);
		
		pm = MMOPlayerManager.getInstance();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent e) {
		
		final Player p = e.getPlayer();
		final Location pLoc = p.getEyeLocation().add(
				p.getLocation().getDirection().normalize());
		
		// Bukkit.broadcastMessage("Commy is a noob");
		// debug===========================================
		if (p.getItemInHand().getType() != Material.STICK)
			return;
		
		// // Spawn
		// final WrapperPlayServerSpawnEntity packet = new
		// WrapperPlayServerSpawnEntity();
		// packet.setEntityID(Integer.MAX_VALUE - 100);
		// packet.setType(76);
		// packet.setObjectData(0);
		// packet.setX(pLoc.getX());
		// packet.setY(pLoc.getY());
		// packet.setZ(pLoc.getZ());
		//
		// // Meta
		// final WrapperPlayServerEntityMetadata metaPacket = new
		// WrapperPlayServerEntityMetadata();
		//
		// final WrappedDataWatcher dw = new WrappedDataWatcher();
		//
		// final ItemStack stack = new ItemStack(Material.FIREWORK, 64);
		// final FireworkMeta firework = (FireworkMeta)
		// stack.getItemMeta();
		// firework.addEffect(FireworkEffect.builder().
		// with(Type.BALL).
		// withColor(Color.ORANGE).
		// build()
		// );
		// firework.setPower(1);
		// stack.setItemMeta(firework);
		//
		// dw.setObject(0, (byte) 0);
		// dw.setObject(1, (short) 300);
		// dw.setObject(8, stack);
		//
		// metaPacket.setEntityId(Integer.MAX_VALUE - 100);
		// metaPacket.setEntityMetadata(dw.getWatchableObjects());
		//
		// // Kill
		// final WrapperPlayServerEntityDestroy destroyPacket = new
		// WrapperPlayServerEntityDestroy();
		// destroyPacket.setEntities(new int[] { Integer.MAX_VALUE - 100
		// });
		//
		// // Detonate
		// final WrapperPlayServerEntityStatus explode = new
		// WrapperPlayServerEntityStatus();
		// explode.setEntityId(Integer.MAX_VALUE - 100);
		// explode.setEntityStatus(17);
		//
		// new WisdomEntityAbility(p).execute();
		//
		// for (final Player bp : Bukkit.getOnlinePlayers()) {
		// packet.sendPacket(bp);
		// metaPacket.sendPacket(bp);
		// explode.sendPacket(bp);
		// destroyPacket.sendPacket(bp);
		// }
		
	}
}
