package com.midi_automator;

import java.awt.Component;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.swing.JComponent;

import com.midi_automator.guiautomator.GUIAutomation;

/**
 * Things the main application should provide.
 * 
 * @author aguelle
 * 
 */
public interface IApplication {

	public static final String FILE_EXTENSION = ".mido";
	public static final String SWITCH_DIRECTION_PREV = "previous";
	public static final String SWITCH_DIRECTION_NEXT = "next";
	public static final int OPEN_FILE_MIDI_COMMAND = ShortMessage.CONTROL_CHANGE;
	public static final int OPEN_FILE_MIDI_CHANNEL = 1;
	public static final int OPEN_FILE_MIDI_CONTROL_NO = 102;
	public static final int SWITCH_NOTIFIER_MIDI_COMMAND = ShortMessage.CONTROL_CHANGE;
	public static final int SWITCH_NOTIFIER_MIDI_CHANNEL = 1;
	public static final int SWITCH_NOTIFIER_MIDI_CONTROL_NO = 103;
	public static final int SWITCH_NOTIFIER_MIDI_VALUE = 127;
	public static final long WAIT_BEFORE_OPENING = 100;
	public static final long WAIT_BEFORE_SLAVE_SEND = 2000;
	public static final String OPEN_FILE_MIDI_SIGNATURE = "channel 1: CONTROL CHANGE 102";
	public static final String SWITCH_NOTIFIER_MIDI_SIGNATURE = "channel 1: CONTROL CHANGE 103";
	public static final String METRONOM_FIRST_CLICK_MIDI_SIGNATURE = "channel 16: NOTE ON A4";
	public static final String METRONOM_CLICK_MIDI_SIGNATURE = "channel 16: NOTE ON E4";

	/**
	 * Loads the properties file.
	 * 
	 * @throws MidiUnavailableException
	 */
	public void reloadProperties();

	/**
	 * Returns if the application is in midi learn mode
	 * 
	 * @return <TRUE> application is in midi learn mode, <FALSE> application is
	 *         not in midi learn mode
	 */
	public boolean isInMidiLearnMode();

	/**
	 * Sets the application to midi learn mode
	 * 
	 * @param midiLearn
	 *            <TRUE> application is in midi learn mode, <FALSE> application
	 *            is not in midi learn mode
	 * @param learningComponent
	 *            The component for which shall be learned, may be <NULL> if
	 *            midi learn is <FALSE>
	 */
	public void setMidiLearnMode(boolean midiLearn, JComponent learningComponent);

	/**
	 * Sets the midi signature. The component will be implicitly taken from the
	 * learningComponent field
	 * 
	 * @param signature
	 *            The midi signature
	 */
	public void setMidiSignature(String signature);

	/**
	 * Sets the midi signature for a given Component
	 * 
	 * @param signature
	 *            The midi signature
	 * @param component
	 *            The component to set the midi signature for
	 */
	public void setMidiSignature(String signature, Component component);

	/**
	 * Gets the midi signature of the switch directions
	 * 
	 * @param switchDirection
	 *            SWITCH_DIRECTION_PREV previous direction,
	 *            SWITCH_DIRECTION_NEXT next direction
	 * @return The midi signature
	 */
	public String getMidiSignature(String switchDirection);

	/**
	 * Gets the midi signature for the file list index
	 * 
	 * @param index
	 *            The index in the file list
	 * @return The midi signature
	 */
	public String getMidiSignature(int index);

	/**
	 * Gets the configured GUI automations.
	 * 
	 * @return The GUI automations as an array
	 */
	public GUIAutomation[] getGUIAutomations();

	/**
	 * Returns if the application is in debug mode
	 * 
	 * @return <TRUE> if the appplication is in debug mode, <FALSE> if the
	 *         application is not in debug mode
	 */
	public boolean isInDebugMode();

	/**
	 * Returns if the application is in test mode
	 * 
	 * @return <TRUE> if the appplication is in development mode, <FALSE> if the
	 *         application is not in development mode
	 */
	public boolean isInTestMode();

	/**
	 * Runs the function for the midi message
	 * 
	 * @param message
	 *            The midi message
	 */
	public void executeMidiMessage(MidiMessage message);

	/**
	 * Executes the midi metronom's click
	 * 
	 * @beat the current clicked beat
	 */
	public void metronomClick(int beat);

	/**
	 * Opens a file from the file list
	 * 
	 * @param index
	 *            The index of the file to open from the list
	 * @param send
	 *            <TRUE> opened index will be sent to slaves, <FALSE> index will
	 *            not be sent
	 */
	public void openFileByIndex(int index, boolean send);

	/**
	 * Opens the previous file in the list
	 */
	public void openPreviousFile();

	/**
	 * Opens the next file in the list
	 */
	public void openNextFile();

