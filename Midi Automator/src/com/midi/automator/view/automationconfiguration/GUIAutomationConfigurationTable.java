package com.midi.automator.view.automationconfiguration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

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

import com.midi.automator.guiautomator.GUIAutomation;
import com.midi.automator.view.CacheableJTable;
import com.midi.automator.view.DeActivateableMouseAdapter;
import com.midi.automator.view.MidiLearnPopupMenu;
import com.midi.automator.view.ScaleableImageIcon;

import de.tieffrequent.midi.automator.IApplication;

/**
 * A JTable for configuring the GUI automation
 * 
 * @author aguelle
 * 
 */
public class GUIAutomationConfigurationTable extends CacheableJTable {

	private static final long serialVersionUID = 1L;

	private final int TABLEWIDTH = 900;
	private final int TABLEHEIGHT = 140;
	private final int ROWHEIGHT = 40;
	private final int COLWIDTH_SCREENSHOT = 100;
	private final int COLWIDTH_MIN_DELAY = 40;
	private final int COLWIDTH_MIDI_MESSAGE = 210;
	private final int COLWIDTH_SEARCH = 64;
	private final int MARGIN_SCREENSHOT = 4;

	private final String LABEL_SEARCHBUTTON = "Screenshot...";

	private final String COLNAME_SCREENSHOT = "Screenshot";
	private final String COLNAME_TYPE = "Type";
	private final String COLNAME_TRIGGER = "Trigger";
	private final String COLNAME_MIN_DELAY = "Delay in (ms)";
	private final String COLNAME_MIDI = "Midi Message";
	private final String COLNAME_SEARCH_BUTTON = "";
	private Vector<String> columnNames;

	private final String DEFAULT_TYPE = GUIAutomation.CLICKTYPE_LEFT;
	private final String DEFAULT_TRIGGER = GUIAutomation.CLICKTRIGGER_ALWAYS;
	private final String DEFAULT_MIDI_MESSAGE = "";
	private final Long DEFAULT_MINDELAY = GUIAutomation.MINDELAY_MIN_VALUE;
	private final Long MINDELAY_STEP_SIZE = 1L;

	private final String NAME_MENU_ITEM_MIDI_LEARN = "GUIAutomationConfigurationTable midi learn";

	private Vector<Vector<Object>> data;

