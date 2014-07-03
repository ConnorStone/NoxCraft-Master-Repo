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

package com.noxpvp.core.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.events.ChatPingEvent;
import com.noxpvp.core.localization.CoreLocale;
import com.noxpvp.core.utils.gui.MessageUtil;

public class ChatPingListener extends NoxListener<NoxCore> {

	private String color;
	private String chatChar;

	public ChatPingListener() {
		super(NoxCore.getInstance());

		this.color = CoreLocale.SPECIAL_CHAT_PINGCOLOR.get();
		this.chatChar = CoreLocale.SPECIAL_CHAT_PINGSYMBAL.get();
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onChatPingTabComplete(PlayerChatTabCompleteEvent event) {
		String token = event.getLastToken();

		if (!token.startsWith(chatChar)) return;

		int index = token.indexOf(chatChar);

		if (index < 0)
			return;
		if ((index + 1) >= token.length())
			return;

		String tempToken = token.substring(token.indexOf(chatChar) + 1);
		Player tabber = event.getPlayer();

		for (Player p : CommonUtil.getOnlinePlayers()) {
			String name = p.getName();

			if (name.toLowerCase().startsWith(tempToken.toLowerCase()) && tabber.canSee(p))
				event.getTabCompletions().add(chatChar + name);//yes this works, says someone on bukkit
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChatEvent(AsyncPlayerChatEvent event) {
		final String msg = event.getMessage();
		final List<Player> players = new ArrayList<Player>();
		for (final Player p : CommonUtil.getOnlinePlayers()) {
			if (msg.contains(chatChar + p.getName())) {
				players.add(p);
			}
		}


		Bukkit.getServer().getScheduler().runTaskLater(NoxCore.getInstance(), new Runnable() {
			public void run() {
				for (Player p : players) {
					ChatPingEvent ping = CommonUtil.callEvent(new ChatPingEvent(p, false));
					if (!ping.isCancelled())
						p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 100, 0); // this is the long distance bow hit marker, much louder
				}
			}
		}, 0);

		if (players.isEmpty())
			return;

		List<String> list = new ArrayList<String>();
		List<String> newNames = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(new String(msg));
		String lastCol = MessageUtil.getLastColors(msg);

		final String reset = ChatColor.RESET + lastCol;

		int i = 0;

		for (Player p : players) {
			list.add(i, chatChar + p.getName());
			newNames.add(i++, color + p.getName() + reset);

		}

		int n = 0;
		for (String s : list) {
			while (sb.indexOf(s, 0) >= 0) {
				sb.replace((sb.indexOf(s, 0)), (sb.indexOf(s, 0) + s.length()), newNames.get(n));
			}
			n++;
		}
		event.setMessage(sb.toString());
	}
}