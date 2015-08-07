package com.tony.volleydemo.http.core;

import java.util.Collections;
import java.util.Map;

/**
 * 通过与一个字节数组作为数据的字符串键入一个缓存的接口。
 * 
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Jul 30, 2015 8:41:18 AM
 */
public interface Cache {
	// 检索从高速缓存中的条目。
	public Entry get(String key);

	// Adds or replaces an entry to the cache.
	public void put(String key, Entry entry);

	// 执行任何可能长时间运行初始化缓存需要采取的行动;会从一个工作线程调用。
	public void initialize();

	// 无效高速缓存中的条目。
	public void invalidate(String key, boolean fullExpire);

	// Remove an entry from the cache
	public void remove(String key);

	// Empties the cache
	public void clear();

	/**
	 * 数据和元数据被高速缓存返回的条目。
	 * 
	 * @author user
	 */
	public static class Entry {
		// The data returned from cache
		public byte[] data;
		// ETAG的高速缓存一致性。
		public String etag;
		// 这个响应的日期所报告的服务器。
		public long serverDate;
		// 最后修改日期为请求的对象。
		public long lastModified;
		// TTL此记录。
		public long ttl;
		// 软TTL此记录。
		public long softTtl;
		// 从服务器接收不可改变的响应头;必须为非空。
		public Map<String, String> responseHeaders = Collections.emptyMap();

		// 是否過期
		public boolean isExpired() {
			return this.ttl < System.currentTimeMillis();
		}

		// 如果刷新需要从原始数据源真。
		public boolean refreshNeeded() {
			return this.softTtl < System.currentTimeMillis();
		}
	}
}
