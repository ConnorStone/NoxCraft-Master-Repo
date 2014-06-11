package com.noxpvp.core.collection;

import java.util.Locale;

/**
 * DuelAccessMap with extra string functions.
 *
 * The code mainly belongs to BKCOMMONLIB as it is derived from that. But implemented to fix the new map type.
 * @param <V>
 */
public class DualAccessStringMap<V> extends DualAccessMap<String, V> {

	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * The lower case version of the key is tested against.
	 * Null keys are supported.
	 *
	 * @param key to test against
	 * @return True if the map contains the key, False if not
	 */
	public boolean containsKeyLower(Object key) {
		return containsKey(key instanceof String ? ((String) key).toLowerCase(Locale.ENGLISH) : null);
	}

	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * The upper case version of the key is tested against.
	 * Null keys are supported.
	 *
	 * @param key to test against
	 * @return True if the map contains the key, False if not
	 */
	public boolean containsKeyUpper(Object key) {
		return containsKey(key instanceof String ? ((String) key).toUpperCase(Locale.ENGLISH) : null);
	}

	/**
	 * Removes the mapping for the specified key from this map if present.
	 * The value mapped to the lower case value of the key is removed.
	 * Null keys are supported.
	 *
	 * @param key to remove
	 * @return the removed value, or null if not found
	 */
	public V removeLower(Object key) {
		return remove(key instanceof String ? ((String) key).toLowerCase(Locale.ENGLISH) : null);
	}

	/**
	 * Removes the mapping for the specified key from this map if present.
	 * The value mapped to the upper case value of the key is removed.
	 * Null keys are supported.
	 *
	 * @param key to remove
	 * @return the removed value, or null if not found
	 */
	public V removeUpper(Object key) {
		return remove(key instanceof String ? ((String) key).toUpperCase(Locale.ENGLISH) : null);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or null if this map contains no mapping for the key.
	 * The value is mapped to the lower case value of the key is returned.
	 * Null keys are supported.
	 *
	 * @param key to get the value of
	 * @return the value, or null if not contained
	 */
	public V getLower(String key) {
		return get(key == null ? null : key.toLowerCase(Locale.ENGLISH));
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or null if this map contains no mapping for the key.
	 * The value is mapped to the upper case value of the key is returned.
	 * Null keys are supported.
	 *
	 * @param key to get the value of
	 * @return the value, or null if not contained
	 */
	public V getUpper(String key) {
		return get(key == null ? null : key.toUpperCase(Locale.ENGLISH));
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 * If the map previously contained a mapping for the key, the old value is replaced.
	 * The value is mapped to the lower case value of the key.
	 * Null keys are supported.
	 *
	 * @param key to put the value at
	 * @param value to put
	 * @return the previous value, or null if there was none
	 */
	public V putLower(String key, V value) {
		return put(key == null ? null : key.toLowerCase(Locale.ENGLISH), value);
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 * If the map previously contained a mapping for the key, the old value is replaced.
	 * The value is mapped to the upper case value of the key.
	 * Null keys are supported.
	 *
	 * @param key to put the value at
	 * @param value to put
	 * @return the previous value, or null if there was none
	 */
	public V putUpper(String key, V value) {
		return put(key == null ? null : key.toUpperCase(Locale.ENGLISH), value);
	}
}
