package com.main.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.main.facialRecognition.EyeRect;
import com.main.facialRecognition.FacialRecognition;

public class EyeWindow extends Canvas implements Runnable {

	private Thread thread;
	private boolean running = false;
	private JFrame window;
	
	private BufferedImage eyeReading = null;
	private FacialRecognition fr;
	
	public EyeWindow (FacialRecognition fr) {
		this.fr = fr;
		
		window = new JFrame("Current Eye Read");
		window.add(this);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(250, 500);
		window.setLocation(0, 500);
		window.setResizable(false);
		window.setVisible(true);
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				fr.getEyeReader().setBaseColor(new Color(eyeReading.getRGB(e.getX(), e.getY())));
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					fr.getEyeReader().incTolerance();
					break;
				case KeyEvent.VK_B:
					fr.getEyeReader().decTolerance();
					break;
				case KeyEvent.VK_C:
					fr.getEyeReader().incThreshold();
					break;
				case KeyEvent.VK_D:
					fr.getEyeReader().decThreshold();
					break;
				case KeyEvent.VK_E:
					fr.getEyeReader().incRed();
					break;
				case KeyEvent.VK_F:
					fr.getEyeReader().decRed();
					break;
				case KeyEvent.VK_G:
					fr.getEyeReader().incGreen();
					break;
				case KeyEvent.VK_H:
					fr.getEyeReader().decGreen();
					break;
				case KeyEvent.VK_I:
					fr.getEyeReader().incBlue();
					break;
				case KeyEvent.VK_J:
					fr.getEyeReader().decBlue();
					break;
				case KeyEvent.VK_K:
					System.out.println("Image:" + fr.getEyeReader().getValue(eyeReading));
					break;
				case KeyEvent.VK_L:
					fr.getEyeReader().save();
					break;
				case KeyEvent.VK_M:
					fr.getEyeReader().load("res/erData.dat");
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		
		start();
	}
	
	@Override
	public void run () {
		long tps = 5;
		long timePerTick = 1000000000 / tps;
		long startTick = System.nanoTime();
		while (running) {
			if (System.nanoTime() - startTick > timePerTick) {
				tick();
				render();
				startTick += timePerTick;
			}
		}
		halt();
	}
	
	private void tick () {
		EyeRect er = fr.getEyeRect();
		if (fr == null || er == null) return;
		eyeReading = fr.getFaceFrame().getSubimage(er.getX(), er.getY(), er.getWidth(), er.getHeight());
	}
	private void render () {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 250, 500);
		if (eyeReading != null) {
			g.drawImage(eyeReading, 0, 0, null);
			
			for (int y = 0; y < eyeReading.getHeight(); y++) {
				for (int x = 0; x < eyeReading.getWidth(); x++) {
					g.setColor(fr.getEyeReader().isSpecialPixel(eyeReading.getRGB(x, y)) ? Color.RED : Color.BLACK);
					g.fillRect(x + 25 + eyeReading.getWidth(), y, 1, 1);
				}
			}
		}
		
		g.drawString("Value: " + (eyeReading != null ? fr.getEyeReader().getValue(eyeReading) : "null"), 0, 85);
		g.drawString("Color: " + fr.getEyeReader().getBaseColor(), 0, 100);
		g.drawString("Threshold: " + fr.getEyeReader().getThreshold(), 0, 115);
		g.drawString("Tolerance: " + fr.getEyeReader().getTolerance(), 0, 130);
		
		g.drawString("[A] - inc tolerance", 0, 150);
		g.drawString("[B] - dec tolerance", 0, 165);
		g.drawString("[C] - inc theshold", 0, 180);
		g.drawString("[D] - dec theshold", 0, 195);
		g.drawString("[E] - inc red", 0, 210);
		g.drawString("[F] - dec red", 0, 225);
		g.drawString("[G] - inc green", 0, 240);
		g.drawString("[H] - dec green", 0, 255);
		g.drawString("[I] - inc blue", 0, 270);
		g.drawString("[J] - dec blue", 0, 285);
		g.drawString("[L] - save", 0, 300);
		g.drawString("[M] - load", 0, 315);
		
		g.dispose();
		bs.show();
	}
	
	private synchronized void start () {
		if (running) return;
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	public synchronized void halt () {
		if (!running) return;
		running = false;
	}
	
}
