package de.tieffrequent.midi.automator.test.helper;

public class SikuliHelper {

	static public String translateUSToKeyboard(String input) {
		input = input.replace("/", "&");
		return input;
	}

}
