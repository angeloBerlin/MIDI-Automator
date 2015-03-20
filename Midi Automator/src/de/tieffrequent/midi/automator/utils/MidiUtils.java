package de.tieffrequent.midi.automator.utils;

/*
 * Copyright (c) 1999 - 2001 by Matthias Pfisterer
 * Copyright (c) 2003 by Florian Bomers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Transmitter;

/**
 * Supports several utilities for MIDI handling.
 * 
 * @author aguelle
 * 
 */
public class MidiUtils {

	private static final String NOTE_ON = "NOTE ON";
	private static final String NOTE_OFF = "NOTE OFF";
	private static final String POLYPHONIC_KEY_PRESSURE = "POLYPHONIC KEY PRESSURE";
	private static final String CONTROL_CHANGE = "CONTROL CHANGE";
	private static final String PROGRAM_CHANGE = "PROGRAM CHANGE";
	private static final String KEY_PRESSURE = "KEY PRESSURE";
	private static final String PITCH_WHEEL_CHANGE = "PITCH WHEEL CHANGE";
	private static final String SYSTEM_MESSAGE = "SYSTEM MESSAGE";
	public static final String UNKNOWN_MESSAGE = "UNKNOWN MESSAGE";

	public static long seByteCount = 0;
	public static long smByteCount = 0;
	public static long seCount = 0;
	public static long smCount = 0;

	private static final String[] sm_astrKeyNames = { "C", "C#", "D", "D#",
			"E", "F", "F#", "G", "G#", "A", "A#", "B" };

	private static final String[] sm_astrKeySignatures = { "Cb", "Gb", "Db",
			"Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#" };

	private static final String[] QUARTER_FRAME_MESSAGE_TEXT = {
			"frame count LS: ", "frame count MS: ", "seconds count LS: ",
			"seconds count MS: ", "minutes count LS: ", "minutes count MS: ",
			"hours count LS: ", "hours count MS: " };

	private static final String[] FRAME_TYPE_TEXT = { "24 frames/second",
			"25 frames/second", "30 frames/second (drop)",
			"30 frames/second (non-drop)", };

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final String[] SYSTEM_MESSAGE_TEXT = {
			"System Exclusive (should not be in ShortMessage!)",
			"MTC Quarter Frame: ", "Song Position: ", "Song Select: ",
			"Undefined", "Undefined", "Tune Request",
			"End of SysEx (should not be in ShortMessage!)", "Timing clock",
			"Undefined", "Start", "Continue", "Stop", "Undefined",
			"Active Sensing", "System Reset" };

	/**
	 * Gets all MIDI devices names
	 * 
	 * @return A list with the names of the devices
	 */
	public static List<String> getMidiDeviceSignatures() {
		return getMidiDeviceSignatures(null);
	}

