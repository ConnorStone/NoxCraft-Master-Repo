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

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.utils.UUIDUtil;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.renderers.BaseAbilityCyclerRenderer;
import com.noxpvp.mmo.renderers.ItemDisplayACRenderer;

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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class AbilityCycler extends Cycler<Ability> implements ConfigurationSerializable {
//	public static final String TEMP_PCTK = "ability-cycler.active-count";

	static ConcurrentMap<String, List<AbilityCycler>> cyclers = null;
	private static NoxListener<NoxMMO> iHeld = null, iInteract;
	private ItemStack cycleItem;
	private Reference<OldMMOPlayer> player = null;
	private BaseAbilityCyclerRenderer renderer;
	private int lastSlot = 0;

	public void setRenderer(BaseAbilityCyclerRenderer renderer) {
		this.renderer = renderer;
	}


	public AbilityCycler(int size, OfflinePlayer player, ItemStack cycleItem) {
		this(size, MMOPlayerManager.getInstance().getPlayer(player), cycleItem);
	}

	public AbilityCycler(Collection<Ability> data, OfflinePlayer player, ItemStack cycleItem) {
		this(data, MMOPlayerManager.getInstance().getPlayer(player), cycleItem);
	}

	public AbilityCycler(int size, OldMMOPlayer player, ItemStack cycleItem) {
		super(size);
		this.player = new SoftReference<OldMMOPlayer>(player);
		this.cycleItem = cycleItem;

		register(this);
	}

	public AbilityCycler(Collection<Ability> data, OldMMOPlayer player, ItemStack cycleItem) {
		super(data);
		this.player = new SoftReference<OldMMOPlayer>(player);
		this.cycleItem = cycleItem;
		
		register(this);
	}

	public static void register(AbilityCycler cycler) {
		Validate.notNull(cycler.getMMOPlayer());

		OldMMOPlayer p = cycler.getMMOPlayer();

		final String identity = UUIDUtil.compressUUID(p.getUUID());
		if (cyclers == null) cyclers = new MapMaker().concurrencyLevel(4).makeMap();
		if (cyclers.containsKey(identity))
			cyclers.get(identity).add(cycler);
		else {
			List<AbilityCycler> abilities = new ArrayList<AbilityCycler>();
			abilities.add(cycler);
			cyclers.put(identity, abilities);
		}
		
		cycler.setRenderer(new ItemDisplayACRenderer(cycler));
	}

	public static List<AbilityCycler> getCyclers(String identity) {
		if (isRegistered(identity))
			return cyclers.get(identity);
		return Collections.emptyList();
	}

	public BaseAbilityCyclerRenderer getRenderer() {
		if (this.renderer != null)
			return this.renderer;
		return BaseAbilityCyclerRenderer.dummy;
	}

	public void renderDisplay() {
		getRenderer().render();
	}

	public boolean isCyclerMatch(ItemStack stack) {
		return (stack.getType().equals(cycleItem.getType()) &&
				stack.getData().equals(cycleItem.getData()));
	}

	public boolean isPlayerMatch(Object ob) {
		if (!(ob instanceof OfflinePlayer || UUIDUtil.isUUID(ob) || ob instanceof OldMMOPlayer))
			return false;

		if (UUIDUtil.isUUID(ob) && getMMOPlayer() != null)
			return getMMOPlayer().getUUID().equals(UUIDUtil.toUUID(ob));

		if (getMMOPlayer() != null && ob instanceof OldMMOPlayer)
			return getMMOPlayer().equals(ob);

		return false;
	}

	public static AbilityCycler getCycler(String identity, final ItemStack item) {
		List<AbilityCycler> cyclers = getCyclers(identity);
		if (cyclers.isEmpty()) return null;

		for (AbilityCycler cycler : cyclers)
			if (cycler.isCyclerMatch(item))
				return cycler;
		return null;
	}

	static boolean isRegistered(String identity) {
		return cyclers != null && cyclers.containsKey(identity);
	}

	//Helpers...
	private static int getChange(int prev, int next) {
		if (next > prev)
			return 1;
		else if (next < prev)
			return -1;
		else
			return 0;
	}
	
	public void setLastSlot(int lastSlot) {
		this.lastSlot = lastSlot;
	}
	
	public int getLastSlot() {
		return lastSlot;
	}

	//INITIALIZE
	public static void init() {
		cyclers = new MapMaker().concurrencyLevel(2).makeMap();

		iHeld = new NoxListener<NoxMMO>(NoxMMO.getInstance()) {
			@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
			public void onItemHeldEvent(PlayerItemHeldEvent event) {
				final Player player = event.getPlayer();
				final String identity = UUIDUtil.compressUUID(player.getUniqueId());
				if (!player.isSneaking())
					return;

				final OldMMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(player);

				if (/*mmoPlayer.getTempData().get(TEMP_PCTK, 0) <= 0 || */!AbilityCycler.isRegistered(identity)) return; //Skip because we have no actual objects for this user.

				final ItemStack heldItem = player.getInventory().getItemInHand();

				AbilityCycler cycler = AbilityCycler.getCycler(identity, heldItem);
				if (cycler == null) return;

//				final int change = getChange(event.getPreviousSlot(), event.getNewSlot());
				switch (/*change */getChange(event.getPreviousSlot(), event.getNewSlot())) {
					case 1:
						cycler.setLastSlot(36 + event.getPreviousSlot());
						cycler.next();
						event.setCancelled(true);
						return;
					case -1:
						cycler.setLastSlot(36 + event.getPreviousSlot());
						cycler.previous();
						event.setCancelled(true);
						return;
					default:
						return;
				}
			}

		};

		iInteract = new NoxListener<NoxMMO>(NoxMMO.getInstance()) {
				@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
				public void onInteract(PlayerInteractEvent event) {
					if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
							!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
						return;
					
					final Player player = event.getPlayer();
					final String identity = UUIDUtil.compressUUID(player.getUniqueId());

					final OldMMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(player);

					if (/*mmoPlayer.getTempData().get(TEMP_PCTK, 0) <= 0 || */!AbilityCycler.isRegistered(identity)) return; //Skip because we have no actual objects for this user.

					final ItemStack heldItem = player.getInventory().getItemInHand();

					AbilityCycler cycler = AbilityCycler.getCycler(identity, heldItem);
					if (cycler == null) return;

					Ability a = cycler.current();
					if (a instanceof IPassiveAbility) return;

					//TODO: If complained about. Check if weapon to cancel event.

					event.setCancelled(a.execute().getResult());
				}
		};

		iHeld.register();
		iInteract.register();
	}

	boolean isValid() {
		return player != null && player.get() != null;
	}

	public Map<String, Object> serialize() {
		Map<String, Object> ret = new HashMap<String, Object>();

		//Ability Storage.
		List<String> abilities = new LinkedList<String>();
		for (Ability ability : getList())
			abilities.add(ability.getName());

		//Store current state.
		ret.put("current-index", currentIndex());

		ret.put("player", getMMOPlayer().getUUID(true));

		//Store item that tells if its the cycler item.
		ret.put("cycle-item", getCycleItem());

		//Actually store the abilities.
		ret.put("abilities", abilities);

		return ret;
	}

	private static boolean isDataValid(Map<String, Object> data) {
		return data.containsKey("player") && data.containsKey("cycle-item") && data.containsKey("abilities") && UUIDUtil.isUUID(data.get("player"));
	}

	public static AbilityCycler valueOf(Map<String, Object> data) {
		if (!isDataValid(data))
			return null;

		UUID id = UUIDUtil.toUUID(data.get("player"));
		OfflinePlayer player = Bukkit.getOfflinePlayer(id);

		ItemStack cycleItem = null;
		if (data.get("cycle-item") instanceof ItemStack)
			cycleItem = (ItemStack) data.get("cycle-item");

		List<String> abilityNames = null;
		if (data.get("abilities") instanceof List)
			abilityNames = (List<String>) data.get("abilities");

		if (id == null || cycleItem == null || LogicUtil.nullOrEmpty(abilityNames))
			return null;

		OldMMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(player);

		List<Ability> abilities = new ArrayList<Ability>();

		for (Ability a : mmoPlayer.getAllAbilities())
			if (abilityNames.contains(a.getName()))
				abilities.add(a);

		if (LogicUtil.nullOrEmpty(abilities))
			return null;

		return new AbilityCycler(abilities, mmoPlayer, cycleItem);
	}

	@Override
	public Ability previous() {
		Ability ret = super.previous();
		renderDisplay();
		return ret;
	}

	@Override
	public Ability next() {
		Ability ret = super.next();
		renderDisplay();
		return ret;
	}

	public OldMMOPlayer getMMOPlayer() {
		if (player != null) return player.get();
		return null;
	}

	public ItemStack getCycleItem() {
		return cycleItem;
	}

	public void setCycleItem(ItemStack itemStack) {
		this.cycleItem = itemStack;
	}

	public Player getPlayer() {
		if (isValid() && getMMOPlayer().isOnline())
			return getMMOPlayer().getPlayer();

		return null;
	}

	public static AbilityCycler getCycler(Player player) {
		return getCycler(UUIDUtil.compressUUID(player.getUniqueId()), player.getItemInHand());
	}
}
