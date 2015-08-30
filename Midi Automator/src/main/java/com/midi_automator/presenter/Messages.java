package com.midi_automator.presenter;

import java.util.HashMap;
import java.util.Map;

public class Messages {

	public static final String MSG_FILE_LIST_NOT_FOUND = "The file <b>\"%s\"</b> could not be found.";
	public static final String MSG_FILE_LIST_NOT_READABLE = "The file <b>\"%s\"</b> is not readable.";
	public static final String MSG_FILE_LIST_EMPTY = "File list is empty.";
	public static final String MSG_FILE_LIST_TOO_BIG = "The file <b>\"%s\"</b> cannot be loaded because it has more than 128 entries.";
	public static final String MSG_OPENING_FILE = "The file <b>\"%s\"</b> will be opened...";
	public static final String MSG_OPENING_ENTRY = "Opening <b>\"%s\"</b>...";
	public static final String MSG_FILE_COULD_NOT_BE_OPENED = "No program found to open file <b>\"%s\"</b>.";
	public static final String MSG_FILE_NOT_FOUND = "File <b>\"%s\"</b> could not be found.";
	public static final String MSG_MIDI_DEVICE_NOT_AVAILABLE = "The MIDI device <b>%s</b> is unavailable.";
	public static final String MSG_MIDI_LEARN_MODE = "Waiting for midi input...";
	public static final String MSG_MIDI_LEARN_SUCCESS = "Midi message <b>%s</b> learned.";
	public static final String MSG_MIDI_UNLEARNED = "Midi message for <b>%s</b> unlearned.";
	public static final String MSG_DUPLICATE_MIDI_SIGNATURE = "The midi message <b>%s</b> is already set for a function. Please use another one.";
	public static final String MSG_FILE_LIST_IS_FULL = "The entry <b>%s</b> could not be added because the list contains already the maximum of 128 entries.";

	public static Map<String, String> builtMessages = new HashMap<String, String>();
	public static final String KEY_ERROR_DUPLICATE_MIDI_SIGNATURE = "KEY_ERROR_DUPLICATE_MIDI_SIGNATURE";
	public static final String KEY_INFO_ENTRY_OPENED = "KEY_INFO_ENTRY_OPENED";
	public static final String KEY_ERROR_MIDO_FILE_TOO_BIG = "KEY_ERROR_MIDO_FILE_TOO_BIG";
	public static final String KEY_ERROR_MIDO_FILE_NOT_FOUND = "KEY_ERROR_MIDO_FILE_NOT_FOUND";
	public static final String KEY_ERROR_MIDO_FILE_IO = "KEY_ERROR_MIDO_FILE_IO";
	public static final String KEY_ERROR_ITEM_FILE_NOT_FOUND = "KEY_ERROR_ITEM_FILE_NOT_FOUND";
	public static final String KEY_ERROR_ITEM_FILE_IO = "KEY_ERROR_ITEM_FILE_IO";
	public static final String KEY_ERROR_PROPERTIES_FILE_NOT_FOUND = "KEY_ERROR_PROPERTIES_FILE_NOT_FOUND";
	public static final String KEY_ERROR_PROPERTIES_FILE_NOT_READABLE = "KEY_ERROR_PROPERTIES_FILE_NOT_READABLE";
	public static final String KEY_ERROR_TOO_MUCH_ENTRIES = "KEY_ERROR_TOO_MUCH_ENTRIES";
	public static final String KEY_MIDI_IN_REMOTE_DEVICE_UNVAILABLE = "KEY_MIDI_IN_REMOTE_DEVICE_UNVAILABLE";
	public static final String KEY_MIDI_IN_METRONOM_DEVICE_UNVAILABLE = "KEY_MIDI_IN_METRONOM_DEVICE_UNVAILABLE";
	public static final String KEY_MIDI_OUT_REMOTE_DEVICE_UNAVAILABLE = "KEY_MIDI_OUT_REMOTE_DEVICE_UNAVAILABLE";
	public static final String KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE_UNAVAILABLE = "KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE_UNAVAILABLE";
	public static final String KEY_MIDI_OUT_SWITCH_ITEM_DEVICE_UNAVAILABLE = "KEY_MIDI_OUT_SWITCH_ITEM_DEVICE_UNAVAILABLE";
}
