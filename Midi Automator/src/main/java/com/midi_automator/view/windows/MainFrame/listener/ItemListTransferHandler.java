package com.midi_automator.view.windows.MainFrame.listener;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.view.windows.MainFrame.IToolTipItem;
import com.midi_automator.view.windows.MainFrame.ItemList;
import com.midi_automator.view.windows.MainFrame.MainFrame;
import com.midi_automator.view.windows.MainFrame.TransferableJListToolTipItem;

/**
 * The transfer handler for drag and dropping the list items.
 * 
 * @author aguelle
 *
 */
@Component
public class ItemListTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(ItemListTransferHandler.class
			.getName());

	@Autowired
	private ItemList itemList;
	@Autowired
	private MainFrame mainFrame;

	@Autowired
	private ItemListService itemListService;

	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDrop()) {
			return false;
		}

		return support.isDataFlavorSupported(DataFlavor.stringFlavor);
	}

	@Override
	public boolean importData(TransferSupport support) {

		if (!canImport(support)) {
			return false;
		}

		Transferable transferable = support.getTransferable();

		try {
			TransferableJListToolTipItem transferItem = (TransferableJListToolTipItem) transferable
					.getTransferData(TransferableJListToolTipItem.toolTipItemFlavor);
			JList.DropLocation dl = (JList.DropLocation) support
					.getDropLocation();

			int oldIndex = transferItem.getIndex();

			// temporarily save item data before deleting the item
			String entryName = itemListService.getEntryNameByIndex(oldIndex);
			String filePath = itemListService.getEntryFilePathByIndex(oldIndex);
			String programPath = itemListService
					.getEntryProgramPathByIndex(oldIndex);
			String midiListeningSignature = itemListService
					.getMidiFileListListeningSignature(oldIndex);
			String midiSendingSignature = itemListService
					.getMidiFileListSendingSignature(oldIndex);

			// delete the item from the list
			itemListService.deleteItem(transferItem.getIndex());

			// set item to the new index
			int newIndex = dl.getIndex();
			ListModel<IToolTipItem> model = itemList.getModel();

			if (newIndex > model.getSize()) {
				newIndex = model.getSize();
			}

			itemListService.insertItem(newIndex, entryName, filePath,
					programPath, midiListeningSignature, midiSendingSignature,
					false);

			mainFrame.reload();
			log.debug("Moved \"" + entryName + "\" from index " + oldIndex
					+ " to index " + newIndex);

		} catch (UnsupportedFlavorException e) {
			log.error("TransferFlavor for set list item not supported.", e);
			return false;
		} catch (IOException e) {
			log.error("IO failure on dropping set list item.", e);
			return false;
		}

		return true;
	}
}