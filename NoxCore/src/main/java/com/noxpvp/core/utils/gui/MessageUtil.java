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

package com.noxpvp.core.utils.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.filtering.Filter;
import com.bergerkiller.bukkit.common.localization.LocalizationEnum;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.internal.CommandSenderFilter;
import com.noxpvp.core.internal.PlayerFilter;
import com.noxpvp.core.utils.BukkitUtil;

public class MessageUtil {
	
	public static void broadcast(String message) {
		sendMessage(BukkitUtil.getOnlinePlayers(), message);
	}
	
	public static void broadcast(String... messages) {
		sendMessage(BukkitUtil.getOnlinePlayers(), messages);
	}
	
	public static void broadcast(final String permission, String message) {
		sendMessage(BukkitUtil.getOnlinePlayers(), permission, message);
	}
	
	public static void broadcast(final String permission, String... messages) {
		sendMessage(BukkitUtil.getOnlinePlayers(), permission, messages);
	}
	
	public static void broadcast(World world, String message) {
		for (final Player player : world.getPlayers()) {
			sendMessage(player, message);
		}
	}
	
	public static void broadcast(World world, String... messages) {
		for (final Player player : world.getPlayers()) {
			sendMessage(player, messages);
		}
	}
	
	public static void broadcast(World world, final String permission, String message) {
		sendMessage(world.getPlayers(), permission, message);
	}
	
	public static void broadcast(World world, final String permission,
			String... messages) {
		sendMessage(world.getPlayers(), permission, messages);
	}
	
	public static String getGlobalLocale(NoxPlugin plugin, String locale,
			String... params) {
		return plugin.getGlobalLocale(locale, params);
	}
	
	public static String getLastColors(String message) {
		final char[] chars = message.toCharArray();
		
		int i = 0;
		
		String ret = "";
		boolean c = false, f = false;
		while (i < chars.length) {
			if (!c) {
				if (chars[i] == ChatColor.COLOR_CHAR) {
					c = true;
				}
				i++;
				continue;
			} else {
				final ChatColor color = ChatColor.getByChar(chars[i]);
				if (color != null) {
					if (!f) {
						ret = color.toString();
						f = true;
					} else {
						if (color.isColor()) {
							ret = color.toString();
						}
						ret += color.toString();
					}
				}
				c = false;
				i++;
				continue;
			}
		}
		if (LogicUtil.nullOrEmpty(ret))
			return ChatColor.WHITE.toString();
		
		return ret;
	}
	
	public static String getLocale(NoxPlugin plugin, String locale, String... params) {
		return plugin.getLocale(locale, params);
	}
	
	public static boolean globalLocaleExists(NoxPlugin plugin, String locale,
			String... args) {
		final String l = getGlobalLocale(plugin, locale, args);
		return l == null || l.length() == 0 || l == "" || l.equals(""); // TODO:
																		// Remove
																		// unneeded
																		// checks.
																		// Not
																		// sure
																		// which
																		// yet.
	}
	
	public static boolean localeExists(NoxPlugin plugin, String locale,
			String... args) {
		final String l = getLocale(plugin, locale, args);
		return l == null || l.length() == 0 || l == "" || l.equals(""); // TODO:
																		// Remove
																		// unneeded
																		// checks.
																		// Not
																		// sure
																		// which
																		// yet.
	}
	
