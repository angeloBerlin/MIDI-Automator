package com.midi_automator.view.frames;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.FileListService;

@org.springframework.stereotype.Component
@Scope("prototype")
public class AddFrame extends AbstractBasicDialog {

	static Logger log = Logger.getLogger(AddFrame.class.getName());
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

	public static String NAME = "Add Frame";
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

	@Autowired
	protected Presenter presenter;

	@Autowired
	protected FileListService fileListService;

	/**
	 * Initializes the frame
	 */
	public void init() {
		super.init();

		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setName(NAME);

		createEntryName();
		createEntryFile();
		createProgramPath();
		createMidiSendingSignature();

		// Save
		saveAction = new SaveAction();
		buttonSave.addActionListener(saveAction);
		footerPanel.add(buttonSave);

		// Cancel
		cancelAction = new CancelAction();
		buttonCancel.addActionListener(cancelAction);
		footerPanel.add(buttonCancel);

		setAlwaysOnTop(true);
		setVisible(true);
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
		searchButton.addActionListener(new FileSearchAction(this));
		searchButton.setName(NAME_FILE_SEARCH_BUTTON);
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
		searchButton.addActionListener(new ProgramSearchAction(this));
		searchButton.setName(NAME_PROGRAM_SEARCH_BUTTON);
		addComponentAtPosition(searchButton, 2, gridRows - 1);

	}

	/**
	 * Creates the label for the midi sending signature
	 */
	private void createMidiSendingSignature() {

		midiSendingSignatureValueLabel = createMetaLabel(LABEL_SENDING_MIDI,
				NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL, META_LABEL_WIDTH,
				TEXT_PANE_WIDTH);

		initMidiSendingSignatureValueLabelText();
	}

	/**
	 * Initializes the text of the midi sending signature label
	 */
	protected void initMidiSendingSignatureValueLabelText() {
		midiSendingSignatureValueLabel.setText(fileListService
				.getUniqueSendingMidiSignature());
	}

	/**
	 * Opens a file chooser and puts the return value to the file text pane
	 * 
	 * @author aguelle
	 * 
	 */
	class FileSearchAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final JFileChooser fileChooser;
		private AddFrame parent;

		/**
		 * Constructor
		 * 
		 * @param parent
		 *            the parent component
		 */
		public FileSearchAction(AddFrame parent) {
			this.parent = parent;
			UIManager.put("FileChooser.acceptAllFileFilterText", "All Files");
			fileChooser = new JFileChooser();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = fileChooser.showOpenDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				fileTextField.setText(file.getAbsolutePath());
			}
		}
	}

	/**
	 * Opens a file chooser and puts the return value to the program text pane
	 * 
	 * @author aguelle
	 * 
	 */
	class ProgramSearchAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final JFileChooser fileChooser;
		private AddFrame parent;

		/**
		 * Constructor
		 * 
		 * @param parent
		 *            the parent component
		 */
		public ProgramSearchAction(AddFrame parent) {
			this.parent = parent;
			UIManager.put("FileChooser.acceptAllFileFilterText", "All Files");
			fileChooser = new JFileChooser();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = fileChooser.showOpenDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				programTextField.setText(file.getAbsolutePath());
			}
		}
	}

	/**
	 * Closes the frame, saves the entry and reloads the entry list
	 * 
	 * @author aguelle
	 * 
	 */
	class SaveAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			fileListService.addItem(nameTextField.getText(),
					fileTextField.getText(), programTextField.getText(),
					midiSendingSignatureValueLabel.getText());
			new CancelAction().actionPerformed(e);
		}
	}

	/**
	 * Closes the frame
	 * 
	 * @author aguelle
	 * 
	 */
	class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Component component = (Component) e.getSource();
			JFrame frame = (JFrame) SwingUtilities.getRoot(component);
			WindowEvent windowClosing = new WindowEvent(frame,
					WindowEvent.WINDOW_CLOSING);
			frame.dispatchEvent(windowClosing);
		}
	}
}
