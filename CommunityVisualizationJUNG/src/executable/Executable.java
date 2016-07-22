package executable;

import processing.core.*;
import utilities.TestPerformance;
import visualElements.Canvas;

public class Executable extends PApplet {
	Logica app;
	Canvas canvas;
	TestPerformance performance;
	
	public void setup(){
		//size(1200,600,P2D);
		size(displayWidth,displayHeight-100,P2D);
		canvas = new Canvas(this);
		textSize(10);
		//
		app = new Logica();
		//
		performance = new TestPerformance();
		
	}
	
	public void draw (){
		background(0);
		//
		pushMatrix();
		canvas.translateCenter(width/2, height/2);
		canvas.transform();
		app.show(canvas);
		popMatrix();
		//
		canvas.showLegend(new PVector(width-20, 20));
		canvas.displayValues(new PVector(width-20, 40));
		performance.displayValues(canvas, new PVector(width-20, height-60));
	}
}