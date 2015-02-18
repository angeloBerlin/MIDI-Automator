package de.tieffrequent.midi.automator.view;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.tieffrequent.midi.automator.IApplication;
import de.tieffrequent.midi.automator.Messages;
import de.tieffrequent.midi.automator.view.frames.AddFrame;
import de.tieffrequent.midi.automator.view.frames.EditFrame;
import de.tieffrequent.midi.automator.view.frames.MainFrame;

/**
 * The PopupMenu of the file list in the main window.
 * 
 * @author aguelle
 * 
 */
public class MainFramePopupMenu extends MidiLearnPopupMenu {

	private static final long serialVersionUID = 1L;

	private final String MENU_ITEM_MIDI_UNLEARN = "Midi unlearn";
	private final String MENU_ITEM_MOVE_UP = "Move up";
	private final String MENU_ITEM_MOVE_DOWN = "Move down";
	private final String MENU_ITEM_DELETE = "Delete";
	private final String MENU_ITEM_ADD = "Add";
	private final String MENU_ITEM_EDIT = "Edit";

	private final String NAME_MENU_ITEM_MOVE_UP = "move up";
	private final String NAME_MENU_ITEM_MOVE_DOWN = "move down";
	private final String NAME_MENU_ITEM_DELETE = "delete";
	private final String NAME_MENU_ITEM_ADD = "add";
	private final String NAME_MENU_ITEM_EDIT = "edit";

	private final String NAME_MENU_ITEM_MIDI_UNLEARN = "midi unlearn";

	private JMenuItem midiUnlearnMenuItem;
	private JMenuItem moveUpMenuItem;
	private JMenuItem moveDownMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem addMenuItem;
	private JMenuItem editMenuItem;

	private MainFrame mainFrame;

	/**
	 * Constructor
	 * 
	 * @param parentFrame
	 *            The frame the menu was called from
	 * @param application
	 *            The application
	 */
	public MainFramePopupMenu(JFrame parentFrame, IApplication application) {
		super(application);

		if (parentFrame instanceof MainFrame) {
			mainFrame = (MainFrame) parentFrame;
		}

		moveUpMenuItem = new JMenuItem(MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setName(NAME_MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setEnabled(true);
		moveUpMenuItem.addActionListener(new MoveUpAction());

		moveDownMenuItem = new JMenuItem(MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setName(NAME_MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setEnabled(true);
		moveDownMenuItem.addActionListener(new MoveDownAction());

		deleteMenuItem = new JMenuItem(MENU_ITEM_DELETE);
		deleteMenuItem.setName(NAME_MENU_ITEM_DELETE);
		deleteMenuItem.setEnabled(true);
		deleteMenuItem.addActionListener(new DeleteAction());

		addMenuItem = new JMenuItem(MENU_ITEM_ADD);
		addMenuItem.setName(NAME_MENU_ITEM_ADD);
		addMenuItem.setEnabled(true);
		addMenuItem.addActionListener(new AddAction());

		editMenuItem = new JMenuItem(MENU_ITEM_EDIT);
		editMenuItem.setName(NAME_MENU_ITEM_EDIT);
		editMenuItem.setEnabled(true);
		editMenuItem.addActionListener(new EditAction());

		midiUnlearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setName(NAME_MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setEnabled(false);
		midiUnlearnMenuItem.addActionListener(new MidiUnlearnAction());

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
	class AddAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			new AddFrame(application, mainFrame);
		}
	}

	/**
	 * Deletes the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class DeleteAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			application.deleteItem(mainFrame.getFileList().getSelectedIndex());
		}
	}

	/**
	 * Deletes the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class EditAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			new EditFrame(application, mainFrame, mainFrame.getFileList()
					.getSelectedIndex());
		}
	}

	/**
	 * Deletes the learned midi signature from the component
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiUnlearnAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JMenuItem menuItem = (JMenuItem) arg0.getSource();

			JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
			Component component = popupMenu.getInvoker();
			application.setMidiSignature(null, component);
			mainFrame.getFileList().setLastSelectedIndex();

			mainFrame.setInfoText(String.format(Messages.MSG_MIDI_UNLEARNED,
					mainFrame.getMidiComponentName(component)));
		}
	}

	/**
	 * Moves the selected item one step up in the list
	 * 
	 * @author aguelle
	 * 
	 */
	class MoveUpAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			application.moveUpItem(mainFrame.getFileList().getSelectedIndex());
		}
	}

	/**
	 * Moves the selected item one step down in the list
	 * 
	 * @author aguelle
	 * 
	 */
	class MoveDownAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			application
					.moveDownItem(mainFrame.getFileList().getSelectedIndex());
		}
	}

	/**
	 * Gets the menu item for move up action
	 * 
	 * @return The move up menu item
	 */
	public JMenuItem getMoveUpMenuItem() {
		return moveUpMenuItem;
	}

	/**
	 * Gets the menu item for move down action
	 * 
	 * @return The move down menu item
	 */
	public JMenuItem getMoveDownMenuItem() {
		return moveDownMenuItem;
	}

	/**
	 * Gets the menu item for midi unlearn action
	 * 
	 * @return The midi unlearn menu item
	 */
	public JMenuItem getMidiUnlearnMenuItem() {
		return midiUnlearnMenuItem;
	}
}
