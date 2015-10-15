package com.midi_automator.view;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class TransferableJListToolTipItem implements Transferable {

	public static DataFlavor toolTipItemFlavor = new DataFlavor(
			TransferableJListToolTipItem.class,
			"An IToolTipItem Object in a JList");

	public static DataFlavor[] supportedFlavors = { toolTipItemFlavor,
			DataFlavor.stringFlavor };

	private IToolTipItem item;
	private int index;

	public TransferableJListToolTipItem(IToolTipItem item, int index) {
		this.item = item;
		this.index = index;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return supportedFlavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {

		if (flavor.equals(toolTipItemFlavor)
				|| flavor.equals(DataFlavor.stringFlavor)) {
			return true;
		}

		return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException {

		if (flavor.equals(toolTipItemFlavor)) {
			return this;
		} else if (flavor.equals(DataFlavor.stringFlavor)) {
			return item.getValue();
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	public IToolTipItem getItem() {
		return item;
	}

	public int getIndex() {
		return index;
	}
}
