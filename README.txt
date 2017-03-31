----------------------------------------------
N-Way Set Associative Cache
----------------------------------------------

Note** For design details please refer to the Technical Design Document.

----------------------------------------------
1. Dependencies
----------------------------------------------
a. Build dependencies:
JDK 1.8
maven 3.3.9

b. Test dependencies:
junit 4.12

----------------------------------------------
2. API Build
----------------------------------------------
For creating the build go to command-line and execute the following steps:
Note**: We will refer to the source-code directory as PROJECT_DIR environment variable.
	1. cd $PROJECT_DIR
	2. mvn clean install
		or mvn clean package
	3. if the build was successful run command - cd target and Find the NWayCache.jar file
	
----------------------------------------------
3. API documents
----------------------------------------------
For generating the API documents go to command-line and execute the following steps:
Note**: We will refer to the source-code directory as PROJECT_DIR environment variable.
	1. cd $PROJECT_DIR
	2. mvn javadoc:javadoc
	3. if the build was successful run command - cd target and Find the java docs in
		target/site/apidocs directory. The API doc index file will be index.html

----------------------------------------------
4. Cache Configuration
----------------------------------------------
The configuration file name is cache.properties. It can be found at $PROJECT_DIR/src/main/resources

cache.lineSize -- set it to the desired value of N (slots in a set). N must be greater than 1.

cache.size -- set this property to number of elements in cache. this must be a multiple of cache.lineSize

cache.evictionPolicy.implClass -- set it to the name of the class implementing the cache replacement alogorithm

Sample setup:

#Listing 1.	cache.properties:

cache.lineSize=4
cache.size=1024
cache.evictionPolicy.implClass=com.amit.nwaycache.eviction.LRUPolicy

----------------------------------------------
4. How to use the Cache
----------------------------------------------
Steps:
	1. Add NWayCache.jar in your CLASSPATH or use maven dependency management
		for maven projects
		Example: CLASSPATH=$CLASSPATH:<path-to-jar>/NWayCache.jar
	
	2. If you have your own implementation of the cache replacement algorithm,
		add the implementation class in to CLASSPATH or use maven dependency 
		management for maven projects
		
		See section #5 for instructions to implement your own cache replacement
		algorithm.
	
	3. Make sure that the cache.properties file is properly configured and
		added to the CLASSPATH
	
	4. Create your cache client implementation and run it. See section #6 for
		a usage example.
	
----------------------------------------------
5. How to implement your own replacement policy
----------------------------------------------
1. Create a class by implementing the EvictionPolicy interface
2. Override the evict and (optionally) update methods and provide your implementation.

Example:
	if I want to implement a customize algorithm like LFU (Least Frequently Used)
	that chooses the victim based on the hitCount or the number of times the
	element has been accessed, I would write the following class:

#Listing 2.	LFUPolicy.java

#Code Starts Here

package com.amit.custom.policy;

import java.util.ArrayList;
import java.util.List;

import com.amit.nwaycache.eviction.EvictionPolicy;

public class LFUPolicy implements EvictionPolicy  {

	class LFUStats {
		private long hitCount;

		/**
		 * @param hitCount
		 */
		public LFUStats(long hitCount) {
			this.hitCount = hitCount;
		}

		public LFUStats() {
		}

		/**
		 * @return the hitCount
		 */
		public long getHitCount() {
			return hitCount;
		}

		/**
		 * @param hitCount
		 *            the hitCount to set
		 */
		public void setHitCount(long hitCount) {
			this.hitCount = hitCount;
		}

	}

	private List<List<LFUStats>> evictionList;

	public LFUPolicy(int numSets, int lineSize) {
		evictionList = new ArrayList<List<LFUStats>>();
		for (int i = 0; i < numSets; i++) {
			List<LFUStats> list = new ArrayList<>(lineSize);
			for (int j = 0; j < lineSize; j++) {
				list.add(new LFUStats());
			}
			evictionList.add(list);
		}
	}

	@Override
	public int evict(int setNum) {
		List<LFUStats> set = evictionList.get(setNum);
		long leastHitCount = set.get(0).getHitCount();
		int index = 0;
		for (int i = 0; i < set.size(); i++) {
			if (set.get(i).hitCount < leastHitCount) {
				leastHitCount = set.get(i).hitCount;
				index = i;
			}
		}
		return index;
	}

	@Override
	public void update(int setNum, int index) {
		List<LFUStats> setElements = evictionList.get(setNum);
		LFUStats stats = setElements.get(index);
		stats.hitCount++;
	}
}
#Code ends here

The cache.properties would look as below:
#Listing 3. cache.properties
cache.lineSize=4
cache.size=1024
cache.evictionPolicy.implClass=com.amit.custom.policy.LFUPolicy

----------------------------------------------
6. How to use the Cache
----------------------------------------------
To use the cache please follow the example listing below:

#Listing 4.	NWayCacheClient.java

# Code starts here

package com.amit.client;

import com.amit.nwaycache.cache.NWayCache;
import com.amit.nwaycache.cache.NWayCacheImpl;

public class NWayCacheClient {
	public static void main(String[] args) throws Exception {

		// Create the cache instance
		NWayCache<String, String> cache = NWayCacheImpl.getCache();

		// put values in cache
		cache.put("1", "One");
		cache.put("12", "Twelve");
		cache.put("23", "Twenty Three");
		cache.put("34", "Thirty Four");

		// Get values from cache and pint them
		System.out.println(cache.get("1"));
		System.out.println(cache.get("34"));
		System.out.println(cache.get("1"));
		System.out.println(cache.get("23"));
		System.out.println(cache.get("1"));
		System.out.println(cache.get("23"));
		System.out.println(cache.get("12"));
		System.out.println(cache.get("12"));
		
		// cache replacement algo check
		cache.put("45", "Forty Five");

		// replace a value in the cache
		cache.put("45", "Forty Six");
		
		System.out.println(cache.get("45"));
		
		System.out.println(cache.get("1"));
		System.out.println(cache.get("12"));
		System.out.println(cache.get("23"));
		System.out.println(cache.get("34"));

		// Print cache statistics
		System.out.println(cache.getStats().toString());
	}
}
	
# Code ends here

----------------------------------------------
7. Meta
----------------------------------------------
Author:	Amit Kumar