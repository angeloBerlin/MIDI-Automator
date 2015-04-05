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

import com.midi.automator.IApplication;
import com.midi.automator.PropertiesReloadThread;
import com.midi.automator.guiautomator.GUIAutomation;
import com.midi.automator.model.MidiAutomatorProperties;
import com.midi.automator.utils.GUIUtils;
import com.midi.automator.utils.MidiUtils;
import com.midi.automator.view.CacheableJTable;
import com.midi.automator.view.HTMLLabel;
import com.midi.automator.view.automationconfiguration.AutomationIndexDoesNotExistException;
import com.midi.automator.view.automationconfiguration.GUIAutomationConfigurationPanel;
import com.midi.automator.view.automationconfiguration.GUIAutomationConfigurationTable;

public class PreferencesFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final int PADDING_HORIZONTAL = 20;
	private final int PADDING_VERTICAL = 10;
	private final String TITLE = "Preferences";
	private final String LABEL_MIDI_IN_REMOTE_DEVICES = "Midi Remote IN:";
	private final String LABEL_MIDI_OUT_REMOTE_DEVICES = "Midi Master OUT:";
	private final String LABEL_MIDI_OUT_REMOTE_OPEN = "Master open: ch 1 CC 102 &lt;file number - 1&gt;";
	private final String LABEL_MIDI_OUT_SWITCH_NOTIFIER_DEVICES = "Midi Notifier OUT:";
	private final String LABEL_MIDI_OUT_SWITCH_NOTIFIER_INFO = "Notifier: ch 1 CC 103 value 127";
	private final String LABEL_MIDI_IN_METRONOM_DEVICES = "Midi Metronom IN:";
	private final String LABEL_MIDI_IN_METRONOM_INFO = "1st click: ch 16 NOTE ON A4<br/>"
			+ "Other clicks: ch 16 NOTE ON E4";
	private final String LABEL_GUI_AUTOMATION = "Mouse Automation:";
	private final String BUTTON_SEND_NOTIFIER = "Send...";
	private final String BUTTON_SAVE = "Save";
	private final String BUTTON_CANCEL = "Cancel";
	private final String MIDI_IN_REMOTE_DEVICE_COMBO_BOX_NAME = "midiINRemoteDeviceComboBox";
	private final String MIDI_OUT_REMOTE_DEVICE_COMBO_BOX_NAME = " midiOUTRemoteDeviceComboBox";
	private final String MIDI_OUT_SWITCH_NOTIFIER_DEVICE_COMBO_BOX_NAME = "midiOUTSwitchNotifierDeviceComboBox";
	private final String MIDI_IN_METRONROM_DEVICE_COMBO_BOX_NAME = "midiINMetronomDeviceComboBox";

	private JPanel middlePanel;
	private JPanel footerPanel;
	private JLabel midiINRemoteDevicesLabel;
	private JLabel midiOUTRemoteDevicesLabel;
	private JLabel midiOUTSwitchNotifierDevicesLabel;
	private JLabel guiAutomationLabel;
	private HTMLLabel midiOUTSwitchNotifierInfoLabel;
	private JLabel midiINMetronomDevicesLabel;
	private HTMLLabel midiINMetronomInfoLabel;
	private JComboBox<String> midiINRemoteDeviceComboBox;
	private JComboBox<String> midiOUTRemoteDeviceComboBox;
	private JComboBox<String> midiOUTSwitchNotifierDeviceComboBox;
	private JComboBox<String> midiINMetronomDeviceComboBox;
	private GUIAutomationConfigurationPanel guiAutomationPanel;
	private JButton buttonSendNotify;
	private JButton buttonSave;
	private JButton buttonCancel;

	private IApplication application;
	private JFrame programFrame;
	private int gridRowCount = 0;

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
	public PreferencesFrame(IApplication application, JFrame programFrame)
			throws HeadlessException {
		super();

		this.setResizable(false);
		this.application = application;
		this.programFrame = programFrame;

		setTitle(TITLE);
		setLocation(this.programFrame.getLocationOnScreen());

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

		// MIDI OUT Switch Notifier
		createSwitchNotifierMidiOutDevices();

		// GUI Automation
		createGUIAutomation();
		application.setGUIAutomationsToActive(false);

		// Save
		buttonSave = new JButton(BUTTON_SAVE);
		buttonSave.addActionListener(new SaveAction());
		footerPanel.add(buttonSave);

		// Cancel
		buttonCancel = new JButton(BUTTON_CANCEL);
		buttonCancel.addActionListener(new CancelAction());
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
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		middlePanel.add(midiINRemoteDevicesLabel, c);

		List<String> midiINDevices = MidiUtils.getMidiDeviceSignatures("IN");
		midiINDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiINRemoteDeviceComboBox = new JComboBox<String>(
				(String[]) midiINDevices.toArray(new String[0]));
		midiINRemoteDeviceComboBox
				.setName(MIDI_IN_METRONROM_DEVICE_COMBO_BOX_NAME);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		middlePanel.add(midiINRemoteDeviceComboBox, c);

	}

	/**
	 * Creates the midi out devices combo box for the remote switch with a label
	 */
	private void createRemoteMidiOutDevices() {

		GridBagConstraints c = new GridBagConstraints();
		midiOUTRemoteDevicesLabel = new JLabel(LABEL_MIDI_OUT_REMOTE_DEVICES);
		c.insets = new Insets(10, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		middlePanel.add(midiOUTRemoteDevicesLabel, c);

		List<String> midiOutDevices = MidiUtils.getMidiDeviceSignatures("OUT");
		midiOutDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiOUTRemoteDeviceComboBox = new JComboBox<String>(
				(String[]) midiOutDevices.toArray(new String[0]));
		midiOUTRemoteDeviceComboBox
				.setName(MIDI_OUT_REMOTE_DEVICE_COMBO_BOX_NAME);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		middlePanel.add(midiOUTRemoteDeviceComboBox, c);

		// Info label
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		midiOUTSwitchNotifierInfoLabel = new HTMLLabel(
				"<span style='font-family:Arial; font-size:8px'>"
						+ LABEL_MIDI_OUT_REMOTE_OPEN + "</span>");
		middlePanel.add(midiOUTSwitchNotifierInfoLabel, c);
	}

	/**
	 * Creates the midi in devices combo box for metronom with a label.
	 */
	private void createMetronomMidiInDevices() {

		GridBagConstraints c = new GridBagConstraints();
		midiINMetronomDevicesLabel = new JLabel(LABEL_MIDI_IN_METRONOM_DEVICES);
		c.insets = new Insets(10, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		middlePanel.add(midiINMetronomDevicesLabel, c);

		List<String> midiInDevices = MidiUtils.getMidiDeviceSignatures("IN");
		midiInDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiINMetronomDeviceComboBox = new JComboBox<String>(
				(String[]) midiInDevices.toArray(new String[0]));
		midiINMetronomDeviceComboBox
				.setName(MIDI_IN_REMOTE_DEVICE_COMBO_BOX_NAME);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		middlePanel.add(midiINMetronomDeviceComboBox, c);

		// Info label
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		midiINMetronomInfoLabel = new HTMLLabel(
				"<span style='font-family:Arial; font-size:8px'>"
						+ LABEL_MIDI_IN_METRONOM_INFO + "</span>");
		middlePanel.add(midiINMetronomInfoLabel, c);
	}

	/**
	 * Creates the midi out devices combo box for the switch notifier with a
	 * label
	 */
	private void createSwitchNotifierMidiOutDevices() {

		GridBagConstraints c = new GridBagConstraints();
		midiOUTSwitchNotifierDevicesLabel = new JLabel(
				LABEL_MIDI_OUT_SWITCH_NOTIFIER_DEVICES);
		c.insets = new Insets(10, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		middlePanel.add(midiOUTSwitchNotifierDevicesLabel, c);

		List<String> midiOutDevices = MidiUtils.getMidiDeviceSignatures("OUT");
		midiOutDevices.add(0, MidiAutomatorProperties.VALUE_NULL);
		midiOUTSwitchNotifierDeviceComboBox = new JComboBox<String>(
				(String[]) midiOutDevices.toArray(new String[0]));
		midiOUTSwitchNotifierDeviceComboBox
				.setName(MIDI_OUT_SWITCH_NOTIFIER_DEVICE_COMBO_BOX_NAME);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount;
		middlePanel.add(midiOUTSwitchNotifierDeviceComboBox, c);

		// Button test notification
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = gridRowCount++;
		buttonSendNotify = new JButton(BUTTON_SEND_NOTIFIER);
		buttonSendNotify.addActionListener(new SendNotificationAction());
		buttonSendNotify.setMaximumSize(new Dimension(7, 10));
		middlePanel.add(buttonSendNotify, c);

		// Info label
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		midiOUTSwitchNotifierInfoLabel = new HTMLLabel(
				"<span style='font-family:Arial; font-size:8px'>"
						+ LABEL_MIDI_OUT_SWITCH_NOTIFIER_INFO + "</span>");
		middlePanel.add(midiOUTSwitchNotifierInfoLabel, c);
	}

	/**
	 * Creates the GUI automation configuration panel
	 */
	private void createGUIAutomation() {

		GridBagConstraints c = new GridBagConstraints();
		guiAutomationLabel = new JLabel(LABEL_GUI_AUTOMATION);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridRowCount++;
		middlePanel.add(guiAutomationLabel, c);

		guiAutomationPanel = new GUIAutomationConfigurationPanel(application);
		guiAutomationPanel.setOpaque(true);

		c.gridx = 0;
		c.gridy = gridRowCount++;
		c.gridwidth = 3;
		middlePanel.add(guiAutomationPanel, c);
	}

	/**
	 * Sets a learned midi signature.
	 * 
	 * @param signature
	 *            The signature
	 * @param component
	 *            The learned component
	 */
	public void setMidiSignature(String signature, Component component) {

		// learning for automation list
		if (component.getName().equals(
				GUIAutomationConfigurationPanel.NAME_CONFIGURATION_TABLE)) {
			GUIAutomationConfigurationTable table = guiAutomationPanel
					.getConfigurationTable();
			try {
				table.setMidiMessage(signature, table.getSelectedRow());
			} catch (AutomationIndexDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reloads the data of the frame
	 */
	public void reload() {

		// midi in remote opener
		String midiInDeviceName = application.getMidiINRemoteDeviceName();

		if (midiInDeviceName != null) {
			midiINRemoteDeviceComboBox.setSelectedItem(midiInDeviceName);
		}

		// midi out remote opener
		String midiOUTDevice = application.getMidiOUTRemoteDeviceName();

		if (midiOUTDevice != null) {
			midiOUTRemoteDeviceComboBox.setSelectedItem(midiOUTDevice);
		}

		// midi in metronom
		String midiINMetronomDevice = application.getMidiINMetronomDeviceName();

		if (midiINMetronomDevice != null) {
			midiINMetronomDeviceComboBox.setSelectedItem(midiINMetronomDevice);
		}

		// midi out switch notifier
		String midiOUTSwitchNotifierDevice = application
				.getMidiOUTSwitchNotifierDeviceName();

		if (midiOUTSwitchNotifierDevice != null) {
			midiOUTSwitchNotifierDeviceComboBox
					.setSelectedItem(midiOUTSwitchNotifierDevice);
		}

		// gui automations
		GUIAutomation[] guiAutomations = application.getGUIAutomations();
		GUIAutomationConfigurationTable table = guiAutomationPanel
				.getConfigurationTable();

		for (int i = 0; i < guiAutomations.length; i++) {
			table.setAutomation(guiAutomations[i].getImagePath(),
					guiAutomations[i].getType(),
					guiAutomations[i].getTrigger(),
					guiAutomations[i].getMinDelay(),
					guiAutomations[i].getMidiSignature(), i);
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

		guiAutomationPanel.getPopupMenu().midiLearnOn();
		CacheableJTable table = guiAutomationPanel.getConfigurationTable();

		if (learningComponent.getName() != null) {
			if (learningComponent.getName().equals(
					GUIAutomationConfigurationPanel.NAME_CONFIGURATION_TABLE)) {

				GUIUtils.deHighlightTableRow(table, true);

				if (application.isInDebugMode()) {
					System.out.println("Learning midi for automation index: "
							+ table.getSelectedRow());
				}

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
		guiAutomationPanel.getPopupMenu().midiLearnOff();

		GUIUtils.deHighlightTableRow(
				guiAutomationPanel.getConfigurationTable(), false);
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
			GUIAutomation[] guiAutomations = guiAutomationPanel
					.getGUIAutomations();

			application.setMidiINRemoteDeviceName(midiINRemoteDeviceName);
			application.setMidiOUTRemoteDeviceName(midiOUTRemoteDeviceName);
			application.setMidiINMetronomDeviceName(midiINMetronomDeviceName);
			application
					.setMidiOUTSwitchNotifierDeviceName(midiOUTSwitchNotifierDeviceName);
			application.setGUIAutomations(guiAutomations);

			new CancelAction().actionPerformed(e);
			new PropertiesReloadThread(application).start();
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
			try {
				MidiDevice device = MidiUtils.getMidiDevice(deviceName, "OUT");
				application.sendItemChangeNotifier(device);
			} catch (MidiUnavailableException e1) {
				e1.printStackTrace();
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
			application.setGUIAutomationsToActive(true);
		}
	}
}
