package com.midi_automator.model;

import com.midi_automator.utils.SystemUtils;

/**
 * Represents an entry of the set list.
 * 
 * @author aguelle
 *
 */
public class SetListItem {

	private String name;
	private String filePath;
	private String midiListeningSignature;
	private String midiSendingSignature;

	public SetListItem(String name) {
		super();
		this.name = name;
	}

	public SetListItem(String name, String filePath) {
		super();
		this.name = name;
		this.filePath = filePath;
	}

	public SetListItem(String name, String filePath,
			String midiListeningSignature, String midiSendingSignature) {
		super();
		this.name = name;
		this.filePath = filePath;
		this.midiListeningSignature = midiListeningSignature;
		this.midiSendingSignature = midiSendingSignature;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		if (filePath == null) {
			return "";
		}
		return SystemUtils.replaceSystemVariables(filePath);
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMidiListeningSignature() {
		return midiListeningSignature;
	}

	public void setMidiListeningSignature(String midiSignature) {
		this.midiListeningSignature = midiSignature;
	}

	public String getMidiSendingSignature() {
		return midiSendingSignature;
	}

	public void setMidiSendingSignature(String midiSendingSignature) {
		this.midiSendingSignature = midiSendingSignature;
	}
}
