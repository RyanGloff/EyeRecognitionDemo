package com.main.eyeRecognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.Test;

public class EyeReaderTesting {

	@Test
	public void eyeReaderTesting () {
		// Creating the EyeReader, trainSet and testSet
		EyeReader er = new EyeReader();
		File testDir = new File("res/testEyes/");
		System.out.println("NumFiles: " + testDir.listFiles().length);
		ArrayList<BufferedImage> openTestImgs = new ArrayList<BufferedImage>();
		ArrayList<BufferedImage> closedTestImgs = new ArrayList<BufferedImage>();
		for (File file : testDir.listFiles()) {
			BufferedImage testImg = null;
			try {
				testImg = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (testImg == null) throw new IllegalStateException("The image was not found");
			switch (file.getName().charAt(0)) {
			case 'O':
				er.addOpen(testImg);
				break;
			case 'C':
				er.addClosed(testImg);
				break;
			case 'T':
				if (file.getName().charAt(1) == 'O') {
					openTestImgs.add(testImg);
				} else {
					closedTestImgs.add(testImg);
				}
			}
		}
		er.calc();
		System.out.println("EyeReader Threshold: " + er.getThreshold());
		System.out.println("Open average: " + er.getAvgOpenWhiteness());
		System.out.println("Closed average: " + er.getAvgClosedWhiteness());
		
		// Testing the test set
		int succ = 0;
		int fail = 0;
		for (BufferedImage img : openTestImgs) {
			if (er.isOpen(img)) {
				System.out.println("Success");
				succ++;
			} else {
				System.out.println("Fail");
				fail++;
			}
		}
		for (BufferedImage img : closedTestImgs) {
			if (er.isOpen(img)) {
				System.out.println("Fail");
				fail++;
			} else {
				System.out.println("Success");
				succ++;
			}
		}
		System.out.println("Succeeded: " + succ);
		System.out.println("Failed: " + fail);
		System.out.println("Success Rate: " + (fail != 0 ? ((double)succ / (fail + succ)) : 1d));
		
	}
	
}
