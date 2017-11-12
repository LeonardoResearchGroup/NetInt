package examples;

import netInt.canvas.Canvas;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;

public class GraphicsMaker {

	private Circle[] circles;

	private PGraphics[] graphics;
	boolean done = false;

	public GraphicsMaker() {
		init();
	}

	public void init() {
		circles = new Circle[80000];

		for (int i = 0; i < circles.length; i++) {
			circles[i] = new Circle((float) Math.random() * Canvas.app.width,
					(float) Math.random() * (Canvas.app.height - 100));
		}
		graphics = new PGraphics[2];

		// Bottom layer
		graphics[0] = Canvas.app.createGraphics(Canvas.app.width, Canvas.app.height, PApplet.P2D);

		// Top layer
		graphics[1] = Canvas.app.createGraphics(Canvas.app.width, Canvas.app.height, PApplet.P2D);
		
		makeBottomLayer();
		makeTopLayer();
		eventRegister(Canvas.app);
	}

	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}

	public void mouseEvent(MouseEvent e) {

		if (e.getAction() == MouseEvent.CLICK) {
			makeBottomLayer();
		}

		if (e.getAction() == MouseEvent.MOVE) {
			makeTopLayer();
		}

	}

	public void makeBottomLayer() {
		graphics[0].beginDraw();
		graphics[0].background(70);
		for (int i = 0; i < circles.length; i++) {
			circles[i].show();
		}
		graphics[0].endDraw();
		System.out.println("Graphics made");
	}

	public void makeTopLayer() {
		graphics[1].beginDraw();
		graphics[1].clear();
		graphics[1].line(Canvas.app.mouseX, 0, Canvas.app.mouseX, Canvas.app.height);
		graphics[1].endDraw();
	}

	public PGraphics[] getPGraphic() {
		return graphics;
	}

	///// **********
}
