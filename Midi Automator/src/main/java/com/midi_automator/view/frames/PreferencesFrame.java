package com.midi_automator.view.frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.GUIAutomationsService;
import com.midi_automator.presenter.services.MidiItemChangeNotificationService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.utils.GUIUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.CacheableJTable;
import com.midi_automator.view.HTMLLabel;
import com.midi_automator.view.automationconfiguration.AutomationIndexDoesNotExistException;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationPanel;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;

@org.springframework.stereotype.Component
@Scope("prototype")
public class PreferencesFrame extends AbstractBasicDialog {

	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(PreferencesFrame.class.getName());

	private final int PADDING_HORIZONTAL = 20;
	private final int PADDING_VERTICAL = 10;

	private final String TITLE = "Preferences";
	private final String LABEL_MIDI_IN_REMOTE_DEVICES = "Midi Remote IN:";
	private final String LABEL_MIDI_OUT_REMOTE_DEVICES = "Midi Master OUT:";
	private final String LABEL_MIDI_OUT_REMOTE_INFO = "Master open: ch 1 CC 102 &lt;list entry - 1&gt;";
	private final String LABEL_MIDI_OUT_SWITCH_NOTIFIER_DEVICES = "Midi Switch Notifier OUT:";
	private final String LABEL_MIDI_OUT_SWITCH_NOTIFIER_INFO = "Notifier: ch 1 CC 103 value 127";
	private final String LABEL_MIDI_OUT_SWITCH_ITEM_DEVICES = "Midi Switch List Entry OUT:";
	private final String LABEL_MIDI_OUT_SWITCH_ITEM_INFO = "Switch item: ch 16 CC [1 - 127] value 127";
	private final String LABEL_MIDI_IN_METRONOM_DEVICES = "Midi Metronom IN:";
	private final String LABEL_MIDI_IN_METRONOM_INFO = "1st click: ch 16 NOTE ON A4<br/>"
			+ "Other clicks: ch 16 NOTE ON E4";
	private final String LABEL_GUI_AUTOMATION = "Mouse Automation:";
	private final String BUTTON_SEND_NOTIFIER = "Send...";

	public static final String NAME = "preferences frame";
	public static final String NAME_MIDI_IN_REMOTE_DEVICE_COMBO_BOX = "midiINRemoteDeviceComboBox";
	public static final String NAME_MIDI_OUT_REMOTE_DEVICE_COMBO_BOX = " midiOUTRemoteDeviceComboBox";
	public static final String NAME_MIDI_OUT_SWITCH_NOTIFIER_DEVICE_COMBO_BOX = "midiOUTSwitchNotifierDeviceComboBox";
	public static final String NAME_MIDI_OUT_SWITCH_ITEM_DEVICE_COMBO_BOX = "midiOUTSwitchItemDeviceComboBox";
	public static final String NAME_MIDI_IN_METRONROM_DEVICE_COMBO_BOX = "midiINMetronomDeviceComboBox";
	public static final String NAME_BUTTON_SEND_NOTIFIER = "buttonSendNotifier";

	private JPanel middlePanel;
	private JPanel footerPanel;
	private JLabel midiINRemoteDevicesLabel;
	private JLabel midiOUTRemoteDevicesLabel;
	private JLabel midiOUTSwitchNotifierDevicesLabel;
	private JLabel midiOUTSwitchItemDevicesLabel;
	private JLabel labelHeader;
	private JLabel guiAutomationLabel;
	private HTMLLabel midiOUTSwitchNotifierInfoLabel;
	private HTMLLabel midiOUTSwitchItemInfoLabel;
	private HTMLLabel midiINMetronomInfoLabel;
	private JComboBox<String> midiINRemoteDeviceComboBox;
	private JComboBox<String> midiOUTRemoteDeviceComboBox;
	private JComboBox<String> midiOUTSwitchNotifierDeviceComboBox;
	private JComboBox<String> midiINMetronomDeviceComboBox;
	private JComboBox<String> midiOUTSwitchItemDeviceComboBox;

	private GUIAutomationConfigurationPanel guiAutomationConfigurationPanel;

	private JButton buttonSendNotify;

	private final Insets INSETS_LABEL_HEADER = new Insets(0, 5, 0, 10);
	private final Insets INSETS_COMBO_BOX = new Insets(0, 0, 0, 10);
	private final Insets INSETS_LABEL_INFO = new Insets(0, 5, 0, 10);
	private final int CONSTRAINT_FILL_COMBO_BOX = GridBagConstraints.NONE;
	private final int CONSTRAINT_ANCHOR_COMBO_BOX = GridBagConstraints.WEST;

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private Presenter presenter;

	@Autowired
	private GUIAutomationsService guiAutomationsService;
	@Autowired
	private MidiService midiService;
	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;

