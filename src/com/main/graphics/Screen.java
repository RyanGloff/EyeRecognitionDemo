package com.main.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Screen extends Canvas implements Runnable, KeyListener {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	private Thread thread;
	private boolean running = false;
	private JFrame window;
	private int fps = 0;
	private Controller controller;
	
	public static void main (String[] args) {
		Screen screen = new Screen();
	}
	
	public Screen () {
		controller = new Controller();
		
		window = new JFrame("Title");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(this);
		window.setSize(WIDTH, HEIGHT);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setVisible(true);
		window.addKeyListener(this);
		
		start();
	}
	
	@Override
	public void run () {
		final int tps = 60;
		final long timePerTick = 1000000000 / tps;
		int frames = 0;
		long startSecond = System.currentTimeMillis();
		long startTick = System.nanoTime();
		while (running) {
			if (System.nanoTime() - startTick > timePerTick) {
				update();
				render();
				frames++;
				startTick += timePerTick;
			}
			if (System.currentTimeMillis() - startSecond > 1000) {
				fps = frames;
				frames = 0;
				startSecond += 1000;
				//System.out.println("FPS: " + fps);
			}
		}
		controller.clean();
	}
	
	private void update () {
		controller.update();
	}
	
	private void render () {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		controller.render(g);
		g.dispose();
		bs.show();
	}
	
	private synchronized void start () {
		if (running) return;
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	public void halt () {
		running = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		controller.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		controller.keyReleased(e);		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		controller.keyTyped(e);		
	}
	
}
