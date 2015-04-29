package com.midi_automator.view;

import java.awt.event.MouseAdapter;

import com.midi_automator.presenter.IDeActivateable;

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
