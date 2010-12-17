package net.silencily.sailing.security.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataSecurityMap<K,V>{
	
	private static final long serialVersionUID = 1L;

	Map<K,List<V>> map = new HashMap<K,List<V>>(0);
	
	public void put(K key, V value) {
		List<V> list = map.get(key);
		if (list == null) {
			list = new ArrayList<V>();
		}	
		list.add(value);
		map.put(key, list);
	}
	
	public List<V> get(K key) {
		return map.get(key);
	}
	
	
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}
	
}

