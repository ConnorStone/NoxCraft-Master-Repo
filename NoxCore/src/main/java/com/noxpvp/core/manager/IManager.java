package com.noxpvp.core.manager;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.Persistent;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public interface IManager<T extends Persistent> {
	public void log(Level lv, String msg);

	public FileConfiguration getConfig(String name);

	public Map<UUID, T> getLoadeds();

	public NoxPlugin getPlugin();

	public boolean isLoaded(T object);

	public void unload(T object);

	public void save(T object);

	public void load();
	public void save();

}