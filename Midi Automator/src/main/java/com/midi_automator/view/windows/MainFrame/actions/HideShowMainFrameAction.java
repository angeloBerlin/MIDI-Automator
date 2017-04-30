package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.PresenterService;

/**
 * Hides and restores the Main Frame.
 * 
 * @author aguelle
 * 
 */
@Component
public class HideShowMainFrameAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PresenterService presenterService;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		presenterService.hideShowMainFrame();
	}
}
