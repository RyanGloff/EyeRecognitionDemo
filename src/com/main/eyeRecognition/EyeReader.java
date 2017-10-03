package com.main.eyeRecognition;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EyeReader {

	private int tolerance = 30;
	private double threshold = 0;
	
	private int openAvg;
	private int closedAvg;
	
	private ArrayList<BufferedImage> openImgs;
	private ArrayList<BufferedImage> closedImgs;
	
	/**
	 * Basic constructor. Just initializes the data containers needed for the
	 * training set.
	 */
	public EyeReader () {
		openImgs = new ArrayList<BufferedImage>();
		closedImgs = new ArrayList<BufferedImage>();
	}
	
	/**
	 * Add an image of an open eye to the training set
	 * @param img
	 */
	public void addOpen (BufferedImage img) {
		openImgs.add(img);
	}
	/**
	 * Add an image of a closed eye to the training set
	 * @param img
	 */
	public void addClosed (BufferedImage img) {
		closedImgs.add(img);
	}
	
	/**
	 * Updates the current threshold between open and closed eyes
	 */
	public void calc () {
		//if (openImgs.size() < 5) throw new IllegalStateException("Not enough training data. Add more openEye images.");
		//if (closedImgs.size() < 5) throw new IllegalStateException("Not enough training data. Add more closedEye images.");
		
		openAvg = 0;
		for (BufferedImage img : openImgs) {
			openAvg += getWhiteness(img);
		}
		openAvg /= openImgs.size();
		
		closedAvg = 0;
		for (BufferedImage img : closedImgs) {
			closedAvg += getWhiteness(img);
		}
		closedAvg /= closedImgs.size();
		threshold = (openAvg + closedAvg) / 2;
	}
	
	/**
	 * Determines whether an image is of an open eye or a closed eye
	 * @param img
	 * @return true if the eye is open false if it is not
	 */
	public boolean isOpen (BufferedImage img) {
		if (getWhiteness(img) > threshold) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the whiteness threshold for determining whether eye is open or closed
	 * @return The threshold for open/closed
	 */
	public double getThreshold () {
		return threshold;
	}
	
	/**
	 * Gets the average whiteness score for open states
	 * @return Average whiteness of the open state
	 */
	public double getAvgOpenWhiteness () {
		return openAvg;
	}
	/**
	 * Gets the average whiteness score for the closed states
	 * @return Average whiteness of the closed state
	 */
	public double getAvgClosedWhiteness () {
		return closedAvg;
	}
	
	/**
	 * Gets the amount of white/gray pixels are in the image. With the main concept that pictures of 
	 * open eyes would have more white/gray in them than a picture that doesn't contain the eyeball 
	 * AKA the eye lid would be covering it.
	 * @param img
	 * @return The amount of pixels that are seen as white/gray due to their RGB values being close
	 * enough to each other
	 */
	private int getWhiteness (BufferedImage img) {
		int sum = 0;
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				Color c = new Color(img.getRGB(x, y));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				if (Math.abs(r - b) + Math.abs(r - g) + Math.abs(b - g) < tolerance) {
					sum++;
				}
			}
		}
		return sum;
	}
	
}
