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

package com.noxpvp.mmo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.abilities.internal.SilentAbility;
import com.noxpvp.mmo.events.ability.post.PostPlayerAbilityEvent;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.prism.AbilityUsePrismEvent;

public class AbilityListener extends NoxListener<NoxMMO> {
	
	private final boolean	       isPrismActive;
	private final MMOPlayerManager	pm;
	
	public AbilityListener(boolean isPrismActive) {
		this(NoxMMO.getInstance(), isPrismActive);
	}
	
	public AbilityListener(NoxMMO plugin, boolean isPrismActive) {
		super(plugin);
		
		this.isPrismActive = isPrismActive;
		pm = MMOPlayerManager.getInstance();
	}
	
	// TODO finish
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerAbilityExecuted(PostPlayerAbilityEvent event) {
		
		final Player p = event.getPlayer();
		final MMOPlayer mp = pm.getPlayer(p);
		final NoxPlayer np = mp.getNoxPlayer();
		final PlayerAbility ab = event.getAbility();
		final AbilityResult result = event.getResult();
		
		final boolean hasMessages = result.hasMessages();
		final boolean silent = ab instanceof SilentAbility;
		final boolean hasCD = ab.getCD().toStamp() > 0;
		
		if (result.isSuccessful()) {
			
			if (isPrismActive) {
				AbilityUsePrismEvent.trigger(p, result);
			}
			
			if (hasCD) {
				np.addCoolDown(ab.getName(), ab.getCD(), !silent);
			}
		}
		
		if (!silent)
			if (!hasMessages) {
				if (result.isSuccessful()) {
					MessageUtil.sendLocale(p, MMOLocale.ABIL_USE, ab.getName());
				}
			} else {
				p.sendMessage(MessageUtil.parseColor(StringUtil.join(" ", result
				        .getMessages())));
			}
	}
	
	public void onPlayerTargetedAbilityExecuted(PostTargettedPlayerAbilityEvent event) {
		final Player p = event.getPlayer();
		final MMOPlayer mp = pm.getPlayer(p);
		final NoxPlayer np = mp.getNoxPlayer();
		final TargetedPlayerAbility ab = event.getAbility();
		final AbilityResult result = event.getResult();
		
		final boolean hasMessages = result.hasMessages();
		final boolean silent = ab instanceof SilentAbility;
		final boolean hasCD = ab.getCD().toStamp() > 0;
		
		if (result.isSuccessful()) {
			
			if (isPrismActive) {
				AbilityUsePrismEvent.trigger(p, result);
			}
			
			if (hasCD) {
				np.addCoolDown(ab.getName(), ab.getCD(), !silent);
			}
		}
		
		final String target = ab.getTarget() instanceof Player ?
				pm.getPlayer((Player) ab.getTarget()).getNoxPlayer().getFullName() :
				ab.getTarget().getType().name().toLowerCase();
		
		if (!silent) {
			if (hasMessages) {
				p.sendMessage(MessageUtil.parseColor(StringUtil.join(" ", result
						.getMessages())));
			} else if (ab instanceof DamagingAbility) {
				final DamagingAbility dab = (DamagingAbility) ab;
				if (dab.getDamage() > 0 && result.isSuccessful()) {
					MessageUtil.sendLocale(p, MMOLocale.ABIL_USE_TARGET_DAMAGED, ab
							.getName(), target, String.format("%.2f", dab
							.getDamage()));
					
					if (ab.getTarget() instanceof Player) {
						MessageUtil.sendLocale((Player) ab.getTarget(),
								MMOLocale.ABIL_HIT_ATTACKER_DAMAGED, np
										.getFullName(), ab.getName(), String.format(
										"%.2f", dab.getDamage()));
					}
				}
			} else if (result.isSuccessful()) {
				MessageUtil.sendLocale(p, MMOLocale.ABIL_USE_TARGET, ab.getName(),
						target);
				
				if (ab.getTarget() instanceof Player) {
					MessageUtil.sendLocale((Player) ab.getTarget(),
							MMOLocale.ABIL_HIT_ATTACKER, np.getFullName(), ab
									.getName());
				}
			}
		}
		
		return;
	}
	
}
