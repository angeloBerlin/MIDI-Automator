package de.tieffrequent.midi.automator.view.automationconfiguration;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.table.TableCellEditor;

/**
 * This is a cell editor with a JSpinner.
 * 
 * @author aguelle
 * 
 */
public class JTableSpinnerEditor extends AbstractCellEditor implements
		TableCellEditor {

	private static final long serialVersionUID = 1L;
	private final JSpinner SPINNER = new JSpinner();

	/**
	 * Constructor
	 * 
	 * @param model
	 *            The spinner model
	 */
	public JTableSpinnerEditor(SpinnerModel model) {
		SPINNER.setModel(model);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		SPINNER.setValue(value);
		return SPINNER;
	}

	@Override
	public boolean isCellEditable(EventObject evt) {
		if (evt instanceof MouseEvent) {
			return ((MouseEvent) evt).getClickCount() >= 2;
		}
		return true;
	}

	@Override
	public Object getCellEditorValue() {
		return SPINNER.getValue();
	}
}