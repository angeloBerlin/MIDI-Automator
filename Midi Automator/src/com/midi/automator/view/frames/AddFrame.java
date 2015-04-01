package com.midi.automator.view.frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.midi.automator.IApplication;

public class AddFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final String TITLE = "Add";
	protected final int WIDTH = 500;
	protected final int HEIGHT = 130;
	private final int LOCATION_X_OFFSET = 50;
	private final int LOCATION_Y_OFFSET = 50;
	protected final int LABEL_WIDTH = 50;
	protected final int TEXT_PANE_WIDTH = 340;
	private final String LABEL_NAME = "Name:";
	private final String LABEL_FILE = "File:";
	private final String BUTTON_SEARCH = "Search...";
	private final String BUTTON_SAVE = "Save";
	private final String BUTTON_CANCEL = "Cancel";

	protected JPanel middlePanel;
	private JPanel footerPanel;
	private JLabel nameLabel;
	private JLabel fileLabel;
	protected JTextField nameTextField;
	protected JTextField fileTextField;
	private JButton searchButton;
	protected JButton buttonSave;
	private JButton buttonCancel;

	protected IApplication application;
	protected JFrame programFrame;

	protected ActionListener saveListener;
	protected ActionListener cancelListener;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The main application
	 * @param programFrame
	 *            The main programFrame
	 * @throws HeadlessException
	 *             If no GUI available
	 */
	public AddFrame(IApplication application, JFrame programFrame)
			throws HeadlessException {
		super();

		this.application = application;
		this.programFrame = programFrame;
		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocation(this.programFrame.getLocationOnScreen().x
				+ LOCATION_X_OFFSET, this.programFrame.getLocationOnScreen().y
				+ LOCATION_Y_OFFSET);

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

		// Save
		buttonSave = new JButton(BUTTON_SAVE);
		saveListener = new SaveAction();
		buttonSave.addActionListener(saveListener);
		footerPanel.add(buttonSave);

		// Cancel
		buttonCancel = new JButton(BUTTON_CANCEL);
		cancelListener = new CancelAction();
		buttonCancel.addActionListener(cancelListener);
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
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		middlePanel.add(fileTextField, c);

		searchButton = new JButton(BUTTON_SEARCH);
		searchButton.addActionListener(new SearchAction(this));
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		middlePanel.add(searchButton, c);

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
		private final JFileChooser fc;
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
			JFileChooser.setDefaultLocale(Locale.ENGLISH);
			fc = new JFileChooser();
			fc.setLocale(Locale.ENGLISH);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = fc.showOpenDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
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
			application.addItem(nameTextField.getText(),
					fileTextField.getText());
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
