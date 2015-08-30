package com.midi_automator.view;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.view.frames.AddFrame;
import com.midi_automator.view.frames.EditFrame;
import com.midi_automator.view.frames.MainFrame;

/**
 * The PopupMenu of the file list in the main window.
 * 
 * @author aguelle
 * 
 */
public class MainFramePopupMenu extends MidiLearnPopupMenu {

	private static final long serialVersionUID = 1L;

	private final String MENU_ITEM_MOVE_UP = "Move up";
	private final String MENU_ITEM_MOVE_DOWN = "Move down";
	private final String MENU_ITEM_DELETE = "Delete";
	private final String MENU_ITEM_ADD = "Add";
	private final String MENU_ITEM_EDIT = "Edit";
	private final String MENU_ITEM_SEND_MIDI = "Send midi";

	private final String NAME_MENU_ITEM_MOVE_UP = "move up";
	private final String NAME_MENU_ITEM_MOVE_DOWN = "move down";
	private final String NAME_MENU_ITEM_DELETE = "delete";
	private final String NAME_MENU_ITEM_ADD = "add";
	private final String NAME_MENU_ITEM_EDIT = "edit";
	private final String NAME_MENU_ITEM_SEND_MIDI = "send midi";

	private JMenuItem moveUpMenuItem;
	private JMenuItem moveDownMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem addMenuItem;
	private JMenuItem editMenuItem;
	private JMenuItem sendMidiMenuItem;

	/**
	 * Constructor
	 * 
	 * @param program_frame
	 *            The frame the menu was called from
	 * @param application
	 *            The application
	 */
	public MainFramePopupMenu(MainFrame program_frame, MidiAutomator application) {
		super(program_frame, application);

		moveUpMenuItem = new JMenuItem(MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setName(NAME_MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setEnabled(true);
		moveUpMenuItem.addActionListener(new MoveUpAction(program_frame));

		moveDownMenuItem = new JMenuItem(MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setName(NAME_MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setEnabled(true);
		moveDownMenuItem.addActionListener(new MoveDownAction(program_frame));

		deleteMenuItem = new JMenuItem(MENU_ITEM_DELETE);
		deleteMenuItem.setName(NAME_MENU_ITEM_DELETE);
		deleteMenuItem.setEnabled(true);
		deleteMenuItem.addActionListener(new DeleteAction(program_frame));

		addMenuItem = new JMenuItem(MENU_ITEM_ADD);
		addMenuItem.setName(NAME_MENU_ITEM_ADD);
		addMenuItem.setEnabled(true);
		addMenuItem.addActionListener(new AddAction(program_frame));

		editMenuItem = new JMenuItem(MENU_ITEM_EDIT);
		editMenuItem.setName(NAME_MENU_ITEM_EDIT);
		editMenuItem.setEnabled(true);
		editMenuItem.addActionListener(new EditAction(program_frame));

		sendMidiMenuItem = new JMenuItem(MENU_ITEM_SEND_MIDI);
		sendMidiMenuItem.setName(NAME_MENU_ITEM_SEND_MIDI);
		sendMidiMenuItem.setEnabled(false);
		sendMidiMenuItem.addActionListener(new SendMidiAction(program_frame));

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

	/**
	 * Adds the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class AddAction extends PopUpMenuAction {

		public AddAction(MainFrame PROGRAM_FRAME) {
			super(PROGRAM_FRAME);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			new AddFrame(APPLICATION, PROGRAM_FRAME);
		}
	}

	/**
	 * Deletes the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class DeleteAction extends PopUpMenuAction {

		public DeleteAction(MainFrame PROGRAM_FRAME) {
			super(PROGRAM_FRAME);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			APPLICATION.deleteItem(PROGRAM_FRAME.getFileList()
					.getSelectedIndex());
		}
	}

	/**
	 * Deletes the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class EditAction extends PopUpMenuAction {

		public EditAction(MainFrame PROGRAM_FRAME) {
			super(PROGRAM_FRAME);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			new EditFrame(APPLICATION, PROGRAM_FRAME, PROGRAM_FRAME
					.getFileList().getSelectedIndex());
		}
	}

	/**
	 * Moves the selected item one step up in the list
	 * 
	 * @author aguelle
	 * 
	 */
	class MoveUpAction extends PopUpMenuAction {

		public MoveUpAction(MainFrame PROGRAM_FRAME) {
			super(PROGRAM_FRAME);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			APPLICATION.moveUpItem(PROGRAM_FRAME.getFileList()
					.getSelectedIndex());
		}
	}

	/**
	 * Moves the selected item one step down in the list
	 * 
	 * @author aguelle
	 * 
	 */
	class MoveDownAction extends PopUpMenuAction {

		public MoveDownAction(MainFrame PROGRAM_FRAME) {
			super(PROGRAM_FRAME);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			APPLICATION.moveDownItem(PROGRAM_FRAME.getFileList()
					.getSelectedIndex());
		}
	}

	/**
	 * Sends the midi signature of the current selected item.
	 * 
	 * @author aguelle
	 * 
	 */
	class SendMidiAction extends PopUpMenuAction {

		public SendMidiAction(MainFrame parentFrame) {
			super(parentFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			APPLICATION.sendItemSignature(PROGRAM_FRAME.getFileList()
					.getSelectedIndex());
		}
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
