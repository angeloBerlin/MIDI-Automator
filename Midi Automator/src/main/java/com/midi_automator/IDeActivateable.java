package com.midi_automator;

/**
 * Gives the implemented class an active state
 * 
 * @author aguelle
 * 
 */
public interface IDeActivateable {

	/**
	 * Returns if the object is marked as active
	 * 
	 * @return <TRUE> if it is active, <FALSE> if it is inactive
	 */
	public boolean isActive();

	/**
	 * Sets the active state
	 * 
	 * @param active
	 *            the active state
	 */
	public void setActive(boolean active);
}
