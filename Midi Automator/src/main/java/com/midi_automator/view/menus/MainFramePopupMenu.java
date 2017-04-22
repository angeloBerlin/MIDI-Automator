package com.midi_automator.view.menus;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.view.actions.AddItemAction;
import com.midi_automator.view.actions.DeleteItemAction;
import com.midi_automator.view.actions.EditItemAction;
import com.midi_automator.view.actions.MoveDownItemAction;
import com.midi_automator.view.actions.MoveUpItemAction;
import com.midi_automator.view.actions.SendItemMidiSignatureAction;

/**
 * The PopupMenu of the file list in the main window.
 * 
 * @author aguelle
 * 
 */
@Controller
public class MainFramePopupMenu extends MidiLearnPopupMenu {

	private static final long serialVersionUID = 1L;

	private final String MENU_ITEM_MOVE_UP = "Move up";
	private final String MENU_ITEM_MOVE_DOWN = "Move down";
	private final String MENU_ITEM_DELETE = "Delete";
	private final String MENU_ITEM_ADD = "Add";
	private final String MENU_ITEM_EDIT = "Edit";
	private final String MENU_ITEM_SEND_MIDI = "Send midi";

	public static final String NAME = "main frame popup menu";
	public static final String NAME_MENU_ITEM_MOVE_UP = "move up";
	public static final String NAME_MENU_ITEM_MOVE_DOWN = "move down";
	public static final String NAME_MENU_ITEM_DELETE = "delete";
	public static final String NAME_MENU_ITEM_ADD = "add";
	public static final String NAME_MENU_ITEM_EDIT = "edit";
	public static final String NAME_MENU_ITEM_SEND_MIDI = "send midi";

	private JMenuItem moveUpMenuItem;
	private JMenuItem moveDownMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem addMenuItem;
	private JMenuItem editMenuItem;
	private JMenuItem sendMidiMenuItem;

	@Autowired
	private MoveUpItemAction moveUpAction;
	@Autowired
	private MoveDownItemAction moveDownAction;
	@Autowired
	private DeleteItemAction deleteAction;
	@Autowired
	private AddItemAction addAction;
	@Autowired
	private EditItemAction editAction;
	@Autowired
	private SendItemMidiSignatureAction sendMidiAction;

	/**
	 * Initializes the popup menu
	 */
	public void init() {
		super.init();

		setName(NAME);

		moveUpMenuItem = new JMenuItem(MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setName(NAME_MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setEnabled(true);
		moveUpMenuItem.addActionListener(moveUpAction);
		moveUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
				ActionEvent.ALT_MASK));

		moveDownMenuItem = new JMenuItem(MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setName(NAME_MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setEnabled(true);
		moveDownMenuItem.addActionListener(moveDownAction);
		moveDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DOWN, ActionEvent.ALT_MASK));

		deleteMenuItem = new JMenuItem(MENU_ITEM_DELETE);
		deleteMenuItem.setName(NAME_MENU_ITEM_DELETE);
		deleteMenuItem.setEnabled(true);
		deleteMenuItem.addActionListener(deleteAction);
		deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0));

		addMenuItem = new JMenuItem(MENU_ITEM_ADD);
		addMenuItem.setName(NAME_MENU_ITEM_ADD);
		addMenuItem.setEnabled(true);
		addMenuItem.addActionListener(addAction);
		addMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));

		editMenuItem = new JMenuItem(MENU_ITEM_EDIT);
		editMenuItem.setName(NAME_MENU_ITEM_EDIT);
		editMenuItem.setEnabled(true);
		editMenuItem.addActionListener(editAction);
		editMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.ALT_MASK));

		sendMidiMenuItem = new JMenuItem(MENU_ITEM_SEND_MIDI);
		sendMidiMenuItem.setName(NAME_MENU_ITEM_SEND_MIDI);
		sendMidiMenuItem.setEnabled(false);
		sendMidiMenuItem.addActionListener(sendMidiAction);
		sendMidiMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.ALT_MASK));
	}

	/**
	 * Configures the popup menu of the file list.
	 */
	public void configureFileListPopupMenu() {

		removeAll();
		add(addMenuItem);
		add(editMenuItem);
		add(deleteMenuItem);
		add(moveUpMenuItem);
		add(moveDownMenuItem);
		addSeparator();
		add(midiLearnMenuItem);
		add(midiUnlearnMenuItem);
		addSeparator();
		add(sendMidiMenuItem);
	}

	/**
	 * Creates the popup menu of the switch buttons
	 */
	public void configureSwitchButtonPopupMenu() {

		removeAll();
		add(midiLearnMenuItem);
		add(midiUnlearnMenuItem);
	}

	public JMenuItem getAddMenuItem() {
		return addMenuItem;
	}

	public JMenuItem getMoveUpMenuItem() {
		return moveUpMenuItem;
	}

	public JMenuItem getMoveDownMenuItem() {
		return moveDownMenuItem;
	}

	public JMenuItem getDeleteMenuItem() {
		return deleteMenuItem;
	}

	public JMenuItem getEditMenuItem() {
		return editMenuItem;
	}

	public JMenuItem getSendMidiMenuItem() {
		return sendMidiMenuItem;
	}
}
