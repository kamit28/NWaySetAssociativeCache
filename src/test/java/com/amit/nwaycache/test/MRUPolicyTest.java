/**
 * 
 */
package com.amit.nwaycache.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amit.nwaycache.cache.NWayCache;
import com.amit.nwaycache.cache.NWayCacheImpl;
import com.amit.nwaycache.eviction.MRUPolicy;
import com.amit.nwaycache.model.CacheConfig;

/**
 * @author Anshu
 *
 */
public class MRUPolicyTest {

	private NWayCache<Integer, String> cache;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		cache = NWayCacheImpl.getCache();
		MRUPolicy mru = new MRUPolicy(4, 4);
		CacheConfig config = new CacheConfig(16, 4, mru);
		Field field = cache.getClass().getDeclaredField("config");
		field.setAccessible(true);
		field.set(cache, config);
		field.setAccessible(false);
		assertTrue(cache.getCachePolicy() instanceof MRUPolicy);
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
	@After
	public void tearDown() throws Exception {
		cache.clear();
		cache = null;
	}

	/**
	 * Test method for {@link com.amit.nwaycache.eviction.MRUPolicy#evict(int)}.
	 */
	@Test
	public final void testEvictWhenNoElementAccessed() {
		cache.put(64, "Sixty Four");
		// key 48 should have got evicted
		String value = cache.get(48);
		assertNull(value);
	}

	/**
	 * Test method for {@link com.amit.nwaycache.eviction.LRUPolicy#evict(int)}.
	 */
	@Test
	public final void testEvictWhenElementAccessed() {
		// access this so that this key gets evicted.
		cache.get(32);
		cache.put(64, "Sixty Four");
		// key 48 should not got evicted
		String value = cache.get(48);
		assertNotNull(value);
		// key 32 must have got evicted
		value = cache.get(32);
		assertNull(value);
	}
}
