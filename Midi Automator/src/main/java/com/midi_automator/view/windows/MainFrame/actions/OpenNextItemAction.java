package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ItemListService;

/**
 * Opens the next item from the item list
 * 
 * @author aguelle
 * 
 */
@Component
public class OpenNextItemAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ItemListService itemListService;

	@Override
	public void actionPerformed(ActionEvent ae) {
		setEnabled(false);
		itemListService.openNextFile();
		setEnabled(true);
	}
}