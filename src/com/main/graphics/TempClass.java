//test to show zoom using the ImageZoom class.

package com.main.graphics;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TempClass extends Application{
	
	public void start(Stage primaryStage) {
		//create ImageZoom which extends ImageView
		//must change file path name to correct location 
		ImageZoom zoomView = new ImageZoom("res/testImage.png");
		StackPane pane = new StackPane();
		pane.getChildren().add(zoomView);
		pane.setAlignment(Pos.CENTER);
		Scene scene = new Scene(pane, 1500, 1000);
		
		//press spacebar to zoom in by a little, this will be changed to getting a value from eye reader
		scene.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.SPACE) {
				zoomView.zoom(1);
			}
		});
		
		//display window
		primaryStage.setTitle("Zoom example");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
	       launch(args);
	    }
	
	}

