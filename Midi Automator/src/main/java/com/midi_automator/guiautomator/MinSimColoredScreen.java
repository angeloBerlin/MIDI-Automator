package com.midi_automator.guiautomator;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
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

	static Logger log = Logger.getLogger(MinSimColoredScreen.class.getName());

	private final double MAX_COLOR_DIFFERENCE = 0.08;

	@Override
	public <PatternOrString> Match wait(PatternOrString target, double timeout)
			throws FindFailed {

		Match match = super.wait(target, timeout);
		Rectangle targetRect = match.getRect();
		BufferedImage targetImage = match.getImage().get();

		// take screenshot of found region
		BufferedImage foundImage = null;
		try {
			foundImage = new Robot().createScreenCapture(new Rectangle(
					targetRect));
		} catch (AWTException e) {
			log.error("Creating screen capture failed.", e);
		}

		// compare average colors of target image and screenshot of the found
		// region
		Color colorFoundImage = new Color(
				ImageUtils.getAverageColor(foundImage));
		Color colorTargetImage = new Color(
				ImageUtils.getAverageColor(targetImage));

		double colorDifference = ImageUtils.getColorDifference(colorFoundImage,
				colorTargetImage);

		if (colorDifference > MAX_COLOR_DIFFERENCE) {
			throw new FindFailed("Color does not match");
		}

		return match;
	}
}
