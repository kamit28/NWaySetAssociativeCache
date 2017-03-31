package com.amit.nwaycache.eviction;

/**
 * EvictionPolicy interface defines the behavior of the eviction or replacement
 * policy of the cache. User of the cache can define their own EvictionPolicy
 * with custom replacement algorithm by implementing this interface.
 * <p>
 * The <tt>evict</tt> method returns the index of the candidate element.
 * 
 * @author Amit
 *
 */
public interface EvictionPolicy {

	/**
	 * Returns the index of the candidate element to be replaced per the
	 * implemented algorithm.
	 * 
	 * @param setNum
	 *            set number from which to evict
	 * @return the index of the candidate element to be replaced
	 */
	public int evict(int setNum);

	/**
	 * Updates the (optional) eviction list maintained by the eviction policy.
	 * An implementation may choose to no implement this method.
	 * 
	 * @param setNum
	 *            set number where updation will be made.
	 * @param index
	 *            index of the element whose access statistics have changed and
	 *            need updating.
	 */
	public void update(int setNum, int index);
}