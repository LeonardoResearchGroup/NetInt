package executable;

import gui.Zoom;
import processing.core.*;
import visualElements.Dot;
import processing.opengl.*;

public class Executable extends PApplet {
	Logica app;
	PVector mousePos;
	Zoom canvasTransformer;
	Dot[] dots;

	public void setup() {
		size(displayWidth - 100, displayHeight - 200, P2D);
		app = new Logica(this);
		textSize(10);
		textAlign(CENTER);
		// noLoop();
		ellipseMode(CENTER);

		// Zoom
		canvasTransformer = new Zoom(this);
		// z.translateCenter(width / 2, height / 2);
		dots = new Dot[10];
		makeElements();
	}

	public void draw() {
		background(0);

		// Transformation of the canvas according to the zoom parameters
		pushMatrix();
		canvasTransformer.active();
		app.show(this);
		drawElements(canvasTransformer.getCanvasMouse());
		popMatrix();
		// Display GUI info
		canvasTransformer.showLegend(new PVector(width - 20, 20));
		canvasTransformer.displayValues(new PVector(width - 20, 45));

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