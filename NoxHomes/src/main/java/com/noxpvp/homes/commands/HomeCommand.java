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
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.localization.GlobalLocale;
import com.noxpvp.core.utils.PlayerUtils;
import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.homes.HomesPlayer;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.managers.HomesPlayerManager;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.noxpvp.homes.locale.HomeLocale.*;
import static com.noxpvp.homes.permissions.HomePermission.*;

public class HomeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "home";
	public static final String PERM_NODE = "home";
	private final PermissionHandler permHandler;

	public HomeCommand() {
		super(COMMAND_NAME, true);
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}

	public CommandResult execute(CommandContext context) {
		if (!context.isPlayer()) {
			MessageUtil.sendLocale(context.getSender(), GlobalLocale.CONSOLE_ONLYPLAYER);
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

		if (LogicUtil.nullOrEmpty(homeName)) homeName = DefaultHome.PERM_NODE;

		if (!playerString.equals(sender.getName()) && !PlayerUtils.isOnline(playerString))
			return new CommandResult(this, true, "For safety we could not allow creating homes for offline players.", "This feature will be made at a later time. ", " This is due to the UUID update.");
		else
			player = Bukkit.getPlayer(playerString);

		HomesPlayer hPlayer = HomesPlayerManager.getInstance().getPlayer(player);

		BaseHome home = hPlayer.getHome(homeName);


		boolean own = sender.getName().equals(player);
		boolean isDefault = home instanceof DefaultHome;

		String perm = (own ? (isDefault ? WARP_OWN_DEFAULT : WARP_OWN_NAMED) : (isDefault ? WARP_OTHER_DEFAULT : WARP_OTHER_NAMED)).getName();
		String permMulti = perm + ".multi";

		if (!permHandler.hasPermission(sender, perm))
			throw new NoPermissionException(sender, perm, new StringBuilder().append("Teleport to ").append((own ? "your " : "others ")).append("homes.").toString());

		if (home == null)
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "The home \"" + (homeName == null ? "default" : homeName) + "\" does not exist");
		else if (home.tryTeleport(sender, permHandler.hasPermission(sender, permMulti))) {
			MessageUtil.sendLocale(sender, (own) ? HOME_OWN : HOME_OTHERS, player.getName(), (homeName == null ? "default" : homeName));
			if (home.isOwner(sender)) {

				String perm2 = StringUtil.join(".", perm, "other-towns");
				if (TownyUtil.isClaimedLand(home.getLocation()) && !TownyUtil.isOwnLand(sender, home.getLocation()) && !permHandler.hasPermission(sender, perm2)) {
					MessageUtil.sendLocale(sender, DELHOME_INVALID, (homeName == null ? "default" : homeName), BAD_LOCATION.get("Not part of wild and not your own town."));
					hPlayer.removeHome(home);
				}
			}
		} else
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Could not warp home.");

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
