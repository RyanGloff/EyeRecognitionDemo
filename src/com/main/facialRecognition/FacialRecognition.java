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
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

class FacialRecognition 
{
	private CascadeClassifier faceDetector;
	private CascadeClassifier eyeDetector;
	private VideoCapture capture;
	private BufferedImage faceFrame;
	private EyeRect eye;
	
	public static void main(String [] args) {
		
		FacialRecognition faceDetector = new FacialRecognition();
		faceDetector.runFacialRecognition();
		JFrame window = new JFrame("Facial Recognition");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(500, 500);
		JLabel label = new JLabel(new ImageIcon(faceDetector.getFaceFrame()));
		window.getContentPane().setLayout(new FlowLayout());
		window.getContentPane().add(label);
		window.pack();
		window.setVisible(true);
		
		while(true) {
			faceDetector.runFacialRecognition();
			label.setIcon(new ImageIcon(faceDetector.faceFrame));
		}
	}
	//loads opencv lib, cascadeclassifiers for eyes and face, and opens videostream to webcam
	public FacialRecognition() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		faceDetector = new CascadeClassifier("lbpcascade_frontalface.xml");
		eyeDetector = new CascadeClassifier("haarcascade_eye.xml");
		capture = new VideoCapture(0);
	}
	
	public void runFacialRecognition() {	
		Mat frame = new Mat();
		capture.open(0);
		
		if(capture.isOpened()) {
			capture.read(frame);
			if(!frame.empty())
			{
				matToImage(frame);
				if(detectFaces(frame)) {
					detectEyes(frame);
					if(eye != null) {
						System.out.println("eye rect created");
						eye.printPoints();
					}
				}
			}	
		}
	}
	//converts Mat to array of bytes to a bufferedimage for easier display
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
		
		if(faces.toArray().length > 0) {
			System.out.println("faceDetected");
			return true;
		}
		else {
			return false;
		}
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
			System.out.println("Eyes detected");
			eye = new EyeRect(eyeArray[0]);
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
}