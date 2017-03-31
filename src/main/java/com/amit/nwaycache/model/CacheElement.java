/**
 * 
 */
package com.amit.nwaycache.model;

import java.io.Serializable;

/**
 * This class defines the element of the cache.
 * 
 * @author Amit
 *
 */
public class CacheElement<K, V> implements Serializable {
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -2376249297571009843L;

	/**
	 * Key of the mapping.
	 */
	private final K key;

	/**
	 * Value mapped to key.
	 */
	private final V value;

	/**
	 * Computed hashCode of the key. We don't want to compute the hashCode
	 * everyTime.
	 */
	private final int hash;

	/**
	 * 
	 * @param key
	 *            key of the mapping
	 * @param value
	 *            value to be mapped
	 */
	public CacheElement(K key, V value) {
		this.key = key;
		this.value = value;
		hash = (key == null) ? 0 : key.hashCode();
	}

	/**
	 * @return the hash
	 */
	public int getHash() {
		return hash;
	}

	/**
	 * @return the key of the cache element.
	 */
	public final K getKey() {
		return this.key;
	}

	/**
	 * @return the value
	 */
	public final V getValue() {
		return this.value;
	}

	@Override
	public final boolean equals(Object object) {
		if ((object == null) || (!(object instanceof CacheElement))) {
			return false;
		}

		@SuppressWarnings("unchecked")
		CacheElement<K, V> other = (CacheElement<K, V>) object;
		if ((this.key == null) || (other.getKey() == null)) {
			return false;
		}

		return this.key.equals(other.getKey());
	}

	@Override
	public final int hashCode() {
		return hash;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("[ Key:: ").append(key).append(", Value:: ").append(value)
				.append(", hash:: ").append(hash).append(" ]");
		return sb.toString();
	}
}
