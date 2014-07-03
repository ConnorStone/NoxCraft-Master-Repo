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

package com.noxpvp.homes.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.localization.GlobalLocale;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.managers.old.HomesPlayerManager;
import com.noxpvp.homes.tp.BaseHome;

public class DeleteHomeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "delhome";
	public static final String PERM_NODE = "delhome";
	private HomesPlayerManager manager;
	private PermissionHandler permHandler;

	public DeleteHomeCommand() {
		super(COMMAND_NAME, false);
		manager = getPlugin().getHomeManager();
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}


	public CommandResult execute(CommandContext context) {
		if (context.hasFlag("h") || context.hasFlag("help")) {
			displayHelp(context.getSender());
			if (!context.isPlayer())
				return new CommandResult(this, false, "Use (-p | --player) to specify a player to use this on.");
			else
				return new CommandResult(this, false);
		}

		Player player;

		if (context.hasFlag("p"))
			player = Bukkit.getPlayer(context.getFlag("p", String.class));
		else if (context.hasFlag("player"))
			player = Bukkit.getPlayer(context.getFlag("player", String.class));
		else
			player = (context.isPlayer()) ? context.getPlayer() : null;

		if (player == null) {
			MessageUtil.sendLocale(context.getSender(), GlobalLocale.CONSOLE_NEEDPLAYER, "To delete a home.");
			return new CommandResult(this, true);
		}

		boolean own = player.equals(context.getSender());

		String homeName = null;
		if (context.hasArgument(0))
			homeName = context.getArgument(0);

		String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, PERM_NODE, (own ? "" : "others.") + (homeName == null ? "default" : "named"));

		if (!permHandler.hasPermission(context.getSender(), perm))
			throw new NoPermissionException(context.getSender(), perm, new StringBuilder().append("Delete ").append(((own) ? "Own" : "Others")).append(" homes.").toString());

		BaseHome home = manager.getHome(player.getName(), homeName);

		if (home != null)
			manager.removeHome(home);

		MessageUtil.sendLocale(getPlugin(), context.getSender(), "homes.delhome" + ((own) ? ".own" : ""), player.getName(), (homeName == null ? "default" : homeName));
		return new CommandResult(this, true);
	}

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).aqua(" [").aqua("name]").newLine();
		return mb.lines();
	}

	public String[] getFlags() {
		return new String[]{"h", "help", "p", "player"};
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}
}
