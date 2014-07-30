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

package com.noxpvp.core.json;

import java.util.Stack;

import org.json.JSONException;
import org.json.JSONStringer;
import org.json.JSONWriter;

public class TrackedJSONStringer extends JSONStringer {

	private enum ACTION {
		ARRAY, OBJECT
	}

	private Stack<ACTION> actions = new Stack<TrackedJSONStringer.ACTION>();

	public TrackedJSONStringer() {
		super();
	}

	public boolean isCurrentlyArray() {
		if (!actions.isEmpty())
			return actions.peek() == ACTION.ARRAY;
		return false;
	}

	public boolean isCurrentlyObject() {
		if (!actions.isEmpty())
			return actions.peek() == ACTION.OBJECT;
		return false;
	}

	@Override
	public JSONWriter array() throws JSONException {
		JSONWriter ret = super.array();
		actions.add(ACTION.ARRAY);
		return ret;
	}

	@Override
	public JSONWriter object() throws JSONException {
		JSONWriter ret = super.object();
		actions.add(ACTION.OBJECT);
		return ret;
	}

	@Override
	public JSONWriter endArray() throws JSONException {
		JSONWriter ret = super.endArray();
		if (!actions.isEmpty())
			if (actions.peek() == ACTION.ARRAY)
				actions.pop();

		return ret;
	}

	@Override
	public JSONWriter endObject() throws JSONException {
		JSONWriter ret = super.endObject();
		if (!actions.isEmpty())
			if (actions.peek() == ACTION.OBJECT)
				actions.pop();

		return ret;
	}

	public JSONWriter close() throws JSONException {
		while (!actions.isEmpty()) {
			ACTION a = actions.pop();
			if (a.equals(ACTION.ARRAY))
				endArray();
			else if (a.equals(ACTION.OBJECT))
				endObject();
		}

		return this;
	}
}
