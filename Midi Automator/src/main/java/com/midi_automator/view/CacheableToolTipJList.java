package com.midi_automator.view;

import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

/**
 * A cahcable JList.
 * 
 * @author aguelle
 * 
 */
public class CacheableToolTipJList<T> extends JList<T> implements ICacheable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ICacheable cacheableImpl;
	private int selectedIndex;

	public CacheableToolTipJList() {
		super();
		initialize();
	}

	public CacheableToolTipJList(ListModel<T> dataModel) {
		super(dataModel);
		initialize();
	}

	public CacheableToolTipJList(T[] listData) {
		super(listData);
		initialize();
	}

	public CacheableToolTipJList(Vector<? extends T> listData) {
		super(listData);
		initialize();
	}

	@Override
	public void setCache(Object object) {
		cacheableImpl.setCache(object);
	}

	@Override
	public Object getCache() {
		return cacheableImpl.getCache();
	}

	@Override
	public String getToolTipText(MouseEvent e) {

		int index = locationToIndex(e.getPoint());
		if (index > -1) {
			Object item = getModel().getElementAt(index);

			if (item instanceof IToolTipItem) {
				return ((IToolTipItem) item).getToolTipText();
			}
		}

		return null;
	}

	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		selectedIndex = index;
	}

	@Override
	public int getSelectedIndex() {
		int index = super.getSelectedIndex();

		if (index < 0) {
			index = selectedIndex;
		}

		return index;
	}

	/**
	 * Sets the last selected index.
	 */
	public void setLastSelectedIndex() {
		setSelectedIndex(selectedIndex);
	}

	/**
	 * Sets the tooltip on a specific list item
	 * 
	 * @param text
	 *            The tooltip text
	 * @param index
	 *            the index of the list item
	 */
	public void setToolTipText(String text, int index) {
		Object obj = getModel().getElementAt(index);

		if (obj instanceof IToolTipItem) {
			((IToolTipItem) obj).setToolTipText(text);
		}
	}

	/**
	 * Initializes the ICachable delegate.
	 */
	private void initialize() {
		cacheableImpl = new CacheableImpl();
		setToolTipText("");
	}

}
