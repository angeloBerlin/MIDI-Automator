package com.midi_automator.model;

import com.midi_automator.utils.SystemUtils;

/**
 * Represents an entry of the set list.
 * 
 * @author aguelle
 *
 */
public class SetListItem {

	public SetListItem(String name) {
		super();
		this.name = name;
	}

	public SetListItem(String name, String filePath) {
		super();
		this.name = name;
		this.filePath = filePath;
	}

	public SetListItem(String name, String filePath, String midiSignature) {
		super();
		this.name = name;
		this.filePath = filePath;
		this.midiSignature = midiSignature;
	}

	private String name;
	private String filePath;
	private String midiSignature;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return SystemUtils.replaceSystemVariables(filePath);
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMidiSignature() {
		return midiSignature;
	}

	public void setMidiSignature(String midiSignature) {
		this.midiSignature = midiSignature;
	}
}
