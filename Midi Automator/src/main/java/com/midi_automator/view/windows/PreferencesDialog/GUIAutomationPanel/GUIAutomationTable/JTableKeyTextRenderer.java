package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable;

import java.util.regex.Pattern;

import com.midi_automator.utils.CommonUtils;

/**
 * CellRenderer for key code representation
 * 
 * @author aguelle
 * 
 */
class JTableKeyTextRenderer extends JTableNonEditableRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public void setText(String text) {

		String keys = "";

		if (text != null) {
			if (!text.equals("")) {

				String[] keyCodeStrings = text.split(Pattern
						.quote(GUIAutomationTable.KEYS_DELIMITER));

				int[] keyCodes = CommonUtils
						.stringArrayToIntArray(keyCodeStrings);

				keys = GUIAutomationTable
						.keyCodesToString(keyCodes);
			}
		}

		super.setText(keys);
	}
}