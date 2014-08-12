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

import java.util.Set;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.commands.SafeNullPointerException;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.command.subcommands.AbilityBindCommand;
import com.noxpvp.mmo.command.subcommands.AbilityInfoCommand;
import com.noxpvp.mmo.command.subcommands.AbilityListCommand;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class AbilityCommand extends BaseCommand {
	
	public static final String		COMMAND_NAME	= "ability";
	
	private static final String[]	flags			= new String[] { "h", "help" };
	
	public AbilityCommand() {
		super(COMMAND_NAME, true);
		registerSubCommand(new AbilityBindCommand());
		registerSubCommand(new AbilityInfoCommand());
		registerSubCommand(new AbilityListCommand());
	}
	
	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {
		
		if (!context.hasArgument(0))
			return new CommandResult(this, false);
		
		final String abilityName = StringUtil.join(" ", context.getArguments());
		
		final MMOPlayer mPlayer = MMOPlayerManager.getInstance().getPlayer(
				context.getPlayer());
		
		if (mPlayer == null)
			return new CommandResult(this, true, new MessageBuilder().red(
					"mPlayer object is null!").lines());
		
		final Set<BasePlayerAbility> abilities = mPlayer.getAbilities();
		
		BasePlayerAbility ability = null;
		if ((ability = mPlayer.getAbility(abilityName)) == null)
			throw new SafeNullPointerException("Ability \"" + abilityName
					+ "\" does not exist!");
		
		ability.execute();
		
		return new CommandResult(this, true);
	}
	
	public String[] getFlags() {
		return flags;
	}
	
	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
	
}
