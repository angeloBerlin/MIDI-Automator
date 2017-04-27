package com.midi_automator.view.windows.MainFrame;
import javax.swing.JLabel;

/**
 * A blinking JLabel. The blinking behavior is controlled by the injected
 * strategy.
 * 
 * @author aguelle
 *
 */
public class BlinkingJLabel extends JLabel implements IBlinkable {

	private static final long serialVersionUID = 1L;
	private IBlinkingStrategy blinkingStrategy;

	public BlinkingJLabel(String text) {
		super(text);
	}

	@Override
	public void startBlinking() {
		blinkingStrategy.startBlinking();
	}

	@Override
	public void setBlinkingStrategy(IBlinkingStrategy blinkingStrategy) {
		this.blinkingStrategy = blinkingStrategy;
	}

}
