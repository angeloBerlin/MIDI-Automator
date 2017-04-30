package com.midi_automator.view;

import java.awt.event.MouseAdapter;

import org.springframework.stereotype.Component;

import com.midi_automator.IDeActivateable;

/**
 * A MouseAdapter with a boolean "active" field.
 * 
 * @author aguelle
 * 
 */
@Component
public abstract class DeActivateableMouseAdapter extends MouseAdapter implements
		IDeActivateable {

	protected boolean active = true;

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
}
