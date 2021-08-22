package com.amit.nwaycache.cache;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.amit.nwaycache.eviction.EvictionPolicy;
import com.amit.nwaycache.model.CacheConfig;
import com.amit.nwaycache.model.CacheElement;
import com.amit.nwaycache.model.Stats;

/**
 * Implementation class for NWayCache. Implements the cache using a list of
 * one-dimensional arrays. Each array is a cache line (slot).
 * 
 * @author Amit
 *
 * @param <K>
 *            key class of the cache element.
 * @param <V>
 *            value class of the cache element.
 */
public class NWayCacheImpl<K, V> implements NWayCache<K, V> {

	/**
	 * The Cache data structure. Its a list of arrays where sets are arrays and
	 * there is a list of n such arrays.
	 */
	private List<CacheElement<K, V>[]> cache;

	/**
	 * Configuration class
	 */
	private CacheConfig config;

	/**
	 * Class for maintaining the cache statistics.
	 */
	private final Stats stats;

	/**
	 * The properties file.
	 */
	private static final String propFileName = "cache.properties";

	/**
	 * Static factory method for instantiation the cache. The cache properties
	 * are read from the <tt>cache.properties</tt> file which must be available
	 * on the CLASSPATH.
	 * 
	 * @return cache object.
	 * @throws Exception
	 */
	public static <K, V> NWayCache<K, V> getCache() throws Exception {
		Properties cacheProps = new Properties();
		InputStream inStream = NWayCacheImpl.class.getClassLoader()
				.getResourceAsStream(propFileName);
		if (inStream != null) {
			cacheProps.load(inStream);
		} else {
			throw new FileNotFoundException("Property file: " + propFileName
					+ " was not found on classpath.");
		}
		EvictionPolicy evictionPolicy;
		int cacheSize = 10240;
		int lineSize = 2;
		String policyClass = "com.amit.nwaycache.eviction.LRUPolicy";

		if (null != cacheProps.getProperty("cache.lineSize")) {
			lineSize = Integer.parseInt(cacheProps
					.getProperty("cache.lineSize"));
			if (!(lineSize >= 2)) {
				throw new IllegalArgumentException(
						"Line size must be greater than or equal to 2");
			}
		}

		if (null != cacheProps.getProperty("cache.size")) {
			cacheSize = Integer.parseInt(cacheProps.getProperty("cache.size"));
			if (!(cacheSize >= lineSize && cacheSize % lineSize == 0)) {
				throw new IllegalArgumentException(
						"Cache size must be multiple of lineSize");
			}
		}

		if (null != cacheProps.getProperty("cache.evictionPolicy.implClass")) {
			policyClass = cacheProps
					.getProperty("cache.evictionPolicy.implClass");
		}

		Class<?>[] args = new Class[2];
		args[0] = int.class;
		args[1] = int.class;
		@SuppressWarnings("unchecked")
		Constructor<EvictionPolicy> ct = (Constructor<EvictionPolicy>) Class
				.forName(policyClass).getDeclaredConstructor(args);

		evictionPolicy = ct.newInstance((cacheSize / lineSize), lineSize);

		CacheConfig config = new CacheConfig(cacheSize, lineSize,
				evictionPolicy);
		NWayCache<K, V> cache = new NWayCacheImpl<K, V>(config);
		return cache;
	}

	/**
	 * @param config
	 *            the configuration of the cache
	 */
	@SuppressWarnings("unchecked")
	private NWayCacheImpl(final CacheConfig config) {
		this.config = config;
		this.cache = new ArrayList<>(config.getNumSets());
		for (int i = 0; i < config.getNumSets(); i++) {
			cache.add(new CacheElement[config.getCacheLines()]);
		}
		this.stats = new Stats(0, 0, 0, 0, config.getCacheSize());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void put(final K key, final V value) {
		if (null == key)
			throw new IllegalArgumentException("key can not be null!");
		int hash = key.hashCode();
		int setNum = getSetNum(hash);
		boolean putSucceeded = false;
		CacheElement<K, V> element = new CacheElement<>(key, value);
		CacheElement<K, V>[] set = cache.get(setNum);

		synchronized (set) {
			for (int i = 0; i < set.length && !putSucceeded; i++) {
				if (set[i] == null
						|| (set[i].hashCode() == hash && set[i].getKey()
								.equals(key))) {
					set[i] = element;
					getCachePolicy().update(setNum, i);
					putSucceeded = true;
				}
			}

			if (!putSucceeded) {
				EvictionPolicy policy = getCachePolicy();
				int index = policy.evict(setNum);
				set[index] = element;
				policy.update(setNum, index);
				putSucceeded = true;
			}
		}
		stats.incrementNumUpdates();
		stats.incrementNumEvictions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V get(K key) {
		if (null == key)
			throw new IllegalArgumentException("key can not be null!");
		V value = null;
		int hash = key.hashCode();
		int setNum = getSetNum(hash);
		CacheElement<K, V>[] set = cache.get(setNum);
		synchronized (set) {
			int elementIndex = getElementIndex(setNum, key, hash);
			if (elementIndex != -1) {
				value = set[elementIndex].getValue();
			}
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(K key) {
		if (null == key)
			throw new IllegalArgumentException("key can not be null!");
		int hash = key.hashCode();
		int setNum = getSetNum(hash);
		CacheElement<K, V>[] set = cache.get(setNum);
		synchronized (set) {
			int elementIndex = getElementIndex(setNum, key, hash);
			if (elementIndex != -1) {
				set[elementIndex] = null;
				stats.incrementNumUpdates();
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns start location for the set in which the element should be placed
	 * or searched.
	 * 
	 * @param hash
	 * @return the index of set in which the element should be placed /
	 *         searched.
	 */
	private int getSetNum(int hash) {
		return (hash % config.getNumSets());
	}

	/**
	 * Returns the index of the key in the cache, if found, -1 otherwise.
	 * 
	 * @param setNum
	 *            the set number to be searched.
	 * @param key
	 *            key of the element whose index is required.
	 * @param hash
	 *            hash of the key
	 * @return the index of the element in the cache, if found, -1 otherwise
	 */
	private int getElementIndex(int setNum, K key, int hash) {
		int index = -1;
		CacheElement<K, V>[] set = cache.get(setNum);
		for (int i = 0; i < set.length; i++) {
			if (null == set[i] || set[i].hashCode() != hash) {
				continue;
			} else if (set[i].hashCode() == hash) {
				if (key.equals(set[i].getKey())) {
					index = i;
					stats.incrementCacheHits();
					getCachePolicy().update(setNum, i);
					break;
				}
			}
		}
		if (index == -1)
			stats.incrementCacheMisses();
		return index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void clear() {
		for (CacheElement<K, V>[] set : cache) {
			for (int i = 0; i < set.length; i++) {
				set[i] = null;
			}
		}
		stats.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EvictionPolicy getCachePolicy() {
		return config.getEvictionPolicy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stats getStats() {
		final Stats statsCopy = (Stats) stats.clone();
		return statsCopy;
	}
}
