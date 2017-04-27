package com.midi_automator.view.windows.MainFrame.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.presenter.services.MidiItemChangeNotificationService;
import com.midi_automator.view.windows.MainFrame.ItemList;
import com.midi_automator.view.windows.MainFrame.MainFrame;
import com.midi_automator.view.windows.MainFrame.menus.MainFramePopupMenu;

/**
 * Global key listener for "any time" key strokes.
 * 
 * @author aguelle
 *
 */
@Component
public class GlobalKeyListener implements KeyListener {

	@Autowired
	private ItemList itemList;
	@Autowired
	private MainFrame mainFrame;

	@Autowired
	private ItemListService itemListService;
	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;

	@Autowired
	private MainFramePopupListener mainFramePopupListener;
	@Autowired
	private MainFramePopupMenu mainFramePopupMenu;

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();
		int keyMod = e.getModifiers();

		int index = itemList.getSelectedIndex();

		MouseEvent rightClick = new MouseEvent(itemList,
				MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0,
				itemList.getSelectedLocation().x + itemList.getWidth() / 2 + 1,
				itemList.getSelectedLocation().y
						+ itemList.getSelectionHeight() / 2, 1, true);

		switch (keyCode) {

		case KeyEvent.VK_SPACE:
			itemListService.openNextFile();
			break;
		case KeyEvent.VK_RIGHT:
			itemListService.openNextFile();
			break;
		case KeyEvent.VK_ENTER:
			itemList.setSelectedIndex(index);
			itemListService.openEntryByIndex(index, true);
			break;
		case KeyEvent.VK_LEFT:
			itemListService.openPreviousFile();
			break;
		case KeyEvent.VK_BACK_SPACE:
			itemListService.openPreviousFile();
			break;
		case KeyEvent.VK_CONTEXT_MENU:
			mainFramePopupListener.mouseReleased(rightClick);
			break;
		case KeyEvent.VK_META:
			mainFramePopupListener.mouseReleased(rightClick);
			break;
		case KeyEvent.VK_DELETE:
			itemListService.deleteItem(index);
			break;
		}

		// ALT + A
		if (keyCode == mainFramePopupMenu.getAddMenuItem().getAccelerator()
				.getKeyCode()
				&& keyMod == 8) {
			mainFrame.openAddDialog();
		}

		// ALT + E
		if (keyCode == mainFramePopupMenu.getEditMenuItem().getAccelerator()
				.getKeyCode()
				&& keyMod == 8) {
			mainFrame.openEditDialog();
		}

		// ALT + M
		if (keyCode == mainFramePopupMenu.getSendMidiMenuItem()
				.getAccelerator().getKeyCode()
				&& keyMod == 8) {
			midiNotificationService.sendItemSignature(index);
		}

		// arrow up
		if ((keyCode == mainFramePopupMenu.getMoveUpMenuItem().getAccelerator()
				.getKeyCode())) {
			if (keyMod == 0) {
				itemList.setSelectedIndex(index - 1);
			}
			// ALT
			if (keyMod == 8) {
				itemListService.moveUpItem(index);
			}
		}

		// arrow down
		if (keyCode == mainFramePopupMenu.getMoveDownMenuItem()
				.getAccelerator().getKeyCode()) {
			if (keyMod == 0) {
				if (itemList.isSelectionEmpty()) {
					itemList.setSelectedIndex(0);
				} else {
					itemList.setSelectedIndex(index + 1);
				}
			}
			// ALT
			if (keyMod == 8) {
				itemListService.moveDownItem(index);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
