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

package com.noxpvp.core.internal;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.internal.CommonPlugin;
import com.noxpvp.core.NoxPlugin;

public class PermissionHandler {
	public static boolean defaultLogToConsole = true;
	private final com.bergerkiller.bukkit.common.internal.PermissionHandler permHandler;
	private NoxPlugin plugin;

	public PermissionHandler(NoxPlugin plugin) {
		this.plugin = plugin;
		permHandler = CommonPlugin.getInstance().getPermissionHandler();
	}

	public boolean hasPermission(CommandSender sender, boolean logConsole, String permission) {
		boolean isTrue = permHandler.handlePermission(sender, permission);

		if (logConsole && !isTrue)
			plugin.log(Level.WARNING, new MessageBuilder().red("Failed permission from \"").yellow(sender.getName()).red("\" for permission \"").yellow(permission).red("\"").toString());

		return isTrue;
	}

	public boolean hasPermission(CommandSender sender, String permission) {
		return hasPermission(sender, defaultLogToConsole, permission);
	}

}
