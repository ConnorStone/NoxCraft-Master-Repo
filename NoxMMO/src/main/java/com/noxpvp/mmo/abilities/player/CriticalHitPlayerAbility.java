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

package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class CriticalHitPlayerAbility extends BasePlayerAbility implements IPassiveAbility<EntityDamageByEntityEvent>, PVPAbility {

	public static final String PERM_NODE = "critical-hit";
	public static final String ABILITY_NAME = "Critical Hit";
	private MMOPlayerManager pm;

	public CriticalHitPlayerAbility(Player p) {
		super(ABILITY_NAME, p);

		this.pm = MMOPlayerManager.getInstance();
	}

	@Override
	public String getDescription() {
		return "A random chance to land a critical hit, causing nausia and increased damage on the target";
	}

	public AbilityResult execute(EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player playerAttacker = (Player) ((event.getDamager() instanceof Player) ? event.getDamager() : null);


		if (playerAttacker == null || !playerAttacker.equals(getPlayer()))
			return new AbilityResult(this, false);
		
		String itemName = playerAttacker.getItemInHand().getType().name().toUpperCase();
		if (!itemName.contains("SWORD") && !itemName.contains("AXE"))
			return new AbilityResult(this, false);

		MMOPlayer player = pm.getPlayer(getPlayer());

		if (player == null)
			return new AbilityResult(this, false);

		IPlayerClass clazz = player.getPrimaryClass();

		double damage = (clazz.getLevel() + clazz.getTotalLevel()) / 75;
		if ((Math.random() * 100) > (damage * 45))
			return new AbilityResult(this, false);

		event.setDamage(damage);

		if (event.getEntity() instanceof LivingEntity)
			((LivingEntity) event.getEntity()).addPotionEffect(
					new PotionEffect(PotionEffectType.CONFUSION, 40, 2, false));

		return new AbilityResult(this, true);
	}

	/**
	 * Always Returns True Due To Being Passive!
	 */
	public AbilityResult execute() {
		return new AbilityResult(this, true);
	}

}
