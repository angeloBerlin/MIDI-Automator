package com.midi.automator.view;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * A cacheable JTable.
 * 
 * @author aguelle
 * 
 */
public class CacheableJTable extends JTable implements ICacheable {

	private static final long serialVersionUID = 1L;

	private ICacheable cacheableImpl;

	public CacheableJTable() {
		super();
		initialize();
	}

	public CacheableJTable(int numRows, int numColumns) {
		super(numRows, numColumns);
		initialize();
	}

	public CacheableJTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		initialize();
	}

	public CacheableJTable(TableModel dm) {
		super(dm);
		initialize();
	}

	public CacheableJTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
		initialize();
	}

	public CacheableJTable(TableModel dm, TableColumnModel cm,
			ListSelectionModel sm) {
		super(dm, cm, sm);
		initialize();
	}

	public CacheableJTable(Vector<?> rowData, Vector<?> columnNames) {
		super(rowData, columnNames);
		initialize();
	}

	/**
	 * Initializes the ICachable delegate.
	 */
	private void initialize() {
		cacheableImpl = new CacheableImpl();
	}

	@Override
	public void setCache(Object object) {
		cacheableImpl.setCache(object);
	}

	@Override
	public Object getCache() {
		return cacheableImpl.getCache();
	}
}
