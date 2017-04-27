package com.midi_automator.view.windows.MainFrame.listener;

import java.awt.Component;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;

import javax.swing.JList;

import org.apache.log4j.Logger;

import com.midi_automator.view.windows.MainFrame.IToolTipItem;
import com.midi_automator.view.windows.MainFrame.TransferableJListToolTipItem;

/**
 * Drag listener for the file list. This allows to drag list items from one
 * index to another.
 * 
 * @author aguelle
 *
 */
@org.springframework.stereotype.Component
public class ItemListDragGestureListener implements DragGestureListener {

	static Logger log = Logger.getLogger(ItemListDragGestureListener.class
			.getName());

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {

		Component component = dge.getComponent();

		@SuppressWarnings("unchecked")
		JList<IToolTipItem> fileList = (JList<IToolTipItem>) component;

		IToolTipItem item = fileList.getSelectedValue();
		int index = fileList.getSelectedIndex();

		if (item != null) {
			dge.startDrag(null, new TransferableJListToolTipItem(item, index));
			log.debug("Dragging \"" + item.getValue() + "\"");
		}
	}

}
