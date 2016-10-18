package com.midi_automator.view.automationconfiguration;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.view.MidiLearnPopupMenu;
import com.midi_automator.view.ScaleableImageIcon;

/**
 * A configuration panel for adding, deleting and editing GUI automation.
 * 
 * @author aguelle
 * @date 10-12-2014
 */
@org.springframework.stereotype.Component
@Scope("prototype")
public class GUIAutomationConfigurationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final String MIN_SIMILARITY_LABEL = "Similarity:";
	private final String ADD_LABEL = "+";
	private final String DELETE_LABEL = "-";
	private final int EDITOR_BUTTON_SIZE = 41; // min size for Windows

	private JPanel globalSettingsPanel;
	private JPanel editorPanel;
	private JSpinner minSimilaritySpinner;
	private JButton addButton;
	private JButton deleteButton;
	private boolean initialized = false;

	public static final String NAME_ADD_BUTTON = "automation add button";
	public static final String NAME_DELETE_BUTTON = "automation delete button";
	public static final String NAME_MIN_SIMILARITY_SPINNER = "automation min similarity";

	private final Float MIN_SIMILARITY_STEP_SIZE = 0.01f;

	private GUIAutomationConfigurationTable configurationTable;

	@Autowired
	private ApplicationContext ctx;

	public static final String NAME = "GUI automation panel";

	/**
	 * Initializes the panel
	 */
	public void init() {

		if (!initialized) {

			setName(NAME);
			setLayout(new GridBagLayout());

			createGlobalSettingsPanel();
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.WEST;
			add(globalSettingsPanel, c);

			// configuration table
			configurationTable = ctx.getBean("GUIAutomationConfigurationTable",
					GUIAutomationConfigurationTable.class);

			configurationTable.init();
			configurationTable.setCache(configurationTable
					.getSelectionBackground());
			JScrollPane scrollPane = new JScrollPane(configurationTable);

			c.gridx = 0;
			c.gridy = 1;
			add(scrollPane, c);

			createEditorPanel();
			c.gridx = 0;
			c.gridy = 2;
			c.anchor = GridBagConstraints.WEST;
			add(editorPanel, c);

			initialized = true;
		}
	}

	/**
	 * Gets the popup menu
	 * 
	 * @return The popup menu
	 */
	public MidiLearnPopupMenu getPopupMenu() {
		return configurationTable.getPopupMenu();
	}

	/**
	 * Gets the configuration table
	 * 
	 * @return The configuration JTable
	 */
	public GUIAutomationConfigurationTable getGUIAutomationsTable() {
		return configurationTable;
	}

	/**
	 * Gets the minimum similarity
	 * 
	 * @return The minimum similarity
	 */
	public float getMinSimilarity() {
		return (float) minSimilaritySpinner.getValue();
	}

	/**
	 * Sets the minimum similarity
	 * 
	 * @param minSimilarity
	 *            The minimum similarity
	 */
	public void setMinSimilarity(float minSimilarity) {

		if (minSimilarity != 0) {
			minSimilaritySpinner.setValue(minSimilarity);
		}
	}

	/**
	 * Gets all GUI automations
	 * 
	 * @return An array with all GUI automations
	 */
	public GUIAutomation[] getGUIAutomations() {

		TableModel model = configurationTable.getModel();
		int numberOfConfigurations = model.getRowCount();
		GUIAutomation[] guiAutomations = new GUIAutomation[numberOfConfigurations];

		// stop open edits
		if (configurationTable.isEditing()) {
			configurationTable.getCellEditor().stopCellEditing();
		}

		// build GUI automation objects
		for (int row = 0; row < numberOfConfigurations; row++) {

			String imagePath = getAutomationImagePath(row);
			String type = getAutomationType(row);
			String trigger = getAutomationTrigger(row);
			long delay = getAutomationMinDelay(row);
			long timeout = getAutomationTimeout(row);
			String midiSignature = getAutomationMidiSignature(row);
			float scanRate = getAutomationScanRate(row);
			boolean isMovable = getAutomationMovable(row);

			GUIAutomation guiAutomation = new GUIAutomation(imagePath, type,
					trigger, delay, timeout, midiSignature, scanRate, isMovable);
			guiAutomations[row] = guiAutomation;
		}

		return guiAutomations;
	}

	/**
	 * Gets the image path for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The path to the image
	 */
	private String getAutomationImagePath(int row) {

		String imagePath = null;

		ScaleableImageIcon image = getAutomationsTableValue(
				GUIAutomationConfigurationTable.COLNAME_IMAGE, row,
				ScaleableImageIcon.class);

		if (image != null) {
			imagePath = image.getPath();
		}
		return imagePath;
	}

	/**
	 * Gets the type for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation type
	 */
	private String getAutomationType(int row) {

		return getAutomationsTableValue(
				GUIAutomationConfigurationTable.COLNAME_TYPE, row, String.class);
	}

	/**
	 * Gets the trigger for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation trigger
	 */
	private String getAutomationTrigger(int row) {

		return getAutomationsTableValue(
				GUIAutomationConfigurationTable.COLNAME_TRIGGER, row,
				String.class);
	}

	/**
	 * Gets the minimum delay for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation minimum delay
	 */
	private long getAutomationMinDelay(int row) {

		return getAutomationsTableValue(
				GUIAutomationConfigurationTable.COLNAME_MIN_DELAY, row,
				Long.class);
	}

	/**
	 * Gets the timeout for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation timeout
	 */
	private long getAutomationTimeout(int row) {

		return getAutomationsTableValue(
				GUIAutomationConfigurationTable.COLNAME_TIMEOUT, row,
				Long.class);
	}

	/**
	 * Gets the midi signature for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation midi signature
	 */
	private String getAutomationMidiSignature(int row) {

		return getAutomationsTableValue(
				GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE, row,
				String.class);
	}

	/**
	 * Gets the scan rate for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation minimum similarity
	 */
	private float getAutomationScanRate(int row) {

		return getAutomationsTableValue(
				GUIAutomationConfigurationTable.COLNAME_SCAN_RATE, row,
				Float.class);
	}

	/**
	 * Gets the movable option for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation movable option
	 */
	private boolean getAutomationMovable(int row) {

		return getAutomationsTableValue(
				GUIAutomationConfigurationTable.COLNAME_MOVABLE, row,
				Boolean.class);
	}

	/**
	 * Gets the value from the automations table
	 * 
	 * @param columnName
	 *            The name of the column
	 * @param row
	 *            The row
	 * @param type
	 *            The type of calue
	 * @return The value
	 */
	@SuppressWarnings("unchecked")
	private <T> T getAutomationsTableValue(String columnName, int row,
			Class<T> type) {

		int index = configurationTable.getColumn(columnName).getModelIndex();
		Object value = configurationTable.getModel().getValueAt(row, index);

		return (T) value;
	}

	/**
	 * Creates the panel for the global settings.
	 */
	private void createGlobalSettingsPanel() {

		globalSettingsPanel = new JPanel();
		globalSettingsPanel.setLayout(new FlowLayout());

		JLabel minSimilarityLabel = new JLabel(MIN_SIMILARITY_LABEL);

		SpinnerModel spinnerModel = new SpinnerNumberModel(
				GUIAutomation.DEFAULT_MIN_SIMILARITY,
				GUIAutomation.MIN_SIMILARITY_MIN_VALUE,
				GUIAutomation.MIN_SIMILARITY_MAX_VALUE,
				MIN_SIMILARITY_STEP_SIZE);

		minSimilaritySpinner = new JSpinner(spinnerModel);
		minSimilaritySpinner.setName(NAME_MIN_SIMILARITY_SPINNER);

		globalSettingsPanel.add(minSimilarityLabel);
		globalSettingsPanel.add(minSimilaritySpinner);
	}

	/**
	 * Creates the panel with add and delete buttons.
	 */
	private void createEditorPanel() {

		editorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		addButton = new JButton(ADD_LABEL);
		addButton.setName(NAME_ADD_BUTTON);
		addButton.addActionListener(new AddAutomationListener());
		addButton.setPreferredSize(new Dimension(EDITOR_BUTTON_SIZE,
				EDITOR_BUTTON_SIZE));
		editorPanel.add(addButton);

		deleteButton = new JButton(DELETE_LABEL);
		deleteButton.setName(NAME_DELETE_BUTTON);
		deleteButton.addActionListener(new DeleteAutomationListener());
		deleteButton.setPreferredSize(new Dimension(EDITOR_BUTTON_SIZE,
				EDITOR_BUTTON_SIZE));
		editorPanel.add(deleteButton);
	}

	/**
	 * Adds a new configuration to the configuration table.
	 */
	class AddAutomationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			configurationTable.setAutomation(null, null, null,
					GUIAutomation.DEFAULT_MIN_DELAY,
					GUIAutomation.DEFAULT_TIMEOUT, null,
					GUIAutomation.DEFAULT_SCAN_RATE,
					GUIAutomation.DEFAULT_IS_MOVABLE, -1);
		}
	}

	/**
	 * Deletes the chosen configuration from the configuration table.
	 */
	class DeleteAutomationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			configurationTable.deleteAutomation(configurationTable
					.getSelectedRow());
		}
	}
}