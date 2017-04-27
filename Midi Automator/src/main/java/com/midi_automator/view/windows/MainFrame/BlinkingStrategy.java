package com.midi_automator.view.windows.MainFrame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

import org.apache.log4j.Logger;

public class BlinkingStrategy implements IBlinkingStrategy {

	protected Logger log = Logger.getLogger(this.getClass().getName());

	private Timer blinkTimer;
	private JComponent blinkingComponent;
	private Color blinkingColor;
	private int blinkingRate;
	private int blinkingAmount;

	/**
	 * Constructor for the blinking strategy
	 * 
	 * @param blinkingComponent
	 *            The blinking component
	 * @param blinkingColor
	 *            The blinking color
	 * @param blinkingRate
	 *            The blinking rate in ms
	 * @param blinkingAmount
	 *            The amount of blinks
	 */
	public BlinkingStrategy(JComponent blinkingComponent, Color blinkingColor,
			int blinkingRate, int blinkingAmount) {
		super();
		this.blinkingComponent = blinkingComponent;
		this.blinkingColor = blinkingColor;
		this.blinkingRate = blinkingRate;
		this.blinkingAmount = blinkingAmount;

		this.blinkingComponent.setOpaque(true);
		blinkTimer = new Timer(this.blinkingRate, new BlinkingTimerListener());
		blinkTimer.setInitialDelay(0);
	}

	@Override
	public Color getBlinkColor() {
		return blinkingColor;
	}

	@Override
	public void setBlinkColor(Color blinkColor) {
		this.blinkingColor = blinkColor;
	}

	@Override
	public int getBlinkingRate() {
		return blinkingRate;
	}

	@Override
	public void setBlinkingRate(int blinkingRate) {
		this.blinkingRate = blinkingRate;
	}

	@Override
	public int getBlinkingAmount() {
		return blinkingAmount;
	}

	@Override
	public void setBlinkingAmount(int blinkingAmount) {
		this.blinkingAmount = blinkingAmount;
	}

	@Override
	public void startBlinking() {
		blinkTimer.start();
	}

	/**
	 * Stops blinking
	 */
	private void stopBlinking() {
		blinkTimer.stop();
	}

	/**
	 * Timer listener blinks the component once and stops the timer.
	 * 
	 * @author aguelle
	 *
	 */
	private class BlinkingTimerListener implements ActionListener {

		private Color defaultBackgroundColor;
		private boolean isBlinking = true;
		private int timerCount;

		public BlinkingTimerListener() {
			defaultBackgroundColor = blinkingComponent.getBackground();
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			timerCount++;
			if (isBlinking) {
				log.debug(blinkingComponent.getName() + " blinking on.");
				blinkingComponent.setBackground(blinkingColor);
			} else {
				log.debug(blinkingComponent.getName() + " blinking off.");
				blinkingComponent.setBackground(defaultBackgroundColor);
			}
			isBlinking = !isBlinking;

			if (timerCount == 2 * blinkingAmount) {
				timerCount = 0;
				stopBlinking();
			}
		}
	}
}
