package com.midi_automator.view.automationconfiguration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.CacheableJTable;
import com.midi_automator.view.DeActivateableMouseAdapter;
import com.midi_automator.view.MidiLearnPopupMenu;
import com.midi_automator.view.ScaleableImageIcon;

/**
 * A JTable for configuring the GUI automation
 * 
 * @author aguelle
 * 
 */
public class GUIAutomationConfigurationTable extends CacheableJTable {

	static Logger log = Logger.getLogger(MidiAutomator.class.getName());

	private static final long serialVersionUID = 1L;

	private final int TABLEWIDTH = 950;
	private final int TABLEHEIGHT = 140;
	private final int ROWHEIGHT = 40;
	private final int COLWIDTH_SCREENSHOT = 100;
	private final int COLWIDTH_MIN_DELAY = 40;
	private final int COLWIDTH_MIDI_MESSAGE = 250;
	private final int COLWIDTH_MIN_SIMILARITY = 30;
	private final int COLWIDTH_MOVABLE = 30;
	private final int COLWIDTH_SCREENSHOT_SEARCH = 90;
	private final int MARGIN_SCREENSHOT = 4;

	private final String LABEL_SEARCHBUTTON = "Screenshot...";

	private final String COLNAME_IMAGE = "Screenshot";
	private final String COLNAME_TYPE = "Type";
	private final String COLNAME_TRIGGER = "Trigger";
	private final String COLNAME_MIN_DELAY = "Delay (ms)";
	private final String COLNAME_MIDI_SIGNATURE = "Midi Message";
	private final String COLNAME_MIN_SIMILARITY = "Similarity";
	private final String COLNAME_MOVABLE = "Movable";
	private final String COLNAME_SEARCH_BUTTON = "";
	private Vector<String> columnNames;

	private final String DEFAULT_TYPE = GUIAutomation.CLICKTYPE_LEFT;
	private final String DEFAULT_TRIGGER = GUIAutomation.CLICKTRIGGER_ALWAYS;
	private final String DEFAULT_MIDI_MESSAGE = "";
	private final Long MIN_DELAY_STEP_SIZE = 1L;
	private final Float MIN_SIMILARITY_STEP_SIZE = 0.01f;

	private final String NAME_MENU_ITEM_MIDI_LEARN = "GUIAutomationConfigurationTable midi learn";
	private final String NAME_COMBOBOX_TRIGGER_EDITOR = "COMBOBOX_TRIGGER_EDITOR";

	private Vector<Vector<Object>> data;

