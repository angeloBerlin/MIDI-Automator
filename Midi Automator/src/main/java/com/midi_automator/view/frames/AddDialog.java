package com.midi_automator.view.frames;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.FileListService;
import com.midi_automator.presenter.services.PresenterService;

@org.springframework.stereotype.Component
public class AddDialog extends AbstractBasicDialog {

	static Logger log = Logger.getLogger(AddDialog.class.getName());
	private static final long serialVersionUID = 1L;

	private final String TITLE = "Add";
	protected final int WIDTH = 530;
	protected final int HEIGHT = 180;
	protected final int META_LABEL_WIDTH = 70;
	protected final int TEXT_PANE_WIDTH = 340;
	private final String LABEL_NAME = "Name:";
	private final String LABEL_FILE = "File:";
	private final String LABEL_PROGRAM = "Program:";
	private final String LABEL_SENDING_MIDI = "Sending:";
	private final String BUTTON_FILE_SEARCH = "Search...";
	private final String BUTTON_PROGRAM_SEARCH = "Search...";

	public static String NAME = "Add Dialog";
	public static String NAME_NAME_TEXT_FIELD = "name text field";
	public static String NAME_FILE_TEXT_FIELD = "file text field";
	public static String NAME_PROGRAM_TEXT_FIELD = "program text field";
	public static String NAME_FILE_SEARCH_BUTTON = "file search button";
	public static String NAME_PROGRAM_SEARCH_BUTTON = "program search button";
	public static String NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL = "MIDI sending label";

	protected JTextField nameTextField;
	protected JTextField fileTextField;
	protected JTextField programTextField;
	protected JLabel midiSendingSignatureLabel;
	protected JLabel midiSendingSignatureValueLabel;
	protected JFileChooser fileChooser;
	protected JFileChooser programChooser;
	protected String fileChooserDir;
	protected String programChooserDir;

	@Autowired
	protected Presenter presenter;

	@Autowired
	protected FileListService fileListService;

	@Autowired
	protected PresenterService presenterService;

	/**
	 * Initializes the frame
	 */
	public void init() {

		if (!initialized) {
			super.init();

			setTitle(TITLE);
			setSize(WIDTH, HEIGHT);
			setName(NAME);

			UIManager.put("FileChooser.acceptAllFileFilterText", "All Files");
			fileChooser = new JFileChooser();
			programChooser = new JFileChooser();
			programChooser
					.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			createEntryName();
			createEntryFile();
			createProgramPath();
			createMidiSendingSignature();

			fileChooserDir = presenterService.getLastFileChooserDirectory();
			programChooserDir = presenterService
					.getLastProgramChooserDirectory();

			footerPanel.add(buttonSave);
			footerPanel.add(buttonCancel);

			initialized = true;
		}

		initMidiSendingSignatureValueLabelText();
	}

	/**
	 * Creates the text pane for the name of the list entry with a label
	 */
	private void createEntryName() {
		nameTextField = createTextField(LABEL_NAME, NAME_NAME_TEXT_FIELD,
				META_LABEL_WIDTH, TEXT_PANE_WIDTH);
	}

	/**
	 * Creates the text pane for the file to open with a label and a search
	 * button
	 */
	private void createEntryFile() {

		fileTextField = createTextField(LABEL_FILE, NAME_FILE_TEXT_FIELD,
				META_LABEL_WIDTH, TEXT_PANE_WIDTH);

		JButton searchButton = new JButton(BUTTON_FILE_SEARCH);
		searchButton.addActionListener(new FileSearchAction());
		searchButton.setName(NAME_FILE_SEARCH_BUTTON);
		searchButton.addKeyListener(new FileSearchKeyListener());
		addComponentAtPosition(searchButton, 2, gridRows - 1);
	}

	/**
	 * Creates the text pane for the opening program to open with a label and a
	 * search button
	 */
	private void createProgramPath() {

		programTextField = createTextField(LABEL_PROGRAM,
				NAME_PROGRAM_TEXT_FIELD, META_LABEL_WIDTH, TEXT_PANE_WIDTH);

		JButton searchButton = new JButton(BUTTON_PROGRAM_SEARCH);
		searchButton.addActionListener(new ProgramSearchAction());
		searchButton.setName(NAME_PROGRAM_SEARCH_BUTTON);
		searchButton.addKeyListener(new ProgramSearchKeyListener());
		addComponentAtPosition(searchButton, 2, gridRows - 1);

	}

	/**
	 * Creates the label for the midi sending signature
	 */
	private void createMidiSendingSignature() {

		midiSendingSignatureValueLabel = createMetaLabel(LABEL_SENDING_MIDI,
				NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL, META_LABEL_WIDTH,
				TEXT_PANE_WIDTH);
	}

	/**
	 * Initializes the text of the midi sending signature label
	 */
	protected void initMidiSendingSignatureValueLabelText() {
		midiSendingSignatureValueLabel.setText(fileListService
				.getUniqueSendingMidiSignature());
	}

	/**
	 * Opens a file chooser and puts the return value to the file text pane.
	 */
	protected void openFileChooser() {

		if (fileChooserDir != null) {
			fileChooser.setCurrentDirectory(new File(fileChooserDir));
		}

		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			fileChooserDir = file.getParent();
			presenterService.setLastFileChooserDirectory(fileChooserDir);
			fileTextField.setText(file.getAbsolutePath());
		}
	}

	/**
	 * Opens a file chooser and puts the return value to the program text pane.
	 */
	protected void openProgramChooser() {

		if (programChooserDir != null) {
			programChooser.setCurrentDirectory(new File(programChooserDir));
		}

		int returnVal = programChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = programChooser.getSelectedFile();
			programChooserDir = file.getParent();
			presenterService.setLastProgramChooserDirectory(programChooserDir);
			programTextField.setText(file.getAbsolutePath());
		}
	}

	/**
	 * Saves the dialog and closes it.
	 */
	@Override
	protected void save() {
		fileListService.addItem(nameTextField.getText(),
				fileTextField.getText(), programTextField.getText(),
				midiSendingSignatureValueLabel.getText());
		close();
	}

	/**
	 * Opens the file chooser
	 * 
	 * @author aguelle
	 * 
	 */
	class FileSearchAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			openFileChooser();
		}
	}

	/**
	 * Opens the file chooser
	 * 
	 * @author aguelle
	 *
	 */
	class FileSearchKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				openFileChooser();
			}
		}
	}

	/**
	 * Opens the program chooser
	 * 
	 * @author aguelle
	 * 
	 */
	class ProgramSearchAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			openProgramChooser();
		}
	}

	/**
	 * Opens the program chooser
	 * 
	 * @author aguelle
	 *
	 */
	class ProgramSearchKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				openProgramChooser();
			}
		}
	}
}
