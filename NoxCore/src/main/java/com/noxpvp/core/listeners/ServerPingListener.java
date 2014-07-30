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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.utils.gui.MessageUtil;

public class ServerPingListener extends NoxListener<NoxCore> {

	public static final char new_line_char = '\n';

	private String version;
	private List<String> motds;

	public ServerPingListener(NoxCore core) {
		super(core);

		version = Bukkit.getVersion();
		version = "&a&l[" + version.substring(version.indexOf("(") + 1, version.indexOf(")")) + "] ";

		this.motds = core.getCoreConfig().getList("motd.ping", String.class, Arrays.asList("&4&lNoxImperialis &k| &6Build, Battle, Burn!/ntestline"));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPing(ServerListPingEvent event) {
		if (motds == null || motds.size() < 1) return;

		int random = 0;

		if (motds.size() > 1)
			random = RandomUtils.nextInt(motds.size());

		if (motds.get(random) != null) {
			String setter = version + motds.get(random).replaceAll("/n", new_line_char + "");
			event.setMotd(MessageUtil.parseColor(setter));

		}
	}
}
