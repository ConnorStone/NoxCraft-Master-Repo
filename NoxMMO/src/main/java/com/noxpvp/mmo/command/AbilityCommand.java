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

package com.noxpvp.mmo.command;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.commands.SafeNullPointerException;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.command.subcommands.AbilityBindCommand;
import com.noxpvp.mmo.command.subcommands.AbilityInfoCommand;
import com.noxpvp.mmo.command.subcommands.AbilityListCommand;
import com.noxpvp.mmo.manager.MMOPlayerManager;

import java.util.Map;

public class AbilityCommand extends BaseCommand {

	public static final String COMMAND_NAME = "ability";

	private static final String[] flags = new String[]{"h", "help"};

	public AbilityCommand() {
		super(COMMAND_NAME, true);
		registerSubCommand(new AbilityBindCommand());
		registerSubCommand(new AbilityInfoCommand());
		registerSubCommand(new AbilityListCommand());
	}

	public String[] getFlags() {
		return flags;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {

		if (!context.hasArgument(0))
			return new CommandResult(this, false);

		String abilityName = StringUtil.join(" ", context.getArguments());

		MMOPlayer mPlayer = MMOPlayerManager.getInstance().getPlayer(context.getPlayer());

		if (mPlayer == null)
			return new CommandResult(this, true, new MessageBuilder().red("mPlayer object is null!").lines());

		Map<String, Ability> abilities = mPlayer.getAbilitiesMap();

		Ability ability = null;
		if (abilities.containsKey(abilityName))
			ability = abilities.get(abilityName);

		if (ability == null)
			throw new SafeNullPointerException("Ability \"" + abilityName + "\" does not exist!");

		ability.execute();

		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
