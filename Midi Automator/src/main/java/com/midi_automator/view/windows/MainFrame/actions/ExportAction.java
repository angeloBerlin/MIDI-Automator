package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ImportExportService;
import com.midi_automator.view.windows.ExportFileChooser.ExportFileChooser;
import com.midi_automator.view.windows.MainFrame.MainFrame;

/**
 * Exports the set list and the preferences to a zipped file.
 * 
 * @author aguelle
 * 
 */
@Component
public class ExportAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MainFrame mainFrame;
	@Autowired
	private ExportFileChooser fileChooser;

	@Autowired
	private ImportExportService importExportService;

	@Override
	public void actionPerformed(ActionEvent e) {

		fileChooser.init();
		int returnVal = fileChooser.showSaveDialog(mainFrame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			String fileExtension = "."
					+ ImportExportService.MIDI_AUTOMATOR_FILE_EXTENSIONS[0];

			if (!filePath.endsWith(fileExtension)) {
				filePath += fileExtension;
			}

			importExportService.exportMidautoFile(filePath);
		}
	}
}
