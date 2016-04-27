package com.midi_automator.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import com.midi_automator.presenter.services.FileListService;
import com.midi_automator.presenter.services.MidiItemChangeNotificationService;
import com.midi_automator.view.frames.MainFrame;

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
	private MainFrame mainFrame;

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private FileListService fileListService;
	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;

	/**
	 * Initializes the popup menu
	 */
	public void init() {
		super.init();

		setName(NAME);

		moveUpMenuItem = new JMenuItem(MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setName(NAME_MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setEnabled(true);
		moveUpMenuItem.addActionListener(new MoveUpAction(mainFrame));
		moveUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
				ActionEvent.ALT_MASK));

		moveDownMenuItem = new JMenuItem(MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setName(NAME_MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setEnabled(true);
		moveDownMenuItem.addActionListener(new MoveDownAction(mainFrame));
		moveDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DOWN, ActionEvent.ALT_MASK));

		deleteMenuItem = new JMenuItem(MENU_ITEM_DELETE);
		deleteMenuItem.setName(NAME_MENU_ITEM_DELETE);
		deleteMenuItem.setEnabled(true);
		deleteMenuItem.addActionListener(new DeleteAction(mainFrame));
		deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0));

		addMenuItem = new JMenuItem(MENU_ITEM_ADD);
		addMenuItem.setName(NAME_MENU_ITEM_ADD);
		addMenuItem.setEnabled(true);
		addMenuItem.addActionListener(new AddAction(mainFrame));
		addMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));

		editMenuItem = new JMenuItem(MENU_ITEM_EDIT);
		editMenuItem.setName(NAME_MENU_ITEM_EDIT);
		editMenuItem.setEnabled(true);
		editMenuItem.addActionListener(new EditAction(mainFrame));
		editMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.ALT_MASK));

		sendMidiMenuItem = new JMenuItem(MENU_ITEM_SEND_MIDI);
		sendMidiMenuItem.setName(NAME_MENU_ITEM_SEND_MIDI);
		sendMidiMenuItem.setEnabled(false);
		sendMidiMenuItem.addActionListener(new SendMidiAction(mainFrame));
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

	/**
	 * Adds the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class AddAction extends AbstractPopUpMenuAction {

		public AddAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			mainFrame.openAddFrame();
		}
	}

	/**
	 * Deletes the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class DeleteAction extends AbstractPopUpMenuAction {

		public DeleteAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			fileListService.deleteItem(mainFrame.getFileList()
					.getSelectedIndex());
		}
	}

	/**
	 * Deletes the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class EditAction extends AbstractPopUpMenuAction {

		public EditAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			mainFrame.openEditFrame();
		}
	}

	/**
	 * Moves the selected item one step up in the list
	 * 
	 * @author aguelle
	 * 
	 */
	class MoveUpAction extends AbstractPopUpMenuAction {

		public MoveUpAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			fileListService.moveUpItem(mainFrame.getFileList()
					.getSelectedIndex());
		}
	}

	/**
	 * Moves the selected item one step down in the list
	 * 
	 * @author aguelle
	 * 
	 */
	class MoveDownAction extends AbstractPopUpMenuAction {

		public MoveDownAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			fileListService.moveDownItem(mainFrame.getFileList()
					.getSelectedIndex());
		}
	}

	/**
	 * Sends the midi signature of the current selected item.
	 * 
	 * @author aguelle
	 * 
	 */
	class SendMidiAction extends AbstractPopUpMenuAction {

		public SendMidiAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			midiNotificationService.sendItemSignature(mainFrame.getFileList()
					.getSelectedIndex());
		}
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
