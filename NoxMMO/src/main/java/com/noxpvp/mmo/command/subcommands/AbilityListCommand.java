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

import org.bukkit.ChatColor;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.utils.NoxMessageBuilder;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class AbilityListCommand extends BaseCommand {

	public static final String COMMAND_NAME = "list";

	public AbilityListCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return blankStringArray;
	}

	public int getMaxArguments() {
		return 0;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {

		MMOPlayer player = MMOPlayerManager.getInstance().getPlayer(context.getPlayer());
		NoxMessageBuilder mb = new NoxMessageBuilder(getPlugin()).commandHeader("Ability List", true);

		for (Ability ability : player.getAbilities()) {
			mb.yellow(ability.getDisplayName()).gold(": ").newLine();
			for (String lore : ability.getLore(ChatColor.RED, 30))
				mb.append(lore).newLine();
		}

		mb.headerClose();
		mb.send(context.getSender());

		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
