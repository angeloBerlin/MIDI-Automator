package de.tieffrequent.midi.automator.view.automationconfiguration;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Defines the look of a non editable JTable cell.
 * 
 * @author aguelle
 * 
 */
public class JTableNonEditableRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component component = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		component.setForeground(Color.gray);
		return component;
	}
}
