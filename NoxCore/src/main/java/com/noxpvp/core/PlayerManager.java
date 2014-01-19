/*
 * 
 */
package com.noxpvp.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.data.CoreBar;
import com.noxpvp.core.data.CoreBoard;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.events.PlayerDataUnloadEvent;

public class PlayerManager implements Persistant {

	protected FileConfiguration config;
	
	private Map<String, CoreBar> coreBars = new HashMap<String, CoreBar>();
	
	private Map<String, CoreBoard> coreBoards = new HashMap<String, CoreBoard>();
	private Map<String, NoxPlayer> players;
	private NoxCore plugin;
	
	public PlayerManager() {
		this(new FileConfiguration(NoxCore.getInstance().getDataFile("players.yml")), NoxCore.getInstance());
		config = new FileConfiguration(NoxCore.getInstance().getDataFile("players.yml"));
		players = new HashMap<String, NoxPlayer>();
	}
	
	public PlayerManager(FileConfiguration conf, NoxCore plugin)
	{
		this.plugin = plugin;
		this.config = conf;
		players = new HashMap<String, NoxPlayer>();
	}
	
	/**
	 * 
	 * @param name The Key
	 * @param bar The CoreBar to add
	 * @throws NullPointerException If the key or CoreBar are null
	 */
	public void addCoreBar(CoreBar bar){
		if (bar == null)
			throw new NullPointerException("Cannot CoreBar to active CoreBar list");
		
		this.coreBars.put(bar.p.getName(), bar);
	}

