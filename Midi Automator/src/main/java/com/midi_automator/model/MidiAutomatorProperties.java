package com.midi_automator.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.midi_automator.Resources;

@Repository
public class MidiAutomatorProperties extends Properties {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(MidiAutomatorProperties.class
			.getName());

	@Autowired
	private Resources resources;

	public static final String KEY_VERSION = "VERSION";

	public static final String KEY_MIDI_IN_REMOTE_DEVICE = "MIDI_IN_REMOTE_DEVICE";
	public static final String KEY_MIDI_IN_METRONOM_DEVICE = "MIDI_IN_METRONOM_DEVICE";
	public static final String KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE = "GUI_AUTOMATION_TRIGGER_DEVICE";
	public static final String KEY_MIDI_OUT_REMOTE_DEVICE = "MIDI_OUT_REMOTE_DEVICE";
	public static final String KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE = "MIDI_OUT_SWITCH_NOTIFIER_DEVICE";
	public static final String KEY_MIDI_OUT_SWITCH_ITEM_DEVICE = "MIDI_OUT_SWITCH_ITEM_DEVICE";

	public static final String KEY_PREV_MIDI_SIGNATURE = "PREV_MIDI_SIGNATURE";
	public static final String KEY_NEXT_MIDI_SIGNATURE = "NEXT_MIDI_SIGNATURE";

	public static final String KEY_LAST_FILE_CHOOSER_DIR = "LAST_FILE_CHOOSER_DIR";
	public static final String KEY_LAST_PROGRAM_CHOOSER_DIR = "LAST_PROGRAM_CHOOSER_DIR";
	public static final String KEY_LAST_SCREENSHOT_CHOOSER_DIR = "LAST_SCREENSHOT_CHOOSER_DIR";

	public static final String INDEX_SEPARATOR = "_";
	public static final String VALUE_NULL = "-none-";
	public static final String KEY_CODES_DELIMITER = " ";

	public enum GUIAutomationKey {
		GUI_AUTOMATION_IMAGE, //
		GUI_AUTOMATION_TYPE, //
		GUI_AUTOMATION_KEY_CODES, //
		GUI_AUTOMATION_TRIGGER, //
		GUI_AUTOMATION_MIN_DELAY, //
		GUI_AUTOMATION_TIMEOUT, //
		GUI_AUTOMATION_MIDI_SIGNATURE, //
		GUI_AUTOMATION_MIN_SIMILARITY, //
		GUI_AUTOMATION_SCAN_RATE, //
		GUI_AUTOMATION_IS_MOVABLE
	}

	/**
	 * Stores properties to the file order by keys.
	 * 
	 * @throws IOException
	 */
	public void store() throws IOException {

		Enumeration<Object> keysEnum = super.keys();
		Vector<Object> keyList = new Vector<Object>();

		while (keysEnum.hasMoreElements()) {
			keyList.add(keysEnum.nextElement());
		}

		Collections.sort(keyList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		FileWriter fileWriter = new FileWriter(getPropertiesFilePath());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		for (Object key : keyList) {
			String strKey = (String) key;
			bufferedWriter.write(strKey + "=" + getProperty(strKey));
			bufferedWriter.write("\r\n");
		}
		bufferedWriter.close();
	}

	/**
	 * Loads properties from the file.
	 *
	 * @throws IOException
	 */
	public void load() throws IOException {

		Reader reader = null;
		String filepath = getPropertiesFilePath();

		try {
			reader = new FileReader(filepath);
		} catch (FileNotFoundException e) {

			File f = new File(filepath);
			f.getParentFile().mkdirs();
			f.createNewFile();
			reader = new FileReader(filepath);
		}

		load(reader);
		reader.close();
	}

	@Override
	public Object setProperty(String key, String value) {

		if (value == null) {
			value = MidiAutomatorProperties.VALUE_NULL;
		} else {
			if (value.equals("null") || value.equals("")) {
				value = MidiAutomatorProperties.VALUE_NULL;
			}
		}

		Object obj = super.setProperty(key, value);
		return obj;
	}

	/**
	 * Saves a common property to the properties file.
	 * 
	 * @param key
	 *            The key of the property
	 * 
	 * @param value
	 *            The value of the property
	 */
	public void setProperty(String key, Object value) {

		String strValue = String.valueOf(value);
		setProperty(key, strValue);

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
	 * Migrates the properties file to the current version
	 */
	public void migrate() {
		setProperty(KEY_VERSION, resources.getVersion());

		// 1.6
		// Convert GUI_AUTOMATION_MIN_SIMILARITY_0 to
		// GUI_AUTOMATION_MIN_SIMILARITY
		String minSimilarity = (String) get("GUI_AUTOMATION_MIN_SIMILARITY_0");

		if (minSimilarity != null) {
			setProperty(
					GUIAutomationKey.GUI_AUTOMATION_MIN_SIMILARITY.toString(),
					minSimilarity);
		}

		// remove all GUI_AUTOMATION_MIN_SIMILARITY_ keys
		removeKeys("GUI_AUTOMATION_MIN_SIMILARITY_");

		try {
			store();
		} catch (IOException e) {
			log.error(
					"Storing migration of "
							+ resources.getMidiAutomatorPropertiesFileName()
							+ " failed.", e);
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
		return resources.getPropertiesPath() + File.separator
				+ resources.getMidiAutomatorPropertiesFileName();
	}
}
