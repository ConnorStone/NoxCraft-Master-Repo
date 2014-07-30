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

package com.noxpvp.mmo.locale;

import com.noxpvp.core.localization.NoxLocale;
import com.noxpvp.mmo.NoxMMO;

public class MMOLocale extends NoxLocale {

	//Error
	/**
	 * @param 1 class
	 */
	public static final MMOLocale CLASS_NONE_BY_NAME;

	/**
	 * @param 1 class
	 * @param 2 reason
	 */
	public static final MMOLocale CLASS_LOCKED;

	/**
	 * @param 1 class
	 * @param 2 reason
	 */
	public static final MMOLocale CLASS_TIER_LOCKED;

	/**
	 * @param 1 class
	 * @param 2 tier
	 */
	public static final MMOLocale CLASS_NO_TIER;

	/**
	 * @param 1 ability
	 * @param 2 remaining time
	 */
	public static final MMOLocale ABIL_ON_COOLDOWN;

	/**
	 * @param 1 ability
	 * @param 2 target
	 */
	public static final MMOLocale ABIL_BAD_TARGET;

	/**
	 * @param none
	 */
	public static final MMOLocale ABIL_NO_TARGET;

	/**
	 * @param 1 ability
	 * @param 2 town/region/other name
	 */
	public static final MMOLocale ABIL_NO_PVP_ZONE;

	/**
	 * @param 1 ability
	 */
	public static final MMOLocale ABIL_NO_PVP;
	
	/**
	 * @param 1 amount
	 * @param 2 Material
	 */
	public static final MMOLocale ABIL_NOT_ENOUGH_REGENT;
	
	/**
	 * @param 1 max distance
	 */
	public static final MMOLocale ABIL_RANGED_TOO_FAR;
	
	/**
	 * @param 1 ability
	 */
	public static final MMOLocale ABIL_NO_LOS;
	
	/**
	 * @param 1 ability
	 */
	public static final MMOLocale ABIL_ALREADY_ACTIVE;
	
	/**
	 * @param 1 Ability name
	 */
	public static final MMOLocale ABIL_NOT_FOUND;

	//Display
	public static final MMOLocale CLASS_DN;
	public static final MMOLocale CLASS_DISPLAY_NAME;
	public static final MMOLocale ABIL_DISPLAY_NAME;

	/**
	 * @param 1 ability
	 */
	public static final MMOLocale ABIL_USE;

	/**
	 * @param 1 ability
	 * @param 2 target
	 */
	public static final MMOLocale ABIL_USE_TARGET;

	/**
	 * @param 1 ability
	 * @param 2 target
	 * @param 3 damage amount (format %.2f please)
	 */
	public static final MMOLocale ABIL_USE_TARGET_DAMAGED;
	
	/**
	 * @param 1 ability
	 */
	public static final MMOLocale ABIL_ACTIVATED;
	
	/**
	 * @param 1 ability
	 */
	public static final MMOLocale ABIL_DEACTIVATED;

	/**
	 * @param 1 ability
	 */
	public static final MMOLocale ABIL_HIT;

	/**
	 * @param 1 attacker
	 * @param 2 ability
	 */
	public static final MMOLocale ABIL_HIT_ATTACKER;

	/**
	 * @param 1 attacker
	 * @param 2 ability
	 * @param 3 damage amount (format %.2f please)
	 */
	public static final MMOLocale ABIL_HIT_ATTACKER_DAMAGED;

	/**
	 * @param 1 Ability name
	 * @param 2 Description
	 */
	public static final MMOLocale ABIL_INFO;

	//Guis
	public static final MMOLocale GUI_MENU_NAME_COLOR;
	public static final MMOLocale GUI_BAR_COLOR;

	//INTERNAL
	/**
	 * @deprecated This is used internally. Do not use!
	 */
	public static final MMOLocale ABIL_DN;