	/**
	 * Initializes the frame
	 */
	public void init() {
		super.init();
		setResizable(false);
		setTitle(TITLE);
		setName(NAME);

		// set layout
		JPanel contentPanel = new JPanel();
		Border padding = BorderFactory.createEmptyBorder(PADDING_VERTICAL,
				PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL);
		contentPanel.setBorder(padding);
		contentPanel.setLayout(new BorderLayout());
		setContentPane(contentPanel);
		middlePanel = new JPanel(new GridBagLayout());
		footerPanel = new JPanel(new FlowLayout());

		// MIDI IN Remote
		createRemoteMidiInDevices();

		// MIDI OUT Remote
		createRemoteMidiOutDevices();

		// MIDI IN Metronom
		createMetronomMidiInDevices();

		// MIDI OUT Switch Item
		createSwitchItemOutDevices();

		// MIDI OUT Switch Notifier
		createSwitchNotifierMidiOutDevices();

		// GUI Automation
		createGUIAutomation();
		guiAutomationsService.setGUIAutomationsToActive(false);

		// Save
		saveAction = new SaveAction();
		buttonSave.addActionListener(saveAction);
		footerPanel.add(buttonSave);

		// Cancel
		cancelAction = new CancelAction();
		buttonCancel.addActionListener(cancelAction);
		footerPanel.add(buttonCancel);

		add(middlePanel, BorderLayout.CENTER);
		add(footerPanel, BorderLayout.PAGE_END);

		// load data
		reload();

		setAlwaysOnTop(true);
		pack();
		setVisible(true);

		// action on close
		addWindowListener(new WindowCloseListener());
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
		c.gridy = 0;
		middlePanel.add(midiINRemoteDevicesLabel, c);

		List<String> midiINDevices = MidiUtils.getMidiDeviceSignatures("IN");
		midiINDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiINRemoteDeviceComboBox = new JComboBox<String>(
				(String[]) midiINDevices.toArray(new String[0]));
		midiINRemoteDeviceComboBox
				.setName(NAME_MIDI_IN_REMOTE_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = INSETS_COMBO_BOX;
		c.fill = CONSTRAINT_FILL_COMBO_BOX;
		c.anchor = CONSTRAINT_ANCHOR_COMBO_BOX;
		c.gridx = 0;
		c.gridy = 1;
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
		c.gridy = 0;
		middlePanel.add(midiOUTRemoteDevicesLabel, c);

		List<String> midiOutDevices = MidiUtils.getMidiDeviceSignatures("OUT");
		midiOutDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiOUTRemoteDeviceComboBox = new JComboBox<String>(
				(String[]) midiOutDevices.toArray(new String[0]));
		midiOUTRemoteDeviceComboBox
				.setName(NAME_MIDI_OUT_REMOTE_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = INSETS_COMBO_BOX;
		c.fill = CONSTRAINT_FILL_COMBO_BOX;
		c.anchor = CONSTRAINT_ANCHOR_COMBO_BOX;
		c.gridx = 1;
		c.gridy = 1;
		middlePanel.add(midiOUTRemoteDeviceComboBox, c);

		// Info label
		c = new GridBagConstraints();
		c.insets = INSETS_LABEL_INFO;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
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
		c.gridy = 0;
		middlePanel.add(midiOUTSwitchNotifierDevicesLabel, c);

		List<String> midiOutDevices = MidiUtils.getMidiDeviceSignatures("OUT");
		midiOutDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiOUTSwitchNotifierDeviceComboBox = new JComboBox<String>(
				(String[]) midiOutDevices.toArray(new String[0]));
		midiOUTSwitchNotifierDeviceComboBox
				.setName(NAME_MIDI_OUT_SWITCH_NOTIFIER_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = CONSTRAINT_FILL_COMBO_BOX;
		c.anchor = CONSTRAINT_ANCHOR_COMBO_BOX;
		c.gridx = 2;
		c.gridy = 1;
		middlePanel.add(midiOUTSwitchNotifierDeviceComboBox, c);

		// Button test notification
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 3;
		c.gridy = 1;
		buttonSendNotify = new JButton(BUTTON_SEND_NOTIFIER);
		buttonSendNotify.setName(NAME_BUTTON_SEND_NOTIFIER);
		buttonSendNotify.addActionListener(new SendNotificationAction());
		buttonSendNotify.setMaximumSize(new Dimension(7, 10));
		middlePanel.add(buttonSendNotify, c);

		// Info label
		c = new GridBagConstraints();
		c.insets = INSETS_LABEL_INFO;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
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
		c.gridy = 3;
		middlePanel.add(midiOUTSwitchItemDevicesLabel, c);

		List<String> devices = MidiUtils.getMidiDeviceSignatures("OUT");
		devices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiOUTSwitchItemDeviceComboBox = new JComboBox<String>(
				(String[]) devices.toArray(new String[0]));
		midiOUTSwitchItemDeviceComboBox
				.setName(NAME_MIDI_OUT_SWITCH_ITEM_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = INSETS_COMBO_BOX;
		c.fill = CONSTRAINT_FILL_COMBO_BOX;
		c.anchor = CONSTRAINT_ANCHOR_COMBO_BOX;
		c.gridx = 0;
		c.gridy = 4;
		middlePanel.add(midiOUTSwitchItemDeviceComboBox, c);

		// Info label
		c = new GridBagConstraints();
		c.insets = INSETS_LABEL_INFO;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
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
		c.gridy = 3;
		middlePanel.add(labelHeader, c);

		List<String> midiInDevices = MidiUtils.getMidiDeviceSignatures("IN");
		midiInDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiINMetronomDeviceComboBox = new JComboBox<String>(
				(String[]) midiInDevices.toArray(new String[0]));
		midiINMetronomDeviceComboBox
				.setName(NAME_MIDI_IN_METRONROM_DEVICE_COMBO_BOX);

		c = new GridBagConstraints();
		c.insets = INSETS_COMBO_BOX;
		c.fill = CONSTRAINT_FILL_COMBO_BOX;
		c.anchor = CONSTRAINT_ANCHOR_COMBO_BOX;
		c.gridx = 1;
		c.gridy = 4;
		middlePanel.add(midiINMetronomDeviceComboBox, c);

		// Info label
		c = new GridBagConstraints();
		c.insets = INSETS_LABEL_INFO;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
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

		guiAutomationLabel = new JLabel(LABEL_GUI_AUTOMATION);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 8;
		middlePanel.add(guiAutomationLabel, c);

		guiAutomationConfigurationPanel = ctx.getBean(
				"GUIAutomationConfigurationPanel",
				GUIAutomationConfigurationPanel.class);
		guiAutomationConfigurationPanel.init();
		guiAutomationConfigurationPanel.setOpaque(true);
		c.gridx = 0;
		c.gridy = 9;
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
		GUIAutomationConfigurationTable table = guiAutomationConfigurationPanel
				.getGUIAutomationsTable();
		try {
			table.setMidiSignature(signature, table.getSelectedRow());
		} catch (AutomationIndexDoesNotExistException e) {
			log.error("The automation for the MIDI signature does not exist", e);
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

		// gui automations
		GUIAutomation[] guiAutomations = guiAutomationsService
				.getGuiAutomations();
		GUIAutomationConfigurationTable table = guiAutomationConfigurationPanel
				.getGUIAutomationsTable();

		if (guiAutomations != null) {
			for (int i = 0; i < guiAutomations.length; i++) {
				table.setAutomation(guiAutomations[i].getImagePath(),
						guiAutomations[i].getType(),
						guiAutomations[i].getTrigger(),
						guiAutomations[i].getMinDelay(),
						guiAutomations[i].getTimeout(),
						guiAutomations[i].getMidiSignature(),
						guiAutomations[i].getMinSimilarity(),
						guiAutomations[i].getScanRate(),
						guiAutomations[i].isMovable(), i);
			}
		}
	}

	/**
	 * Puts the frame to midi learn mode
	 * 
	 * @param learningComponent
	 *            The component to learn for
	 */
	public void midiLearnOn(JComponent learningComponent) {

		// disable inputs
		GUIUtils.disEnableAllInputs(this, false);

		guiAutomationConfigurationPanel.getPopupMenu().midiLearnOn();
		CacheableJTable table = guiAutomationConfigurationPanel
				.getGUIAutomationsTable();

		if (learningComponent.getName() != null) {
			if (learningComponent.getName().equals(
					GUIAutomationConfigurationTable.NAME)) {

				GUIUtils.deHighlightTableRow(table, true);

				log.info("Learning midi for automation index: "
						+ table.getSelectedRow());

			}
		}
	}

	/**
	 * Puts the frame to normal mode.
	 */
	public void midiLearnOff() {

		// enable inputs
		GUIUtils.disEnableAllInputs(this, true);

		// change menu item
		guiAutomationConfigurationPanel.getPopupMenu().midiLearnOff();

		GUIUtils.deHighlightTableRow(
				guiAutomationConfigurationPanel.getGUIAutomationsTable(), false);
	}

	public GUIAutomationConfigurationPanel getGuiAutomationPanel() {
		return guiAutomationConfigurationPanel;
	}

	/**
	 * Closes the preferences window, saves and loads the configuration.
	 * 
	 * @author aguelle
	 * 
	 */
	class SaveAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

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
			midiService
					.setMidiDeviceName(
							midiOUTSwitchNotifierDeviceName,
							MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE);
			midiService.setMidiDeviceName(midiOUTSwitchItemDeviceName,
					MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE);
			guiAutomationsService.saveGUIAutomations(guiAutomations);

			new CancelAction().actionPerformed(e);
			presenter.loadProperties();
		}
	}

	/**
	 * Closes the preferences window.
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

	/**
	 * Sends a notification through the chosen midi device.
	 * 
	 * @author aguelle
	 * 
	 */
	class SendNotificationAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			String deviceName = (String) midiOUTSwitchNotifierDeviceComboBox
					.getSelectedItem();

			if (!deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {
				try {
					MidiDevice device = MidiUtils.getMidiDevice(deviceName,
							"OUT");
					midiNotificationService.sendItemChangeNotifier(device);
				} catch (MidiUnavailableException ex) {
					log.error(
							"The MIDI device for the switch notification is unavailable",
							ex);
				}
			}
		}
	}

	/**
	 * All actions that shall be done when window is closed
	 * 
	 * @author aguelle
	 * 
	 */
	class WindowCloseListener extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			e.getWindow().dispose();
			guiAutomationsService.setGUIAutomationsToActive(true);
		}
	}
}
