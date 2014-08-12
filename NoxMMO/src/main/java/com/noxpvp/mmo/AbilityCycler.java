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

package com.noxpvp.mmo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.comphenix.attribute.AttributeStorage;
import com.noxpvp.core.Persistent;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.manager.AbilityCyclerManager;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.renderers.BaseAbilityCyclerRenderer;

public class AbilityCycler extends Cycler<String> implements
		Persistent, ConfigurationSerializable {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static ModuleLogger				logger;
	private static NoxListener<NoxMMO>		iHeld, iInteract;
	private static Map<UUID, AbilityCycler>	cyclers;
	
	// Serializers start
	private static final String				SERIALIZE_ID		= "id";
	private static final String				SERIALIZE_PLAYER_ID	= "player-id";
	private static final String				SERIALIZE_ABILITIES	= "abilities";
	// Serializers end
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final UUID						id;
	private final UUID						player;
	private Set<String>						abilities;
	private BaseAbilityCyclerRenderer		renderer;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public AbilityCycler(Collection<PlayerAbility> data, UUID playerID,
			ItemStack item) {
		super();
		
		player = playerID;
		id = UUID.randomUUID();
		AttributeStorage.newTarget(item, id).setData(id.toString());
		
		final List<String> abs = new ArrayList<String>();
		for (final PlayerAbility ab : data) {
			abs.add(ab.getName());
		}
		
		addAll(abs);
		
		register(this);
		AbilityCyclerManager.getInstance().loadObject(this);
	}
	
	public AbilityCycler(Map<String, Object> data) {
		super();
		
		Validate.isTrue(data.containsKey(SERIALIZE_ID)
				&& data.containsKey(SERIALIZE_PLAYER_ID)
				&& data.containsKey(abilities),
				"Data is missing required data from save!");
		
		Object getter;
		
		if ((getter = data.get(SERIALIZE_ID)) != null && getter instanceof String) {
			id = UUID.fromString((String) getter);
		} else {
			id = UUID.randomUUID();
		}
		
		if ((getter = data.get(SERIALIZE_PLAYER_ID)) != null
				&& getter instanceof String) {
			player = UUID.fromString((String) getter);
		} else
			throw new IllegalStateException(
					"Data contained key for player-id, but could not be make into a UUID!");
		
		if ((getter = data.get(SERIALIZE_ABILITIES)) != null
				&& getter instanceof Collection) {
			abilities = new HashSet<String>((Collection<String>) getter);
		} else {
			abilities = new HashSet<String>();
		}
		
		addAll(abilities);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static AbilityCycler getCycler(UUID id) {
		return cyclers.get(id);
	}
	
	// INITIALIZE
	public static void init() {
		
		cyclers = new HashMap<UUID, AbilityCycler>();
		
		iHeld = new NoxListener<NoxMMO>(NoxMMO.getInstance()) {
			
			@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
			public void onItemHeldEvent(PlayerItemHeldEvent event) {
				if (!event.getPlayer().isSneaking())
					return;
				
				for (final AbilityCycler ac : cyclers.values())
					if (ac.isCycleItem(event.getPlayer().getItemInHand())) {
						ac.onHeld(event);
					}
				
			}
			
		};
		
		iInteract = new NoxListener<NoxMMO>(NoxMMO.getInstance()) {
			
			@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
			public void onInteract(PlayerInteractEvent event) {
				if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
						!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					return;
				
				for (final AbilityCycler ac : cyclers.values())
					if (ac.isValid()) {
						ac.onInteract(event);
					}
			}
		};
		
		iHeld.register();
		iInteract.register();
	}
	
	public static boolean isRegistered(UUID cyclerID) {
		return cyclers != null && cyclers.containsKey(cyclerID);
	}
	
	public static void register(AbilityCycler cycler) {
		Validate.notNull(cycler);
		
		if (!cyclers.containsKey(cycler.getPersistentID())) {
			cyclers.put(cycler.getPersistentID(), cycler);
		}
	}
	
	private static int getChange(int prev, int next) {
		if (next > prev)
			return 1;
		else if (next < prev)
			return -1;
		else
			return 0;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public MMOPlayer getMMOPlayer() {
		return MMOPlayerManager.getInstance().getPlayer(getPlayer());
	};
	
	public String getPersistenceNode() {
		return getPersistentID().toString();
	}
	
	public UUID getPersistentID() {
		return id;
	};
	
	public Player getPlayer() {
		if (!isValid())
			return null;
		
		OfflinePlayer p;
		if ((p = Bukkit.getOfflinePlayer(player)) != null)
			return p.getPlayer();
		
		return null;
	}
	
	public BaseAbilityCyclerRenderer getRenderer() {
		if (renderer != null)
			return renderer;
		
		return BaseAbilityCyclerRenderer.dummy;
	}
	
	public boolean isCycleItem(ItemStack stack) {
		final AttributeStorage as = AttributeStorage.newTarget(stack, id);
		
		if (as.getData(null) == id.toString())
			return true;
		
		return false;
	}
	
	public void log(Level level, String msg) {
		if (logger == null) {
			logger = new ModuleLogger("AbilityCycler-"
					+ Bukkit.getPlayer(player).getName());
		}
		
		logger.log(level, msg);
	}
	
	@Override
	public String next() {
		final String ret = super.next();
		renderDisplay();
		
		return ret;
	}
	
	@Override
	public String previous() {
		final String ret = super.previous();
		renderDisplay();
		
		return ret;
	}
	
	public void renderDisplay() {
		getRenderer().render();
	}
	
	public Map<String, Object> serialize() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		
		ret.put(SERIALIZE_ID, id.toString());
		ret.put(SERIALIZE_PLAYER_ID, player.toString());
		ret.put(SERIALIZE_ABILITIES, abilities);
		
		return ret;
	}
	
	public void setRenderer(BaseAbilityCyclerRenderer renderer) {
		this.renderer = renderer;
	}
	
	private void onHeld(PlayerItemHeldEvent event) {
		
	}
	
	private void onInteract(PlayerInteractEvent event) {
		
	}
	
	boolean isValid() {
		return getPlayer() != null;
	}
}
