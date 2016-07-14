package executable;

import org.gicentre.utils.move.*;
import processing.core.*;

public class Executable extends PApplet {
	Logica app;
	boolean clockwise = true;
	private ZoomPan zoomer;
	PVector mousePos;

	public void setup() {
		size(1200, 600);
		// size(1500, 600);
		app = new Logica(this);
		textSize(10);
		textAlign(CENTER);
		// noLoop();
		ellipseMode(CENTER);

		// Zoom & Pan
//		mousePos = new PVector();
		zoomer = new ZoomPan(this);
//		// zoomer.setMouseMask(app.SHIFT); // Only active while shift key held
//		// down.
//		zoomer.allowZoomButton(true);
//		zoomer.allowPanButton(true);
	}

	public void draw() {
		background(0);

		// Zoom Pan
		// app.pushMatrix();
		zoomer.transform();
		// mousePos = zoomer.getMouseCoord();
		// app.popMatrix();
		// mousePos = zoomer.getCoordToDisp(mousePos);
		//

		app.show(this);
	}

	public void keyPressed() {
		if (key == 'r') {
			clockwise = !clockwise;
		}
	}

}
