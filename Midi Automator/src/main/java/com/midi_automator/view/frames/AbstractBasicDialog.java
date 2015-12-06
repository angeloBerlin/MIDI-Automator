package com.midi_automator.view.frames;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Base class of dialogs with save and cancel buttons.
 * 
 * @author aguelle
 *
 */
public abstract class AbstractBasicDialog extends JFrame {

	private static final long serialVersionUID = 1L;

	private final String BUTTON_SAVE = "Save";
	private final String BUTTON_CANCEL = "Cancel";

	public static String NAME_SAVE_BUTTON = "save button";
	public static String NAME_CANCEL_BUTTON = "cancel button";

	protected JButton buttonSave;
	protected JButton buttonCancel;
	protected ActionListener saveAction;
	protected ActionListener cancelAction;

	/**
	 * Initializes some basic dialog features.
	 */
	public void init() {
		buttonSave = new JButton(BUTTON_SAVE);
		buttonSave.setName(NAME_SAVE_BUTTON);
		buttonCancel = new JButton(BUTTON_CANCEL);
		buttonCancel.setName(NAME_CANCEL_BUTTON);
	}
}
