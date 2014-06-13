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

package com.noxpvp.mmo.command.subcommands;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.util.NoxMMOMessageBuilder;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassInfoCommand extends BaseCommand {

	public static final String COMMAND_NAME = "info";

	public ClassInfoCommand() {
		super(COMMAND_NAME, false);
	}

	public String[] getFlags() {
		return blankStringArray;
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public CommandResult execute(CommandContext context) throws NoPermissionException {

		if (!context.hasArgument(0))
			return new CommandResult(this, false);

		String className = context.getArgument(0).toLowerCase();

		if (!PlayerClassUtil.hasClass(className))
			return new CommandResult(this, false);

		PlayerClass clazz = null;
		for (PlayerClass c : PlayerClassUtil.getAllowedPlayerClasses(context.getPlayer()))
			if (c.getName().equalsIgnoreCase(className)) {
				clazz = c;
				break;
			}

		if (clazz == null)
			return new CommandResult(this, false);

		NoxMMOMessageBuilder mb = new NoxMMOMessageBuilder(getPlugin());
		mb.commandHeader(clazz.getDisplayName() + " Class", true);

		mb.withClassInfo(clazz).headerClose(true);
		mb.send(context.getSender());

		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
