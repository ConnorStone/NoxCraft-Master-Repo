package com.noxpvp.homes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.OfflinePlayer;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;

public class HomesPlayer extends BaseNoxPlayerAdapter implements Persistant {
	
	/**
	 * Instantiates a new homes playerRef.
	 *
	 * @param playerName the name of the playerRef.
	 */
	public HomesPlayer(String playerName)
	{
		super(playerName);
	}
	
	/**
	 * Instantiates a new homes playerRef.
	 *
	 * @param playerRef an OfflinePlayer
	 */
	public HomesPlayer(OfflinePlayer player)
	{
		super(player);
	}
	
	public HomesPlayer(NoxPlayerAdapter player)
	{
		super(player);
	}
	
	/**
	 * Gets the list of homes.
	 *
	 * @return homes List
	 */
	public List<BaseHome> getHomes() {
		List<BaseHome> homes = new ArrayList<BaseHome>();
		ConfigurationNode data = getPersistantData().getNode("homes");
		for (String node : data.getKeys())
			homes.add(data.get(node, BaseHome.class));
		return Collections.unmodifiableList(homes);
	}
	
	public int getHomeCount() {
		return getPersistantData().getNode("homes").getKeys().size();
	}
	
	/**
	 * Gets the home names.
	 *
	 * @return the home names
	 */
	public List<String> getHomeNames()
	{
		List<String> names = new ArrayList<String>();
		for (BaseHome home : getHomes())
			names.add(home.getName());
		return Collections.unmodifiableList(names);
	}
	
	/**
	 * Checks for homes.
	 *
	 * @return true, if successful
	 */
	public boolean hasHomes() {
		return getHomeCount() > 0;
	}

	
	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#load()
	 */
	public void load() {
		//Data is already preloaded.
	}
	
	/**
	 * Adds the home to data.
	 * <br/>
	 * Saves data after completion.
	 * @param home of type BaseHome to add.
	 */
	public void addHome(BaseHome home)
	{
		ConfigurationNode node = getPersistantData().getNode("homes");
		node.set(home.getName(), home);
		saveToManager();
	}
	
	/**
	 * Removes the home from data.
	 * <br/>
	 * Saves data after completion.
	 *
	 * @param home to remove.
	 * @return true if successful.
	 */
	public boolean removeHome(BaseHome home)
	{
		ConfigurationNode node = getPersistantData().getNode("homes");
		
		node.remove(home.getName());
		saveToManager();
		return !node.contains(home.getName());
	}


	/**
	 * Sets the homes.
	 * <br/>
	 * Saves data after completion.
	 * @param list the replacement list of homes.
	 */
	protected final void setHomes(List<BaseHome> list) {
		ConfigurationNode node = getPersistantData().getNode("homes");
		
		node.clear();
		for (BaseHome home: list)
			node.set(home.getName(), home);
		
		saveToManager();
	}

	/**
	 * Gets the specified home.
	 *
	 * @param name of the home
	 * @return the home
	 */
	public BaseHome getHome(String name) {
		if (name == null)
			return getHome(DefaultHome.PERM_NODE);

		return getPersistantData().get("homes."+name, BaseHome.class);
	}

	@Override
	public void save() {
		//Data is automatically saved on edit...
	}
	
}
