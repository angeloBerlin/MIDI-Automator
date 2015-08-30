package com.midi_automator.view.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.GUIUtils;
import com.midi_automator.view.CacheableJButton;
import com.midi_automator.view.CacheableToolTipJList;
import com.midi_automator.view.DeActivateableMouseAdapter;
import com.midi_automator.view.HTMLLabel;
import com.midi_automator.view.IToolTipItem;
import com.midi_automator.view.MainFramePopupMenu;
import com.midi_automator.view.ToolTipItemImpl;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(MainFrame.class.getName());

	private final String TITLE = "Midi Automator";
	private final int WIDTH = 500;
	private final int HEIGHT = 600;
	public static final int TEST_POSITION_X = 0;
	public static final int TEST_POSITION_Y = 50;
	private final int MAIN_LAYOUT_HORIZONTAL_GAP = 10;
	private final int MAIN_LAYOUT_VERTICAL_GAP = 10;
	private final int FONT_SIZE_FILE_LIST = 26;
	private final String FONT_FAMILY = "Arial";
	private final String ICON_PATH_PREV;
	private final String ICON_PATH_NEXT;
	private final String MENU_FILE = "File";
	private final String MENU_ITEM_EXIT = "Exit";
	private final String MENU_ITEM_PREFERENCES = "Preferences";
	private final String LABEL_MIDI_IN_DETECT = "IN";
	private final String LABEL_MIDI_OUT_DETECT = "OUT";

	public static final String NAME_PREV_BUTTON = "previous button";
	public static final String NAME_NEXT_BUTTON = "next button";
	public static final String NAME_FILE_LIST = "file list";

	private final String NAME_MENU_ITEM_PREFERENCES = "preferences";
	private final String NAME_MENU_ITEM_EXIT = "exit";

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private final MainFramePopupMenu POPUP_MENU;
	private JMenuItem exitMenuItem;
	private JMenuItem preferencesMenuItem;
	private JLabel midiINdetect;
	private JLabel midiOUTdetect;
	private HTMLLabel infoLabel;
	private JScrollPane fileJScrollPane;
	private CacheableToolTipJList<IToolTipItem> fileJList;
	private CacheableJButton prevButton;
	private CacheableJButton nextButton;

	private Runnable midiINflasher;
	private Runnable midiOUTflasher;
	private boolean midiINflasherFlag;
	private boolean midiOUTflasherFlag;

	private final MidiAutomator APPLICATION;
	private List<String> fileEntries;
	private List<String> midiListeningSignatures;
	private List<String> midiSendingSignatures;

	private int lastSelectedIndex;
	private boolean popupWasShown;

	private PreferencesFrame preferencesFrame;

	/**
	 * The main window.
	 * 
	 * @param application
	 *            The main application
	 * @param version
	 *            The version of the application
	 * @throws HeadlessException
	 *             Thrown if run on an OS without GUI representation
	 */
	public MainFrame(MidiAutomator application, String version)
			throws HeadlessException {

		// initialize frame
		super();
		this.APPLICATION = application;
		this.setTitle(TITLE + " " + version);
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setResizable(true);

		if (application.isInTestMode()) {
			this.setLocation(new Point(MainFrame.TEST_POSITION_X,
					MainFrame.TEST_POSITION_Y));
		}

		String icon16 = application.getResources().getImagePath()
				+ "MidiAutomatorIcon16.png";
		String icon32 = application.getResources().getImagePath()
				+ "MidiAutomatorIcon32.png";
		String icon64 = application.getResources().getImagePath()
				+ "MidiAutomatorIcon64.png";
		String icon128 = application.getResources().getImagePath()
				+ "MidiAutomatorIcon128.png";
		String icon256 = application.getResources().getImagePath()
				+ "MidiAutomatorIcon256.png";
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(icon16).getImage());
		icons.add(new ImageIcon(icon32).getImage());
		icons.add(new ImageIcon(icon64).getImage());
		icons.add(new ImageIcon(icon128).getImage());
		icons.add(new ImageIcon(icon256).getImage());
		setIconImages(icons);

		ICON_PATH_PREV = application.getResources().getImagePath()
				+ "arrow_prev.png";
		ICON_PATH_NEXT = application.getResources().getImagePath()
				+ "arrow_next.png";

		// Menu
		createMenuItems();
		createMenu();
		POPUP_MENU = new MainFramePopupMenu(this, application);
		popupWasShown = false;

		// Layout
		getContentPane().setLayout(
				new BorderLayout(MAIN_LAYOUT_HORIZONTAL_GAP,
						MAIN_LAYOUT_VERTICAL_GAP));

		// Header
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new FlowLayout());
		add(headerPanel, BorderLayout.PAGE_START);

		createInfo(headerPanel);
		createMidiDetect(headerPanel);

		// Middle
		createFileList(this);

		// left and right margins
		add(new JPanel(), BorderLayout.LINE_START);
		add(new JPanel(), BorderLayout.LINE_END);

		// Footer
		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new GridBagLayout());
		add(footerPanel, BorderLayout.PAGE_END);

		createSwitchButtons(footerPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setAlwaysOnTop(true);
		setVisible(true);
	}

	public HTMLLabel getInfoLabel() {
		return infoLabel;
	}

	public CacheableToolTipJList<IToolTipItem> getFileList() {
		return fileJList;
	}

	public int getLastSelectedIndex() {
		return lastSelectedIndex;
	}

	/**
	 * Sets the file entries
	 * 
	 * @param entries
	 *            The entries for the files
	 */
	public void setFileEntries(List<String> entries) {
		fileEntries = entries;
	}

	/**
	 * Sets the midi signatures the items are listening to
	 * 
	 * @param signatures
	 *            The signatures for the items
	 */
	public void setMidiListeningSignatures(List<String> signatures) {
		midiListeningSignatures = signatures;
	}

	/**
	 * Sets the midi signatures the items are sending on opening
	 * 
	 * @param signatures
	 *            The signatures for the files
	 */
	public void setMidiSendingSignatures(List<String> signatures) {
		midiSendingSignatures = signatures;
	}

	/**
	 * Reloads the data and properties.
	 */
	public void reload() {

		// set list entries
		fileJList.setListData(createViewableFileList(fileEntries));
		if (fileEntries.isEmpty()) {
			prevButton.setEnabled(false);
			nextButton.setEnabled(false);
		} else {
			prevButton.setEnabled(true);
			nextButton.setEnabled(true);
		}

		// set tooltips
		for (int i = 0; i < midiListeningSignatures.size(); i++) {
			String listeningSignature = midiListeningSignatures.get(i);
			String sendingSignature = midiSendingSignatures.get(i);

			setListItemTooltip("Listening: " + listeningSignature
					+ "\nSending: " + sendingSignature,
					MainFrame.NAME_FILE_LIST, i);
		}

		// reload preferences frame
		if (preferencesFrame != null) {
			preferencesFrame.reload();
		}
	}

	/**
	 * Sets the text of the info label
	 * 
	 * @param text
	 */
	public void setInfoText(String text) {
		infoLabel
				.setText("<span style='font-family:Arial'>" + text + "</span>");
		this.repaint();
	}

	/**
	 * Sets the tooltip for a button.
	 * 
	 * @param text
	 *            The tooltip text
	 * @param buttonName
	 *            The name of the button
	 */
	public void setButtonTooltip(String text, String buttonName) {

		Component component = GUIUtils.getComponentByName(this, buttonName);

		if (component instanceof JComponent) {
			((JComponent) component).setToolTipText(text);
		}
	}

	/**
	 * Sets the tooltip for a list item
	 * 
	 * @param text
	 *            The tooltip text
	 * @param listName
	 *            The name of the list
	 * @param index
	 *            The index of the list item
	 */
	public void setListItemTooltip(String text, String listName, int index) {

		Component component = GUIUtils.getComponentByName(this, listName);

		if (component instanceof CacheableToolTipJList) {
			((CacheableToolTipJList<?>) component).setToolTipText(text, index);
		}
	}

	/**
	 * Puts the frame to normal mode.
	 */
	public void midiLearnOff() {

		// set all frames to midi learn off
		if (preferencesFrame != null) {
			preferencesFrame.midiLearnOff();
		}

		// enable inputs
		GUIUtils.disEnableAllInputs(this, true);

		// change menu item
		POPUP_MENU.midiLearnOff();

		GUIUtils.deHighlightComponent(prevButton, false);
		GUIUtils.deHighlightComponent(nextButton, false);
		GUIUtils.deHighlightListItem(fileJList, false);

		Object cache = fileJList.getCache();
		if (cache instanceof Color) {
			fileJList.setSelectionBackground((Color) cache);
		}
	}

	/**
	 * Puts the frame to midi learn mode
	 * 
	 * @param learningComponent
	 *            The component to learn for
	 */
	public void midiLearnOn(JComponent learningComponent) {

		// set all frames to midi learn on
		if (preferencesFrame != null) {
			preferencesFrame.midiLearnOn(learningComponent);
		}

		// disable inputs
		GUIUtils.disEnableAllInputs(this, false);

		// activate popup menu listener
		MouseListener[] mouseListeners = learningComponent.getMouseListeners();
		for (int i = 0; i < mouseListeners.length; i++) {
			if (mouseListeners[i] instanceof DeActivateableMouseAdapter) {
				DeActivateableMouseAdapter mouseAdapter = (DeActivateableMouseAdapter) mouseListeners[i];
				mouseAdapter.setActive(true);
			}
		}

		// change menu item text
		POPUP_MENU.midiLearnOn();

		// highlight component
		if (learningComponent.getName() != null) {
			if (learningComponent.getName().equals(NAME_FILE_LIST)) {

				GUIUtils.deHighlightListItem(fileJList, true);

				log.info("Learning midi for index: "
						+ (fileJList.getSelectedIndex() + 1));

			} else {

				if (learningComponent.getName().equals(NAME_PREV_BUTTON)
						|| learningComponent.getName().equals(NAME_NEXT_BUTTON)) {
					GUIUtils.deHighlightComponent(learningComponent, true);

					log.info("Learning midi for button: "
							+ learningComponent.getName());
				}
			}
		}
	}

	/**
	 * Sets the selected item in the file list
	 * 
	 * @param index
	 *            The index
	 */
	public void setSelectedIndex(int index) {
		fileJList.setSelectedIndex(index);
		fileJList.ensureIndexIsVisible(index);
	}

	/**
	 * Flashes the MIDI IN detector
	 */
	public void flashMidiINDetect() {

		if (!midiINflasherFlag) {
			midiINflasherFlag = true;
			new Thread(midiINflasher).start();
		}
	}

	/**
	 * Flashes the MIDI OUT detector
	 */
	public void flashMidiOUTDetect() {

		if (!midiOUTflasherFlag) {
			midiOUTflasherFlag = true;
			new Thread(midiOUTflasher).start();
		}
	}

	/**
	 * Creates all menus.
	 */
	private void createMenu() {

		menuBar = new JMenuBar();
		fileMenu = new JMenu(MENU_FILE);

		menuBar.add(fileMenu);

		fileMenu.add(preferencesMenuItem);
		fileMenu.add(exitMenuItem);

		setJMenuBar(menuBar);
	}

	/**
	 * Creates all menu items
	 */
	private void createMenuItems() {

		preferencesMenuItem = new JMenuItem(MENU_ITEM_PREFERENCES);
		preferencesMenuItem.setName(NAME_MENU_ITEM_PREFERENCES);
		preferencesMenuItem.setEnabled(true);
		preferencesMenuItem.addActionListener(new PreferencesAction(this));

		exitMenuItem = new JMenuItem(MENU_ITEM_EXIT);
		exitMenuItem.setName(NAME_MENU_ITEM_EXIT);
		exitMenuItem.setEnabled(true);
		exitMenuItem.addActionListener(new ExitAction());
	}

	/**
	 * Creates the info label on top
	 * 
	 * @param parent
	 *            The parent Container
	 * 
	 */
	private void createInfo(Container parent) {
		final Dimension dimension = new Dimension(426, 50);

		infoLabel = new HTMLLabel();
		infoLabel.setPreferredSize(dimension);

		JScrollPane scrollingInfoLabel = new JScrollPane(infoLabel);
		scrollingInfoLabel.getViewport().setBackground(this.getBackground());

		parent.add(scrollingInfoLabel);
	}

	/**
	 * Creates the midi detectors
	 * 
	 * @param parent
	 *            The parent Container
	 * 
	 */
	private void createMidiDetect(Container parent) {
		final Dimension dimension = new Dimension(25, 25);
		final Font font = new Font(FONT_FAMILY, Font.PLAIN, 10);

		midiINdetect = new JLabel(LABEL_MIDI_IN_DETECT);
		midiOUTdetect = new JLabel(LABEL_MIDI_OUT_DETECT);
		midiINdetect.setFont(font);
		midiOUTdetect.setFont(font);
		midiINdetect.setHorizontalAlignment(SwingConstants.CENTER);
		midiOUTdetect.setHorizontalAlignment(SwingConstants.CENTER);
		midiINdetect.setBorder(BorderFactory.createLineBorder(Color.black));
		midiOUTdetect.setBorder(BorderFactory.createLineBorder(Color.black));
		midiINdetect.setPreferredSize(dimension);
		midiOUTdetect.setPreferredSize(dimension);
		midiINdetect.setOpaque(true);
		midiOUTdetect.setOpaque(true);

		midiINflasher = new MidiDetectFlasher(LABEL_MIDI_IN_DETECT);
		midiOUTflasher = new MidiDetectFlasher(LABEL_MIDI_OUT_DETECT);
		midiINflasherFlag = false;
		midiOUTflasherFlag = false;

		JPanel detectorPanel = new JPanel();
		GridLayout grid = new GridLayout(2, 1);
		grid.setVgap(3);
		detectorPanel.setLayout(grid);
		detectorPanel.add(midiINdetect);
		detectorPanel.add(midiOUTdetect);
		parent.add(detectorPanel);
	}

	/**
	 * Creates the list of files to open in the middle
	 * 
	 * @param parent
	 *            The parent Container
	 */
	private void createFileList(Container parent) {

		fileJList = new CacheableToolTipJList<IToolTipItem>();
		fileJList.addMouseListener(new OpenFileOnDoubleClick());
		fileJList.addMouseListener(new PopupListener());
		fileJList
				.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_FILE_LIST));
		fileJList.setName(NAME_FILE_LIST);
		fileJList.setCache(fileJList.getSelectionBackground());
		fileJList.setFocusable(false);

		fileJScrollPane = new JScrollPane(fileJList);
		fileJScrollPane.setViewportView(fileJList);
		parent.add(fileJScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Creates the previous and next buttons
	 * 
	 * @param parent
	 *            The parent container
	 */
	private void createSwitchButtons(Container parent) {
		final Dimension dimension = new Dimension(230, 230);

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 10, 0);
		c.gridy = 0;

		prevButton = new CacheableJButton();
		prevButton.setName(NAME_PREV_BUTTON);
		prevButton.setPreferredSize(dimension);
		prevButton.setAction(new PrevAction());
		prevButton.setIcon(new ImageIcon(ICON_PATH_PREV));
		prevButton.addMouseListener(new PopupListener());
		prevButton.setFocusable(false);

		c.gridx = 0;
		parent.add(prevButton, c);
		prevButton.setCache(prevButton.getBorder());

		nextButton = new CacheableJButton();
		nextButton.setName(NAME_NEXT_BUTTON);
		nextButton.setPreferredSize(dimension);
		nextButton.setAction(new NextAction());
		nextButton.setIcon(new ImageIcon(ICON_PATH_NEXT));
		nextButton.addMouseListener(new PopupListener());
		nextButton.setFocusable(false);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		parent.add(nextButton, c);
		nextButton.setCache(prevButton.getBorder());
	}

	/**
	 * Prepares the file list for the view, by adding indexes and returns it as
	 * an array.
	 * 
	 * @param entryNames
	 *            The list with entry names
	 * @return an array with the content for JList
	 */
	private IToolTipItem[] createViewableFileList(List<String> entryNames) {

		IToolTipItem[] result = new IToolTipItem[entryNames.size()];
		int i = 0;
		for (String entryName : entryNames) {
			String value = (i + 1) + " " + entryName;
			result[i] = new ToolTipItemImpl(value);
			i++;
		}
		return result;
	}

	/**
	 * Returns the name for a component that can take a midi signature
	 * 
	 * @param component
	 *            The component
	 * @return A printable unique name
	 */
	public String getMidiComponentName(Component component) {

		if (component.getName().equals(MainFrame.NAME_FILE_LIST)) {

			return "\""
					+ APPLICATION.getEntryNameByIndex(fileJList
							.getSelectedIndex()) + "\"";
		}

		if (component instanceof JButton) {
			return component.getName();
		}
		return null;
	}

	/**
	 * Flashes the background color of the content pane.
	 * 
	 * @color The color for flashing
	 */
	public void flashFileList(Color color) {

		final int duration = 100;
		Color originalColor = null;

		try {
			originalColor = fileJList.getBackground();
			fileJList.setBackground(color);
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			log.error("The delay of the file list flasher failed", e);
		} finally {
			fileJList.setBackground(originalColor);
		}
	}

	/**
	 * Gets the preferences frame
	 * 
	 * @return The preferences frame
	 */
	public PreferencesFrame getPreferencesFrame() {
		return preferencesFrame;
	}

	/**
	 * Sets a learned midi signature.
	 * 
	 * @param signature
	 *            The signature
	 * @param component
	 *            The learned component
	 */
	public void setMidiSignature(String signature, Component component) {

		if (preferencesFrame != null) {
			preferencesFrame.setMidiSignature(signature, component);
		}
	}

	/**
	 * Opens the previous file from the file list.
	 * 
	 * @author aguelle
	 * 
	 */
	class PrevAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {
			setEnabled(false);
			APPLICATION.openPreviousFile();
			setEnabled(true);
		}
	}

	/**
	 * Opens the next file from the file list
	 * 
	 * @author aguelle
	 * 
	 */
	class NextAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {
			setEnabled(false);
			APPLICATION.openNextFile();
			setEnabled(true);
		}
	}

	/**
	 * Opens the double-clicked file from the JList
	 * 
	 * @author aguelle
	 * 
	 */
	class OpenFileOnDoubleClick extends DeActivateableMouseAdapter {

		@SuppressWarnings("unchecked")
		public void mouseClicked(MouseEvent me) {

			if (isActive()) {
				if (me.getSource() instanceof JList) {

					JList<String> fileJList = (JList<String>) me.getSource();
					lastSelectedIndex = fileJList
							.locationToIndex(me.getPoint());

					// left double click
					if (me.getButton() == MouseEvent.BUTTON1
							&& me.getClickCount() == 2) {
						if (!popupWasShown) {
							APPLICATION
									.openFileByIndex(lastSelectedIndex, true);
						}
						popupWasShown = false;
					}
				}
			}
		}
	}

	/**
	 * Closes all open frames and exits.
	 * 
	 * @author aguelle
	 * 
	 */
	class ExitAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			// Find the active frame before creating and dispatching the event
			for (Frame frame : Frame.getFrames()) {
				if (frame.isActive()) {

					WindowEvent windowClosing = new WindowEvent(frame,
							WindowEvent.WINDOW_CLOSING);
					frame.dispatchEvent(windowClosing);
				}
			}
		}
	}

	/**
	 * Opens the preferences window
	 * 
	 * @author aguelle
	 * 
	 */
	class PreferencesAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private JFrame programFrame;

		public PreferencesAction(JFrame programFrame) {
			super();
			this.programFrame = programFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			preferencesFrame = new PreferencesFrame(APPLICATION, programFrame);
		}
	}

	/**
	 * Shows the context menu.
	 * 
	 * @author aguelle
	 * 
	 */
	class PopupListener extends DeActivateableMouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			mouseReleased(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isActive()) {
				maybeShowPopup(e);
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
			if (e.isPopupTrigger()) {

				// getting the name of the component
				Component component = (Component) e.getSource();
				String componentName = component.getName();

				// getting midi device names
				String midiInRemoteDeviceName = APPLICATION
						.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);
				String switchItemDeviceName = APPLICATION
						.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE);

				if (componentName != null) {

					// en/disable midi unlearn
					POPUP_MENU.getMidiUnlearnMenuItem().setEnabled(false);
					if (isMidiLearned(componentName)) {
						POPUP_MENU.getMidiUnlearnMenuItem().setEnabled(true);
					}

					// show pop-up of file list
					if (componentName.equals(MainFrame.NAME_FILE_LIST)) {

						// set selection in file list
						fileJList.setSelectedIndex(fileJList.locationToIndex(e
								.getPoint()));

						POPUP_MENU.configureFileListPopupMenu();
						POPUP_MENU.show(e.getComponent(), e.getX(), e.getY());

						// en/disable edit
						POPUP_MENU.getEditMenuItem().setEnabled(false);
						if (fileJList.getSelectedIndex() > -1) {
							POPUP_MENU.getEditMenuItem().setEnabled(true);
						}

						// en/disable delete
						POPUP_MENU.getDeleteMenuItem().setEnabled(false);
						if (fileJList.getSelectedIndex() > -1) {
							POPUP_MENU.getDeleteMenuItem().setEnabled(true);
						}

						// en/disable move up
						POPUP_MENU.getMoveUpMenuItem().setEnabled(false);
						if (!isFirstItem() && fileJList.getSelectedIndex() > -1) {
							POPUP_MENU.getMoveUpMenuItem().setEnabled(true);
						}

						// en/disable move down
						POPUP_MENU.getMoveDownMenuItem().setEnabled(false);
						if (!isLastItem() && fileJList.getSelectedIndex() > -1) {
							POPUP_MENU.getMoveDownMenuItem().setEnabled(true);
						}

						// en/disable midi learn
						POPUP_MENU.getMidiLearnMenuItem().setEnabled(false);
						if (fileJList.getSelectedIndex() > -1
								&& midiInRemoteDeviceName != null
								&& !midiInRemoteDeviceName
										.equals(MidiAutomatorProperties.VALUE_NULL)) {
							POPUP_MENU.getMidiLearnMenuItem().setEnabled(true);
						}

						// en/disable send midi
						POPUP_MENU.getSendMidiMenuItem().setEnabled(false);
						if (!switchItemDeviceName
								.equals(MidiAutomatorProperties.VALUE_NULL)
								&& switchItemDeviceName != null) {
							POPUP_MENU.getSendMidiMenuItem().setEnabled(true);
						}

						popupWasShown = true;
					}

					// show pop-up of switch buttons
					if (componentName.equals(MainFrame.NAME_NEXT_BUTTON)
							|| (componentName
									.equals(MainFrame.NAME_PREV_BUTTON))) {

						if (midiInRemoteDeviceName != null
								&& !midiInRemoteDeviceName
										.equals(MidiAutomatorProperties.VALUE_NULL)) {
							POPUP_MENU.getMidiLearnMenuItem().setEnabled(true);
						}

						POPUP_MENU.configureSwitchButtonPopupMenu();
						POPUP_MENU.show(e.getComponent(), e.getX(), e.getY());
						popupWasShown = true;
					}
				}
			}
		}
	}

	/**
	 * Checks if a midi signature was learned for the given component name
	 * 
	 * @param componentName
	 *            The name of the component
	 * @return <TRUE> if it was learned, <FALSE> if it was not learned or
	 *         unlearned
	 */
	private boolean isMidiLearned(String componentName) {
		boolean isLearned = false;

		// previous switch button
		String prevSignature = APPLICATION
				.getMidiSignature(MidiAutomator.SWITCH_DIRECTION_PREV);
		if (prevSignature != null) {
			if (componentName.equals(MainFrame.NAME_PREV_BUTTON)
					&& (!prevSignature.equals(""))) {
				isLearned = true;
			}
		}

		// next switch button
		String nextSignature = APPLICATION
				.getMidiSignature(MidiAutomator.SWITCH_DIRECTION_NEXT);
		if (nextSignature != null) {
			if (componentName.equals(MainFrame.NAME_NEXT_BUTTON)
					&& (!nextSignature.equals(""))) {
				isLearned = true;
			}
		}

		// file list item
		String selectedSignature = APPLICATION.getMidiSignature(fileJList
				.getSelectedIndex());
		if (selectedSignature != null) {
			if (componentName.equals(MainFrame.NAME_FILE_LIST)
					&& (!selectedSignature.equals(""))) {
				isLearned = true;
			}
		}

		return isLearned;
	}

	/**
	 * Checks if the selected item is the first item in the list.
	 * 
	 * @return <TRUE> if it is the first item, <FALSE> if it is not the first
	 *         item
	 */
	private boolean isFirstItem() {
		boolean result = false;
		if (fileJList.getSelectedIndex() == 0) {
			result = true;
		}
		return result;
	}

	/**
	 * Checks if the selected item is the last item in the list.
	 * 
	 * @return <TRUE> if it is the last item, <FALSE> if it is not the last item
	 */
	private boolean isLastItem() {
		boolean result = false;
		if (fileJList.getSelectedIndex() == (fileEntries.size() - 1)) {
			result = true;
		}
		return result;
	}

	/**
	 * Flashes the desired midi detector.
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiDetectFlasher implements Runnable {

		private final int duration = 100;
		private final Color color = Color.YELLOW;
		private String midiDetect;

		/**
		 * Constructor
		 * 
		 * @param midiDetect
		 *            The midi detector to flash, "MIDI IN" or "MIDI OUT"
		 */
		public MidiDetectFlasher(String midiDetect) {
			this.midiDetect = midiDetect;
		}

		@Override
		public void run() {

			if (midiDetect.equals(LABEL_MIDI_IN_DETECT)) {
				flashLabel(midiINdetect, duration, color);
				midiINflasherFlag = false;
			}

			if (midiDetect.equals(LABEL_MIDI_OUT_DETECT)) {
				flashLabel(midiOUTdetect, duration, color);
				midiOUTflasherFlag = false;
			}
		}

		/**
		 * Flashes the background color of a JLabel
		 * 
		 * @param label
		 *            The JLabel
		 * @param duration
		 *            The time to flash
		 * @param color
		 *            The color to flash
		 */
		private void flashLabel(JLabel label, int duration, Color color) {

			try {
				label.setBackground(color);
				Thread.sleep(duration);
				label.setBackground(null);
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				log.error("The delay of the label flasher failed", e);
			}
		}
	}

	public boolean isPopupWasShown() {
		return popupWasShown;
	}

	public void setPopupWasShown(boolean popupWasShown) {
		this.popupWasShown = popupWasShown;
	}
}
