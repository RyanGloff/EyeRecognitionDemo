package com.main.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.main.facialRecognition.FacialRecognition;

public class Controller {

	private BufferedImage testImg = null;
	
	private double zoom = 0.1;      //scaling factor
	private double zoomPercent= .00005; //how much to zoom in each time
	
	private FacialRecognition faceRec;
	private EyeWindow ew;
	
	public Controller () {
		// Loading the image from the file system
		try {
			testImg = ImageIO.read(new File("res/TestImage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Facial Recognition
		faceRec = new FacialRecognition();
		faceRec.start();
		
		ew = new EyeWindow(faceRec);
		
	}
	
	/**
	 *  Update runs 60 times per second to update the current state of the program
	 */
	public void update () {
		if (faceRec.eyesOpen()) {
			zoom -= zoomPercent;
		} else {
			zoom += zoomPercent;
		}
	}
	/**
	 * Draws to the screen
	 * @param g - The Graphics object for the current window
	 */
	public void render (Graphics g) {
		// Drawing an image to the screen
		// convert to Graphics2D to be able to use scale
		Graphics2D g2D = (Graphics2D) g;
		//g2D.translate(testImg.getWidth() / 2, testImg.getHeight() / 2);
		g2D.scale(zoom,zoom);
		g.drawImage(testImg, 0, 0, null);
		g2D.scale(1 / zoom, 1 / zoom);
		//g2D.translate(-1 * testImg.getWidth() / 2, -1 * testImg.getHeight() / 2);
		// Drawing the training states
		g.setColor(Color.RED);
		g.drawString((faceRec.getTraining() ? "Training" : "Not Training") + "   [1]", 100, 100);
		g.drawString((faceRec.getOpen() ? "Open" : "Closed") + "   [2]", 100, 125);
	}
	
	/**
	 * Runs any time a key is pressed on the keyboard
	 * @param e - Holds all of the information about the key. Ex) Specific button
	 */
	public void keyPressed (KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			zoom += zoomPercent;
			break;
		case KeyEvent.VK_DOWN:
			zoom -= zoomPercent;
			break;
		case KeyEvent.VK_1:
			faceRec.toggleTraining();
			break;
		case KeyEvent.VK_2:
			faceRec.toggleOpen();
			break;
		default:
			System.out.println("Warning: Key not mapped to any action.");
			break;
		}
	}
	/**
	 * Runs any time a key is released on the keyboard
	 * @param e - Holds all of the information about the key. Ex) Specific button
	 */
	public void keyReleased (KeyEvent e) {}
	/**
	 * Runs any the a key is typed on the keyboard
	 * @param e - Holds all of the information about the key. Ex) Specific button
	 */
	public void keyTyped (KeyEvent e) {}
	
	public void clean () {
		faceRec.halt();
		ew.halt();
	}
	
}
