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

import com.noxpvp.core.external.prism.BaseNoxPrismEvent;
import com.noxpvp.core.utils.PrismUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.internal.TieredAbility;
import com.noxpvp.mmo.abilities.internal.TargetedAbility;
import org.bukkit.entity.Player;

public class AbilityUsePrismEvent extends BaseNoxPrismEvent {
	
	public static void trigger(Player player, AbilityResult abrs) {
		MMOPrismEventArgBuilder b = new MMOPrismEventArgBuilder();
		
		TieredAbility ab = abrs.getAbility();
		b.withAbility(ab);
		/*
		if (ab instanceof DamagingAbility) {
			double damage;
			if ((damage = ((DamagingAbility) ab).getDamage()) > 0)
				b.withDamage(damage);
			
			List<Entity> effected;
			if ((effected = ((BaseEntityAbility) ab).getEffectedEntities()).size() > 0)
				b.withEffectEntities(effected);
		}
		*/ //FIXME: Reimplement the damage code.
		
		if (ab instanceof TargetedAbility) {
			b.withTarget(((TargetedAbility) ab).getTarget());
		}
		
		
		PrismUtil.callCustomPrismEvent(new AbilityUsePrismEvent(player, b.build()));
	}
	
	public AbilityUsePrismEvent(Player player, String message) {
		super(NoxMMO.getInstance(), UsedAbilityActionType.name, player, message);
	}

}
