package com.midi_automator.view;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

/**
 * A cachable, blinkable JList.
 * 
 * @author aguelle
 * 
 */
public class CacheableBlinkableToolTipJList<T> extends JList<T> implements
		ICacheable, IBlinkable {

	private static final long serialVersionUID = 1L;

	private ICacheable cacheableImpl;
	private IBlinkingStrategy blinkingStrategy;
	private int selectedIndex;

	public CacheableBlinkableToolTipJList() {
		super();
		initialize();
	}

	public CacheableBlinkableToolTipJList(ListModel<T> dataModel) {
		super(dataModel);
		initialize();
	}

	public CacheableBlinkableToolTipJList(T[] listData) {
		super(listData);
		initialize();
	}

	public CacheableBlinkableToolTipJList(Vector<? extends T> listData) {
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
	 * Gets the location of the selected list entry
	 * 
	 * @return the location
	 */
	public Point getSelectedLocation() {

		int selectedIndex = getSelectedIndex();

		if (selectedIndex < 0) {
			selectedIndex = 0;
		}

		return new Point(getLocation().x, selectedIndex * getSelectionHeight());
	}

	/**
	 * Gets the height of a selection in pixels
	 * 
	 * @return the height in pixels
	 */
	public int getSelectionHeight() {
		return 33;
	}

	/**
	 * Gets the number of viewable items in the list
	 * 
	 * @return
	 */
	private int getNumberOfViewableItems() {
		return 7;
	}

	/**
	 * Calculates the index of the file list that has to be visible so that the
	 * selected index is on top.
	 * 
	 * @param selectedIndex
	 *            The current selected index
	 * @return the index that has to be visible
	 */
	private int getVisibleIndex(int selectedIndex) {

		int maxIndex = getModel().getSize() - 1;
		int lastVisibleIndex = getLastVisibleIndex();
		int viewableItems = getNumberOfViewableItems();
		int visibleIndex = 0;

		if (selectedIndex + viewableItems > maxIndex) {
			visibleIndex = maxIndex;
		} else {
			visibleIndex = selectedIndex + (viewableItems - 1);
		}

		if (lastVisibleIndex > visibleIndex) {
			visibleIndex = visibleIndex - (viewableItems - 1);
		}
		// || selectedIndex > lastVisibleIndex) {
		// visibleIndex = selectedIndex + viewableItems - 1;
		// }

		return visibleIndex;
	}

	/**
	 * Ensures that the selected index is on top of the list.
	 * 
	 * @param index
	 *            The index
	 */
	public void ensureIndexIsOnTop(int index) {
		ensureIndexIsVisible(getVisibleIndex(index));
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

	@Override
	public void startBlinking() {
		blinkingStrategy.startBlinking();
	}

	@Override
	public void setBlinkingStrategy(IBlinkingStrategy blinkingStrategy) {
		this.blinkingStrategy = blinkingStrategy;
	}
}