	/**
	 * Gets the entry name of the file list by index
	 * 
	 * @param index
	 *            The index
	 * @return the name of the file entry
	 */
	public String getEntryNameByIndex(int index);

	/**
	 * Gets the entry file path of the file list by index
	 * 
	 * @param index
	 *            The index
	 * @return the file path of the entry
	 */
	public String getEntryPathByIndex(int index);

	/**
	 * Gets the name of the remote midi input device
	 * 
	 * @return The midi device name
	 */
	public String getMidiINRemoteDeviceName();

	/**
	 * Sets the metronom midi input device name
	 * 
	 * @param midiINDeviceName
	 *            The midi device name
	 */
	public void setMidiINMetronomDeviceName(String midiINDeviceName);

	/**
	 * Gets the name of the metronom midi input device
	 * 
	 * @return The midi device name
	 */
	public String getMidiINMetronomDeviceName();

	/**
	 * Sets the remote midi input device name
	 * 
	 * @param midiINDeviceName
	 *            The midi device name
	 */
	public void setMidiINRemoteDeviceName(String midiINDeviceName);

	/**
	 * Gets the name of the remote midi out device
	 * 
	 * @return The midi device name
	 */
	public String getMidiOUTRemoteDeviceName();

	/**
	 * Sets the remote midi out device name
	 * 
	 * @param midi
	 *            device name The midi device name
	 */
	public void setMidiOUTRemoteDeviceName(String deviceName);

	/**
	 * Gets the name of the switch notifier midi out device
	 * 
	 * @return The midi device name
	 */
	public String getMidiOUTSwitchNotifierDeviceName();

	/**
	 * Sets the switch notifier midi out device name
	 * 
	 * @param midi
	 *            device name The midi device name
	 */
	public void setMidiOUTSwitchNotifierDeviceName(String deviceName);

	/**
	 * Sets an error message
	 * 
	 * @param message
	 *            The info message
	 */
	public void setInfoMessage(String message);

	/**
	 * Removes the info message
	 * 
	 * @param message
	 *            The info message
	 */
	public void removeInfoMessage(String message);

	/**
	 * Indicates a midi IN signal
	 */
	public void showMidiINSignal();

	/**
	 * Indicates a midi OUT signal
	 */
	public void showMidiOUTSignal();

	/**
	 * Closes all resources of the application
	 */
	public void close();

	/**
	 * Returns the resources handler
	 * 
	 * @return The resources handler
	 */
	public Resources getResources();

	/**
	 * Pushes the item at the given index one step up
	 * 
	 * @param index
	 *            index of the item
	 */
	public void moveUpItem(int index);

	/**
	 * Pushes the item at the given index one step down
	 * 
	 * @param index
	 *            index of the item
	 */
	public void moveDownItem(int index);

	/**
	 * Deletes the item at the given index from the model
	 * 
	 * @param index
	 *            index of the item
	 */
	public void deleteItem(int index);

	/**
	 * Adds a new item
	 * 
	 * @param entryName
	 *            the name of the entry
	 * @param filePath
	 *            the path to the file
	 */
	public void addItem(String entryName, String filePath);

	/**
	 * Sets the attributes of an existing item
	 * 
	 * @param index
	 *            the index of the entry
	 * @param entryName
	 *            the name of the entry
	 * @param filePath
	 *            the path to the file
	 * @param midiSignature
	 *            the midi signature
	 */
	public void setItem(Integer index, String entryName, String filePath,
			String midiSignature);

	/**
	 * Sends a midi message as notifier that the item has changed.
	 * 
	 * @param device
	 *            The midi notification device
	 */
	public void sendItemChangeNotifier(MidiDevice device);

	/**
	 * Sets all GUI automations.
	 * 
	 * @param guiAutomations
	 */
	public void setGUIAutomations(GUIAutomation[] guiAutomations);

	/**
	 * Removes all GUI automations.
	 */
	public void removeGUIAutomations();

	/**
	 * De-/Activates the GUI automation.
	 * 
	 * @param active
	 *            <TRUE> activete GUI automation, <FALSE> deactivate GUI
	 *            automation
	 */
	public void setGUIAutomationsToActive(boolean active);

	/**
	 * Gets the state if midi messages shall be executed.
	 * 
	 * @return <TRUE> if messages shall not be executed, <FALSE> if they shall
	 *         be executed
	 */
	public boolean isDoNotExecuteMidiMessage();

	/**
	 * Sets the state if midi messages shall be executed.
	 * 
	 * @param doNotExecuteMidiMessage
	 *            <TRUE> if messages shall not be executed, <FALSE> if they
	 *            shall be executed
	 */
	public void setDoNotExecuteMidiMessage(boolean doNotExecuteMidiMessage);
}
