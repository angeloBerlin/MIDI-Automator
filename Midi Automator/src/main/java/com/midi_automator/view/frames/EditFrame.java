package com.midi_automator.view.frames;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.midi_automator.MidiAutomator;

public class EditFrame extends AddFrame {

	private static final long serialVersionUID = 1L;

	private final String TITLE = "Edit";
	private final int HEIGHT = 150;
	private final String LABEL_MIDI = "Midi:";
	private JLabel midiSignatureLabel;
	private JLabel midiSignatureValueLabel;
	private int index;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The main application
	 * @param programFrame
	 *            The main programFrame
	 * @param index
	 *            The current chosen index
	 * @throws HeadlessException
	 *             If no GUI available
	 */
	public EditFrame(MidiAutomator application, JFrame programFrame, int index)
			throws HeadlessException {
		super(application, programFrame);
		this.index = index;

		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		// change save action
		buttonSave.removeActionListener(super.saveListener);
		saveListener = new SaveAction();
		buttonSave.addActionListener(saveListener);

		createMidiSignature();

		nameTextField.setText(application.getEntryNameByIndex(index));
		fileTextField.setText(application.getEntryPathByIndex(index));
		midiSignatureValueLabel.setText(application.getMidiSignature(index));
	}

	/**
	 * Creates the label for the midi signature
	 */
	private void createMidiSignature() {

		GridBagConstraints c = new GridBagConstraints();
		midiSignatureLabel = new JLabel(LABEL_MIDI);
		midiSignatureLabel.setPreferredSize(new Dimension(LABEL_WIDTH,
				midiSignatureLabel.getPreferredSize().height));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		middlePanel.add(midiSignatureLabel, c);

		midiSignatureValueLabel = new JLabel(" ");
		midiSignatureValueLabel.setPreferredSize(new Dimension(TEXT_PANE_WIDTH,
				midiSignatureValueLabel.getPreferredSize().height));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		middlePanel.add(midiSignatureValueLabel, c);
	}

	/**
	 * Closes the frame, saves the entry and reloads the entry list
	 * 
	 * @author aguelle
	 * 
	 */
	class SaveAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			application.setItem(index, nameTextField.getText(),
					fileTextField.getText(), midiSignatureValueLabel.getText());
			new CancelAction().actionPerformed(e);
		}
	}
}
