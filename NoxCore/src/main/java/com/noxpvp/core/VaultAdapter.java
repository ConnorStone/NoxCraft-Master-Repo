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

package com.noxpvp.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam.FriendlyFireType;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.OldNoxPlayer;
import com.noxpvp.core.data.PluginPlayer;
import com.noxpvp.core.localization.CoreLocale;
import com.noxpvp.core.utils.BukkitUtil;

public class VaultAdapter {
	
	public static Chat			chat		= null;
	public static Economy		economy		= null;
	public static Permission	permission	= null;
	
	public static boolean isChatLoaded() {
		return chat != null;
	}
	
	public static boolean isEconomyLoaded() {
		return economy != null;
	}
	
	public static boolean isPermissionsLoaded() {
		return permission != null;
	}
	
	public static void load() {
		GroupUtils.log = NoxCore.getInstance().getModuleLogger("VaultAdapter",
				"GroupUtils");
		setupChat();
		setupEconomy();
		setupPermission();
	}
	
	public static boolean reloadTeams() {
		if (isPermissionsLoaded()) {
			GroupUtils.setupTeams();
			GroupUtils.reloadAllGroupTags();
			
			return true;
		} else
			return false;
		
	}
	
	public static boolean setupChat() {
		final RegisteredServiceProvider<Chat> service = Bukkit.getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.chat.Chat.class);
		if (service != null) {
			chat = service.getProvider();
		}
		return chat != null;
	}
	
	public static boolean setupEconomy() {
		final RegisteredServiceProvider<Economy> service = Bukkit.getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (service != null) {
			economy = service.getProvider();
		}
		return economy != null;
	}
	
	public static boolean setupPermission() {
		final RegisteredServiceProvider<Permission> service = Bukkit.getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (service != null) {
			permission = service.getProvider();
		}
		return permission != null;
	}
	
	public static class GroupUtils {
		
		static ModuleLogger	log;
		
		public static String getFormattedPlayerName(Player p) {
			final String group = getPlayerGroup(p);
			
			if (group != null)
				return CoreLocale.GROUP_TAG_PREFIX.get(group) + p.getName()
						+ CoreLocale.GROUP_TAG_SUFFIX.get(group);
			
			return p.getName();
		}
		
		public static List<String> getGroupList() {
			if (isPermissionsLoaded() && permission.hasGroupSupport())
				return Arrays.asList(VaultAdapter.permission.getGroups());
			else if (log != null) {
				log.warning("Could not get group list... "
						+ (!isPermissionsLoaded() ? "Permissions not loaded."
								: permission.hasGroupSupport() ? ""
										: "No Group Support"));
			}
			
			return Collections.emptyList();
		}
		
		public static String getPlayerGroup(Player p) {
			if (isPermissionsLoaded())
				return VaultAdapter.permission.getPrimaryGroup(p);
			return null;
		}
		
		public static void reloadAllGroupTags() {
			if (isChatLoaded() && isPermissionsLoaded()
					&& permission.hasGroupSupport()) {
				for (final Player p : BukkitUtil.getOnlinePlayers()) {
					loadGroupTag(p);
				}
			}
		}
		
		public static void reloadGroupTag(Player p) {
			if (p == null)
				return;
			
			loadGroupTag(p);
		}
		
		private static void loadGroupTag(Player p) {
			final String[] groups = VaultAdapter.permission.getPlayerGroups(p);
			
			if (groups.length < 0)
				return;
			
			final String group = getPlayerGroup(p);
			final String teamName = group + "Team";
			
			final CommonScoreboard pBoard = CommonScoreboard.get(p);
			CommonTeam team;
			
			if ((team = CommonScoreboard.getTeam(teamName)) == null) {
				team = CommonScoreboard.loadTeam(teamName);
			}
			
			if (team != null) {
				pBoard.setTeam(team);
				team.addPlayer(p);
				CommonScoreboard.saveTeam(team);
			}
			
		}
		
		private static void setupTeams() {
			for (final String group : getGroupList()) {
				
				final String name = group + "Team";
				if (CommonScoreboard.getTeam(name) == null) {
					
					final CommonTeam team = CommonScoreboard.loadTeam(name);
					
					team.setFriendlyFire(FriendlyFireType.ON);
					team.setPrefix(CoreLocale.GROUP_TAG_PREFIX.get(group));
					team.setSuffix(CoreLocale.GROUP_TAG_SUFFIX.get(group));
					team.setSendToAll(true);
					
					team.show();
					
					CommonScoreboard.saveTeam(team);
				}
			}
		}
		
	}
	
	public static class PermUtils {
		
		public static boolean hasPermission(NoxPlayer p, String string) {
			if (p.getStats().getLastWorld() != null)
				return hasPermission(p.getStats().getLastWorld(), p.getPlayer(),
						string);
			return hasPermission(p.getStats().getLastWorldName(), p.getPlayerName(),
					string);
		}
		
		@Deprecated
		public static boolean hasPermission(OldNoxPlayer p, String string) {
			if (p.isOnline())
				return hasPermission(p.getLastWorld(), p.getPlayer(), string);
			return hasPermission(p.getLastWorldName(), p.getPlayerName(), string);
		}
		
		public static boolean hasPermission(PluginPlayer<?> p, String string) {
			return hasPermission((NoxPlayer) p, string);
		}
		
		public static boolean hasPermission(String world, String playerName,
				String perm) {
			if (isPermissionsLoaded())
				return permission.has(world, playerName, perm);
			return false;
		}
		
		public static boolean hasPermission(World w, Player p, String perm) {
			if (isPermissionsLoaded())
				return permission.has(p, perm);
			
			return p.hasPermission(perm);
		}
	}
}
