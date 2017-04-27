package com.midi_automator.view.windows.PreferencesDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.GUIAutomationsService;
import com.midi_automator.presenter.services.MidiItemChangeNotificationService;
import com.midi_automator.presenter.services.MidiLearnService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.presenter.services.PresenterService;
import com.midi_automator.utils.GUIUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.windows.AbstractBasicDialog;
import com.midi_automator.view.windows.MainFrame.HTMLLabel;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.AutomationIndexDoesNotExistException;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationPanel;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;
import com.midi_automator.view.windows.PreferencesDialog.actions.SendNotificationAction;
import com.midi_automator.view.windows.PreferencesDialog.listener.PreferencesDialogCloseListener;

@org.springframework.stereotype.Component
public class PreferencesDialog extends AbstractBasicDialog {

	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(PreferencesDialog.class.getName());

	private final String TITLE = "Preferences";
	private final String TITLE_PROGRAM_SETTINGS = "Program Settings";
	private final String LABEL_MIDI_IN_REMOTE_DEVICES = "Midi Remote IN:";
	private final String LABEL_MIDI_OUT_REMOTE_DEVICES = "Midi Master OUT:";
	private final String LABEL_MIDI_OUT_REMOTE_INFO = "Master open: ch 1 CC 102 &lt;list entry - 1&gt;";
	private final String LABEL_MIDI_OUT_SWITCH_NOTIFIER_DEVICES = "Midi Switch Notifier OUT:";
	private final String LABEL_MIDI_OUT_SWITCH_NOTIFIER_INFO = "Notifier: ch 1 CC 103 value 127";
	private final String LABEL_MIDI_OUT_SWITCH_ITEM_DEVICES = "Midi Switch List Entry OUT:";
	private final String LABEL_MIDI_OUT_SWITCH_ITEM_INFO = "Switch item: ch 16 CC [1 - 127] value 127";
	private final String LABEL_MIDI_IN_METRONOM_DEVICES = "Midi Metronom IN:";
	private final String LABEL_MINIMIZE_ON_CLOSE = "Minimize program on close";
	private final String LABEL_MIDI_IN_METRONOM_INFO = "1st click: ch 16 NOTE ON A4<br/>"
			+ "Other clicks: ch 16 NOTE ON E4";
	private final String LABEL_GUI_AUTOMATION = "Automations";
	private final String BUTTON_SEND_NOTIFIER = "Send...";

	public static final String NAME = "preferences dialog";
	public static final String NAME_MIDI_IN_REMOTE_DEVICE_COMBO_BOX = "midiINRemoteDeviceComboBox";
	public static final String NAME_MIDI_OUT_REMOTE_DEVICE_COMBO_BOX = "midiOUTRemoteDeviceComboBox";
	public static final String NAME_MIDI_OUT_SWITCH_NOTIFIER_DEVICE_COMBO_BOX = "midiOUTSwitchNotifierDeviceComboBox";
	public static final String NAME_MIDI_OUT_SWITCH_ITEM_DEVICE_COMBO_BOX = "midiOUTSwitchItemDeviceComboBox";
	public static final String NAME_MIDI_IN_METRONROM_DEVICE_COMBO_BOX = "midiINMetronomDeviceComboBox";
	public static final String NAME_BUTTON_SEND_NOTIFIER = "buttonSendNotifier";
	public static final String NAME_CHECKBOX_MINIMIZE_ON_CLOSE = "checkcoxMinimizeOnClose";

	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel footerPanel;
	private JLabel midiINRemoteDevicesLabel;
	private JLabel midiOUTRemoteDevicesLabel;
	private JLabel midiOUTSwitchNotifierDevicesLabel;
	private JLabel midiOUTSwitchItemDevicesLabel;
	private JLabel labelHeader;
	private HTMLLabel midiOUTSwitchNotifierInfoLabel;
	private HTMLLabel midiOUTSwitchItemInfoLabel;
	private HTMLLabel midiINMetronomInfoLabel;
	private JComboBox<String> midiINRemoteDeviceComboBox;
	private JComboBox<String> midiOUTRemoteDeviceComboBox;
	private JComboBox<String> midiOUTSwitchNotifierDeviceComboBox;
	private JComboBox<String> midiINMetronomDeviceComboBox;
	private JComboBox<String> midiOUTSwitchItemDeviceComboBox;
	private JCheckBox minimizeOnCloseCheckBox;

