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

package com.noxpvp.core.effect;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;


public class StaticEffects {

	private static final NoxCore plugin = NoxCore.getInstance();
	private static List<LivingEntity> frozenEntitys = new ArrayList<LivingEntity>();

	public static void SmokeScreen(Location loc, int size) {
		for (double x = (loc.getX() - size); x < (loc.getX() + size); x++)
			for (double z = (loc.getZ() - size); z < (loc.getZ() + size); z++)
				for (double y = (loc.getY() - size); y < (loc.getY() + size); y++)
					new ParticleRunner(ParticleType.cloud, new Location(loc.getWorld(), x, y, z), true, 0, 2, 1).start(0);
	}
	
	public static void DamageAmountParticle(LivingEntity e, double damage) {
		DamageAmountParticle(e.getEyeLocation(), damage);
	}

	public static void DamageAmountParticle(Location loc, double damage) {
		if (plugin.isHoloAPIActive()) {
			final Hologram damageParticle = HoloAPI.getManager()
					.createSimpleHologram(loc, 3, true, ChatColor.RED + "-" + String.format("%.1f", damage));

			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

				public void run() {
					HoloAPI.getManager().stopTracking(damageParticle);
				}
			}, 20 * 3);
		}

	}

	public static void HealAmountParticle(LivingEntity e, double heal) {
		HealAmountParticle(e.getEyeLocation(), heal);
	}

	public static void HealAmountParticle(Location loc, double heal) {
		if (plugin.isHoloAPIActive()) {
			final Hologram healParticle = HoloAPI.getManager()
					.createSimpleHologram(loc, 3, true, ChatColor.GREEN + "+" + String.format("%.1f", heal));

			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

				public void run() {
					HoloAPI.getManager().stopTracking(healParticle);
				}
			}, 20 * 3);
		}

	}

	public static void SkullBreak(LivingEntity e) {
		SkullBreak(e.getEyeLocation(), plugin);
	}

	public static void SkullBreak(Location loc, NoxPlugin plugin) {
		new ParticleRunner("blockcrack_155_0", loc, false, .1F, 20, 2).start(0);
	}

	public static void BloodEffect(Entity e) {
		BloodEffect(e, plugin);
	}

	public static void BloodEffect(Entity e, NoxPlugin plugin) {
		new ParticleRunner("blockdust_35_14", e.getLocation(), false, .12F, 15, 1).runTaskTimer(plugin, 0, 0);
	}

	public static void HeartEffect(Entity e, double healAmount) {
		HeartEffect(e, healAmount, plugin);
	}

	public static void HeartEffect(Entity e, double healAmount, NoxPlugin plugin) {
		new ParticleRunner(ParticleType.heart, e, false, 0, 1, (int) Math.ceil(healAmount)).start(0, 5);
	}

	public static void BroadcastSound(Entity e, Sound sound) {
		BroadcastSound(e.getLocation(), sound);
	}

	public static void BroadcastSound(Location loc, Sound sound) {
		loc.getWorld().playSound(loc, sound, 1, 0);
	}

	public static void PlaySound(Player p, Sound sound) {
		p.playSound(p.getLocation(), sound, 1, 0);
	}

	public static void playSound(Player p, String sound) {
		p.playSound(p.getLocation(), sound, 1, 1);
	}

	public static void playSound(Player p, String sound, float volume, float pitch) {
		p.playSound(p.getLocation(), sound, volume, pitch);
	}

	public static void PlaySound(Player p, Location loc, String sound) {
		PlaySound(p, loc, sound, 1, 1);
	}

	public static void PlaySound(Player p, Location loc, String sound, float volume, float pitch) {
		p.playSound(loc, sound, volume, pitch);
		
		/*CommonPacket packet = new CommonPacket(PacketType.OUT_NAMED_SOUND_EFFECT);
		
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.soundName, sound);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.volume, volume);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.pitch, 0);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.x, (int) (loc.getX() + 0.5D) * 8);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.y, (int) (loc.getY() + 0.5D) * 8);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.z, (int) (loc.getZ() + 0.5D) * 8);
		
		PacketUtil.sendPacket(p, packet, false);*/
	}

	/**
	 * Freezes the entity (jump and movement) for the set ticks.
	 * If the entity has already been and is still frozen, will return false
	 *
	 * @param e
	 * @param ticks
	 * @return Boolean if freezing was successful
	 */
	public static boolean Freeze(final LivingEntity e, int ticks) {
		if (frozenEntitys.contains(e))
			return false;

		final List<PotionEffect> preserve = new ArrayList<PotionEffect>();
		for (PotionEffect effect : e.getActivePotionEffects()) {
			preserve.add(new PotionEffect(
					effect.getType(),
					effect.getDuration() - ticks,
					effect.getAmplifier(),
					effect.isAmbient()));
		}

		PotionEffect walking = new PotionEffect(PotionEffectType.SPEED, ticks, -50, false),
				jumping = new PotionEffect(PotionEffectType.JUMP, ticks, -50, false);

		e.addPotionEffect(walking, true);
		e.addPotionEffect(jumping, true);


		final NoxListener<NoxPlugin> freezeRemovalPrevention = new NoxListener<NoxPlugin>(plugin) {

			@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
			public void onPotionDrink(PlayerItemConsumeEvent event) {
				if (!e.equals(event.getPlayer()) || event.getItem().getType() != Material.POTION)
					return;

				if (!frozenEntitys.contains(e)) {
					this.unregister();
					return;
				}

				event.setCancelled(true);
			}

			@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
			public void onPotionSplash(PotionSplashEvent event) {
				if (!event.getAffectedEntities().contains(e))
					return;

				if (!frozenEntitys.contains(e)) {
					this.unregister();
					return;
				}
				try {
					event.getAffectedEntities().remove(e);
					event.setCancelled(true);
				} catch (UnsupportedOperationException e) {
				}

			}
		};

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

			public void run() {
				if (e == null || !e.isValid()) {
					return;
				}

				for (PotionEffect effect : preserve)
					if (effect.getDuration() > 0)
						e.addPotionEffect(effect, true);

				if (StaticEffects.frozenEntitys.contains(e))
					StaticEffects.frozenEntitys.remove(e);

				freezeRemovalPrevention.unregister();
			}
		}, ticks);


		freezeRemovalPrevention.register();

		return frozenEntitys.add(e);
	}

}
