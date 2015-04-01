package com.midi.automator.view;

import java.awt.event.MouseAdapter;

import de.tieffrequent.midi.automator.IDeActivateable;

/**
 * A MouseAdapter with a boolean "active" field.
 * 
 * @author aguelle
 * 
 */
public abstract class DeActivateableMouseAdapter extends MouseAdapter implements
		IDeActivateable {

	private boolean active = true;

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
}
