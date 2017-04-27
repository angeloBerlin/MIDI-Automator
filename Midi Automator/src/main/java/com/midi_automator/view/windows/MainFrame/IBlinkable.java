package com.midi_automator.view.windows.MainFrame;


/**
 * Interface for a blinkable component
 * 
 * @author aguelle
 *
 */
public interface IBlinkable {

	/**
	 * Start blinking the component. Call from here
	 * {@link IBlinkingStrategy#startBlinking()} .
	 */
	public void startBlinking();

	/**
	 * Sets the blinking strategy
	 * 
	 * @param blinkingStrategy
	 */
	public void setBlinkingStrategy(IBlinkingStrategy blinkingStrategy);
}
