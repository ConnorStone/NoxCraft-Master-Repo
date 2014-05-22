package com.noxpvp.core.listeners;

import java.io.IOException;
import java.util.logging.FileHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.manager.CorePlayerManager;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener extends NoxListener<NoxCore> {
	private static FileHandler handle = null;
	private static boolean isGood = false;
	private static ModuleLogger log = null;
	private final CorePlayerManager manager;

	public VoteListener() {
		super(NoxCore.getInstance());
		manager = CorePlayerManager.getInstance();
		destroy();
		init();
	}

	private static void init() {
		try {
			handle = new FileHandler(NoxCore.getInstance().getDataFile("votelogs.log").getPath(), true);
			log = NoxCore.getInstance().getModuleLogger("Vote", "Log");
			log.addHandler(handle);
			isGood = handle != null;
		} catch (SecurityException e) {
			if (handle != null)
				handle.close();
			e.printStackTrace();
		} catch (IOException e) {
			if (handle != null)
				handle.close();
			e.printStackTrace();
		}
	}

	public void destroy() {
		if (isGood) {
			log.removeHandler(handle);
			handle.close();
		}

		HandlerList.unregisterAll(this);

		handle = null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		boolean usePlayer = false;

		if (vote.getUsername().length() > 4)
			usePlayer = true;

		StringBuilder sb = new StringBuilder();
		sb.append("Vote Recieved| Timestamp:").append(vote.getTimeStamp()).append(" Address:").append(vote.getAddress()).append(" ServiceName:").append(vote.getServiceName()).append(" Username: ").append(vote.getUsername());

		log.info(sb.toString());

		final String user = vote.getUsername();
		if (event.isAsynchronous() && usePlayer) {
			CommonUtil.nextTick(new Runnable() { // TODOD: Double check. This might not be needed with multithreading.. Must test concurrency support of player handler
				public void run() {
					NoxPlayer player = manager.getPlayer(user);

					synchronized (player) {
						player.incrementVote();
					}
				}
			});
		} else {
			NoxPlayer player = manager.getPlayer(vote.getUsername());

			synchronized (player) {
				player.incrementVote();
			}
		}
	}
}
