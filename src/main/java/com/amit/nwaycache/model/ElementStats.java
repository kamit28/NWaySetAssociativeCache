package com.amit.nwaycache.model;

/**
 * 
 * @author Amit
 *
 */
public class ElementStats {

	/**
	 * index of the element that was accessed or modified.
	 */
	private int index;

	/**
	 * last updated time of the element at index.
	 */
	private long lastUpdated;

	/**
	 * 
	 */
	public ElementStats() {
	}

	/**
	 * @param index
	 * @param lastUpdated
	 */
	public ElementStats(int index, long lastUpdated) {
		this.index = index;
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the lastUpdated
	 */
	public long getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
