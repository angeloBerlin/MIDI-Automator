package com.midi.automator.view;


/**
 * An implementation of ICachable used for delegation pattern.
 * 
 * @author aguelle
 * 
 */
public class CacheableImpl implements ICacheable {

	private Object cache;

	@Override
	public void setCache(Object object) {
		cache = object;
	}

	@Override
	public Object getCache() {
		return cache;
	}

}