	@Autowired
	private GUIAutomationPanel guiAutomationConfigurationPanel;

	private JButton buttonSendNotify;

	private final Insets INSETS_LABEL_HEADER = new Insets(0, 5, 0, 10);
	private final Insets INSETS_COMBO_BOX = new Insets(0, 0, 0, 10);
	private final Insets INSETS_LABEL_INFO = new Insets(0, 5, 0, 10);

	private int currentMiddleGridY = 0;
	private int currentTopGridY = 0;

	@Autowired
	private Presenter presenter;

	@Autowired
	private PresenterService presenterService;
	@Autowired
	private MidiLearnService midiLearnService;
	@Autowired
	private GUIAutomationsService guiAutomationsService;
	@Autowired
	private MidiService midiService;
	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;

	@Autowired
	private PreferencesDialogCloseListener windowCloseListener;

	@Autowired
	private SendNotificationAction sendNotificationAction;

	/**
	 * Initializes the frame
	 */
	public void init() {
		super.init();

		setTitle(TITLE);
		setName(NAME);

		// set layout
		topPanel = new JPanel(new GridBagLayout());
		middlePanel = new JPanel(new GridBagLayout());
		footerPanel = new JPanel(new FlowLayout());

		TitledBorder titleBorder = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				TITLE_PROGRAM_SETTINGS);
		Border topPadding = BorderFactory.createEmptyBorder(5, 0, 10, 0);
		CompoundBorder topBorder = BorderFactory.createCompoundBorder(
				titleBorder, topPadding);
		topPanel.setBorder(topBorder);

		createMinimizeOnCloseCheckbox();

		createRemoteMidiInDevices();
		createRemoteMidiOutDevices();
		createSwitchNotifierMidiOutDevices();
		currentMiddleGridY += 3;

		createSwitchItemOutDevices();
		createMetronomMidiInDevices();
		currentMiddleGridY += 3;

		// GUI Automation
		createGUIAutomation();
		guiAutomationsService.setGUIAutomatorsToActive(false);

		// Save
		footerPanel.add(buttonSave);

		// Cancel
		footerPanel.add(buttonCancel);

		add(topPanel, BorderLayout.PAGE_START);
		add(middlePanel, BorderLayout.CENTER);
		add(footerPanel, BorderLayout.PAGE_END);

		// load data
		reload();

