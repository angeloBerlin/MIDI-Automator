package com.midi_automator.presenter.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.view.frames.MainFrame;

/**
 * Handles the messages in the info label.
 * 
 * @author aguelle
 *
 */
@Service
public class InfoMessagesService {

	private List<String> infoMessages = new ArrayList<String>();

	@Autowired
	private MainFrame mainFrame;

	/**
	 * Sets an error message
	 * 
	 * @param message
	 *            The info message
	 */
	public void setInfoMessage(String message) {
		if (!infoMessages.contains(message)) {
			infoMessages.add(message);
		}
		mainFrame.setInfoText(messagesToString(infoMessages));
	}

	/**
	 * Removes the info message
	 * 
	 * @param message
	 *            The info message
	 */
	public void removeInfoMessage(String message) {
		infoMessages.remove(message);

		if (mainFrame != null) {
			mainFrame.setInfoText(messagesToString(infoMessages));
		}
	}

	/**
	 * Transforms all messages to a HTML formatted String
	 * 
	 * @param messages
	 *            A list of messages
	 * @return A HTML formatted String
	 */
	private String messagesToString(List<String> messages) {
		String result = "";

		for (String message : messages) {
			result = result + message + "<br/>";
		}

		return result;
	}

}
