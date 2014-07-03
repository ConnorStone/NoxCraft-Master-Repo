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

package com.noxpvp.core.utils;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.localization.GlobalLocale;

public class NoxMessageBuilder extends MessageBuilder {

	private NoxPlugin plugin;

	public NoxMessageBuilder(NoxPlugin plugin) {
		this(plugin, false);
	}

	public NoxMessageBuilder(NoxPlugin plugin, boolean withHeader) {
		super();

		this.plugin = plugin;
		if (withHeader)
			helpHeader(true);
	}

	public NoxMessageBuilder commandHeader(String command) {
		return commandHeader(command, true);
	}

	public NoxMessageBuilder commandHeader(String command, boolean addNewline) {
		append(GlobalLocale.COMMAND_HELP_HEADER.get(plugin.getName(), command));
		if (addNewline)
			newLine();

		return this;
	}

	public NoxMessageBuilder helpHeader() {
		return helpHeader(false);
	}

	public NoxMessageBuilder helpHeader(boolean addNewline) {
		append(GlobalLocale.HELP_HEADER.get(plugin.getName()));
		if (addNewline)
			newLine();

		return this;
	}

	public NoxMessageBuilder headerClose() {
		return headerClose(false);
	}

	public NoxMessageBuilder headerClose(boolean newlineFirst) {
		if (newlineFirst)
			newLine();

		return (NoxMessageBuilder) append(GlobalLocale.HEADER_CLOSE.get());
	}

	public NoxMessageBuilder withCommand(BaseCommand command) {
		return withCommand(command, false);
	}

	public NoxMessageBuilder withCommand(BaseCommand command, boolean addNewline) {
		gold("/")
				.yellow(command.getFullName())
				.append(" ");

		if (command.getFlags().length > 0)
			inBrackets(command.getFlags());

		if (addNewline)
			newLine();

		return this;
	}

	public NoxMessageBuilder inBrackets(String... text) {
		return (NoxMessageBuilder) gold("[").yellow(StringUtil.join(", ", text)).gold("]");
	}

	private NoxMessageBuilder cast(MessageBuilder mb) {
		if (mb instanceof NoxMessageBuilder)
			return (NoxMessageBuilder) mb;
		else
			throw new IllegalArgumentException("Cannot cast a real MessageBuilder fool!");
	}

}
