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

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;

public class CoreLocale extends NoxLocale {

	public static final CoreLocale GROUP_TAG_PREFIX;
	public static final CoreLocale GROUP_TAG_SUFFIX;
	public static final CoreLocale SPECIAL_CHAT_PINGCOLOR;
	public static final CoreLocale SPECIAL_CHAT_PINGSYMBAL;

	static {
		GROUP_TAG_PREFIX = new CoreLocale("special.ptag.prefix", "");
		GROUP_TAG_SUFFIX = new CoreLocale("special.ptag.suffix", "");
		SPECIAL_CHAT_PINGCOLOR = new CoreLocale("special.chatping.color", "&2");
		SPECIAL_CHAT_PINGSYMBAL = new CoreLocale("special.chatping.symbal", "@");
	}

	public CoreLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxCore.getInstance();
	}
}
