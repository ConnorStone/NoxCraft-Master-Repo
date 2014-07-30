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

import com.noxpvp.core.utils.NoxMessageBuilder;

public interface Command {

	public String[] getAliases();

	public void displayHelp(ICommandContext sender);

	public NoxMessageBuilder onPreDisplayMessage(NoxMessageBuilder message, ICommandContext context);

	public NoxMessageBuilder onPostDisplayHelp(NoxMessageBuilder message, ICommandContext context);

	public CommandResult executeCommand(CommandContext context) throws NoPermissionException;

	public String[] getFlags();

	public int getMinArguments();

	public int getMaxArguments();

	public String getName();

	public Command getParent();

	public Command getRoot();

	public boolean hasArgumentLimit();

	public boolean isPlayerOnly();

	public boolean isRoot();

	public static class CommandResult {
		public final boolean success;
		public final BaseCommand executer;
		public final String[] extraMessages;

		public CommandResult(BaseCommand executer, boolean success, String... msgs) {
			this.executer = executer;
			this.success = success;
			this.extraMessages = msgs;
		}
	}
}
