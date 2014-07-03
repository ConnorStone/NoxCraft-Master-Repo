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

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.localization.GlobalLocale;
import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.managers.old.HomesPlayerManager;
import com.noxpvp.homes.locale.HomeLocale;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;

public class HomeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "home";
	public static final String PERM_NODE = "home";
	private final PermissionHandler permHandler;
	private HomesPlayerManager manager;

	public HomeCommand() {
		super(COMMAND_NAME, true);
		manager = getPlugin().getHomeManager();
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}

	public CommandResult execute(CommandContext context) {

		if (!context.isPlayer()) {
			MessageUtil.sendLocale(context.getSender(), GlobalLocale.CONSOLE_ONLYPLAYER);
			return new CommandResult(this, true);
		}

		if (context.hasFlag("h") || context.hasFlag("help"))
			return new CommandResult(this, false); //Its caught anyway why repeat display help code...

		Player sender = context.getPlayer();

		String player = null, homeName = null;

		if (context.hasFlag("p"))
			player = context.getFlag("p", String.class);
		else if (context.hasFlag("player"))
			player = context.getFlag("player", String.class);
		else
			player = context.getPlayer().getName(); //Impossible to NPE. If it does. We got problems..

		if (context.hasArgument(0))
			homeName = context.getArgument(0);

		boolean own = sender.getName().equals(player);

		String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, PERM_NODE, (own ? "" : "others.") + (homeName == null ? DefaultHome.PERM_NODE : NamedHome.PERM_NODE));
		if (!permHandler.hasPermission(sender, perm))
			throw new NoPermissionException(sender, perm, new StringBuilder().append("Teleport to ").append((own ? "your " : "others ")).append("homes.").toString());

		BaseHome home = manager.getHome(player, homeName);
		if (home == null)
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "The home \"" + (homeName == null ? "default" : homeName) + "\" does not exist");
		else if (home.tryTeleport(sender, permHandler.hasPermission(sender, perm + ".multi"))) {
			MessageUtil.sendLocale(getPlugin(), sender, "homes.home" + (own ? ".own" : ""), player, (homeName == null ? "default" : homeName));
			if (home.isOwner(sender)) {

				String perm2 = StringUtil.join(".", NoxHomes.HOMES_NODE, PERM_NODE, "other-towns");
				if (TownyUtil.isClaimedLand(home.getLocation()) && !TownyUtil.isOwnLand(sender, home.getLocation()) && !permHandler.hasPermission(sender, perm2)) {
					MessageUtil.sendLocale(sender, HomeLocale.DELHOME_INVALID, (homeName == null ? "default" : homeName), HomeLocale.BAD_LOCATION.get("Not part of wild and not your own town."));
					manager.removeHome(home);
				}
			}
		} else
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Could not teleport home.");

		return new CommandResult(this, true);
	}

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).aqua(" [name]").newLine();
		return mb.lines();
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
