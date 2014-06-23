package com.noxpvp.mmo.util;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.reflection.SafeField;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.collection.DualAccessStringMap;
import com.noxpvp.core.utils.UUIDUtil;
import com.noxpvp.mmo.OldMMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.AxesPlayerClass;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerClassUtil {

	public static final String LOG_MODULE_NAME = "PlayerClass";

	private static ModuleLogger log;

	private static PlayerClassConstructUtil constructUtil;


	public static void init() {
		constructUtil = new PlayerClassConstructUtil();

	}

	/**
	 * Retrieves a single class of the specified (name <b>OR</b> id) for the specified player.
	 * @param clazz name <b>OR</b> id of player class
	 * @param player the player to grab the class instance from.
	 * @return PlayerClass object or null if none exist with the specified (name <b>OR</b> id)
	 */
	public static PlayerClass getClass(String clazz, OfflinePlayer player) {
		return getClass(clazz, MMOPlayerManager.getInstance().getPlayer(player));
	}

	public static PlayerClass getClass(String clazz, OldMMOPlayer player) {
		if (!hasClass(clazz)) return null;

		return constructUtil.safeConstructClass(clazz, player.getPlayer());
	}

	/**
	 * Tells whether or not this class exists for generation.
	 * @param clazz name or id of class
	 * @return true if exists else false.
	 */
	public static boolean hasClass(String clazz) {
		return (constructUtil.hasClassNameIgnoreCase(clazz)) || (constructUtil.hasClassId(clazz));
	}

	/**
	 * Retrieves all player classes associated with the player.
	 *
	 * This will include any classes they do not have access to but have data stored in them.
	 *
	 * @see #getAllPlayerClasses(com.noxpvp.mmo.OldMMOPlayer)
	 *
	 * @param player
	 * @return List of classes
	 */
	public static List<PlayerClass> getAllPlayerClasses(OfflinePlayer player) {
		return getAllPlayerClasses(MMOPlayerManager.getInstance().getPlayer(player));
	}

	/**
	 * Retrieves all player classes associated with the player.
	 *
	 * This will include any classes they do not have access to but have data stored in them.
	 *
	 * @param player OldMMOPlayer object.
	 * @return List of classes or null if (player == null)
	 */
	public static List<PlayerClass> getAllPlayerClasses(OldMMOPlayer player) {
		if (player == null) return null;
//		List<PlayerClass> ret = player.getClasses(); //Can be empty if they don't have any classes.

		return constructUtil.getAllClasses((LogicUtil.nullOrEmpty(player.getUID())?player.getName(): player.getUID()));

	}

	/**
	 * Retrieves all usable classes associated with the player.
	 *
	 * This will not include any classes they cannot access. Even if data is present for it.
	 *
	 * @see #getAllowedPlayerClasses(com.noxpvp.mmo.OldMMOPlayer)
	 *
	 * @param player the player to grab the classes from.
	 * @return List of classes or null if (player == null)
	 */
	public static List<PlayerClass> getAllowedPlayerClasses(OfflinePlayer player) {
		return getAllowedPlayerClasses(MMOPlayerManager.getInstance().getPlayer(player));
	}

	/**
	 * Retrieves all usable classes associated with the player.
	 *
	 * This will not include any classes they cannot access. Even if data is present for it.
	 *
	 * @see #filterAllowedClasses(java.util.List)
	 *
	 * @param player the player to grab the classes from.
	 * @return List of classes or null if (player == null)
	 */
	public static List<PlayerClass> getAllowedPlayerClasses(OldMMOPlayer player) {
		if (player == null) return null;
		List<PlayerClass> ret = constructUtil.getAllClasses(player.getPlayer());
		filterAllowedClasses(ret);

		return ret;
	}

	/**
	 * Filters the classes to only allow anything that the player has permission to use to stay on the list.
	 * @param classes a valid modifiable list of player classes to edit.
	 */
	public static void filterAllowedClasses(List<PlayerClass> classes) {
		Iterator<PlayerClass> iterator = classes.iterator();
		while (iterator.hasNext()) {
//			PlayerClass c = iterator.next();
			if (!/*c.canUseClass()*/iterator.next().canUseClass())
				iterator.remove();
		}

	}

	/**
	 * Filters the classes to only allow anything that has data other than default.
	 * @param classes a valid modifiable list of player classes to edit.
	 */
	public static void filterChangedClasses(List<PlayerClass> classes) {
		Iterator<PlayerClass> iterator = classes.iterator();

		while (iterator.hasNext()) {
//			PlayerClass next = iterator.next();
			if (/*next*/iterator.next().getTotalExp() <= 1)
				iterator.remove();
		}
	}

	/**
	 * Retrieves all classes that may be saved to data.
	 *
	 * @see #getAllChangedPlayerClasses(com.noxpvp.mmo.OldMMOPlayer)
	 *
	 * @param player player to grab classes from
	 * @return List of player class objects.
	 */
	public static List<PlayerClass> getAllChangedPlayerClasses(OfflinePlayer player) {
		return getAllChangedPlayerClasses(MMOPlayerManager.getInstance().getPlayer(player));
	}

	/**
	 * Retrieves all classes that may be saved to data.
	 *
	 * @see #filterChangedClasses(java.util.List)
	 *
	 * @param player player to grab classes from
	 * @return List of player class objects.
	 */
	public static List<PlayerClass> getAllChangedPlayerClasses(OldMMOPlayer player) {
		List<PlayerClass> ret = getAllPlayerClasses(player);

		filterChangedClasses(ret);

		return ret;
	}

	/**
	 * Classes must have the following constructors. (Each number represent a constructor with the following params.
	 * Bold signifies that internal mechanisms depend heavily on and <b>MUST</b> be implemented.
	 * <ol>
	 * <li><b>(String playerName)</b></li>
	 * </ol>
	 * <br/><br/>
	 * You must implement the following:<br/>
	 * <ul>
	 * <li> public static final String variable of "uniqueID";</li>
	 * <li> public static final String variable of "className";</li>
	 * </ul>
	 * <p/>
	 * <p/>
	 * This will discard invalid classes and throw {@linkplain java.util.logging.Level#SEVERE} log message into console.l
	 *
	 * @see com.noxpvp.mmo.util.PlayerClassUtil.PlayerClassConstructUtil#registerPlayerClass(Class)
	 *
	 * @param clazz class for registration
	 */
	public static void registerPlayerClass(Class<? extends PlayerClass> clazz) {
		constructUtil.registerPlayerClass(clazz);
	}

	/**
	 * This is purely an internal call and must not be used. It may cause inconsistencies with plugin if used in wrong context.
	 *
	 * @param data serialized data.
	 * @return PlayerClass object or null if invalid data provided
	 */
	public static PlayerClass safeConstructClass(Map<String, Object> data) {
		return constructUtil.safeConstructClass(data);
	}

	/**
	 * Internal methods to deal with class initialization on a dynamic bases.
	 *
	 * <p>These implementations should not be hooked into directly unless you know the ins and outs of how the class system works.</p>
	 *
	 * <!--
	 *      <p>This includes the original creator it.</p>
	 * -->
	 */
	protected static class PlayerClassConstructUtil { //TODO: UUID's

		public PlayerClassConstructUtil() {
			log = NoxMMO.getInstance().getModuleLogger(LOG_MODULE_NAME);

			classIdNameMap = new DualAccessStringMap<String>();
			pClasses = new DualAccessStringMap<Class<? extends PlayerClass>>();

			addDefaults();
		}

		private final Class<? extends PlayerClass>[] classes = new Class[]{
				AxesPlayerClass.class
		};

		/*
		 * When the player does not have uuid the cache could reference their name.
		 * When doing so and their uuid is now available. It will now think that they don't exist in cache causing double class object storage.
		 * During this the objects still remain near complete duplicates. There is only a very slight chance of data not being saved during such transition.
		 *
		 * However it will still load data no matter what. So this should be fine. Data is saved quite often anyways Especially on setting a home.
		 * Setting homes forces a save operation anyways.
		 */
		private DualAccessStringMap<String> classIdNameMap; //Key = classId : value = className

		private DualAccessStringMap<Class<? extends PlayerClass>> pClasses;    //Used to cache class instances. Used to not generate many class objects.

		/**
		 * Classes must have the following constructors. (Each number represent a constructor with the following params.
		 * Bold signifies that internal mechanisms depend heavily on and <b>MUST</b> be implemented.
		 * <ol>
		 * <li><b>(String playerName)</b></li>
		 * </ol>
		 * <br/><br/>
		 * You must implement the following:<br/>
		 * <ul>
		 * <li> public static final String variable of "uniqueID";</li>
		 * <li> public static final String variable of "className";</li>
		 * </ul>
		 * <p/>
		 * <p/>
		 * This will discard invalid classes and throw {@linkplain java.util.logging.Level#SEVERE} log message into console.l
		 *
		 * @param clazz for registration
		 */
		public void registerPlayerClass(Class<? extends PlayerClass> clazz) {
			try {
				SafeField<String> className = new SafeField<String>(clazz, "className");
				SafeField<String> classId = new SafeField<String>(clazz, "uniqueID");

				boolean bad = false;
				if (!classId.isValid() || !classId.isStatic()) {
					log.severe("PlayerClass \"" + clazz.getName() + "\" is not valid. It does not have a static String of className");
					bad = true;
				}

				if (!className.isValid() || !className.isStatic()) {
					log.severe("PlayerClass \"" + clazz.getName() + "\" is not valid. It does not have a static String of uniqueID");
					bad = true;
				}

				if (bad)
					return;

				String cName = className.get(clazz), cId = classId.get(clazz);

				pClasses.putLower(cId, clazz);
				classIdNameMap.putLower(cId, cName);

				NoxMMO mmo = NoxMMO.getInstance();

				log.fine("Registered the class \"" + cName + "\" with id \"" + cId + "\".");
				mmo.loadLocale(MMOLocale.CLASS_DISPLAY_NAME.getName() + "." + cName, cName);

				mmo.saveLocalization();
			} catch (Exception e) {
				log.severe("An error occurred that is not being tracked...");
				e.printStackTrace();
			}
		}

		@Deprecated
		public List<PlayerClass> getAvailableClasses(Player player) {
			List<PlayerClass> ret = new ArrayList<PlayerClass>();
			for (PlayerClass c : getAllClasses(player))
				if (c.canUseClass())
					ret.add(c);

			//		return ret;
			return getAllClasses(player);
		}

		@Deprecated
		private List<PlayerClass> getAllClasses(String playerIdentifier) {
			List<PlayerClass> ret = new ArrayList<PlayerClass>();
			for (Class c : getPClasses()) {
				PlayerClass p = safeConstructClass(c, playerIdentifier);
				if (p != null)
					ret.add(p);
			}

			return ret;
		}

		@Deprecated
		private List<PlayerClass> getAllClasses(Player player) {
			List<PlayerClass> ret = new ArrayList<PlayerClass>();
			for (Class c : getPClasses()) {
				PlayerClass p = safeConstructClass(c, player);
				if (p != null)
					ret.add(p);
			}

			return ret;
		}

		public Collection<String> getAllClassNames() {
			return classIdNameMap.valueSet();
		}

		public String getClassNameById(String id) {
			if (hasClassId(id))
				return classIdNameMap.getLower(id);
			else
				return null;
		}

		public String getIdByClassName(String name) {
			if (hasClassName(name))
				return classIdNameMap.getByValue(name);
			else
				return null;
		}

		public Collection<Class<? extends PlayerClass>> getPClasses() {
			return pClasses.values();
		}

		public boolean hasClassId(String id) {
			return classIdNameMap.containsKeyLower(id);
		}

		public boolean hasClassName(String name) {
			return classIdNameMap.containsValue(name);
		}

		public boolean hasClassNameIgnoreCase(String name) {
			for (String cn : classIdNameMap.values())
				if (cn.equalsIgnoreCase(name))
					return true;

			return false;
		}

		public String getClassIDbyClass(Class<?> clazz) {
			if (pClasses.containsValue(clazz))
				return pClasses.getByValue(clazz);
			return null;
		}

		private PlayerClass safeConstructClass(Class clazz, Player player) {
			return safeConstructClass(clazz, UUIDUtil.compressUUID(player.getUniqueId()));
		}

		private PlayerClass safeConstructClass(Class c, String playerIdentifier) {
			String classID = getClassIDbyClass(c);
			OldMMOPlayer player = MMOPlayerManager.getInstance().getPlayer(playerIdentifier);
			Map<String, PlayerClass> classes = player.getClassMap();
			if (classID != null) {
				if (classes.containsKey(classID))
					return classes.get(classID);
			}

			SafeConstructor sc = new SafeConstructor(c, String.class);

			if (!sc.isValid())
				return null;

			Object o = sc.newInstance(playerIdentifier);

			if (!(o instanceof PlayerClass))
				return null;

			PlayerClass ret = (PlayerClass) o;

			classID = ret.getUniqueID();
			String cs = playerIdentifier + "|" + classID;
			if (LogicUtil.nullOrEmpty(classID))
				log.warning("ClassID for class " + ret.getName() + " is null or empty!");
//			else
//				player.getClassMap().put(classID, ret);

			return ret;
		}

		public PlayerClass safeConstructClass(String classId, Player player) {
			if (player == null)
				throw new IllegalArgumentException("Player cannot be null!");
			return safeConstructClass(classId, player.getUniqueId().toString());
		}

		public PlayerClass safeConstructClass(String classId, String playerIdentifier) {
			if (!pClasses.containsKeyLower(classId))
				return null;

			Class c = pClasses.getLower(classId);
			if (c == null)
				return null;

			return safeConstructClass(c, playerIdentifier);
		}

		private void addDefaults() {
			for (Class<? extends PlayerClass> clazz : classes)
				registerPlayerClass(clazz);
		}

		/**
		 * Creates a new class based on serialized data maps.
		 * <br/>
		 * This will also load the data associated with the serialized method.
		 * @param data map of serialized data.
		 */
		public PlayerClass safeConstructClass(Map<String, Object> data) {
			final String uuid = data.get("class.uuid").toString();
			final String playerIdent = data.get("player-ident").toString();

			PlayerClass ret = safeConstructClass(uuid, playerIdent);
			if (ret != null) ret.onLoad(data);

			return ret;
		}
	}

}
