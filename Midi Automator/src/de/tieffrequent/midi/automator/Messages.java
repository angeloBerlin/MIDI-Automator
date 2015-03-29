package de.tieffrequent.midi.automator;

public class Messages {

	public static final String MSG_FILE_LIST_NOT_FOUND = "The file <b>%s</b> could not be found.";
	public static final String MSG_FILE_LIST_NOT_READABLE = "The file <b>%s</b> is not readable.";
	public static final String MSG_FILE_LIST_EMPTY = "File list is empty.";
	public static final String MSG_FILE_LIST_TOO_BIG = "The file <b>%s</b> cannot be loaded because it has more than 128 entries.";
	public static final String MSG_OPENING_FILE = "The file <b>%s</b> will be opened...";
	public static final String MSG_OPENING_ENTRY = "Opening <b>%s</b>...";
	public static final String MSG_FILE_COULD_NOT_BE_OPENED = "No program found to open file <b>%s</b>.";
	public static final String MSG_FILE_NOT_FOUND = "File <b>%s</b> could not be found.";
	public static final String MSG_MIDI_DEVICE_NOT_AVAILABLE = "The MIDI device <b>%s</b> is unavailable.";
	public static final String MSG_MIDI_LEARN_MODE = "Waiting for midi input...";
	public static final String MSG_MIDI_LEARN_SUCCESS = "Midi message <b>%s</b> learned.";
	public static final String MSG_MIDI_UNLEARNED = "Midi message for <b>%s</b> unlearned.";
	public static final String MSG_DUPLICATE_MIDI_SIGNATURE = "The midi message <b>%s</b> is already set for a function. Please use another one.";
	public static final String MSG_FILE_LIST_IS_FULL = "The entry <b>%s</b> could not be added because the list contains already the maximum of 128 entries.";
}
