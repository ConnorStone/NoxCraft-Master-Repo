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

package com.noxpvp.core.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.manager.CorePlayerManager;

public abstract class CoreBox extends NoxListener<NoxPlugin> implements
		ICoreBox, MenuItemRepresentable, Cloneable {
	
	private final UUID					playerID;
	private final String				name;
	private Inventory					box;
	private Map<Integer, CoreBoxItem>	menuItems;
	private CoreBox						backButton;
	public Runnable						closeRunnable;
	private final CorePlayerManager		pm;
	private final AttributeHider		attributeHider;
	
	private ItemStack					identifiableItem;
	
	public CoreBox(Player p, String name, int size) {
		this(p, name, size, null, NoxCore.getInstance());
	}
	
	public CoreBox(Player p, String name, int size,
			@Nullable CoreBox backButton) {
		this(p, name, size, backButton, NoxCore.getInstance());
	}
	
	public CoreBox(Player p, String name, int size,
			@Nullable CoreBox backbutton, NoxPlugin plugin) {
		this(p, name, InventoryType.CHEST, size, backbutton, plugin);
	}
	
	public CoreBox(Player p, String name, InventoryType type) {
		this(p, name, type, 0, null, NoxCore.getInstance());
	}
	
	public CoreBox(Player p, String name, InventoryType type,
			@Nullable CoreBox backButton) {
		this(p, name, type, 0, backButton, NoxCore.getInstance());
	}
	
	public CoreBox(final Player p, String name, InventoryType type,
			int size, @Nullable CoreBox backButton, NoxPlugin plugin) {
		super(plugin);
		
		pm = CorePlayerManager.getInstance();
		playerID = p.getUniqueId();
		
		this.name = ChatColor.GOLD + name;
		box = size == 0 ?
				Bukkit.getServer().createInventory(null, type, this.name) :
				Bukkit.getServer().createInventory(null, size, this.name);
		
		menuItems = new HashMap<Integer, CoreBoxItem>();
		
		if (backButton != null) {
			this.backButton = backButton;
			
			final ItemStack button = new ItemStack(Material.ARROW);
			final ItemMeta meta = button.getItemMeta();
			
			meta.setDisplayName(this.name);
			meta.setLore(Arrays.asList(ChatColor.AQUA
					+ "<- Go Back To The \"" + ChatColor.GOLD
					+ backButton.getName().replaceAll("(?i)menu", "")
					+ ChatColor.AQUA + "\" Menu"));
			
			button.setItemMeta(meta);
			
			box.setItem(box.getSize() - 1, button);
		}
		
		closeRunnable = new Runnable() {
			
			final CoreBox	thisBox	= CoreBox.this;
			
			public void run() {
				Player p;
				if ((p = getPlayer()) != null
						&& box.getViewers().contains(p)) {
					p.closeInventory();
				}
				
				final NoxPlayer gp = pm.getPlayer(p);
				if (gp.hasCoreBox(thisBox)) {
					gp.deleteCoreBox();
				}
				
				attributeHider.unregister();
				thisBox.unregister();
				box.clear();
				menuItems = null;
			}
		};
		
		attributeHider = new AttributeHider();
		
	}
	
	public boolean addMenuItem(int slot, CoreBoxItem item) {
		box.setItem(slot, item.getItem());
		menuItems.put(slot, item);
		
		ItemStack checkNull;
		return (checkNull = box.getItem(slot)) != null
				&& checkNull.equals(item.getItem());
	}
	
	public void clickHandler(InventoryClickEvent event) {
		
	}
	
	public void closeHandler(InventoryCloseEvent event) {
		
	}
	
	public CoreBox getBackButton() {
		return backButton;
	}
	
	public Inventory getBox() {
		return box;
	}
	
	public ItemStack getIdentifiableItem() {
		if (identifiableItem == null) {
			identifiableItem = new ItemStack(Material.CHEST);
			final ItemMeta meta = identifiableItem.getItemMeta();
			
			meta.setDisplayName(new MessageBuilder().gold("Menu: ")
					.yellow(ChatColor.stripColor(name)).toString());
			
			identifiableItem.setItemMeta(meta);
		}
		
		return identifiableItem.clone();
	}
	
	public CoreBoxItem getMenuItem(CoreBoxItem item) {
		for (final CoreBoxItem menuItem : menuItems.values()) {
			if (menuItem.equals(item))
				return menuItem;
			else {
				continue;
			}
		}
		
		return null;
	}
	
	public CoreBoxItem getMenuItem(int slot) {
		try {
			return menuItems.get(slot);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(playerID);
	}
	
	public void hide() {
		if (isValid()) {
			CommonUtil.nextTick(closeRunnable);
		}
		
		return;
	}
	
	public boolean isValid() {
		final Player p = getPlayer();
		return p != null && p.isValid()
				&& pm.getPlayer(p).hasCoreBox(this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onClick(InventoryClickEvent event) {
		if (!isValid()) {
			hide();
			
			return;
		}
		
		if (!event.getInventory().equals(box))
			return;
		
		final Player player = getPlayer();
		if (player == null)
			return;
		
		event.setCancelled(true);
		updatePlayerInvetory();
		
		final ItemStack clickedItem = event.getCurrentItem();
		if (event.getRawSlot() < box.getSize() - 1) {
			if (clickedItem != null
					&& clickedItem.getType() != Material.AIR) {
				
				CoreBoxItem item;
				if ((item = getMenuItem(event.getRawSlot())) != null)
					if (item.onClick(event)) {
						player.playSound(player.getLocation(),
								Sound.CLICK, 1, 0);
					} else {
						player.playSound(player.getLocation(),
								Sound.NOTE_BASS, 2, -2);
					}
			}
		} else if (backButton != null
				&& event.getRawSlot() == box.getSize() - 1) {
			try {
				((CoreBox) backButton.clone()).show();
				player.playSound(player.getLocation(), Sound.CLICK, 1, 0);
			} catch (final CloneNotSupportedException e) {
			}
			
			return;
		}
		
		clickHandler(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onClose(InventoryCloseEvent event) {
		if (!isValid()) {
			hide();
			
			return;
		}
		
		if (event.getInventory().equals(box)) {
			closeHandler(event);
		}
	}
	
	public boolean removeMenuItem(CoreBoxItem item) {
		return box.removeItem(item.getItem()) == null
				&& menuItems.values().remove(item);
	}
	
	public void removeMenuItem(int slot) {
		box.setItem(slot, null);
		
		return;
	}
	
	public void setBox(Inventory newBox) {
		box = newBox;
	}
	
	public void show() {
		Player p;
		if ((p = getPlayer()) == null)
			return;
		
		attributeHider.register();
		register();
		
		pm.getPlayer(p).setCoreBox(this);
		p.openInventory(box);
		
	}
	
	protected void updatePlayerInvetory() {
		CommonUtil.nextTick(new Runnable() {
			
			public void run() {
				getPlayer().updateInventory();
			}
		});
	}
	
}
