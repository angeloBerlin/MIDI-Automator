package com.midi.automator.view.automationconfiguration;

import javax.swing.table.DefaultTableModel;

/**
 * The model for the automation configuration table.
 * 
 * @author aguelle
 * 
 */
public class ConfigurationTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	public static final int COLUMN_INDEX_IMAGE = 0;
	public static final int COLUMN_INDEX_TYPE = 1;
	public static final int COLUMN_INDEX_TRIGGER = 2;
	public static final int COLUMN_INDEX_MIN_DELAY = 3;
	public static final int COLUMN_INDEX_MIDISIGNATURE = 4;
	public static final int COLUMN_INDEX_BROWSE_BUTTON = 5;

	@Override
	public boolean isCellEditable(int row, int column) {
		return column != COLUMN_INDEX_MIDISIGNATURE;
	}
}
