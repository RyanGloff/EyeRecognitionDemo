package com.main.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.main.facialRecognition.*;

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
		window.setSize(100, 100);
		window.setLocation(0, 500);
		window.setResizable(false);
		window.setVisible(true);
		
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
		g.fillRect(0, 0, 300, 300);
		g.drawImage(eyeReading, 0, 0, null);
		
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
