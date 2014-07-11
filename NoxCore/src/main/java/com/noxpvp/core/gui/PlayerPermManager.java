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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.OldNoxPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.Vector;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.manager.CorePlayerManager;

public class PlayerPermManager extends CoreBox {
	
	static {
		setupItems();
	}
	
	private static void setupItems() {
		addGroup = new ItemStack(Material.BOOK_AND_QUILL);
		removeGroup = new ItemStack(Material.BOOK_AND_QUILL);
		addPerm = new ItemStack(Material.BOOK_AND_QUILL);
		removePerm = new ItemStack(Material.BOOK_AND_QUILL);
		
		ItemMeta meta = addGroup.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "Add Group");
		addGroup.setItemMeta(meta);
		
		meta.setDisplayName(ChatColor.GOLD + "Remove Group");
		removeGroup.setItemMeta(meta);
		
		meta.setDisplayName(ChatColor.GOLD + "Add Permission");
		addPerm.setItemMeta(meta);
		
		meta.setDisplayName(ChatColor.GOLD + "Remove Permission");
		removePerm.setItemMeta(meta);
		
	}
	
	public static final String MENU_NAME = "Permission Manager";
	private static ItemStack addGroup, removeGroup, addPerm, removePerm;
	
	public PlayerPermManager(Player p) {
		this(p, null);
	}
	
	public PlayerPermManager(Player p, @Nullable CoreBox backbutton) {
		super(p, MENU_NAME, 18, backbutton);
		
		if (!VaultAdapter.isPermissionsLoaded())
			return;
			
		NoxPlayer player = CorePlayerManager.getInstance().getPlayer(p);
		
		ItemStack playerHead = new ItemStack(Material.SKULL_ITEM);
		ItemMeta meta = playerHead.getItemMeta();
		meta.setDisplayName(player.getFullName());
		
		playerHead.setItemMeta(meta);
		
		//Sections
		CoreBoxRegion options = new CoreBoxRegion(this, new Vector(1, 0, 2), 1, 5);
		
		//Player head
		addMenuItem(4, new CoreBoxItem(this, playerHead) {
			public boolean onClick(InventoryClickEvent click) {
				return false;
			}
		});
		
		options.add(new CoreBoxItem(this, addGroup) {
			public boolean onClick(InventoryClickEvent click) {
				new GroupBox(getPlayer(), true, VaultAdapter.GroupUtils.getGroupList()).show();;
				return true;
			}
		});
		
		options.add(new CoreBoxItem(this, removeGroup) {
			public boolean onClick(InventoryClickEvent click) {
				List<String> pGroups = new ArrayList<String>();
				for (String g : VaultAdapter.permission.getPlayerGroups(getPlayer()))
					pGroups.add(g);
				
				new GroupBox(getPlayer(), false, pGroups).show();;
				return true;
			}
		});
		
		options.add(new CoreBoxItem(this, addPerm) {
			public boolean onClick(InventoryClickEvent click) {
				new PermBox(getPlayer(), true, null);
				return true;
			}
		});
		
		options.add(new CoreBoxItem(this, removePerm) {
			public boolean onClick(InventoryClickEvent click) {
				List<String> perms = new ArrayList<String>();
				for (PermissionAttachmentInfo p : getPlayer().getEffectivePermissions())
					perms.add(p.getPermission());
				
				new PermBox(getPlayer(), false, perms).show();
				return true;
			}
		});
		
	}
	
	private class GroupBox extends CoreBox {
		
		public final boolean add;
		private final Player p;
		
		public GroupBox(Player p, boolean add, List<String> groups) {
			super(p, add? "Add" : "Remove" + " Group For " + p.getName(),(int) Math.max(9, 9* Math.ceil(groups.size() / 9.0)));
			
			this.add = add;
			this.p = p;
			
			ItemStack group = new ItemStack(Material.BOOK);
			ItemMeta meta = group.getItemMeta();
			
			for (int i = 0; i < groups.size() && i < getBox().getSize(); i++) {
				final String name = groups.get(i);
				meta.setDisplayName(ChatColor.AQUA + name);
				group.setItemMeta(meta);
				
				addMenuItem(i, new CoreBoxItem(this, group.clone()) {
					public boolean onClick(InventoryClickEvent click) {
						if (!VaultAdapter.isPermissionsLoaded())
							return false;
						
						if (GroupBox.this.add)
							VaultAdapter.permission.playerAddGroup(GroupBox.this.p, name);
						else {
							VaultAdapter.permission.playerRemoveGroup(GroupBox.this.p, name);
							removeMenuItem(click.getSlot());
						}
						
						return true;
					}
				});
			}
			
		}
		
	}
	
	private class PermBox extends CoreBox {
		
		private final Player p;
		
		public PermBox(Player p, boolean add, @Nullable List<String> permissions) {
			super(p,  add? "Add" : "Remove"  + " Permission For " + p.getName(), permissions != null? Math.min(54, Math.max(9, (int) Math.ceil(permissions.size() / 9) * 9)) : 0);
			
			this.p = p;
			
			if (add && VaultAdapter.isPermissionsLoaded()) {
				new TextPrompt(p) {
					
					@Override
					public void onReturn(String[] lines) {
						try {
							VaultAdapter.permission.playerAdd(PermBox.this.p, StringUtil.join("", lines));
						} catch (Exception e) {}
					}

					public NoxPlugin getPlugin() {
						return NoxCore.getInstance();
					}
				}.show();
			} else if (permissions != null && permissions.size() > 0) {
				ItemStack permItem = new ItemStack(Material.PAPER);
				ItemMeta meta = permItem.getItemMeta();

				for (int i = 0; i < permissions.size() && i < getBox().getSize(); i++) {
					final String name = permissions.get(i);
					meta.setDisplayName(ChatColor.AQUA + name);
					permItem.setItemMeta(meta);
					
					addMenuItem(i, new CoreBoxItem(this, permItem.clone()) {
						public boolean onClick(InventoryClickEvent click) {
							if (!VaultAdapter.isPermissionsLoaded())
								return false;
							
							VaultAdapter.permission.playerRemove(PermBox.this.p, name);
							removeMenuItem(click.getSlot());
							
							return true;
						}
					});
				}
			} else {
				hide();
				return;
			}
			
		}
		
	}
}