		// action on close
		addWindowListener(windowCloseListener);

	}

	/**
	 * Creates the minimize on close check box
	 */
	private void createMinimizeOnCloseCheckbox() {

		minimizeOnCloseCheckBox = new JCheckBox(LABEL_MINIMIZE_ON_CLOSE);
		minimizeOnCloseCheckBox.setName(NAME_CHECKBOX_MINIMIZE_ON_CLOSE);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = currentTopGridY;
		topPanel.add(minimizeOnCloseCheckBox, c);
	}

	/**
	 * Creates the midi in devices combo box for the remote switch with a label
	 */
	private void createRemoteMidiInDevices() {

		GridBagConstraints c = new GridBagConstraints();
		midiINRemoteDevicesLabel = new JLabel(LABEL_MIDI_IN_REMOTE_DEVICES);
		c.insets = INSETS_LABEL_HEADER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = currentMiddleGridY;
		middlePanel.add(midiINRemoteDevicesLabel, c);

		List<String> midiINDevices = MidiUtils.getMidiDeviceSignatures("IN");
		midiINDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiINRemoteDeviceComboBox = new JComboBox<String>(
				(String[]) midiINDevices.toArray(new String[0]));
		midiINRemoteDeviceComboBox
				.setName(NAME_MIDI_IN_REMOTE_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = INSETS_COMBO_BOX;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = currentMiddleGridY + 1;
		middlePanel.add(midiINRemoteDeviceComboBox, c);

	}

	/**
	 * Creates the midi out devices combo box for the remote switch with a label
	 */
	private void createRemoteMidiOutDevices() {

		GridBagConstraints c = new GridBagConstraints();
		midiOUTRemoteDevicesLabel = new JLabel(LABEL_MIDI_OUT_REMOTE_DEVICES);
		c.insets = INSETS_LABEL_HEADER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = currentMiddleGridY;
		middlePanel.add(midiOUTRemoteDevicesLabel, c);

		List<String> midiOutDevices = MidiUtils.getMidiDeviceSignatures("OUT");
		midiOutDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiOUTRemoteDeviceComboBox = new JComboBox<String>(
				(String[]) midiOutDevices.toArray(new String[0]));
		midiOUTRemoteDeviceComboBox
				.setName(NAME_MIDI_OUT_REMOTE_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = INSETS_COMBO_BOX;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		c.gridy = currentMiddleGridY + 1;
		middlePanel.add(midiOUTRemoteDeviceComboBox, c);

		// Info label
		c = new GridBagConstraints();
		c.insets = INSETS_LABEL_INFO;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = currentMiddleGridY + 2;
		midiOUTSwitchNotifierInfoLabel = new HTMLLabel(
				"<span style='font-family:Arial; font-size:8px'>"
						+ LABEL_MIDI_OUT_REMOTE_INFO + "</span>");
		middlePanel.add(midiOUTSwitchNotifierInfoLabel, c);
	}

	/**
	 * Creates the midi out devices combo box for the switch notifier with a
	 * label
	 */
	private void createSwitchNotifierMidiOutDevices() {

		GridBagConstraints c = new GridBagConstraints();
		midiOUTSwitchNotifierDevicesLabel = new JLabel(
				LABEL_MIDI_OUT_SWITCH_NOTIFIER_DEVICES);
		c.insets = INSETS_LABEL_HEADER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = currentMiddleGridY;
		middlePanel.add(midiOUTSwitchNotifierDevicesLabel, c);

		List<String> midiOutDevices = MidiUtils.getMidiDeviceSignatures("OUT");
		midiOutDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiOUTSwitchNotifierDeviceComboBox = new JComboBox<String>(
				(String[]) midiOutDevices.toArray(new String[0]));
		midiOUTSwitchNotifierDeviceComboBox
				.setName(NAME_MIDI_OUT_SWITCH_NOTIFIER_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = currentMiddleGridY + 1;
		middlePanel.add(midiOUTSwitchNotifierDeviceComboBox, c);

		// Button test notification
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 3;
		c.gridy = currentMiddleGridY + 1;
		buttonSendNotify = new JButton(BUTTON_SEND_NOTIFIER);
		buttonSendNotify.setName(NAME_BUTTON_SEND_NOTIFIER);
		buttonSendNotify.addActionListener(sendNotificationAction);
		buttonSendNotify.setMaximumSize(new Dimension(7, 10));
		middlePanel.add(buttonSendNotify, c);

		// Info label
		c = new GridBagConstraints();
		c.insets = INSETS_LABEL_INFO;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = currentMiddleGridY + 2;
		midiOUTSwitchNotifierInfoLabel = new HTMLLabel(
				"<span style='font-family:Arial; font-size:8px'>"
						+ LABEL_MIDI_OUT_SWITCH_NOTIFIER_INFO + "</span>");
		middlePanel.add(midiOUTSwitchNotifierInfoLabel, c);
	}

	/**
	 * Creates the midi out devices combo box for item switch with a label.
	 */
	private void createSwitchItemOutDevices() {

		GridBagConstraints c = new GridBagConstraints();
		midiOUTSwitchItemDevicesLabel = new JLabel(
				LABEL_MIDI_OUT_SWITCH_ITEM_DEVICES);
		c.insets = INSETS_LABEL_HEADER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = currentMiddleGridY;
		middlePanel.add(midiOUTSwitchItemDevicesLabel, c);

		List<String> devices = MidiUtils.getMidiDeviceSignatures("OUT");
		devices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiOUTSwitchItemDeviceComboBox = new JComboBox<String>(
				(String[]) devices.toArray(new String[0]));
		midiOUTSwitchItemDeviceComboBox
				.setName(NAME_MIDI_OUT_SWITCH_ITEM_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = INSETS_COMBO_BOX;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = currentMiddleGridY + 1;
		middlePanel.add(midiOUTSwitchItemDeviceComboBox, c);

		// Info label
		c = new GridBagConstraints();
		c.insets = INSETS_LABEL_INFO;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = currentMiddleGridY + 2;
		midiOUTSwitchItemInfoLabel = new HTMLLabel(
				"<span style='font-family:Arial; font-size:8px'>"
						+ this.LABEL_MIDI_OUT_SWITCH_ITEM_INFO + "</span>");
		middlePanel.add(midiOUTSwitchItemInfoLabel, c);
	}

	/**
	 * Creates the midi in devices combo box for metronom with a label.
	 */
	private void createMetronomMidiInDevices() {

		GridBagConstraints c = new GridBagConstraints();
		labelHeader = new JLabel(LABEL_MIDI_IN_METRONOM_DEVICES);
		c.insets = INSETS_LABEL_HEADER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = currentMiddleGridY;
		middlePanel.add(labelHeader, c);

		List<String> midiInDevices = MidiUtils.getMidiDeviceSignatures("IN");
		midiInDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiINMetronomDeviceComboBox = new JComboBox<String>(
				(String[]) midiInDevices.toArray(new String[0]));
		midiINMetronomDeviceComboBox
				.setName(NAME_MIDI_IN_METRONROM_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = INSETS_COMBO_BOX;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		c.gridy = currentMiddleGridY + 1;
		middlePanel.add(midiINMetronomDeviceComboBox, c);

		// Info label
		c = new GridBagConstraints();
		c.insets = INSETS_LABEL_INFO;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = currentMiddleGridY + 2;
		midiINMetronomInfoLabel = new HTMLLabel(
				"<span style='font-family:Arial; font-size:8px'>"
						+ LABEL_MIDI_IN_METRONOM_INFO + "</span>");
		middlePanel.add(midiINMetronomInfoLabel, c);
	}

	/**
	 * Creates the GUI automation configuration panel
	 */
	private void createGUIAutomation() {

		GridBagConstraints c = new GridBagConstraints();

		guiAutomationConfigurationPanel.init();
		guiAutomationConfigurationPanel.setOpaque(true);
		Border loweredetched = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		guiAutomationConfigurationPanel.setBorder(BorderFactory
				.createTitledBorder(loweredetched, LABEL_GUI_AUTOMATION));
		c.gridx = 0;
		c.gridy = currentMiddleGridY;
		c.gridwidth = 4;
		middlePanel.add(guiAutomationConfigurationPanel, c);

	}

	/**
	 * Sets the learned midi signature for the selected GUI automation
	 * 
	 * @param signature
	 *            The signature
	 */
	public void setAutomationMidiSignature(String signature) {

		// learning for automation list
		GUIAutomationTable table = guiAutomationConfigurationPanel
				.getGUIAutomationsTable();
		try {
			table.setMidiSignature(signature, table.getSelectedRow());
		} catch (AutomationIndexDoesNotExistException e) {
			log.error("The automation for the MIDI signature does not exist", e);
		}
	}

	/**
	 * Adds a learned key code for the selected GUI automation
	 * 
	 * @param keyCode
	 *            The key code
	 */
	public void addKeyCodeToSelectedAutomation(int keyCode) {

		GUIAutomationTable table = guiAutomationConfigurationPanel
				.getGUIAutomationsTable();
		try {
			table.addKeyCode(keyCode, table.getSelectedRow());
		} catch (AutomationIndexDoesNotExistException e) {
			log.error("The automation for the key does not exist", e);
		}
	}

	/**
	 * Deletes a learned key code for the selected GUI automation
	 * 
	 * @param keyCode
	 *            The key code
	 */
	public void deleteKeyCodeFromSelectedAutomation() {

		GUIAutomationTable table = guiAutomationConfigurationPanel
				.getGUIAutomationsTable();
		try {
			table.deleteKeys(table.getSelectedRow());
		} catch (AutomationIndexDoesNotExistException e) {
			log.error("The automation of the key does not exist", e);
		}
	}

	/**
	 * Reloads the data of the frame
	 */
	public void reload() {

		// midi in remote opener
		String midiInRemoteDeviceName = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);

		if (midiInRemoteDeviceName != null) {
			midiINRemoteDeviceComboBox.setSelectedItem(midiInRemoteDeviceName);
		}

		// midi out remote opener
		String midiOUTRemoteDevice = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE);

		if (midiOUTRemoteDevice != null) {
			midiOUTRemoteDeviceComboBox.setSelectedItem(midiOUTRemoteDevice);
		}

		// midi in metronom
		String midiINMetronomDevice = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE);

		if (midiINMetronomDevice != null) {
			midiINMetronomDeviceComboBox.setSelectedItem(midiINMetronomDevice);
		}

		// midi out switch notifier
		String midiOUTSwitchNotifierDevice = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE);

		if (midiOUTSwitchNotifierDevice != null) {
			midiOUTSwitchNotifierDeviceComboBox
					.setSelectedItem(midiOUTSwitchNotifierDevice);
		}

		// midi out item switch
		String midiOUTSwitchItemDevice = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE);

		if (midiOUTSwitchItemDevice != null) {
			midiOUTSwitchItemDeviceComboBox
					.setSelectedItem(midiOUTSwitchItemDevice);
		}

		// minimize on close
		boolean minimizeOnClose = presenterService.isMinimizeOnClose();
		minimizeOnCloseCheckBox.setSelected(minimizeOnClose);

		// gui automations
		GUIAutomation[] guiAutomations = guiAutomationsService
				.getGuiAutomations();
		GUIAutomationTable table = guiAutomationConfigurationPanel
				.getGUIAutomationsTable();

		guiAutomationConfigurationPanel.setMinSimilarity(guiAutomationsService
				.getMinSimilarity());

		if (guiAutomations != null) {
			for (int i = 0; i < guiAutomations.length; i++) {
				table.setAutomation(guiAutomations[i].getImagePath(),
						guiAutomations[i].getType(),
						guiAutomations[i].getKeyCodes(),
						guiAutomations[i].getTrigger(),
						guiAutomations[i].getFocusedProgram(),
						guiAutomations[i].getMinDelay(),
						guiAutomations[i].getTimeout(),
						guiAutomations[i].getMidiSignature(),
						guiAutomations[i].getScanRate(),
						guiAutomations[i].isMovable(), i);
			}
		}
		validate();
	}

	/**
	 * Puts the frame to learn mode
	 */
	private void learnOn() {
		// disable inputs
		GUIUtils.disEnableAllInputs(this, false, GUIAutomationTable.NAME);
	}

	/**
	 * Puts the frame to learn mode for the component
	 * 
	 * @param learningComponent
	 *            The component to learn for
	 */
	private void learnOn(Component learningComponent) {

		learnOn();

		GUIAutomationTable table = guiAutomationConfigurationPanel
				.getGUIAutomationsTable();

		if (learningComponent.getName() != null) {
			if (learningComponent.getName().equals(GUIAutomationTable.NAME)) {

				GUIUtils.deHighlightTableRow(table, true);

				log.info("Learning for automation index: "
						+ table.getSelectedRow());

			}
		}
	}

	/**
	 * Puts the frame to midi learn mode
	 */
	public void midiLearnOn() {

		learnOn();
		guiAutomationConfigurationPanel.getMidiLearnPopupMenu().midiLearnOn();

	}

	/**
	 * Puts the frame to midi learn mode for the given component key
	 * 
	 * @param key
	 *            The key of the component to learn for
	 */
	public void midiLearnOn(String key) {

		learnOn(getLearningComponentFromKey(key));
		guiAutomationConfigurationPanel.getMidiLearnPopupMenu().midiLearnOn();

	}

	/**
	 * Gets the learning component for the key
	 * 
	 * @param key
	 *            The learning component key
	 * @return The learning component
	 */
	private Component getLearningComponentFromKey(String key) {

		switch (midiLearnService.getKeyFromIndexedMidiLearnKey(key)) {
		case MidiLearnService.KEY_MIDI_LEARN_AUTOMATION_TRIGGER:
			return guiAutomationConfigurationPanel.getGUIAutomationsTable();
		}

		return null;
	}

	/**
	 * Puts the frame to key learn mode
	 * 
	 * @param learningComponent
	 *            The component to learn for
	 */
	public void keyLearnOn(JComponent learningComponent) {

		learnOn(learningComponent);
		guiAutomationConfigurationPanel.getKeyLearnPopupMenu().keyLearnOn();

	}

	/**
	 * Puts the frame to normal mode.
	 */
	private void learnOff() {

		// enable inputs
		GUIUtils.disEnableAllInputs(this, true);

		GUIUtils.deHighlightTableRow(
				guiAutomationConfigurationPanel.getGUIAutomationsTable(), false);
	}

	/**
	 * Puts the frame to normal mode.
	 */
	public void midiLearnOff() {

		// change menu item
		guiAutomationConfigurationPanel.getMidiLearnPopupMenu().midiLearnOff();

		learnOff();
	}

	/**
	 * Puts the frame to normal mode.
	 */
	public void keyLearnOff() {

		// change menu item
		guiAutomationConfigurationPanel.getKeyLearnPopupMenu().keyLearnOff();

		learnOff();
	}

	public GUIAutomationPanel getGuiAutomationPanel() {
		return guiAutomationConfigurationPanel;
	}

	/**
	 * Gets the name of the switch notifier MIDI device.
	 * 
	 * @return
	 */
	public String getSwitchNotifierMidiDeviceName() {
		return (String) midiOUTSwitchNotifierDeviceComboBox.getSelectedItem();
	}

	/**
	 * Closes the preferences window, saves and loads the configuration.
	 */
	protected void save() {

		String midiINRemoteDeviceName = (String) midiINRemoteDeviceComboBox
				.getSelectedItem();
		String midiOUTRemoteDeviceName = (String) midiOUTRemoteDeviceComboBox
				.getSelectedItem();
		String midiINMetronomDeviceName = (String) midiINMetronomDeviceComboBox
				.getSelectedItem();
		String midiOUTSwitchNotifierDeviceName = (String) midiOUTSwitchNotifierDeviceComboBox
				.getSelectedItem();
		String midiOUTSwitchItemDeviceName = (String) midiOUTSwitchItemDeviceComboBox
				.getSelectedItem();
		GUIAutomation[] guiAutomations = guiAutomationConfigurationPanel
				.getGUIAutomations();

		midiService.setMidiDeviceName(midiINRemoteDeviceName,
				MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);
		midiService.setMidiDeviceName(midiINMetronomDeviceName,
				MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE);
		midiService.setMidiDeviceName(midiOUTRemoteDeviceName,
				MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE);
		midiService.setMidiDeviceName(midiOUTSwitchNotifierDeviceName,
				MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE);
		midiService.setMidiDeviceName(midiOUTSwitchItemDeviceName,
				MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE);
		guiAutomationsService.saveGUIAutomations(guiAutomations,
				guiAutomationConfigurationPanel.getMinSimilarity());
		presenterService.setMinimizeOnClose(minimizeOnCloseCheckBox
				.isSelected());

		close();
		presenter.loadProperties();
	}
}
