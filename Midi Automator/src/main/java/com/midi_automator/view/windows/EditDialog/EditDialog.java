package com.midi_automator.view.windows.EditDialog;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

import com.midi_automator.view.windows.AddDialog.AddDialog;

@org.springframework.stereotype.Component
public class EditDialog extends AddDialog {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(EditDialog.class.getName());

	private final String TITLE = "Edit";
	private final int HEIGHT = 200;
	private final String LABEL_LISTENING_TO_MIDI = "Listening:";
	private JLabel midiListeningSignatureValueLabel;
	private int index;

	public static String NAME = "Edit Dialog";
	public static String NAME_MIDI_LISTENING_SIGNATURE_VALUE_LABEL = "MIDI listening label";

	/**
	 * Initializes the edit frame
	 * 
	 * @param index
	 *            The current chosen index
	 */
	public void init(int index) {

		if (!initialized) {
			super.init();

			setName(NAME);
			setTitle(TITLE);
			setSize(WIDTH, HEIGHT);

			createMidiListeningSignature();
			initMidiSendingSignatureValueLabelText();
		}

		this.index = index;
		nameTextField.setText(fileListService.getEntryNameByIndex(index));
		fileTextField.setText(fileListService.getEntryFilePathByIndex(index));
		programTextField.setText(fileListService
				.getEntryProgramPathByIndex(index));
		String midiListeningSignature = fileListService
				.getMidiFileListListeningSignature(index);
		midiListeningSignatureValueLabel.setText(midiListeningSignature);
		String midiSendingSignature = fileListService
				.getMidiFileListSendingSignature(index);
		midiSendingSignatureValueLabel.setText(midiSendingSignature);

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
	protected void save() {
		fileListService.setItem(index, nameTextField.getText(),
				fileTextField.getText(), programTextField.getText(),
				midiListeningSignatureValueLabel.getText(),
				midiSendingSignatureValueLabel.getText());
		close();
	}

	@Override
	protected void initMidiSendingSignatureValueLabelText() {

		midiSendingSignatureValueLabel.setText(fileListService
				.getMidiFileListSendingSignature(index));
	}
}
