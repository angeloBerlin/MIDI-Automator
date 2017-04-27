package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ImportExportService;
import com.midi_automator.view.windows.ImportFileChooser.ImportFileChooser;
import com.midi_automator.view.windows.MainFrame.MainFrame;

/**
 * Opens the dialog for importing the zip file.
 * 
 * @author aguelle
 * 
 */
@Component
public class ImportAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ImportFileChooser fileChooser;

	@Autowired
	private MainFrame mainFrame;
	@Autowired
	private ImportExportService importExportService;

	@Override
	public void actionPerformed(ActionEvent e) {

		fileChooser.init();
		int returnVal = fileChooser.showOpenDialog(mainFrame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			importExportService.importMidautoFile(file);
		}
	}
}
