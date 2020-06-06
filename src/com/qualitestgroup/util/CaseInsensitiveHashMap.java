package com.qualitestgroup.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A case insensitive HashMap implementation
 * 
 * @author Brian Van Stone
 *
 * @param <V> The type of the value object
 */
public class CaseInsensitiveHashMap<V> extends HashMap<Object, V> {
	
	private static final long serialVersionUID = 5386083312863793822L;
	
	@Override
	public V put(Object key, V val) {
		String k = key.toString().toLowerCase();
		return super.put(k, val);
	}
	
	@Override
	public V get(Object key) {
		String k = key.toString().toLowerCase();
		return super.get(k);
	}
	
	@Override
	public boolean containsKey(Object key) {
		String k = key.toString().toLowerCase();
		return super.containsKey(k);
	}
	
	@Override
	public V remove(Object key) {
		String k = key.toString().toLowerCase();
		return super.remove(k);
	}
	
	@Override
	public void putAll(Map<? extends Object, ? extends V> m) {
		for(Map.Entry<? extends Object, ? extends V> e : m.entrySet()) {
			this.put(e.getKey(), e.getValue());
		}
	}
}
