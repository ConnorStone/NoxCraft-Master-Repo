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
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.homes.HomesPlayer;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.managers.HomesPlayerManager;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.noxpvp.core.localization.GlobalLocale.CONSOLE_ONLYPLAYER;

public class SetHomeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "sethome";
	public static final String PERM_NODE = "sethome";

	public SetHomeCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).aqua(" [name]").newLine();

		return mb.lines();
	}

	public CommandResult execute(CommandContext context) {
		if (!context.isPlayer()) {
			MessageUtil.sendLocale(context.getSender(), CONSOLE_ONLYPLAYER);
			return new CommandResult(this, true);
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
			return new CommandResult(this, true, "For safety we could not allow creating homes for offline players. ", " This feature will be made at a later time. ", " This is due to the UUID update.");
		else
			player = Bukkit.getPlayer(playerString);

		BaseHome newHome;
		if (homeName == null)
			newHome = new DefaultHome(playerString, sender);
		else
			newHome = new NamedHome(playerString, homeName, sender);

		HomesPlayer hPlayer = HomesPlayerManager.getInstance().getPlayer(player);
		hPlayer.tryAddHome(sender, newHome);

		return new CommandResult(this, true);
	}

	public String[] getFlags() {
		return new String[]{"h", "help", "p", "player"};
	}

	public int getMaxArguments() {
		return 1;
	}

	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}
}
