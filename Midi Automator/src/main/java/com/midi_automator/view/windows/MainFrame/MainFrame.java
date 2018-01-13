package com.midi_automator.view.windows.MainFrame;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.midi_automator.MidiAutomator;
import com.midi_automator.Resources;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.ImportExportService;
import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.presenter.services.MidiExecuteService;
import com.midi_automator.presenter.services.MidiItemChangeNotificationService;
import com.midi_automator.presenter.services.MidiLearnService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.presenter.services.PresenterService;
import com.midi_automator.utils.GUIUtils;
import com.midi_automator.view.ZoomUI;
import com.midi_automator.view.tray.Tray;
import com.midi_automator.view.tray.listener.TrayMenuCloseListener;
import com.midi_automator.view.windows.AddDialog.AddDialog;
import com.midi_automator.view.windows.EditDialog.EditDialog;
import com.midi_automator.view.windows.MainFrame.actions.ExitAction;
import com.midi_automator.view.windows.MainFrame.actions.ExportAction;
import com.midi_automator.view.windows.MainFrame.actions.HideShowMainFrameAction;
import com.midi_automator.view.windows.MainFrame.actions.ImportAction;
import com.midi_automator.view.windows.MainFrame.actions.OpenNextItemAction;
import com.midi_automator.view.windows.MainFrame.actions.OpenPreviousitemAction;
import com.midi_automator.view.windows.MainFrame.actions.PreferencesAction;
import com.midi_automator.view.windows.MainFrame.listener.GlobalKeyListener;
import com.midi_automator.view.windows.MainFrame.listener.HideMainFrameListener;
import com.midi_automator.view.windows.MainFrame.listener.MainFramePopupListener;
import com.midi_automator.view.windows.MainFrame.menus.MainFramePopupMenu;
import com.midi_automator.view.windows.PreferencesDialog.PreferencesDialog;

