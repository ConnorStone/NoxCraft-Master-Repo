package com.noxpvp.homes.tp;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.tp.WarpPoint;
import com.noxpvp.homes.NoxHomes;

public abstract class BaseHome implements WarpPoint, ConfigurationSerializable {
	
	public static final String HOME_NODE		= "nox.homes.home";
	public static final String OTHER_HOME_NODE	= "nox.homes.others.home";
	
	protected String owner;
	protected transient final PermissionHandler permHandler;
	protected SafeLocation warpPoint;
	
	public static BaseHome deserialize(Map<String, Object> data)
	{
		try {
			String owner = data.get("owner").toString();
//			SafeLocation warpPoint = SafeLocation.deserialize((Map<String, Object>) data.get("warpPoint"));
			SafeLocation warpPoint = (SafeLocation) data.get("warpPoint");
			String name = data.get("name").toString();
			
			if (name.equalsIgnoreCase(DefaultHome.PERM_NODE))
				return new DefaultHome(owner, warpPoint.toLocation());
			else
				return new NamedHome(owner, name, warpPoint.toLocation());
		} catch (ClassCastException e) {
			NoxHomes plugin = NoxHomes.getInstance();
			
			plugin.log(Level.SEVERE, "Severe error occured during deserialization of Home class from config.");
			plugin.handle(e);
		}
		catch (Throwable e) {
			NoxHomes plugin = NoxHomes.getInstance();
			
			plugin.log(Level.SEVERE, "Something happened on trying to make new basehome off of config value.");
			NoxCore.getInstance().handle(e);
		}
		return null;
	}
	
	public BaseHome(String owner, Location location) {
		permHandler = NoxHomes.getInstance().getPermissionHandler();
		this.owner = owner;
		this.warpPoint = new SafeLocation(location);
	}
	
	public BaseHome(String owner, Entity e)
	{
		this(owner, e.getLocation());
	}
	
	public BaseHome(Player player)
	{
		this(player.getName(), player.getLocation());
	}
	
	public boolean tryTeleport(Entity entity, boolean multi) {
		if (canTeleport(entity, multi))
			teleport(entity, multi);
		else
			return false;
		return true;
	}
	
	protected String getNode() {
		return HOME_NODE;
	}
	
	protected String getOtherNode() {
		return OTHER_HOME_NODE;
	}
	
	public boolean canTeleport(Player player, boolean multi)
	{
		return player.hasPermission(((isOwner(player))?getNode():getOtherNode()) + (multi?".multi":""));
	}

	public boolean canTeleport(Entity entity, boolean multi) {
		if (entity instanceof Player)
			return canTeleport((Player)entity, multi);
		return false;
	}

	public void teleport(Entity entity, boolean multi) {
		Location loc = getLocation();
		Chunk locChunk = loc.getChunk();
		
		
		entity.setFallDistance(0);
		Entity last = null, current = entity;
		Stack<Entity> b2t = new Stack<Entity>();
		if (multi) {
			if (!loc.getWorld().isChunkLoaded(locChunk))
				locChunk.load();
			
			while (current != null) {
				last = current;
				current = current.getPassenger(); //Get topmost to start from there.
			}
			current = last;
			while (current != null) {
				last = current;
				current = current.getVehicle(); //Retrieve everything below.
				b2t.push(last); //
			}
			
			current = last;
			last = null;
			while (!b2t.empty()) {
				
				Entity e = b2t.pop();
				e.eject();
				e.teleport(loc);
				if (last != null)
					last.setPassenger(e);
				last = e;
			}
		} else
			if (!loc.getWorld().isChunkLoaded(locChunk))
				locChunk.load();
		
			entity.teleport(loc);
	}
	
	public boolean isOwner(Player player)
	{
		return isOwner(player.getName());
	}
	
	public boolean isOwner(String owner)
	{
		return this.owner.equals(owner);
	}
	
	public final String getOwner()
	{
		return owner;
	}
	
	public Location getLocation()
	{
		return warpPoint.toLocation();
	}

	public abstract String getName();
	
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", getName());
		data.put("owner", getOwner());
//		data.put("warpPoint", new SafeLocation(getLocation()).serialize());
		data.put("warpPoint", new SafeLocation(getLocation()));
		
		return data;
	}
}
