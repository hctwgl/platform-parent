package com.fid.thread;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 单例的缓存map
 */

public class CachePool<Key, Value> extends AbstractMap<Key, Value> {

	// 私有化缓存对象实例
	private static CachePool cachePool = new CachePool();
	private int maxCount = 1000000;
	private BlockingQueue<Entry> queue = new LinkedBlockingQueue<Entry>();

	/**
	 * private Constructor.
	 * 
	 * @return
	 */
	private CachePool() {
	}

	/**
	 * 开放一个公有方法，判断是否已经存在实例，有返回，没有新建一个在返回
	 * 
	 * @return
	 */
	public static CachePool getInstance() {
		return cachePool;
	}

	/**
	 * The Entry for this Map.
	 * 
	 */
	private class Entry implements Map.Entry<Key, Value> {
		private Key key;
		private Value value;

		public Entry(Key key, Value value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}

		public Key getKey() {
			return key;
		}

		public Value getValue() {
			return value;
		}

		public Value setValue(Value value) {
			return this.value = value;
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param size
	 *            the size of the pooled map;
	 */
	public CachePool(int size) {
		maxCount = size;
	}

	@Override
	public Value put(Key key, Value value) {
		while (queue.size() >= maxCount) {
			queue.remove();
		}
		queue.add(new Entry(key, value));
		return value;
	}

	@Override
	public Value get(Object key) {
		for (Iterator<Entry> iter = queue.iterator(); iter.hasNext();) {
			Entry type = iter.next();
			if (type.key.equals(key)) {
				queue.remove(type);
				queue.add(type);
				return type.value;
			}
		}
		return null;
	}

	@Override
	public Set<Map.Entry<Key, Value>> entrySet() {
		Set<Map.Entry<Key, Value>> set = new HashSet<Map.Entry<Key, Value>>();
		set.addAll(queue);
		return set;
	}

	@Override
	public void clear() {
		queue.clear();
	}

	@Override
	public Set<Key> keySet() {
		Set<Key> set = new HashSet<Key>();
		for (Entry e : queue) {
			set.add(e.getKey());
		}
		return set;
	}

	@Override
	public Value remove(Object obj) {
		for (Entry e : queue) {
			if (e.getKey().equals(obj)) {
				queue.remove(e);
				return e.getValue();
			}
		}
		return null;
	}

	@Override
	public int size() {
		return queue.size();
	}
}