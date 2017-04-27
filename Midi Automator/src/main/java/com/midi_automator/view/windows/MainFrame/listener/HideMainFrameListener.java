package com.midi_automator.view.windows.MainFrame.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.PresenterService;
import com.midi_automator.view.windows.MainFrame.MainFrame;
import com.midi_automator.view.windows.MainFrame.actions.HideShowMainFrameAction;

/**
 * Hides the window and displays a dialog that the program was just hidden.
 * 
 * @author aguelle
 *
 */
@Component
public class HideMainFrameListener extends WindowAdapter {

	@Autowired
	private MainFrame programFrame;

	@Autowired
	private PresenterService presenterService;

	@Autowired
	private HideShowMainFrameAction hideShowAction;

	@Override
	public void windowClosing(WindowEvent e) {

		if (!programFrame.isExiting() && presenterService.isMinimizeOnClose()) {
			hideShowAction.actionPerformed(null);
		} else {
			programFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

	}
}
