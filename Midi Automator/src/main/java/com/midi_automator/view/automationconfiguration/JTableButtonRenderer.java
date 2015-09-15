package com.midi_automator.view.automationconfiguration;

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
	 * Constructor
	 * 
	 * @param buttonText
	 *            The text of the button
	 */
	public JTableButtonRenderer(String buttonText) {
		super();
		button = new JTableButton();
		button.setText(buttonText);
		add(button);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		return this;
	}
}