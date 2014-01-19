package com.noxpvp.core.permissions;

import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.permissions.IPermissionDefault;
import com.noxpvp.core.NoxPlugin;

public class NoxPermission implements IPermissionDefault {
	private NoxPermission[] children;
	private PermissionDefault defaultPermission;
	private String description;
	private final String name;
	private String node;
	private String[] parents;
	private NoxPlugin plugin;
	
	public NoxPermission(NoxPlugin plugin, String node, String description, PermissionDefault defaults)
	{
		this(plugin, node, description, defaults, new NoxPermission[0]);
	}
	
	public NoxPermission(NoxPlugin plugin, String node, String description, PermissionDefault defaults, NoxPermission... children)
	{
		this.plugin = plugin;
		if (!node.startsWith("nox."))
			this.node = "nox." + node;
		else
			this.node = node;
		
		this.name = this.node;
		this.children = children;
		this.parents = new String[1];
		this.description = description;
		this.defaultPermission = defaults;
		this.parents[0] = this.node.substring(0, this.node.lastIndexOf('.')) + "*";
	}
	
	public NoxPermission[] getChildren()
	{
		return children;
	}
	
	public PermissionDefault getDefault() {
		return defaultPermission;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getParentNodes()
	{
		return parents;
	}

	public NoxPlugin getPlugin() {
		return plugin;
	}
}
