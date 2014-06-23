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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.noxpvp.core.data.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.AsyncTask;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.data.OldNoxPlayerAdapter;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.util.InventoryActionCombo;


//FIXME: Add visuals (Text item display / ability display.
public class OldAbilityCycler extends Cycler<Ability> implements ConfigurationSerializable {
	public static final String TEMP_PCTK = "ability-cycler.active-count";
	private static AsyncTask cleaner;
	private static long cleaner_delay = 5000;
	private static ConcurrentMap<OldMMOPlayer, List<Reference<OldAbilityCycler>>> d;
	private static NoxListener<NoxMMO> iHeld, iMove, login;

	protected final String player_name;

	private ItemStack cycleItem;

	private Reference<OldMMOPlayer> player;

	public OldAbilityCycler(OldNoxPlayerAdapter adapter, Collection<Ability> data) {
		super(data);
		this.player = new SoftReference<OldMMOPlayer>(fetchMMOPlayer(adapter));
		this.player_name = ((getMMOPlayer() != null) ? getMMOPlayer().getPlayerName() : null);
		if (this.player_name == null)
			throw new IllegalArgumentException("The player adapter is invalid and does not have a name. OR is non existant.");

		register(this);
	}

	public OldAbilityCycler(OldNoxPlayerAdapter adapter, int size) {
		super(size);
		this.player = new SoftReference<OldMMOPlayer>(fetchMMOPlayer(adapter));
		this.player_name = ((getMMOPlayer() != null) ? getMMOPlayer().getPlayerName() : null);
		if (this.player_name == null)
			throw new IllegalArgumentException("The player adapter is invalid and does not have a name. OR is non existant.");

		register(this);
	}

	public static void register(OldAbilityCycler cycler) {
		OldMMOPlayer p = cycler.getMMOPlayer();
		Reference<OldAbilityCycler> c = new WeakReference<OldAbilityCycler>(cycler);
		if (p != null) {
			if (d.containsKey(p) && d.get(p) != null) {
				d.get(p).add(c);
			} else {
				d.put(p, new ArrayList<Reference<OldAbilityCycler>>());
				d.get(p).add(c);
			}
		}
	}

	public static OldAbilityCycler getCycler(OldMMOPlayer p, ItemStack stack) {
		if (d.containsKey(p))
			if (!d.get(p).isEmpty())
				for (Iterator<Reference<OldAbilityCycler>> iterator = d.get(p).iterator(); iterator.hasNext(); ) {
					Reference<OldAbilityCycler> ref = iterator.next();

					OldAbilityCycler cy = ref.get();
					if (cy == null) {
						iterator.remove(); //Self cleanup. In case the last execute of cleaner did not catch it.
						continue;
					}

					if (cy.isCycleItem(stack))
						return cy;
				}

		return null;
	}

	/**
	 * @return the cleaner
	 */
	public static AsyncTask getCleanerTask() {
		return cleaner;
	}

	/**
	 * @return the cleaner_delay
	 */
	public static synchronized long getCleanerDelay() {
		return cleaner_delay;
	}

	/**
	 * @param cleaner_delay the cleaner_delay to set
	 */
	public static synchronized void setCleanerDelay(long cleaner_delay) {
		OldAbilityCycler.cleaner_delay = cleaner_delay;
	}

	/**
	 * @return the iHeld
	 */
	public static synchronized NoxListener<NoxMMO> getiHeldListener() {
		return iHeld;
	}

	/**
	 * @return the iMove
	 */
	public static synchronized NoxListener<NoxMMO> getiMoveListener() {
		return iMove;
	}

	/**
	 * @return the login
	 */
	public static synchronized NoxListener<NoxMMO> getLoginListener() {
		return login;
	}

