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

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ICommandContext {
	public String getArgument(int arg);

	public int getArgumentCount();

	public String[] getArguments();

	public Object getFlag(String flag);

	public <T> T getFlag(String flag, Class<T> clazz);

	public <T> T getFlag(String flag, T def);

	public int getFlagCount();

	public Map<String, Object> getFlags();

	public Player getPlayer();

	public CommandSender getSender();

	public boolean hasArgument(int argNumber);

	public boolean hasFlag(String flag);

	public boolean isPlayer();
}
