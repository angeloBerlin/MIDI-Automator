package de.tieffrequent.midi.automator.view.automationconfiguration;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * CellRenderer for a JPanel with JButton.
 * 
 * @author aguelle
 * 
 */
class JTableButtonRenderer extends JPanel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	JTableButton button;

	/**
	 * Standard constructor
	 */
	public JTableButtonRenderer() {
		super();
		button = new JTableButton();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		if (table.isRowSelected(row)) {
			setBackground(table.getSelectionBackground());
		} else {
			setBackground(table.getBackground());
		}

		button.setText(value.toString());
		add(button);
		return this;
	}
}