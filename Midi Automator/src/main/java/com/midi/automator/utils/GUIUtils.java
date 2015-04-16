package com.midi.automator.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.border.Border;

import com.midi.automator.view.DeActivateableMouseAdapter;
import com.midi.automator.view.ICacheable;

/**
 * Several utilities for working with the applications GUI.
 * 
 * @author aguelle
 * 
 */
public class GUIUtils {

	/**
	 * Gets all components of a Container
	 * 
	 * @param c
	 *            The parent Container
	 * @return a List with all components
	 */
	public static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}

	/**
	 * Gets a Component of a Container by name
	 * 
	 * @param c
	 *            The parent Container
	 * @param name
	 *            The name
	 * @return a Component, <NULL> if no component was found
	 */
	public static Component getComponentByName(final Container c,
			final String name) {
		List<Component> comps = getAllComponents(c);
		for (Component component : comps) {
			String compName = component.getName();
			if (compName != null) {
				if (compName.equals(name)) {
					return component;
				}
			}
		}
		return null;
	}

	/**
	 * Removes all MouseListeners from a component.
	 * 
	 * @param component
	 */
	public static void removeMouseListeners(Component component) {

		MouseListener[] mouseListeners = component.getMouseListeners();
		for (int i = 0; i < mouseListeners.length; i++) {
			component.removeMouseListener(mouseListeners[i]);
		}
	}

	/**
	 * Deactivates all MouseListeners from a component.
	 * 
	 * @param component
	 *            The Component
	 * @param enable
	 *            <TRUE> for enable, <FALSE> for disable
	 */
	public static void deActivateAllMouseListeners(final Component component,
			final boolean enable) {

		MouseListener[] mouseListeners = component.getMouseListeners();
		for (int i = 0; i < mouseListeners.length; i++) {
			if (mouseListeners[i] instanceof DeActivateableMouseAdapter) {
				DeActivateableMouseAdapter mouseAdapter = (DeActivateableMouseAdapter) mouseListeners[i];
				mouseAdapter.setActive(enable);
			}

		}
	}

	/**
	 * Disables or enables all JButtons and JLists within a parent Component and
	 * (de)activates all MouseListeners.
	 * 
	 * @param rootComponent
	 *            The the root component
	 * @param enable
	 *            <TRUE> for enable, <FALSE> for disable
	 */
	public static void disEnableAllInputs(final Component rootComponent,
			final boolean enable) {

		List<Component> compList = GUIUtils
				.getAllComponents((Container) rootComponent);

		for (Component component : compList) {

			if (component instanceof JComponent) {
				component.setEnabled(enable);
				deActivateAllMouseListeners(component, enable);
			}
		}
	}

	/**
	 * Highlights the selected component.
	 * 
	 * @param component
	 *            The component to highlight
	 * @param highlight
	 *            <TRUE> highlight the component, <FALSE> de-highlight the
	 *            component
	 */
	public static void deHighlightComponent(JComponent component,
			boolean highlight) {

		int borderWidth = 8;
		Border highlightedBorder = BorderFactory.createMatteBorder(borderWidth,
				borderWidth, borderWidth, borderWidth, Color.RED);
		Border defaultBorder = null;

		// try to get possible cached border
		defaultBorder = getCachedBorder(component);

		if (highlight) {
			component.setBorder(highlightedBorder);
		} else {
			component.setBorder(defaultBorder);
		}

	}

	/**
	 * Highlights the selected list item.
	 * 
	 * @param list
	 *            The JList to highlight
	 * @param highlight
	 *            <TRUE> highlight the item, <FALSE> de-highlight the item
	 */
	public static void deHighlightListItem(JList<?> list, boolean highlight) {

		Color highlightedColor = Color.RED;
		Color defaultColor = null;

		// try to get possible cached color
		defaultColor = getCachedColor(list);

		if (highlight) {
			list.setSelectionBackground(highlightedColor);
		} else {
			list.setSelectionBackground(defaultColor);
		}
	}

	/**
	 * Highlights the selected table row.
	 * 
	 * @param table
	 *            The JTable to highlight
	 * @param highlight
	 *            <TRUE> highlight the item, <FALSE> de-highlight the item
	 */
	public static void deHighlightTableRow(JTable table, boolean highlight) {

		Color highlightedColor = Color.RED;
		Color defaultColor = null;

		// try to get possible cached color
		defaultColor = getCachedColor(table);

		if (highlight) {
			table.setSelectionBackground(highlightedColor);
		} else {
			table.setSelectionBackground(defaultColor);
		}
	}

	/**
	 * Tries to obtain a possible cached Color of a component that implements
	 * ICachceable
	 * 
	 * @param component
	 *            The component
	 * @return The cached Color
	 */
	private static Color getCachedColor(JComponent component) {

		Color color = null;

		if (component instanceof ICacheable) {
			Object cache = ((ICacheable) component).getCache();
			if (cache instanceof Color) {
				color = (Color) cache;
			}
		}

		return color;
	}

	/**
	 * Tries to obtain a possible cached Border of a component that implements
	 * ICachceable
	 * 
	 * @param component
	 *            The component
	 * @return The cached Border
	 */
	private static Border getCachedBorder(JComponent component) {

		Border border = null;

		if (component instanceof ICacheable) {
			Object cache = ((ICacheable) component).getCache();
			if (cache instanceof Border) {
				border = (Border) cache;
			}
		}

		return border;
	}
}
