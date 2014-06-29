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

import java.util.Random;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.homes.managers.old.HomesPlayerManager;
import com.noxpvp.homes.NoxHomes;

public class HomeAdminWipeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "wipehomes";
	public static final String PERM_NODE = "wipe.homes";
	private static final Random r = new Random();
	private HomesPlayerManager manager;

	private String key;
	private String[] helpLines;

	public HomeAdminWipeCommand() {
		super(COMMAND_NAME, false);
		key = getNextKey();

		updateHelp();

		manager = getPlugin().getHomeManager();
	}

	public CommandResult execute(CommandContext context) {
		String[] args = context.getArguments();
		CommandSender sender = context.getSender();

		boolean wiped = false;
		if (args.length < 1) {
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Must Specify Safety Key.");
			return new CommandResult(this, false);
		}

		String k = null;
		if (args.length > 0) {
			k = args[0];

			if (k.equals(key)) {
				manager.clear();
				manager.save();
				wiped = true;
			} else {
				MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Incorrect safety key. Key is not \"" + k + "\"");
				return new CommandResult(this, false);
			}
		}

		if (wiped) {
			key = getNextKey();
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_SUCCESS, "Wiped home data.");

			updateHelp();
		} else {
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Could not wipe data.");
		}
		return new CommandResult(this, true);
	}

	private String getNextKey() {
		return String.valueOf(r.nextInt(NoxCore.getInstance().getConfig().getInt("wipe-confirmation-number", 9999)));
	}

	private void updateHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.blue("/").append(HomeAdminCommand.COMMAND_NAME).append(' ').append(COMMAND_NAME).newLine();
		mb.red("WILL WIPE ALL HOME LOCATIONS ON ALL PLAYERS").newLine();
		mb.red("Current Safety Key: ").yellow(key);

		helpLines = mb.lines();
	}

	public String[] getHelp() {
		return helpLines;
	}

	public String[] getDescription() {
		return new String[]{"Wipes all home data from all players."};
	}

	public String[] getFlags() {
		return new String[]{"h", "help"};
	}

	public int getMaxArguments() {
		return 1;
	}

	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}

}
