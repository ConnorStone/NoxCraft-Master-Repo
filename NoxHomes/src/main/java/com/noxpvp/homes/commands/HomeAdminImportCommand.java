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
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.homes.HomeImporter;
import org.bukkit.command.CommandSender;

public class HomeAdminImportCommand extends BaseCommand {
	public static final String COMMAND_NAME = "import";
	public static final String PERM_NODE = "import";
	private final PermissionHandler permHandler;
	private String[] importerNames;


	public HomeAdminImportCommand() {
		super(COMMAND_NAME, false);

		if (NoxCore.getInstance() == null)
			throw new RuntimeException("NoxCore plugin is not loaded! Do not use this class in other plugins please.");

		permHandler = NoxHomes.getInstance().getPermissionHandler();

		HomeImporter[] vals = HomeImporter.values();
		importerNames = new String[vals.length];

		for (int i = 0; i < importerNames.length; i++)
			importerNames[i] = vals[i].name();
	}

	public CommandResult execute(CommandContext context) {
		CommandSender sender = context.getSender();
		String[] args = context.getArguments();

		return new CommandResult(this, true, "This command is not implemented for this version of NoxHomes!");
	}

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.blue("/").append(HomeAdminCommand.COMMAND_NAME).append(" ").append(COMMAND_NAME).append(" ").yellow("[importerName]");
		mb.newLine().blue("Importers: ").newLine();
		mb.aqua("[").green(StringUtil.combineNames(importerNames)).aqua("]");

		return mb.lines();
	}

	public String[] getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("Imports data from another homes type plugin.");

		return sb.toString().split(System.lineSeparator());
	}

	public String[] getFlags() {
		return new String[]{"e", "erase", "overwrite", "h", "help"};
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}
}
