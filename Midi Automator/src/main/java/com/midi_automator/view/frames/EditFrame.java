package com.midi_automator.view.frames;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;

@org.springframework.stereotype.Component
@Scope("prototype")
public class EditFrame extends AddFrame {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(EditFrame.class.getName());

	private final String TITLE = "Edit";
	private final int HEIGHT = 160;
	private final String LABEL_LISTENING_TO_MIDI = "Listening:";
	private JLabel midiListeningSignatureLabel;
	private JLabel midiListeningSignatureValueLabel;
	private int index;

	public static String NAME = "Edit Frame";

	/**
	 * Initializes the edit frame
	 * 
	 * @param index
	 *            The current chosen index
	 */
	public void init(int index) {
		super.init();

		this.index = index;
		setName(NAME);
		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		// change save action
		buttonSave.removeActionListener(super.saveAction);
		saveAction = new SaveAction();
		buttonSave.addActionListener(saveAction);

		createMidiListeningSignature();
		setMidiSendingSignatureValueLabelText();

		nameTextField.setText(fileListService.getEntryNameByIndex(index));
		fileTextField.setText(fileListService.getEntryFilePathByIndex(index));
		midiListeningSignatureValueLabel.setText(fileListService
				.getMidiFileListListeningSignature(index));
	}

	/**
	 * Creates the label for the midi listening signature
	 */
	private void createMidiListeningSignature() {

		GridBagConstraints c = new GridBagConstraints();
		midiListeningSignatureLabel = new JLabel(LABEL_LISTENING_TO_MIDI);
		midiListeningSignatureLabel.setPreferredSize(new Dimension(LABEL_WIDTH,
				midiListeningSignatureLabel.getPreferredSize().height));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		middlePanel.add(midiListeningSignatureLabel, c);

		midiListeningSignatureValueLabel = new JLabel(" ");
		midiListeningSignatureValueLabel.setPreferredSize(new Dimension(
				TEXT_PANE_WIDTH, midiListeningSignatureValueLabel
						.getPreferredSize().height));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		middlePanel.add(midiListeningSignatureValueLabel, c);
	}

	@Override
	protected void setMidiSendingSignatureValueLabelText() {
		midiSendingSignatureValueLabel.setText(fileListService
				.getMidiFileListSendingSignature(index));
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
			log.debug("Save edited item on index: " + (index + 1));
			fileListService.setItem(index, nameTextField.getText(),
					fileTextField.getText(),
					midiListeningSignatureValueLabel.getText(),
					midiSendingSignatureValueLabel.getText());
			new CancelAction().actionPerformed(e);
		}
	}
}
