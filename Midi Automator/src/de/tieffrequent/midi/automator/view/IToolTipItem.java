package de.tieffrequent.midi.automator.view;

/**
 * Interface for JList<String> items containing tooltips
 * 
 * @author aguelle
 * 
 */
public interface IToolTipItem {

	/**
	 * Returns the tooltip
	 * 
	 * @return the tooltip
	 */
	public String getToolTipText();

	/**
	 * Sets the tooltip text
	 * 
	 * @param text
	 *            The tooltip text
	 */
	public void setToolTipText(String text);

	/**
	 * Returns the value of the list item
	 * 
	 * @return the value
	 */
	public String getValue();

	/**
	 * Sets the value of the list item
	 * 
	 * @param value
	 *            The value
	 */
	public void setValue(String value);

	/**
	 * Transforms the item to String representation
	 * 
	 * @return a readable String
	 */
	public String toString();
}