	private MidiLearnPopupMenu popupMenu;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The application
	 */
	public GUIAutomationConfigurationTable(IApplication application) {

		data = new Vector<Vector<Object>>();

		columnNames = new Vector<String>();
		columnNames.add(COLNAME_SCREENSHOT);
		columnNames.add(COLNAME_TYPE);
		columnNames.add(COLNAME_TRIGGER);
		columnNames.add(COLNAME_MIN_DELAY);
		columnNames.add(COLNAME_MIDI);
		columnNames.add(COLNAME_SEARCH_BUTTON);

		ConfigurationTableModel model = new ConfigurationTableModel();
		model.setDataVector(data, columnNames);
		setModel(model);

		setPreferredScrollableViewportSize(new Dimension(TABLEWIDTH,
				TABLEHEIGHT));

		setRowHeight(ROWHEIGHT);

		setShowGrid(true);
		setGridColor(Color.LIGHT_GRAY);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// screenshot
		getColumn(COLNAME_SCREENSHOT).setPreferredWidth(COLWIDTH_SCREENSHOT);

		// min delay field
		getColumn(COLNAME_MIN_DELAY).setPreferredWidth(COLWIDTH_MIN_DELAY);
		SpinnerModel minDelaySpinnerModel = new SpinnerNumberModel(
				DEFAULT_MINDELAY, GUIAutomation.MINDELAY_MIN_VALUE,
				GUIAutomation.MINDELAY_MAX_VALUE, MINDELAY_STEP_SIZE);
		JTableSpinnerEditor minDelayEditor = new JTableSpinnerEditor(
				minDelaySpinnerModel);
		getColumn(COLNAME_MIN_DELAY).setCellEditor(minDelayEditor);

		// midi message
		getColumn(COLNAME_MIDI).setPreferredWidth(COLWIDTH_MIDI_MESSAGE);
		JTableNonEditableRenderer nonEditableRenderer = new JTableNonEditableRenderer();
		getColumn(COLNAME_MIDI).setCellRenderer(nonEditableRenderer);

		// browse button
		getColumn(COLNAME_SEARCH_BUTTON).setPreferredWidth(COLWIDTH_SEARCH);
		JTableButtonRenderer buttonRenderer = new JTableButtonRenderer();
		JTableButtonEditor buttonEditor = new JTableButtonEditor();
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
	 */
	private void addAutomation(String imagePath, String type, String trigger,
			long minDelay, String midiSignature) {

		// generate click type ComboBox
		String[] clickTypes = { GUIAutomation.CLICKTYPE_LEFT,
				GUIAutomation.CLICKTYPE_RIGHT, GUIAutomation.CLICKTYPE_DOUBLE };
		TableCellRenderer clickTypeComboBoxRenderer = new JTableComboBoxRenderer(
				clickTypes);
		TableCellEditor clickTypeComboBoxEditor = new JTableComboBoxEditor(
				clickTypes);

		getColumn(COLNAME_TYPE).setCellRenderer(clickTypeComboBoxRenderer);
		getColumn(COLNAME_TYPE).setCellEditor(clickTypeComboBoxEditor);

		// generate click trigger ComboBox
		String[] triggerTypes = { GUIAutomation.CLICKTRIGGER_ALWAYS,
				GUIAutomation.CLICKTRIGGER_ONCE_PER_CHANGE,
				GUIAutomation.CLICKTRIGGER_ONCE,
				GUIAutomation.CLICKTRIGGER_MIDI };
		JTableComboBoxRenderer frequencyComboBoxRenderer = new JTableComboBoxRenderer(
				triggerTypes);
		JTableComboBoxEditor frequencyComboBoxEditor = new JTableComboBoxEditor(
				triggerTypes);
		getColumn(COLNAME_TRIGGER).setCellRenderer(frequencyComboBoxRenderer);
		getColumn(COLNAME_TRIGGER).setCellEditor(frequencyComboBoxEditor);

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
		rowData.add(ConfigurationTableModel.COLUMN_INDEX_MIDISIGNATURE,
				initMidiMessage(midiSignature));

		// search button
		rowData.add(ConfigurationTableModel.COLUMN_INDEX_BROWSE_BUTTON,
				LABEL_SEARCHBUTTON);

		model.addRow(rowData);
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
	 * @param index
	 *            The index of the automation, if < 0 a new row will be added
	 */
	public void setAutomation(String imagePath, String type, String trigger,
			long minDelay, String midiSignature, int index) {

		TableModel model = getModel();

		if (model.getRowCount() <= index || index < 0) {
			addAutomation(imagePath, type, trigger, minDelay, midiSignature);
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
					ConfigurationTableModel.COLUMN_INDEX_MIDISIGNATURE);
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
	 * @param message
	 *            The midi message
	 * @param row
	 *            The row of the autoamtion
	 * @throws AutomationIndexDoesNotExistException
	 */
	public void setMidiMessage(String message, int row)
			throws AutomationIndexDoesNotExistException {

		DefaultTableModel model = (DefaultTableModel) getModel();

		try {
			// set midi signature
			model.setValueAt(message, row,
					ConfigurationTableModel.COLUMN_INDEX_MIDISIGNATURE);
			// set trigger to midi
			model.setValueAt(GUIAutomation.CLICKTRIGGER_MIDI, row,
					ConfigurationTableModel.COLUMN_INDEX_TRIGGER);
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
				} catch (AutomationIndexDoesNotExistException e1) {
					e1.printStackTrace();
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

				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}
