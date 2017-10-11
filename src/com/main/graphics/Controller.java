package com.main.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Controller {

	private BufferedImage testImg = null;
	
	private double zoom = 1.0;      //scaling factor
	private double zoomPercent= .5; //how much to zoom in each time
	
	public Controller () {
		// Loading the image from the file system
		try {
			testImg = ImageIO.read(new File("res/testImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  Update runs 60 times per second to update the current state of the program
	 */
	public void update () {
		
	}
	/**
	 * Draws to the screen
	 * @param g - The Graphics object for the current window
	 */
	public void render (Graphics g) {
		// Drawing an image to the screen
		//convert to Graphics2D to be able to use scale
		Graphics2D g2D = (Graphics2D) g;
		g2D.scale(zoom,zoom);
		g.drawImage(testImg, 0, 0, null);
	}
	
	/**
	 * Runs any time a key is pressed on the keyboard
	 * @param e - Holds all of the information about the key. Ex) Specific button
	 */
	public void keyPressed (KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			zoom += zoomPercent;
		}
	}
	/**
	 * Runs any time a key is released on the keyboard
	 * @param e - Holds all of the information about the key. Ex) Specific button
	 */
	public void keyReleased (KeyEvent e) {
		
	}
	/**
	 * Runs any the a key is typed on the keyboard
	 * @param e - Holds all of the information about the key. Ex) Specific button
	 */
	public void keyTyped (KeyEvent e) {
		
	}
	
}
