package executable;

import gui.Zoom;
import processing.core.*;
import visualElements.Dot;
import processing.opengl.*;

public class Executable extends PApplet {
	Logica app;
	Zoom canvasTransformer;

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
	}

	public void draw() {
		background(0);
		// Transformation of the canvas according to the zoom parameters
		pushMatrix();
		canvasTransformer.active();
		app.show(this);
		popMatrix();
		// Display GUI info
		canvasTransformer.showLegend(new PVector(width - 20, 20));
		canvasTransformer.displayValues(new PVector(width - 20, 45));

	}
}