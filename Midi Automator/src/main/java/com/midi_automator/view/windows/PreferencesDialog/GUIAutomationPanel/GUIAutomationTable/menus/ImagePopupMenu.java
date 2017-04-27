package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.menus;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.view.windows.PreferencesDialog.actions.NewImageAction;
import com.midi_automator.view.windows.PreferencesDialog.actions.RemoveImageAction;

@Controller
public class ImagePopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	public static final String MENU_ITEM_NEW_IMAGE = "Screenshot...";
	public static final String MENU_ITEM_REMOVE_IMAGE = "Remove screenshot";

	public static final String NAME_MENU_ITEM_NEW_IMAGE = "new image";
	public static final String NAME_MENU_ITEM_REMOVE_IMAGE = "remove image";

	protected JMenuItem newImageMenuItem;
	protected JMenuItem removeImageMenuItem;

	@Autowired
	private NewImageAction newImageAction;
	@Autowired
	private RemoveImageAction removeImageAction;

	/**
	 * Initialize the popup menu
	 */
	public void init() {

		removeAll();
		newImageMenuItem = new JMenuItem(MENU_ITEM_NEW_IMAGE);
		newImageMenuItem.setName(NAME_MENU_ITEM_NEW_IMAGE);
		newImageMenuItem.addActionListener(newImageAction);

		removeImageMenuItem = new JMenuItem(MENU_ITEM_REMOVE_IMAGE);
		removeImageMenuItem.setName(NAME_MENU_ITEM_REMOVE_IMAGE);
		removeImageMenuItem.setEnabled(false);
		removeImageMenuItem.addActionListener(removeImageAction);

		add(newImageMenuItem);
		add(removeImageMenuItem);

	}

	public JMenuItem getNewImageMenuItem() {
		return newImageMenuItem;
	}

	public JMenuItem getRemoveImageMenuItem() {
		return removeImageMenuItem;
	}
}
