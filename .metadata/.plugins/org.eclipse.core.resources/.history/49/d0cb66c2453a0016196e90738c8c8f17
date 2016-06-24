package executable;

import processing.core.*;
import utilities.geometry.Involute;

public class Executable extends PApplet {

	Logica app;
	//Involute inv;
	boolean clockwise = true;

	public void setup() {
		size(1200,600);
		//size(1500, 600);
		app = new Logica(this);
		textSize(10);
		textAlign(CENTER);
		// noLoop();
		ellipseMode(CENTER);
	//	inv = new Involute(this, 100, 50, 7);// PApplet app, float radius, int
												// sections, int index
	}

	public void draw() {
		background(0);
		 app.show(this);
	//	inv.runInvolute(90, 1, clockwise);
	//	inv.show(new PVector(400,300));
	}

	
	public void keyPressed(){
		if (key=='r'){
			clockwise = !clockwise;
			System.out.println("direction switched");
		}
	}
}
