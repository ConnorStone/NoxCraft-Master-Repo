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

package com.noxpvp.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;

import com.bergerkiller.bukkit.common.bases.mutable.LocationAbstract;
import com.noxpvp.core.data.OldNoxPlayer;
import com.noxpvp.core.internal.OldCooldownHandler;
import me.botsko.prism.Prism;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.conversion.BasicConverter;
import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.bergerkiller.bukkit.common.localization.ILocalizationDefault;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.dsh105.holoapi.HoloAPICore;
import com.noxpvp.core.commands.Command;
import com.noxpvp.core.commands.NoxCommand;
import com.noxpvp.core.commands.ReloadCommand;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.listeners.ChatPingListener;
import com.noxpvp.core.listeners.ChestBlockListener;
import com.noxpvp.core.listeners.DeathListener;
import com.noxpvp.core.listeners.LoginListener;
import com.noxpvp.core.listeners.OnLogoutSaveListener;
import com.noxpvp.core.listeners.ServerPingListener;
import com.noxpvp.core.listeners.VoteListener;
import com.noxpvp.core.locales.CoreLocale;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.manager.old.CorePlayerManager;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.BaseReloader;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.StaticCleaner;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.command.TownyAdminCommand;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class NoxCore extends NoxPlugin {
	@SuppressWarnings("unchecked")
	private static final Class<Command>[] commands = (Class<Command>[]) new Class[]{NoxCommand.class, ReloadCommand.class};
	private static NoxCore instance;
	private static boolean useNanoTime = false;
	private static boolean useUserFile = true;
	private ChatPingListener chatPingListener;
	private FileConfiguration config;
	private DeathListener deathListener;
	private FileConfiguration globalLocales;
	private LoginListener loginListener;
	private ServerPingListener pingListener;
	private PermissionHandler permHandler;
	private transient WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>> permission_cache = new WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>>();
	private List<NoxPermission> permissions = new ArrayList<NoxPermission>();
	private OnLogoutSaveListener saveListener;
	private VoteListener voteListener = null;
	private Towny towny = null;
	private WorldGuardPlugin worldGuard = null;
	private HoloAPICore holoAPI = null;
	private Prism prism = null;
	private OldCooldownHandler oldCDH;
	private Command noxCommand;

	//FIXME: Switch to new Cooldown Handler
	//FIXME: Switch to new CorePlayerManager

	public static NoxCore getInstance() {
		return instance;
	}

	private static void setInstance(NoxCore instance) {
		NoxCore.instance = instance;
	}

	/**
	 * @return the useUserFile
	 */
	public static boolean isUseUserFile() {
		return useUserFile;
	}

	/**
	 * @param useUserFile the useUserFile to set
	 */
	public static void setUseUserFile(boolean useUserFile) {
		NoxCore.useUserFile = useUserFile;
	}

	/**
	 * @return the useNanoTime
	 */
	public static synchronized boolean isUsingNanoTime() {
		return useNanoTime;
	}

	/**
	 * @param useNanoTime the useNanoTime to set
	 */
	public static synchronized void setUseNanoTime(boolean useNanoTime) {
		NoxCore.useNanoTime = useNanoTime;
	}

	public final Towny getTowny() {
		return towny;
	}

	public final WorldGuardPlugin getWorldGuard() {
		return worldGuard;
	}

	public final HoloAPICore getHoloAPI() {
		return holoAPI;
	}
	
	public final Prism getPrism() {
		return prism;
	}

	public final boolean isTownyActive() {
		return towny != null && Bukkit.getPluginManager().isPluginEnabled(towny);
	}

	public final boolean isWorldGuardActive() {
		return worldGuard != null && Bukkit.getPluginManager().isPluginEnabled(worldGuard);
	}

	public final boolean isHoloAPIActive() {
		return holoAPI != null && Bukkit.getPluginManager().isPluginEnabled(holoAPI);
	}
	
	public final boolean isPrismActive() {
		return prism != null && Bukkit.getPluginManager().isPluginEnabled(prism);
	}

	@Override
	public void addPermission(NoxPermission permission) {
		NoxPlugin plugin = permission.getPlugin();
		if (!permission_cache.containsKey(plugin))
			permission_cache.put(plugin, new WeakHashMap<String, NoxPermission>());

		Map<String, NoxPermission> cache = permission_cache.get(plugin);

		if (cache == null) {
			log(Level.WARNING, new StringBuilder().append("Failed to initialize plugin reference for permission for plugin ").append(plugin.getName()).append("! Could not cache permissions for that plugin!").toString());
			return;
		}

		if (cache.containsKey(permission.getName()))
			return;
		cache.put(permission.getName(), permission);
		permissions.add(permission);
		if (permission.getChildren().length > 0)
			addPermissions(permission.getChildren());


//		if (permission_cache.containsKey(permission.getName()))
//			return;
//		permission_cache.put(permission.getName(), permission);
//		permissions.add(permission);
//
//		if (permission.getChildren().length > 0)
//			addPermissions(permission.getChildren());

//		if (permission.getParentNodes().length > 0) //Should not be needed since we know we are creating parents before we create nodes. Unless someone develops plugin ontop of this plugin that is not our developers.
//			for (String node : permission.getParentNodes())
//			{
//				NoxPermission perm = new NoxPermission(node, "Parent node of " + permission.getName() + ".", PermissionDefault.OP);
//				addPermission(plugin, perm);
//			}

		plugin.loadPermission(permission);
	}

	@Override
	public void addPermissions(NoxPermission... perms) {
		for (NoxPermission perm : perms)
			addPermission(perm);
	}

	/**
	 * @param r reloader to add.
	 * @return true if successful and false otherwise.
	 * @deprecated Use {@link MasterReloader#getInstance()} instead.
	 */
	public boolean addReloader(Reloader r) {
		return getMasterReloader().addModule(r);
	}

	@Override
	public void disable() {
		saveConfig();
		CorePlayerManager.getInstance().save();

		oldCDH.stop();
		cleanup();
	}

	private void cleanup() {

		HandlerList.unregisterAll(this);
		setInstance(null);

		if (voteListener != null)
			voteListener.destroy();

		Class<?>[] classes = {
				VaultAdapter.class,
				VaultAdapter.GroupUtils.class,
				CoreLocale.class, GlobalLocale.class,
				CorePlayerManager.class, MasterReloader.class
		};

		String[] internalClasses = {};

		new StaticCleaner(this, getClassLoader(), internalClasses, classes).resetAll();


	}

	@Override
	public void enable() {
		if (instance != null && instance != this) {
			log(Level.SEVERE, "This plugin already has an instance running!! Disabling second run.");
			setEnabled(false);
			return;
		} else if (instance == null)
			setInstance(this);

		registerSerials(this);
		for (Plugin p : CommonUtil.getPlugins())
			if (p instanceof NoxPlugin && CommonUtil.isDepending(p, this))
				registerSerials((NoxPlugin) p);

		permHandler = new PermissionHandler(this);

		getMasterReloader();

		Conversion.register(new BasicConverter<OldNoxPlayer>(OldNoxPlayer.class) {
			@Override
			protected OldNoxPlayer convertSpecial(Object object, Class<?> obType, OldNoxPlayer def) {
				if (object instanceof NoxPlayerAdapter)
					return ((NoxPlayerAdapter) object).getNoxPlayer();
				return def;
			}
		});

		Conversion.register(new BasicConverter<SafeLocation>(SafeLocation.class) {
			protected SafeLocation convertSpecial(Object o, Class<?> aClass, SafeLocation def) {
				if (o instanceof Location) return new SafeLocation((Location) o);
				else if (o instanceof LocationAbstract) return new SafeLocation(((LocationAbstract)o).toLocation());
				else if (o instanceof SafeLocation) return (SafeLocation) o;
				else return def;
			}
		});

		chatPingListener = new ChatPingListener();
		voteListener = new VoteListener();
		deathListener = new DeathListener();
		loginListener = new LoginListener();
		saveListener = new OnLogoutSaveListener(this);
		pingListener = new ServerPingListener(this);

		chatPingListener.register();

		if (CommonUtil.isPluginEnabled("Votifier")) //Fixes console error message.
			voteListener.register();

		saveListener.register();
		deathListener.register();
		loginListener.register();
		pingListener.register();

		CommonUtil.queueListenerLast(loginListener, PlayerLoginEvent.class);
		VaultAdapter.load();

		PluginManager pm = Bukkit.getPluginManager();

		{
			Plugin plugin = pm.getPlugin("WorldGuard");
			if (plugin != null && plugin instanceof WorldGuardPlugin)
				worldGuard = (WorldGuardPlugin) plugin;
		}

		{
			Plugin plugin = pm.getPlugin("Towny");
			if (plugin != null && plugin instanceof Towny)
				towny = (Towny) plugin;
		}

		{
			Plugin plugin = pm.getPlugin("HoloAPI");
			if (plugin != null && plugin instanceof HoloAPICore)
				holoAPI = (HoloAPICore) plugin;
		}
		
		{
			Plugin plugin = pm.getPlugin("Prism");
			if (plugin != null && plugin instanceof Prism)
				prism = (Prism) plugin;
		}

		Reloader r = new BaseReloader(getMasterReloader(), "NoxCore") {
			public boolean reload() {
				return false;
			}
		};

		registerAllCommands();

		r.addModule(new BaseReloader(r, "config.yml") {
			public boolean reload() {
				NoxCore.this.reloadConfig();
				return true;
			}
		});

		r.addModule(new BaseReloader(r, "locale") {
			public boolean reload() {
				localization();
				return true;
			}
		});

		r.addModule(new BaseReloader(r, "players") {
			public boolean reload() {
				CorePlayerManager.getInstance().load();
				return true;
			}
		});

		r.addModule(new BaseReloader(r, "group-name") {

			public boolean reload() {
				VaultAdapter.GroupUtils.reloadAllGroupTags();
				return true;
			}
		});

		register(r);
		if (getTowny() != null) {
			r = new BaseReloader(getMasterReloader(), "Towny") {
				public boolean reload() {
					return false;
				}
			};

			r.addModule(new BaseReloader(r, "reload") {

				public boolean reload() {
					PluginCommand cmd = getTowny().getCommand("townyadmin");
					if (cmd == null)
						return false;
					if (!(cmd.getExecutor() instanceof TownyAdminCommand))
						return false;
					((TownyAdminCommand) cmd.getExecutor()).reloadTowny(false);
					return true;
				}
			});

			register(r);
		}

		// ==== Localization ====
		if (!this.globalLocales.isEmpty()) {
			this.saveGlobalLocalization();
		}

		oldCDH = new OldCooldownHandler();

		oldCDH.start();

		reloadConfig();
	}

	/**
	 * Acquires all permission nodes.
	 * <p/>
	 * See addPermission and removePermission for manipulating this list.
	 *
	 * @return an unmodifiable list of permissions
	 */
	public final List<NoxPermission> getAllNoxPerms() {
		return Collections.unmodifiableList(permissions);
	}

	public org.bukkit.configuration.file.FileConfiguration getConfig() {
		return getCoreConfig().getSource();
	}

	@Override
	public NoxCore getCore() {
		return (NoxCore) this;
	}

	public FileConfiguration getCoreConfig() {
		if (config == null)
			config = new FileConfiguration(getDataFile("config.yml"));

		config.load();

		return config;
	}

	@Override
	public String getGlobalLocale(String path, String... arguments) {
		path = path.toLowerCase(Locale.ENGLISH);
		// First check if the path leads to a node
		if (this.globalLocales.isNode(path)) {
			// Redirect to the proper sub-node
			// Check recursively if the arguments are contained
			String newPath = path + ".default";
			if (arguments.length > 0) {
				StringBuilder tmpPathBuilder = new StringBuilder(path);
				String tmpPath = path;
				for (int i = 0; i < arguments.length; i++) {
					tmpPathBuilder.append('.');
					if (arguments[i] == null) {
						tmpPathBuilder.append("null");
					} else {
						tmpPathBuilder.append(arguments[i].toLowerCase(Locale.ENGLISH));
					}
					tmpPath = tmpPathBuilder.toString();
					// New argument appended path exists, update the path
					if (this.globalLocales.contains(tmpPath)) {
						newPath = tmpPath;
					} else {
						break;
					}
				}
			}
			// Update path to lead to the new path
			path = newPath;
		}
		// Regular loading going on
		if (arguments.length > 0) {
			StringBuilder locale = new StringBuilder(this.globalLocales.get(path, ""));
			for (int i = 0; i < arguments.length; i++) {
				StringUtil.replaceAll(locale, "%" + i + "%", LogicUtil.fixNull(arguments[i], "null"));
			}
			return locale.toString();
		} else {
			return this.globalLocales.get(path, String.class, "");
		}
	}

	@Override
	public ConfigurationNode getGlobalLocalizationNode(String path) {
		return this.globalLocales.getNode(path);
	}

	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}

	public PermissionHandler getPermissionHandler() {
		return permHandler;
	}

	/**
	 * @deprecated Use {@link CorePlayerManager#getInstance()} instead.
	 */
	public CorePlayerManager getPlayerManager() {
		return CorePlayerManager.getInstance();
	}

	/**
	 * @deprecated Use {@link MasterReloader#getInstance()} instead.
	 */
	public Reloader getReloader(String path) {
		return getMasterReloader().getModule(path);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends ConfigurationSerializable>[] getSerialiables() {
		return new Class[]{SafeLocation.class, CoolDown.class};
	}

	/**
	 * @deprecated Use {@link MasterReloader#getInstance()} instead.
	 */
	public boolean hasReloader(String path) {
		return getMasterReloader().hasModule(path);
	}

	/**
	 * @deprecated Use {@link MasterReloader#getInstance()} instead.
	 */
	public boolean hasReloaders() {
		return getMasterReloader().hasModules();
	}

	public void loadGlobalLocale(ILocalizationDefault localizationDefault) {
		this.loadGlobalLocale(localizationDefault.getName(), localizationDefault.getDefault());
	}

	public void loadGlobalLocale(String path, String defaultValue) {
		path = path.toLowerCase(Locale.ENGLISH);
		if (!this.globalLocales.contains(path)) {
			this.globalLocales.set(path, defaultValue);
		}
	}

	public void loadGlobalLocales(Class<? extends ILocalizationDefault> localizationDefaults) {
		for (ILocalizationDefault def : CommonUtil.getClassConstants(localizationDefaults))
			this.loadGlobalLocale(def);
	}

	public final void loadGlobalLocalization() {
		this.globalLocales.load();
	}

	@Override
	public void localization() {
		if (instance == null)
			setInstance(this);

		VaultAdapter.load();
		Common.loadClasses("com.noxpvp.core.locales.CoreLocale", "com.noxpvp.core.VaultAdapter");

		globalLocales = new FileConfiguration(this, "Global-Localization.yml");

		// load
		if (this.globalLocales.exists()) {
			this.loadGlobalLocalization();
		}

		// header
		this.globalLocales.setHeader("Below are the global localization nodes set for Nox Plugins '" + this.getName() + "'.");
		this.globalLocales.addHeader("For colors, use the & character followed up by 0 - F");
		this.globalLocales.addHeader("Need help with this file? Please visit:");
		this.globalLocales.addHeader("http://dev.bukkit.org/server-mods/bkcommonlib/pages/general/localization/");

		loadGlobalLocales(GlobalLocale.class);
		loadLocales(CoreLocale.class);

		for (String group : VaultAdapter.GroupUtils.getGroupList()) {
			loadLocale(CoreLocale.GROUP_TAG_PREFIX.getName() + "." + group, "");
			loadLocale(CoreLocale.GROUP_TAG_SUFFIX.getName() + "." + group, "");
		}
	}

	@Override
	public void permissions() {
		addPermission( //Currently does nothing.
				new NoxPermission(this, "core.*", "All noxcore permissions (Including admin nodes).", PermissionDefault.OP,
						new NoxPermission(this, "core.reload", "Reload command for Nox Core", PermissionDefault.OP),
						new NoxPermission(this, "core.save", "Save permission for saving everything in core.", PermissionDefault.OP),
						new NoxPermission(this, "core.load", "Load permission for loading everything in core.", PermissionDefault.OP),
						new NoxPermission(this, "nox.upgrade", "Upgrade permission for the upgrade command in the core.", PermissionDefault.OP)
				)
		);
	}

	private void registerAllCommands() {
		for (Class<Command> cls : commands) {
			SafeConstructor<Command> cons = new SafeConstructor<Command>(cls, new Class[0]);
			Command rn = cons.newInstance();

			if (rn != null) {
				if (rn.getName().equals(NoxCommand.COMMAND_NAME))
					setNoxCommand(rn);
				registerCommand(rn);
			}
		}
	}

	/**
	 * Used to create command tree's unique to nox.
	 * <p>Mainly for if people prefer to type /nox before commands. Mehh Not really big deal.
	 *
	 * @return the master command.
	 */
	public Command getNoxCommand() {
		return this.noxCommand;
	}

	private void setNoxCommand(Command rn) {
		this.noxCommand = rn;
	}

	private void registerSerials(NoxPlugin p) {
		if (p.getSerialiables() != null) {
			log(Level.INFO, new StringBuilder().append("Attempting to load ").append(p.getSerialiables().length).append(" '").append(p.getName()).append("' serializables.").toString());

			for (Class<? extends ConfigurationSerializable> c : p.getSerialiables())
				ConfigurationSerialization.registerClass(c);
		}
	}

	@Override
	public void reloadConfig() {
		getCoreConfig().load();

		loginListener.unregister();
		loginListener = new LoginListener(this);
		loginListener.register();

		pingListener.unregister();
		pingListener = new ServerPingListener(this);
		pingListener.register();

		ChestBlockListener.isRemovingOnInteract = config.get("custom.events.chestblocked.isRemovingOnInteract", ChestBlockListener.isRemovingOnInteract);
		ChestBlockListener.usePlaceEvent = config.get("custom.events.chestblocked.usePlaceEvent", ChestBlockListener.usePlaceEvent);
		ChestBlockListener.useFormEvent = config.get("custom.events.chestblocked.useFormEvent", ChestBlockListener.useFormEvent);

		VaultAdapter.reloadTeams();
	}

	public void removePermission(NoxPlugin plugin, String name) {
		removePermission(plugin, name, false);
	}

	public void removePermission(NoxPlugin plugin, String name, boolean force) {
		if (!permission_cache.containsKey(plugin))
			return;

		Map<String, NoxPermission> cache = permission_cache.get(plugin);

		if (cache.containsKey(name)) {
			Bukkit.getPluginManager().removePermission(name);

			NoxPermission perm = cache.get(name);

			cache.remove(name);
			permissions.remove(perm);

		} else if (force) {
			NoxPermission permFound = null;
			for (NoxPermission perm : permissions)
				if (perm.getName().equals(name)) {
					Bukkit.getPluginManager().removePermission(name);
					permFound = perm;
					break;
				}

			if (permFound != null)
				permissions.remove(permFound);

		}
	}

	@Override
	public void saveConfig() {
		config.set("custom.events.chestblocked.isRemovingOnInteract", ChestBlockListener.isRemovingOnInteract);
		config.set("custom.events.chestblocked.usePlaceEvent", ChestBlockListener.usePlaceEvent);
		config.set("custom.events.chestblocked.useFormEvent", ChestBlockListener.useFormEvent);
		config.save();
	}

	public final void saveGlobalLocalization() {
		this.globalLocales.save();
	}
}