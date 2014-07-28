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
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;
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

		String className = null;
		if (context.hasArgument(0)) className = context.getArgument(0).toLowerCase();

		IPlayerClass clazz = null;

		if (className != null && !PlayerClassUtil.hasClass(className)) {
			return new CommandResult(this, false);
		}

		if (className != null) {
			for (PlayerClass c : PlayerClassUtil.getAllowedPlayerClasses(context.getPlayer()))
				if (c.getName().equalsIgnoreCase(className)) {
					clazz = c;
					break;
				}
		}


		NoxMMOMessageBuilder mb = new NoxMMOMessageBuilder(getPlugin());

		if (clazz != null) {
			mb.commandHeader(clazz.getDisplayName() + " Class", true);

			mb.withClassInfo(clazz).headerClose(true);
			mb.send(context.getSender());
		} else {
			MMOPlayer p = MMOPlayerManager.getInstance().getPlayer(context.getPlayer());

			mb.commandHeader(p.getPrimaryClass().getDisplayName() + " Class", true);

			mb.withClassInfo(p.getPrimaryClass()).headerClose(true);
			mb.headerClose();
			mb.newLine();
			mb.commandHeader(p.getSecondaryClass().getDisplayName() + " Class", true);

			mb.withClassInfo(p.getSecondaryClass()).headerClose(true);
			mb.headerClose();

			mb.send(context.getSender());
		}

		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
