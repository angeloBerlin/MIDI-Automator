package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable;
import javax.swing.JButton;

/**
 * A button with metadata row and column
 * 
 * @author aguelle
 * 
 */
public class JTableButton extends JButton {

	private static final long serialVersionUID = 1L;

	private int column;
	private int row;

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

}
