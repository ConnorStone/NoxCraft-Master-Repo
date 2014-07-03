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

package com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.MasterReloader;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.localization.GlobalLocale;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.NoxMessageBuilder;

public class ReloadCommand extends BaseCommand {
	public static final String COMMAND_NAME = "Reloader";
	public static final String PERM_NODE = "nox.reload";
	private static final String ENTER_TREE = ">";

	private static final String[] flags = new String[]{"h", "help"};

	public ReloadCommand() {
		super(COMMAND_NAME, false);
	}

	@Override
	public NoxMessageBuilder onDisplayHelp(NoxMessageBuilder message) {
		message.withCommand(this, true);
		message.aqua("Put * on the end of any module to also load all sub modules").newLine().append(' ').newLine();//Have to have something there or it wont NL a null line

		message.green("Current reloadable modules:");

		MasterReloader mr = getPlugin().getMasterReloader();

		if (mr.hasModules())
			for (Reloader module : mr.getModules())
				nextTree(message.yellow(" "), module, 0);
		else
			message.red("No Modules Loaded?!");

		return (NoxMessageBuilder) message.newLine();
	}

	public CommandResult execute(CommandContext context) {

		CommandSender sender = context.getSender();
		String[] args = context.getArguments();

		if (!handler.hasPermission(sender, PERM_NODE))
			throw new NoPermissionException(sender, PERM_NODE, "You may not use this command!");

		if (context.getFlag("?", false) || context.getFlag("h", false) || context.getFlag("help", false) || args.length == 0) {
			return new CommandResult(this, false);
		}

		String module = null;
		if (args.length > 1)
			module = StringUtil.join(":", args);
		else if (args.length == 1)
			module = args[0];

		if (module == null)
			module = "";

		boolean all = false;
		if (module != null)
			all = module.endsWith("*");

		if (all)
			module = module.substring(0, module.length() - 1);

		Reloader r = null;
		if (module.equals("") || module.length() == 0 && all)
			r = getPlugin().getMasterReloader();
		else
			r = getPlugin().getMasterReloader().getModule(module.toLowerCase());

		try {
			if (all) {
				r.reload();
				r.reloadAll();
				MessageBuilder mb = new MessageBuilder(GlobalLocale.COMMAND_SUCCESS.get("Reloaded modules ->"));
				nextTree(mb, r, 0);
				sender.sendMessage(mb.lines());
			} else {
				r.reload();
				GlobalLocale.COMMAND_SUCCESS.message(sender, "Module \"" + module + "\" reloaded!");
			}
		} catch (NullPointerException e) {
			GlobalLocale.COMMAND_FAILED.message(sender, "Module does not exist. \"" + module + "\"");
		} catch (Exception e) {
			GlobalLocale.COMMAND_FAILED.message(sender, "An error occured: " + e.getMessage());
			e.printStackTrace();
		}
		
		return new CommandResult(this, true);
	}

	public String[] getDescription() {
		return new String[0];
	}

	public String[] getFlags() {
		return flags;
	}

	public int getMaxArguments() {
		return -1;
	}

	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}

	private void nextTree(MessageBuilder mb, Reloader module, int level) {
		mb.newLine();
		for (int i = 0; i < (level); i++)
			mb.yellow("-");
		if (level > 0)
			mb.append(ENTER_TREE + ' ');

		mb.yellow(module.getName());
		if (module.hasModules())
			mb.append(':');
		for (Reloader subModule : module.getModules())
			nextTree(mb, subModule, level + 1);
	}

}
