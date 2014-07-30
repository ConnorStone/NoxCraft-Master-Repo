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

package org.json;

/**
 * The JSONException is thrown by the JSON.org classes when things are amiss.
 *
 * @author JSON.org
 * @version 2013-02-10
 */
public class JSONException extends RuntimeException {
	private static final long serialVersionUID = 0;
	private Throwable cause;

	/**
	 * Constructs a JSONException with an explanatory message.
	 *
	 * @param message
	 *			Detail about the reason for the exception.
	 */
	public JSONException(String message) {
		super(message);
	}

	/**
	 * Constructs a new JSONException with the specified cause.
	 */
	public JSONException(Throwable cause) {
		super(cause.getMessage());
		this.cause = cause;
	}

	/**
	 * Returns the cause of this exception or null if the cause is nonexistent
	 * or unknown.
	 *
	 * @returns the cause of this exception or null if the cause is nonexistent
	 *		  or unknown.
	 */
	public Throwable getCause() {
		return this.cause;
	}
}
