package eu.europa.ec.etrustex.services.mock;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
 
public class CacheBase<K, V> {
 
    private static CacheManager cacheManager;
 
    public static void setCacheManager(CacheManager cacheManager) {
        CacheBase.cacheManager = cacheManager;
    }
 
    private String cacheName;
 
    public CacheBase(String cacheName) {
        this.cacheName = cacheName;
    }
 
    public void cache(K key, V value) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new RuntimeException("Cannot get cache from cache manager");
        }
        cache.put(key, value);
    }
}
