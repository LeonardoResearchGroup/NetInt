package netInt.visualElements;

import netInt.canvas.Canvas;
import netInt.utilities.geometry.CircleCircleIntersection;
import netInt.utilities.geometry.CircleCircleTangent;
import netInt.visualElements.primitives.VisualAtom;
import processing.core.PApplet;
import processing.core.PVector;

public class Tray extends VisualAtom {
	
	private PVector circle1;
	
	private PVector circle2;
	
	private int segments;
	
	private float[] anglesA;
	
	private float[] anglesB;

	public Tray(float circle1X, float circle1Y, int circle1R) {
		
		super(circle1X, circle1Y, circle1R, circle1R);
		
		circle1 = new PVector(circle1X, circle1Y, circle1R);
		
		segments = 7;
		
		anglesA = new float[segments + 3];
		
		anglesB = new float[segments + 3];
	}

//	public Tray(float circle1X, float circle1Y, int circle1R, float circle2X, float circle2Y, int circle2R) {
//		
//		super(circle1X, circle1Y, circle1R, circle1R);
//		
//		circle1 = new PVector(circle1X, circle1Y, circle1R);
//		
//		circle2 = new PVector(circle2X, circle2Y, circle2R);
//		
//		segments = 7;
//		
//		anglesA = new float[segments + 3];
//		
//		anglesB = new float[segments + 3];
//		
//		makeTangents();
//	}

//	public void update(float circle1X, float circle1Y, float circle1R, float circle2X, float circle2Y,
//			float circle2R) {
//		
//		circle1 = new PVector(circle1X, circle1Y, circle1R);
//		
//		circle2 = new PVector(circle2X, circle2Y, circle2R);
//		
//		makeTangents();
//	}

	PVector [] points;
	public void updateCircle1(float circle1X, float circle1Y, float circle1R) {
		circle1 = new PVector(circle1X, circle1Y, circle1R);
		
		if (circle2 != null)
		
			//makeTangents();
			
			points = CircleCircleTangent.getTangetPoints(circle1, circle2);
	}

	public void updateCircle2(float circle2X, float circle2Y, float circle2R) {
		circle2 = new PVector(circle2X, circle2Y, circle2R);
		
		//makeTangents();
		
		points = CircleCircleTangent.getTangetPoints(circle1, circle2);
	}

	public void show() {
		
		if (points != null) {
			Canvas.app.stroke(0);
			Canvas.app.strokeWeight(3);
			Canvas.app.line(points[0].x, points[0].y, points[1].x, points[1].y);
			Canvas.app.line(points[2].x, points[2].y, points[3].x, points[3].y);
		}

		Canvas.app.fill(color.getRGB(), 20);

		if (circle2 != null) {
			// display
			Canvas.app.stroke(30,70);
			
			Canvas.app.beginShape();
			
			// first circle
			float posX = 0;
			
			float posY = 0;
			
			for (int i = 0; i < anglesA.length - 1; i++) {
			
				posX = circle1.x + PApplet.cos(anglesA[i]) * circle1.z / 2;
				
				posY = circle1.y + PApplet.sin(anglesA[i]) * circle1.z / 2;
				
				Canvas.app.curveVertex(posX, posY);
				
			}

			// second circle
			for (int i = 0; i < anglesB.length; i++) {
			
				posX = circle2.x + PApplet.cos(anglesB[i]) * circle2.z / 2;
				
				posY = circle2.y + PApplet.sin(anglesB[i]) * circle2.z / 2;
				
				Canvas.app.curveVertex(posX, posY);
			}
			
			Canvas.app.endShape(PApplet.CLOSE);
			
		} else {

			Canvas.app.ellipse(circle1.x, circle1.y, circle1.z, circle1.z);
		}

	}

	@Override
	public void eventRegister(PApplet theApp) {
		// TODO Auto-generated method stub
		
	}

}