package visualElements;

import processing.core.*;

import java.awt.Color;

public class Bezier {
	public PVector A, B, cA, cB;
	private int alpha = 40;
	private Color bodyColor = Color.WHITE;
	private Color headColor = new Color (255, 158, 15);
	private Color tailColor = new Color (115, 33, 86);
	private float control;
	private boolean aboveArc;

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
	
	public void setAndUpdateSourceAndTarget(PVector source, PVector target){
		A.set(source);
		B.set(target);
		updateControlPoints(A, B);
	}

	/**
	 * To be used after the source and target PVectors were set after instantiation. See setSourceAndTarget()
	 * @param app
	 * @param source
	 * @param target
	 */
	public void drawBezier2D(PApplet app) {
		updateControlPoints(A, B);
		app.noFill();
		app.stroke(bodyColor.getRGB(), alpha);
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
	}
	
	/**
	 * To be used after the source and target PVectors were set after instantiation. See setSourceAndTarget()
	 * @param app
	 * @param source
	 * @param target
	 */
	public void drawBezier2D(PApplet app, Color color) {
		app.noFill();
		app.stroke(color.getRGB(), alpha);
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
	}
	
	public void drawHeadBezier2D(PApplet app) {
		getBezierThird(A, cA, cB, B).drawBezier2D(app,headColor);
	}

	public void drawTailBezier2D(PApplet app) {
		getBezierThird(B, cB, cA, A).drawBezier2D(app,tailColor);
	}

	public void drawHeadAndTailBezier2D(PApplet app) {
		getBezierThird(A, cA, cB, B).drawBezier2D(app,headColor);
		getBezierThird(B, cB, cA, A).drawBezier2D(app,tailColor);
	}

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

	private PVector getThird(PVector a, PVector b) {
		float tempX = a.x - ((a.x - b.x) / 3);
		float tempY = a.y - ((a.y - b.y) / 3);
		return new PVector(tempX, tempY);
	}

	public void setAlpha(int val) {
		alpha = val;
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
}