package com.midi_automator.view.automationconfiguration;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 * CellEditor for JComboBox in a JTable.
 * 
 * @author aguelle
 * 
 */
public class JTableComboBoxEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param items
	 *            The items of the JComboBox.
	 */
	public JTableComboBoxEditor(String[] items) {
		super(new JComboBox<String>(items));
	}

	/**
	 * Constructor
	 * 
	 * @param comboBox
	 *            A JComboBox
	 */
	public JTableComboBoxEditor(JComboBox<?> comboBox) {
		super(comboBox);
	}

}
