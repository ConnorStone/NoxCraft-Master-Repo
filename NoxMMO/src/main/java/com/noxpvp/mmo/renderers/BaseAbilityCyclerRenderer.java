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

package com.noxpvp.mmo.renderers;

import com.noxpvp.core.runners.RenderRunner;
import com.noxpvp.mmo.AbilityCycler;

public abstract class BaseAbilityCyclerRenderer extends RenderRunner {
	
	public static final BaseAbilityCyclerRenderer	dummy	= new BaseAbilityCyclerRenderer() {
																
																@Override
																public void renderCurrent() {
																	
																}
																
																@Override
																public void renderNext() {
																	
																}
																
																@Override
																public void renderPrevious() {
																	
																}
															};
	
	private final AbilityCycler						cycler;
	
	public BaseAbilityCyclerRenderer(AbilityCycler cycler) {
		this.cycler = cycler;
	}
	
	private BaseAbilityCyclerRenderer() {
		this(null);
	}
	
	public AbilityCycler getCycler() {
		return cycler;
	}
	
	public final void render() {
		renderCurrent();
	}
	
	/**
	 * Renders a view provided by the current through
	 * {@link com.noxpvp.mmo.AbilityCycler#current()}.
	 */
	public abstract void renderCurrent();
	
	/**
	 * Renders a view provided by the
	 * {@link com.noxpvp.mmo.AbilityCycler#peekNext()} method.
	 */
	public abstract void renderNext();
	
	/**
	 * Renders a view provided by the
	 * {@link com.noxpvp.mmo.AbilityCycler#peekPrevious()} method.
	 */
	public abstract void renderPrevious();
}
