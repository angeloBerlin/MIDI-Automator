package com.midi_automator.view.windows.MainFrame.listener;

import java.awt.Component;
import java.awt.event.MouseEvent;

import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.presenter.services.MidiLearnService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.view.DeActivateableMouseAdapter;
import com.midi_automator.view.windows.MainFrame.ItemList;
import com.midi_automator.view.windows.MainFrame.MainFrame;
import com.midi_automator.view.windows.MainFrame.menus.MainFramePopupMenu;

/**
 * Shows the context menu.
 * 
 * @author aguelle
 * 
 */
@org.springframework.stereotype.Component
public class MainFramePopupListener extends DeActivateableMouseAdapter {

	@Autowired
	private MidiService midiService;
	@Autowired
	private ItemListService itemListService;
	@Autowired
	private MidiLearnService midiLearnService;

	@Autowired
	private MainFrame mainFrame;

	@Autowired
	private MainFramePopupMenu popupMenu;

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			mouseReleased(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (isActive()) {
				maybeShowPopup(e);
			}
		}
	}

	/**
	 * Shows the popup menu if it was a popup trigger. The trigger is OS
	 * specific.
	 * 
	 * @param e
	 *            The mouse event
	 */
	private void maybeShowPopup(MouseEvent e) {

		// getting the name of the component
		Component component = (Component) e.getSource();
		String componentName = component.getName();

		if (componentName != null) {
			// show pop-up of file list
			if (componentName.equals(ItemList.NAME)) {

				showPopupOnItemList(e);
				mainFrame.setPopupWasShown(true);
			}

			// show pop-up of switch buttons
			if (componentName.equals(MainFrame.NAME_NEXT_BUTTON)
					|| (componentName.equals(MainFrame.NAME_PREV_BUTTON))) {
				showPopUpOnSwitchButtons(e);
			}

			configureMidiUnlearnItem(componentName);

		}
	}

	/**
	 * Sets the MIDI unlearn menu item enabled or disabled depending if a MIDI
	 * signature was learned for the component.
	 * 
	 * @param componentName
	 *            The nam eof the component
	 */
	private void configureMidiUnlearnItem(String componentName) {

		// en/disable midi unlearn
		popupMenu.getMidiUnlearnMenuItem().setEnabled(false);
		if (midiLearnService.isMidiLearned(componentName)) {
			popupMenu.getMidiUnlearnMenuItem().setEnabled(true);
		}
	}

	/**
	 * Configures and shows the popup menu on the switch buttons
	 * 
	 * @param e
	 *            The invoking mouse event
	 */
	private void showPopUpOnSwitchButtons(MouseEvent e) {

		popupMenu.createSwitchButtonPopupMenu();
		configurePopUpMenuOnSwitchButton();
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		mainFrame.setPopupWasShown(true);
	}

	/**
	 * Configures the popup menu on the switch buttons
	 */
	private void configurePopUpMenuOnSwitchButton() {

		String midiInRemoteDeviceName = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);

		if (midiInRemoteDeviceName != null
				&& !midiInRemoteDeviceName
						.equals(MidiAutomatorProperties.VALUE_NULL)) {
			popupMenu.getMidiLearnMenuItem().setEnabled(true);
		}
	}

	/**
	 * Configures and shows the popup menu on the item list
	 * 
	 * @param e
	 *            The invoking mouse event
	 */
	private void showPopupOnItemList(MouseEvent e) {

		ItemList itemList = mainFrame.getItemList();

		// set selection in file list
		itemList.setSelectedIndex(itemList.locationToIndex(e.getPoint()));

		popupMenu.createItemListPopupMenu();
		configurePopUpMenuOnItemList(itemList);
		popupMenu.show(e.getComponent(), e.getX(), e.getY());

	}

	/**
	 * Configures the popup menu on the item list
	 * 
	 * @param itemList
	 *            The item list
	 */
	private void configurePopUpMenuOnItemList(ItemList itemList) {

		String midiInRemoteDeviceName = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);
		String switchItemDeviceName = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE);

		// en/disable add
		popupMenu.getAddMenuItem().setEnabled(false);
		if (!midiLearnService.isMidiLearning()) {
			popupMenu.getAddMenuItem().setEnabled(true);
		}

		// en/disable edit
		popupMenu.getEditMenuItem().setEnabled(false);
		if (!midiLearnService.isMidiLearning()
				&& itemList.getSelectedIndex() > -1) {
			popupMenu.getEditMenuItem().setEnabled(true);
		}

		// en/disable delete
		popupMenu.getDeleteMenuItem().setEnabled(false);
		if (!midiLearnService.isMidiLearning()
				&& itemList.getSelectedIndex() > -1) {
			popupMenu.getDeleteMenuItem().setEnabled(true);
		}

		// en/disable move up
		popupMenu.getMoveUpMenuItem().setEnabled(false);
		if (!midiLearnService.isMidiLearning() && !itemList.isFirstItem()
				&& itemList.getSelectedIndex() > -1) {
			popupMenu.getMoveUpMenuItem().setEnabled(true);
		}

		// en/disable move down
		popupMenu.getMoveDownMenuItem().setEnabled(false);
		if (!midiLearnService.isMidiLearning() && !itemList.isLastItem()
				&& itemList.getSelectedIndex() > -1) {
			popupMenu.getMoveDownMenuItem().setEnabled(true);
		}

		// en/disable midi learn
		popupMenu.getMidiLearnMenuItem().setEnabled(false);
		if (itemList.getSelectedIndex() > -1
				&& midiInRemoteDeviceName != null
				&& !midiInRemoteDeviceName
						.equals(MidiAutomatorProperties.VALUE_NULL)) {
			popupMenu.getMidiLearnMenuItem().setEnabled(true);
		}

		// en/disable send midi
		String sendingSignature = itemListService
				.getMidiFileListSendingSignature(itemList.getSelectedIndex());

		popupMenu.getSendMidiMenuItem().setEnabled(false);
		if (!midiLearnService.isMidiLearning()
				&& switchItemDeviceName != null
				&& !switchItemDeviceName
						.equals(MidiAutomatorProperties.VALUE_NULL)
				&& sendingSignature != null) {
			popupMenu.getSendMidiMenuItem().setEnabled(true);
		}
	}
}