	private MidiLearnPopupMenu popupMenu;
	private final MidiAutomator APPLICATION;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The application
	 */
	public GUIAutomationConfigurationTable(MidiAutomator application) {

		APPLICATION = application;
		data = new Vector<Vector<Object>>();

		columnNames = new Vector<String>();
		columnNames.add(ConfigurationTableModel.COLUMN_INDEX_IMAGE,
				COLNAME_IMAGE);
		columnNames
				.add(ConfigurationTableModel.COLUMN_INDEX_TYPE, COLNAME_TYPE);
		columnNames.add(ConfigurationTableModel.COLUMN_INDEX_TRIGGER,
				COLNAME_TRIGGER);
		columnNames.add(ConfigurationTableModel.COLUMN_INDEX_MIN_DELAY,
				COLNAME_MIN_DELAY);
		columnNames.add(ConfigurationTableModel.COLUMN_INDEX_MIDI_SIGNATURE,
				COLNAME_MIDI_SIGNATURE);
		columnNames.add(ConfigurationTableModel.COLUMN_INDEX_MIN_SIMILARITY,
				COLNAME_MIN_SIMILARITY);
		columnNames.add(ConfigurationTableModel.COLUMN_INDEX_MOVABLE,
				COLNAME_MOVABLE);
		columnNames.add(ConfigurationTableModel.COLUMN_INDEX_SEARCH_BUTTON,
				COLNAME_SEARCH_BUTTON);

		ConfigurationTableModel tableModel = new ConfigurationTableModel();
		tableModel.setDataVector(data, columnNames);
		setModel(tableModel);

		setPreferredScrollableViewportSize(new Dimension(TABLEWIDTH,
				TABLEHEIGHT));

		setRowHeight(ROWHEIGHT);

		setShowGrid(true);
		setGridColor(Color.LIGHT_GRAY);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// screenshot
		getColumn(COLNAME_IMAGE).setPreferredWidth(COLWIDTH_SCREENSHOT);

		// generate click type ComboBox
		String[] clickTypes = { GUIAutomation.CLICKTYPE_LEFT,
				GUIAutomation.CLICKTYPE_RIGHT, GUIAutomation.CLICKTYPE_DOUBLE };
		TableCellRenderer clickTypeComboBoxRenderer = new JTableComboBoxRenderer(
				clickTypes);
		TableCellEditor clickTypeComboBoxEditor = new JTableComboBoxEditor(
				clickTypes);

		getColumn(COLNAME_TYPE).setCellRenderer(clickTypeComboBoxRenderer);
		getColumn(COLNAME_TYPE).setCellEditor(clickTypeComboBoxEditor);

		// click trigger
		List<String> triggerTypes = new ArrayList<String>();
		triggerTypes.add(GUIAutomation.CLICKTRIGGER_ALWAYS);
		triggerTypes.add(GUIAutomation.CLICKTRIGGER_ONCE_PER_CHANGE);
		triggerTypes.add(GUIAutomation.CLICKTRIGGER_ONCE);

		for (String deviceName : MidiUtils.getMidiDeviceSignatures("IN")) {
			triggerTypes.add(GUIAutomation.CLICKTRIGGER_MIDI + deviceName);
		}

		JTableComboBoxRenderer triggerComboBoxRenderer = new JTableComboBoxRenderer(
				triggerTypes.toArray(new String[0]));
		JComboBox<String> triggerComboBox = new JComboBox<String>(
				triggerTypes.toArray(new String[0]));
		triggerComboBox.addActionListener(new TriggerComboBoxListener());
		JTableComboBoxEditor triggerComboBoxEditor = new JTableComboBoxEditor(
				triggerComboBox);
		getColumn(COLNAME_TRIGGER).setCellRenderer(triggerComboBoxRenderer);
		getColumn(COLNAME_TRIGGER).setCellEditor(triggerComboBoxEditor);

		// min delay field
		getColumn(COLNAME_MIN_DELAY).setPreferredWidth(COLWIDTH_MIN_DELAY);
		SpinnerModel minDelaySpinnerModel = new SpinnerNumberModel(
				GUIAutomation.DEFAULT_MIN_DELAY,
				GUIAutomation.MIN_DELAY_MIN_VALUE,
				GUIAutomation.MIN_DELAY_MAX_VALUE, MIN_DELAY_STEP_SIZE);
		JTableSpinnerEditor minDelayEditor = new JTableSpinnerEditor(
				minDelaySpinnerModel);
		getColumn(COLNAME_MIN_DELAY).setCellEditor(minDelayEditor);

		// midi message
		getColumn(COLNAME_MIDI_SIGNATURE).setPreferredWidth(
				COLWIDTH_MIDI_MESSAGE);
		JTableNonEditableRenderer nonEditableRenderer = new JTableNonEditableRenderer();
		getColumn(COLNAME_MIDI_SIGNATURE).setCellRenderer(nonEditableRenderer);

		// min similarity field
		getColumn(COLNAME_MIN_SIMILARITY).setPreferredWidth(
				COLWIDTH_MIN_SIMILARITY);
		SpinnerModel minSimilaritySpinnerModel = new SpinnerNumberModel(
				GUIAutomation.DEFAULT_MIN_SIMILARITY,
				GUIAutomation.MIN_SIMILARITY_MIN_VALUE,
				GUIAutomation.MIN_SIMILARITY_MAX_VALUE,
				MIN_SIMILARITY_STEP_SIZE);
		JTableSpinnerEditor minSimilarityEditor = new JTableSpinnerEditor(
				minSimilaritySpinnerModel);
		getColumn(COLNAME_MIN_SIMILARITY).setCellEditor(minSimilarityEditor);

		// movable
		getColumn(COLNAME_MOVABLE).setPreferredWidth(COLWIDTH_MOVABLE);

		// browse button
		getColumn(COLNAME_SEARCH_BUTTON).setPreferredWidth(
				COLWIDTH_SCREENSHOT_SEARCH);
		JTableButtonRenderer buttonRenderer = new JTableButtonRenderer(
				LABEL_SEARCHBUTTON);
		JTableButtonEditor buttonEditor = new JTableButtonEditor(
				LABEL_SEARCHBUTTON);
		buttonEditor.addActionListener(new ImageSearchButtonListener(this));
		getColumn(COLNAME_SEARCH_BUTTON).setCellRenderer(buttonRenderer);
		getColumn(COLNAME_SEARCH_BUTTON).setCellEditor(buttonEditor);

		// popup Menu
		addMouseListener(new PopupListener());
		popupMenu = new MidiLearnPopupMenu(null, application);
		popupMenu.setName(NAME_MENU_ITEM_MIDI_LEARN);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		// Return the class of a column to choose the appropriate renderer
		Object obj = getValueAt(0, column);
		Class<?> objClass = obj.getClass();
		return objClass;
	}

