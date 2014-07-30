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

package com.noxpvp.core.utils.gui;

import org.bukkit.ChatColor;

public class ColoredStringScroller {

	StringBuilder text;
	String curColor;
	String curFormat;
	char cChar;

	public ColoredStringScroller(String text) {
		this.text = new StringBuilder(text);

		cChar = ChatColor.COLOR_CHAR;
		curColor = "";
		curFormat = "";
	}

	public String getString() {
		return text.toString();
	}

	public String setCurrent(String code) {
		if (code.matches("[&" + cChar + "][0-9a-fr]")) {
			curFormat = "";
			return (curColor = code);

		} else if (code.matches("[&" + cChar + "][kmlno]")) {
			return (curFormat = code);

		}

		return "";
	}

	public String scroll() {
		while (text.substring(0, 2).matches("[&" + cChar + "][0-9a-frkmlno]")) {
			text.append(setCurrent(text.substring(0, 2))).delete(0, 2);

		}

		text.append(text.charAt(0)).deleteCharAt(0);
		return MessageUtil.parseColor(curColor + curFormat + text.toString());
	}

}
