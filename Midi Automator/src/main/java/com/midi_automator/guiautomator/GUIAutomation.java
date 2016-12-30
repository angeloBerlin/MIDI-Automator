package com.midi_automator.guiautomator;

import org.apache.log4j.Logger;
import org.sikuli.script.Region;

import com.midi_automator.presenter.IDeActivateable;
import com.midi_automator.utils.SystemUtils;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;

/**
 * Represents a GUI automation.
 * 
 * @author aguelle
 * 
 */
public class GUIAutomation implements IDeActivateable {

	static Logger log = Logger.getLogger(GUIAutomation.class.getName());

	public static final String[] SCREENSHOT_FILE_EXTENSIONS = { "png", "PNG" };
	public static final String SCREENSHOT_FILE_TYPE = "Portable Network Graphics (png)";
	public static final String CLICKTYPE_LEFT = "left click";
	public static final String CLICKTYPE_RIGHT = "right click";
	public static final String CLICKTYPE_DOUBLE = "double click";
	public static final String TYPE_SENDKEY = "send key";
	public static final String DEFAULT_TYPE = CLICKTYPE_LEFT;

	public static final String TRIGGER_ALWAYS = "always";
	public static final String TRIGGER_ONCE_PER_CHANGE = "once per opening";
	public static final String TRIGGER_ONCE = "once";
	public static final String TRIGGER_MIDI = "MIDI: ";
	public static final String DEFAULT_TRIGGER = TRIGGER_ALWAYS;

	public static final Long DEFAULT_MIN_DELAY = 0L;
	public static final Long MIN_DELAY_MIN_VALUE = 0L;
	public static final Long MIN_DELAY_MAX_VALUE = Long.MAX_VALUE;

	public static final Long DEFAULT_TIMEOUT = 0L;
	public static final Long TIMEOUT_MIN_VALUE = 0L;
	public static final Long TIMEOUT_MAX_VALUE = Long.MAX_VALUE;

	public static final Float DEFAULT_MIN_SIMILARITY = 0.98f;
	public static final Float MIN_SIMILARITY_MIN_VALUE = 0.5f;
	public static final Float MIN_SIMILARITY_MAX_VALUE = 0.99f;

	public static final Float DEFAULT_SCAN_RATE = 1f;
	public static final Float MIN_SCAN_RATE = 0.1f;
	public static final Float MAX_SCAN_RATE = 5f;

	public static final boolean DEFAULT_IS_MOVABLE = false;

	private String imagePath;
	private String type = DEFAULT_TYPE;
	private int[] keyCodes;
	private String trigger = DEFAULT_TRIGGER;
	private String focusedProgram;
	private String midiSignature;
	private boolean active;
	private long minDelay = DEFAULT_MIN_DELAY;
	private float scanRate = DEFAULT_SCAN_RATE;
	private boolean movable = DEFAULT_IS_MOVABLE;
	private long timeout;
	private long initTime;
	private Region lastFoundRegion;

	/**
	 * Standard constructor
	 */
	public GUIAutomation() {
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
	 * @param focusedProgram
	 *            The program to focus
	 * @param delay
	 *            The minimum delay before the automation runs
	 * @param timeout
	 *            The timeout for the screenshot search
	 * @param midiSignature
	 *            The midi signature. Used if trigger is set to a midi port
	 * @param scanRate
	 *            The scan rate for the image recognition
	 * @param isMovable
	 *            Flag if the image is movable
	 * @param key
	 *            The key to press
	 */
	public GUIAutomation(String imagePath, String type, String trigger,
			String focusedProgram, long delay, long timeout,
			String midiSignature, float scanRate, boolean isMovable,
			int[] keyCodes) {
		new GUIAutomation();
		setImagePath(imagePath);
		setType(type);
		setTrigger(trigger);
		setFocusedProgram(focusedProgram);
		setMinDelay(delay);
		setTimeout(timeout);
		setMidiSignature(midiSignature);
		setScanRate(scanRate);
		setMovable(isMovable);
		setKeyCodes(keyCodes);
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

		if (trigger.equals(TRIGGER_ALWAYS) || trigger.equals(TRIGGER_ONCE)) {
			setActive(true);
		} else {
			setActive(false);
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
	 * Gets the time out for the search.
	 * 
	 * @return The search time out
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * Sets the time out for the search in seconds.
	 * 
	 * @param timeout
	 *            The search time-out
	 */
	public void setTimeout(long timeout) {
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

	/**
	 * Is active if active flag is set and timeout is not reached yet.
	 */
	@Override
	public boolean isActive() {

		long usedTime = System.currentTimeMillis() - initTime;

		if (active && (usedTime > timeout) && (initTime != 0) && (timeout != 0)) {
			log.info("Automation: " + this + " timed out after " + usedTime
					+ "ms.");
			setActive(false);
		}

		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;

		if (active) {
			initTime = System.currentTimeMillis();
		}
	}

	@Override
	public String toString() {

		return "IMAGE: \"" + SystemUtils.replaceSystemVariables(imagePath)
				+ "\" TYPE: " + type + " KEYS: "
				+ GUIAutomationConfigurationTable.keyCodesToString(keyCodes)
				+ " TRIGGER: " + trigger + " FOCUS: \"" + focusedProgram
				+ "\" MIDI: " + midiSignature + " TIMEOUT: " + timeout
				+ " SCANRATE: " + scanRate + " MOVABLE: " + movable;
	}

	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public Region getLastFoundRegion() {
		return lastFoundRegion;
	}

	public void setLastFoundRegion(Region lastFoundRegion) {
		this.lastFoundRegion = lastFoundRegion;
	}

	public float getScanRate() {
		return scanRate;
	}

	public void setScanRate(float scanRate) {
		this.scanRate = scanRate;
	}

	public int[] getKeyCodes() {
		return keyCodes;
	}

	public void setKeyCodes(int[] keyCodes) {
		this.keyCodes = keyCodes;
	}

	public String getFocusedProgram() {
		return focusedProgram;
	}

	public void setFocusedProgram(String focusedProgram) {
		this.focusedProgram = focusedProgram;
	}

}
