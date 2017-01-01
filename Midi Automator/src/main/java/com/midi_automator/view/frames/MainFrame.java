package com.midi_automator.view.frames;

import java.awt.AWTException;
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
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.midi_automator.MidiAutomator;
import com.midi_automator.Resources;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.FileListService;
import com.midi_automator.presenter.services.ImportExportService;
import com.midi_automator.presenter.services.MidiItemChangeNotificationService;
import com.midi_automator.presenter.services.MidiRemoteOpenService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.utils.GUIUtils;
import com.midi_automator.view.BlinkingJLabel;
import com.midi_automator.view.BlinkingStrategy;
import com.midi_automator.view.CacheableBlinkableToolTipJList;
import com.midi_automator.view.CacheableJButton;
import com.midi_automator.view.DeActivateableMouseAdapter;
import com.midi_automator.view.HTMLLabel;
import com.midi_automator.view.IBlinkingStrategy;
import com.midi_automator.view.IToolTipItem;
import com.midi_automator.view.MainFramePopupMenu;
import com.midi_automator.view.ToolTipItemImpl;
import com.midi_automator.view.TransferableJListToolTipItem;

@org.springframework.stereotype.Component
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(MainFrame.class.getName());

	private final String TITLE = "Midi Automator";
	private final int WIDTH = 500;
	private final int HEIGHT = 610;
	public static final int TEST_POSITION_X = 0;
	public static final int TEST_POSITION_Y = 50;
	private final int FRAME_LOCATION_X_OFFSET = 50;
	private final int FRAME_LOCATION_Y_OFFSET = 50;
	private final int MAIN_LAYOUT_HORIZONTAL_GAP = 10;
	private final int MAIN_LAYOUT_VERTICAL_GAP = 10;
	private final int FONT_SIZE_FILE_LIST = 26;
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
	public static final String NAME_MENU_ITEM_IMPORT = "import";
	public static final String NAME_MENU_ITEM_EXPORT = "export";
	public static final String NAME_MENU_ITEM_PREFERENCES = "preferences";
	public static final String NAME_MENU_ITEM_EXIT = "exit";
	public static final String NAME_MENU_ITEM_OPEN_MIDI_AUTOMATOR = "open midi automator";
	public static final String NAME_PREV_BUTTON = "previous button";
	public static final String NAME_NEXT_BUTTON = "next button";
	public static final String NAME_FILE_LIST = "file list";
	public static final String NAME_INFO_LABEL = "info label";
	public static final String NAME_MIDI_IN_DETECT_LABEL = "midi IN label";
	public static final String NAME_MIDI_OUT_DETECT_LABEL = "midi OUT label";
	public static final String NAME_MIDI_IMPORT_FILECHOOSER = "import file chooser";
	public static final String NAME_MIDI_EXPORT_FILECHOOSER = "export file chooser";
	public static final String NAME_TRAY = "MIDI Automator";

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem importMenuItem;
	private JMenuItem exportMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem preferencesMenuItem;
	private PopupMenu trayPopupMenu;
	private MenuItem restoreMainFrameMenuItem;
	private BlinkingJLabel midiINdetect;
	private BlinkingJLabel midiOUTdetect;
	private HTMLLabel infoLabel;
	private JScrollPane fileListScrollPane;
	private CacheableBlinkableToolTipJList<IToolTipItem> fileList = new CacheableBlinkableToolTipJList<IToolTipItem>();
	private CacheableJButton prevButton = new CacheableJButton();
	private CacheableJButton nextButton = new CacheableJButton();
	private TrayIcon trayIcon;

	private KeyListener globalKeyListener = new GlobalKeyListener();
	private PopupListener popupListener = new PopupListener();
	private AbstractAction prevAction = new PrevAction();
	private AbstractAction nextAction = new NextAction();

	private List<String> fileEntries = new ArrayList<String>();
	private List<String> midiListeningSignatures = new ArrayList<String>();
	private List<String> midiSendingSignatures = new ArrayList<String>();

	private int lastSelectedIndex;
	private boolean popupWasShown;
	private boolean exiting;

	public static final int MIDI_DETECT_BLINK_RATE = 200;
	public static final Color MIDI_DETECT_COLOR = Color.YELLOW;

	public static final int METRONOM_BLINK_RATE = 200;
	public static final Color METRONOM_COLOR_FIRST_CLICK = Color.RED;
	public static final Color METRONOM_COLOR_OTHER_CLICK = Color.GREEN;
	private IBlinkingStrategy firstClickStrategy;
	private IBlinkingStrategy otherClickStrategy;

	@Autowired
	private ApplicationContext ctx;

	private PreferencesDialog preferencesDialog;

	@Autowired
	private Presenter presenter;

	@Autowired
	private ImportExportService importExportService;
	@Autowired
	private FileListService fileListService;
	@Autowired
	private MidiService midiService;
	@Autowired
	private MidiRemoteOpenService midiRemoteOpenService;
	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;

	@Autowired
	private Resources resources;

	@Autowired
	private MainFramePopupMenu popupMenu;

	/**
	 * Initializes the frame
	 */
	public void init() {

		setName(NAME);
		JFileChooser.setDefaultLocale(MidiAutomator.locale);

		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setResizable(true);

		if (presenter.isInTestMode()) {
			this.setLocation(new Point(MainFrame.TEST_POSITION_X,
					MainFrame.TEST_POSITION_Y));
		}

		setTitle("");

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

		// Tray
		createTray(icon16);

		iconPathPrev = resources.getImagePath() + File.separator
				+ "arrow_prev.png";
		iconPathNext = resources.getImagePath() + File.separator
				+ "arrow_next.png";

		// Menu
		createMenuItems();
		createMenu();

		// PopUp Menu
		popupMenu.init();
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

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setAlwaysOnTop(true);
		setVisible(true);

		addWindowListener(new WindowHideListener(this));
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

	public CacheableBlinkableToolTipJList<IToolTipItem> getFileList() {
		return fileList;
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
	 * 
	 */
	public void reload() {

		// // set list entries
		fileList.setListData(createViewableFileList(fileEntries));

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
	private void learnOff() {

		// enable inputs
		GUIUtils.disEnableAllInputs(this, true);

		// change menu item
		popupMenu.midiLearnOff();

		GUIUtils.deHighlightComponent(prevButton, false);
		GUIUtils.deHighlightComponent(nextButton, false);
		GUIUtils.deHighlightListItem(fileList, false);

		Object cache = fileList.getCache();
		if (cache instanceof Color) {
			fileList.setSelectionBackground((Color) cache);
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
	 * Puts the GUI to the learning mode
	 * 
	 * @param learningComponent
	 */
	private void learnOn(JComponent learningComponent) {

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

		// highlight component
		if (learningComponent.getName() != null) {
			if (learningComponent.getName().equals(NAME_FILE_LIST)) {

				GUIUtils.deHighlightListItem(fileList, true);

				log.info("Learning for index: "
						+ (fileList.getSelectedIndex() + 1));

			} else {

				if (learningComponent.getName().equals(NAME_PREV_BUTTON)
						|| learningComponent.getName().equals(NAME_NEXT_BUTTON)) {
					GUIUtils.deHighlightComponent(learningComponent, true);

					log.info("Learning for button: "
							+ learningComponent.getName());
				}
			}
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
		if (preferencesDialog != null && preferencesDialog.isVisible()) {
			preferencesDialog.midiLearnOn(learningComponent);
		}

		learnOn(learningComponent);

		// change menu item text
		popupMenu.midiLearnOn();

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
	 * Sets the selected item in the file list
	 * 
	 * @param index
	 *            The index
	 */
	public void setSelectedIndex(int index) {
		fileList.setSelectedIndex(index);
		fileList.ensureIndexIsOnTop(index);
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

		if (beat == 1) {
			log.debug("Metronom first click");
			fileList.setBlinkingStrategy(firstClickStrategy);
			fileList.startBlinking();
		} else {
			log.debug("Metronom other click");
			fileList.setBlinkingStrategy(otherClickStrategy);
			fileList.startBlinking();
		}
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
		importMenuItem.addActionListener(new ImportAction(this));
		importMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.ALT_MASK));

		exportMenuItem = new JMenuItem(MENU_ITEM_EXPORT);
		exportMenuItem.setName(NAME_MENU_ITEM_EXPORT);
		exportMenuItem.setEnabled(true);
		exportMenuItem.addActionListener(new ExportAction(this));
		exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.ALT_MASK));

		preferencesMenuItem = new JMenuItem(MENU_ITEM_PREFERENCES);
		preferencesMenuItem.setName(NAME_MENU_ITEM_PREFERENCES);
		preferencesMenuItem.setEnabled(true);
		preferencesMenuItem.addActionListener(new PreferencesAction());
		preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_P, ActionEvent.ALT_MASK));

		exitMenuItem = new JMenuItem(MENU_ITEM_EXIT);
		exitMenuItem.setName(NAME_MENU_ITEM_EXIT);
		exitMenuItem.setEnabled(true);
		exitMenuItem.addActionListener(new ExitAction(this));
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.ALT_MASK));
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
		infoLabel.setName(NAME_INFO_LABEL);
		infoLabel.setPreferredSize(dimension);
		infoLabel.addKeyListener(globalKeyListener);
		infoLabel.requestFocusInWindow();

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
		midiINdetect.setPreferredSize(dimension);
		midiOUTdetect.setPreferredSize(dimension);
		midiINdetect.setOpaque(true);
		midiOUTdetect.setOpaque(true);

		JPanel detectorPanel = new JPanel();
		GridLayout grid = new GridLayout(2, 1);
		grid.setVgap(3);
		detectorPanel.setLayout(grid);
		detectorPanel.add(midiINdetect);
		detectorPanel.add(midiOUTdetect);
		parent.add(detectorPanel);
	}

	/**
	 * Creates a tray icon
	 * 
	 * @param iconFileName
	 *            The path to the icon file
	 */
	private void createTray(String iconFileName) {

		if (SystemTray.isSupported()) {

			SystemTray tray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage(iconFileName);

			ActionListener mainFrameRestoreAction = new MainFrameRestoreAction(
					this);

			trayPopupMenu = new PopupMenu();
			restoreMainFrameMenuItem = new MenuItem(
					MENU_ITEM_OPEN_MIDI_AUTOMATOR);
			restoreMainFrameMenuItem.addActionListener(mainFrameRestoreAction);
			trayPopupMenu.add(restoreMainFrameMenuItem);
			trayIcon = new TrayIcon(image, NAME_TRAY, trayPopupMenu);
			trayIcon.addActionListener(mainFrameRestoreAction);

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				log.error("Error on adding tray icon.", e);
			}
		}
	}

	/**
	 * Creates the list of files to open in the middle
	 * 
	 * @param parent
	 *            The parent Container
	 */
	private void createFileList(Container parent) {

		firstClickStrategy = new BlinkingStrategy(fileList,
				MainFrame.METRONOM_COLOR_FIRST_CLICK,
				MainFrame.METRONOM_BLINK_RATE, 1);
		otherClickStrategy = new BlinkingStrategy(fileList,
				MainFrame.METRONOM_COLOR_OTHER_CLICK,
				MainFrame.METRONOM_BLINK_RATE, 1);

		fileList.setBlinkingStrategy(otherClickStrategy);
		fileList.addMouseListener(new OpenFileOnDoubleClick());
		fileList.addMouseListener(popupListener);
		fileList.setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_FILE_LIST));
		fileList.setName(NAME_FILE_LIST);
		fileList.setCache(fileList.getSelectionBackground());
		fileList.setFocusable(false);
		fileList.setDropMode(DropMode.INSERT);
		fileList.setTransferHandler(new FileListTransferHandler());
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.addListSelectionListener(new CurrentSelectionHandler());

		DragSource fileListDragSource = new DragSource();
		fileListDragSource.createDefaultDragGestureRecognizer(fileList,
				DnDConstants.ACTION_MOVE, new FileListDragGestureListener());

		fileListScrollPane = new JScrollPane(fileList);
		fileListScrollPane.setViewportView(fileList);
		parent.add(fileListScrollPane, BorderLayout.CENTER);
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

		prevButton.setName(NAME_PREV_BUTTON);
		prevButton.setPreferredSize(dimension);
		prevButton.setAction(prevAction);
		prevButton.setIcon(new ImageIcon(iconPathPrev));
		log.debug("Loading \"prev\" icon: " + iconPathPrev);
		prevButton.addMouseListener(popupListener);
		prevButton.setFocusable(false);

		c.gridx = 0;
		parent.add(prevButton, c);
		prevButton.setCache(prevButton.getBorder());

		nextButton.setName(NAME_NEXT_BUTTON);
		nextButton.setPreferredSize(dimension);
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

			return fileListService.getEntryNameByIndex(fileList
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
			fileListService.openPreviousFile();
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
			fileListService.openNextFile();
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
							fileListService.selectEntryByIndex(
									lastSelectedIndex, true);
						}
						popupWasShown = false;
					}
				}
			}
		}
	}

	/**
	 * Hides the window and displays a dialog that the program was just hidden.
	 * 
	 * @author aguelle
	 *
	 */
	class WindowHideListener extends WindowAdapter {

		private final String DIALOG_MESSAGE = "The program window will be minimized to the system tray.";
		private MainFrame programFrame;

		public WindowHideListener(MainFrame programFrame) {
			this.programFrame = programFrame;
		}

		@Override
		public void windowClosing(WindowEvent e) {

			if (!programFrame.isExiting()) {

				JFrame frame = new JFrame();
				frame.setAlwaysOnTop(true);

				if (System.getProperty("os.name").contains("Windows")) {
					// Show dialog
					JOptionPane.showMessageDialog(frame, DIALOG_MESSAGE);

					// hide window
					programFrame.setVisible(false);
				} else {
					new ExitAction(programFrame).actionPerformed(null);
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
		private MainFrame programFrame;

		public ExitAction(MainFrame programFrame) {
			this.programFrame = programFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			programFrame.setExiting(true);
			programFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			presenter.close();

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
	 * Opens the dialog for importing the zip file.
	 * 
	 * @author aguelle
	 * 
	 */
	class ImportAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private JFrame programFrame;
		private final JFileChooser fileChooser = new JFileChooser();

		public ImportAction(JFrame programFrame) {
			super();
			this.programFrame = programFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			FileFilter filter = new FileNameExtensionFilter(
					ImportExportService.MIDI_AUTOMATOR_FILE_TYPE,
					ImportExportService.MIDI_AUTOMATOR_FILE_EXTENSIONS);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(filter);
			fileChooser.setName(NAME_MIDI_IMPORT_FILECHOOSER);
			int returnVal = fileChooser.showOpenDialog(programFrame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				importExportService.importMidautoFile(file);

			}
		}
	}

	/**
	 * Restores the Main Frame.
	 * 
	 * @author aguelle
	 * 
	 */
	class MainFrameRestoreAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private JFrame programFrame;

		public MainFrameRestoreAction(JFrame programFrame) {
			super();
			this.programFrame = programFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			programFrame.setState(Frame.NORMAL);
			programFrame.setVisible(true);
		}
	}

	/**
	 * Exports the set list and the preferences to a zipped file.
	 * 
	 * @author aguelle
	 * 
	 */
	class ExportAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private JFrame programFrame;
		private final JFileChooser fileChooser = new JFileChooser();

		public ExportAction(JFrame programFrame) {
			super();
			this.programFrame = programFrame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			FileFilter filter = new FileNameExtensionFilter(
					ImportExportService.MIDI_AUTOMATOR_FILE_TYPE,
					ImportExportService.MIDI_AUTOMATOR_FILE_EXTENSIONS);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(filter);
			fileChooser.setName(NAME_MIDI_EXPORT_FILECHOOSER);
			int returnVal = fileChooser.showSaveDialog(programFrame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String filePath = fileChooser.getSelectedFile()
						.getAbsolutePath();
				String fileExtension = "."
						+ ImportExportService.MIDI_AUTOMATOR_FILE_EXTENSIONS[0];

				if (!filePath.endsWith(fileExtension)) {
					filePath += fileExtension;
				}

				importExportService.exportMidautoFile(filePath);
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

		@Override
		public void actionPerformed(ActionEvent e) {

			preferencesDialog = ctx.getBean("preferencesDialog",
					PreferencesDialog.class);
			preferencesDialog.init();
			preferencesDialog.setLocation(getLocationOnScreen());
			preferencesDialog.showDialog();
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

			// getting midi device names
			String midiInRemoteDeviceName = midiService
					.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);
			String switchItemDeviceName = midiService
					.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE);

			if (componentName != null) {

				// show pop-up of file list
				if (componentName.equals(MainFrame.NAME_FILE_LIST)) {

					// set selection in file list
					fileList.setSelectedIndex(fileList.locationToIndex(e
							.getPoint()));

					popupMenu.configureFileListPopupMenu();
					popupMenu.show(e.getComponent(), e.getX(), e.getY());

					// en/disable edit
					popupMenu.getEditMenuItem().setEnabled(false);
					if (fileList.getSelectedIndex() > -1) {
						popupMenu.getEditMenuItem().setEnabled(true);
					}

					// en/disable delete
					popupMenu.getDeleteMenuItem().setEnabled(false);
					if (fileList.getSelectedIndex() > -1) {
						popupMenu.getDeleteMenuItem().setEnabled(true);
					}

					// en/disable move up
					popupMenu.getMoveUpMenuItem().setEnabled(false);
					if (!isFirstItem() && fileList.getSelectedIndex() > -1) {
						popupMenu.getMoveUpMenuItem().setEnabled(true);
					}

					// en/disable move down
					popupMenu.getMoveDownMenuItem().setEnabled(false);
					if (!isLastItem() && fileList.getSelectedIndex() > -1) {
						popupMenu.getMoveDownMenuItem().setEnabled(true);
					}

					// en/disable midi learn
					popupMenu.getMidiLearnMenuItem().setEnabled(false);
					if (fileList.getSelectedIndex() > -1
							&& midiInRemoteDeviceName != null
							&& !midiInRemoteDeviceName
									.equals(MidiAutomatorProperties.VALUE_NULL)) {
						popupMenu.getMidiLearnMenuItem().setEnabled(true);
					}

					// en/disable send midi
					String sendingSignature = fileListService
							.getMidiFileListSendingSignature(fileList
									.getSelectedIndex());

					popupMenu.getSendMidiMenuItem().setEnabled(false);
					if (switchItemDeviceName != null
							&& !switchItemDeviceName
									.equals(MidiAutomatorProperties.VALUE_NULL)
							&& sendingSignature != null) {
						popupMenu.getSendMidiMenuItem().setEnabled(true);
					}

					popupWasShown = true;
				}

				// en/disable midi unlearn
				popupMenu.getMidiUnlearnMenuItem().setEnabled(false);
				if (midiRemoteOpenService.isMidiLearned(component)) {
					popupMenu.getMidiUnlearnMenuItem().setEnabled(true);
				}

				// show pop-up of switch buttons
				if (componentName.equals(MainFrame.NAME_NEXT_BUTTON)
						|| (componentName.equals(MainFrame.NAME_PREV_BUTTON))) {

					if (midiInRemoteDeviceName != null
							&& !midiInRemoteDeviceName
									.equals(MidiAutomatorProperties.VALUE_NULL)) {
						popupMenu.getMidiLearnMenuItem().setEnabled(true);
					}

					popupMenu.configureSwitchButtonPopupMenu();
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
					popupWasShown = true;
				}
			}

		}
	}

	/**
	 * Drag listener for the file list. This allows to drag list items from one
	 * index to another.
	 * 
	 * @author aguelle
	 *
	 */
	class FileListDragGestureListener implements DragGestureListener {

		@Override
		public void dragGestureRecognized(DragGestureEvent dge) {

			Component component = dge.getComponent();

			@SuppressWarnings("unchecked")
			JList<IToolTipItem> fileList = (JList<IToolTipItem>) component;

			IToolTipItem item = fileList.getSelectedValue();
			int index = fileList.getSelectedIndex();

			if (item != null) {
				dge.startDrag(null, new TransferableJListToolTipItem(item,
						index));
				log.debug("Dragging \"" + item.getValue() + "\"");
			}
		}

	}

	/**
	 * The transfer handler for drag and dropping the file list items.
	 * 
	 * @author aguelle
	 *
	 */
	class FileListTransferHandler extends TransferHandler {

		private static final long serialVersionUID = 1L;

		@Override
		public boolean canImport(TransferSupport support) {
			if (!support.isDrop()) {
				return false;
			}

			return support.isDataFlavorSupported(DataFlavor.stringFlavor);
		}

		@Override
		public boolean importData(TransferSupport support) {

			if (!canImport(support)) {
				return false;
			}

			Transferable transferable = support.getTransferable();

			try {
				TransferableJListToolTipItem transferItem = (TransferableJListToolTipItem) transferable
						.getTransferData(TransferableJListToolTipItem.toolTipItemFlavor);
				JList.DropLocation dl = (JList.DropLocation) support
						.getDropLocation();

				int oldIndex = transferItem.getIndex();

				// temporarily save item data before deleting the item
				String entryName = fileListService
						.getEntryNameByIndex(oldIndex);
				String filePath = fileListService
						.getEntryFilePathByIndex(oldIndex);
				String programPath = fileListService
						.getEntryProgramPathByIndex(oldIndex);
				String midiListeningSignature = fileListService
						.getMidiFileListListeningSignature(oldIndex);
				String midiSendingSignature = fileListService
						.getMidiFileListSendingSignature(oldIndex);

				// delete the item from the list
				fileListService.deleteItem(transferItem.getIndex());

				// set item to the new index
				int newIndex = dl.getIndex();
				ListModel<IToolTipItem> model = fileList.getModel();

				if (newIndex > model.getSize()) {
					newIndex = model.getSize();
				}

				fileListService.insertItem(newIndex, entryName, filePath,
						programPath, midiListeningSignature,
						midiSendingSignature, false);

				reload();
				log.debug("Moved \"" + entryName + "\" from index " + oldIndex
						+ " to index " + newIndex);

			} catch (UnsupportedFlavorException e) {
				log.error("TransferFlavor for set list item not supported.", e);
				return false;
			} catch (IOException e) {
				log.error("IO failure on dropping set list item.", e);
				return false;
			}

			return true;
		}
	}

	/**
	 * Checks if the selected item is the first item in the list.
	 * 
	 * @return <TRUE> if it is the first item, <FALSE> if it is not the first
	 *         item
	 */
	private boolean isFirstItem() {
		boolean result = false;
		if (fileList.getSelectedIndex() == 0) {
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
		if (fileList.getSelectedIndex() == (fileEntries.size() - 1)) {
			result = true;
		}
		return result;
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
		editDialog.init(fileList.getSelectedIndex());
		editDialog.setLocation(getLocationOnScreen().x
				+ FRAME_LOCATION_X_OFFSET, getLocationOnScreen().y
				+ FRAME_LOCATION_Y_OFFSET);
		editDialog.showDialog();
	}

	/**
	 * Global key listener for "any time" key strokes.
	 * 
	 * @author aguelle
	 *
	 */
	class GlobalKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {

			int keyCode = e.getKeyCode();
			int keyMod = e.getModifiers();

			int index = fileList.getSelectedIndex();

			MouseEvent rightClick = new MouseEvent(fileList,
					MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0,
					fileList.getSelectedLocation().x + fileList.getWidth() / 2
							+ 1, fileList.getSelectedLocation().y
							+ fileList.getSelectionHeight() / 2, 1, true);

			switch (keyCode) {

			case KeyEvent.VK_SPACE:
				fileListService.openNextFile();
				break;
			case KeyEvent.VK_RIGHT:
				fileListService.openNextFile();
				break;
			case KeyEvent.VK_ENTER:
				setSelectedIndex(index);
				fileListService.openEntryByIndex(index, true);
				break;
			case KeyEvent.VK_LEFT:
				fileListService.openPreviousFile();
				break;
			case KeyEvent.VK_BACK_SPACE:
				fileListService.openPreviousFile();
				break;
			case KeyEvent.VK_CONTEXT_MENU:
				popupListener.mouseReleased(rightClick);
				break;
			case KeyEvent.VK_META:
				popupListener.mouseReleased(rightClick);
				break;
			case KeyEvent.VK_DELETE:
				fileListService.deleteItem(index);
				break;
			}

			// ALT + A
			if (keyCode == popupMenu.getAddMenuItem().getAccelerator()
					.getKeyCode()
					&& keyMod == 8) {
				openAddDialog();
			}

			// ALT + E
			if (keyCode == popupMenu.getEditMenuItem().getAccelerator()
					.getKeyCode()
					&& keyMod == 8) {
				openEditDialog();
			}

			// ALT + M
			if (keyCode == popupMenu.getSendMidiMenuItem().getAccelerator()
					.getKeyCode()
					&& keyMod == 8) {
				midiNotificationService.sendItemSignature(index);
			}

			// arrow up
			if ((keyCode == popupMenu.getMoveUpMenuItem().getAccelerator()
					.getKeyCode())) {
				if (keyMod == 0) {
					setSelectedIndex(index - 1);
				}
				// ALT
				if (keyMod == 8) {
					fileListService.moveUpItem(index);
				}
			}

			// arrow down
			if (keyCode == popupMenu.getMoveDownMenuItem().getAccelerator()
					.getKeyCode()) {
				if (keyMod == 0) {
					if (fileList.isSelectionEmpty()) {
						setSelectedIndex(0);
					} else {
						setSelectedIndex(index + 1);
					}
				}
				// ALT
				if (keyMod == 8) {
					fileListService.moveDownItem(index);
				}
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}

	/**
	 * This handler sets the current file list item whenever the selection of
	 * the file list changes.
	 * 
	 * @author aguelle
	 *
	 */
	class CurrentSelectionHandler implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			fileListService.setCurrentItem(fileList.getSelectedIndex());
		}
	}
}
