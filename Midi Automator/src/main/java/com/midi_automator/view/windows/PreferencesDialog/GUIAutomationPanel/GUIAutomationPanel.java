package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.utils.CommonUtils;
import com.midi_automator.view.windows.PreferencesDialog.ScaleableImageIcon;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.menus.KeyLearnPopupMenu;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.listener.AddAutomationListener;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.listener.DeleteAutomationListener;
import com.midi_automator.view.windows.menus.MidiLearnPopupMenu;

/**
 * A configuration panel for adding, deleting and editing GUI automation.
 * 
 * @author aguelle
 * @date 10-12-2014
 */
@Component
public class GUIAutomationPanel extends JPanel {

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

	@Autowired
	private GUIAutomationTable configurationTable;

	@Autowired
	private AddAutomationListener addAutomationListener;
	@Autowired
	private DeleteAutomationListener deleteAutomationListener;

	public static final String NAME = "GUI automation panel";

	/**
	 * Initializes the panel
	 */
	public void init() {

		GridBagConstraints c = new GridBagConstraints();

		if (!initialized) {

			setName(NAME);
			setLayout(new GridBagLayout());

			createGlobalSettingsPanel();

			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.WEST;
			add(globalSettingsPanel, c);

			configurationTable.setCache(configurationTable
					.getSelectionBackground());
			JScrollPane scrollPane = new JScrollPane(configurationTable);

			c.insets = new Insets(0, 20, 0, 20);
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

		configurationTable.init();
	}

	/**
	 * Reloads an initialized panel
	 */
	public void reload() {
		if (initialized) {
			configurationTable.init();
		}
	}

	/**
	 * Gets the midi learn popup menu
	 * 
	 * @return The midi learn popup menu
	 */
	public MidiLearnPopupMenu getMidiLearnPopupMenu() {
		return configurationTable.getMidiLearnPopupMenu();
	}

	/**
	 * Gets the key learn popup menu
	 * 
	 * @return The key learn popup menu
	 */
	public KeyLearnPopupMenu getKeyLearnPopupMenu() {
		return configurationTable.getKeyLearnPopupMenu();
	}

	/**
	 * Gets the configuration table
	 * 
	 * @return The configuration JTable
	 */
	public GUIAutomationTable getGUIAutomationsTable() {
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
	 * Gets all GUI automation
	 * 
	 * @return An array with all GUI automation
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
			int[] keyCodes = getAutomationKeyCodes(row);
			String trigger = getAutomationTrigger(row);
			String focus = getAutomationFocusedProgram(row);
			long delay = getAutomationMinDelay(row);
			long timeout = getAutomationTimeout(row);
			String midiSignature = getAutomationMidiSignature(row);
			float scanRate = getAutomationScanRate(row);
			boolean isMovable = getAutomationMovable(row);

			GUIAutomation guiAutomation = new GUIAutomation(imagePath, type,
					trigger, focus, delay, timeout, midiSignature, scanRate,
					isMovable, keyCodes);
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
				GUIAutomationTable.COLNAME_IMAGE, row, ScaleableImageIcon.class);

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

		return getAutomationsTableValue(GUIAutomationTable.COLNAME_TYPE, row,
				String.class);
	}

	/**
	 * Gets the key codes for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation key codes, <NULL> if there are no key codes
	 */
	private int[] getAutomationKeyCodes(int row) {

		String keyCodesString = getAutomationsTableValue(
				GUIAutomationTable.COLNAME_KEYS, row, String.class);

		if (keyCodesString == null) {
			return null;
		}
		if (keyCodesString.equals("")) {
			return null;
		}

		String[] keyCodeStrings = keyCodesString.split(Pattern
				.quote(GUIAutomationTable.KEYS_DELIMITER));
		int[] keyCodes = CommonUtils.stringArrayToIntArray(keyCodeStrings);
		return keyCodes;

	}

	/**
	 * Gets the trigger for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation trigger
	 */
	private String getAutomationTrigger(int row) {

		return getAutomationsTableValue(GUIAutomationTable.COLNAME_TRIGGER,
				row, String.class);
	}

	/**
	 * Gets the program to focus for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation focus
	 */
	private String getAutomationFocusedProgram(int row) {

		return getAutomationsTableValue(GUIAutomationTable.COLNAME_FOCUS, row,
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

		return getAutomationsTableValue(GUIAutomationTable.COLNAME_MIN_DELAY,
				row, Long.class);
	}

	/**
	 * Gets the timeout for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation timeout
	 */
	private long getAutomationTimeout(int row) {

		return getAutomationsTableValue(GUIAutomationTable.COLNAME_TIMEOUT,
				row, Long.class);
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
				GUIAutomationTable.COLNAME_MIDI_SIGNATURE, row, String.class);
	}

	/**
	 * Gets the scan rate for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation minimum similarity
	 */
	private float getAutomationScanRate(int row) {

		return getAutomationsTableValue(GUIAutomationTable.COLNAME_SCAN_RATE,
				row, Float.class);
	}

	/**
	 * Gets the movable option for a specific row from the automation table
	 * 
	 * @param row
	 *            The row
	 * @return The automation movable option
	 */
	private boolean getAutomationMovable(int row) {

		return getAutomationsTableValue(GUIAutomationTable.COLNAME_MOVABLE,
				row, Boolean.class);
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
		addButton.addActionListener(addAutomationListener);
		addButton.setPreferredSize(new Dimension(EDITOR_BUTTON_SIZE,
				EDITOR_BUTTON_SIZE));
		editorPanel.add(addButton);

		deleteButton = new JButton(DELETE_LABEL);
		deleteButton.setName(NAME_DELETE_BUTTON);
		deleteButton.addActionListener(deleteAutomationListener);
		deleteButton.setPreferredSize(new Dimension(EDITOR_BUTTON_SIZE,
				EDITOR_BUTTON_SIZE));
		editorPanel.add(deleteButton);
	}
}