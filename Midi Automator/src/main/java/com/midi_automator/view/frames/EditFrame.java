package com.midi_automator.view.frames;

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
	private final int HEIGHT = 200;
	private final String LABEL_LISTENING_TO_MIDI = "Listening:";
	private JLabel midiListeningSignatureValueLabel;
	private int index;

	public static String NAME = "Edit Frame";
	public static String NAME_MIDI_LISTENING_SIGNATURE_VALUE_LABEL = "MIDI listening label";

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

		// change save action
		buttonSave.removeActionListener(super.saveAction);
		saveAction = new SaveAction();
		buttonSave.addActionListener(saveAction);

		createMidiListeningSignature();
		initMidiSendingSignatureValueLabelText();

		nameTextField.setText(fileListService.getEntryNameByIndex(index));
		fileTextField.setText(fileListService.getEntryFilePathByIndex(index));
		programTextField.setText(fileListService
				.getEntryProgramPathByIndex(index));
		midiListeningSignatureValueLabel.setText(fileListService
				.getMidiFileListListeningSignature(index));
	}

	/**
	 * Creates the label for the midi listening signature
	 */
	private void createMidiListeningSignature() {

		midiListeningSignatureValueLabel = createMetaLabel(
				LABEL_LISTENING_TO_MIDI,
				NAME_MIDI_LISTENING_SIGNATURE_VALUE_LABEL, META_LABEL_WIDTH,
				TEXT_PANE_WIDTH);
	}

	@Override
	protected void initMidiSendingSignatureValueLabelText() {

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
					fileTextField.getText(), programTextField.getText(),
					midiListeningSignatureValueLabel.getText(),
					midiSendingSignatureValueLabel.getText());
			new CancelAction().actionPerformed(e);
		}
	}
}
