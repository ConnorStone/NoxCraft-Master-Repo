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

package com.noxpvp.core.collection;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class DualAccessMap<K, V> implements Map<K, V> {

	private LinkedList<K> keys = new LinkedList<K>();
	private LinkedList<V> values = new LinkedList<V>();

	public int size() {
		return keys.size();
	}

	public boolean isEmpty() {
		return keys.isEmpty();
	}

	public boolean containsKey(Object key) {
		return keys.contains(key);
	}

	public boolean containsValue(Object value) {
		return values.contains(value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DualAccessMap that = (DualAccessMap) o;

		if (!keys.equals(that.keys)) return false;
		if (!values.equals(that.values)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = keys.hashCode();
		result = 31 * result + values.hashCode();
		return result;
	}

	/**
	 * Internally uses and returns {@link DualAccessMap#getByKey(Object)}
	 * <br><hr><br>
	 * {@inheritDoc}
	 */
	public V get(Object key) {
		return getByKey(key);
	}

	public K getByValue(Object value) {
		if (!values.contains(value))
			return null;
		return keys.get(values.indexOf(value));
	}

	public V getByKey(Object key) {
		if (!keys.contains(key))
			return null;
		return values.get(keys.indexOf(key));
	}

	public V put(K key, V value) {
		if (keys.contains(key)) {
			int i = keys.indexOf(key);
			keys.set(i, key);
			values.set(i, value);
		}
		if (values.contains(value)) {
			int i = values.indexOf(value);
			keys.set(i, key);
			values.set(i, value);
		} else {
			keys.add(key);
			values.add(value);
		}

		return value;
	}

	public V remove(Object key) {
		return removeByKey(key);
	}

	public V removeByKey(Object key) {
		V value = null;
		int index = keys.indexOf(key);

		if (index >= 0) {
			value = values.get(index);
			values.remove(index);
			keys.remove(index);
		}

		return value;
	}

	public K removeByValue(Object value) {
		K key = null;
		int index = values.indexOf(value);

		if (index >= 0) {
			key = keys.get(index);
			values.remove(index);
			keys.remove(index);
		}

		return key;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		for (java.util.Map.Entry<? extends K, ? extends V> entry : m.entrySet())
			put(entry.getKey(), entry.getValue());
	}

	public void clear() {
		keys.clear();
		values.clear();
	}

	public Set<K> keySet() {
		return new LinkedHashSet<K>(keys);
	}

	public Set<V> valueSet() {
		return new LinkedHashSet<V>(values);
	}

	public Collection<V> values() {
		return valueSet();
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> ret = new LinkedHashSet<Map.Entry<K, V>>();

		for (Entry<K, V> entry : directEntrySet())
			ret.add(entry);

		return ret;
	}

	public Set<Entry<K, V>> directEntrySet() {
		Set<Entry<K, V>> ret = new LinkedHashSet<Entry<K, V>>();

		for (int i = 0; i < size(); i++)
			ret.add(new Entry<K, V>(this, i));

		return ret;
	}

	public static class Entry<K, V> implements java.util.Map.Entry<K, V> {
		DualAccessMap<K, V> origin;
		private int index;

		public Entry(DualAccessMap<K, V> origin, int index) {
			this.origin = origin;
			this.index = index;
		}

		public K getKey() {
			return origin.keys.get(index);
		}

		public V getValue() {
			return origin.values.get(index);
		}

		public K setKey(K key) {
			K ret = getKey();
			origin.keys.set(index, key);

			return ret;
		}

		public V setValue(V value) {
			V ret = getValue();
			origin.values.set(index, value);

			return ret;
		}
	}
}
