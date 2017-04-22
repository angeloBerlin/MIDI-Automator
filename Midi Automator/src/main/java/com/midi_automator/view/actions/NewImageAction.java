package com.midi_automator.view.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.PresenterService;
import com.midi_automator.view.automationconfiguration.AutomationIndexDoesNotExistException;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;

/**
 * Sets a new screenshot image
 * 
 * @author aguelle
 * 
 */
@org.springframework.stereotype.Component
public class NewImageAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(Presenter.class.getName());

	private final JFileChooser fileChooser = new JFileChooser();
	private String screenShotFileChooserDir;

	@Autowired
	private PresenterService presenterService;

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

		screenShotFileChooserDir = presenterService
				.getLastScreenshotChooserDirectory();

		if (screenShotFileChooserDir != null) {
			fileChooser.setCurrentDirectory(new File(screenShotFileChooserDir));
		}

		// get automation table
		if (component.getName().equals(GUIAutomationConfigurationTable.NAME)) {

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