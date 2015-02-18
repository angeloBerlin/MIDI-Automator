package de.tieffrequent.midi.automator.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import de.tieffrequent.midi.automator.IApplication;
import de.tieffrequent.midi.automator.utils.MidiUtils;

/**
 * 
 * @author aguelle
 * 
 *         A special midi receiver that normalizes incoming midi data and puts
 *         out debug information.
 */
public class MidiOpenerReceiver implements Receiver {

	protected IApplication application;
	protected long lastTimeStamp = 0;
	protected final long timeTolerance = 1000000; // in microseconds

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The main application
	 */
	public MidiOpenerReceiver(IApplication appl) {
		application = appl;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {

		if (application.isInDebugMode()) {
			System.out.println(this.getClass().getName() + " received: "
					+ timeStamp + " " + MidiUtils.messageToString(message));
		}
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
}
