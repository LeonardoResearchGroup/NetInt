package visualElements;

import processing.core.*;

public class Dot {
	PVector pos;

	public Dot(float posX, float posY, float diam) {
		pos = new PVector(posX, posY, diam);
	}

	public void show(PApplet app, PVector mousePos) {
		app.ellipseMode(PConstants.CENTER);
		if (PApplet.dist(mousePos.x, mousePos.y, pos.x, pos.y) < pos.z / 2) {
			app.fill(255, 0, 0);
		} else {
			app.fill(255);
		}
		app.ellipse(pos.x, pos.y, pos.z, pos.z);
		app.fill(250, 90);
		app.text("x: " + pos.x + "y: " + pos.y, pos.x, pos.y);
	}
}
