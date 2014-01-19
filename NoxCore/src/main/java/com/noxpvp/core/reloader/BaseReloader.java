package com.noxpvp.core.reloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseReloader implements Reloader {
	private final String name;
	private Reloader parent;
	protected Map<String, Reloader> reloaders;
	
	public BaseReloader(Reloader parent, String name)
	{
		this.name = name;
		this.parent = parent;
		
		reloaders = new HashMap<String, Reloader>();
	}
	
	public boolean addModule(Reloader module) {
		final String name = module.getName();
		
		if (reloaders.containsKey(name))
			return false;
		else
			reloaders.put(name, module);
		
		return true;
	}

	public String getCurrentPath() {
		Reloader reloader = this;
		Reloader prev = null;
		StringBuilder sb = new StringBuilder();
		sb.append(reloader.getName());
		while ((reloader = reloader.getParent()) != prev)
			sb.insert(0, '.').insert(0, (prev = reloader).getName());
		
		return sb.toString();
	}
	
	/**
	 * Fetches the specified Reloader Module.
	 * <br/><br/>
	 * <b>Remember to null check the return value. Value may be null</b>
	 * @param path - Name of the module to fetch. ':' denotes a path.
	 * @return Reloader object or null if module does not exist.
	 */
	public Reloader getModule(String path) {
		Reloader section = this;
		int i1 = -1, i2;
		while ((i1 = path.indexOf(':', i2 = i1 + 1)) != -1) {
			section = section.getModule(path.substring(i2, i1));
			if (section == null)
				return null;
		}
		
		if (section == this)
			return ((BaseReloader)section).reloaders.get(path.substring(i2));
		else
			return section.getModule(path.substring(i2));
	}

	public Reloader[] getModules() {
		return new ArrayList<Reloader>(reloaders.values()).toArray(new Reloader[reloaders.size()]);
	}

	public String getName() {
		return name;
	}

	public Reloader getParent() {
		return parent;
	}

	public Reloader getRoot() {
		Reloader reloader = this, prev = null;
		while (prev != (prev = reloader))
			reloader = reloader.getParent();
		return reloader;
	}
	
	public boolean hasModule(String name) {
		return getModule(name) != null;
	}
	
	public boolean hasModules() {
		return !reloaders.isEmpty();
	}

	public boolean hasParent() {
		return parent != null;
	}
	
	public boolean reload(String module) {
		Reloader r;
		if ((r = getModule(module)) != null)
			return r.reload();
		return false;
	}
	
	/**
	 * This implementation always results in true.
	 * <br/>
	 * <br/>
	 * @see Reloader#reloadAll()
	 * @return true
	 */
	public boolean reloadAll() {
		if (hasModules()) {
			for (Reloader r: reloaders.values())
				r.reloadAll();
		}
		return true;
	}
}
