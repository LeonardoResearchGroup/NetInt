package executable;

import gui.Zoom;
import processing.core.*;
import visualElements.Dot;

public class Executable extends PApplet {
	Logica app;
	PVector mousePos;
	Zoom z;
	Dot[] dots;

	public void setup() {
		size(1200, 600);
		app = new Logica(this);
		textSize(10);
		textAlign(CENTER);
		// noLoop();
		ellipseMode(CENTER);

		// Zoom
		z = new Zoom(this);
		//z.translateCenter(width / 2, height / 2);
		dots = new Dot[10];
		makeElements();
	}

	public void draw() {
		background(0);

		pushMatrix();
		z.active();
		app.show(this);
		drawElements(z.getCanvasMouse());
		popMatrix();
		// Display GUI info
		z.showLegend(new PVector(width-20,20));
		z.displayValues(new PVector(width-20,35));
	}
	
	
	// These methods are provisional while the implementation of the Zoom Class
	
	void makeElements() {
		// An arbitrary design so that we have something to see!
		for (int i = 0; i < 10; i++) {
			stroke(0, 50);
			float h = 100;
			Dot temp = new Dot(random(-h, h), random(-h, h), 12f);
			dots[i] = temp;
		}
	}

	void drawElements(PVector theMouse) {
		// An arbitrary design so that we have something to see!
		randomSeed(1);
		for (int i = 0; i < 10; i++) {
			dots[i].show(this, theMouse);
		}
	}
}