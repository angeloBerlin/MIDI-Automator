package com.midi_automator.view.automationconfiguration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.GUIAutomationsService;
import com.midi_automator.presenter.services.KeyLearnService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.presenter.services.PresenterService;
import com.midi_automator.utils.GUIUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.CacheableJTable;
import com.midi_automator.view.DeActivateableMouseAdapter;
import com.midi_automator.view.ScaleableImageIcon;
import com.midi_automator.view.listener.KeyLearnListener;
import com.midi_automator.view.menus.ImagePopupMenu;
import com.midi_automator.view.menus.KeyLearnPopupMenu;
import com.midi_automator.view.menus.MidiLearnPopupMenu;

/**
 * A JTable for configuring the GUI automation
 * 
 * @author aguelle
 * 
 */
@org.springframework.stereotype.Component
public class GUIAutomationConfigurationTable extends CacheableJTable {

	static Logger log = Logger.getLogger(Presenter.class.getName());

	private static final long serialVersionUID = 1L;

	private final int COLWIDTH_IMAGE = 100;
	private final int COLWIDTH_TYPE = 110;
	private final int COLWIDTH_KEY = 80;
	private final int COLWIDTH_TRIGGER = 220;
	private final int COLWIDTH_FOCUS = 300;
	private final int COLWIDTH_MIN_DELAY = 80;
	private final int COLWIDTH_TIMEOUT = 80;
	private final int COLWIDTH_MIDI_MESSAGE = 150;
	private final int COLWIDTH_SCAN_RATE = 80;
	private final int COLWIDTH_MOVABLE = 60;
	private final int MARGIN_SCREENSHOT = 4;
	private final int TABLEHEIGHT = 140;
	private final int ROWHEIGHT = 40;
	private final int TABLEWIDTH = COLWIDTH_IMAGE + COLWIDTH_TYPE
			+ COLWIDTH_KEY + COLWIDTH_TRIGGER + COLWIDTH_FOCUS
			+ COLWIDTH_MIN_DELAY + COLWIDTH_TIMEOUT + COLWIDTH_MIDI_MESSAGE
			+ COLWIDTH_SCAN_RATE + COLWIDTH_MOVABLE;

	public static final String COLNAME_IMAGE = "Screenshot";
	public static final String COLNAME_TYPE = "Type";
	public static final String COLNAME_KEYS = "Keys";
	public static final String COLNAME_TRIGGER = "Trigger";
	public static final String COLNAME_FOCUS = "Focus Program";
	public static final String COLNAME_MIN_DELAY = "Delay (ms)";
	public static final String COLNAME_TIMEOUT = "Timeout (ms)";
	public static final String COLNAME_MIDI_SIGNATURE = "Midi Message";
	public static final String COLNAME_SCAN_RATE = "Scans (1/s)";
	public static final String COLNAME_MOVABLE = "Movable";

	private final String DEFAULT_MIDI_MESSAGE = "";
	private static final String DEFAULT_KEYS = "";
	private final Long MIN_DELAY_STEP_SIZE = 1L;
	private final Long TIMEOUT_STEP_SIZE = 1L;
	private final Float SCAN_RATE_STEP_SIZE = 0.1f;

	private final String NAME_POPUP_MENU_MIDI_LEARN = "GUIAutomationConfigurationTable midi learn popup";
	private final String NAME_POPUP_MENU_KEY_LEARN = "GUIAutomationConfigurationTable key learn popup";
	private final String NAME_POPUP_MENU_IMAGE = "GUIAutomationConfigurationTable image popup";

	private final String NAME_COMBOBOX_TRIGGER_EDITOR = "COMBOBOX_TRIGGER_EDITOR";

	public static final String NAME = "GUI automation table";
	public static final String KEYS_DELIMITER = "+";

	private DefaultTableModel tableModel = new ConfigurationTableModel();
	private Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	private Vector<String> columnNames = new Vector<String>();

	@Autowired
	@Qualifier("midiLearnPopupMenu")
	private MidiLearnPopupMenu midiLearnPopupMenu;

	@Autowired
	@Qualifier("keyLearnPopupMenu")
	private KeyLearnPopupMenu keyLearnPopupMenu;

	@Autowired
	@Qualifier("imagePopupMenu")
	private ImagePopupMenu imagePopupMenu;

