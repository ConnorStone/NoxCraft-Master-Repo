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

package com.noxpvp.core.utils;

import com.bergerkiller.bukkit.common.AsyncTask;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.google.common.collect.ImmutableList;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.annotation.Blocking;
import com.noxpvp.core.events.uuid.NoxUUIDFoundEvent;
import com.noxpvp.core.events.uuid.NoxUUIDLostEvent;
import com.noxpvp.core.listeners.NoxListener;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UUIDUtil extends NoxListener<NoxCore> {
	public static final UUID ZERO_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	public static final Pattern uuidPattern = Pattern.compile("(\\w{8})-?(\\w{4})-?(\\w{4})-?(\\w{4})-?(\\w{12})");
	public static final String ZERO_UUID_COMPRESSED = UUIDUtil.compressUUID(UUID.fromString("00000000-0000-0000-0000-000000000000"));
	private static volatile UUIDUtil instance;
	private ConcurrentHashMap<String, UUID> name2UUID;

	private UUIDUtil() {
		super(NoxCore.getInstance());

		name2UUID = new ConcurrentHashMap<String, UUID>();
		register();
	}

	public static String compressUUID(Object ob) {
		return ob != null? compressUUID(ob.toString()) : null;
	}

	public static String compressUUID(String id) {
		Validate.notNull(id);
		Matcher m = uuidPattern.matcher(id);
		Validate.isTrue(m.matches(), "Not a valid UUID string!"); //functionally equivalant to isUUID(id). Just less cpu and faster.

		return m.replaceAll("$1$2$3$4$5");
	}

	public static String decompressUUID(String id) {
		Validate.notNull(id);
		Matcher m = uuidPattern.matcher(id);
		Validate.isTrue(m.matches(), "Not a valid UUID string!");

		return m.replaceAll("$1-$2-$3-$4-$5");
	}

	public static UUID toUUID(String id) {
		if (id == null)
			return null;

		try {
			id = decompressUUID(id);
			return UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static boolean isUUID(Object object) {
		return (object != null) && uuidPattern.matcher(object.toString()).matches();
	}

	public static UUIDUtil getInstance() {
		if (instance == null)
			instance = new UUIDUtil();

		return instance;
	}

	public static <T> List<T> toList(T name) {
		List<T> names = new ArrayList<T>();
		names.add(name);
		return names;
	}

	public void ensurePlayerUUIDByName(String name) {
		ensurePlayerUUIDsByName(toList(name));
	}

	public void ensurePlayerUUIDsByName(final List<String> names) {
		for (String name : names)
			name2UUID.put(name, ZERO_UUID);

		if (Bukkit.isPrimaryThread())
			fetchByOnlineNames(names);

		new AsyncTask() {
			public void run() {
				fetchByNames(names);
			}
		}.start();
	}

	private void mapIDS(Map<String, UUID> map) {
		for (Entry<String, UUID> entry : map.entrySet())
			mapID(entry);
	}

	private void mapID(Entry<String, UUID> entry) {
		mapID(entry.getKey(), entry.getValue());
	}

	private void mapID(String username, UUID id) {
		Validate.notNull(id);
		Validate.notNull(username);

		if ((!name2UUID.containsKey(username) || (ZERO_UUID.equals(name2UUID.get(username))) && !id.equals(name2UUID.get(username)))) {
			CommonUtil.callEvent(new NoxUUIDLostEvent(username, name2UUID.get(username)));
			name2UUID.remove(username);
		}

		if (!name2UUID.containsKey(username) || ZERO_UUID.equals(name2UUID.get(username))) {
			name2UUID.put(username, id);
			CommonUtil.callEvent(new NoxUUIDFoundEvent(username, id));
		}
	}

	private void fetchByNames(List<String> names) {
		Map<String, UUID> call = new HashMap<String, UUID>();
		if (Bukkit.isPrimaryThread())
			call.putAll(fetchByOnlineNames(names));

		try {
			call.putAll(new UUIDFetcher(names).call());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mapIDS(call);
	}

	private Map<String, UUID> fetchByOnlineNames(List<String> names) {
		Validate.isTrue(Bukkit.isPrimaryThread(), "Must run on server thread for safety.");

		Map<String, UUID> call = new HashMap<String, UUID>();
		for (Iterator<String> iterator = names.iterator(); iterator.hasNext(); ) {
			String name = iterator.next();
			Player p = Bukkit.getPlayer(name); //Check if player is actually online. Save up on that heavy lock.
			if (p != null && p.getUniqueId() != null) {
				call.put(name, p.getUniqueId());
				iterator.remove();
			}
		}
		mapIDS(call);
		return call;
	}

	/**
	 * @param name of player
	 * @return UUID of player
	 */
	@Blocking
	public UUID getID(String name) {
		Validate.notNull(name);

		if (name2UUID.containsKey(name)) {
			UUID id = name2UUID.get(name);

			if (id != ZERO_UUID)
				return id;
		}

		fetchByNames(toList(name));
		return name2UUID.get(name);
	}

	/**
	 * Attempts to get a UUID without blocking threads.
	 *
	 * @param name of player
	 * @return UUID of player or null if never cached which in return should be available in future.
	 */
	public UUID tryGetID(String name) {
		if (name2UUID.containsKey(name)) {
			UUID id = name2UUID.get(name);
			return (id == ZERO_UUID) ? null : id;
		} else {
			if (Bukkit.isPrimaryThread()) {
				UUID id = fetchByOnlineNames(toList(name)).get(name);
				if (id != null) {
					name2UUID.put(name, id);
					return id;
				}
			}

			ensurePlayerUUIDByName(name);
			return null;
		}
	}

	/**
	 * Attempts to get a UUID without blocking threads.
	 *
	 * @param player object
	 * @return UUID of player or null if never cached which in return should be available in future.
	 */
	public UUID tryGetID(Player player) {
		String name = player.getName();
		UUID puid = player.getUniqueId();
		if (puid != null && name2UUID.containsKey(name) && !name2UUID.get(name).equals(puid))
			mapID(name, puid);

		if (name2UUID.containsKey(name)) {
			UUID id = name2UUID.get(name);
			return (id == ZERO_UUID) ? null : id;
		} else {
			if (Bukkit.isPrimaryThread()) {
				UUID id = player.getUniqueId();
				if (id != null) {
					mapID(name, id);
					return id;
				}
			}

			ensurePlayerUUIDByName(name);
			return null;
		}
	}

	@EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if (p.getUniqueId() != null) {
			UUID uuid = tryGetID(p);
			if (uuid != null && !uuid.equals(ZERO_UUID)) {
				mapID(p.getName(), uuid);
				return;
			}
		}
		ensurePlayerUUIDsByName(toList(p.getName()));

	}

	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		remove(event.getPlayer());
	}

	private void remove(Player p) {
		remove(p.getName());
	}

	private void remove(String name) {
		if (name2UUID.containsKey(name)) {
			CommonUtil.callEvent(new NoxUUIDLostEvent(name, name2UUID.get(name)));
			name2UUID.remove(name);
		}
	}

	public static UUID toUUID(Object id) {
		if (isUUID(id))
			return (id instanceof UUID)? (UUID)id: toUUID(id.toString());
		return null;
	}

	/**
	 * @author EvilMidget
	 */
	static class UUIDFetcher implements Callable<Map<String, UUID>> {

		private static final String AGENT = "minecraft";
		private static final int MAX_SEARCH = 100;
		private static final String PROFILE_URL = "https://api.mojang.com/profiles/page/";
		private final JSONParser jsonParser = new JSONParser();
		private final List<String> names;

		public UUIDFetcher(List<String> names) {
			this.names = ImmutableList.copyOf(names);
		}

		@SuppressWarnings("unchecked")
		private static String buildBody(List<String> names) {
			List<JSONObject> lookups = new ArrayList<JSONObject>();
			for (String name : names) {
				JSONObject obj = new JSONObject();
				obj.put("name", name);
				obj.put("agent", AGENT);
				lookups.add(obj);
			}
			return JSONValue.toJSONString(lookups);
		}

		private static HttpURLConnection createConnection(int page) throws Exception {
			URL url = new URL(PROFILE_URL + page);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			return connection;
		}

		private static void writeBody(HttpURLConnection connection, String body) throws Exception {
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.write(body.getBytes());
			writer.flush();
			writer.close();
		}

		public Map<String, UUID> call() throws Exception {
			Map<String, UUID> uuidMap = new HashMap<String, UUID>();
			String body = buildBody(names);
			for (int i = 1; i < MAX_SEARCH; i++) {
				HttpURLConnection connection = createConnection(i);
				writeBody(connection, body);
				JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
				JSONArray array = (JSONArray) jsonObject.get("profiles");
				Number count = (Number) jsonObject.get("size");
				if (count.intValue() == 0) {
					break;
				}
				for (Object profile : array) {
					JSONObject jsonProfile = (JSONObject) profile;
					String id = (String) jsonProfile.get("id");
					String name = (String) jsonProfile.get("name");
					UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
					uuidMap.put(name, uuid);
				}
			}
			return uuidMap;
		}
	}
}