package com.midi.automator.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * This class holds functions to maniulate images.
 * 
 * @author aguelle
 * 
 */
public class ImageUtils {

	private static final int COMMON_COLOR_MAX_DIMENSION = 100;

	/**
	 * Converts a given Image into a BufferedImage
	 * 
	 * @param img
	 *            The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage imageToBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	/**
	 * Gets the average color by scaling the image to just one pixel.
	 * 
	 * @param image
	 *            The image
	 * @return The average color of the image
	 */
	public static Integer getAverageColor(BufferedImage image) {

		Image scaledImaged = image.getScaledInstance(1, 1,
				Image.SCALE_AREA_AVERAGING);
		BufferedImage scaledBufferedImage = imageToBufferedImage(scaledImaged);
		Integer color = scaledBufferedImage.getRGB(0, 0);

		return color;
	}

	/**
	 * Gets the color with the highest amount from an image.
	 * 
	 * @param image
	 *            The image
	 * @return The color with the highest amount in the format RED GREEN BLUE
	 */
	public static Integer getMostCommonColor(BufferedImage image) {

		BufferedImage scaledImage = scaleBufferedImageToDimension(image,
				COMMON_COLOR_MAX_DIMENSION);

		Map<Integer, Integer> colorStatistics = buildColorStatisticsOfImage(scaledImage);

		// sort map for highest amount
		List<Map.Entry<Integer, Integer>> colorList = new LinkedList<Map.Entry<Integer, Integer>>(
				colorStatistics.entrySet());
		Collections.sort(colorList, new RGBStatisticsComparator());

		// get highest amount entry
		Map.Entry<Integer, Integer> lastPixel = (Map.Entry<Integer, Integer>) colorList
				.get(colorList.size() - 1);

		return lastPixel.getKey();
	}

	/**
	 * Scales a BufferedImage to a fixed squared dimension.
	 * 
	 * @param image
	 *            The image
	 * @param dimension
	 *            The dimension
	 * @return A BufferedImage that fits in to the dimension of the square
	 */
	public static BufferedImage scaleBufferedImageToDimension(
			BufferedImage image, int dimension) {

		Image scaledImaged;

		if (image.getWidth(null) > image.getHeight(null)) {
			scaledImaged = image.getScaledInstance(dimension, -1,
					Image.SCALE_DEFAULT);
		} else {
			scaledImaged = (BufferedImage) image.getScaledInstance(-1,
					dimension, Image.SCALE_DEFAULT);
		}

		return imageToBufferedImage(scaledImaged);
	}

	/**
	 * Builds a statistical map with all found colors as keys within a given
	 * image. The values of the map represent the amount pixels found in the
	 * key-colour.
	 * 
	 * @param image
	 *            The image to operate on
	 * @return A Map with <colorcode, amount>
	 */
	private static Map<Integer, Integer> buildColorStatisticsOfImage(
			BufferedImage image) {

		Map<Integer, Integer> colorStatistics = new HashMap<Integer, Integer>();

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				int rgb = image.getRGB(i, j);

				Integer counter = (Integer) colorStatistics.get(rgb);

				if (counter == null) {
					counter = 0;
				}

				counter++;
				colorStatistics.put(new Integer(rgb), counter);
			}
		}
		return colorStatistics;
	}

	/**
	 * Extracts the red green and blue portions as of a pixel and returns them
	 * as String.
	 * 
	 * @param rgb
	 *            The color of the pixel as bitfield
	 * @return A String of the decimal representations of RED, GREEN and BLUE
	 */
	public static String rgbBitfieldToString(int rgb) {

		return getRed(rgb) + " " + getGreen(rgb) + " " + getBlue(rgb);
	}

	/**
	 * Extracts the red portion from a given RGB color
	 * 
	 * @param rgb
	 *            The color as bitfield
	 * @return the decimal portion of red
	 */
	public static int getRed(int rgb) {
		return (rgb >> 16) & 0xff;
	}

	/**
	 * Extracts the green portion from a given RGB color
	 * 
	 * @param rgb
	 *            The color as bitfield
	 * @return the decimal portion of green
	 */
	public static int getGreen(int rgb) {
		return (rgb >> 8) & 0xff;
	}

	/**
	 * Extracts the blue portion from a given RGB color
	 * 
	 * @param rgb
	 *            The color as bitfield
	 * @return the decimal portion of blue
	 */
	public static int getBlue(int rgb) {
		return (rgb) & 0xff;
	}

	/**
	 * Loads a BufferedImage from a given file path
	 * 
	 * @param path
	 *            The path to the image
	 * @return A BufferedImage
	 * @throws IOException
	 *             If the file could not be read
	 */
	public static BufferedImage getBufferedImageFromPath(String path)
			throws IOException {

		BufferedImage image = null;

		ImageInputStream is = ImageIO.createImageInputStream(new File(path));
		Iterator<ImageReader> iter = ImageIO.getImageReaders(is);
		ImageReader imageReader = (ImageReader) iter.next();
		imageReader.setInput(is);
		image = imageReader.read(0);

		return image;
	}

	/**
	 * Calculates the difference of two colors between 0.0 and 1.0. 1.0 is the
	 * biggest possible difference.
	 * 
	 * @param c1
	 *            First color
	 * @param c2
	 *            Second color
	 * @return The difference between 0.0 and 1.0
	 */
	public static double getColorDifference(Color c1, Color c2) {

		// max distance from (0,0,0) to (255,255,255)
		double maxLength = Math.sqrt(65025 + 65025 + 65025);

		double result = Math.pow((c1.getRed() - c2.getRed()), 2)
				+ Math.pow(c1.getGreen() - c2.getGreen(), 2)
				+ Math.pow(c1.getBlue() - c2.getBlue(), 2);

		result = Math.sqrt(result);

		// normalizing
		result = result / maxLength;
		return result;
	}

	/**
	 * Compares the amount of two color statistic entries.
	 * 
	 * @author aguelle
	 * 
	 */
	static class RGBStatisticsComparator implements
			Comparator<Map.Entry<Integer, Integer>> {

		@Override
		public int compare(Map.Entry<Integer, Integer> entry1,
				Map.Entry<Integer, Integer> entry2) {

			return (entry1.getValue().compareTo(entry2.getValue()));
		}
	}
}