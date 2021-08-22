package com.amit.nwaycache.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amit.nwaycache.cache.NWayCache;
import com.amit.nwaycache.cache.NWayCacheImpl;
import com.amit.nwaycache.eviction.EvictionPolicy;
import com.amit.nwaycache.eviction.LRUPolicy;
import com.amit.nwaycache.model.Stats;

/**
 * @author Amit
 *
 */
public class NWayCacheTest {
	private NWayCache<Integer, String> cache;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		cache = NWayCacheImpl.getCache();
		assertNotNull(cache);
		// For eviction test
		cache.put(0, "Zero");
		cache.put(16, "Twelve");
		cache.put(32, "Thirteen");
		cache.put(48, "Fourteen");
		// For other tests
		cache.put(1, "One");
		cache.put(2, "Two");
		cache.put(3, "Three");
		cache.put(12, "Twelve");
		cache.put(23, "Twenty Three");
		cache.put(34, "Thirty Four");
	}

	/**
	 * Test method for
	 * {@link com.amit.nwaycache.cache.NWayCacheImpl#put(java.lang.Object, java.lang.Object)}
	 * .
	 */
	@Test
	public final void testPutIfKeyIsNull() {
		Integer key = null;
		String value = "null";
		assertThrows(IllegalArgumentException.class, () -> {
			cache.put(key, value);
		});
	}

	/**
	 * Test method for
	 * {@link com.amit.nwaycache.cache.NWayCacheImpl#put(java.lang.Object, java.lang.Object)}
	 * .
	 */
	@Test
	public final void testPutIfKeyIsNotNullAndSlotIsAvaialable() {
		// verify that empty slot is available
		String value = cache.get(2);
		if (null != value) {
			cache.remove(2);
		}
		value = cache.get(2);
		assertNull(value);
		// Now put the mapping back with key = 2
		Integer key = 2;
		value = "TWO";
		cache.put(key, value);
		assertEquals(value, cache.get(key));
	}

	/**
	 * Test method for
	 * {@link com.amit.nwaycache.cache.NWayCacheImpl#put(java.lang.Object, java.lang.Object)}
	 * .
	 */
	@Test
	public final void testPutIfKeyIsNotNullAndNoSlotAvaialableInSet() {
		// Our cache lineSize is 4
		Stats stats = cache.getStats();
		long numEvictions = stats.getNumEvictions();
		cache.put(64, "Sixty Four");
		stats = cache.getStats();
		long newNumEvictions = stats.getNumEvictions();
		assertEquals(numEvictions + 1, newNumEvictions);
	}

	/**
	 * Test method for
	 * {@link com.amit.nwaycache.cache.NWayCacheImpl#get(java.lang.Object)}.
	 */
	@Test
	public final void testGet() {
		String value = cache.get(2);
		assertEquals("Should return true", "Two", value);
		value = cache.get(4);
		assertNull("value should be null", value);
		assertNotEquals("value should be 34", "Thirty Five", value);
	}

	/**
	 * Test method for
	 * {@link com.amit.nwaycache.cache.NWayCacheImpl#remove(java.lang.Object)}.
	 */
	@Test
	public final void testRemove() {
		String value = cache.get(23);
		assertEquals("Value should be Twenty Three", "Twenty Three", value);
		// now lets remove this
		boolean result = cache.remove(23);
		assertTrue(result);
		// try to get the value again
		value = cache.get(23);
		assertNull("value must be null", value);
	}

	/**
	 * Test method for {@link com.amit.nwaycache.cache.NWayCacheImpl#clear()}.
	 */
	@Test
	public final void testClear() {
		cache.clear();
		String value = cache.get(1);
		assertNull("Should be null", value);
	}

	/**
	 * Test method for
	 * {@link com.amit.nwaycache.cache.NWayCacheImpl#getCachePoicy()}.
	 */
	@Test
	public final void testGetCachePoicy() {
		EvictionPolicy policy = cache.getCachePolicy();
		assertNotNull(policy);
		assertTrue(policy instanceof LRUPolicy);
	}
}
