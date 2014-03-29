package com.noxpvp.core.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;

public class PlayerManager extends BasePlayerManager<NoxPlayer> implements Persistant {
	
	private static PlayerManager instance;
	private static List<IPlayerManager<?>> managers = new ArrayList<IPlayerManager<?>>();
	
	public static void addManager(IPlayerManager<?> manager) {
		if (PlayerManager.managers.contains(manager))
			return;
		
		PlayerManager.managers.add(manager);
	}
	
	@Override
	public boolean unloadIfOffline(String name) {
		boolean unloaded = super.unloadIfOffline(name);
		if (unloaded)
			unloadPlayer(name);
		return unloaded;
	}
	
	@Override
	public void unloadPlayer(String name) {
		super.unloadPlayer(name);
		for (IPlayerManager<?> manager : managers)
			if (manager != this)
				manager.unloadPlayer(name);
	}
	
	
	public static PlayerManager getInstance() {
		if (instance == null)
			instance = new PlayerManager();
		
		return instance;
	}
	
	public static PlayerManager getInstance(FileConfiguration conf, NoxCore plugin)
	{
		if (instance == null)
			instance = new PlayerManager(conf, plugin);
		
		return instance;
	}
	
	protected FileConfiguration config;
	
	private NoxCore plugin;
	
	protected PlayerManager() {
		this(new FileConfiguration(NoxCore.getInstance().getDataFile("players.yml")), NoxCore.getInstance());
		config = new FileConfiguration(NoxCore.getInstance().getDataFile("players.yml"));
	}

	protected PlayerManager(FileConfiguration conf, NoxCore plugin)
	{
		super(NoxPlayer.class);
		this.plugin = plugin;
		this.config = conf;
	}
	
	@Override
	protected NoxPlayer craftNew(NoxPlayerAdapter adapter) {
		return adapter.getNoxPlayer();
	}
	
	@Override
	protected NoxPlayer craftNew(String name) {
		return new NoxPlayer(this, name);
	}
	
	@Override
	protected Map<String, NoxPlayer> craftNewStorage() {
		return new HashMap<String, NoxPlayer>();
	}

	public List<String> getAllPlayerNames() {
		List<String> ret = new ArrayList<String>();
		if (isMultiFile())
		{
			try {
				for (File f : NoxCore.getInstance().getDataFile("playerdata").listFiles())
					ret.add(f.getName().replace(".yml", ""));
			} catch (NullPointerException e) {//We don't have a nullfix yet...
				 
			}
		} else {
			for (ConfigurationNode node : config.getNode("players").getNodes())
				ret.add(node.getName());
		}
		
		for (NoxPlayer p : getLoadedPlayers())
			if (!ret.contains(p.getName()))
				ret.add(p.getName());
		
		return ret;
	}
	
	public NoxPlayer[] getLoadedPlayers() {
		return getPlayerMap().values().toArray(new NoxPlayer[0]);
	}	
	
/**
	 * ARE YOU CRAZY!?
	 * @deprecated returns the param that was given. 
	 * @param player
	 * @return player param
	 */
	public NoxPlayer getPlayer(NoxPlayer player) {
		return player;
	}
	
	/**
	 * Gets the player file. <br><br>
	 * 
	 * Will not auto move files that are not updated for 1.8 UID.
	 *
	 * New files will grab UID version of file.
	 *
	 * @see #getPlayerFile(String)
	 * @param noxPlayer the NoxPlayer object
	 * @return the player file
	 */
	public File getPlayerFile(NoxPlayer noxPlayer) {
		File old = getPlayerFile(noxPlayer.getName() + ".yml");
		File supposed = getPlayerFile("NEED-UID", noxPlayer.getName() + ".yml");
		File uidF = getPlayerFile(noxPlayer.getUUID().toString() + ".yml");
		if (old.exists() && noxPlayer.getUUID() == null)
			if (FileUtil.copy(old, supposed))
				if (!old.delete())
					old.deleteOnExit(); //Attempt to delete on exit.
		if (noxPlayer.getUUID() == null)
			return supposed;
		else {
			if (supposed.exists()) {
				if (FileUtil.copy(supposed, uidF))
					if (!supposed.delete())
						supposed.deleteOnExit();
			} else if (old.exists()) {
				if (FileUtil.copy(old, uidF))
					if (!old.delete())
						old.deleteOnExit();
			}
			
			return uidF;
			
		}
	}
	
