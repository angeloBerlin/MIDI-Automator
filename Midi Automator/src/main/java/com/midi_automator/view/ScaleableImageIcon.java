package com.midi_automator.view;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.midi_automator.utils.SystemUtils;

/**
 * An ImageIcon that can be scaled to a defined size in its original aspect
 * ratio.
 * 
 * @author aguelle
 * 
 */
public class ScaleableImageIcon extends ImageIcon {

	private static final long serialVersionUID = 1L;

	private String path;

	/**
	 * Standard constructor
	 */
	public ScaleableImageIcon() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param path
	 *            The path of the image
	 * @param maxWidth
	 *            The maximal width of the image
	 * @param maxHeight
	 *            The maximal height of the image
	 */
	public ScaleableImageIcon(String path, int maxWidth, int maxHeight) {
		super(SystemUtils.replaceSystemVariables(path));
		this.path = path;
		scale(maxWidth, maxHeight);
	}

	/**
	 * Scales the icon to the defined size.
	 * 
	 * @param maxWidth
	 *            The maximal width of the image
	 * @param maxHeight
	 *            The maximal height of the image
	 */
	public void scale(int maxWidth, int maxHeight) {

		int width = getImage().getWidth(null);
		int height = getImage().getHeight(null);
		double aspect = (double) width / (double) height;
		double desiredAspect = (double) maxWidth / (double) maxHeight;

		if (aspect > desiredAspect) {
			if (width > maxWidth) {
				setImage(getImage().getScaledInstance(maxWidth, -1,
						Image.SCALE_DEFAULT));
			}
		} else {
			if (height > maxHeight) {
				setImage(getImage().getScaledInstance(-1, maxHeight,
						Image.SCALE_DEFAULT));
			}
		}
	}

	/**
	 * Gets the image path in the file system
	 * 
	 * @return The path to the image
	 */
	public String getPath() {
		return path;
	}
}
