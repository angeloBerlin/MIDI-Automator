package com.midi_automator.view.frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	protected final int HEIGHT = 140;
	protected final int LABEL_WIDTH = 70;
	protected final int TEXT_PANE_WIDTH = 340;
	private final String LABEL_NAME = "Name:";
	private final String LABEL_FILE = "File:";
	private final String LABEL_SENDING_MIDI = "Sending:";
	private final String BUTTON_SEARCH = "Search...";

	public static String NAME = "Add Frame";
	public static String NAME_NAME_TEXT_FIELD = "name text field";
	public static String NAME_FILE_TEXT_FIELD = "file text field";
	public static String NAME_SEARCH_BUTTON = "search button";
	public static String NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL = "MIDI sending label";

	protected JPanel middlePanel;
	private JPanel footerPanel;
	private JLabel nameLabel;
	private JLabel fileLabel;
	protected JTextField nameTextField;
	protected JTextField fileTextField;
	protected JLabel midiSendingSignatureLabel;
	public JLabel midiSendingSignatureValueLabel;
	private JButton searchButton;

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
		setResizable(false);
		setName(NAME);

		// set layout
		getContentPane().setLayout(new BorderLayout());
		middlePanel = new JPanel(new GridBagLayout());
		add(middlePanel, BorderLayout.CENTER);
		footerPanel = new JPanel(new FlowLayout());
		add(footerPanel, BorderLayout.PAGE_END);

		// Name
		createEntryName();

		// File
		createEntryFile();

		// sending midi signature
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

		GridBagConstraints c = new GridBagConstraints();
		nameLabel = new JLabel(LABEL_NAME);
		nameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, nameLabel
				.getPreferredSize().height));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		middlePanel.add(nameLabel, c);

		nameTextField = new JTextField();
		nameTextField.setPreferredSize(new Dimension(TEXT_PANE_WIDTH,
				nameTextField.getPreferredSize().height));
		nameTextField.setName(NAME_NAME_TEXT_FIELD);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		middlePanel.add(nameTextField, c);
	}

	/**
	 * Creates the text pane for the file to open with a label and a search
	 * button
	 */
	private void createEntryFile() {

		GridBagConstraints c = new GridBagConstraints();
		fileLabel = new JLabel(LABEL_FILE);
		fileLabel.setPreferredSize(new Dimension(LABEL_WIDTH, fileLabel
				.getPreferredSize().height));
		c.insets = new Insets(10, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		middlePanel.add(fileLabel, c);

		fileTextField = new JTextField();
		fileTextField.setPreferredSize(new Dimension(TEXT_PANE_WIDTH,
				fileTextField.getPreferredSize().height));
		fileTextField.setName(NAME_FILE_TEXT_FIELD);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		middlePanel.add(fileTextField, c);

		searchButton = new JButton(BUTTON_SEARCH);
		searchButton.addActionListener(new SearchAction(this));
		searchButton.setName(NAME_SEARCH_BUTTON);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		middlePanel.add(searchButton, c);

	}

	/**
	 * Creates the label for the midi sending signature
	 */
	private void createMidiSendingSignature() {

		GridBagConstraints c = new GridBagConstraints();
		midiSendingSignatureLabel = new JLabel(LABEL_SENDING_MIDI);
		midiSendingSignatureLabel.setPreferredSize(new Dimension(LABEL_WIDTH,
				midiSendingSignatureLabel.getPreferredSize().height));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		middlePanel.add(midiSendingSignatureLabel, c);

		midiSendingSignatureValueLabel = new JLabel("x");
		midiSendingSignatureValueLabel
				.setName(NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL);
		midiSendingSignatureValueLabel.setPreferredSize(new Dimension(
				TEXT_PANE_WIDTH, midiSendingSignatureValueLabel
						.getPreferredSize().height));

		setMidiSendingSignatureValueLabelText();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		middlePanel.add(midiSendingSignatureValueLabel, c);
	}

	/**
	 * Sets the text of the midi sending signature label
	 */
	protected void setMidiSendingSignatureValueLabelText() {
		midiSendingSignatureValueLabel.setText(fileListService
				.getUniqueSendingMidiSignature());
	}

	/**
	 * Returns the text pane with the file path to open
	 * 
	 * @return the file text pane
	 */
	public JTextField getFileTextPane() {
		return fileTextField;
	}

	/**
	 * Opens a file chooser and puts the return value to the file text pane
	 * 
	 * @author aguelle
	 * 
	 */
	class SearchAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private final JFileChooser fileChooser;
		private AddFrame parent;

		/**
		 * Constructor
		 * 
		 * @param parent
		 *            the parent component
		 */
		public SearchAction(AddFrame parent) {
			this.parent = parent;
			UIManager.put("FileChooser.acceptAllFileFilterText", "All Files");
			fileChooser = new JFileChooser();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = fileChooser.showOpenDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				parent.getFileTextPane().setText(file.getAbsolutePath());
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
					fileTextField.getText(),
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
