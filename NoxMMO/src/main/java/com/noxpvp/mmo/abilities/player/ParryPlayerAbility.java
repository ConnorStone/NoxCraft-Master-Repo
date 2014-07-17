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

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NoxPVP
 */
public class ParryPlayerAbility extends BasePlayerAbility implements IPassiveAbility<EntityDamageByEntityEvent>, PVPAbility {

	public static final String ABILITY_NAME = "Parry";
	public static final String PERM_NODE = "parry";

	public List<Material> parriedWeapons = new ArrayList<Material>();

	private float percentChance;
	private boolean mustBlock;

	/**
	 * @param player The Player type user for this ability instance
	 */
	public ParryPlayerAbility(Player player) {
		super(ABILITY_NAME, player);

		this.mustBlock = false;
	}

	/**
	 * @return boolean If this ability is set to only succeed if the user is blocking with a sword
	 */
	public boolean isMustBlock() {
		return mustBlock;
	}

	/**
	 * @param mustBlock boolean if the ability should only succeed if the player is blocking with a sword
	 * @return ParryAbility This instance, used for chaining
	 */
	public ParryPlayerAbility setMustBlock(boolean mustBlock) {
		this.mustBlock = mustBlock;
		return this;
	}

	public AbilityResult execute() {
		return new AbilityResult(this, true);
	}


	public AbilityResult execute(EntityDamageByEntityEvent event) {
		if (event.getEntity() != getPlayer() || !mayExecute())
			return new AbilityResult(this, false);

		Player p = getPlayer();

		MMOPlayer mmoPlayer;

		if ((mmoPlayer = MMOPlayerManager.getInstance().getPlayer(p)) == null || (mustBlock && (!parriedWeapons.contains(p.getItemInHand().getType()))))
			return new AbilityResult(this, false);

		float chance = mmoPlayer.getPrimaryClass().getTotalLevel() / 6;
		percentChance = (chance <= 75) ? chance : 75;
		if (RandomUtils.nextFloat() > percentChance)
			return new AbilityResult(this, false);


		Entity damager = event.getDamager();
		if (damager instanceof Damageable) {
			((Damageable) damager).damage(event.getDamage() / .7, p);
		}

		return new AbilityResult(this, true);
	}

}
