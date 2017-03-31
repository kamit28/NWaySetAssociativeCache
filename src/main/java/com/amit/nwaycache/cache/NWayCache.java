package com.amit.nwaycache.cache;

import com.amit.nwaycache.eviction.EvictionPolicy;
import com.amit.nwaycache.model.Stats;

/**
 * A N-Way Set Associative cache. User of the cache can instantiate a cache for
 * any class and can define the size, number of slots (N-Way) and the eviction
 * policy for the cache through a configuration property file.
 * <p>
 * 
 * The user can put new elements, search for elements, remove and replace the
 * elements of the cache. User can also clear the cache and get the statistics
 * of the cache.
 * 
 * @author Amit
 *
 */
public interface NWayCache<K, V> {
	/**
	 * Clears the cache.
	 */
	public void clear();

	/**
	 * Returns the value to which the specified key is mapped (cache-hit), or
	 * {@code null} if the cache contains no mapping for the key (cache-miss).
	 * 
	 * @param key
	 *            key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or {@code null}
	 *         if the cache contains no mapping for the key
	 * @throws NullPointerException
	 *             if the specified key is <code>null</code>
	 */
	public V get(K key);

	/**
	 * Returns the EvictionPolicy class for the current instance of Cache.
	 * 
	 * @return EvictionPolicy
	 */
	public EvictionPolicy getCachePolicy();

	/**
	 * Puts the specified value with the specified key in this cache. If all the
	 * cache slots of the set are full, eviction algorithm will be used to evict
	 * the old element to make room for the new element.
	 * 
	 * @param key
	 *            the cache key
	 * @param value
	 *            the value
	 * @exception NullPointerException
	 *                if the key or value is <code>null</code>
	 */
	public void put(K key, V value);

	/**
	 * Removes the mapping of the <tt>element</tt> from the cache if the mapping
	 * is found.
	 * 
	 * @param key
	 *            the key to be removed from cache.
	 * @return true if mapping is found, false otherwise.
	 * @throws NullPointerException
	 *             if the key is <code>null</code>
	 */
	public boolean remove(K key);

	/**
	 * Returns the statistics object for the cache
	 * 
	 * @return the statistics object for the cache
	 */
	public Stats getStats();
}
