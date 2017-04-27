package com.midi_automator.view.windows.MainFrame;

import java.awt.Font;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.view.windows.MainFrame.listener.CurrentSelectionHandler;
import com.midi_automator.view.windows.MainFrame.listener.ItemListDragGestureListener;
import com.midi_automator.view.windows.MainFrame.listener.ItemListTransferHandler;
import com.midi_automator.view.windows.MainFrame.listener.OpenFileOnDoubleClickListener;

/**
 * A list with items.
 * 
 * @author aguelle
 *
 */
@Component
public class ItemList extends CacheableBlinkableToolTipJList<IToolTipItem> {

	public static final String NAME = "item list";

	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(ItemList.class.getName());

	private IToolTipItem[] items = new IToolTipItem[0];
	private IBlinkingStrategy firstClickStrategy;
	private IBlinkingStrategy otherClickStrategy;
	private int lastSelectedIndex;

	private final int FONT_SIZE = 26;
	private final String FONT_FAMILY = "Arial";

	@Autowired
	private OpenFileOnDoubleClickListener doubleClickListener;
	@Autowired
	private ItemListTransferHandler transferHandler;
	@Autowired
	private CurrentSelectionHandler selectionHandler;
	@Autowired
	private ItemListDragGestureListener itemListDragGestureListener;

	/**
	 * Initializer
	 */
	public void init() {
		super.init();

		firstClickStrategy = new BlinkingStrategy(this,
				MainFrame.METRONOM_COLOR_FIRST_CLICK,
				MainFrame.METRONOM_BLINK_RATE, 1);
		otherClickStrategy = new BlinkingStrategy(this,
				MainFrame.METRONOM_COLOR_OTHER_CLICK,
				MainFrame.METRONOM_BLINK_RATE, 1);

		setName(NAME);
		addMouseListener(doubleClickListener);
		setBlinkingStrategy(otherClickStrategy);
		setFont(new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE));
		setCache(getSelectionBackground());
		setDropMode(DropMode.INSERT);
		setTransferHandler(transferHandler);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addListSelectionListener(selectionHandler);

		DragSource fileListDragSource = new DragSource();
		fileListDragSource.createDefaultDragGestureRecognizer(this,
				DnDConstants.ACTION_MOVE, itemListDragGestureListener);
	}

	/**
	 * Checks if the selected item is the first item in the list.
	 * 
	 * @return <TRUE> if it is the first item, <FALSE> if it is not the first
	 *         item
	 */
	public boolean isFirstItem() {
		boolean result = false;
		if (getSelectedIndex() == 0) {
			result = true;
		}
		return result;
	}

	/**
	 * Checks if the selected item is the last item in the list.
	 * 
	 * @return <TRUE> if it is the last item, <FALSE> if it is not the last item
	 */
	public boolean isLastItem() {
		boolean result = false;
		if (getSelectedIndex() == (items.length - 1)) {
			result = true;
		}
		return result;
	}

	/**
	 * Sets the file entries
	 * 
	 * @param items
	 *            The entries for the files
	 */
	public void setItems(List<String> items) {
		this.items = createViewableFileList(items);
		this.setListData(this.items);
	}

	/**
	 * Flashes the background color of the content pane.
	 * 
	 * @beat the current clicked beat
	 */
	public void blinkMetronom(int beat) {

		if (beat == 1) {
			log.debug("Metronom first click");
			setBlinkingStrategy(firstClickStrategy);
			startBlinking();
		} else {
			log.debug("Metronom other click");
			setBlinkingStrategy(otherClickStrategy);
			startBlinking();
		}
	}

	public IToolTipItem[] getItems() {
		return items;
	}

	/**
	 * Sets the selected item in the file list
	 * 
	 * @param index
	 *            The index
	 */
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		ensureIndexIsOnTop(index);
	}

	public void setLastSelectedIndex(int index) {
		lastSelectedIndex = index;
	}

	public int getLastSelectedIndex() {
		return lastSelectedIndex;
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
}
