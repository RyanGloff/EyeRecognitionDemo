package com.main.facialRecognition;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.opencv.core.Rect;
//container class for eye location on screen
public class EyeRect {
	
	private Rect eye;
	
	private Point topLeft;
	private Point topRight;
	private Point botLeft;
	private Point botRight;
	private int width;
	private int height;
	
	public EyeRect(Rect eye) {
		this.eye = eye;
		topLeft = new Point(eye.x, eye.y);
		topRight = new Point((eye.x + eye.width), eye.y);
		botLeft = new Point(eye.x, (eye.y - eye.height));
		botRight = new Point((eye.x + eye.width), (eye.y - eye.height));
		this.width = eye.width;
		this.height = eye.height;
	}
	
	@Override
	public String toString() {
		String str = "";
		str += "topLeft: " + topLeft.toString() + "\n";
		str += "topRight: " + topRight.toString() + "\n";
		str += "botLeft: " + botLeft.toString() + "\n";
		str += "botRight: " + botRight.toString() + "\n";
		return str;
	}
	
	public int getX () {
		return eye.x;
	}
	public int getY () {
		return eye.y;
	}
	
	public Point2D getTopLeft() {
		return topLeft;
	}
	
	public Point2D getTopRight() {
		return topRight;
	}
	
	public Point2D getBotLeft() {
		return botLeft;
	}
	
	public Point2D getBotRight() {
		return botRight;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
