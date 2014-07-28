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

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.locale.MMOLocale;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NoxPVP
 */
public class BankShotPlayerAbility extends BasePlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "bank-shot";
	private static final String ABILITY_NAME = "Bank Shot";
	private BaseMMOEventHandler<ProjectileHitEvent> hitHandler;
	private BaseMMOEventHandler<ProjectileLaunchEvent> launchHandler;
	private List<Arrow> arrows = new ArrayList<Arrow>();
	private boolean firing = false;
	private boolean active = false;
	private long firingTicks = 100L; //5 seconds default
	private int range = 20;
	private boolean hitPlayers = true;
	private boolean hitCreatures = true;
	private boolean hitSelf = false;
	private boolean singleShot = true;

	public BankShotPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);

		hitHandler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public void execute(ProjectileHitEvent event) {
				if (event.getEntity() instanceof Arrow && BankShotPlayerAbility.this.arrows.contains((Arrow)event.getEntity()))
					BankShotPlayerAbility.this.eventExecute((Arrow) event.getEntity());
			}

			public Class<ProjectileHitEvent> getEventType() {
				return ProjectileHitEvent.class;
			}

			public String getEventName() {
				return "ProjectileHitEvent";
			}
		};

		launchHandler = new BaseMMOEventHandler<ProjectileLaunchEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileLaunchEvent").toString(),
				EventPriority.MONITOR, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public Class<ProjectileLaunchEvent> getEventType() {
				return ProjectileLaunchEvent.class;
			}

			public String getEventName() {
				return "ProjectileLaunchEvent";
			}

			public void execute(ProjectileLaunchEvent event) {
				if (event.getEntityType() != EntityType.ARROW)
					return;

				Arrow arrow = (Arrow) event.getEntity();
				if (!(arrow.getShooter() instanceof Player))
					return;

				Player shooter = (Player) arrow.getShooter();
				if (!shooter.equals(BankShotPlayerAbility.this.getPlayer()))
					return;

				if (isSingleShotMode() && !arrows.isEmpty())
					arrows.clear();

				arrows.add(arrow);
				setActive(true);

				if (isSingleShotMode())
					setFiring(false);
			}
		};
	}

	public void eventExecute(Arrow a) {

		if (!this.arrows.contains(a))
			return;

		if (!(a.getShooter() instanceof Player))
			return;

		Entity e = null;

		for (Entity it : a.getNearbyEntities(20, 20, 20)) {

			if (!(it instanceof LivingEntity || it == a)) continue;

			if (!hitPlayers && it instanceof Player) continue;

			if (!hitSelf && it == a.getShooter()) continue;

			if (!hitCreatures && it instanceof Creature) continue;

			Entity losChecker = a.getWorld().spawnEntity(a.getLocation(), EntityType.BAT);

			((LivingEntity) losChecker).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5, 1));

			if (!((LivingEntity) losChecker).hasLineOfSight(it)) {
				losChecker.remove();
				continue;
			}
			losChecker.remove();

			e = it;
			break;
		}
		if (e == null)
			return;

		Location pLoc = a.getLocation();

		new ParticleRunner(ParticleType.explode, e, false, 0, 2, 3).start(0);
		a.setVelocity(e.getLocation().toVector().subtract(pLoc.toVector()));

		return;
	}

	public long getFiringTicks() {
		return firingTicks;
	}

	public BankShotPlayerAbility setFiringTicks(long l) {
		firingTicks = l;
		return this;
	}

	public double getFiringSeconds() {
		return (firingTicks / 20);
	}

	public BankShotPlayerAbility setFiringSeconds(double seconds) {
		return setFiringTicks(Math.round(seconds * 20));
	}

	public boolean isSingleShotMode() {
		return this.singleShot;
	}

	public BankShotPlayerAbility setSingleShotMode(boolean single) {
		this.singleShot = single;
		return this;
	}

	/**
	 * Tells whether or not we are actively using ability.
	 *
	 * @return true or false if we are listening for arrow hit events.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets wheter or not the listener is actively Listening.
	 * <br/>
	 * <b>This is mostly an internal method.</b>
	 *
	 * @param active what to set to.
	 * @return this
	 */
	public BankShotPlayerAbility setActive(boolean active) {
		boolean changed = this.active != active;
		this.active = active;

		MasterListener m = NoxMMO.getInstance().getMasterListener();

		if (changed)
			if (active)
				m.registerHandler(hitHandler);
			else
				m.unregisterHandler(hitHandler);

		return this;
	}

	/**
	 * Tells whether or not we are actively shooting arrows bot.
	 *
	 * @return true or false if we are listing for bow shoot events.
	 */
	public boolean isFiring() {
		return firing;
	}

	/**
	 * Sets whether or not the bow shot listener is actively listening.
	 * <br/>
	 * <b>This is mostly an internal method.</b>
	 *
	 * @param firing what to set to.
	 * @return this
	 */
	public BankShotPlayerAbility setFiring(boolean firing) {
		boolean changed = this.firing != firing;
		this.firing = firing;

		MasterListener m = NoxMMO.getInstance().getMasterListener();

		if (changed)
			if (firing)
				m.registerHandler(launchHandler);
			else
				m.unregisterHandler(launchHandler);

		return this;

	}

	/**
	 * @return Integer The currently set range for this ability instance
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @param range The Integer range that the ricochet'd arrow should look for arrows target
	 * @return BankShotAbility This instance, used for chaining
	 */
	public BankShotPlayerAbility setRange(int range) {
		this.range = range;
		return this;
	}

	/**
	 * @return Boolean if the ricochet'd arrow will look for players as targets
	 */
	public boolean isHitPlayers() {
		return hitPlayers;
	}

	/**
	 * @param hitPlayers Boolean is the ricochet'd arrow should look for players as targets
	 * @return
	 */
	public BankShotPlayerAbility setHitPlayers(boolean hitPlayers) {
		this.hitPlayers = hitPlayers;
		return this;
	}

	/**
	 * @return Boolean If the ricochet'd arrow will look for Creature types as targets
	 */
	public boolean isHitCreatures() {
		return hitCreatures;
	}

	/**
	 * @param hitCreatures Boolean if the ricochet'd arrow should look for Creature types as targets
	 * @return
	 */
	public BankShotPlayerAbility setHitCreatures(boolean hitCreatures) {
		this.hitCreatures = hitCreatures;
		return this;
	}

	/**
	 * @return Boolean If the ricochet'd arrow can consider the shooter as arrows target
	 */
	public boolean isHitSelf() {
		return hitSelf;
	}

	/**
	 * @param hitSelf Boolean if the ricochet'd arrow should consider the shooter as arrows target
	 * @return BankShotAbility this instance, used for chaining
	 */
	public BankShotPlayerAbility setHitSelf(boolean hitSelf) {
		this.hitSelf = hitSelf;
		return this;
	}

	public AbilityResult<BankShotPlayerAbility> execute() {
		if (!mayExecute())
			return new AbilityResult<BankShotPlayerAbility>(this, false);

		if (!isFiring() && !isActive()) {
			setFiring(true);

			return new AbilityResult<BankShotPlayerAbility>(this, true, MMOLocale.ABIL_ACTIVATED.get(getName()));
		} else {
			return new AbilityResult<BankShotPlayerAbility>(this, false, MMOLocale.ABIL_ALREADY_ACTIVE.get(getName()));
		}
	}

}
