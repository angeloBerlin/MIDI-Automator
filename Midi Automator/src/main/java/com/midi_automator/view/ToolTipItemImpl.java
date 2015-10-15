package com.midi_automator.view;

/**
 * Implementation for a transferable JList item with a tool tip
 * 
 * @author aguelle
 * 
 */
public class ToolTipItemImpl implements IToolTipItem {

	private String value;
	private String tooltip;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            The value
	 */
	public ToolTipItemImpl(String value) {
		super();
		this.value = value;
	}

	@Override
	public String getToolTipText() {
		return tooltip;
	}

	@Override
	public void setToolTipText(String text) {
		tooltip = text;

	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
