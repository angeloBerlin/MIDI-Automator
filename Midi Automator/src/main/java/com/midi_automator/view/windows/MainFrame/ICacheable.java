package com.midi_automator.view.windows.MainFrame;

/**
 * A cache for arbitrary objects.
 * 
 * @author aguelle
 * 
 */
public interface ICacheable {

	/**
	 * Caches the given object
	 * 
	 * @param object
	 *            The object to cache
	 * 
	 */
	public void setCache(Object object);

	/**
	 * Returns a cached object.
	 * 
	 * @param type
	 *            The type of cache to be returned
	 * @return the cached object, <NULL> if type is not cached
	 */
	public Object getCache();
}
