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

import com.noxpvp.core.commands.CommandContext;
import org.bukkit.command.CommandSender;

import java.util.*;

public class CommandUtil {
	private static final char escapeChar = '\\';

	private static final char flagChar = '-';

	private static List<Character> quoteChars = new ArrayList<Character>(Arrays.asList('\'', '"'));

	///WARNING THIS IS A COMPLEX FUNCTION DO NOT MODIFY WITHOUT PROPER TESTING!
	public static CommandContext parseCommand(CommandSender sender, String argLine) {
		Map<String, Object> flags = new LinkedHashMap<String, Object>();

		List<String> args = new ArrayList<String>();

		StringBuilder asb = new StringBuilder();
		StringBuilder fsb = new StringBuilder();
		StringBuilder fvsb = new StringBuilder();

		char[] r = argLine.toCharArray();

		boolean isQuoting = false, isFlag = false, isLongFlag = false, isFlagValue = false, isEscaped = false;
		char quoteChar = '\0';
		int i = 0;

		while (i < r.length) {
//			if (((i - 1) >= 0) && r[i-1] == ' ') 
			if (r[i] == flagChar && i+1 < r.length && ((((i - 1) >= 0) && r[i - 1] == ' ') || (i - 1 < 0))) {
				isFlag = true;
				if (r[i + 1] == flagChar) {
					isLongFlag = true;
					i++;
				}
				i++;
				while (i < r.length && (isFlag || isFlagValue)) {
					if (r[i] == ' ' && !isQuoting) {
						if (isFlag) {
							isFlag = false;
							isFlagValue = true;
						} else if (isFlagValue)
							isFlagValue = false;

						i++;
						if (r[i] == flagChar)
							break;
						if (!isFlag && !isFlagValue)
							break;
					} else if (r[i] == escapeChar) {
						if (!isEscaped)
							isEscaped = true;
						else if (isFlag)
							fsb.append(r[i]);
						else if (isFlagValue)
							fvsb.append(r[i]);
						else
							break;
						if (++i >= r.length)
							break;
						continue;
					} else if (isFlag) {
						fsb.append(r[i]);
						i++;
						continue;
					} else if (isFlagValue) {
						if (r[i] == ' ' && !isQuoting) {
							isFlagValue = false;
							continue;
						} else if (isEscaped) {
							fvsb.append(r[i]);
						} else if (r[i] == quoteChar && isQuoting) {
							isQuoting = false;
						} else if (quoteChars.contains(r[i]) && !isQuoting) {
							quoteChar = r[i];
							isQuoting = true;
						} else
							fvsb.append(r[i]);
						i++;
						continue;
					}
				}
				if (!isLongFlag)
					for (int i2 = 0; i2 < fsb.length(); i2++)
						if ((i2 + 1) == fsb.length() && fvsb.length() > 0)
							flags.put(String.valueOf(fsb.charAt(i2)), fvsb.toString());
						else
							flags.put(String.valueOf(fsb.charAt(i2)), true);
				else if (fvsb.length() > 0)
//					if (ParseUtil.isBool(fvsb.toString()))
//						flags.put(fsb.toString(), ParseUtil.parseBool(fvsb.toString()));
//					else if (ParseUtil.isNumeric(fvsb.toString()))
//						flags.put(fsb.toString(), ParseUtil.parseDouble(fvsb.toString(), 0));
//					else
					flags.put(fsb.toString(), fvsb.toString());
				else
					flags.put(fsb.toString(), true);

				fsb = new StringBuilder();
				fvsb = new StringBuilder();

				continue;
			} else {
				if (r[i] == ' ' && !isQuoting) {
					if (asb.length() > 0)
						args.add(asb.toString());
					asb = new StringBuilder();
				} else {
					if (isEscaped) {
						asb.append(r[i]);
					} else if (r[i] == quoteChar && isQuoting) {
						isQuoting = false;
					} else if (quoteChars.contains(r[i]) && !isQuoting) {
						quoteChar = r[i];
						isQuoting = true;
					} else
						asb.append(r[i]);
				}
				i++;
				continue;
			}
		}
		if (asb.length() > 0 && !args.contains(asb.toString())) //Safety catch.
			args.add(asb.toString());

		return new CommandContext(sender, flags, args.toArray(new String[args.size()]));
	}
}