	public static void init() {
		d = new MapMaker().weakKeys().concurrencyLevel(2).makeMap();
		cleaner = new AsyncTask("OldAbilityCycler Static Cleaner", Thread.MIN_PRIORITY) {
			boolean first = true;

			public void run() {
				if (Bukkit.isPrimaryThread()) {
					stop();
					throw new IllegalStateException("THE STATIC CLEANER WAS RUNNING ON MAIN THREAD!");
				}

				if (first) {
					sleep();
					first = false;
				}

				List<OldMMOPlayer> r = new ArrayList<OldMMOPlayer>();
				for (OldMMOPlayer k : d.keySet())
					if (d.get(k).isEmpty())
						r.add(k);
					else {
						for (Iterator<Reference<OldAbilityCycler>> iterator = d.get(k).iterator(); iterator.hasNext(); )
							if (iterator.next().get() == null)
								iterator.remove();
						if (d.get(k).isEmpty())
							r.add(k);
					}

				sleep();
			}

			private void sleep() {
				if (Bukkit.isPrimaryThread()) {
					throw new IllegalStateException("MUST NEVER SLEEP MAIN THREAD!");
				}

				try {
					Thread.sleep(getCleanerDelay());
				} catch (InterruptedException e) {
				}
			}
		};

		cleaner.start();

		iHeld = new NoxListener<NoxMMO>(NoxMMO.getInstance()) { //ACTUAL HANDLING OF ABILITY CYCLER

			@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
			public void onItemheld(PlayerItemHeldEvent event) {
				Player p = event.getPlayer();
				OldMMOPlayer player = MMOPlayerManager.getInstance().getPlayer(p);


				if (!d.containsKey(player))
					return; //Do not need to listen.

				ItemStack held = p.getItemInHand();


				if (!p.isSneaking())
					return; //Ignore non sneak.

				int c = getChange(event.getPreviousSlot(), event.getNewSlot());

				if (c == 0)
					return;

				OldAbilityCycler cycler = getCycler(player, held);

				if (cycler == null)
					return;

				switch (c) {
					case 1:
						cycler.next();
						break;
					case -1:
						cycler.previous();
						break;
				}

				if (c != 0)
					event.setCancelled(true); //FIXME: Add visuals (Text item display / ability display.
			}
		};

		//noinspection BooleanMethodIsAlwaysInverted
		iMove = new NoxListener<NoxMMO>(NoxMMO.getInstance()) { //Registers the held event on item moved to hotbar. (SAVES CPU)
			InventoryActionCombo place = InventoryActionCombo.ANY_PLACE
					,
					pickup = InventoryActionCombo.ANY_PICKUP; //Easy Access

			@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
			public void onInvClick(InventoryClickEvent event) {
				if (!(event.getWhoClicked() instanceof Player))
					return; //I WANT A REAL PLAYER!

				InventoryAction action = event.getAction();
				if (!isActionable(action))
					return; //IGNORE ME

				Player p = (Player) event.getWhoClicked();

				if (!event.getInventory().getHolder().equals(p) && (event.getInventory().getHolder() instanceof Player))
					p = (Player) event.getInventory().getHolder(); //Fixes things like openInventory plugin. If this is occuring. Not 100% sure.

				OldMMOPlayer player = MMOPlayerManager.getInstance().getPlayer(p);

				ItemStack cursor = event.getCursor();
				ItemStack clicked = event.getCurrentItem();

				OldAbilityCycler cycler = null;

				if (place.contains(action)) {
					cycler = getCycler(player, cursor);
				}
				//FIXME: GOD IM EMPTY!!!
			}

			private boolean isActionable(InventoryAction action) { //TODO: Thoroughly test...
				switch (action) {
					case DROP_ALL_SLOT:
					case HOTBAR_MOVE_AND_READD:
					case HOTBAR_SWAP:
					case PLACE_ALL:
					case PLACE_ONE:
					case PLACE_SOME:
					case PICKUP_ALL:
					case SWAP_WITH_CURSOR:
						return true;
					default:
						return false;
				}
			}
		};

		login = new NoxListener<NoxMMO>(NoxMMO.getInstance()) {
			@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
			public void onLogin(PlayerJoinEvent event) {
				Player p = event.getPlayer();
				MMOPlayerManager.getInstance().isLoaded(p.getName());
			}
		};
	}

	public static int getActiveCyclers(OldBaseNoxPlayerAdapter player) {
		return player.getTempData().get(TEMP_PCTK, 0);
	}

	private static void addActiveCycler(OldBaseNoxPlayerAdapter player) {
		setActiveCyclers(player, getActiveCyclers(player) + 1);
	}

	private static void subActiveCycler(OldBaseNoxPlayerAdapter player) {
		setActiveCyclers(player, getActiveCyclers(player) - 1);
	}

	private static void setActiveCyclers(OldBaseNoxPlayerAdapter player, int active) {
		player.getTempData().set(TEMP_PCTK, active);
	}

	private static int getChange(int prev, int next) {
		if (next > prev)
			return 1;
		else if (next < prev)
			return -1;
		else
			return 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OldAbilityCycler))
			return false;

		OldAbilityCycler other = (OldAbilityCycler) obj;
		if (cycleItem == null) {
			if (other.cycleItem != null)
				return false;
		} else if (!cycleItem.equals(other.cycleItem))
			return false;
		if (player_name == null) {
			if (other.player_name != null)
				return false;
		} else if (!player_name.equals(other.player_name))
			return false;
		return true;
	}

	public ItemStack getCycleItem() {
		return cycleItem;
	}

	public OldAbilityCycler setCycleItem(ItemStack item) {
		cycleItem = item.clone();
		cycleItem.setAmount(1);
		cycleItem.setDurability((short) 0);

		return this;
	}

	public String getDisplayName() {
		return current().getDisplayName();
	}

	public OldMMOPlayer getMMOPlayer() {
		return (player == null) ? null : player.get();
	}

	public final Player getPlayer() {
		return Bukkit.getPlayer(player_name);
	}

	public final String getPlayerName() {
		return player_name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cycleItem == null) ? 0 : cycleItem.hashCode());
		result = prime * result + ((player_name == null) ? 0 : player_name.hashCode());
		return result;
	}

	public boolean isCycleItem(ItemStack stackToCheck) {
		ItemStack c = stackToCheck.clone(); //Work around for item name checks.
		c.getItemMeta().setDisplayName(getCycleItem().getItemMeta().getDisplayName()); //This will prevent item name changes on real items.

		return c.isSimilar(getCycleItem());
	}

	private OldMMOPlayer fetchMMOPlayer(OldNoxPlayerAdapter adapter) {
		return MMOPlayerManager.getInstance().getPlayer(adapter);
	}

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();

		List<String> r = new ArrayList<String>();
		for (Ability ability : getList())
			r.add(ability.getName());

		data.put("abilities", r);
		data.put("cycle-item", getCycleItem());
		data.put("player-name", getPlayerName());
		data.put("current-index", currentIndex());
		return data;
	}
}
