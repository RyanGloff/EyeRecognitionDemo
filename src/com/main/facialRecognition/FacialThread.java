package com.main.facialRecognition;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FacialThread implements Runnable{

		private Thread thread;
		private boolean running = false;
		
		public static void main(String[] args) {
			FacialThread facialThread = new FacialThread();
			facialThread.start();
			facialThread.halt();
		}
		
		public FacialThread() {
			
		}
		
		@Override
		public void run() {
			FacialRecognition faceDetector = new FacialRecognition();
			JFrame window = new JFrame("Facial Recognition");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setSize(500, 500);
			faceDetector.runFacialRecognition();
			JLabel label = new JLabel(new ImageIcon(faceDetector.getFaceFrame()));
			window.getContentPane().setLayout(new FlowLayout());
			window.getContentPane().add(label);
			window.pack();
			window.setVisible(true);
			
			while(running) {			
				faceDetector.runFacialRecognition();
				label.setIcon(new ImageIcon(faceDetector.getFaceFrame()));
			}
		}
		
		public synchronized void start() {
			if (running) {
				return;
			}
			thread = new Thread(this);
			thread.start();
			running = true;
		}
		
		public void halt() {
			running = false;
		}
}
