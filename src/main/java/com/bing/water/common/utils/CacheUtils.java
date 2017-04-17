/**
 *
 */
package com.bing.water.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.bing.water.common.cache.CacheMeta;
import com.bing.water.common.cache.CacheVo;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Cache工具类
 *
 * @version 2013-5-29
 */
public class CacheUtils {

    private static CacheManager cacheManager = SpringContextHolder.getBean(CacheManager.class);

    private static final String SYS_CACHE = "sysCache";

    private static final String META_CACHE = "metaCache";

    private static final String SESSION_CACHE = "sessionsCache";

    public static void putSessionCache(String sessionId, String key, Object value) {
        put(SESSION_CACHE, sessionId + key, value);
    }

    public static Object getSessionCache(String sessionId, String key) {
        return get(SESSION_CACHE, sessionId + key);
    }

    public static void clearSessionCache(String sessionId, String key) {
        remove(SESSION_CACHE, sessionId + key);
    }

    /**
     * 获取所有的缓存对象，Object[]是长度为2的数组，第一个是key、第二个是Map集合
     *
     * @return
     */
    public static List<CacheVo> getMetadatas() {
        List<String> list = getCache(META_CACHE).getKeys();
        List<CacheVo> vos = Lists.newArrayList();
        for (String k : list) {
            List<CacheMeta> metas = Lists.newArrayList();
            // Object o = getCache(META_CACHE).get(k);
            Object o = get(META_CACHE, k);
            if (o != null) {
                Map<String, CacheMeta> map = (Map<String, CacheMeta>) o;
                for (String key : map.keySet()) {
                    if (key != null) {
                        CacheMeta t = map.get(key);
                        if (t != null) {
                            metas.add(t);
                        }
                    }
                }
            }
            Collections.sort(metas, new Comparator<CacheMeta>() {
                @Override
                public int compare(CacheMeta m1, CacheMeta m2) {
                    if (m1 != null && m1.getKey() != null) {
                        return 1;
                    }
                    if (m2 != null && m2.getKey() != null) {
                        return -1;
                    }
                    return m1.getKey().compareTo(m2.getKey());
                }
            });
            vos.add(new CacheVo(k, metas));
        }

        return vos;
    }

    /**
     * 维护缓存元数据
     *
     * @param key
     * @param message
     */
    private static void putMetadata(String key, String message) {
        if (StringUtils.isNotBlank(message)) {
            Object o = get(META_CACHE, message);
            Map<String, CacheMeta> cm = null;
            Date now = new Date();
            if (o == null) {
                cm = Maps.newHashMap();
                put(META_CACHE, message, cm);
            } else {
                cm = (Map<String, CacheMeta>) o;
            }
            CacheMeta m = cm.get(key);
            if (m == null) {
                CacheMeta meta = new CacheMeta(key, now, now);
                cm.put(key, meta);
            } else {
                m.setUpdateDate(now);
            }
        }
    }

    /**
     * 清理元数据
     */
    public static void clearMetadata() {
        List<String> list = getCache(META_CACHE).getKeys();
        for (String key : list) {
            Object o = get(META_CACHE, key);
            if (o != null) {
                Map<String, CacheMeta> map = (Map<String, CacheMeta>) o;
                Set<String> tmps = Sets.newHashSet();
                for (String k : map.keySet()) {
                    Object v = get(SYS_CACHE, k);
                    if (v == null) {
                        tmps.add(k);
                    }
                }
                // 清理map中的值
                for (String t : tmps) {
                    map.remove(t);
                }
                // 如果map中已经空了，就直接清理掉该缓存
                if (map.size() == 0) {
                    remove(META_CACHE, key);
                }
            }
        }

    }

    /**
     * 获取SYS_CACHE缓存
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return get(SYS_CACHE, key);
    }

    /**
     * 写入SYS_CACHE缓存
     *
     * @param key
     * @return
     */
    public static void put(String key, Object value, String message) {
        put(SYS_CACHE, key, value);
        putMetadata(key, message);
        //getCache(SYS_CACHE).getMemoryStoreSize();
    }

    /**
     * 从SYS_CACHE缓存中移除
     *
     * @param key
     * @return
     */
    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }

    /**
     * 按照表结构进行清理
     *
     * @param key
     */
    public static void removeTable(String key) {
        Object o = get(META_CACHE, key);
        if (o != null) {
            Map<String, CacheMeta> map = (Map<String, CacheMeta>) o;
            for (String k : map.keySet()) {
                remove(k);
            }
            remove(META_CACHE, key);
        }
    }

    /**
     * 获取缓存
     *
     * @param cacheName
     * @param key
     * @return
     */
    private static Object get(String cacheName, String key) {
        Element element = getCache(cacheName).get(key);
        return element == null ? null : element.getObjectValue();
    }

    /**
     * 写入缓存
     *
     * @param cacheName
     * @param key
     * @param value
     */
    private static void put(String cacheName, String key, Object value) {
        Element element = new Element(key, value);
        getCache(cacheName).put(element);
    }

    /**
     * 从缓存中移除
     *
     * @param cacheName
     * @param key
     */
    private static void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }

    /**
     * 获得一个Cache，没有则创建一个。
     *
     * @param cacheName
     * @return
     */
    private static Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
            //cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

}
