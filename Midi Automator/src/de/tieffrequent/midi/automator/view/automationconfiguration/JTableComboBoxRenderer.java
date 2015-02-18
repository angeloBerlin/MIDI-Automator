package de.tieffrequent.midi.automator.view.automationconfiguration;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * CellRenderer for a JComboBox.
 * 
 * @author aguelle
 * 
 */
public class JTableComboBoxRenderer extends JComboBox<String> implements
		TableCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param items
	 *            The items of the JComboBox
	 */
	public JTableComboBoxRenderer(String[] items) {
		super(items);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		setOpaque(true);

		if (table.isRowSelected(row)) {
			setBackground(table.getSelectionBackground());
		} else {
			setBackground(table.getBackground());
		}

		setSelectedItem(value);
		return this;
	}
}
