package com.limpid.messenger.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 利用Guava实现本地缓存
 *
 * @auther cuiqiongyu
 * @create 2020/5/27 17:20
 */
public class GuavaCache<V> {

    private final static Logger logger = LoggerFactory.getLogger(GuavaCache.class);

    private LoadingCache<String, V> loadingCache = null; // 缓存对象
    private int maximumSize = 1000; // 指定的最大缓存容量，默认100
    private int expireTime = 30; // 缓存项在给定时间内没有被写访问（创建或覆盖）则自动回收，默认30
    private TimeUnit timeUnit = TimeUnit.SECONDS; // 缓存时间单位，默认s
    private static boolean isRecordStats = false; // 是否开启统计功能，默认不开启

    public GuavaCache() {
        initLoadingCache();
    }

    /**
     * 构建缓存对象
     *
     * @param maximumSize
     * @param expireTime
     * @param timeUnit
     */
    public GuavaCache(int maximumSize, int expireTime, TimeUnit timeUnit) {
        this(maximumSize, expireTime, timeUnit, isRecordStats);
    }

    /**
     * 构建缓存对象
     *
     * @param maximumSize
     * @param expireTime
     * @param timeUnit
     * @param isRecordStats
     */
    public GuavaCache(int maximumSize, int expireTime, TimeUnit timeUnit, boolean isRecordStats) {
        this.maximumSize = maximumSize;
        if (expireTime > 0) {
            this.expireTime = expireTime;
        }
        if (null != timeUnit) {
            this.timeUnit = timeUnit;
        }
        this.isRecordStats = isRecordStats;
        initLoadingCache();
    }

    /**
     * 初始化LoadingCache
     */
    private void initLoadingCache() {
        this.loadingCache = (isRecordStats ? CacheBuilder.newBuilder().recordStats() : CacheBuilder.newBuilder())
                .maximumSize(maximumSize)
                .expireAfterAccess(expireTime, timeUnit)
                .build(new CacheLoader<String, V>() {
                    @Override
                    public V load(String key) throws Exception {
                        return null;
                    }
                });
    }

    /**
     * 获取缓存计数对象
     *
     * @return
     */
    public final CacheStats getStats() {
        CacheStats stats = this.loadingCache.stats();
        return stats;
    }

    /**
     * 手动删除全部缓存
     */
    public final GuavaCache<V> invalidate() {
        this.loadingCache.invalidateAll();
        return this;
    }

    /**
     * 手动删除指定的多个缓存
     *
     * @param keys
     */
    public final GuavaCache<V> invalidate(List<String> keys) {
        this.loadingCache.invalidateAll(keys);
        return this;
    }

    /**
     * 手动删除指定的单个缓存
     *
     * @param key
     */
    public final GuavaCache<V> invalidate(String key) {
        this.loadingCache.invalidate(key);
        return this;
    }

    /**
     * 存入缓存
     *
     * @param key
     * @param value
     */
    public final GuavaCache<V> put(String key, V value) {
        this.loadingCache.put(key, value);
        return this;
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public final V getCache(String key) {
        V v = null;
        if (!StringUtils.isEmpty(key)) {
            try {
                v = this.loadingCache.get(key);
            } catch (Exception e) {
                logger.error("getCache--error，Key:{}，errCause：{}", key, e.getCause());
            }
        }

        return v;
    }

}
