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

package com.noxpvp.mmo.prism;

import java.util.Map;

import org.bukkit.ChatColor;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;

import me.botsko.prism.actions.GenericAction;

public class AbilityUseAction extends GenericAction {

	@Override
	public String getNiceName() {
		if (type.getName() == UsedAbilityActionType.name && data != null && !data.isEmpty()) {
			MessageBuilder ret = new MessageBuilder();
			Map<String, Object> args = MMOPrismEventArgBuilder.getMapFromBuilder(data);
			
			String ability, target, damage, heal;
			String[] multiTarget;

			//Ability
			if ((ability = (String) args.get(MMOPrismEventArgBuilder.ABILITY_ARG)) != null)
				ret.gold(ability);
			
			//Target
			if ((target = (String) args.get(MMOPrismEventArgBuilder.TARGET_ARG)) != null)
				ret.white(" on ").gold(target);
			
			//Damage
			if ((damage = (String) args.get(MMOPrismEventArgBuilder.DAMAGE_ARG)) != null)
				ret.white(", damage: ").gold(damage); 
			
			//Heal
			if ((heal = (String) args.get(MMOPrismEventArgBuilder.HEAL_ARG)) != null)
				ret.white(", heal: ").gold(heal); 

			//Multi-target
			if ((multiTarget = (String[]) args.get(MMOPrismEventArgBuilder.TARGET_MULTIPLE_ARG)) != null)
				ret.white(". effected: ").gold(StringUtil.join(ChatColor.WHITE + ", " + ChatColor.GOLD, multiTarget));
			
			if (!ret.isEmpty())
				return ret.toString();
		}
		return "REPORT TO STAFF - UNKNOWN_LOG: " + data;
	}
	
}
