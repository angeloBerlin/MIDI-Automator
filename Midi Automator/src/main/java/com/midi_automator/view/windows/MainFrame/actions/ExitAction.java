package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.Presenter;

/**
 * Closes all open frames and exits.
 * 
 * @author aguelle
 * 
 */
@Component
public class ExitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	Presenter presenter;

	@Override
	public void actionPerformed(ActionEvent e) {
		presenter.close();
	}
}