	/**
	 * Sets the click image of a specific row.
	 * 
	 * @param path
	 *            Path to the click image file
	 * @param row
	 *            Number of the row
	 */
	public void setClickImage(String path, int row)
			throws AutomationIndexDoesNotExistException {

		DefaultTableModel model = (DefaultTableModel) getModel();

		ScaleableImageIcon icon = initScreenshotIcon(path);

		try {
			model.setValueAt(icon, row,
					ConfigurationTableModel.COLUMN_INDEX_IMAGE);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AutomationIndexDoesNotExistException();
		}
	}

	/**
	 * Adds an automation row to the table.
	 * 
	 * @param imagePath
	 *            The path to the click image, <NULL> for empty screenshot
	 * @param type
	 *            The type of automation, <NULL> for default type
	 * @param trigger
	 *            The trigger for the automation, <NULL> for default trigger
	 * @param minDelay
	 *            The minimum delay for the automation, <NULL> for default
	 *            trigger
	 * @param midiSignature
	 *            The midi signature, <NULL> for empty signature
	 * @param minSimilarity
	 *            The defualt value for the min similarity
	 * @param isMovable
	 *            <TRUE> if image may occur on different places on the screen,
	 *            <FALSE> is not
	 */
	private void addAutomation(String imagePath, String type, String trigger,
			long minDelay, String midiSignature, float minSimilarity,
			boolean isMovable) {

		// extend table model
		DefaultTableModel model = (DefaultTableModel) getModel();
		Vector<Object> rowData = new Vector<Object>();

		// fill table data
		rowData.add(ConfigurationTableModel.COLUMN_INDEX_IMAGE,
				initScreenshotIcon(imagePath));
		rowData.add(ConfigurationTableModel.COLUMN_INDEX_TYPE, initType(type));
		rowData.add(ConfigurationTableModel.COLUMN_INDEX_TRIGGER,
				initTrigger(trigger));
		rowData.add(ConfigurationTableModel.COLUMN_INDEX_MIN_DELAY, minDelay);
		rowData.add(ConfigurationTableModel.COLUMN_INDEX_MIDI_SIGNATURE,
				initMidiMessage(midiSignature));
		rowData.add(ConfigurationTableModel.COLUMN_INDEX_MIN_SIMILARITY,
				minSimilarity);
		rowData.add(ConfigurationTableModel.COLUMN_INDEX_MOVABLE, isMovable);

		model.addRow(rowData);

		JTableComboBoxEditor cellEditor = (JTableComboBoxEditor) getCellEditor(
				model.getRowCount() - 1,
				ConfigurationTableModel.COLUMN_INDEX_TRIGGER);
		@SuppressWarnings("unchecked")
		JComboBox<String> triggerEditorComboBox = (JComboBox<String>) cellEditor
				.getComponent();
		triggerEditorComboBox.setName(NAME_COMBOBOX_TRIGGER_EDITOR + "_"
				+ (model.getRowCount() - 1));
	}

