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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.listeners.NoxPLPacketListener;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.HealRunnable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedPackPlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Med Pack";
	public static final String PERM_NODE = "med-pack";

	private static List<MedPackDataWrapper> packs = new ArrayList<MedPackPlayerAbility.MedPackDataWrapper>();
	private BaseMMOEventHandler<PlayerPickupItemEvent> handler;
	private BaseMMOEventHandler<InventoryPickupItemEvent> inventoryPickupHandler;
	private NoxPLPacketListener camoflouge;
	private int health;
	private boolean isActive;
	public MedPackPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);

		this.handler = new BaseMMOEventHandler<PlayerPickupItemEvent>(
				new StringBuilder().append(ABILITY_NAME).append(player.getName()).append("PlayerPickupItemEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public Class<PlayerPickupItemEvent> getEventType() {
				return PlayerPickupItemEvent.class;
			}

			public String getEventName() {
				return "PlayerPickupItemEvent";
			}

			public void execute(PlayerPickupItemEvent event) {
				Item eventItem = event.getItem();
				if (!hasPack(eventItem)) {
					return;
				}

				event.setCancelled(true);

				Player abilPlayer = getPlayer();
				Player pickupPlayer = event.getPlayer();
				if (event.getPlayer() == abilPlayer || pickupPlayer.getHealth() == pickupPlayer.getMaxHealth())
					return;

				eventItem.remove();

				double health = getPack(eventItem).health;

				new HealRunnable(pickupPlayer, 1, (int) health).runTaskTimer(NoxMMO.getInstance(), 5, 5);

				MessageUtil.sendLocale(NoxMMO.getInstance(), pickupPlayer, "ability.medpack.pick-up", pickupPlayer.getName(), (abilPlayer != null ? abilPlayer.getName() : MedPackPlayerAbility.this.getMMOPlayer().getPlayerName()));
				if (abilPlayer != null && abilPlayer.isOnline())
					MessageUtil.sendLocale(NoxMMO.getInstance(), abilPlayer, "ability.medpack.pick-up.other", pickupPlayer.getName(), MedPackPlayerAbility.this.getMMOPlayer().getPlayerName());

				packs.remove(getPack(eventItem));

				if (packs.isEmpty())
					setActive(false);

				return;
			}
		};

		this.inventoryPickupHandler = new BaseMMOEventHandler<InventoryPickupItemEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("InventoryPickupItemEvent").toString(),
				EventPriority.HIGH, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public Class<InventoryPickupItemEvent> getEventType() {
				return InventoryPickupItemEvent.class;
			}

			public String getEventName() {
				return "InventoryPickupItemEvent";
			}

			public void execute(InventoryPickupItemEvent event) {
				if (packs.contains(event.getItem()))
					event.setCancelled(true);

				if (packs.isEmpty())
					setActive(false);

				return;
			}
		};

		this.camoflouge = new NoxPLPacketListener(NoxMMO.getInstance(), PacketType.Play.Server.ENTITY_METADATA) {

			@Override
			public void onPacketSending(PacketEvent event) {

				PacketContainer packet = event.getPacket();

				try {
					if (packet.getEntityModifier(event).read(0) instanceof Item) {

						int id = packet.getEntityModifier(event).read(0).getEntityId();

						WrappedDataWatcher watcher = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
						ItemStack stack = watcher.getItemStack(10);

						if (stack != null && hasPack(id)) {

							stack = new ItemStack(stack);
							ItemMeta meta = stack.getItemMeta();

							stack.setType(Material.EMERALD);
							meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);

							stack.setItemMeta(meta);

							watcher = watcher.deepClone();
							watcher.setObject(10, stack);

							packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (packs.isEmpty())
					setActive(false);

				return;
			}

			@Override
			public NoxPlugin getPlugin() {
				return NoxMMO.getInstance();
			}
		};

		this.health = 8;
		this.isActive = false;
	}

	private static boolean hasPack(Item pack) {
		return hasPack(pack.getEntityId());
	}

	private static boolean hasPack(int entityId) {
		for (MedPackDataWrapper wrap : packs)
			if (wrap.pack.getEntityId() == entityId) {
				return true;
			} else {
				continue;
			}

		return false;
	}

	private static MedPackDataWrapper getPack(Item pack) {
		for (MedPackDataWrapper wrap : packs)
			if (wrap.pack.equals(pack))
				return wrap;

		return null;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public MedPackPlayerAbility setActive(boolean active) {
		boolean changed = this.isActive != active;
		this.isActive = active;

		if (changed)
			if (active) {
				registerHandler(handler);
				registerHandler(inventoryPickupHandler);
				camoflouge.register();
			} else {
				unregisterHandler(handler);
				unregisterHandler(inventoryPickupHandler);
				camoflouge.unRegister();
			}

		return this;
	}

	public int getHealth() {
		return health;
	}

	public MedPackPlayerAbility setHealth(int health) {
		this.health = health;
		return this;
	}

	private ItemStack craftNewPackStack() {
		ItemStack medPack = new ItemStack(Material.COAL, 1);
		ItemMeta meta = medPack.getItemMeta();

		meta.setDisplayName(ChatColor.RED + "Report me to bbc please!");
		meta.setLore(Arrays.asList(getPlayer().getName() + ABILITY_NAME + System.currentTimeMillis()));

		medPack.setItemMeta(meta);

		return medPack;
	}

	public AbilityResult<MedPackPlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<MedPackPlayerAbility>(this, false);

		Player p = getPlayer();
		ItemStack medPack = craftNewPackStack();

		Item pack = p.getWorld().dropItem(p.getLocation(), medPack);
		pack.setVelocity(p.getLocation().getDirection());
		new ParticleRunner(ParticleType.happyVillager, pack, true, 1F, 6, 0).start(0, 10);

		if (!packs.add(new MedPackDataWrapper(pack, p, health))) {
			pack.remove();
			return new AbilityResult<MedPackPlayerAbility>(this, false);
		}

		setActive(true);
		return new AbilityResult<MedPackPlayerAbility>(this, true);
	}

	private class MedPackDataWrapper {
		public Item pack;
		public Player user;
		public double health;

		public MedPackDataWrapper(Item pack, Player user, double healAmount) {
			this.pack = pack;
			this.user = user;
			this.health = healAmount;
		}

	}

}
