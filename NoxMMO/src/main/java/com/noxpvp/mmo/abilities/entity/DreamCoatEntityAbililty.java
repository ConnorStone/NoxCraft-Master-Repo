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

package com.noxpvp.mmo.abilities.entity;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutEntityEquipment;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;

public class DreamCoatEntityAbililty extends BaseEntityAbility {

	public static final String ABILITY_NAME = "Dream Coat";
	public static final String PERM_NODE = "dream-coat";
	private static WeakHashMap<LivingEntity, DreamCoatRunnable> runnables = new WeakHashMap<LivingEntity, DreamCoatEntityAbililty.DreamCoatRunnable>();
	private static SortedMap<Long, Color> colours = new TreeMap<Long, Color>(Collections.reverseOrder());
	private boolean anyArmor;

	public DreamCoatEntityAbililty(Entity e) {
		super(ABILITY_NAME, e);

		setupColors();
		this.setAnyArmor(false);
	}

	@Override
	public String getDescription() {
		return "The coat of much technicolor";
	}

	private void setupColors() {
		int[] values = new int[]{0, 32, 64, 96, 128, 160, 192, 224, 255};
		for (int r : values) {
			for (int g : values) {
				for (int b : values) {
					long brightness = (r + g + b) / 3;
					long colourScore = (brightness << 24) + (r << 8) + (g << 16) + b;
					colours.put(colourScore, Color.fromRGB(r, g, b));
				}
			}
		}

	}

	public boolean isAnyArmor() {
		return anyArmor;
	}

	public DreamCoatEntityAbililty setAnyArmor(boolean anyArmor) {
		this.anyArmor = anyArmor;
		return this;
	}

	public AbilityResult<DreamCoatEntityAbililty> execute() {
		if (!(getEntity() instanceof LivingEntity))
			return new AbilityResult<DreamCoatEntityAbililty>(this, false);

		LivingEntity a = (LivingEntity) getEntity();

		if (runnables.containsKey(a)) {
			while (runnables.containsKey(a)) {
				runnables.get(a).safeCancel();
				runnables.remove(a);
			}
			sendFakeArmor(a, null);

			return new AbilityResult<DreamCoatEntityAbililty>(this, false, "&e" + getName() + " &6has been deactivated");
		}

		System.out.println("starting");
		DreamCoatRunnable b = new DreamCoatRunnable(a);
		runnables.put(a, b);

		b.runTaskTimer(NoxMMO.getInstance(), 0, 1);
		return new AbilityResult<DreamCoatEntityAbililty>(this, true);
	}

	public void sendFakeArmor(LivingEntity e, @Nullable Color color) {

		CommonPacket packet;
		NMSPacketPlayOutEntityEquipment nms = new NMSPacketPlayOutEntityEquipment();

		int slot;
		for (ItemStack i : e.getEquipment().getArmorContents()) {
			switch (i.getType()) {
				case LEATHER_BOOTS:
					slot = 1;
					break;
				case LEATHER_LEGGINGS:
					slot = 2;
					break;
				case LEATHER_CHESTPLATE:
					slot = 3;
					break;
				case LEATHER_HELMET:
					slot = 4;
					break;
				default:
					continue;
			}

			packet = new CommonPacket(PacketType.OUT_ENTITY_EQUIPMENT);

			ItemStack item = i.clone();
			LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

			if (color != null) {
				meta.setColor(color);
				item.setItemMeta(meta);
			}

			packet.write(nms.entityId, e.getEntityId());
			packet.write(nms.item, item);
			packet.write(nms.slot, slot);

			for (Entity it : e.getNearbyEntities(75, 100, 75))
				if (it instanceof Player)
					PacketUtil.sendPacket((Player) it, packet, false);

		}
	}

	private class DreamCoatRunnable extends BukkitRunnable {

		private LivingEntity e;
		private Cycler<Color> colors = new Cycler<Color>(colours.values());

		public DreamCoatRunnable(LivingEntity e) {
			this.e = e;
		}

		public void safeCancel() {
			try {
				this.cancel();
				return;
			} catch (IllegalStateException e) {
			}
		}

		public void run() {

			if (e == null || !e.isValid() || e.isDead())
				safeCancel();

			sendFakeArmor(e, colors.next());

		}

	}

}