	/**
	 * Sets an automation row to the table. If the row does not exist, a new row
	 * will be added.
	 * 
	 * @param imagePath
	 *            The path to the click image, <NULL> for empty screenshot
	 * @param type
	 *            The type of automation, <NULL> for default type
	 * @param trigger
	 *            The trigger for the automation, <NULL> for default trigger
	 * @param minDelay
	 *            The minimum delay for the automation
	 * @param midiSignature
	 *            The midi signature, <NULL> for empty signature
	 * @param minSimilarity
	 *            The default value for the minimum image recognition similarity
	 * @param isMovable
	 *            <TRUE> if image may occur on different places on the screen,
	 *            <FALSE> is not
	 * @param index
	 *            The index of the automation, if < 0 a new row will be added
	 */
	public void setAutomation(String imagePath, String type, String trigger,
			long minDelay, String midiSignature, float minSimilarity,
			boolean isMovable, int index) {

		TableModel model = getModel();

		if (model.getRowCount() <= index || index < 0) {
			addAutomation(imagePath, type, trigger, minDelay, midiSignature,
					minSimilarity, isMovable);
		} else {

			model.setValueAt(initScreenshotIcon(imagePath), index,
					ConfigurationTableModel.COLUMN_INDEX_IMAGE);
			model.setValueAt(initType(type), index,
					ConfigurationTableModel.COLUMN_INDEX_TYPE);
			model.setValueAt(initTrigger(trigger), index,
					ConfigurationTableModel.COLUMN_INDEX_TRIGGER);
			model.setValueAt(minDelay, index,
					ConfigurationTableModel.COLUMN_INDEX_MIN_DELAY);
			model.setValueAt(initMidiMessage(midiSignature), index,
					ConfigurationTableModel.COLUMN_INDEX_MIDI_SIGNATURE);
			model.setValueAt(minSimilarity, index,
					ConfigurationTableModel.COLUMN_INDEX_MIN_SIMILARITY);
			model.setValueAt(isMovable, index,
					ConfigurationTableModel.COLUMN_INDEX_MOVABLE);
		}
	}