	/**
	 * 
	 * @param name The Key
	 * @param board The CoreBoard to add
	 * @throws NullPointerException If the key or CoreBoard are null
	 */
	public void addCoreBoard(CoreBoard board){
		if (board == null)
			throw new NullPointerException("Cannot CoreBoard to active CoreBoard list");
		
		this.coreBoards.put(board.p.getName(), board);
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

	/**
	 * 
	 * 
	 * @param name The Key
	 * @return CoreBar The CoreBar
	 * @throws NullPointerException If the key is null
	 */
	public CoreBar getCoreBar(String name){
		if (name == null)
			throw new NullPointerException("Cannot use with null key");
		
		return this.coreBars.containsKey(name) ? this.coreBars.get(name) : new CoreBar(NoxCore.getInstance(), Bukkit.getPlayer(name));
	}
	
	/**
	 * 
	 * @param name The Key
	 * @return CoreBoard The CoreBoard
	 * @throws NullPointerException If the key is null
	 */
	public CoreBoard getCoreBoard(String name){
		if (name == null)
			throw new NullPointerException("Cannot use with null key");
			
		return this.coreBoards.get(name);
	}
	
	
	/**
	 * Gets all the currently active coreBoards
	 * 
	 * @return Collection<CoreBoard> The CoreBoards
	 */
	public Collection<CoreBoard> getCoreBoards(){
		return this.coreBoards.values();
	}
	
	public NoxPlayer[] getLoadedPlayers() {
		return players.values().toArray(new NoxPlayer[0]);
	}
	
	/**
	 * Gets the player.
	 *
	 * @see #getPlayer(String)
	 * @param player the player
	 * @return the player
	 */
	public NoxPlayer getPlayer(OfflinePlayer player) {
		if (player == null)
			return null;
		return getPlayer(player.getName());
	}

	/**
	 * Gets the specified player.
	 *
	 * @param name of the player
	 * @return the player
	 */
	public NoxPlayer getPlayer(String name)
	{
		NoxPlayer player = null;
		if (players.containsKey(name))
			player = players.get(name);
		else {
			player = new NoxPlayer(this, name);
			players.put(name, player);
		}
		return player;
	}

	/**
	 * Gets the player file.
	 *
	 * @see #getPlayerFile(String)
	 * @param noxPlayer the NoxPlayer object
	 * @return the player file
	 */
	public File getPlayerFile(NoxPlayer noxPlayer) {
		return getPlayerFile(noxPlayer.getName());
	}
	
	/**
	 * Gets the player file.
	 *
	 * @param name of the player
	 * @return the player file
	 */
	public File getPlayerFile(String name)
	{
		return NoxCore.getInstance().getDataFile("playerdata", name);
	}

	/**
	 * Gets the player node.
	 * <b>INTERNAL METHOD</b> Best not to use this!
	 * @param name the name
	 * @return the player node
	 */
	public ConfigurationNode getPlayerNode(String name)
	{
		if (isMultiFile())
			return new FileConfiguration(NoxCore.getInstance(), "playerdata"+File.separatorChar+name+".yml");
		else if (config != null)
			return config.getNode("players").getNode(name);
		else
			return null;
	}
	
	public NoxCore getPlugin() { return plugin; }
	
	/**
	 * 
	 * @param name The Key
	 * @return boolean If there is a CoreBar active with the specific key
	 * @throws NullPointerException If the key is null
	 */
	public boolean hasCoreBar(String name) {
		if (name == null)
			throw new NullPointerException("Cannot check with null key");
		
		return this.coreBars.containsKey(name);
	}
	
	

	/**
	 * 
	 * @param name The Key
	 * @return boolean If there is a CoreBoard active with the specific key
	 * @throws NullPointerException If the key is null
	 */
	public boolean hasCoreBoard(String name){
		if (name == null)
			throw new NullPointerException("Cannot check with null key");
			
		return this.coreBoards.containsKey(name);
	}
	
	

	
	
//////// HELPER FUNCTIONS
	/**
	 * Checks if is multi file.
	 * <b>INTERNALLY USED METHOD</b>
	 * @return true, if is using the multi file structure for player data.
	 */
	public boolean isMultiFile() { return NoxCore.isUseUserFile(); }
	
	/**
	 * Checks if is player is in memory.
	 *
	 * @param name of the player.
	 * @return true, if is player in memory
	 */
	public boolean isPlayerInMemory(String name) {
		return players.containsKey(name);
	}
	
	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#load()
	 */
	public void load() {
		Collection<String> pls = players.keySet();

		players.clear();
		config.load();
		
		for (String name : pls)
			loadOrCreate(name);
		
		for (Player player : Bukkit.getOnlinePlayers())
			loadOrCreate(player.getName());
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
		noxPlayer.load();
	}
	
	/**
	 * Load player.
	 *
	 * @param name of the player
	 */
	public void loadPlayer(String name) {
		loadPlayer(getPlayer(name));
	}
	
	/**
	 * 
	 * @param name The key for the CoreBar to remove
	 * @return PlayerManager This instance
	 * @throws NullPointerException If the key is null
	 */
	public PlayerManager removeCoreBar(String name){
		if (name == null)
			throw new NullPointerException("Cannot remove null Key from list");
		
		this.coreBars.remove(name); return this;
	}
	
	/** 
	 * @param name The key for the CoreBoard to remove
	 * @return PlayerManager This instance
	 * @throws NullPointerException If the key is null
	 */
	public PlayerManager removeCoreBoard(String name){
		if (name == null)
			throw new NullPointerException("Cannot remove null Key from list");
		
		this.coreBoards.remove(name); return this;
	}
	
	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#save()
	 */
	public void save() {
		config.save();
	}
	
	/**
	 * Save player.
	 *
	 * @param player the player
	 */
	public void savePlayer(NoxPlayer player) 
	{
		player.save();
	}
	
	/**
	 * Save player.
	 *
	 * @see #savePlayer(NoxPlayer)
	 * @param player of the player
	 */
	public void savePlayer(OfflinePlayer player){
		savePlayer(getPlayer(player));
	}
	
	/**
	 * Save player.
	 *
	 * @see #savePlayer(NoxPlayer)
	 * @param name of the player
	 */
	public void savePlayer(String name) {
		savePlayer(getPlayer(name));
	}
	
	/**
	 * Unload and save player.
	 * 
	 * @see #unloadPlayer(String)
	 * @see #savePlayer(String)
	 * @param name the name
	 */
	public void unloadAndSavePlayer(String name) {
		savePlayer(name);
		unloadPlayer(name);
	}
	
	/**
	 * Unload if player is offline.
	 * <br/>
	 * <b> WARNING IF THERE ARE PLUGINS THAT ARE IMPROPERLY CACHING THIS OBJECT. IT WILL NEVER TRUELY UNLOAD</b>
	 * @param name of the player
	 * @return true, if it unloads the player from memory.
	 */
	public boolean unloadIfOffline(String name) {
		if (isPlayerInMemory(name) && getPlayer(name).getPlayer() != null)
		{
			unloadAndSavePlayer(name);
			return true;
		}
		return false;
	}
	
	/**
	 * Unload the named player.
	 *
	 * @param name of the player
	 */
	public void unloadPlayer(String name) {
		/*PlayerDataUnloadEvent e = */CommonUtil.callEvent(new PlayerDataUnloadEvent(getPlayer(name), false));
		players.remove(name);
	}
}
