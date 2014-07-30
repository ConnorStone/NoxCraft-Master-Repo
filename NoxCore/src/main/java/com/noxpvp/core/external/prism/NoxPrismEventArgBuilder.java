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

package com.noxpvp.core.external.prism;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;

public class NoxPrismEventArgBuilder extends MessageBuilder {
	private static final String listKW = "FORCELIST";
	
	public NoxPrismEventArgBuilder() {
		super();
	}
	
	public void withArg(String key, String arg) {
		append(key + ":" + arg + ",");
	}
	
	public void withList(String key, String... args) {
		HashSet<String> newArgs = new HashSet<String>();
		
		for (String s : args)
			newArgs.add(s);
		
		if (newArgs.size() > 0)
			withList(key, newArgs);		
	}
	
	public void withList(String key, Collection<String> args) {
		append(listKW + key + ":" + StringUtil.join("|", args) + ",");
	}
	
	public String build() {
		String s = toString();
		if (s.charAt(s.length() - 1) == ',');
			s = (s.substring(0, s.length() - 1));
		
		return s;
	}
	
	public static Map<String, Object> getMapFromBuilder(String message) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		final String orig = message;
		String[] splits = orig.split(",");
		
		for (int i = 0; i < splits.length; i++) {
			String pair = splits[i];
			
			String[] keyValue = pair.split(":");
			
			String[] chanceArray;
			if (keyValue[0].startsWith(listKW)) {
				chanceArray = keyValue[1].split("\\|");
				ret.put(keyValue[0].replace(listKW, ""), chanceArray);
			} else 
				ret.put(keyValue[0], keyValue[1]);
		}
		
		return ret;
	}

}
