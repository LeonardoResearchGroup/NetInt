package visualElements;

import processing.core.*;

import java.awt.Color;

public class BezierBeta {
	public static final int NORMAL = 1;
	public static final int PROPAGATE = 2;
	private PVector A, B, cA, cB;
	private int localAlpha;
	private Color bodyColor = new Color(147, 111, 180);
	private Color currentColor = bodyColor;
	private Color headColor = new Color(125, 125, 25);
	private Color tailColor = new Color(232, 20, 23);
	private Color propagated = new Color(250, 0, 0);
	private double inclination = Math.PI / 2;
	private float sagitta = 20;
	private boolean aboveArc;

	// Constructors
	/**
	 * The Beziers are instantiated defining the control points of origin and
	 * destination PVectors and a distance.
	 * 
	 * @param source
	 *            Start PVector
	 * @param target
	 *            End PVector
	 * @param sagitta
	 *            The displacement of the control point
	 */
	public BezierBeta(PVector source, PVector target, float sagitta) {
		A = source;
		B = target;
		if (sagitta > getChord() / 2) {
			sagitta = getChord() / 2;
		} else {
			this.sagitta = sagitta;
		}
		localAlpha = 255;
	}

	/**
	 * A Bezier with source and target at the same origin
	 *
	 * @param aboveArc
	 */
	public BezierBeta() {
		A = new PVector(0, 0);
		B = new PVector(0, 0);
		localAlpha = 255;
	}

	/**
	 * @param app
	 * @param oA
	 *            Origin A
	 * @param cA
	 *            Control point of A
	 * @param cB
	 *            Control point of B
	 * @param oB
	 *            Origin B
	 */
	public BezierBeta(PVector oA, PVector cA, PVector cB, PVector oB) {
		this.A = oA;
		this.cA = cA;
		this.cB = cB;
		this.B = oB;
		localAlpha = 255;
	}

	private float getChord() {
		double deltaX = Math.pow((A.x - B.x), 2);
		double deltaY = Math.pow((A.y - B.y), 2);
		return (float) Math.sqrt(deltaX + deltaY);
	}

	private double getDirection() {
		double angle = 0;
		float deltaX = A.x - B.x;
		// If target is at the left of source
		if (deltaX < 0) {
			angle = Math.PI;
		}
		float chord = getChord();
		double deltaY = A.y - B.y;
		if (A.y != B.y) {
			angle = (deltaY / chord);
		}
		// Validate in which quadrant falls the angle and make the correction
		return Math.asin(angle);
	}

	private double getDirectionB(PApplet app) {
		double angle = 0;
		app.pushMatrix();
		app.translate(A.x, A.y);
		angle = PApplet.atan2(B.y - A.y, B.x - A.x);
		app.popMatrix();
		if (angle < 0) {
			angle = PApplet.TWO_PI + angle;
		}
		return angle;
	}

	private PVector getControlPoint(PVector origin, double angle) {
		PVector rtn = null;
		double X = Math.cos(angle - inclination);
		double Y = Math.sin(angle - inclination);
		if (sagitta > getChord() / 2) {
			sagitta = getChord() / 2;
		}
		X = X * sagitta;
		Y = Y * sagitta;
		rtn = new PVector((float) X, (float) Y);
		return rtn.add(origin);
	}

	// Display Methods
	/**
	 * To be used after the source and target PVectors were set after
	 * instantiation. See setSourceAndTarget()
	 * 
	 * @param app
	 */
	public void drawBezierAndControls(PApplet app, float thickness) {
		app.noFill();
		app.strokeWeight(thickness);
		cA = getControlPoint(A, getDirectionB(app));
		cB = getControlPoint(B, getDirectionB(app));
		app.stroke(255, 0, 0, 50);
		app.line(A.x, A.y, cA.x, cA.y);
		app.ellipse(A.x, A.y, 3, 3);
		app.line(B.x, B.y, cB.x, cB.y);
		app.ellipse(B.x, B.y, 3, 3);
		app.stroke(0, 255, 0, 50);
		app.line(A.x, A.y, B.x, B.y);
		app.stroke(currentColor.getRGB(), localAlpha);
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
	}

	/**
	 * 
	 * @param app
	 * @param thickness
	 */
	public void drawBezier2D(PApplet app, float thickness) {
		cA = getControlPoint(A, getDirectionB(app));
		cB = getControlPoint(B, getDirectionB(app));
		app.noFill();
		app.stroke(currentColor.getRGB(), localAlpha);
		app.strokeWeight(thickness);
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
	}


