package com.amit.nwaycache.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This class maintains the statistics of the cache instance. Statistics can
 * help in performance and accuracy measurement of the cache.
 * 
 * @author Amit
 *
 */
public class Stats implements Cloneable {
	private AtomicLong cacheHits;
	private AtomicLong cacheMisses;
	private AtomicLong numEvictions;
	private AtomicLong numUpdates;
	private int size;

	/**
	 * @param cacheHits
	 * @param cacheMisses
	 * @param numEvictions
	 * @param numUpdates
	 * @param size
	 */
	public Stats(long cacheHits, long cacheMisses, long numEvictions,
			long numUpdates, int size) {
		this.cacheHits = new AtomicLong(cacheHits);
		this.cacheMisses = new AtomicLong(cacheMisses);
		this.numEvictions = new AtomicLong(numEvictions);
		this.numUpdates = new AtomicLong(numUpdates);
		this.size = size;
	}

	/**
	 * @return the cacheHits
	 */
	public long getCacheHits() {
		return cacheHits.get();
	}

	/**
	 * @return the cacheMisses
	 */
	public long getCacheMisses() {
		return cacheMisses.get();
	}

	/**
	 * @return the numEvictions
	 */
	public long getNumEvictions() {
		return numEvictions.get();
	}

	/**
	 * @return the numUpdates
	 */
	public long getNumUpdates() {
		return numUpdates.get();
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
		numUpdates.incrementAndGet();
	}

	/**
	 * increments the numEvictions by one.
	 */
	public void incrementNumEvictions() {
		numEvictions.incrementAndGet();
	}

	/**
	 * increments the cacheHits by one.
	 */
	public void incrementCacheHits() {
		cacheHits.incrementAndGet();
	}

	/**
	 * increments the cacheMisses by one.
	 */
	public void incrementCacheMisses() {
		cacheMisses.incrementAndGet();
	}

	@Override
	public Object clone() {
		final Stats clone = new Stats(cacheHits.get(), cacheMisses.get(),
				numEvictions.get(), numUpdates.get(), size);
		return clone;
	}
	
	/**
	 * Clear all statistics
	 */
	public void clear() {
		cacheHits.set(0);
		cacheMisses.set(0);
		numEvictions.set(0);
		numUpdates.set(0);
	}
}
