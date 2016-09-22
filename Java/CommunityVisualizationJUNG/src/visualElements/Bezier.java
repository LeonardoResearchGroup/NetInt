package visualElements;

import processing.core.*;

import java.awt.Color;

public class Bezier {
	public static final int NORMAL = 1;
	public static final int PROPAGATE = 2;
	private PVector A, B, cA, cB;
	private int localAlpha;
	private Color bodyColor = new Color(75,38,93);
	private Color currentColor = bodyColor;
	private Color headColor = new Color(125,125,25);
	private Color tailColor = new Color(232, 20, 23);
	private Color propagated = new Color(250,0,0);
	private float control;
	private boolean aboveArc;

	// Constructors
	/**
	 * Bezier 2D
	 */
	public Bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		A = new PVector(x1, y1);
		cA = new PVector(x2, y2);
		cB = new PVector(x3, y3);
		B = new PVector(x4, y4);
		control = Math.abs(y1 - y2);
		aboveArc = false;
		localAlpha = 255;
	}

	/**
	 * The Beziers are instantiated defining the control points of origin and
	 * destination PVectors that are passed later in the show method.
	 * 
	 * @param source
	 *            Start PVector
	 * @param target
	 *            End PVector
	 * @param distanceControlPoint
	 *            The displacement of the control point
	 * @param isLoop
	 */
	public Bezier(PVector source, PVector target, float distanceControlPoint, boolean aboveArc) {
		A = source;
		B = target;
		control = distanceControlPoint;
		this.aboveArc = aboveArc;
		updateControlPoints(source, target);
		localAlpha = 255;
	}

	/**
	 * The Beziers are instantiated defining the control points for PVectors at
	 * (0,0).
	 *
	 * @param distanceControlPoint
	 *            The displacement of the control point
	 * @param isLoop
	 */
	public Bezier(float distanceControlPoint, boolean aboveArc) {
		A = new PVector(0, 0);
		B = new PVector(0, 0);
		control = distanceControlPoint;
		this.aboveArc = aboveArc;
		updateControlPoints(A, B);
		localAlpha = 255;
	}

	/**
	 * A Bezier is instantiated defining just its convexity
	 *
	 * @param aboveArc
	 */
	public Bezier(boolean aboveArc) {
		A = new PVector(0, 0);
		B = new PVector(0, 0);
		control = 0;
		this.aboveArc = aboveArc;
		updateControlPoints(A, B);
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
	public Bezier(PVector oA, PVector cA, PVector cB, PVector oB) {
		this.A = oA;
		this.cA = cA;
		this.cB = cB;
		this.B = oB;
		localAlpha = 255;
	}

	public void updateControlPoints(PVector source, PVector target) {
		float modifier = Math.abs(control);
		// if directed graph
		if (!aboveArc) {
			modifier = -modifier;
		}
		// if loop
		if (A.equals(B)) {
			cA = new PVector(A.x - 20, A.y - 20);
			cB = new PVector(B.x + 20, B.y - 20);
		} else {
			cA = new PVector(A.x, A.y - modifier);
			cB = new PVector(B.x, B.y - modifier);
		}
	}

	public void setAndUpdateSourceAndTarget(PVector source, PVector target) {
		A.set(source);
		B.set(target);
		updateControlPoints(A, B);
	}

	// Display Methods
	/**
	 * To be used after the source and target PVectors were set after
	 * instantiation. See setSourceAndTarget()
	 * 
	 * @param app
	 * @param source
	 * @param target
	 */
	public void drawBezier2D(PApplet app, float thickness) {
		updateControlPoints(A, B);
		app.noFill();
		app.stroke(currentColor.getRGB(), localAlpha);
		//System.out.println("Bezier> drawBezier2D A: "+ localAlpha);
		app.strokeWeight(thickness);
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
	}

	/**
	 * To be used after the source and target PVectors were set after
	 * instantiation. See setSourceAndTarget()
	 * 
	 * @param app
	 * @param source
	 * @param target
	 */
	private void drawBezier2D(PApplet app, Color color, float thickness, int alpha) {
		app.noFill();
		app.stroke(color.getRGB(), alpha);
		//System.out.println("Bezier> drawBezier2D B:"+ alpha);
		app.strokeWeight(thickness);
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
	}

	public void drawHeadBezier2D(PApplet app, float thickness, int alpha) {
		getBezierThird(A, cA, cB, B).drawBezier2D(app, headColor, thickness, alpha);
		//System.out.println("Bezier> drawHeadBezier2D:"+ alpha);
	}

	public void drawTailBezier2D(PApplet app, float thickness, int alpha) {
		getBezierThird(B, cB, cA, A).drawBezier2D(app, tailColor, thickness,alpha);
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
	public Bezier getBezierThird(PVector A, PVector cA, PVector cB, PVector B) {

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
		return new Bezier(A, C, F, H);
	}

	// Getters and setters
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
	}

	public void setTailColor(Color tailColor) {
		this.tailColor = tailColor;
	}

	public Color getPropagated() {
		return propagated;
	}

	public void setPropagated(Color propagated) {
		this.propagated = propagated;
	}

	public float getControl() {
		return control;
	}

	public boolean isAboveArc() {
		return aboveArc;
	}

	public void setControl(float control) {
		this.control = control;
	}

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
