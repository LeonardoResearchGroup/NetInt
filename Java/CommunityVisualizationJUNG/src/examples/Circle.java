package examples;

import netInt.canvas.Canvas;
import netInt.visualElements.primitives.VisualAtom;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class Circle extends VisualAtom {

	private static final long serialVersionUID = 1L;

	public Circle(float x, float y) {
		super(x, y);
		eventRegister(Canvas.app);
	}

	public void show() {
		if (isMouseOver) {
			Canvas.app.fill(170, 25, 30);
		} else {
			Canvas.app.fill(70, 25, 130, 30);
		}
		Canvas.app.noStroke();
		Canvas.app.ellipse(pos.x, pos.y, getDiameter(), getDiameter());
	}

	public void sense() {
		if (isMouseOver) {
			Canvas.app.strokeWeight(2);
			Canvas.app.stroke(255, 0, 0);
			Canvas.app.ellipse(pos.x, pos.y, getDiameter(), getDiameter());
			Canvas.app.strokeWeight(1);
			Canvas.app.stroke(255, 255, 255);
		}
	}

	public void mouseMoved(MouseEvent e) {
			sense();
	}


	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}
}