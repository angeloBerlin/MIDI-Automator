package com.midi_automator.view.windows.MainFrame.listener;

import java.awt.event.MouseEvent;

import javax.swing.JList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.view.DeActivateableMouseAdapter;
import com.midi_automator.view.windows.MainFrame.MainFrame;

/**
 * Opens the double clicked item from the item list
 * 
 * @author aguelle
 * 
 */
@Component
public class OpenFileOnDoubleClickListener extends DeActivateableMouseAdapter {

	@Autowired
	MainFrame mainFrame;

	@Autowired
	ItemListService itemListService;

	@Override
	public void mouseClicked(MouseEvent me) {

		if (isActive()) {
			if (me.getSource() instanceof JList) {

				JList<?> fileJList = (JList<?>) me.getSource();
				mainFrame.getItemList().setLastSelectedIndex(
						fileJList.locationToIndex(me.getPoint()));

				// left double click
				if (me.getButton() == MouseEvent.BUTTON1
						&& me.getClickCount() == 2) {
					if (!mainFrame.isPopupWasShown()) {
						itemListService.selectEntryByIndex(mainFrame
								.getItemList().getLastSelectedIndex(), true);
					}

					mainFrame.setPopupWasShown(false);
				}
			}
		}
	}
}