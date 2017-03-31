package com.amit.nwaycache.eviction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.amit.nwaycache.model.ElementStats;

/**
 * Implements <tt>MRU</tt> or the "Most-Recently used" replacement policy. The
 * element whose last access time was most recent among all in the set will be
 * chosen.
 * 
 * @author Amit
 *
 */
public final class MRUPolicy implements EvictionPolicy {

	/**
	 * This list contains the access / update statistics to be used for eviction
	 * algorithm.
	 */
	private List<List<ElementStats>> evictionList;

	/**
	 * Name of the policy.
	 */
	public static final String POLICY_NAME = "MRU";

	/**
	 * 
	 * @param numSets
	 * @param cacheLines
	 */
	public MRUPolicy(int numSets, int cacheLines) {
		evictionList = new ArrayList<List<ElementStats>>();
		for (int i = 0; i < numSets; i++) {
			List<ElementStats> list = new ArrayList<>(cacheLines);
			for (int j = 0; j < cacheLines; j++) {
				list.add(new ElementStats());
			}
			evictionList.add(list);
		}
	}

	/**
	 * This algorithm will chose the element whose last access time was most
	 * recent among all in the set as the candidate to be replaced. The index of
	 * this element will be returned.
	 * 
	 * @return the index of the most recently used element in the set.
	 */
	@Override
	public int evict(int setNum) {
		List<ElementStats> setElements = evictionList.get(setNum);
		ElementStats element = Collections.max(setElements,
				new ElementStatsComparator());
		return element.getIndex();
	}

	/**
	 * Updated the elements access / update statistics.
	 * 
	 * @param setNum
	 *            index of set which needs to be updated.
	 * @param index
	 *            inside the set to be updated.
	 */
	@Override
	public void update(int setNum, int index) {
		List<ElementStats> setElements = evictionList.get(setNum);
		ElementStats stats = setElements.get(index);
		if (stats.getLastUpdated() == 0L) {
			stats.setIndex(index);
			stats.setLastUpdated(System.nanoTime());
		} else {
			stats.setLastUpdated(System.nanoTime());
		}
	}

	/**
	 * Comparator for ElementStats objects.
	 * 
	 * @author Amit
	 *
	 */
	class ElementStatsComparator implements Comparator<ElementStats> {
		@Override
		public int compare(ElementStats o1, ElementStats o2) {
			if (o1.getLastUpdated() == 0L)
				return -1;
			if (o2.getLastUpdated() == 0L)
				return 1;
			if (o1 == o2)
				return 0;
			long diff = o1.getLastUpdated() - o2.getLastUpdated();
			return (diff == 0) ? 0 : (diff > 0) ? 1 : -1;
		}
	}
}