	@Autowired
	private KeyLearnListener keyLearnListener;

	@Autowired
	private KeyLearnService keyLearnService;

	@Autowired
	private Presenter presenter;

	@Autowired
	private MidiService midiDevicesService;

	@Autowired
	protected PresenterService presenterService;

	@Autowired
	protected GUIAutomationsService guiAutomationsService;

	/**
	 * Initializes the automation table
	 */
	public void init() {

		setName(NAME);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);
		setPreferredScrollableViewportSize(new Dimension(TABLEWIDTH,
				TABLEHEIGHT));
		setRowHeight(ROWHEIGHT);

		setShowGrid(true);
		setGridColor(Color.LIGHT_GRAY);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		columnNames.add(COLNAME_IMAGE);
		columnNames.add(COLNAME_TYPE);
		columnNames.add(COLNAME_KEYS);
		columnNames.add(COLNAME_TRIGGER);
		columnNames.add(COLNAME_FOCUS);
		columnNames.add(COLNAME_MIN_DELAY);
		columnNames.add(COLNAME_TIMEOUT);
		columnNames.add(COLNAME_MIDI_SIGNATURE);
		columnNames.add(COLNAME_SCAN_RATE);
		columnNames.add(COLNAME_MOVABLE);

		tableModel.setDataVector(data, columnNames);
		setModel(tableModel);

		createImageColumn();
		createTypeColumn();
		createKeysColumn();
		createTriggerColumn();
		createFocusColumn();
		createMinDelayColumn();
		createTimeoutColumn();
		createMidiMessageColumn();
		createScanRateColumn();
		createMovableColumn();

		// popup Menu
		addMouseListener(new PopupListener());
		midiLearnPopupMenu.init();
		midiLearnPopupMenu.setName(NAME_POPUP_MENU_MIDI_LEARN);
		keyLearnPopupMenu.init();
		keyLearnPopupMenu.setName(NAME_POPUP_MENU_KEY_LEARN);
		imagePopupMenu.init();
		imagePopupMenu.setName(NAME_POPUP_MENU_IMAGE);

		// key learn listener
		GUIUtils.addKeyListenerToComponent(keyLearnListener, this);

