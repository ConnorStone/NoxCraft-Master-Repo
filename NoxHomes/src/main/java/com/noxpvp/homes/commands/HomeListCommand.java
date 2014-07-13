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
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.localization.GlobalLocale;
import com.noxpvp.core.utils.PlayerUtils;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.locale.HomeLocale;
import com.noxpvp.homes.managers.HomesPlayerManager;
import com.noxpvp.homes.permissions.HomePermission;
import com.noxpvp.homes.tp.BaseHome;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeListCommand extends BaseCommand {
	public static final String COMMAND_NAME = "homes";
	public static final String LIST_PERM_NODE = "list";
	private final PermissionHandler permHandler;

	public HomeListCommand() {
		super(COMMAND_NAME, false);
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}

	public CommandResult execute(CommandContext context) {
		if (context.hasFlag("h") || context.hasFlag("help"))
			return new CommandResult(this, false);

		CommandSender sender = context.getSender();
		HomesPlayerManager manager = HomesPlayerManager.getInstance();

		String playerName;
		Player player;


		if (context.hasFlag("p"))
			playerName = context.getFlag("p", String.class);
		else if (context.hasFlag("player"))
			playerName = context.getFlag("player", String.class);
		else
			playerName = sender.getName();

		if (!playerName.equals(sender.getName()) && !PlayerUtils.isOnline(playerName))
			return new CommandResult(this, true, "For safety we could not allow removing homes for offline players. ", " This feature will be made at a later time. ", " This is due to the UUID update.");
		else
			player = Bukkit.getPlayer(playerName);

		if ((playerName == null || playerName.length() == 0) && context.isPlayer()) {
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Player match failed. Player was " + ((playerName == null) ? "null" : "blank"));
			return new CommandResult(this, true);
		} else if ((playerName == null || playerName.length() == 0)) {
			MessageUtil.sendLocale(sender, GlobalLocale.CONSOLE_NEEDPLAYER, "Use the -p \"PlayerName\" flag");
			return new CommandResult(this, true);
		}

		boolean own = false;
		if (playerName.equals(sender.getName()) && context.isPlayer()) {
			own = true;
		}

		String perm = ((own) ? HomePermission.LIST_OWN : HomePermission.LIST_OTHERS).getName();
		if (!permHandler.hasPermission(sender, perm)) {
			throw new NoPermissionException(sender, perm, new StringBuilder("Not allowed to view list of ").append((own ? "your own " : "other's ")).append("homes!").toString());
		}


		List<BaseHome> homes = manager.getPlayer(player).getHomes();

		String homeList;
		List<String> homeNames = new ArrayList<String>(homes.size());


		if (homes.isEmpty())
			homeList = "None";
		else {
			for (BaseHome home : homes)
				homeNames.add(home.getName());
			homeList = StringUtil.combineNames(homeNames);
		}

		if (own) playerName = "own";

		MessageUtil.sendLocale(sender, ((own) ? HomeLocale.LIST_OWN  : HomeLocale.LIST_OTHERS), playerName, homeList);//TODO: Prettify large lists.
		return new CommandResult(this, true);
	}

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).newLine();

		return mb.lines();
	}

	public String[] getFlags() {
		return new String[]{"h", "help", "p", "player"};
	}

	public int getMaxArguments() {
		return 0;
	}

	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}
}
