package com.midi_automator.view;
import java.awt.Color;

/**
 * Interface for a blinking strategy.
 * 
 * @author aguelle
 *
 */
public interface IBlinkingStrategy {

	public Color getBlinkColor();

	public void setBlinkColor(Color blinkColor);

	public int getBlinkingRate();

	public void setBlinkingRate(int blinkingRate);

	public int getBlinkingAmount();

	public void setBlinkingAmount(int blinkingAmount);

	/**
	 * Start blinking the component
	 */
	public void startBlinking();
}