	/**
	 * Gets all MIDI devices names
	 * 
	 * @param direction
	 *            The desired port direction, <NULL> gets all
	 * @return A list with the signatures of the devices <index> <directions>:
	 *         <name>
	 */
	public static List<String> getMidiDeviceSignatures(String direction) {

		List<String> midiDeviceSignatures = new ArrayList<String>();
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

		for (int i = 0; i < infos.length; i++) {
			try {

				MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
				String name = infos[i].getName();
				int maxReceivers = device.getMaxReceivers();
				int maxTransmitters = device.getMaxTransmitters();

				if (direction == null) {

					midiDeviceSignatures.add(name);

				} else {

					if (direction.equals("IN") && (maxTransmitters != 0)) {
						midiDeviceSignatures.add(name);
					}

					if (direction.equals("OUT") && (maxReceivers != 0)) {
						midiDeviceSignatures.add(name);
					}
				}

			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}

		return midiDeviceSignatures;
	}

	/**
	 * Retrieve a MidiDevice.Info for a given name.
	 * 
	 * This method tries to return a MidiDevice.Info whose name matches the
	 * passed name. If no matching MidiDevice.Info is found, null is returned.
	 * If bForOutput is true, then only output devices are searched, otherwise
	 * only input devices.
	 * 
	 * @param strDeviceName
	 *            the name of the device for which an info object should be
	 *            retrieved.
	 * @param bForOutput
	 *            If true, only output devices are considered. If false, only
	 *            input devices are considered.
	 * @return A MidiDevice.Info object matching the passed device name or null
	 *         if none could be found.
	 */
	public static MidiDevice.Info getMidiDeviceInfo(String strDeviceName,
			boolean bForOutput) {

		MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();

		for (int i = 0; i < aInfos.length; i++) {

			if (aInfos[i].getName().equals(strDeviceName)) {

				try {
					MidiDevice device = MidiSystem.getMidiDevice(aInfos[i]);
					boolean bAllowsInput = (device.getMaxTransmitters() != 0);
					boolean bAllowsOutput = (device.getMaxReceivers() != 0);
					if ((bAllowsOutput && bForOutput)
							|| (bAllowsInput && !bForOutput)) {
						return aInfos[i];
					}

				} catch (MidiUnavailableException e) {
					// TODO:
				}
			}
		}
		return null;
	}

	/**
	 * Gets a midi device by name
	 * 
	 * @param midiDeviceName
	 *            The name of the midi device
	 * @param outputDevice
	 *            "OUT" only output devices are considered, "IN" only input
	 *            devices are considered.
	 * @return The midi device
	 * @throws MidiUnavailableException
	 *             If the midi device can not be found
	 */
	public static MidiDevice getMidiDevice(String midiDeviceName, String inout)
			throws MidiUnavailableException {

		MidiDevice.Info[] midiInfos;
		MidiDevice device = null;

		if (midiDeviceName != null) {

			midiInfos = MidiSystem.getMidiDeviceInfo();

			for (int i = 0; i < midiInfos.length; i++) {
				if (midiInfos[i].getName().equals(midiDeviceName)) {
					device = MidiSystem.getMidiDevice(midiInfos[i]);

					boolean allowsInput = (device.getMaxTransmitters() != 0);
					boolean allowsOutput = (device.getMaxReceivers() != 0);
					if ((allowsOutput && inout.equals("OUT"))
							|| (allowsInput && inout.equals("IN"))) {
						return device;
					}
				}
			}

		}
		return device;
	}

	/**
	 * Sets a midi receiver to a midi device
	 * 
	 * @param deviceName
	 *            The name of the midi device
	 * @param receiver
	 *            The midi receiver
	 * @throws MidiUnavailableException
	 *             If midi device is not available
	 */
	public static void setReceiverToDevice(MidiDevice device, Receiver receiver)
			throws MidiUnavailableException {

		// check if Receiver was already connected
		List<Transmitter> transmitters = device.getTransmitters();
		for (Transmitter existingTransmitter : transmitters) {
			if (receiver.equals(existingTransmitter.getReceiver())) {
				return;
			}
		}

		Transmitter t = device.getTransmitter();
		t.setReceiver(receiver);
	}

	/**
	 * Formats a midi message to a readable String.
	 * 
	 * @param message
	 *            The midi message
	 * @return a readable String
	 */
	public static String messageToString(MidiMessage message) {

		if (message instanceof ShortMessage) {
			return decodeMessage((ShortMessage) message);
		} else if (message instanceof SysexMessage) {
			return decodeMessage((SysexMessage) message);
		} else if (message instanceof MetaMessage) {
			return decodeMessage((MetaMessage) message);
		} else {
			return MidiUtils.UNKNOWN_MESSAGE;
		}
	}

	/**
	 * Interprets the midi command byte code to a readable String.
	 * 
	 * @param command
	 *            The midi command
	 * @return a readable command
	 */
	private static String decodeCommand(int command) {
		String result;
		switch (command) {
		case 0x80:
			result = NOTE_OFF;
			break;

		case 0x90:
			result = NOTE_ON;
			break;

		case 0xa0:
			result = POLYPHONIC_KEY_PRESSURE;
			break;

		case 0xb0:
			result = CONTROL_CHANGE;
			break;

		case 0xc0:
			result = PROGRAM_CHANGE;
			break;

		case 0xd0:
			result = KEY_PRESSURE;
			break;

		case 0xe0:
			result = PITCH_WHEEL_CHANGE;
			break;

		case 0xF0:
			result = SYSTEM_MESSAGE;

		default:
			result = UNKNOWN_MESSAGE;
			break;
		}
		return result;
	}

	private static String decodeMessage(ShortMessage message) {
		String strMessage = null;
		String command = decodeCommand(message.getCommand());

		switch (command) {
		case NOTE_OFF:
			strMessage = NOTE_OFF + " " + getKeyName(message.getData1());
			break;

		case NOTE_ON:
			strMessage = NOTE_ON + " " + getKeyName(message.getData1());
			break;

		case POLYPHONIC_KEY_PRESSURE:
			strMessage = POLYPHONIC_KEY_PRESSURE + " "
					+ getKeyName(message.getData1()) + " pressure: "
					+ message.getData2();
			break;

		case CONTROL_CHANGE:
			strMessage = CONTROL_CHANGE + " " + message.getData1() + " value: "
					+ message.getData2();
			break;

		case PROGRAM_CHANGE:
			strMessage = PROGRAM_CHANGE + " " + message.getData1();
			break;

		case KEY_PRESSURE:
			strMessage = KEY_PRESSURE + " " + getKeyName(message.getData1())
					+ " pressure: " + message.getData2();
			break;

		case PITCH_WHEEL_CHANGE:
			strMessage = PITCH_WHEEL_CHANGE + " "
					+ get14bitValue(message.getData1(), message.getData2());
			break;

		case SYSTEM_MESSAGE:
			strMessage = SYSTEM_MESSAGE_TEXT[message.getChannel()];

			switch (message.getChannel()) {

			case 1:
				int nQType = (message.getData1() & 0x70) >> 4;
				int nQData = message.getData1() & 0x0F;
				if (nQType == 7) {
					nQData = nQData & 0x1;
				}
				strMessage += QUARTER_FRAME_MESSAGE_TEXT[nQType] + nQData;
				if (nQType == 7) {
					int nFrameType = (message.getData1() & 0x06) >> 1;
					strMessage += ", frame type: "
							+ FRAME_TYPE_TEXT[nFrameType];
				}
				break;

			case 2:
				strMessage += get14bitValue(message.getData1(),
						message.getData2());
				break;

			case 3:
				strMessage += message.getData1();
				break;
			}

			break;

		case UNKNOWN_MESSAGE:
			strMessage = UNKNOWN_MESSAGE + ": status = " + message.getStatus()
					+ ", byte1 = " + message.getData1() + ", byte2 = "
					+ message.getData2();
			break;
		}

		if (command != SYSTEM_MESSAGE) {
			int nChannel = message.getChannel() + 1;
			String strChannel = "channel " + nChannel + ": ";
			strMessage = strChannel + strMessage;
		}

		smCount++;
		smByteCount += message.getLength();
		return strMessage;
	}

	private static String decodeMessage(SysexMessage message) {

		byte[] abData = message.getData();
		String strMessage = null;

		if (message.getStatus() == SysexMessage.SYSTEM_EXCLUSIVE) {

			strMessage = "Sysex message: F0" + getHexString(abData);

		} else if (message.getStatus() == SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE) {
			strMessage = "Continued Sysex message F7" + getHexString(abData);
			seByteCount--; // do not count the F7
		}

		seByteCount += abData.length + 1;
		seCount++; // for the status byte

		return strMessage;
	}

	private static String decodeMessage(MetaMessage message) {
		byte[] abData = message.getData();
		String strMessage = null;
		// System.out.println("data array length: " + abData.length);
		switch (message.getType()) {
		case 0:
			int nSequenceNumber = ((abData[0] & 0xFF) << 8)
					| (abData[1] & 0xFF);
			strMessage = "Sequence Number: " + nSequenceNumber;
			break;

		case 1:
			String strText = new String(abData);
			strMessage = "Text Event: " + strText;
			break;

		case 2:
			String strCopyrightText = new String(abData);
			strMessage = "Copyright Notice: " + strCopyrightText;
			break;

		case 3:
			String strTrackName = new String(abData);
			strMessage = "Sequence/Track Name: " + strTrackName;
			break;

		case 4:
			String strInstrumentName = new String(abData);
			strMessage = "Instrument Name: " + strInstrumentName;
			break;

		case 5:
			String strLyrics = new String(abData);
			strMessage = "Lyric: " + strLyrics;
			break;

		case 6:
			String strMarkerText = new String(abData);
			strMessage = "Marker: " + strMarkerText;
			break;

		case 7:
			String strCuePointText = new String(abData);
			strMessage = "Cue Point: " + strCuePointText;
			break;

		case 0x20:
			int nChannelPrefix = abData[0] & 0xFF;
			strMessage = "MIDI Channel Prefix: " + nChannelPrefix;
			break;

		case 0x2F:
			strMessage = "End of Track";
			break;

		case 0x51:
			int nTempo = ((abData[0] & 0xFF) << 16) | ((abData[1] & 0xFF) << 8)
					| (abData[2] & 0xFF); // tempo in microseconds per beat
			float bpm = convertTempo(nTempo);
			// truncate it to 2 digits after dot
			bpm = (float) (Math.round(bpm * 100.0f) / 100.0f);
			strMessage = "Set Tempo: " + bpm + " bpm";
			break;

		case 0x54:
			// System.out.println("data array length: " + abData.length);
			strMessage = "SMTPE Offset: " + (abData[0] & 0xFF) + ":"
					+ (abData[1] & 0xFF) + ":" + (abData[2] & 0xFF) + "."
					+ (abData[3] & 0xFF) + "." + (abData[4] & 0xFF);
			break;

		case 0x58:
			strMessage = "Time Signature: " + (abData[0] & 0xFF) + "/"
					+ (1 << (abData[1] & 0xFF))
					+ ", MIDI clocks per metronome tick: " + (abData[2] & 0xFF)
					+ ", 1/32 per 24 MIDI clocks: " + (abData[3] & 0xFF);
			break;

		case 0x59:
			String strGender = (abData[1] == 1) ? "minor" : "major";
			strMessage = "Key Signature: "
					+ sm_astrKeySignatures[abData[0] + 7] + " " + strGender;
			break;

		case 0x7F:
			// TODO: decode vendor code, dump data in rows
			String strDataDump = getHexString(abData);
			strMessage = "Sequencer-Specific Meta event: " + strDataDump;
			break;

		default:
			String strUnknownDump = getHexString(abData);
			strMessage = "unknown Meta event: " + strUnknownDump;
			break;

		}
		return strMessage;
	}

	/**
	 * Normalizes the midi signature by removing the velocity from NOTE_ON and
	 * NOTE_OFF messages. Normalizes CONTROL_CHANGE, KEY_PRESSURE,
	 * POLYPHONIC_KEY_PRESSURE and PITCH_WHEEL_CHANGE messages to 0 or 127.
	 * 
	 * @param message
	 *            The original midi message
	 * @return the normalized midi message
	 */
	public static MidiMessage normalizeMidiMesage(MidiMessage message) {

		MidiMessage result = message;
		if (message instanceof ShortMessage) {

			ShortMessage shortMessage = (ShortMessage) message;
			int command = shortMessage.getCommand();
			int channel = shortMessage.getChannel();
			int commandNo = shortMessage.getData1();
			int value = shortMessage.getData2();

			String strCommand = decodeCommand(shortMessage.getCommand());

			try {
				if (strCommand == NOTE_ON || strCommand == NOTE_OFF) {
					value = 0;
					result = new ShortMessage(command, channel, commandNo,
							value);
				}

				if (strCommand == POLYPHONIC_KEY_PRESSURE
						|| strCommand == KEY_PRESSURE
						|| strCommand == PITCH_WHEEL_CHANGE) {

					if (value <= 63) {
						value = 0;
					} else {
						value = 127;
					}
					result = new ShortMessage(command, channel, commandNo,
							value);
				}

			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private static String getKeyName(int nKeyNumber) {
		if (nKeyNumber > 127) {
			return "illegal value";
		} else {
			int nNote = nKeyNumber % 12;
			int nOctave = nKeyNumber / 12;
			return sm_astrKeyNames[nNote] + (nOctave - 1);
		}
	}

	private static int get14bitValue(int nLowerPart, int nHigherPart) {
		return (nLowerPart & 0x7F) | ((nHigherPart & 0x7F) << 7);
	}

	// convert from microseconds per quarter note to beats per minute and vice
	// versa
	private static float convertTempo(float value) {
		if (value <= 0) {
			value = 0.1f;
		}
		return 60000000.0f / value;
	}

	/**
	 * Converts a byte to a HEX String
	 * 
	 * @param aByte
	 *            The byte
	 * @return the HEX value as String
	 */
	private static String getHexString(byte[] aByte) {
		StringBuffer sbuf = new StringBuffer(aByte.length * 3 + 2);

		for (int i = 0; i < aByte.length; i++) {
			sbuf.append(' ');
			sbuf.append(HEX_DIGITS[(aByte[i] & 0xF0) >> 4]);
			sbuf.append(HEX_DIGITS[aByte[i] & 0x0F]);
		}

		return new String(sbuf);
	}

	/**
	 * Closes all transmitters and receivers of the midi device
	 * 
	 * @param device
	 */
	public static void closeAllTransmittersAndReceivers(MidiDevice device) {

		for (Transmitter transmitter : device.getTransmitters()) {
			transmitter.close();
		}

		for (Receiver receiver : device.getReceivers()) {
			receiver.close();
		}
	}
}
