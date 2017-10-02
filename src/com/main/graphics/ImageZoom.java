package com.main.graphics;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageZoom extends ImageView{

	private DoubleProperty zoomProperty = new SimpleDoubleProperty(300);
	
	//empty Constructor
	public ImageZoom() {}
	
	//Constructor: given the image path name 
	public ImageZoom(String imagePath) {
		this.setImage(new Image(imagePath));  
		this.setPreserveRatio(true);
	}
	
	public void zoom(int flag) {
		//zoom in
		if(flag == 1) {
			zoomProperty.set(zoomProperty.get() * 1.1);
			
			this.setFitWidth(zoomProperty.get()*3);
			this.setFitHeight(zoomProperty.get() *4);
		}
		
		//if 0 do nothing
		if(flag == 0) {
			return;
		}
	}
	
}
