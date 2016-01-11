package com.midi_automator.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.midi_automator.Resources;

@Repository
public class MidiAutomatorProperties extends Properties {

	private static final long serialVersionUID = 1L;

	@Autowired
	private Resources resources;

	private final String FILENAME = "midiautomator.properties";

	public static final String KEY_MIDI_IN_REMOTE_DEVICE = "MIDI_IN_REMOTE_DEVICE";
	public static final String KEY_MIDI_IN_METRONOM_DEVICE = "MIDI_IN_METRONOM_DEVICE";
	public static final String KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE = "GUI_AUTOMATION_TRIGGER_DEVICE";
	public static final String KEY_MIDI_OUT_REMOTE_DEVICE = "MIDI_OUT_REMOTE_DEVICE";
	public static final String KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE = "MIDI_OUT_SWITCH_NOTIFIER_DEVICE";
	public static final String KEY_MIDI_OUT_SWITCH_ITEM_DEVICE = "MIDI_OUT_SWITCH_ITEM_DEVICE";

	public static final String KEY_PREV_MIDI_SIGNATURE = "PREV_MIDI_SIGNATURE";
	public static final String KEY_NEXT_MIDI_SIGNATURE = "NEXT_MIDI_SIGNATURE";

	public static final String INDEX_SEPARATOR = "_";
	public static final String VALUE_NULL = "-none-";

	public enum GUIAutomationKey {
		GUI_AUTOMATION_IMAGE, //
		GUI_AUTOMATION_TYPE, //
		GUI_AUTOMATION_TRIGGER, //
		GUI_AUTOMATION_MIN_DELAY, //
		GUI_AUTOMATION_TIMEOUT, //
		GUI_AUTOMATION_MIDI_SIGNATURE, //
		GUI_AUTOMATION_MIN_SIMILARITY, //
		GUI_AUTOMATION_IS_MOVABLE
	}

	/**
	 * Stores properties to the file.
	 * 
	 * @throws IOException
	 */
	public void store() throws IOException {

		Writer writer = new FileWriter(getPropertiesFilePath());
		store(writer, null);
		writer.close();
	}

	/**
	 * Loads properties from the file.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void load() throws FileNotFoundException, IOException {

		Reader reader = new FileReader(getPropertiesFilePath());
		load(reader);
		reader.close();
	}

	@Override
	public Object setProperty(String key, String value) {
		if (value == null) {
			value = "";
		}
		Object obj = super.setProperty(key, value);
		return obj;
	}

	/**
	 * Returns a Set of all key-value pairs where the key contains a given
	 * substring.
	 * 
	 * @param subKey
	 *            The keys substring
	 * @return A filtered Set of all key-value pairs
	 */
	public Set<Entry<Object, Object>> entrySet(String subKey) {
		Set<Entry<Object, Object>> keyValuePairs = entrySet();
		Set<Entry<Object, Object>> filteredKeyValuePairs = new HashSet<Entry<Object, Object>>();

		for (Entry<Object, Object> keyValuePair : keyValuePairs) {

			String key = (String) keyValuePair.getKey();
			String value = (String) keyValuePair.getValue();

			if (key.contains(subKey)) {
				Entry<Object, Object> filteredKeyValuePair = new AbstractMap.SimpleEntry<Object, Object>(
						key, value);
				filteredKeyValuePairs.add(filteredKeyValuePair);
			}
		}

		return filteredKeyValuePairs;
	}

	/**
	 * Removes a key value pair, where the key contains a given sub string.
	 * 
	 * @param subKey
	 *            The substring of the key
	 */
	public void removeKeys(String subKey) {
		Set<Entry<Object, Object>> keyValuePairs = entrySet(subKey);

		for (Entry<Object, Object> keyValuePair : keyValuePairs) {
			String key = (String) keyValuePair.getKey();
			remove(key);
		}
	}

	/**
	 * Gets the index of a property key
	 * 
	 * @param key
	 *            The property key
	 * @return The index of the key, <NULL> if there is no key
	 */
	public static int getIndexOfPropertyKey(String key) {
		String[] splittedKey = key
				.split(MidiAutomatorProperties.INDEX_SEPARATOR);
		String index = splittedKey[splittedKey.length - 1];
		return Integer.parseInt(index);
	}

	public String getPropertiesFilePath() {
		return resources.getPropertiesPath() + FILENAME;
	}
}