	static {
		//Error
		CLASS_NONE_BY_NAME = new MMOLocale("error.class.none-by-name", "&cThere is no class by the name of &e%0%");
		CLASS_LOCKED = new MMOLocale("error.class.locked", "&cThe class &e%0% &cis locked: &e%1%");
		CLASS_TIER_LOCKED = new MMOLocale("error.class.tier.locked", "&cThe &e%0% &ctier is locked: &e%1%");
		CLASS_NO_TIER = new MMOLocale("error.class.tier.missing", "&cThe class &e%0% &cdoes not have a &e%1% &ctier");
		ABIL_ON_COOLDOWN = new MMOLocale("error.ability.on-cooldown", "&cThe &e%0% &cability has a cooldown of &e%1% &cremaining");
		ABIL_BAD_TARGET = new MMOLocale("error.ability.bad-target", "&cThe &e%0% &cability cannot be used on &e%1% &cright now");
		ABIL_NO_TARGET = new MMOLocale("error.ability.no-target", "&cCould not find a target");
		ABIL_NO_PVP_ZONE = new MMOLocale("error.ability.no-pvp-zone-named", "&cYou can't use the &e%0% &cability in &e%1%");
		ABIL_NO_PVP = new MMOLocale("error.ability.no-pvp-zone", "&cYou can't use the &e%0% &cability here");
		ABIL_NOT_ENOUGH_REGENT = new MMOLocale("error.ability.not-enough-regent", "&cYou need atleast &e%0% &6%1% &cto use that!");
		ABIL_RANGED_TOO_FAR = new MMOLocale("error.ability.ranged-too-far", "&cYou have to be within &e%0% &cblocks");
		ABIL_NO_LOS = new MMOLocale("error.ability.no-los", "&cYou need line of sight to use the &e%0% &cability!");
		ABIL_ALREADY_ACTIVE = new MMOLocale("error.ability.already-active", "&e%0% &cis already active");
		ABIL_NOT_FOUND = new MMOLocale("error.ability.not-found", "&cThe ability &e%0% &ccould not be found");

		//Display
		CLASS_DN = new MMOLocale("display.class.default", "%1%%0%");
		CLASS_DISPLAY_NAME = new MMOLocale("display.class", "");
		ABIL_DN = new MMOLocale("display.ability.default", "%1%%0%");
		ABIL_DISPLAY_NAME = new MMOLocale("display.ability", "");
		ABIL_INFO = new MMOLocale("display.ability.info", "&e%0%&6: &c%1%");
		ABIL_USE = new MMOLocale("display.ability.used", "&6You used the &e%0% &6ability!");
		ABIL_USE_TARGET = new MMOLocale("display.ability.used-on-target", "&6You used the &e%0% &6ability on &c%1%");
		ABIL_USE_TARGET_DAMAGED = new MMOLocale("display.ability.used-on-target-damaged", "&6You used the &e%0% &6ability on &c%1%&6, dealing &c%2% &6damage!");
		ABIL_ACTIVATED = new MMOLocale("display.ability.activated", "&e%0% &6Activated!");
		ABIL_DEACTIVATED = new MMOLocale("display.ability.deactivated", "&e%0% &6Deactivated");
		ABIL_HIT = new MMOLocale("display.ability.hit", "&6You were hit by &e%1%&6!");
		ABIL_HIT_ATTACKER = new MMOLocale("display.ability.hit-attacker", "&c%0% &6used &e%1% &6on you!");
		ABIL_HIT_ATTACKER_DAMAGED = new MMOLocale("display.ability.hit-attacker-damaged", "&c%0% &6used &e%1% &6on you! dealing &c%2% &cdamage");


		//GUIs
		GUI_MENU_NAME_COLOR = new MMOLocale("gui.inventory.name_color", "&e");
		GUI_BAR_COLOR = new MMOLocale("gui.target_bar.color", "&a");

	}

	public MMOLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
}
