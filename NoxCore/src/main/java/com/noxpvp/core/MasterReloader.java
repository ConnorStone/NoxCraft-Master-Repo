package com.noxpvp.core;


public final class MasterReloader extends BaseReloader {
	private static final String root = "Master";
	
	public MasterReloader() {
		super(null, root);
	}
	
	/**
	 * @deprecated Method does nothing.
	 * <br/><br/>
	 * 
	 * Must specify a module in {@link #reload(String)} instead or use {@link #reloadAll()} to reload all modules.
	 * <br/><br/>
	 * 
	 * <b> Warning using {@link #reloadALL()} May be intensive due to fact any plugin hooked into this will reload its configs all at once.</b>
	 */
	public boolean reload() {
		return false;
	}

}