	public static String parseArguments(String message, String... args) {
		final StringBuilder msg = new StringBuilder(message);
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				StringUtil.replaceAll(msg, "%" + i + "%", LogicUtil.fixNull(args[i],
						"null"));
			}
		}
		return msg.toString();
	}
	
	public static String parseColor(String message) {
		return StringUtil.ampToColor(message);
	}
	
	public static String[] parseColor(String[] messages) {
		for (int i = 0; i < messages.length; i++) {
			messages[i] = parseColor(messages[i]);
		}
		
		return messages;
	}
	
	public static void sendGlobalLocale(NoxPlugin plugin, CommandSender sender,
			String locale, String... params) {
		sender.sendMessage(parseColor(plugin.getGlobalLocale(locale, params)));
	}
	
	public static void sendLocale(CommandSender sender, LocalizationEnum locale,
			String... args) {
		locale.message(sender, args);
	}
	
	public static void sendLocale(NoxPlugin plugin, CommandSender sender,
			String locale, String... params) {
		sender.sendMessage(parseColor(plugin.getLocale(locale, params)));
	}
	
	public static <T extends CommandSender> void sendMessage(Collection<T> senders,
			Filter<T> filter, String message) {
		for (final T sender : senders)
			if (filter.isFiltered(sender)) {
				sendMessage(sender, message);
			}
	}
	
	public static <T extends CommandSender> void sendMessage(Collection<T> senders,
			Filter<T> filter, String... messages) {
		for (final T sender : senders)
			if (filter.isFiltered(sender)) {
				sendMessage(sender, messages);
			}
	}
	
	public static <T extends CommandSender> void sendMessage(Collection<T> senders,
			String message) {
		for (final T sender : senders) {
			sender.sendMessage(message);
		}
	}
	
	public static <T extends CommandSender> void sendMessage(Collection<T> senders,
			String... messages) {
		for (final CommandSender sender : senders) {
			sendMessage(sender, messages);
		}
	}
	
	public static <T extends CommandSender> void sendMessage(Collection<T> senders,
			final String permission, String message) {
		sendMessage(senders, new CommandSenderFilter<T>() {
			
			@Override
			public boolean isFiltered(CommandSender sender) {
				if (VaultAdapter.isPermissionsLoaded()
						&& VaultAdapter.permission.has(sender, permission))
					return true;
				else if (sender.hasPermission(permission))
					return true;
				return false;
			}
			
			@Override
			protected boolean nonMatch() {
				return false;
			}
		}, message);
	}
	
	public static <T extends CommandSender> void sendMessage(Collection<T> senders,
			final String permission, String... messages) {
		sendMessage(senders, new CommandSenderFilter<T>() {
			
			@Override
			public boolean isFiltered(T sender) {
				if (VaultAdapter.isPermissionsLoaded()
						&& VaultAdapter.permission.has(sender, permission))
					return true;
				else if (sender.hasPermission(permission))
					return true;
				return false;
			}
			
			@Override
			protected boolean nonMatch() {
				return false;
			}
		}, messages);
	}
	
	public static <T extends CommandSender> void sendMessage(T sender, String message) {
		sender.sendMessage(message);
	}
	
	public static <T extends CommandSender> void sendMessage(T sender,
			String... messages) {
		if (!LogicUtil.nullOrEmpty(messages)) {
			for (final String message : messages) {
				sendMessage(sender, message);
			}
		}
	}
	
	public static void sendMessageNearby(Entity entity, double radX, double radY,
			double radZ, String message) {
		for (final Entity e : WorldUtil.getNearbyEntities(entity, radX, radY, radZ))
			if (e instanceof CommandSender) {
				((CommandSender) e).sendMessage(message);
			}
	}
	
	public static void sendMessageNearby(Entity entity, double radius, String message) {
		sendMessageNearby(entity, radius, radius, radius, message);
	}
	
	public static void sendMessageNearby(Location location, double radX,
			double radY, double radZ, String message) {
		for (final Entity e : WorldUtil
				.getNearbyEntities(location, radX, radY, radZ))
			if (e instanceof CommandSender) {
				((CommandSender) e).sendMessage(message);
			}
	}
	
	public static void sendMessageNearby(Location location, double radius,
			String message) {
		sendMessageNearby(location, radius, radius, radius, message);
	}
	
	public static void sendMessageToGroup(final String groupName, String message) {
		sendMessage(BukkitUtil.getOnlinePlayers(), new PlayerFilter(Player.class) {
			
			@Override
			public boolean isFiltered(Player player) {
				if (VaultAdapter.isPermissionsLoaded()
						&& VaultAdapter.permission.hasGroupSupport()
						&& VaultAdapter.permission.playerInGroup(player.getWorld(),
								player.getName(), groupName))
					return true;
				else if (VaultAdapter.isPermissionsLoaded()
						&& !VaultAdapter.permission.hasGroupSupport())
					return VaultAdapter.permission.has(player, "group." + groupName);
				return false;
			}
			
			@Override
			protected boolean nonMatch() {
				return false;
			}
		}, message);
	}
	
	public static void sendMessageToGroup(final String groupName, String... messages) {
		sendMessage(BukkitUtil.getOnlinePlayers(), new PlayerFilter(Player.class) {
			
			@Override
			public boolean isFiltered(Player player) {
				if (VaultAdapter.isPermissionsLoaded()
						&& VaultAdapter.permission.hasGroupSupport()
						&& VaultAdapter.permission.playerInGroup(player.getWorld(),
								player.getName(), groupName))
					return true;
				else if (VaultAdapter.isPermissionsLoaded()
						&& !VaultAdapter.permission.hasGroupSupport())
					return VaultAdapter.permission.has(player, "group." + groupName);
				return false;
			}
			
			@Override
			protected boolean nonMatch() {
				return false;
			}
		}, messages);
	}
	
	public static String[] splitToArray(String string, int lineLength) {
		final List<String> ret = splitToList(string, lineLength);
		
		return ret.toArray(new String[ret.size()]);
		
	}
	
	public static List<String> splitToList(String string, int lineLength) {
		
		final List<String> ret = new ArrayList<String>();
		
		int one = 0, two = 0;
		boolean ending = false;
		
		for (final char cur : string.toCharArray()) {
			if (two - one >= lineLength && !ending) {
				ending = true;
			}
			
			if (ending && cur == ' ') {
				ret.add(string.substring(one, two));
				
				ending = false;
				one = two;
			}
			
			two++;
		}
		
		final String leftOver = string.substring(one, two);
		if (leftOver != null && !leftOver.isEmpty()) {
			ret.add(leftOver);
		}
		
		return ret;
	}
	
}
