package com.midi_automator.presenter.services;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.presenter.Messages;
import com.midi_automator.view.windows.MainFrame.MainFrame;

/**
 * Handles the messages in the info label.
 * 
 * @author aguelle
 *
 */
@Service
public class InfoMessagesService {

	private Set<String> infoMessages = new LinkedHashSet<String>();

	@Autowired
	private MainFrame mainFrame;

	/**
	 * Sets an info message and stores it under the given key
	 * 
	 * @param key
	 *            The key
	 * @param message
	 *            The message
	 */
	public void setInfoMessage(String key, String message) {
		Messages.put(key, message);
		setInfoMessage(message);
	}

	/**
	 * Sets an info message
	 * 
	 * @param message
	 *            The info message
	 */
	public void setInfoMessage(String message) {
		if (!infoMessages.contains(message)) {
			infoMessages.add(message);
		}
		String displayedMesssage = messagesToString(infoMessages);
		mainFrame.setInfoText(displayedMesssage);
	}

	/**
	 * Removes the info message
	 * 
	 * @param message
	 *            The info message
	 */
	public void removeInfoMessage(String message) {
		infoMessages.remove(message);
		String displayedMesssage = messagesToString(infoMessages);
		mainFrame.setInfoText(displayedMesssage);
	}

	/**
	 * Removes all current info messages.
	 */
	public void clearInfoMessages() {
		infoMessages.clear();
		mainFrame.setInfoText("");
	}

	/**
	 * Transforms all messages to a HTML formatted String
	 * 
	 * @param messages
	 *            A list of messages
	 * @return A HTML formatted String
	 */
	private String messagesToString(Collection<String> messages) {
		String result = "";

		for (String message : messages) {
			result = result + message + "<br/>";
		}

		return result;
	}

}
