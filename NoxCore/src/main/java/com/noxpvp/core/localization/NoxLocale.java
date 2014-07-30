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

package com.noxpvp.core.localization;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.localization.LocalizationEnum;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.utils.gui.MessageUtil;

public abstract class NoxLocale extends LocalizationEnum {

	public NoxLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public String get(String... args) {
		return MessageUtil.parseColor(getPlugin().getLocale(getName(), args));
	}

	public final String getSafe(String... args) {
		return MessageUtil.parseArguments(MessageUtil.parseColor(get()), args);
	}

	public abstract NoxPlugin getPlugin();

	@Override
	public void message(CommandSender sender, String... args) {
		if (!isEmpty(args)) //Ignore blank messages.
			MessageUtil.sendMessage(sender, getSafe(args).split("(/n|\n)"));
	}

	public boolean isEmpty(String... args) {
		String m = get(args);
		return (m.isEmpty() || m.length() == 0);
	}
}
