package com.midi_automator.view.automationconfiguration;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.PresenterService;
import com.midi_automator.view.AbstractPopUpMenuAction;
import com.midi_automator.view.ScaleableImageIcon;
import com.midi_automator.view.frames.MainFrame;

@Controller
public class ImagePopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	public static final String MENU_ITEM_NEW_IMAGE = "Screenshot...";
	public static final String MENU_ITEM_REMOVE_IMAGE = "Remove screenshot";

	public static final String NAME_MENU_ITEM_NEW_IMAGE = "new image";
	public static final String NAME_MENU_ITEM_REMOVE_IMAGE = "remove image";

	protected JMenuItem newImageMenuItem;
	protected JMenuItem removeImageMenuItem;

	private Action newImageAction;
	private Action removeImageAction;
	private String screenShotFileChooserDir;

	@Autowired
	protected Presenter presenter;
	@Autowired
	protected MainFrame mainFrame;

	@Autowired
	private PresenterService presenterService;

	/**
	 * Initialize the popup menu
	 */
	public void init() {

		removeAll();
		newImageMenuItem = new JMenuItem(MENU_ITEM_NEW_IMAGE);
		newImageMenuItem.setName(NAME_MENU_ITEM_NEW_IMAGE);
		newImageAction = new NewImageAction(mainFrame);
		newImageMenuItem.addActionListener(newImageAction);

		removeImageMenuItem = new JMenuItem(MENU_ITEM_REMOVE_IMAGE);
		removeImageMenuItem.setName(NAME_MENU_ITEM_REMOVE_IMAGE);
		removeImageMenuItem.setEnabled(false);
		removeImageAction = new RemoveImageAction(mainFrame);
		removeImageMenuItem.addActionListener(removeImageAction);

		screenShotFileChooserDir = presenterService
				.getLastScreenshotChooserDirectory();

		add(newImageMenuItem);
		add(removeImageMenuItem);

	}

	public JMenuItem getNewImageMenuItem() {
		return newImageMenuItem;
	}

	public JMenuItem getRemoveImageMenuItem() {
		return removeImageMenuItem;
	}

	/**
	 * Sets a new screenshot image
	 * 
	 * @author aguelle
	 * 
	 */
	class NewImageAction extends AbstractPopUpMenuAction {

		private static final long serialVersionUID = 1L;
		private Logger log = Logger.getLogger(Presenter.class.getName());
		private final JFileChooser fileChooser = new JFileChooser();

		public NewImageAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			JMenuItem menuItem = (JMenuItem) e.getSource();
			JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
			Component component = popupMenu.getInvoker();

			FileFilter filter = new FileNameExtensionFilter(
					GUIAutomation.SCREENSHOT_FILE_TYPE,
					GUIAutomation.SCREENSHOT_FILE_EXTENSIONS);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(filter);

			if (screenShotFileChooserDir != null) {
				fileChooser.setCurrentDirectory(new File(
						screenShotFileChooserDir));
			}

			// get automation table
			if (component.getName()
					.equals(GUIAutomationConfigurationTable.NAME)) {

				if (component instanceof GUIAutomationConfigurationTable) {
					GUIAutomationConfigurationTable guiAutomationConfigurationTable = (GUIAutomationConfigurationTable) component;

					int returnVal = fileChooser
							.showOpenDialog(guiAutomationConfigurationTable);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						screenShotFileChooserDir = file.getParent();
						presenterService
								.setLastScreenshotChooserDirectory(screenShotFileChooserDir);
						try {
							if (component instanceof JTable) {
								JTable automationsTable = (JTable) component;
								guiAutomationConfigurationTable.setClickImage(
										file.getAbsolutePath(),
										automationsTable.getSelectedRow());
							}

						} catch (AutomationIndexDoesNotExistException ex) {
							log.error(
									"The automation for the click image does not exist.",
									ex);
						}
					}
				}
			}

		}
	}

	/**
	 * Removes the screenshot image
	 * 
	 * @author aguelle
	 * 
	 */
	class RemoveImageAction extends AbstractPopUpMenuAction {

		public RemoveImageAction(MainFrame mainFrame) {
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
							GUIAutomationConfigurationTable.COLNAME_IMAGE)
							.getModelIndex();
					model.setValueAt(new ScaleableImageIcon(),
							table.getSelectedRow(), columnIndex);
				}
			}
		}
	}
}
