package de.tieffrequent.midi.automator.view.automationconfiguration;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 * CellEditor for JButton in a JTable cell.
 * 
 * @author aguelle
 * 
 */
class JTableButtonEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 1L;
	JPanel panel;
	JTableButton button;
	String txt;

	/**
	 * Standard constructor
	 */
	public JTableButtonEditor() {
		super();
		button = new JTableButton();
		panel = new JPanel();
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		txt = (value == null) ? "" : value.toString();
		button.setText(txt);
		button.setRow(row);
		button.setColumn(column);
		panel.add(button);

		if (table.isRowSelected(row)) {
			panel.setBackground(table.getSelectionBackground());
		} else {
			panel.setBackground(table.getBackground());
		}

		table.repaint();
		return panel;
	}

	/**
	 * Adds an ActionListener to the button
	 * 
	 * @param l
	 *            The ActionListener
	 */
	public void addActionListener(ActionListener l) {
		button.addActionListener(l);
	}
}