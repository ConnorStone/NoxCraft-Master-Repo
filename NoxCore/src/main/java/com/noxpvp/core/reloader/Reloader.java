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

package com.noxpvp.core.reloader;

public interface Reloader {
	/**
	 * Adds module to this reloader. Useful for grouping a module to a master loader.
	 *
	 * @param module - to add.
	 * @return true if added module and false if it already exists.
	 */
	public boolean addModule(Reloader module);

	/**
	 * Returns the path to this module.
	 * <br/><br/>
	 * <i>Any root modules should use Master as current path.</i>
	 *
	 * @return String - Path to this module.
	 */
	public String getCurrentPath();

	/**
	 * Retrieves the specified module.
	 *
	 * @param name - Name of module to retrieve
	 * @return Reloader object of specified name.
	 */
	public Reloader getModule(String name);

	/**
	 * Returns all modules that is part of this reloader.
	 *
	 * @return Reloader array or null if none.
	 */
	public Reloader[] getModules();

	/**
	 * Retrieves the name of the reloader.
	 * <br/><br/>
	 * <i>Please make sure names are semi unique.</i>
	 *
	 * @return String - Name of Reloader.
	 */
	public String getName();

	/**
	 * Retrieves the parent of this Reloader
	 *
	 * @return the parent or self it is the root
	 */
	public Reloader getParent();

	/**
	 * Retrieves the very root of the reloader tree.
	 *
	 * @return Reloader the parent or self if it is the root.
	 */
	public Reloader getRoot();

	/**
	 * Specifies if the named module exists in this reloader.
	 *
	 * @param name - Name of module to check for.
	 * @return true if exists and false if not.<br/> Will always return false if {@link #hasModules()} returns false
	 */
	public boolean hasModule(String name);

	/**
	 * Specifies if this reloader has modules to it.
	 *
	 * @return True if it contains modules. False if it doesn't
	 */
	public boolean hasModules();

	/**
	 * Tells if this is a module of a Master Reloader
	 *
	 * @return true if it is a module and false if its the root.
	 */
	public boolean hasParent();

	/**
	 * Runs the reloading code specified reloader.
	 *
	 * @return true if successful and false if it failed to reload.
	 */
	public boolean reload();

	/**
	 * Reloads the specified module.
	 *
	 * @param module - to reload
	 * @return true if successful and false if it failed to reload that module.
	 * <br/>
	 * <i>Generally will return false if module does not exist.</i>
	 */
	public boolean reload(String module);

	/**
	 * Reloads all submodules and self.
	 *
	 * @return true if successful and false if it failed to reload.<br/>
	 * <b>Warning Most implementations result in true no matter what.</b>
	 */
	public boolean reloadAll();
}
