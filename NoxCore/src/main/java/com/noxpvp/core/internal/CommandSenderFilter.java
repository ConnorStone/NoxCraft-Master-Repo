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

package com.noxpvp.core.internal;

import com.bergerkiller.bukkit.common.filtering.Filter;
import org.bukkit.command.CommandSender;

public abstract class CommandSenderFilter<T extends CommandSender> implements Filter {

	public final boolean isFiltered(Object t) {
		if (t instanceof CommandSender) return isFiltered((T) t);
		else return nonMatch();
	}

	protected abstract boolean nonMatch();

	public abstract boolean isFiltered(T t);
}