@org.springframework.stereotype.Component
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(MainFrame.class.getName());

	private final String TITLE = "Midi Automator";
	private final int MIN_WIDTH = 500;
	private final int MIN_HEIGHT = 610;
	public static final int TEST_POSITION_X = 0;
	public static final int TEST_POSITION_Y = 100;
	private final int FRAME_LOCATION_X_OFFSET = 50;
	private final int FRAME_LOCATION_Y_OFFSET = 50;
	private final int FOOTER_HEIGHT = 230;
	private final String FONT_FAMILY = "Arial";
	private String iconPathPrev;
	private String iconPathNext;
	private final String LABEL_MIDI_IN_DETECT = "IN";
	private final String LABEL_MIDI_OUT_DETECT = "OUT";

	public static final String NAME = "Main Frame";
	public static final String MENU_FILE = "File";
	public static final String MENU_ITEM_IMPORT = "Import...";
	public static final String MENU_ITEM_EXPORT = "Export...";
	public static final String MENU_ITEM_EXIT = "Exit";
	public static final String MENU_ITEM_PREFERENCES = "Preferences";
	public static final String MENU_ITEM_OPEN_MIDI_AUTOMATOR = "Open...";
	public static final String MENU_ITEM_HIDE_MIDI_AUTOMATOR = "Hide...";
	public static final String NAME_MENU_ITEM_IMPORT = "import";
	public static final String NAME_MENU_ITEM_EXPORT = "export";
	public static final String NAME_MENU_ITEM_PREFERENCES = "preferences";
	public static final String NAME_MENU_ITEM_EXIT = "exit";
	public static final String NAME_MENU_ITEM_OPEN_MIDI_AUTOMATOR = "open midi automator";
	public static final String NAME_PREV_BUTTON = "previous button";
	public static final String NAME_NEXT_BUTTON = "next button";
	public static final String NAME_INFO_LABEL = "info label";
	public static final String NAME_MIDI_IN_DETECT_LABEL = "midi IN label";
	public static final String NAME_MIDI_OUT_DETECT_LABEL = "midi OUT label";
	public static final String NAME_TRAY = "MIDI Automator";

	private ZoomUI zoomUI = new ZoomUI();
	private JPanel mainPanel = new JPanel();

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem importMenuItem;
	private JMenuItem exportMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem preferencesMenuItem;
	private BlinkingJLabel midiINdetect;
	private BlinkingJLabel midiOUTdetect;
	private HTMLLabel infoLabel;
	private JScrollPane itemListScrollPane;

	@Autowired
	private ItemList itemList;
	private CacheableJButton prevButton = new CacheableJButton();
	private CacheableJButton nextButton = new CacheableJButton();

	@Autowired
	private GlobalKeyListener globalKeyListener;
	@Autowired
	private HideMainFrameListener windowHideListener;
	@Autowired
	private TrayMenuCloseListener trayCloseListener;

	@Autowired
	private ExitAction exitAction;
	@Autowired
	private OpenPreviousitemAction prevAction;
	@Autowired
	private OpenNextItemAction nextAction;
	@Autowired
	private ImportAction importAction;
	@Autowired
	private ExportAction exportAction;
	@Autowired
	private PreferencesAction preferencesAction;

	private List<String> midiListeningSignatures = new ArrayList<String>();
	private List<String> midiSendingSignatures = new ArrayList<String>();

	private boolean popupWasShown;
	private boolean exiting;

	public static final int MIDI_DETECT_BLINK_RATE = 200;
	public static final Color MIDI_DETECT_COLOR = Color.YELLOW;

	public static final int METRONOM_BLINK_RATE = 200;
	public static final Color METRONOM_COLOR_FIRST_CLICK = Color.RED;
	public static final Color METRONOM_COLOR_OTHER_CLICK = Color.GREEN;

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private PreferencesDialog preferencesDialog;

	@Autowired
	private Presenter presenter;

	@Autowired
	private ImportExportService importExportService;
	@Autowired
	private ItemListService itemListService;
	@Autowired
	private MidiService midiService;
	@Autowired
	private MidiExecuteService midiRemoteOpenService;
	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;
	@Autowired
	private PresenterService presenterService;
	@Autowired
	private MidiLearnService midiLearnService;

	@Autowired
	private Resources resources;

	@Autowired
	private Tray tray;
	@Autowired
	private HideShowMainFrameAction hideShowAction;

	@Autowired
	private MainFramePopupListener popupListener;
	@Autowired
	private MainFramePopupMenu popupMenu;

	/**
	 * Initializes the frame
	 */
	public void init() {

		double zoomFactor = 1;

		setName(NAME);
		JFileChooser.setDefaultLocale(MidiAutomator.locale);

		int width = MIN_WIDTH;
		int height = MIN_HEIGHT;

		if (zoomFactor > 1) {
			width = new Double(MIN_WIDTH * (zoomFactor + 0.1)).intValue();
			height = new Double(MIN_HEIGHT * (zoomFactor + 0.1)).intValue();
		}

		setMinimumSize(new Dimension(width, height));
		setResizable(true);

		if (presenter.isInTestMode()) {
			this.setLocation(new Point(MainFrame.TEST_POSITION_X,
					MainFrame.TEST_POSITION_Y));
		}

		setTitle("");
		createIcons();

		// Menu
		createMenuItems();
		createMenu();

		// PopUp Menu
		popupMenu.init();
		popupWasShown = false;

		// Layout
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		// Header
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout());
		headerPanel.setBorder(BorderFactory.createMatteBorder(5, 20, 0, 20,
				getBackground()));
		headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

		JPanel headerContentPanel = new JPanel();
		headerContentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
		double size[][] = { { TableLayout.FILL, 30 },
				{ headerContentPanel.getMaximumSize().height } };
		headerContentPanel.setLayout(new TableLayout(size));
		headerContentPanel.add(createInfo(), "0, 0");
		headerContentPanel.add(createMidiDetectors(), "1, 0");

		headerPanel.add(headerContentPanel);

		mainPanel.add(headerPanel, BorderLayout.PAGE_START);

		// Middle
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout());
		middlePanel.setBorder(BorderFactory.createMatteBorder(15, 20, 10, 20,
				getBackground()));
		itemListScrollPane = createItemList();

		middlePanel.add(itemListScrollPane);
		mainPanel.add(middlePanel, BorderLayout.CENTER);

		// Footer
		JPanel footerPanel = new JPanel();
		footerPanel.setMaximumSize(new Dimension(width, FOOTER_HEIGHT));
		footerPanel.setLayout(new GridBagLayout());
		mainPanel.add(footerPanel, BorderLayout.PAGE_END);

		createSwitchButtons(footerPanel);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(MIN_WIDTH, MIN_HEIGHT);
		setAlwaysOnTop(true);
		addWindowListener(windowHideListener);

		// Tray
		tray.init();
		GUIUtils.addMouseListenerToAllComponents(this, trayCloseListener);

		zoomUI.setZoom(zoomUI.getZoom() * zoomFactor);
		JLayer<JComponent> mainLayer = new JLayer<JComponent>(mainPanel, zoomUI);
		add(mainLayer);
		setVisible(true);
	}

	public boolean isExiting() {
		return exiting;
	}

	public void setExiting(boolean exiting) {
		this.exiting = exiting;
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(TITLE + " " + resources.getVersion() + title);
	}

	public HTMLLabel getInfoLabel() {
		return infoLabel;
	}

	public ItemList getItemList() {
		return itemList;
	}

	public int getLastSelectedIndex() {
		return itemList.getLastSelectedIndex();
	}

	/**
	 * Sets the file entries
	 * 
	 * @param entries
	 *            The entries for the files
	 */
	public void setItems(List<String> entries) {
		itemList.setItems(entries);
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
	 * 
	 */
	public void reload() {

		if (itemList.getItems().length == 0) {
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
					+ "\nSending: " + sendingSignature, ItemList.NAME, i);
		}

		// reload preferences frame
		if (preferencesDialog != null && preferencesDialog.isVisible()) {
			preferencesDialog.reload();
		}
	}

	/**
	 * Sets the text of the info label
	 * 
	 * @param text
	 */
	public void setInfoText(String text) {
		if (infoLabel != null) {
			infoLabel.setText("<span style='font-family:Arial'>" + text
					+ "</span>");
		}
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

		if (component instanceof CacheableBlinkableToolTipJList) {
			((CacheableBlinkableToolTipJList<?>) component).setToolTipText(
					text, index);
		}
	}

	/**
	 * Puts the frame to normal mode.
	 */
	public void midiLearnOff() {

		// set all frames to midi learn off
		if (preferencesDialog != null && preferencesDialog.isVisible()) {
			preferencesDialog.midiLearnOff();
		}

		// change menu item
		popupMenu.midiLearnOff();
		tray.midiLearnOff();

		learnOff();
	}

	/**
	 * Puts the frame to normal mode.
	 */
	public void keyLearnOff() {

		// set all frames to midi learn off
		if (preferencesDialog != null && preferencesDialog.isVisible()) {
			preferencesDialog.keyLearnOff();
		}

		learnOff();
	}

	/**
	 * Puts the frame to normal mode.
	 */
	private void learnOff() {

		// enable inputs
		GUIUtils.disEnableAllInputs(this, true);

		GUIUtils.deHighlightComponent(prevButton, false);
		GUIUtils.deHighlightComponent(nextButton, false);
		GUIUtils.deHighlightListItem(itemList, false);

		Object cache = itemList.getCache();
		if (cache instanceof Color) {
			itemList.setSelectionBackground((Color) cache);
		}
	}

	/**
	 * Puts the GUI to the learning mode
	 */
	private void learnOn() {

		// disable all inputs
		GUIUtils.disEnableAllInputs(this, false);

		// activate popup menu listener
		popupListener.setActive(true);
	}

	/**
	 * Puts the GUI to the learning mode the given component.
	 * 
	 * @param learningComponent
	 *            The learning component to highlight
	 * 
	 */
	private void learnOn(Component learningComponent) {

		learnOn();

		String componentName = learningComponent.getName();

		switch (componentName) {
		case ItemList.NAME:
			GUIUtils.deHighlightListItem((JList<?>) learningComponent, true);
			log.info("Learning for item index: "
					+ (itemList.getSelectedIndex() + 1));
			break;

		case NAME_PREV_BUTTON:
			GUIUtils.deHighlightComponent((JComponent) learningComponent, true);
			log.info("Learning for button: " + learningComponent.getName());
			break;

		case NAME_NEXT_BUTTON:
			GUIUtils.deHighlightComponent((JComponent) learningComponent, true);
			log.info("Learning for button: " + learningComponent.getName());
			break;

		case NAME:
			log.info("Learning for : " + learningComponent.getName());

		}
	}

	/**
	 * Puts the frame to midi learn mode
	 * 
	 */
	public void midiLearnOn() {

		// set all frames to midi learn on
		if (preferencesDialog != null && preferencesDialog.isVisible()) {
			preferencesDialog.midiLearnOn();
		}

		// change menu item text
		popupMenu.midiLearnOn();
		learnOn();
	}

	/**
	 * Puts the frame to midi learn mode for the given component key
	 * 
	 * @param key
	 *            The key of the component to learn for
	 */
	public void midiLearnOn(String key) {

		// set all frames to midi learn on
		if (preferencesDialog != null && preferencesDialog.isVisible()) {
			preferencesDialog.midiLearnOn(key);
		}

		// change menu item text
		popupMenu.midiLearnOn();
		tray.midiLearnOn();

		Component learningComponent = getLearningComponentFromKey(key);
		learnOn(learningComponent);
	}

	/**
	 * Gets the learning component for the key
	 * 
	 * @param key
	 *            The learning component key
	 * @return The learning component
	 */
	private Component getLearningComponentFromKey(String key) {

		key = midiLearnService.getKeyFromIndexedMidiLearnKey(key);

		switch (key) {
		case MidiLearnService.KEY_MIDI_LEARN_AUTOMATION_TRIGGER:
			return preferencesDialog.getGuiAutomationPanel()
					.getGUIAutomationsTable();
		case MidiLearnService.KEY_MIDI_LEARN_ITEM_LIST_ENTRY:
			return itemList;
		case MidiLearnService.KEY_MIDI_LEARN_NEXT_BUTTON:
			return nextButton;
		case MidiLearnService.KEY_MIDI_LEARN_PREVIOUS_BUTTON:
			return prevButton;
		case MidiLearnService.KEY_MIDI_LEARN_HIDE_SHOW_MAIN_FRAME:
			return this;

		}

		return null;
	}

	/**
	 * Puts the frame to key learn mode
	 * 
	 * @param learningComponent
	 *            The component to learn for
	 */
	public void keyLearnOn(JComponent learningComponent) {

		if (preferencesDialog != null && preferencesDialog.isVisible()) {
			preferencesDialog.keyLearnOn(learningComponent);
		}

		learnOn(learningComponent);
	}

	/**
	 * Blinks the MIDI IN detector
	 */
	public void blinkMidiINDetect() {
		log.debug("Blink MIDI IN");
		midiINdetect.startBlinking();
	}

	/**
	 * Blinks the MIDI OUT detector
	 */
	public void blinkMidiOUTDetect() {
		log.debug("Blink MIDI OUT");
		midiOUTdetect.startBlinking();
	}

	/**
	 * Flashes the background color of the content pane.
	 * 
	 * @beat the current clicked beat
	 */
	public void blinkMetronom(int beat) {
		itemList.blinkMetronom(beat);
	}

	/**
	 * Creates all menus.
	 */
	private void createMenu() {

		menuBar = new JMenuBar();
		fileMenu = new JMenu(MENU_FILE);

		menuBar.add(fileMenu);
		fileMenu.add(importMenuItem);
		fileMenu.add(exportMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(preferencesMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		if (System.getProperty("os.name").contains("Windows")) {
			fileMenu.setMnemonic(KeyEvent.VK_F);
		}

		setJMenuBar(menuBar);
	}

	/**
	 * Creates all menu items
	 */
	private void createMenuItems() {

		importMenuItem = new JMenuItem(MENU_ITEM_IMPORT);
		importMenuItem.setName(NAME_MENU_ITEM_IMPORT);
		importMenuItem.setEnabled(true);
		importMenuItem.addActionListener(importAction);
		importMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.ALT_MASK));

		exportMenuItem = new JMenuItem(MENU_ITEM_EXPORT);
		exportMenuItem.setName(NAME_MENU_ITEM_EXPORT);
		exportMenuItem.setEnabled(true);
		exportMenuItem.addActionListener(exportAction);
		exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.ALT_MASK));

		preferencesMenuItem = new JMenuItem(MENU_ITEM_PREFERENCES);
		preferencesMenuItem.setName(NAME_MENU_ITEM_PREFERENCES);
		preferencesMenuItem.setEnabled(true);
		preferencesMenuItem.addActionListener(preferencesAction);
		preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_P, ActionEvent.ALT_MASK));

		exitMenuItem = new JMenuItem(MENU_ITEM_EXIT);
		exitMenuItem.setName(NAME_MENU_ITEM_EXIT);
		exitMenuItem.setEnabled(true);
		exitMenuItem.addActionListener(exitAction);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.ALT_MASK));
	}

	/**
	 * Creates the icons of the application
	 */
	private void createIcons() {

		String icon16 = resources.getImagePath() + File.separator
				+ "MidiAutomatorIcon16.png";
		String icon32 = resources.getImagePath() + File.separator
				+ "MidiAutomatorIcon32.png";
		String icon64 = resources.getImagePath() + File.separator
				+ "MidiAutomatorIcon64.png";
		String icon128 = resources.getImagePath() + File.separator
				+ "MidiAutomatorIcon128.png";
		String icon256 = resources.getImagePath() + File.separator
				+ "MidiAutomatorIcon256.png";
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(icon16).getImage());
		icons.add(new ImageIcon(icon32).getImage());
		icons.add(new ImageIcon(icon64).getImage());
		icons.add(new ImageIcon(icon128).getImage());
		icons.add(new ImageIcon(icon256).getImage());
		setIconImages(icons);
	}

	/**
	 * Creates the info label on top
	 * 
	 * @return a scroll pane
	 */
	private JScrollPane createInfo() {

		infoLabel = new HTMLLabel();
		infoLabel.setName(NAME_INFO_LABEL);
		infoLabel.addKeyListener(globalKeyListener);
		infoLabel.requestFocusInWindow();

		JScrollPane scrollingInfoLabel = new JScrollPane(infoLabel);
		scrollingInfoLabel.getViewport().setBackground(this.getBackground());

		return scrollingInfoLabel;
	}

	/**
	 * Creates the midi detectors
	 * 
	 * @return a JPanel with the detectors
	 */
	private JPanel createMidiDetectors() {
		final Font font = new Font(FONT_FAMILY, Font.PLAIN, 10);

		midiINdetect = new BlinkingJLabel(LABEL_MIDI_IN_DETECT);
		midiINdetect.setBlinkingStrategy(new BlinkingStrategy(midiINdetect,
				MainFrame.MIDI_DETECT_COLOR, MainFrame.MIDI_DETECT_BLINK_RATE,
				1));
		midiOUTdetect = new BlinkingJLabel(LABEL_MIDI_OUT_DETECT);
		midiOUTdetect.setBlinkingStrategy(new BlinkingStrategy(midiOUTdetect,
				MainFrame.MIDI_DETECT_COLOR, MainFrame.MIDI_DETECT_BLINK_RATE,
				1));
		midiINdetect.setName(NAME_MIDI_IN_DETECT_LABEL);
		midiOUTdetect.setName(NAME_MIDI_OUT_DETECT_LABEL);
		midiINdetect.setFont(font);
		midiOUTdetect.setFont(font);
		midiINdetect.setHorizontalAlignment(SwingConstants.CENTER);
		midiOUTdetect.setHorizontalAlignment(SwingConstants.CENTER);
		midiINdetect.setBorder(BorderFactory.createLineBorder(Color.black));
		midiOUTdetect.setBorder(BorderFactory.createLineBorder(Color.black));
		midiINdetect.setOpaque(true);
		midiOUTdetect.setOpaque(true);

		JPanel detectorPanel = new JPanel();
		detectorPanel.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0,
				getBackground()));
		GridLayout grid = new GridLayout(2, 1);
		grid.setVgap(3);
		detectorPanel.setLayout(grid);
		detectorPanel.add(midiINdetect);
		detectorPanel.add(midiOUTdetect);
		return detectorPanel;
	}

	/**
	 * Creates the scrollable list of items
	 * 
	 * @return The ScrollPane with the item list
	 */
	private JScrollPane createItemList() {

		itemList.init();
		itemList.addMouseListener(popupListener);
		itemList.setFocusable(false);
		JScrollPane itemListScrollPane = new JScrollPane(itemList);
		itemListScrollPane.setViewportView(itemList);

		return itemListScrollPane;
	}

	/**
	 * Creates the previous and next buttons
	 * 
	 * @param parent
	 *            The parent container
	 */
	private void createSwitchButtons(Container parent) {

		iconPathPrev = resources.getImagePath() + File.separator
				+ "arrow_prev.png";
		iconPathNext = resources.getImagePath() + File.separator
				+ "arrow_next.png";

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 10, 0);
		c.gridy = 0;

		prevButton.setName(NAME_PREV_BUTTON);
		prevButton.setAction(prevAction);
		prevButton.setIcon(new ImageIcon(iconPathPrev));
		log.debug("Loading \"prev\" icon: " + iconPathPrev);
		prevButton.addMouseListener(popupListener);
		prevButton.setFocusable(false);

		c.gridx = 0;
		parent.add(prevButton, c);
		prevButton.setCache(prevButton.getBorder());

		nextButton.setName(NAME_NEXT_BUTTON);
		nextButton.setAction(nextAction);
		log.debug("Loading \"next\" icon: " + iconPathNext);
		nextButton.setIcon(new ImageIcon(iconPathNext));
		nextButton.addMouseListener(popupListener);
		nextButton.setFocusable(false);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		parent.add(nextButton, c);
		nextButton.setCache(prevButton.getBorder());
	}

	/**
	 * Returns the name for a component that can take a midi signature
	 * 
	 * @param component
	 *            The component
	 * @return A printable unique name
	 */
	public String getMidiComponentName(Component component) {

		if (component.getName().equals(ItemList.NAME)) {

			return itemListService.getEntryNameByIndex(itemList
					.getSelectedIndex());
		}

		if (component instanceof JButton) {
			return component.getName();
		}
		return null;
	}

	/**
	 * Gets the preferences dialog
	 * 
	 * @return The preferences dialog
	 */
	public PreferencesDialog getPreferencesDialog() {
		return preferencesDialog;
	}

	public boolean isPopupWasShown() {
		return popupWasShown;
	}

	public void setPopupWasShown(boolean popupWasShown) {
		this.popupWasShown = popupWasShown;
	}

	/**
	 * Opens the add dialog
	 */
	public void openAddDialog() {

		AddDialog addDialog = ctx.getBean("addDialog", AddDialog.class);
		addDialog.init();
		addDialog.setLocation(
				getLocationOnScreen().x + FRAME_LOCATION_X_OFFSET,
				getLocationOnScreen().y + FRAME_LOCATION_Y_OFFSET);
		addDialog.showDialog();
	}

	/**
	 * Opens the edit dialog with the selected item
	 */
	public void openEditDialog() {

		EditDialog editDialog = ctx.getBean(EditDialog.class);
		editDialog.init(itemList.getSelectedIndex());
		editDialog.setLocation(getLocationOnScreen().x
				+ FRAME_LOCATION_X_OFFSET, getLocationOnScreen().y
				+ FRAME_LOCATION_Y_OFFSET);
		editDialog.showDialog();
	}
}
