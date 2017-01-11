package visualElements;

import processing.core.PApplet;
import processing.core.PVector;

public class Prueba extends PApplet {
	BezierBeta a, b;
	PVector o, d;

	public void settings() {
		size(600, 600);
	}

	public void setup() {
		o = new PVector(width / 2, height / 2);
		d = new PVector(width/2, 470);

		//a = new BezierBeta(o, d, 100);
		//b = new BezierBeta(d, o, 100);
	}

	public void draw() {
		background(250);
		// a.drawBezier(this, 1);
		b = new BezierBeta(o, new PVector(mouseX, mouseY), 100);
		b.drawBezierAndControls(this, 1);
	}

	public static void main(String[] args) {
		String[] appletArgs = new String[] { "visualElements.Prueba" };
		if (args != null) {
			PApplet.main(concat(appletArgs, args));
		} else {
			PApplet.main(appletArgs);
		}
	}
}
