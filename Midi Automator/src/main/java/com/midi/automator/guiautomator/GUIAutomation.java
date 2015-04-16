package com.midi.automator.guiautomator;

import com.midi.automator.IDeActivateable;
import com.midi.automator.utils.SystemUtils;

/**
 * Represents a GUI automation.
 * 
 * @author aguelle
 * 
 */
public class GUIAutomation implements IDeActivateable {

	private String imagePath;
	private String type;
	private String trigger;
	private String midiSignature;
	private Double timeout;
	private boolean active;
	private double minTimeOut;
	private long minDelay;

	private final double DEFAULT_MIN_TIMEOUT = 0.1;
	private final long DEFAULT_MIN_DELAY = 0;

	public static final String[] SCREENSHOT_FILE_EXTENSIONS = { "png", "PNG" };
	public static final String SCREENSHOT_FILE_TYPE = "Portable Network Graphics (png)";
	public static final String CLICKTYPE_LEFT = "left click";
	public static final String CLICKTYPE_RIGHT = "right click";
	public static final String CLICKTYPE_DOUBLE = "double click";

	public static final String CLICKTRIGGER_ALWAYS = "always";
	public static final String CLICKTRIGGER_ONCE_PER_CHANGE = "once per opening";
	public static final String CLICKTRIGGER_ONCE = "once";
	public static final String CLICKTRIGGER_MIDI = "midi";

	public static final Long MINDELAY_MIN_VALUE = 0L;
	public static final Long MINDELAY_MAX_VALUE = Long.MAX_VALUE;

	/**
	 * Standard constructor
	 */
	public GUIAutomation() {
		minTimeOut = DEFAULT_MIN_TIMEOUT;
		minDelay = DEFAULT_MIN_DELAY;
	}

	/**
	 * Constructor
	 * 
	 * @param imagePath
	 *            The path to the image of the click region
	 * @param type
	 *            The automation type
	 * @param trigger
	 *            The automation trigger
	 * @param midiSignature
	 *            The midi signature. Used if trigger is set to midi
	 * @param minDelay
	 *            The minimum delay before the automation runs
	 * @param delay
	 *            The delay for that automation in seconds
	 */
	public GUIAutomation(String imagePath, String type, String trigger,
			long delay, String midiSignature) {
		new GUIAutomation();
		setImagePath(imagePath);
		setType(type);
		setTrigger(trigger);
		setMinDelay(delay);
		setMidiSignature(midiSignature);
	}

	/**
	 * Gets the path of the screenshot image
	 * 
	 * @return path to the screenshot image
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Sets the path of the screenshot image
	 * 
	 * @param imagePath
	 *            path to the screenshot image
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * Gets the type of automation
	 * 
	 * @return the type of automation
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type of automation
	 * 
	 * @param type
	 *            the type of automation
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the trigger of the automation
	 * 
	 * @return the trigger of the automation
	 */
	public String getTrigger() {
		return trigger;
	}

	/**
	 * Sets the trigger of the automation
	 * 
	 * @param trigger
	 *            the trigger of the automation
	 */
	public void setTrigger(String trigger) {
		this.trigger = trigger;

		if (trigger.equals(CLICKTRIGGER_ALWAYS)
				|| trigger.equals(CLICKTRIGGER_ONCE)) {
			active = true;
		} else {
			active = false;
		}
	}

	/**
	 * Gets the midi signature for the trigger
	 * 
	 * @return the trigger`s midi signature
	 */
	public String getMidiSignature() {
		return midiSignature;
	}

	/**
	 * Sets the midi signature of the trigger
	 * 
	 * @param midiSignature
	 *            the trigger`s midi signature
	 */
	public void setMidiSignature(String midiSignature) {
		this.midiSignature = midiSignature;
	}

	/**
	 * Gets the time-out for the search.
	 * 
	 * @return The search time-out. The minimum time-out of 1 second is returned
	 *         if no time-out was set.
	 */
	public Double getTimeout() {
		if (timeout == null) {
			return minTimeOut;
		} else {
			return timeout;
		}
	}

	/**
	 * Sets the time-out for the search in seconds.
	 * 
	 * @param timeout
	 *            The search time-out
	 */
	public void setTimeout(Double timeout) {
		this.timeout = timeout;
	}

	/**
	 * Gets the minimum delay before running the automation.
	 * 
	 * @return delay in milliseconds
	 */
	public long getMinDelay() {
		return minDelay;
	}

	/**
	 * Sets the minimum delay before running the automation.
	 * 
	 * @param min_delay
	 *            delay in milliseconds
	 */
	public void setMinDelay(long min_delay) {
		this.minDelay = min_delay;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "IMAGE: \"" + SystemUtils.replaceSystemVariables(imagePath)
				+ "\" TYPE: " + type + " TRIGGER: " + trigger + " MIDI: "
				+ midiSignature;
	}
}
