package com.noxpvp.core.internal;

import com.bergerkiller.bukkit.common.AsyncTask;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.events.CooldownExpireEvent;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.manager.CorePlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CooldownHandler extends AsyncTask {
	private Map<UUID, List<CoolDown>> cds;
	private NoxCore core;
	private final long delay;

	public CooldownHandler() {
		cds = new MapMaker().concurrencyLevel(2).makeMap();
		core = NoxCore.getInstance();
		delay = core.getCoreConfig().get("cd-handler-delay", (long) 500);
	}

	public synchronized void loadPlayer(NoxPlayer player) {
		cds.put(player.getPersistantID(), player.getCoolDowns());
	}

	public void loadPlayer(Player player) {
		loadPlayer(CorePlayerManager.getInstance().getPlayer(player));
	}

	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p/>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	public void run() {
		for (Map.Entry<UUID, List<CoolDown>> entry : cds.entrySet()) {
			final UUID uuid = entry.getKey();
			for (final CoolDown cd : entry.getValue())
				if (cd.expired()) {
					CommonUtil.nextTick(new Runnable() {
						public void run() {
							Player p = Bukkit.getPlayer(uuid);
							if (p != null)
								CommonUtil.callEvent(new CooldownExpireEvent(p, cd.getName()));
						}
					});
				}
		}

		sleep(delay);
	}
}
