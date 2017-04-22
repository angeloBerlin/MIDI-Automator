package com.midi_automator.view.menus;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.view.actions.KeysLearnAction;
import com.midi_automator.view.actions.KeysLearnCancelAction;
import com.midi_automator.view.actions.KeysLearnSubmitAction;
import com.midi_automator.view.actions.KeysUnlearnAction;

@Controller
public class KeyLearnPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	public static final String MENU_ITEM_KEYS_UNLEARN = "Keys unlearn";
	public static final String MENU_ITEM_KEYS_LEARN = "Keys learn";
	public static final String MENU_ITEM_KEYS_LEARN_CANCEL = "Cancel keys learn";
	public static final String MENU_ITEM_KEYS_LEARN_SUBMIT = "Submit learned keys";

	public static final String NAME_MENU_ITEM_KEYS_LEARN = "keys learn";
	public static final String NAME_MENU_ITEM_KEYS_SUBMIT = "keys learn";
	public static final String NAME_MENU_ITEM_KEYS_UNLEARN = "keys unlearn";
	public static final String NAME_MENU_ITEM_KEYS_CANCEL = "keys unlearn";

	protected JMenuItem keysLearnMenuItem;
	protected JMenuItem keysUnlearnMenuItem;

	@Autowired
	private KeysLearnAction keysLearnAction;
	@Autowired
	private KeysLearnCancelAction keysCancelAction;
	@Autowired
	private KeysLearnSubmitAction keysSubmitAction;
	@Autowired
	private KeysUnlearnAction keysUnlearnAction;

	/**
	 * Initialize the popup menu
	 */
	public void init() {

		removeAll();
		keysLearnMenuItem = new JMenuItem(MENU_ITEM_KEYS_LEARN);
		keysLearnMenuItem.setName(NAME_MENU_ITEM_KEYS_LEARN);
		keysLearnMenuItem.setEnabled(false);
		keysLearnMenuItem.addActionListener(keysLearnAction);

		keysUnlearnMenuItem = new JMenuItem(MENU_ITEM_KEYS_UNLEARN);
		keysUnlearnMenuItem.setName(NAME_MENU_ITEM_KEYS_UNLEARN);
		keysUnlearnMenuItem.setEnabled(false);
		keysUnlearnMenuItem.addActionListener(keysUnlearnAction);

		add(keysLearnMenuItem);
		add(keysUnlearnMenuItem);
	}

	/**
	 * Puts the frame to key learn mode
	 */
	public void keyLearnOn() {

		keysLearnMenuItem.setText(MENU_ITEM_KEYS_LEARN_SUBMIT);
		keysLearnMenuItem.removeActionListener(keysLearnAction);
		keysLearnMenuItem.addActionListener(keysSubmitAction);

		keysUnlearnMenuItem.setText(MENU_ITEM_KEYS_LEARN_CANCEL);
		keysUnlearnMenuItem.removeActionListener(keysUnlearnAction);
		keysUnlearnMenuItem.addActionListener(keysCancelAction);
	}

	/**
	 * Puts the frame to normal mode.
	 */
	public void keyLearnOff() {

		keysLearnMenuItem.setText(MENU_ITEM_KEYS_LEARN);
		keysLearnMenuItem.removeActionListener(keysSubmitAction);
		keysLearnMenuItem.addActionListener(keysLearnAction);

		keysUnlearnMenuItem.setText(MENU_ITEM_KEYS_UNLEARN);
		keysUnlearnMenuItem.removeActionListener(keysCancelAction);
		keysUnlearnMenuItem.addActionListener(keysUnlearnAction);
	}

	public JMenuItem getKeyLearnMenuItem() {
		return keysLearnMenuItem;
	}

	public JMenuItem getKeyUnlearnMenuItem() {
		return keysUnlearnMenuItem;
	}
}
