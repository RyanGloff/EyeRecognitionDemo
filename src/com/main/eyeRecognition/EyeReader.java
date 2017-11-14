package com.main.eyeRecognition;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class EyeReader {

	private int threshold;
	private int tolerance;
	private Color baseColor;
	
	public EyeReader () {
		load("res/erData.dat");
	}
	public EyeReader (Color baseColor) {
		this.baseColor = baseColor;
	}
	
	public boolean isOpen (BufferedImage img) {
		int value = getValue(img, tolerance);
		return value > threshold;
	}
	
	public int getValue (BufferedImage img, int tolerance) {
		int value = 0;
		for (int y = img.getHeight() / 3; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				if (isSpecialPixel(img.getRGB(x, y))) {
					value++;
				}
			}
		}
		return value;
	}
	public int getValue (BufferedImage img) {
		int value = 0;
		for (int y = img.getHeight() / 3; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				if (isSpecialPixel(img.getRGB(x, y))) {
					value++;
				}
			}
		}
		return value;
	}
	
	public boolean isSpecialPixel (int pixel) {
		Color curColor = new Color(pixel);
		int dr = Math.abs(curColor.getRed() - baseColor.getRed());
		int dg = Math.abs(curColor.getGreen() - baseColor.getGreen());
		int db = Math.abs(curColor.getBlue() - baseColor.getBlue());
		int dTotal = dr + dg + db;
		return dTotal < tolerance;
	}
	
	public void save () {
		File file = new File("res/erData.dat");
		PrintWriter fileOut = null;
		try {
			fileOut = new PrintWriter(file);
			fileOut.println(tolerance);
			fileOut.println(threshold);
			fileOut.println(baseColor.getRed());
			fileOut.println(baseColor.getGreen());
			fileOut.println(baseColor.getBlue());
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void load (String path) {
		File file = new File(path);
		Scanner fileIn = null;
		try {
			fileIn = new Scanner(file);
			tolerance = Integer.parseInt(fileIn.nextLine());
			threshold = Integer.parseInt(fileIn.nextLine());
			int r = Integer.parseInt(fileIn.nextLine());
			int g = Integer.parseInt(fileIn.nextLine());
			int b = Integer.parseInt(fileIn.nextLine());
			baseColor = new Color(r, g, b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getTolerance () {return tolerance;}
	public int getThreshold () {return threshold;}
	public Color getBaseColor () {return baseColor;}
	
	public void incTolerance () {tolerance++;}
	public void decTolerance () {tolerance--;}
	public void incThreshold () {threshold++;}
	public void decThreshold () {threshold--;}
	public void setBaseColor (Color baseColor) {this.baseColor = baseColor;}
	public void incRed () {baseColor = new Color(baseColor.getRed() + 1, baseColor.getGreen(), baseColor.getBlue());}
	public void decRed () {baseColor = new Color(baseColor.getRed() - 1, baseColor.getGreen(), baseColor.getBlue());}
	public void incGreen () {baseColor = new Color(baseColor.getRed(), baseColor.getGreen() + 1, baseColor.getBlue());}
	public void decGreen () {baseColor = new Color(baseColor.getRed(), baseColor.getGreen() - 1, baseColor.getBlue());}
	public void incBlue () {baseColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue() + 1);}
	public void decBlue () {baseColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue() - 1);}
	
}
