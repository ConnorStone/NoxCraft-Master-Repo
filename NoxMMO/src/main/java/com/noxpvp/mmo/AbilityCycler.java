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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.comphenix.attribute.AttributeStorage;
import com.noxpvp.core.Persistent;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.utils.InventoryUtil;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.manager.AbilityCyclerManager;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import com.noxpvp.mmo.renderers.BaseAbilityCyclerRenderer;
import com.noxpvp.mmo.renderers.ItemDisplayACRenderer;

public class AbilityCycler extends Cycler<String> implements
		Persistent, ConfigurationSerializable, MenuItemRepresentable {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static ModuleLogger				logger;
	private static NoxListener<NoxMMO>		onCycle, onUse;
	private static Map<UUID, AbilityCycler>	cyclers;
	
	// Serializers start
	private static final String				SERIALIZE_ID		= "id";
	private static final String				SERIALIZE_PLAYER_ID	= "player-id";
	private static final String				SERIALIZE_ABILITIES	= "abilities";
	// Serializers end
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private ItemStack						lastSeenItem;
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
		
		abilities = new HashSet<String>();
		lastSeenItem = item;
		player = playerID;
		id = UUID.randomUUID();
		AttributeStorage.newTarget(item, id).setData(id.toString());
		renderer = new ItemDisplayACRenderer(this);
		
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
		renderer = new ItemDisplayACRenderer(this);
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
		
		onCycle = new NoxListener<NoxMMO>(NoxMMO.getInstance()) {
			
			@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
			public void onItemHeldEvent(PlayerItemHeldEvent event) {
				
				// Return if not sneaking or no item
				final ItemStack item = event.getPlayer().getItemInHand();
				if (item == null || item.getType().equals(Material.AIR))
					return;
				
				if (!event.getPlayer().isSneaking())
					return;
				
				for (final AbilityCycler ac : cyclers.values())
					if (ac.isCycleItem(event.getPlayer().getItemInHand())) {
						ac.onCycle(event);
					}
				
			}
			
		};
		
		onUse = new NoxListener<NoxMMO>(NoxMMO.getInstance()) {
			
			@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
			public void onInteract(PlayerInteractEvent event) {
				
				// Return if not right clicking
				if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
						!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					return;
				
				for (final AbilityCycler ac : cyclers.values())
					if (ac.isValid()) {
						ac.onUse(event);
					}
			}
		};
		
		onCycle.register();
		onUse.register();
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
	
	public List<PlayerAbility> getAbilityList() {
		pruneAbilitiesFromPlayer();
		final List<PlayerAbility> ret = new ArrayList<PlayerAbility>();
		
		final MMOPlayer p = getMMOPlayer();
		for (final String ab : getList()) {
			ret.add(p.getAbility(ab));
		}
		
		return ret;
	}
	
	public PlayerAbility getCurrentAB() {
		return getMMOPlayer().getAbility(current());
	};
	
	public ItemStack getIdentifiableItem() {
		final ItemStack ret = getLastItemKnown() != null ? getLastItemKnown()
				: new ItemStack(Material.STICK);
		
		ret.setAmount(Math.max(1, getList().size()));
		final ItemMeta meta = ret.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "Ability Cycler");
		
		final List<String> lore = new ArrayList<String>(Arrays.asList(ChatColor.GOLD
				+ "Abilities:"));
		
		for (final String ab : getList()) {
			lore.add(ChatColor.GREEN + ab);
		}
		
		ret.setItemMeta(meta);
		
		return ret;
	}
	
	public ItemStack getLastItemKnown() {
		return lastSeenItem.clone();
	};
	
	public MMOPlayer getMMOPlayer() {
		return MMOPlayerManager.getInstance().getPlayer(getPlayer());
	}
	
	public String getPersistenceNode() {
		return getPersistentID().toString();
	}
	
	public UUID getPersistentID() {
		return id;
	}
	
	public Player getPlayer() {
		
		OfflinePlayer p;
		if ((p = Bukkit.getOfflinePlayer(player)) != null)
			return p.getPlayer();
		
		return null;
	}
	
	public BaseAbilityCyclerRenderer getRenderer() {
		return renderer;
	}
	
	public int getSlotOfItem(ItemStack item) {
		return InventoryUtil.getSlotOfItem(getPlayer().getInventory(), item);
	}
	
	public boolean isCycleItem(ItemStack stack) {
		final AttributeStorage as = AttributeStorage.newTarget(stack, id);
		
		if (as.getData(null) == id.toString()) {
			lastSeenItem = stack.clone();
			return true;
		}
		
		return false;
	}
	
	public void log(Level level, String msg) {
		if (logger == null) {
			logger = new ModuleLogger("AbilityCycler-"
					+ Bukkit.getPlayer(player).getName());
		}
		
		logger.log(level, msg);
	}
	
	public PlayerAbility nextAB() {
		final String next = next();
		final PlayerAbility ab = getMMOPlayer().getAbility(next);
		
		if (ab != null) {
			renderDisplay();
		}
		
		return ab;
	}
	
	public PlayerAbility peekNextAB() {
		return getMMOPlayer().getAbility(peekNext());
	}
	
	public PlayerAbility peekPreviousAB() {
		return getMMOPlayer().getAbility(peekPrevious());
	}
	
	public PlayerAbility previousAB() {
		final String previous = previous();
		final PlayerAbility ab = getMMOPlayer().getAbility(previous);
		
		if (ab != null) {
			renderDisplay();
		}
		
		return ab;
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
	
	private void onCycle(PlayerItemHeldEvent event) {
		switch (getChange(event.getPreviousSlot(), event.getNewSlot())) {
			case 1:
				nextAB();
				renderDisplay();
				break;
			case -1:
				previousAB();
				renderDisplay();
				break;
			default:
				break;
		}
	}
	
	private void onUse(PlayerInteractEvent event) {
		getCurrentAB().execute();
	}
	
	private void pruneAbilitiesFromPlayer() {
		final Iterator<String> abIter = abilities.iterator();
		
		while (abIter.hasNext()) {
			final String ab = abIter.next();
			final MMOPlayer mmop = getMMOPlayer();
			
			if (!mmop.hasAbility(ab)) {
				abIter.remove();
			}
			
		}
	}
	
	boolean isValid() {
		pruneAbilitiesFromPlayer();
		
		return getPlayer() != null && abilities.size() > 0
				&& AbilityCycler.isRegistered(getPersistentID());
	}
}
