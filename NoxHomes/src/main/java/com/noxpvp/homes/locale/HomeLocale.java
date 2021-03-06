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

package com.noxpvp.homes.locale;

import com.noxpvp.core.locales.NoxLocale;
import com.noxpvp.homes.NoxHomes;

public class HomeLocale extends NoxLocale {
	public final static HomeLocale LIST_OWN;
	public final static HomeLocale LIST_OTHERS;
	                               
	public final static HomeLocale HOME_OWN;
	public final static HomeLocale HOME_OTHERS;
	                               
	public final static HomeLocale DELHOME_OWN;
	public final static HomeLocale DELHOME_INVALID;
	public final static HomeLocale DELHOME_OTHERS;
	                               
	public final static HomeLocale SETHOME_OWN;
	public final static HomeLocale SETHOME_OTHERS;
	                               
	public final static HomeLocale WARMUP;
	public final static HomeLocale COOLDOWN;
	
	public final static HomeLocale BAD_LOCATION;
	
	static {
		LIST_OWN 		= new HomeLocale("homes.list.own", "&3Your Homes&r: &e%1%");
		LIST_OTHERS 	= new HomeLocale("homes.list.default", "&e%0%'s &3homes: &e%1%");
		
		HOME_OWN 		= new HomeLocale("homes.home.own", "&3You teleported to home: %1%");
		HOME_OTHERS 	= new HomeLocale("homes.home.default", "&3You teleported to %0%'s home named &e%1%");
		
		DELHOME_OWN		= new HomeLocale("homes.delhome.own", "&cRemoved your home:&e%1%");
		DELHOME_INVALID = new HomeLocale("homes.delhome.invalid", "&cYour home \"%0%\" has been removed. /n&4Reason: %1%");
		DELHOME_OTHERS	= new HomeLocale("homes.delhome.default", "&cDeleted &e%0%'s&c home named &e%1%");
		
		SETHOME_OWN		= new HomeLocale("homes.sethome.own", "&aSet new home named &e%1%&a at &6%2%");
		SETHOME_OTHERS	= new HomeLocale("homes.sethome.default", "&aSet new home for &e%0%&2 &anamed: &e%1%&a at &6%2%");
		
		WARMUP		= new HomeLocale("homes.warmup", "&3Warmup started. &cDo not move!"); //%0% is the time to warmup in seconds
		COOLDOWN	= new HomeLocale("homes.cooldown", "&4Your too tired to get home.&e Wait another &4%0%&e seconds");
		
		BAD_LOCATION = new HomeLocale("homes.bad.location", "&4You cannot set homes here. Reason: %0%");
		
	}
	
	
	public HomeLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}

}
