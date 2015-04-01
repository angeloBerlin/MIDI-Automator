package com.midi.automator.view;

import javax.swing.JTextPane;

/**
 * This is a label that can be formatted by HTML.
 * 
 * @author aguelle
 * @date 22-03-14
 */
public class HTMLLabel extends JTextPane {

	private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor
	 */
	public HTMLLabel() {
		this("");
	}

	/**
	 * Constructor
	 * 
	 * @param text
	 *            text of the label
	 */
	public HTMLLabel(String text) {
		super();
		setEditable(false);
		setContentType("text/html");
		setOpaque(false);
		this.setText(text);
	}

	@Override
	public void setText(String text) {
		super.setText("<html>" + text + "</html>");
	}
}
