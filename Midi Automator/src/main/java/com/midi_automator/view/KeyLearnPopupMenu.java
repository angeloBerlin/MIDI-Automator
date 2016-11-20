package com.midi_automator.view;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.KeyLearnService;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;
import com.midi_automator.view.frames.MainFrame;

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

	private Action keysLearnAction;
	private Action keysCancelAction;
	private Action keysSubmitAction;
	private Action keysUnlearnAction;

	@Autowired
	protected Presenter presenter;
	@Autowired
	protected MainFrame mainFrame;

	@Autowired
	private KeyLearnService keyLearnService;

	/**
	 * Initialize the popup menu
	 */
	public void init() {

		removeAll();
		keysLearnMenuItem = new JMenuItem(MENU_ITEM_KEYS_LEARN);
		keysLearnMenuItem.setName(NAME_MENU_ITEM_KEYS_LEARN);
		keysLearnMenuItem.setEnabled(false);
		keysLearnAction = new KeysLearnAction(mainFrame);
		keysLearnMenuItem.addActionListener(keysLearnAction);

		keysUnlearnMenuItem = new JMenuItem(MENU_ITEM_KEYS_UNLEARN);
		keysUnlearnMenuItem.setName(NAME_MENU_ITEM_KEYS_UNLEARN);
		keysUnlearnMenuItem.setEnabled(false);
		keysUnlearnAction = new KeysUnlearnAction(mainFrame);
		keysUnlearnMenuItem.addActionListener(keysUnlearnAction);

		add(keysLearnMenuItem);
		add(keysUnlearnMenuItem);

		keysCancelAction = new KeysLearnCancelAction(mainFrame);
		keysSubmitAction = new KeysLearnSubmitAction(mainFrame);
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

	/**
	 * Puts the application to the key learn mode for the selected component.
	 * 
	 * @author aguelle
	 * 
	 */
	class KeysLearnAction extends AbstractPopUpMenuAction {

		public KeysLearnAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			JMenuItem menuItem = (JMenuItem) e.getSource();
			JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
			JComponent component = (JComponent) popupMenu.getInvoker();

			mainFrame.keyLearnOn(component);

			if (component.getName()
					.equals(GUIAutomationConfigurationTable.NAME)) {

				if (component instanceof JTable) {
					JTable automationsTable = (JTable) component;
					keyLearnService.activateKeyLearn(
							KeyLearnService.KEY_KEY_LEARN_AUTOMATION,
							automationsTable.getSelectedRow());

				}
				return;
			}
		}
	}

	/**
	 * Deletes the learned keys from the component
	 * 
	 * @author aguelle
	 * 
	 */
	class KeysUnlearnAction extends AbstractPopUpMenuAction {

		public KeysUnlearnAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			JMenuItem menuItem = (JMenuItem) e.getSource();

			JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
			Component component = popupMenu.getInvoker();

			// Automation Table
			if (component.getName()
					.equals(GUIAutomationConfigurationTable.NAME)) {

				if (component instanceof JTable) {
					JTable table = (JTable) component;
					TableModel model = table.getModel();

					int columnIndex = table.getColumn(
							GUIAutomationConfigurationTable.COLNAME_KEYS)
							.getModelIndex();
					model.setValueAt(null, table.getSelectedRow(), columnIndex);
				}
			}
		}
	}

	/**
	 * Cancels the key learn mode and put the application to the normal state.
	 * 
	 * @author aguelle
	 * 
	 */
	class KeysLearnCancelAction extends AbstractPopUpMenuAction {

		public KeysLearnCancelAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			keyLearnService.cancelKeyLearn();
		}
	}

	/**
	 * Submits the learned keys and put the application to the normal state.
	 * 
	 * @author aguelle
	 * 
	 */
	class KeysLearnSubmitAction extends AbstractPopUpMenuAction {

		public KeysLearnSubmitAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			keyLearnService.deActivateKeyLearn();
		}
	}
}
