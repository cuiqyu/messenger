package com.limpid.messenger.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * map缓存
 *
 * @auther cuiqiongyu
 * @create 2020/5/26 17:38
 */
public class LRUMapCache<K, V> {

    private LinkedHashMap<K, V> cache; // 缓存的容器
    private static long cacheSize; // 最大缓存数量
    private final static float DEFAULT_LOAD_FACTOR = 0.75f; // 扩容因子

    public LRUMapCache(long cacheSize) {
        this.cacheSize = cacheSize;
        cache = new LinkedHashMap<K, V>((int) Math.ceil(cacheSize / DEFAULT_LOAD_FACTOR), DEFAULT_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > cacheSize;
            }
        };
    }

    /**
     * 查询缓存
     *
     * @param key
     * @return
     */
    public synchronized V get(K key) {
        return cache.get(key);
    }

    /**
     * 存入缓存
     *
     * @param key
     * @param value
     */
    public synchronized void put(K key, V value) {
        cache.put(key, value);
    }

    /**
     * 清空所有缓存
     */
    public synchronized void clear() {
        cache.clear();
    }

    /**
     * 当前缓存使用的数量
     *
     * @return
     */
    public synchronized int usedSize() {
        return cache.size();
    }

}
