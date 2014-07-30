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

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.commands.SafeNullPointerException;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.manager.MMOPlayerManager;

import java.util.Map;

public class AbilityInfoCommand extends BaseCommand {

	public static final String COMMAND_NAME = "info";

	public AbilityInfoCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return blankStringArray;
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {

		if (!context.hasArgument(0))
			return new CommandResult(this, false);

		String abilityName = context.getArgument(0).toLowerCase();

		MMOPlayer mPlayer = MMOPlayerManager.getInstance().getPlayer(context.getPlayer());

		if (mPlayer == null)
			return new CommandResult(this, true, new MessageBuilder().red("mPlayer object is null!").lines());

		Map<String, Ability> abilities = mPlayer.getAbilitiesMap();

		Ability ability = null;
		if (abilities.containsKey(abilityName))
			ability = abilities.get(abilityName);

		if (ability == null)
			throw new SafeNullPointerException("Ability does not exist!");

		MessageUtil.sendLocale(context.getSender(), MMOLocale.ABIL_INFO, ability.getDisplayName(), ability.getDescription());
		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
