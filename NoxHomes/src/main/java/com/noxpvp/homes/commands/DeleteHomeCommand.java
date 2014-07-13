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

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.utils.PlayerUtils;
import com.noxpvp.homes.HomesPlayer;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.managers.HomesPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DeleteHomeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "delhome";
	public static final String PERM_NODE = "delhome";

	public DeleteHomeCommand() {
		super(COMMAND_NAME, false);
	}


	public CommandResult execute(CommandContext context) {
		if (context.hasFlag("h") || context.hasFlag("help")) {
			displayHelp(context.getSender());
			if (!context.isPlayer())
				return new CommandResult(this, false, "Use (-p | --player) to specify a player to use this on.");
			else
				return new CommandResult(this, false);
		}

		Player sender = context.getPlayer();

		if (context.hasFlag("h") || context.hasFlag("help"))
			return new CommandResult(this, false);

		String playerString;

		if (context.hasFlag("p"))
			playerString = context.getFlag("p", String.class);
		else if (context.hasFlag("player"))
			playerString = context.getFlag("player", String.class);
		else
			playerString = sender.getName();

		Player player;

		String homeName = null;

		if (context.hasArgument(0))
			homeName = context.getArgument(0);

		if (LogicUtil.nullOrEmpty(homeName)) homeName = null;

		if (!playerString.equals(sender.getName()) && !PlayerUtils.isOnline(playerString))
			return new CommandResult(this, true, "For safety we could not allow removing homes for offline players. ", " This feature will be made at a later time. ", " This is due to the UUID update.");
		else
			player = Bukkit.getPlayer(playerString);

		HomesPlayer hPlayer = HomesPlayerManager.getInstance().getPlayer(player);
		hPlayer.tryRemoveHome(sender, homeName);

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
