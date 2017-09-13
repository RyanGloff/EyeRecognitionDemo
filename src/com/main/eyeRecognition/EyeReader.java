package com.main.eyeRecognition;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EyeReader {

	private int tolerance = 20;
	private double threshold = 0;
	
	private ArrayList<BufferedImage> openImgs;
	private ArrayList<BufferedImage> closedImgs;
	
	public EyeReader () {
		openImgs = new ArrayList<BufferedImage>();
		closedImgs = new ArrayList<BufferedImage>();
	}
	
	public void addOpen (BufferedImage img) {
		openImgs.add(img);
	}
	public void addClosed (BufferedImage img) {
		closedImgs.add(img);
	}
	
	public void calc () {
		if (openImgs.size() < 5) throw new IllegalStateException("Not enough training data. Add more openEye images.");
		if (closedImgs.size() < 5) throw new IllegalStateException("Not enough training data. Add more closedEye images.");
		
		int openAvg = 0;
		for (BufferedImage img : openImgs) {
			openAvg += getWhiteness(img);
		}
		openAvg /= openImgs.size();
		
		int closedAvg = 0;
		for (BufferedImage img : closedImgs) {
			closedAvg += getWhiteness(img);
		}
		closedAvg /= closedImgs.size();
		threshold = (openAvg + closedAvg) / 2;
	}
	
	public boolean isOpen (BufferedImage img) {
		if (getWhiteness(img) > threshold) {
			return true;
		} else {
			return false;
		}
	}
	
	private int getWhiteness (BufferedImage img) {
		int sum = 0;
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				Color c = new Color(img.getRGB(x, y));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				if (Math.abs(r - b) + Math.abs(r - g) + Math.abs(b - g) > tolerance) {
					sum++;
				}
			}
		}
		return sum;
	}
	
}