	/**
	 * Used to draw edge's tails and heads
	 * @param app
	 * @param color
	 * @param thickness
	 * @param alpha
	 */
	private void drawBezier2D(PApplet app, Color color, float thickness, int alpha) {
		app.noFill();
		app.stroke(color.getRGB(), alpha);
		// System.out.println("Bezier> drawBezier2D B:"+ alpha);
		app.strokeWeight(thickness);
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
	}

	public void drawHeadBezier2D(PApplet app, float thickness, int alpha) {
		getBezierThird(A, cA, cB, B).drawBezier2D(app, headColor, thickness, alpha);
		// System.out.println("Bezier> drawHeadBezier2D:"+ alpha);
	}

	public void drawTailBezier2D(PApplet app, float thickness, int alpha) {
		getBezierThird(B, cB, cA, A).drawBezier2D(app, tailColor, thickness, alpha);
	}

	public void drawHeadAndTailBezier2D(PApplet app, float thicknessHead, float thicknessTail, int alpha) {
		getBezierThird(A, cA, cB, B).drawBezier2D(app, headColor, thicknessHead, alpha);
		getBezierThird(B, cB, cA, A).drawBezier2D(app, tailColor, thicknessTail, alpha);
	}

	// Partition methods
	/**
	 * based on
	 * http://stackoverflow.com/questions/18655135/divide-bezier-curve-into-two-
	 * equal-halves
	 * 
	 * Set A to the anchor you want to be the source
	 * 
	 * @param A
	 *            The start or end of the bezier curve
	 * @param cA
	 *            The control point of A
	 * @param cB
	 *            The control point of B
	 * @param B
	 *            The opposite end of the bezier curve
	 * @return
	 */
	public BezierBeta getBezierThird(PVector A, PVector cA, PVector cB, PVector B) {

		// Find C that is the first third along the straight line [A cA]
		PVector C = getThird(A, cA);

		// Find D that is the first third along the straight line [cA cB]
		PVector D = getThird(cA, cB);

		// Find E that is the first third along the straight line [cB B]
		PVector E = getThird(cB, B);

		// Find F that is the first third along the straight line [C D].
		PVector F = getThird(C, D);

		// Find G that is the first third along the straight line [D E].
		PVector G = getThird(D, E);

		// Finally, find H that is the first third along the straight line
		// FG.
		PVector H = getThird(F, G);
		return new BezierBeta(A, C, F, H);
	}

	// Getters and setters
	public void setSourceAndTarget(PVector source, PVector target) {
		A.set(source);
		B.set(target);
	}

	private PVector getThird(PVector a, PVector b) {
		float tempX = a.x - ((a.x - b.x) / 3);
		float tempY = a.y - ((a.y - b.y) / 3);
		return new PVector(tempX, tempY);
	}

	public void setAlpha(int val) {
		this.localAlpha = val;
	}

	public void setHeadColor(Color headColor) {
		this.headColor = headColor;
	}

	public void setColor(int color) {
		this.bodyColor = new Color(color);
		currentColor = bodyColor;
	}

	public void setTailColor(Color tailColor) {
		this.tailColor = tailColor;
	}

	public void setSagitta(float sagitta) {
		this.sagitta = sagitta;
	}

	public Color getPropagated() {
		return propagated;
	}

	public void setPropagated(Color propagated) {
		this.propagated = propagated;
	}

	// public float getControl() {
	// return control;
	// }

	public boolean isAboveArc() {
		return aboveArc;
	}

	// public void setControl(float control) {
	// this.control = control;
	// }

	public void setAboveArc(boolean aboveArc) {
		this.aboveArc = aboveArc;
	}

	public void darker() {
		bodyColor.darker();
		headColor.darker();
		tailColor.darker();
		propagated.darker();
	}

	public void brighter() {
		bodyColor.brighter();
		headColor.brighter();
		tailColor.brighter();
		propagated.brighter();
	}

	public void resetColor() {
		bodyColor = Color.WHITE;
		headColor = new Color(255, 158, 15);
		tailColor = new Color(115, 33, 86);
		propagated = new Color(200, 100, 250);
	}

	public void color(int val) {
		switch (val) {
		case (NORMAL):
			currentColor = bodyColor;
			break;
		case (PROPAGATE):
			currentColor = propagated;
		}
	}
}
