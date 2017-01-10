package com.midi_automator.guiautomator;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Location;
import org.sikuli.script.Match;
import org.sikuli.script.Observer;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.midi_automator.utils.ImageUtils;

/**
 * This class implements support for colored pattern matching if similarity has
 * to be quite low.
 * 
 * @author aguelle
 * 
 */
public class MinSimColoredScreen extends Screen {

	public MinSimColoredScreen() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param searchRegion
	 *            The search region
	 */
	public MinSimColoredScreen(Region searchRegion) {
		setSearchRegion(searchRegion);
	}

	static Logger log = Logger.getLogger(MinSimColoredScreen.class.getName());

	private final double MAX_COLOR_DIFFERENCE = 0.07;
	private Region searchRegion;

	@Override
	public <PatternOrString> Match wait(PatternOrString target, double timeout)
			throws FindFailed {

		long startingTime = System.currentTimeMillis();
		long timeoutMS = (long) (timeout * 1000);

		boolean found = false;
		Match match = null;
		long usedTime = 0;
		Color colorFoundImage = null;
		Color colorTargetImage = null;
		double colorDifference = -1;

		while (usedTime < timeoutMS && found == false) {

			match = super.wait(target, timeout);
			Rectangle targetRect = match.getRect();
			BufferedImage targetImage = match.getImage().get();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				log.error("Thread Sleep failed.", e1);
			}

			// take screenshot of found region
			BufferedImage foundImage = null;
			try {
				foundImage = new Robot().createScreenCapture(new Rectangle(
						targetRect));
			} catch (AWTException e) {
				log.error("Creating screen capture failed.", e);
			}

			// compare average colors of target image and screenshot of the
			// found region
			colorFoundImage = new Color(ImageUtils.getAverageColor(foundImage));
			colorTargetImage = new Color(
					ImageUtils.getAverageColor(targetImage));

			colorDifference = ImageUtils.getColorDifference(colorFoundImage,
					colorTargetImage);

			if (colorDifference <= MAX_COLOR_DIFFERENCE) {
				found = true;
			}

			usedTime = System.currentTimeMillis() - startingTime;
		}

		if (!found) {
			throw new FindFailed("Color does not match");
		}

		log.debug(target + " found!");
		return match;
	}

	/**
	 * Sets the search region for the screen
	 * 
	 * @param searchRegion
	 *            The search region
	 */
	public void setSearchRegion(Region searchRegion) {
		this.searchRegion = searchRegion;
		setH(searchRegion.h);
		setW(searchRegion.w);
		this.setLocation(new Location(searchRegion.x, searchRegion.y));
	}

	public Region getSearchRegion() {
		return searchRegion;
	}

	@Override
	public Observer getObserver() {
		return super.getObserver();
	}
}
