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

package com.noxpvp.mmo.abilities.targeted;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.core.data.Vector3D;
import com.noxpvp.core.gui.corebar.LivingEntityTracker;
import com.noxpvp.mmo.OldMMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.abilities.SilentAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;

public class TargetPlayerAbility extends BaseRangedPlayerAbility implements IPassiveAbility<PlayerInteractEvent>, SilentAbility {

	public static final String ABILITY_NAME = "Target";
	public static final String PERM_NODE = "target";

	private Reference<LivingEntity> target_ref;

	/**
	 * @param player - The Player type user for this ability instance
	 */
	public TargetPlayerAbility(Player player) {
		super(ABILITY_NAME, player, 30);

		setCD(1);
	}

	/**
	 * @return Boolean - PassiveAbililty, return true
	 */
	public AbilityResult execute() {
		return new AbilityResult(this, true);
	}

	public AbilityResult execute(PlayerInteractEvent event) {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player p = getPlayer();
		double range = getRange();

		for (Entity it : p.getNearbyEntities(range, range, range)) {

			if (!(it instanceof LivingEntity) || it.equals(p) || !it.isValid() || it.isDead()) continue;
			if ((it instanceof Player) && !(p).canSee((Player) it)) continue;

			Location observerPos = p.getEyeLocation();
			Vector3D observerDir = new Vector3D(observerPos.getDirection());

			Vector3D observerStart = new Vector3D(observerPos);
			Vector3D observerEnd = observerStart.add(observerDir.multiply(range));

			Vector3D targetPos = new Vector3D(it.getLocation());
			Vector3D minimum = targetPos.add(-0.6, 0, -0.6);
			Vector3D maximum = targetPos.add(0.6, 2.0, 0.6);

			if (hasIntersection(observerStart, observerEnd, minimum, maximum)) {
				this.target_ref = new SoftReference<LivingEntity>((LivingEntity) it);

				MMOPlayerManager pm = MMOPlayerManager.getInstance();
				OldMMOPlayer mmoPlayer = pm.getPlayer(p), mmoIt = it instanceof Player ? pm.getPlayer((Player) it) : null;

				if (mmoPlayer == null)
					return new AbilityResult(this, false);

				mmoPlayer.setTarget(target_ref.get());

				String name;

				if (mmoIt != null) {
					IPlayerClass c = mmoIt.getPrimaryClass();

					if (c != null && c.getTier() != null) {
						name = mmoIt.getFullName() + LivingEntityTracker.color + LivingEntityTracker.separater + c.getTier().getDisplayName();
					} else name = mmoIt.getFullName();
				} else {
					if (it instanceof Player) name = ((Player) it).getName();
					else name = it.getType().name();
				}

				new LivingEntityTracker(p, target_ref.get(), name);

				return new AbilityResult(this, true);
			} else {
				continue;
			}
		}

		return new AbilityResult(this, false);
	}

	private boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
		final double epsilon = 0.0001f;

		Vector3D d = p2.subtract(p1).multiply(0.5);
		Vector3D e = max.subtract(min).multiply(0.5);
		Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
		Vector3D ad = d.abs();

		if (Math.abs(c.x) > e.x + ad.x)
			return false;
		if (Math.abs(c.y) > e.y + ad.y)
			return false;
		if (Math.abs(c.z) > e.z + ad.z)
			return false;

		if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
			return false;
		if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
			return false;
		return Math.abs(d.x * c.y - d.y * c.x) <= e.x * ad.y + e.y * ad.x + epsilon;

	}

}
