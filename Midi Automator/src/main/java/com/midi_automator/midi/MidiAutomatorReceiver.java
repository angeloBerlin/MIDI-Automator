package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import org.apache.log4j.Logger;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.MidiUtils;

/**
 * 
 * @author aguelle
 * 
 *         A special midi receiver that normalizes incoming midi data and puts
 *         out debug information.
 */
public class MidiAutomatorReceiver implements Receiver {

	static Logger log = Logger.getLogger(MidiAutomatorReceiver.class.getName());

	protected MidiAutomator application;
	protected long lastTimeStamp = 0;
	protected final long timeTolerance = 0; // in microseconds
	protected String name;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The main application
	 */
	public MidiAutomatorReceiver(MidiAutomator appl) {
		application = appl;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {

		log.debug("MIDI message received: "
				+ MidiUtils.messageToString(message));
	}

	@Override
	public void close() {
	};

	/**
	 * Interprets a message within a certain time frame and normalizes it.
	 * 
	 * @param message
	 *            The midi message
	 * @param timeStamp
	 *            The time stamp of the message
	 * @return The interpreted message
	 */
	protected MidiMessage interpreteMessage(MidiMessage message, long timeStamp) {

		MidiMessage result = null;

		long lockedTimeFrame = lastTimeStamp + timeTolerance;

		if (timeStamp > lockedTimeFrame || timeStamp == 0) {
			lastTimeStamp = timeStamp;
			result = MidiUtils.normalizeMidiMesage(message);
		}
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