		grabFocus();
	}

	/**
	 * Creates the image column.
	 */
	private void createImageColumn() {
		getColumn(COLNAME_IMAGE).setMinWidth(COLWIDTH_IMAGE);
	}

	/**
	 * Creates the click type column with a combo box
	 */
	private void createTypeColumn() {

		getColumn(COLNAME_TYPE).setMinWidth(COLWIDTH_TYPE);

		// set values
		String[] clickTypes = { //
		GUIAutomation.CLICKTYPE_LEFT, //
				GUIAutomation.CLICKTYPE_RIGHT, //
				GUIAutomation.CLICKTYPE_DOUBLE, //
				GUIAutomation.TYPE_SENDKEY };

		TableCellRenderer clickTypeComboBoxRenderer = new JTableComboBoxRenderer(
				clickTypes);
		TableCellEditor clickTypeComboBoxEditor = new JTableComboBoxEditor(
				clickTypes);

		getColumn(COLNAME_TYPE).setCellRenderer(clickTypeComboBoxRenderer);
		getColumn(COLNAME_TYPE).setCellEditor(clickTypeComboBoxEditor);
	}

	/**
	 * Creates the keys column with a text box
	 */
	private void createKeysColumn() {

		getColumn(COLNAME_KEYS).setMinWidth(COLWIDTH_KEY);

		TableCellRenderer keysRenderer = new JTableKeyTextRenderer();
		getColumn(COLNAME_KEYS).setCellRenderer(keysRenderer);
	}

	/**
	 * Gets the keys of the selected row
	 * 
	 * @return The String of the keys
	 */
	public String getKeysOfSelectedRow() {
		int row = getSelectedRow();
		int col = getColumn(GUIAutomationConfigurationTable.COLNAME_KEYS)
				.getModelIndex();
		String keys = (String) getModel().getValueAt(row, col);
		return keys;
	}

	/**
	 * Sets the keys for the selected row
	 * 
	 * @param keys
	 *            The String of the keys
	 */
	public void setKeysOfSelectedRow(String keys) {
		int row = getSelectedRow();
		int col = getColumn(GUIAutomationConfigurationTable.COLNAME_KEYS)
				.getModelIndex();
		getModel().setValueAt(keys, row, col);
	}

	/**
	 * Creates the click trigger column with a combo box.
	 */
	private void createTriggerColumn() {

		getColumn(COLNAME_TRIGGER).setMinWidth(COLWIDTH_TRIGGER);

		// set values
		List<String> triggerTypes = new ArrayList<String>();
		triggerTypes.add(GUIAutomation.TRIGGER_ALWAYS);
		triggerTypes.add(GUIAutomation.TRIGGER_ONCE_PER_CHANGE);
		triggerTypes.add(GUIAutomation.TRIGGER_ONCE);

		// add midi IN devices as values
		for (String deviceName : MidiUtils.getMidiDeviceSignatures("IN")) {
			triggerTypes.add(GUIAutomation.TRIGGER_MIDI + deviceName);
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
	}

	/**
	 * Creates the focus program column with a combo box.
	 */
	private void createFocusColumn() {

		getColumn(COLNAME_FOCUS).setMinWidth(COLWIDTH_FOCUS);

		List<String> openPrograms = guiAutomationsService.getOpenPrograms();
		openPrograms.add(0, MidiAutomatorProperties.VALUE_NULL);

		JTableComboBoxRenderer focusComboBoxRenderer = new JTableComboBoxRenderer(
				openPrograms.toArray(new String[0]));
		JComboBox<String> focusComboBox = new JComboBox<String>(
				openPrograms.toArray(new String[0]));
		JTableComboBoxEditor focusComboBoxEditor = new JTableComboBoxEditor(
				focusComboBox);

		getColumn(COLNAME_FOCUS).setCellRenderer(focusComboBoxRenderer);
		getColumn(COLNAME_FOCUS).setCellEditor(focusComboBoxEditor);
	}

	/**
	 * Creates the minimum delay columns with a spinner.
	 */
	private void createMinDelayColumn() {

		createSpinnerColumn(COLNAME_MIN_DELAY, COLWIDTH_MIN_DELAY, //
				GUIAutomation.DEFAULT_MIN_DELAY,//
				GUIAutomation.MIN_DELAY_MIN_VALUE,//
				GUIAutomation.MIN_DELAY_MAX_VALUE,//
				MIN_DELAY_STEP_SIZE);
	}

	/**
	 * Creates the timeout column with a spinner.
	 */
	private void createTimeoutColumn() {

		createSpinnerColumn(COLNAME_TIMEOUT, COLWIDTH_TIMEOUT, //
				GUIAutomation.DEFAULT_TIMEOUT,//
				GUIAutomation.TIMEOUT_MIN_VALUE,//
				GUIAutomation.TIMEOUT_MAX_VALUE,//
				TIMEOUT_STEP_SIZE);
	}

	/**
	 * Creates the midi message column with a not editable cell.
	 */
	private void createMidiMessageColumn() {

		getColumn(COLNAME_MIDI_SIGNATURE).setMinWidth(COLWIDTH_MIDI_MESSAGE);
		JTableNonEditableRenderer nonEditableRenderer = new JTableNonEditableRenderer();
		getColumn(COLNAME_MIDI_SIGNATURE).setCellRenderer(nonEditableRenderer);
	}

	/**
	 * Creates the scan rate column with a spinner.
	 */
	private void createScanRateColumn() {

		createSpinnerColumn(COLNAME_SCAN_RATE, COLWIDTH_SCAN_RATE,//
				GUIAutomation.DEFAULT_SCAN_RATE,//
				GUIAutomation.MIN_SCAN_RATE,//
				GUIAutomation.MAX_SCAN_RATE,//
				SCAN_RATE_STEP_SIZE);
	}

	/**
	 * Creates the movable column with a check box.
	 */
	private void createMovableColumn() {

		getColumn(COLNAME_MOVABLE).setMinWidth(COLWIDTH_MOVABLE);
	}

	/**
	 * Creates a column with spinner.
	 * 
	 * @param colName
	 *            The column name
	 * @param colWidth
	 *            The column width
	 * @param defaultValue
	 *            The default value of the spinner
	 * @param minValue
	 *            The minimum value of the spinner
	 * @param maxValue
	 *            The maximum value of the spinner
	 * @param stepSize
	 *            The step size of the spinner
	 */
	private void createSpinnerColumn(String colName, int colWidth,
			Number defaultValue, Comparable<?> minValue,
			Comparable<?> maxValue, Number stepSize) {

		getColumn(colName).setMinWidth(colWidth);

		SpinnerModel spinnerModel = new SpinnerNumberModel(defaultValue,
				minValue, maxValue, stepSize);
		JTableSpinnerEditor spinnerEditor = new JTableSpinnerEditor(
				spinnerModel);
		getColumn(colName).setCellEditor(spinnerEditor);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		// Return the class of a column to choose the appropriate renderer
		Class<?> objClass = null;

		Object obj = getValueAt(0, column);
		if (obj != null) {
			objClass = obj.getClass();
		}
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
			model.setValueAt(icon, row, getColumn(COLNAME_IMAGE)
					.getModelIndex());
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
	 * @param focusedProgram
	 *            The focused program for the automation
	 * @param keyCodes
	 *            The keys send from the automation, <NULL> for default key
	 * @param minDelay
	 *            The minimum delay for the automation, <NULL> for default
	 *            trigger
	 * @param timeout
	 *            The timeout for the automation
	 * @param midiSignature
	 *            The midi signature, <NULL> for empty signature
	 * @param scanRate
	 *            The value for the scan rate
	 * @param isMovable
	 *            <TRUE> if image may occur on different places on the screen,
	 *            <FALSE> is not
	 */
	private void addAutomation(String imagePath, String type, int[] keyCodes,
			String trigger, String focusedProgram, long minDelay, long timeout,
			String midiSignature, float scanRate, boolean isMovable) {

		// extend table model
		Vector<Object> rowData = new Vector<Object>();

		// fill table data
		rowData.add(getColumn(COLNAME_IMAGE).getModelIndex(),
				initScreenshotIcon(imagePath));
		rowData.add(getColumn(COLNAME_TYPE).getModelIndex(), initType(type));
		rowData.add(getColumn(COLNAME_KEYS).getModelIndex(), initKeys(keyCodes));
		rowData.add(getColumn(COLNAME_TRIGGER).getModelIndex(),
				initTrigger(trigger));
		rowData.add(getColumn(COLNAME_FOCUS).getModelIndex(),
				initFocusedProgram(focusedProgram));
		rowData.add(getColumn(COLNAME_MIN_DELAY).getModelIndex(), minDelay);
		rowData.add(getColumn(COLNAME_TIMEOUT).getModelIndex(), timeout);
		rowData.add(getColumn(COLNAME_MIDI_SIGNATURE).getModelIndex(),
				initMidiMessage(midiSignature));
		rowData.add(getColumn(COLNAME_SCAN_RATE).getModelIndex(), scanRate);
		rowData.add(getColumn(COLNAME_MOVABLE).getModelIndex(), isMovable);

		setAutomationIndexToTriggerComboBox();

		tableModel.addRow(rowData);
	}

	/**
	 * Sets the automation index to the trigger combo box name.
	 */
	private void setAutomationIndexToTriggerComboBox() {

		JTableComboBoxEditor cellEditor = (JTableComboBoxEditor) getCellEditor(
				tableModel.getRowCount() - 1, getColumn(COLNAME_TRIGGER)
						.getModelIndex());
		@SuppressWarnings("unchecked")
		JComboBox<String> triggerEditorComboBox = (JComboBox<String>) cellEditor
				.getComponent();
		triggerEditorComboBox.setName(NAME_COMBOBOX_TRIGGER_EDITOR + "_"
				+ (tableModel.getRowCount() - 1));
	}

	/**
	 * Sets an automation row to the table. If the row does not exist, a new row
	 * will be added.
	 * 
	 * @param imagePath
	 *            The path to the click image, <NULL> for empty screenshot
	 * @param type
	 *            The type of automation, <NULL> for default
	 * @param keyCodes
	 *            The keys send from the automation, <NULL> for default
	 * @param trigger
	 *            The trigger for the automation, <NULL> for default
	 * @param focusedProgram
	 *            The program to focus for the automation, <NULL> for no focus
	 * @param minDelay
	 *            The minimum delay for the automation
	 * @param timeout
	 *            The Timeout for the automation
	 * @param midiSignature
	 *            The midi signature, <NULL> for empty signature
	 * @param scanRate
	 *            The scan rate of the automation
	 * @param isMovable
	 *            <TRUE> if image may occur on different places on the screen,
	 *            <FALSE> is not
	 * @param row
	 *            The row of the automation, if < 0 a new row will be added
	 */
	public void setAutomation(String imagePath, String type, int[] keyCodes,
			String trigger, String focusedProgram, long minDelay, long timeout,
			String midiSignature, float scanRate, boolean isMovable, int row) {

		TableModel model = getModel();

		if (model.getRowCount() <= row || row < 0) {
			addAutomation(imagePath, type, keyCodes, trigger, focusedProgram,
					minDelay, timeout, midiSignature, scanRate, isMovable);
		} else {

			model.setValueAt(initScreenshotIcon(imagePath), row,
					getColumn(COLNAME_IMAGE).getModelIndex());
			model.setValueAt(initType(type), row, getColumn(COLNAME_TYPE)
					.getModelIndex());
			model.setValueAt(initKeys(keyCodes), row, getColumn(COLNAME_KEYS)
					.getModelIndex());
			model.setValueAt(initTrigger(trigger), row,
					getColumn(COLNAME_TRIGGER).getModelIndex());
			model.setValueAt(initFocusedProgram(focusedProgram), row,
					getColumn(COLNAME_FOCUS).getModelIndex());
			model.setValueAt(minDelay, row, getColumn(COLNAME_MIN_DELAY)
					.getModelIndex());
			model.setValueAt(timeout, row, getColumn(COLNAME_TIMEOUT)
					.getModelIndex());
			model.setValueAt(initMidiMessage(midiSignature), row,
					getColumn(COLNAME_MIDI_SIGNATURE).getModelIndex());
			model.setValueAt(scanRate, row, getColumn(COLNAME_SCAN_RATE)
					.getModelIndex());
			model.setValueAt(isMovable, row, getColumn(COLNAME_MOVABLE)
					.getModelIndex());
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

	public MidiLearnPopupMenu getMidiLearnPopupMenu() {
		return midiLearnPopupMenu;
	}

	public KeyLearnPopupMenu getKeyLearnPopupMenu() {
		return keyLearnPopupMenu;
	}

	/**
	 * Sets the midi message for a GUI automation
	 * 
	 * @param signature
	 *            The midi message
	 * @param row
	 *            The row of the automation
	 * @throws AutomationIndexDoesNotExistException
	 */
	public void setMidiSignature(String signature, int row)
			throws AutomationIndexDoesNotExistException {

		DefaultTableModel model = (DefaultTableModel) getModel();

		try {
			// set midi signature
			model.setValueAt(signature, row, getColumn(COLNAME_MIDI_SIGNATURE)
					.getModelIndex());
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AutomationIndexDoesNotExistException();
		}
	}

	/**
	 * Adds a key code for a GUI automation
	 * 
	 * @param keyCode
	 *            The key code
	 * @param row
	 *            The row of the automation
	 * @throws AutomationIndexDoesNotExistException
	 */
	public void addKeyCode(int keyCode, int row)
			throws AutomationIndexDoesNotExistException {

		DefaultTableModel model = (DefaultTableModel) getModel();

		try {
			String keys = (String) model.getValueAt(row,
					getColumn(COLNAME_KEYS).getModelIndex());

			if (keys.equals("")) {
				keys = keys + keyCode;
			} else {
				keys = keys + KEYS_DELIMITER + keyCode;
			}

			model.setValueAt(keys, row, getColumn(COLNAME_KEYS).getModelIndex());
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AutomationIndexDoesNotExistException();
		}
	}

	/**
	 * Deletes the key for an automation
	 * 
	 * @param row
	 *            The row of the automation
	 * @throws AutomationIndexDoesNotExistException
	 */
	public void deleteKeys(int row) throws AutomationIndexDoesNotExistException {

		DefaultTableModel model = (DefaultTableModel) getModel();

		try {
			model.setValueAt(DEFAULT_KEYS, row, getColumn(COLNAME_KEYS)
					.getModelIndex());
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AutomationIndexDoesNotExistException();
		}
	}

	/**
	 * Initializes a scalable image icon for the screenshot.
	 * 
	 * @param path
	 *            The path to the image, if <NULL> an empty icon will be
	 *            returned
	 * @return A scalable image icon
	 */
	private ScaleableImageIcon initScreenshotIcon(String path) {

		if (path == null) {
			return new ScaleableImageIcon();
		} else {
			int width = COLWIDTH_IMAGE - (MARGIN_SCREENSHOT * 2);
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
			return GUIAutomation.DEFAULT_TRIGGER;
		} else {
			return trigger;
		}
	}

	/**
	 * Initializes a focused program value.
	 * 
	 * @param focusedProgram
	 *            The focused program value, if <NULL> the default value will be
	 *            returned.
	 * @return The program to focus
	 */
	private String initFocusedProgram(String focusedProgram) {

		if (focusedProgram == null) {
			return MidiAutomatorProperties.VALUE_NULL;
		} else {
			return focusedProgram;
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
			return GUIAutomation.CLICKTYPE_LEFT;
		} else {
			return type;
		}
	}

	/**
	 * Initializes the keys signature.
	 * 
	 * @param keyCodes
	 *            The key codes, if <NULL> the default value will be returned.
	 * @return The initialized trigger
	 */
	private String initKeys(int[] keyCodes) {

		String keys = "";

		if (keyCodes != null) {
			for (int i = 0; i < keyCodes.length; i++) {
				keys = keys + keyCodes[i];

				if (i != keyCodes.length - 1) {
					keys = keys
							+ GUIAutomationConfigurationTable.KEYS_DELIMITER;
				}
			}
		}

		return keys;
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
	 * Transforms an array of key codes to a representable String
	 * 
	 * @param keyCodes
	 *            The array of key codes
	 * @return The String representation
	 */
	public static String keyCodesToString(int[] keyCodes) {

		String keyCodesString = "";

		if (keyCodes == null) {
			return "";
		}

		for (int i = 0; i < keyCodes.length; i++) {
			keyCodesString = keyCodesString + KeyEvent.getKeyText(keyCodes[i]);

			if (i < keyCodes.length - 1) {
				keyCodesString = keyCodesString + KEYS_DELIMITER;
			}
		}

		return keyCodesString;
	}

	/**
	 * Shows the context menu.
	 * 
	 * @author aguelle
	 * 
	 */
	class PopupListener extends DeActivateableMouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				mouseReleased(e);
			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				if (isActive()) {
					maybeShowPopup(e);
				}
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

			JTable table = (JTable) e.getSource();
			int row = table.rowAtPoint(e.getPoint());
			int column = table.columnAtPoint(e.getPoint());

			if (!table.isRowSelected(row)) {
				table.changeSelection(row, column, false, false);
			}

			if (column == getColumn(COLNAME_MIDI_SIGNATURE).getModelIndex()) {
				openMidiLearnPopupMenu(table, e.getX(), e.getY());
			}

			if (column == getColumn(COLNAME_KEYS).getModelIndex()) {
				openKeyLearnPopupMenu(table, e.getX(), e.getY());
			}

			if (column == getColumn(COLNAME_IMAGE).getModelIndex()) {
				openImagePopupMenu(table, e.getX(), e.getY());
			}
		}

		/**
		 * Opens the key learn popup menu
		 * 
		 * @param table
		 *            The gui automation table
		 * @param x
		 *            The x coordinate of the menu
		 * @param y
		 *            The y coordinate of the menu
		 */
		private void openKeyLearnPopupMenu(JTable table, int x, int y) {

			int row = table.rowAtPoint(new Point(x, y));

			// de-/activate key learn item
			String automationType = (String) table.getValueAt(row,
					getColumn(COLNAME_TYPE).getModelIndex());

			keyLearnPopupMenu.getKeyLearnMenuItem().setEnabled(false);

			if (automationType.equals(GUIAutomation.TYPE_SENDKEY)) {
				keyLearnPopupMenu.getKeyLearnMenuItem().setEnabled(true);
			}

			// de-/activate key unlearn item
			String keySignature = (String) table.getModel().getValueAt(row,
					getColumn(COLNAME_KEYS).getModelIndex());

			keyLearnPopupMenu.getKeyUnlearnMenuItem().setEnabled(false);

			if (keyLearnService.isKeyLearning()) {
				keyLearnPopupMenu.getKeyUnlearnMenuItem().setEnabled(true);
			}

			if (keySignature != null) {
				if (!keySignature.equals("")) {
					keyLearnPopupMenu.getKeyUnlearnMenuItem().setEnabled(true);
				}
			}

			keyLearnPopupMenu.show(table, x, y);
		}

		/**
		 * Opens the image popup menu
		 * 
		 * @param table
		 *            The gui automation table
		 * @param x
		 *            The x coordinate of the menu
		 * @param y
		 *            The y coordinate of the menu
		 */
		private void openImagePopupMenu(JTable table, int x, int y) {

			int row = table.rowAtPoint(new Point(x, y));

			// de-/activate remove image item
			ScaleableImageIcon image = (ScaleableImageIcon) table.getValueAt(
					row, getColumn(COLNAME_IMAGE).getModelIndex());

			imagePopupMenu.getRemoveImageMenuItem().setEnabled(false);

			if (image != null) {
				if (image.getPath() != null) {
					if (!image.getPath().equals(
							MidiAutomatorProperties.VALUE_NULL))
						imagePopupMenu.getRemoveImageMenuItem()
								.setEnabled(true);
				}
			}

			imagePopupMenu.show(table, x, y);
		}

		/**
		 * Opens the Midi learn popup menu
		 * 
		 * @param table
		 *            The gui automation table
		 * @param x
		 *            The x coordinate of the menu
		 * @param y
		 *            The y coordinate of the menu
		 */
		private void openMidiLearnPopupMenu(JTable table, int x, int y) {

			int row = table.rowAtPoint(new Point(x, y));

			// de-/activate midi learn item
			String midiInAutomationDeviceName = (String) table.getValueAt(row,
					getColumn(COLNAME_TRIGGER).getModelIndex());

			midiLearnPopupMenu.getMidiLearnMenuItem().setEnabled(false);

			if (midiInAutomationDeviceName != null
					&& !midiInAutomationDeviceName
							.equals(MidiAutomatorProperties.VALUE_NULL)
					&& midiInAutomationDeviceName
							.contains(GUIAutomation.TRIGGER_MIDI)) {
				midiLearnPopupMenu.getMidiLearnMenuItem().setEnabled(true);
			}

			// de-/activate midi unlearn item
			String midiSignature = (String) table.getModel().getValueAt(row,
					getColumn(COLNAME_MIDI_SIGNATURE).getModelIndex());

			midiLearnPopupMenu.getMidiUnlearnMenuItem().setEnabled(false);

			if (midiSignature != null) {
				if (!midiSignature.equals("")) {
					midiLearnPopupMenu.getMidiUnlearnMenuItem()
							.setEnabled(true);
				}
			}

			midiLearnPopupMenu.show(table, x, y);
		}
	}

	/**
	 * 
	 * @author aguelle
	 * 
	 */
	class TriggerComboBoxListener implements ActionListener {

		private JComboBox<?> comboBox;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() instanceof JComboBox) {
				comboBox = (JComboBox<?>) e.getSource();
			}

			String functionKey = MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE
					+ "_" + getSelectedAutomationIndex();
			String trigger = (String) comboBox.getSelectedItem();

			if (trigger.contains(GUIAutomation.TRIGGER_MIDI)) {
				midiDevicesService.loadMidiDeviceByFunctionKey(functionKey,
						trigger.replace(GUIAutomation.TRIGGER_MIDI, ""));
			} else {
				midiDevicesService.loadMidiDeviceByFunctionKey(functionKey,
						MidiAutomatorProperties.VALUE_NULL);
			}
		}

		/**
		 * Gets the automation index from the combo box name.
		 * 
		 * @return The automation index, -1 if there is no focus
		 */
		private int getSelectedAutomationIndex() {

			if (comboBox.getName() != null) {
				String automationIndex = comboBox.getName().replace(
						NAME_COMBOBOX_TRIGGER_EDITOR + "_", "");
				return Integer.parseInt(automationIndex);
			}

			return -1;
		}
	}
}
