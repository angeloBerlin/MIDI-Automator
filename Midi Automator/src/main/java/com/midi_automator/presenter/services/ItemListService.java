package com.midi_automator.presenter.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.Messages;
import com.midi_automator.Resources;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.model.Model;
import com.midi_automator.model.SetList;
import com.midi_automator.model.SetListItem;
import com.midi_automator.model.TooManyEntriesException;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.utils.FileUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.windows.MainFrame.ItemList;
import com.midi_automator.view.windows.MainFrame.MainFrame;

/**
 * Handles all functions on the item list.
 * 
 * @author aguelle
 *
 */
@Service
public class ItemListService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private Resources resources;

	@Autowired
	private Model model;

	@Autowired
	private Presenter presenter;

	@Autowired
	private MidiAutomatorProperties properties;

	@Autowired
	private MainFrame mainFrame;
	@Autowired
	private ItemList itemList;

	@Autowired
	private MidiService midiService;
	@Autowired
	private GUIAutomationsService guiAutomationsService;
	@Autowired
	private MidiExecuteService midiRemoteOpenService;
	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;
	@Autowired
	private InfoMessagesService infoMessagesService;

	private int currentIndex = -1;
	private int openedIndex = -1;

	private final long WAIT_BEFORE_OPENING = 100;
	public static final long FAST_SWITCHING_TIMESLOT = 400;
	protected static long lastOpeningTime = 0;

	/**
	 * Adds a new item
	 * 
	 * @param entryName
	 *            the name of the entry
	 * @param filePath
	 *            the path to the file
	 * @param programPath
	 *            the path of the opening program
	 * @param midiSendingSignature
	 *            the midi signature the item will send on opening
	 */
	public void addItem(String entryName, String filePath, String programPath,
			String midiSendingSignature) {
		setItem(null, entryName, filePath, programPath, "",
				midiSendingSignature);
	}

	/**
	 * Sets the attributes of an existing item
	 * 
	 * @param index
	 *            the index of the entry
	 * @param entryName
	 *            the name of the entry
	 * @param filePath
	 *            the path to the file
	 * @param programPath
	 *            the path to the program
	 * @param midiListeningSignature
	 *            the midi signature the item is listening to
	 * @param midiSendingSignature
	 *            the midi signature the item will send on opening
	 * 
	 */
	public void setItem(Integer index, String entryName, String filePath,
			String programPath, String midiListeningSignature,
			String midiSendingSignature) {

		insertItem(index, entryName, filePath, programPath,
				midiListeningSignature, midiSendingSignature, true);
	}

	/**
	 * Inserts an item
	 * 
	 * @param index
	 *            the index of the entry
	 * @param entryName
	 *            the name of the entry
	 * @param filePath
	 *            the path to the file
	 * @param programPath
	 *            the path to the program
	 * @param midiListeningSignature
	 *            the midi signature the item is listening to
	 * @param midiSendingSignature
	 *            the midi signature the item will send on opening
	 * @param overwrite
	 *            <TRUE> the item will be overwritten, <FALSE> the item will be
	 *            inserted
	 * 
	 */
	public void insertItem(Integer index, String entryName, String filePath,
			String programPath, String midiListeningSignature,
			String midiSendingSignature, boolean overwrite) {

		infoMessagesService.removeInfoMessage(Messages
				.get(Messages.KEY_ERROR_TOO_MUCH_ENTRIES));

		if (index == null && model.getSetList().getItems().size() >= 128) {

			String error = String.format(Messages.MSG_FILE_LIST_IS_FULL,
					entryName);
			infoMessagesService.setInfoMessage(
					Messages.KEY_ERROR_TOO_MUCH_ENTRIES, error);

			return;
		}

		if (entryName != null && !entryName.equals("")) {

			SetListItem item = new SetListItem(entryName, filePath,
					programPath, midiListeningSignature, midiSendingSignature);

			if (overwrite) {
				model.getSetList().setItem(item, index);
				log.debug("Set item on index: " + index + ", entry: "
						+ entryName + ", file: " + filePath + ", listening: \""
						+ midiListeningSignature + "\", sending: \""
						+ midiSendingSignature + "\"");
			} else {
				model.getSetList().addItem(item, index);
				log.debug("Add item on index: " + index + ", entry: "
						+ entryName + ", file: " + filePath + ", listening: \""
						+ midiListeningSignature + "\", sending: \""
						+ midiSendingSignature + "\"");
			}

			saveSetList();
		}

		reloadSetList();
	}

	/**
	 * Deletes the item at the given index from the model
	 * 
	 * @param index
	 *            index of the item
	 */
	public void deleteItem(int index) {

		model.getSetList().getItems().remove(index);
		saveSetList();
		reloadSetList();
	}

	/**
	 * Saves the model to a file.
	 */
	public void saveSetList() {

		infoMessagesService.removeInfoMessage(Messages
				.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
		infoMessagesService.removeInfoMessage(Messages
				.get(Messages.KEY_ERROR_MIDO_FILE_IO));
		try {
			model.save();
		} catch (FileNotFoundException e) {
			infoMessagesService.setInfoMessage(Messages
					.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
		} catch (IOException e) {
			infoMessagesService.setInfoMessage(Messages
					.get(Messages.KEY_ERROR_MIDO_FILE_IO));
		}
	}

	/**
	 * Loads the model file.
	 * 
	 * @param selectedIndex
	 *            The last selected index
	 */
	public void reloadSetList() {
		try {
			model.load();
			model.migrate();
			model.load();

			infoMessagesService.removeInfoMessage(Messages
					.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
			infoMessagesService.removeInfoMessage(Messages
					.get(Messages.KEY_ERROR_ITEM_FILE_IO));
			infoMessagesService.removeInfoMessage(Messages
					.get(Messages.KEY_ERROR_MIDO_FILE_TOO_BIG));

			List<SetListItem> items = model.getSetList().getItems();
			List<String> entryNames = new ArrayList<String>();
			List<String> midiListeningSignatures = new ArrayList<String>();
			List<String> midiSedningSignatures = new ArrayList<String>();

			for (SetListItem item : items) {
				entryNames.add(item.getName());
				midiListeningSignatures.add(item.getMidiListeningSignature());
				midiSedningSignatures.add(item.getMidiSendingSignature());
			}

			mainFrame.setItems(entryNames);
			mainFrame.setMidiListeningSignatures(midiListeningSignatures);
			mainFrame.setMidiSendingSignatures(midiSedningSignatures);

		} catch (FileNotFoundException e1) {
			infoMessagesService.setInfoMessage(Messages
					.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
		} catch (IOException e1) {
			infoMessagesService.setInfoMessage(Messages
					.get(Messages.KEY_ERROR_MIDO_FILE_IO));
		} catch (TooManyEntriesException e) {
			infoMessagesService.setInfoMessage(Messages
					.get(Messages.KEY_ERROR_MIDO_FILE_TOO_BIG));
		}

		mainFrame.reload();
	}

	/**
	 * Pushes the item at the given index one step up
	 * 
	 * @param index
	 *            index of the item
	 */
	public void moveUpItem(int index) {

		if (index < 0) {
			return;
		}

		model.getSetList().exchangeIndexes(index, index - 1);
		saveSetList();
		reloadSetList();
		itemList.setSelectedIndex(index - 1);
	}

	/**
	 * Pushes the item at the given index one step down
	 * 
	 * @param index
	 *            index of the item
	 */
	public void moveDownItem(int index) {

		if (index < 0) {
			return;
		}

		model.getSetList().exchangeIndexes(index, index + 1);
		saveSetList();
		reloadSetList();
		itemList.setSelectedIndex(index + 1);
	}

	/**
	 * Selects an entry from the file list
	 * 
	 * @param index
	 *            The index of the file to open from the list
	 * @param send
	 *            <TRUE> opened index will be sent to slaves, <FALSE> index will
	 *            not be sent
	 */
	public void selectEntryByIndex(int index, boolean send) {

		if (index < 0) {
			index = 0;
		}

		itemList.setSelectedIndex(index);
		currentIndex = index;

		EntryOpener entryOpener = new EntryOpener(index, send, this);
		entryOpener.start();
	}

	/**
	 * Opens an entry from the file list
	 * 
	 * @param index
	 *            The index of the file to open from the list
	 * @param send
	 *            <TRUE> opened index will be sent to slaves, <FALSE> index will
	 *            not be sent
	 */
	public void openEntryByIndex(int index, boolean send) {

		infoMessagesService.removeInfoMessage(Messages
				.get(Messages.KEY_INFO_ENTRY_OPENED));

		if (!model.getSetList().getItems().isEmpty()) {

			SetListItem item = model.getSetList().getItems().get(index);

			String infoEntryOpened = String.format(Messages.MSG_OPENING_ENTRY,
					item.getName());
			Messages.put(Messages.KEY_INFO_ENTRY_OPENED, infoEntryOpened);

			infoMessagesService.clearInfoMessages();
			infoMessagesService.setInfoMessage(infoEntryOpened);
			log.debug("Open entry with index: " + index);

			// Send MIDI change notifier
			midiNotificationService
					.sendItemChangeNotifier(midiService
							.getMidiDeviceByKey(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE));
			guiAutomationsService.activateAllOncePerChangeAutomations();

			// Send MIDI remote open
			if (send) {
				midiRemoteOpenService.sendRemoteOpenMidiMessage(index);
			}

			try {
				Thread.sleep(WAIT_BEFORE_OPENING);
			} catch (InterruptedException e) {
				log.error("Delay before opening a file failed.", e);
			}

			// Send MIDI item signature
			midiNotificationService
					.sendItemSignature(
							midiService
									.getMidiDeviceByKey(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE),
							item.getMidiSendingSignature());

			openedIndex = index;
			openFileFromSetListItem(item);
		}
	}

	/**
	 * Opens a set list item with its stored program or with the OS default
	 * program if no program path exists
	 * 
	 * @param item
	 *            the set list item
	 */
	private void openFileFromSetListItem(SetListItem item) {

		String filePath = item.getFilePath();

		infoMessagesService.removeInfoMessage(Messages
				.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
		infoMessagesService.removeInfoMessage(Messages
				.get(Messages.KEY_ERROR_ITEM_FILE_IO));

		String errFileNotFound = String.format(
				Messages.MSG_FILE_LIST_NOT_FOUND, filePath);
		String errFileNotReadable = String.format(
				Messages.MSG_FILE_LIST_NOT_READABLE, filePath);

		Messages.put(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND, errFileNotFound);
		Messages.put(Messages.KEY_ERROR_ITEM_FILE_IO, errFileNotReadable);

		try {

			if (!filePath.equals("")) {
				log.info("Opening file: " + filePath);

				if (item.getProgramPath().equals("")) {
					FileUtils.openFileFromPath(filePath);
				} else {
					FileUtils.openFileFromPathWithProgram(filePath,
							item.getProgramPath());
				}
			}

		} catch (IllegalArgumentException ex) {
			infoMessagesService.setInfoMessage(Messages
					.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
			log.error(Messages.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND), ex);
		} catch (IOException ex) {
			infoMessagesService.setInfoMessage(Messages
					.get(Messages.KEY_ERROR_ITEM_FILE_IO));
			log.error(Messages.get(Messages.KEY_ERROR_ITEM_FILE_IO), ex);
		}

	}

	/**
	 * Opens the previous file in the list
	 */
	public void openPreviousFile() {

		currentIndex--;

		// cycle list
		if (currentIndex < 0) {
			currentIndex = (model.getSetList().getItems().size() - 1);
		}
		selectEntryByIndex(currentIndex, true);
	}

	/**
	 * Opens the next file in the list
	 */
	public void openNextFile() {

		currentIndex++;

		// cylce list
		if (currentIndex >= model.getSetList().getItems().size()) {
			currentIndex = 0;
		}

		selectEntryByIndex(currentIndex, true);
	}

	public void setCurrentItem(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * Gets the entry name of the file list by index
	 * 
	 * @param index
	 *            The index
	 * @return the name of the file entry
	 */
	public String getEntryNameByIndex(int index) {
		try {
			return model.getSetList().getItems().get(index).getName();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Gets the entry file path of the file list by index
	 * 
	 * @param index
	 *            The index
	 * @return the file path of the entry
	 */
	public String getEntryFilePathByIndex(int index) {
		try {
			return model.getSetList().getItems().get(index).getFilePath();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Gets the entry program path of the file list by index
	 * 
	 * @param index
	 *            The index
	 * @return the program path of the entry
	 */
	public String getEntryProgramPathByIndex(int index) {
		try {
			return model.getSetList().getItems().get(index).getProgramPath();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Gets the midi listening signature for the file list index
	 * 
	 * @param index
	 *            The index in the file list
	 * @return The midi signature
	 */
	public String getMidiFileListListeningSignature(int index) {
		try {
			SetList setList = model.getSetList();
			List<SetListItem> list = setList.getItems();
			return list.get(index).getMidiListeningSignature();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Gets the midi signature a file list index is sending
	 * 
	 * @param index
	 *            The index in the file list
	 * @return The midi signature
	 */
	public String getMidiFileListSendingSignature(int index) {
		try {
			SetList setList = model.getSetList();
			List<SetListItem> list = setList.getItems();
			return list.get(index).getMidiSendingSignature();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Gets a unique midi signature that is not used for any item yet.
	 * 
	 * @return The midi signature, <NULL> if there is no unique signature left
	 */
	public String getUniqueSendingMidiSignature() {

		List<String> signatures = model.getSetList().getMidiSendingSignatures();

		ShortMessage message = new ShortMessage();

		int controlNo = 1;
		boolean found = true;
		String signature = null;

		while (found && controlNo <= 127) {
			try {
				message.setMessage(
						ShortMessage.CONTROL_CHANGE,
						MidiItemChangeNotificationService.ITEM_SEND_MIDI_CHANNEL - 1,
						controlNo,
						MidiItemChangeNotificationService.ITEM_SEND_MIDI_VALUE);
			} catch (InvalidMidiDataException e) {
				log.error("Created invalid MIDI message for item", e);
			}
			signature = MidiUtils.messageToString(message);

			if (!signatures.contains(signature)) {
				found = false;
			}
			controlNo++;
		}

		return signature;
	}

	/**
	 * Loads the switch command properties for PREVIOUS and NEXT.
	 * 
	 * @param propertyKey
	 *            The property key
	 */
	private void loadSwitchCommandProperty(String propertyKey) {

		String propertyValue = (String) properties.get(propertyKey);

		if (propertyValue != null) {
			String buttonName = "";

			if (propertyKey
					.equals(MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE)) {
				buttonName = MainFrame.NAME_PREV_BUTTON;
			}

			if (propertyKey
					.equals(MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE)) {
				buttonName = MainFrame.NAME_NEXT_BUTTON;
			}

			if (buttonName != "") {
				mainFrame.setButtonTooltip(propertyValue,
						MainFrame.NAME_NEXT_BUTTON);
			}
		}
	}

	/**
	 * Loads the properties for the service.
	 */
	public void loadProperties() {
		loadSwitchCommandProperty(MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE);
		loadSwitchCommandProperty(MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE);
	}

	/**
	 * Resets the current item's index.
	 */
	public void resetCurrentIndex() {
		currentIndex = -1;
	}

	public int getOpenedIndex() {
		return openedIndex;
	}
}

/**
 * For asynchronous opening of the list entries.
 * 
 * @author aguelle
 *
 */
class EntryOpener extends Thread {

	private Logger log = Logger.getLogger(this.getClass().getName());

	private int index;
	private boolean send;
	private ItemListService fileListService;

	public EntryOpener(int index, boolean send, ItemListService fileListService) {
		super();
		this.index = index;
		this.send = send;
		this.fileListService = fileListService;
	}

	@Override
	public void run() {

		try {
			ItemListService.lastOpeningTime = System.currentTimeMillis();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT);

			if (!isFastSwitching()) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fileListService.openEntryByIndex(index, send);
					}
				});
			}

		} catch (InterruptedException e) {
			log.error("Delay for fast switching file failed.", e);
		}
	}

	/**
	 * Checks if the time since last opening command was bigger than the fast
	 * switching time slot.
	 * 
	 * @return <TRUE> if fast switching, <FALSE> if not fast switching
	 */
	private boolean isFastSwitching() {

		boolean isFastSwitching = false;

		long currentTime = System.currentTimeMillis();
		long lastOpening = ItemListService.lastOpeningTime;
		long timeSinceLastOpening = currentTime - lastOpening;

		if (timeSinceLastOpening < ItemListService.FAST_SWITCHING_TIMESLOT) {
			isFastSwitching = true;
		}

		return isFastSwitching;
	}
}
