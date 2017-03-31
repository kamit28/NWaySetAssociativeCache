package com.amit.nwaycache.model;

/**
 * This class maintains the statistics of the cache instance. Statistics can
 * help in performance and accuracy measurement of the cache.
 * 
 * @author Amit
 *
 */
public class Stats implements Cloneable {
	long cacheHits;
	long cacheMisses;
	long numEvictions;
	long numUpdates;
	int size;

	/**
	 * @param cacheHits
	 * @param cacheMisses
	 * @param numEvictions
	 * @param numUpdates
	 * @param size
	 */
	public Stats(long cacheHits, long cacheMisses, long numEvictions,
			long numUpdates, int size) {
		this.cacheHits = cacheHits;
		this.cacheMisses = cacheMisses;
		this.numEvictions = numEvictions;
		this.numUpdates = numUpdates;
		this.size = size;
	}

	/**
	 * @return the cacheHits
	 */
	public long getCacheHits() {
		return cacheHits;
	}

	/**
	 * @return the cacheMisses
	 */
	public long getCacheMisses() {
		return cacheMisses;
	}

	/**
	 * @return the numEvictions
	 */
	public long getNumEvictions() {
		return numEvictions;
	}

	/**
	 * @return the numUpdates
	 */
	public long getNumUpdates() {
		return numUpdates;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("Stats [cacheHits=%s, cacheMisses=%s, numEvictions=%s, numUpdates=%s, size=%s]",
						cacheHits, cacheMisses, numEvictions, numUpdates, size);
	}

	/**
	 * increments the numUpdates by one.
	 */
	public void incrementNumUpdates() {
		++numUpdates;
	}

	/**
	 * increments the numEvictions by one.
	 */
	public void incrementNumEvictions() {
		++numEvictions;
	}

	/**
	 * increments the cacheHits by one.
	 */
	public void incrementCacheHits() {
		++cacheHits;
	}

	/**
	 * increments the cacheMisses by one.
	 */
	public void incrementCacheMisses() {
		++cacheMisses;
	}

	@Override
	public Object clone() {
		final Stats clone = new Stats(cacheHits, cacheMisses, numEvictions,
				numUpdates, size);
		return clone;
	}
}
