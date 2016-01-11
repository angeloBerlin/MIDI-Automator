package com.midi_automator.guiautomator;

import org.apache.log4j.Logger;
import org.sikuli.script.Region;

import com.midi_automator.presenter.IDeActivateable;
import com.midi_automator.utils.SystemUtils;

/**
 * Represents a GUI automation.
 * 
 * @author aguelle
 * 
 */
public class GUIAutomation implements IDeActivateable {

	static Logger log = Logger.getLogger(GUIAutomator.class.getName());

	public static final String[] SCREENSHOT_FILE_EXTENSIONS = { "png", "PNG" };
	public static final String SCREENSHOT_FILE_TYPE = "Portable Network Graphics (png)";
	public static final String CLICKTYPE_LEFT = "left click";
	public static final String CLICKTYPE_RIGHT = "right click";
	public static final String CLICKTYPE_DOUBLE = "double click";

	public static final String CLICKTRIGGER_ALWAYS = "always";
	public static final String CLICKTRIGGER_ONCE_PER_CHANGE = "once per opening";
	public static final String CLICKTRIGGER_ONCE = "once";
	public static final String CLICKTRIGGER_MIDI = "MIDI: ";

	public static final Long DEFAULT_MIN_DELAY = 0L;
	public static final Long MIN_DELAY_MIN_VALUE = 0L;
	public static final Long MIN_DELAY_MAX_VALUE = Long.MAX_VALUE;

	public static final Long DEFAULT_TIMEOUT = 0L;
	public static final Long TIMEOUT_MIN_VALUE = 0L;
	public static final Long TIMEOUT_MAX_VALUE = Long.MAX_VALUE;

	public static final Float DEFAULT_MIN_SIMILARITY = 0.98f;
	public static final Float MIN_SIMILARITY_MIN_VALUE = 0.5f;
	public static final Float MIN_SIMILARITY_MAX_VALUE = 0.99f;

	public static final boolean DEFAULT_IS_MOVABLE = false;

	private String imagePath;
	private String type;
	private String trigger;
	private String midiSignature;
	private boolean active;
	private long minDelay = DEFAULT_MIN_DELAY;
	private float minSimilarity = DEFAULT_MIN_SIMILARITY;
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
	 * @param delay
	 *            The minimum delay before the automation runs
	 * @param timeout
	 *            The timeout for the screenshot search
	 * @param midiSignature
	 *            The midi signature. Used if trigger is set to a midi port
	 * @param minSimilarity
	 *            The minimum similarity for the image recognition
	 * @param isMovable
	 *            Flag if the image is movable
	 */
	public GUIAutomation(String imagePath, String type, String trigger,
			long delay, long timeout, String midiSignature,
			float minSimilarity, boolean isMovable) {
		new GUIAutomation();
		setImagePath(imagePath);
		setType(type);
		setTrigger(trigger);
		setMinDelay(delay);
		setTimeout(timeout);
		setMidiSignature(midiSignature);
		setMinSimilarity(minSimilarity);
		setMovable(isMovable);
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
				+ "\" TYPE: " + type + " TRIGGER: " + trigger + " MIDI: "
				+ midiSignature;
	}

	public float getMinSimilarity() {
		return minSimilarity;
	}

	public void setMinSimilarity(float minSimilarity) {
		this.minSimilarity = minSimilarity;
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
}