	/**
	 * Deletes an automation row from the table.
	 * 
	 * @param row
	 *            The row that shall be deleted
	 */
	public void deleteAutomation(int row) {

		if (row >= 0) {
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);
		}
	}

	/**
	 * Gets the popup menu
	 * 
	 * @return The popup menu
	 */
	public MidiLearnPopupMenu getPopupMenu() {
		return popupMenu;
	}

	/**
	 * Sets the midi message for a GUI automation
	 * 
	 * @param signature
	 *            The midi message
	 * @param row
	 *            The row of the autoamtion
	 * @throws AutomationIndexDoesNotExistException
	 */
	public void setMidiSignature(String signature, int row)
			throws AutomationIndexDoesNotExistException {

		DefaultTableModel model = (DefaultTableModel) getModel();

		try {
			// set midi signature
			model.setValueAt(signature, row,
					ConfigurationTableModel.COLUMN_INDEX_MIDI_SIGNATURE);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AutomationIndexDoesNotExistException();
		}
	}

	/**
	 * Initializes a scaleable image icon for the screenshot.
	 * 
	 * @param path
	 *            The path to the image, if <NULL> an empty icon will be
	 *            returned
	 * @return A scaleable image icon
	 */
	private ScaleableImageIcon initScreenshotIcon(String path) {

		if (path == null) {
			return new ScaleableImageIcon();
		} else {
			int width = COLWIDTH_SCREENSHOT - (MARGIN_SCREENSHOT * 2);
			int height = ROWHEIGHT - (MARGIN_SCREENSHOT * 2);
			return new ScaleableImageIcon(path, width, height);
		}
	}

	/**
	 * Initializes a trigger value.
	 * 
	 * @param trigger
	 *            The trigger value, if <NULL> the default trigger will be
	 *            returned.
	 * @return The initialized trigger
	 */
	private String initTrigger(String trigger) {

		if (trigger == null) {
			return DEFAULT_TRIGGER;
		} else {
			return trigger;
		}
	}

	/**
	 * Initializes a type value.
	 * 
	 * @param type
	 *            The trigger value, if <NULL> the default value will be
	 *            returned.
	 * @return The initialized trigger
	 */
	private String initType(String type) {

		if (type == null) {
			return DEFAULT_TYPE;
		} else {
			return type;
		}
	}

	/**
	 * Initializes a midi signature.
	 * 
	 * @param message
	 *            The trigger value, if <NULL> the default value will be
	 *            returned.
	 * @return The initialized trigger
	 */
	private String initMidiMessage(String message) {

		if (message == null) {
			return DEFAULT_MIDI_MESSAGE;
		} else {
			return message;
		}
	}

	/**
	 * ActionListener for the image search button
	 * 
	 * @author aguelle
	 * 
	 */
	class ImageSearchButtonListener implements ActionListener {

		private final JFileChooser fc = new JFileChooser();
		private GUIAutomationConfigurationTable parent;

		/**
		 * Constructor
		 * 
		 * @param parent
		 *            the parent component
		 */
		public ImageSearchButtonListener(GUIAutomationConfigurationTable parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int row = ((JTableButton) e.getSource()).getRow();
			FileFilter filter = new FileNameExtensionFilter(
					GUIAutomation.SCREENSHOT_FILE_TYPE,
					GUIAutomation.SCREENSHOT_FILE_EXTENSIONS);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					parent.setClickImage(file.getAbsolutePath(), row);
				} catch (AutomationIndexDoesNotExistException ex) {
					log.error(
							"The automation for the click image does not exist.",
							ex);
				}
			}
		}
	}

	/**
	 * Shows the midi learn context menu.
	 * 
	 * @author aguelle
	 * 
	 */
	class PopupListener extends DeActivateableMouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			mouseReleased(e);

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isActive()) {
				maybeShowPopup(e);
			}
		}

		/**
		 * Shows the popup menu if it was a popup trigger. The trigger is OS
		 * specific.
		 * 
		 * @param e
		 *            The mouse event
		 */
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {

				JTable table = (JTable) e.getSource();
				int row = table.rowAtPoint(e.getPoint());
				int column = table.columnAtPoint(e.getPoint());

				if (!table.isRowSelected(row)) {
					table.changeSelection(row, column, false, false);
				}

				// de-/activate midi learn item
				String midiInAutomationDeviceName = (String) table.getValueAt(
						row, ConfigurationTableModel.COLUMN_INDEX_TRIGGER);

				popupMenu.getMidiLearnMenuItem().setEnabled(false);

				if (midiInAutomationDeviceName != null
						&& !midiInAutomationDeviceName
								.equals(MidiAutomatorProperties.VALUE_NULL)
						&& midiInAutomationDeviceName
								.contains(GUIAutomation.CLICKTRIGGER_MIDI)) {
					popupMenu.getMidiLearnMenuItem().setEnabled(true);
				}

				// de-/activate midi unlearn item
				String midiSignature = (String) table.getModel().getValueAt(
						row,
						ConfigurationTableModel.COLUMN_INDEX_MIDI_SIGNATURE);

				popupMenu.getMidiUnlearnMenuItem().setEnabled(false);

				if (midiSignature != null) {
					if (!midiSignature.equals("")) {
						popupMenu.getMidiUnlearnMenuItem().setEnabled(true);
					}
				}

				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	/**
	 * 
	 * @author aguelle
	 * 
	 */
	class TriggerComboBoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			@SuppressWarnings("unchecked")
			JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
			String automationIndex = comboBox.getName().replace(
					NAME_COMBOBOX_TRIGGER_EDITOR + "_", "");

			String functionKey = MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE
					+ "_" + automationIndex;
			String trigger = (String) comboBox.getSelectedItem();

			if (trigger.contains(GUIAutomation.CLICKTRIGGER_MIDI)) {
				APPLICATION.loadMidiDeviceByFunctionKey(functionKey,
						trigger.replace(GUIAutomation.CLICKTRIGGER_MIDI, ""));
			} else {
				APPLICATION.loadMidiDeviceByFunctionKey(functionKey,
						MidiAutomatorProperties.VALUE_NULL);
			}
		}
	}
}
