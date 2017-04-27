package com.midi_automator.view.windows.ExportFileChooser;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ImportExportService;

/**
 * The file chooser for choosing the export file.
 * 
 * @author aguelle
 *
 */
@Component
public class ExportFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "export file chooser";

	public void init() {

		FileFilter filter = new FileNameExtensionFilter(
				ImportExportService.MIDI_AUTOMATOR_FILE_TYPE,
				ImportExportService.MIDI_AUTOMATOR_FILE_EXTENSIONS);
		setAcceptAllFileFilterUsed(false);
		setFileFilter(filter);
		setName(NAME);
	}

}
