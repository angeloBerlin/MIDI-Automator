package com.midi_automator.view.windows.ImportFileChooser;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ImportExportService;

/**
 * The file chooser for choosing the import file
 * 
 * @author aguelle
 *
 */
@Component
public class ImportFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "import file chooser";

	public void init() {

		FileFilter filter = new FileNameExtensionFilter(
				ImportExportService.MIDI_AUTOMATOR_FILE_TYPE,
				ImportExportService.MIDI_AUTOMATOR_FILE_EXTENSIONS);
		setAcceptAllFileFilterUsed(false);
		setFileFilter(filter);
		setName(NAME);
	}

}
