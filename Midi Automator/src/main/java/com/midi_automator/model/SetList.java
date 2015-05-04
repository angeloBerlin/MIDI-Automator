package com.midi_automator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set list
 * 
 * @author aguelle
 *
 */
public class SetList {

	public SetList() {
		items = new ArrayList<SetListItem>();
	}

	public SetList(List<SetListItem> items) {
		super();
		this.items = items;
	}

	private List<SetListItem> items;

	/**
	 * Gets all items
	 * 
	 * @return a list of items
	 */
	public List<SetListItem> getItems() {
		return items;
	}

	/**
	 * Overrides the complete set list
	 * 
	 * @param items
	 */
	public void setItems(List<SetListItem> items) {
		this.items = items;
	}

	/**
	 * Gets all midi signatures
	 * 
	 * @return a list of midi signatures
	 */
	public List<String> getMidiSignatures() {
		List<String> midiSignatures = new ArrayList<String>();
		for (SetListItem item : items) {
			midiSignatures.add(item.getMidiSignature());
		}
		return midiSignatures;
	}

	/**
	 * Changes the indexes of two items between each other
	 * 
	 * @param index1
	 *            first part of index pair to change
	 * @param index2
	 *            second part of index pair to change
	 */
	public void exchangeIndexes(int index1, int index2) {

		try {
			SetListItem item1 = items.get(index1);
			SetListItem item2 = items.get(index2);

			items.set(index2, item1);
			items.set(index1, item2);

		} catch (IndexOutOfBoundsException e) {

		}
	}

	/**
	 * Clears the set list
	 */
	public void clear() {
		setItems(new ArrayList<SetListItem>());
	}

	/**
	 * Sets a set list item to the desired position
	 * 
	 * @param item
	 *            The item
	 * @param index
	 *            the index of the entry, if <NULL> entry will be added with the
	 *            next available index
	 */
	public void setItem(SetListItem item, Integer index) {

		if (index == null) {
			items.add(item);
		} else {
			items.set(index, item);
		}
	}

	/**
	 * Adds a set list item
	 * 
	 * @param item
	 *            The item
	 */
	public void addItem(SetListItem item) {
		setItem(item, null);
	}
}
