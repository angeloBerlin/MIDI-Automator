package com.midi_automator.view.windows.MainFrame.listener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.view.windows.MainFrame.ItemList;

/**
 * This handler sets the current list item whenever the selection of the file
 * list changes.
 * 
 * @author aguelle
 *
 */
@Component
public class CurrentSelectionHandler implements ListSelectionListener {

	@Autowired
	private ItemListService itemListService;
	@Autowired
	private ItemList itemList;

	@Override
	public void valueChanged(ListSelectionEvent e) {
		itemListService.setCurrentItem(itemList.getSelectedIndex());
	}
}
