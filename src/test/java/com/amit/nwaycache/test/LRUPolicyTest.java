/**
 * 
 */
package com.amit.nwaycache.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amit.nwaycache.cache.NWayCache;
import com.amit.nwaycache.cache.NWayCacheImpl;

/**
 * @author Amit
 *
 */
public class LRUPolicyTest {

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
		cache.put(16, "Sixteen");
		cache.put(32, "Thirty Two");
		cache.put(48, "Forty Eight");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		cache.clear();
		cache = null;
	}

	/**
	 * Test method for {@link com.amit.nwaycache.eviction.LRUPolicy#evict(int)}.
	 */
	@Test
	public final void testEvictWhenNoElementAccessed() {
		cache.put(64, "Sixty Four");
		// key 0 should have got evicted
		String value = cache.get(0);
		assertNull(value);
	}

	/**
	 * Test method for {@link com.amit.nwaycache.eviction.LRUPolicy#evict(int)}.
	 */
	@Test
	public final void testEvictWhenElementAccessed() {
		// access this so that this key does not get evicted.
		cache.get(0);
		cache.put(64, "Sixty Four");
		// key 0 should not got evicted
		String value = cache.get(0);
		assertNotNull(value);
		// key 16 must have got evicted
		value = cache.get(16);
		assertNull(value);
	}
}
