package de.tieffrequent.midi.automator.view;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * A cacheable JButton.
 * 
 * @author aguelle
 * 
 */
public class CacheableJButton extends JButton implements ICacheable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ICacheable cacheableImpl;

	public CacheableJButton() {
		super();
		initialize();
	}

	public CacheableJButton(Action a) {
		super(a);
		initialize();
	}

	public CacheableJButton(Icon icon) {
		super(icon);
		initialize();
	}

	public CacheableJButton(String text, Icon icon) {
		super(text, icon);
		initialize();
	}

	public CacheableJButton(String text) {
		super(text);
		initialize();
	}

	/**
	 * Initializes the ICachable delegate.
	 */
	private void initialize() {
		cacheableImpl = new CacheableImpl();
	}

	@Override
	public void setCache(Object object) {
		cacheableImpl.setCache(object);
	}

	@Override
	public Object getCache() {
		return cacheableImpl.getCache();
	}

}