	/**
	 * Gets the player file.
	 *
	 * @param name of the player
	 * @return the player file
	 */
	public File getPlayerFile(String... path)
	{
		String[] oPath = path;
		path = new String[oPath.length+1];
		path[0] = "playerdata";
		
		//Rough Copy.
		for (int i = 0; i < oPath.length; i++)
			path[i+1] = oPath[i];
		
		return NoxCore.getInstance().getDataFile(path);
	}

	/**
	 * Gets the player node.
	 * <b>INTERNAL METHOD</b> Best not to use this!
	 * @param isUID 
	 * @param name the name
	 * @return the player node
	 */
	public ConfigurationNode getPlayerNode(NoxPlayer player)
	{
		if (isMultiFile() && !(player.getPersistantData() instanceof FileConfiguration))
		{
			FileConfiguration c = new FileConfiguration(getPlayerFile(player)); 
			ConfigurationNode old = player.getPersistantData();
			if (old != null)
				for (Entry<String, Object> entry : old.getValues().entrySet()) //Copy data.
					c.set(entry.getKey(), entry.getValue());
			
			return c;
		}
		else if (isMultiFile() && (player.getPersistantData() instanceof FileConfiguration))
			return (FileConfiguration) player.getPersistantData();
		else if (config != null && !isMultiFile())
			if (player.getUUID() == null && player.getName() != null)
				return config.getNode("players").getNode(player.getName());
			else if (player.getUUID() != null)
				return config.getNode("players").getNode(player.getUUID().toString());
			else
				return null;
		else
			return null;
	}
	
	public NoxCore getPlugin() { return plugin; }
	
	//////// HELPER FUNCTIONS
	/**
	 * Checks if is multi file.
	 * <b>INTERNALLY USED METHOD</b>
	 * @return true, if is using the multi file structure for player data.
	 */
	public boolean isMultiFile() { return NoxCore.isUseUserFile(); }
	
	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#load()
	 */
	public void load() {
		Collection<String> pls = getPlayerMap().keySet();

		getPlayerMap().clear();
		config.load();
		
		for (Player player : Bukkit.getOnlinePlayers())
			loadOrCreate(player.getName());
		
		for (String name : pls)
			if (!isLoaded(name))
				loadOrCreate(name);
		
	}
	
	private void loadOrCreate(String name) {
		loadPlayer(getPlayer(name));
	}
	
	/**
	 * Load player.
	 *
	 * @param noxPlayer the NoxPlayer object to load
	 */
	public void loadPlayer(NoxPlayer noxPlayer) {
		ConfigurationNode persistant_data = getPlayerNode(noxPlayer); 

		if (persistant_data != noxPlayer.getPersistantData()) //Remove desyncs...
			noxPlayer.setPersistantData(persistant_data); 
		
		if (persistant_data instanceof FileConfiguration)
		{
			FileConfiguration fNode = (FileConfiguration) persistant_data;
			fNode.load();
		} else {
			load();
		}
		
		for (IPlayerManager<?> manager : managers)
			if (manager != this)
				manager.loadPlayer(noxPlayer);
	}
	
	/**
	 * Load player.
	 *
	 * @param name of the player
	 */
	public void loadPlayer(String name) {
		loadPlayer(getPlayer(name));
	}

	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#save()
	 */
	public void save() {
		if (!isMultiFile())
			config.save();
//		else
//			//TODO: add logging.
	}

	/**
	 * Save player.
	 *
	 * @param player the player
	 */
	public void savePlayer(NoxPlayer player) 
	{
		ConfigurationNode persistant_data = getPlayerNode(player);
		if (persistant_data != player.getPersistantData()) //Remove desyncs...
			player.setPersistantData(persistant_data); 
		
		player.save();
		for (IPlayerManager<?> manager : managers) { //Iterate through all plugin.
			if (manager != this)
				manager.savePlayer(player); 
		}
		
		if (persistant_data instanceof FileConfiguration)
		{
			FileConfiguration configNode = (FileConfiguration) persistant_data;
			configNode.save();
		} else {
			if (config != null)
				config.save();
		}
	}

	/**
	 * Are you crazy!
	 * @deprecated returns the specified argument.. 
	 */
	protected NoxPlayer craftNew(NoxPlayer adapter) {
		return adapter;
	}
}
