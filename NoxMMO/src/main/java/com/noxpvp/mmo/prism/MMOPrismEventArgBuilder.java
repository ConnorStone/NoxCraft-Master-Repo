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

import com.noxpvp.core.external.prism.NoxPrismEventArgBuilder;
import com.noxpvp.mmo.abilities.internal.Ability;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
//FIXME: some kind of API addition for damage entities of a Map<LivingEntity, Double> of (DamagedEntities, DamageTaken)
public class MMOPrismEventArgBuilder extends NoxPrismEventArgBuilder {
	public static final String ABILITY_ARG = "ability";
	public static final String TARGET_ARG = "target";
	public static final String TARGET_MULTIPLE_ARG = "target-multiple";
	public static final String DAMAGE_ARG = "damage";
	public static final String HEAL_ARG = "heal";
	
	public void withAbility(Ability ab) {
		withArg(ABILITY_ARG, ab.getDisplayName(ChatColor.GOLD));
	}
	
	public void withTarget(LivingEntity target) {
		String tName;
		
		if (target instanceof Player)
			tName = MMOPlayerManager.getInstance().getPlayer((Player) target).getNoxPlayer().getFullName();
		else tName = target.getType().name().toLowerCase();
		
		withArg(TARGET_ARG, tName);
	}

	public void withDamage(double damage) {
		withArg(DAMAGE_ARG, String.format("%.2f", damage));
	}
	
	public void withHeal(double heal) {
		withArg(HEAL_ARG, String.format("%.2f", heal));
	}
	
	public void withEffectEntities(List<Entity> ents) {
		List<String> additions = new ArrayList<String>();
		
		for (Entity e : ents) {
			String name;
			
			if (e instanceof Player)
				name = MMOPlayerManager.getInstance().getPlayer((Player) e).getNoxPlayer().getFullName();
			else name = e.getType().name().toLowerCase();
			
			if (name != null && !name.isEmpty())
				additions.add(name);
		}
		
		if (additions.size() > 0)
			withList(TARGET_MULTIPLE_ARG, additions);
	}

}
