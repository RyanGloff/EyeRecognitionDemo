package com.main.facialRecognition;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import com.main.eyeRecognition.EyeReader;

public class FacialRecognition implements Runnable
{
	private CascadeClassifier faceDetector;
	private CascadeClassifier eyeDetector;
	private VideoCapture capture;
	private BufferedImage faceFrame;
	private EyeRect eye;
	
	private EyeReader eyeReader;
	private boolean eyesOpen = true;
	
	private Thread thread;
	private boolean running;
	
	@Override
	public void run() {
		eyeReader = new EyeReader();
		eyeReader.train("res/EyeImages/");
		JFrame window = new JFrame("Facial Recognition");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(500, 500);
		window.getContentPane().setLayout(new FlowLayout());
		JLabel label = new JLabel();
		window.getContentPane().add(label);
		window.setVisible(true);
		long startTime = System.nanoTime();
		while(running) {
			runFacialRecognition();
			label.setIcon(new ImageIcon(faceFrame));
			
			long currentTime = System.nanoTime();
			double dTime = (double)(currentTime - startTime) / 1000000000;
			//System.out.println("Elapsed Time: " + dTime + " seconds");
			startTime = currentTime;
		}
	}
	
	public synchronized void start() {
		if (running) return;
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public void halt() {
		running = false;
	}
	
	//loads opencv lib, cascadeclassifiers for eyes and face, and opens videostream to webcam
	public FacialRecognition() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		faceDetector = new CascadeClassifier("lbpcascade_frontalface.xml");
		eyeDetector = new CascadeClassifier("haarcascade_eye.xml");
		faceFrame = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		capture = new VideoCapture(0);
	}
	
	public void runFacialRecognition() {
		Mat frame = new Mat();
		capture.open(0);
		
		if(capture.isOpened()) {
			capture.read(frame);
			if(!frame.empty()) {
				matToImage(frame);
				if(detectFaces(frame)) {
					detectEyes(frame);
					if(eye != null) {
						//System.out.println(eye.toString());
					}
				} else {
					eye = null;
				}
			}	
		} else {
			System.err.println("Error: No video device could be opened");
			System.exit(1);
		}
	}
	//converts Mat to array of bytes to a buffered image for easier display
	public void matToImage(Mat original) {
		int width = original.width();
		int height = original.height();
		int channels = original.channels();
		byte[] sourcePixels = new byte[width*height*channels];
		original.get(0, 0, sourcePixels);
		faceFrame = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		final byte[] targetPixels = ((DataBufferByte) faceFrame.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
	}
	//uses opencv to analyze frame and compare it to the cascade classifier
	public boolean detectFaces(Mat frame) {
		Mat mRBG = new Mat();
		Mat mGrey = new Mat();
		MatOfRect faces = new MatOfRect();
		frame.copyTo(mRBG);
		frame.copyTo(mGrey);
		Imgproc.cvtColor(mRBG, mGrey, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(mGrey, mGrey);
		faceDetector.detectMultiScale(mGrey, faces);
		
		return faces.toArray().length > 0;
		
		/* Way to complicated
		if(faces.toArray().length > 0) {
			System.out.println("Face detected");
			return true;
		}
		else {
			System.out.println("Failed to detect face");
			return false;
		}*/
	}
	//uses opencv to detect eyes from cascade classifier
	public void detectEyes(Mat frame) {
		Mat mRBG = new Mat();
		Mat mGrey = new Mat();
		MatOfRect eyes = new MatOfRect();
		frame.copyTo(mRBG);
		frame.copyTo(mGrey);
		Imgproc.cvtColor(mRBG, mGrey, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(mGrey, mGrey);
		eyeDetector.detectMultiScale(mGrey, eyes);
		Rect[] eyeArray = eyes.toArray();
		
		if(eyeArray.length > 0) {
			eye = new EyeRect(eyeArray[0]);
			// Export to file system for now
			BufferedImage eyeImg = faceFrame.getSubimage(eye.getX(), eye.getY(), eye.getWidth(), eye.getHeight());
			System.out.println("Img size: " + eyeImg.getWidth() + " by " + eyeImg.getHeight());
			System.out.println("Eyes Open: " + eyeReader.isOpen(eyeImg) + "\tValue: " + eyeReader.getWhiteness(eyeImg));
			eyesOpen = eyeReader.isOpen(eyeImg);
			/*
			int i = 0;
			File eyeSaveLocation = new File("res/EyeImages/img" + i + ".png");
			while (eyeSaveLocation.exists()) {
				i++;
				eyeSaveLocation = new File("res/EyeImages/img" + i + ".png");
			}
			try {
				ImageIO.write(eyeImg, "png", eyeSaveLocation);
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
	}
	//getter function for the faceFrame
	public BufferedImage getFaceFrame() {
		return faceFrame;
	}
	//getter function for the eyeRect/location
	public EyeRect getEyeRect() {
		return eye;
	}
	public boolean eyesOpen () {
		return eyesOpen;
	}
}