package com.amit.nwaycache.model;

import com.amit.nwaycache.eviction.EvictionPolicy;

/**
 * The CacheConfig class keeps the configuration details of a instance of the
 * cache. These configuration attributes are loaded from the cache.properties
 * class during the cache creation, are used to create the instance. They are
 * also used during the life-cycle of the cache instance. <br>
 * Configuration attributes for the cache are:<br>
 * <ul>
 * <li>cacheSize - Total number of elements the cache can hold.
 * <li>cacheLines - Number of slots (n-way) of each set.
 * <li>numSets - Total number of sets in the cache. This is a computed field
 * (cacheSize / cacheLines)
 * <li>evictionPolicy - The eviction or replacement policy to be used by the
 * cache. {@link EvictionPolicy}
 * </ul>
 * 
 * @author Amit
 *
 */
public class CacheConfig {
	/**
	 * Size of the cache. This must be a multiple of <code>cacheLines</code>
	 */
	private int cacheSize;

	/**
	 * Number of sets in the cache. This is a computed attribute.
	 * <code>numSets = cacheSize / cacheLines</code>
	 */
	private int numSets;

	/**
	 * Number of slots per cache set. This must be greater than 1.
	 */
	private int cacheLines;

	/**
	 * The class implementing the cache replacement policy.
	 */
	private EvictionPolicy evictionPolicy;

	/**
	 * @param cacheSize
	 *            number of elements in the cache
	 * @param cacheLines
	 *            number of slots per set
	 * @param evictionPolicy
	 *            class implementing the cache replacement algorithm
	 */
	public CacheConfig(int cacheSize, int cacheLines,
			EvictionPolicy evictionPolicy) {
		this.cacheSize = cacheSize;
		this.cacheLines = cacheLines;
		this.numSets = cacheSize / cacheLines;
		this.evictionPolicy = evictionPolicy;
	}

	/**
	 * @return the cacheLines
	 */
	public int getCacheLines() {
		return cacheLines;
	}

	/**
	 * @return the cacheSize
	 */
	public int getCacheSize() {
		return cacheSize;
	}

	/**
	 * @return the evictionPolicy
	 */
	public EvictionPolicy getEvictionPolicy() {
		return evictionPolicy;
	}

	/**
	 * @param cacheLines
	 *            the cacheLines to set
	 */
	public void setCacheLines(int cacheLines) {
		this.cacheLines = cacheLines;
	}

	/**
	 * @param cacheSize
	 *            the cacheSize to set
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	/**
	 * @param evictionPolicy
	 *            the evictionPolicy to set
	 */
	public void setEvictionPolicy(EvictionPolicy evictionPolicy) {
		this.evictionPolicy = evictionPolicy;
	}

	/**
	 * @return the numSets
	 */
	public int getNumSets() {
		return numSets;
	}

	/**
	 * @param numSets
	 *            the numSets to set
	 */
	public void setNumSets(int numSets) {
		this.numSets = numSets;
	}
}
