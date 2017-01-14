package visualElements;

import processing.core.*;

import java.awt.Color;

/**
 * This class draws bezier curves from two nodes. The source node is the origin
 * of the curve and target node is the end. The curve may be drawn in three
 * parts: the first third is the head that goes attached to the source. The last
 * third is the tail attached to the target. Head and tail are drawn on top of
 * the curve body, leaving exposed the middle part.
 * 
 * @author jsalam
 *
 */
public class Bezier {
	public static final int NORMAL = 1;
	public static final int PROPAGATE = 2;
	// The source, target, source's control point, and target's control point
	private PVector A, B, cA, cB;
	private int localAlpha;
	// The bezier body color.
	private Color bodyColor = new Color(147, 111, 180);
	// The body color in current use
	private Color currentColor = bodyColor;
	private Color headColor = new Color(125, 125, 25);
	private Color tailColor = new Color(232, 20, 23);
	private Color propagated = new Color(250, 0, 0);
	// The default angle of control points
	private double inclination = Math.PI / 2;
	// The default length of control points
	private float sagitta = 140;

	// Constructors
	/**
	 * The Beziers are instantiated defining the control points of source and
	 * target PVectors and a sagitta corresponding to the lenght of control
	 * points.
	 * 
	 * @param source
	 *            Start PVector
	 * @param target
	 *            End PVector
	 * @param sagitta
	 *            The displacement of the control point. Set to 20 by default
	 */
	public Bezier(PVector source, PVector target) {
		A = source;
		B = target;
		localAlpha = 255;
	}

	/**
	 * A Bezier with source and target at the same origin
	 *
	 * @param aboveArc
	 */
	public Bezier() {
		A = new PVector(0, 0);
		B = new PVector(0, 0);
		localAlpha = 255;
	}

	/**
	 * This bezier gets source, target and their corresponding control points.
	 * It is used to draw segments of primary beziers returned by the
	 * getBezierThird() method.
	 * 
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

	/**
	 * Returns the distance between two points. It is used to calculate the
	 * chord of two points (the source and target PVectors) cutting a
	 * circumference
	 * 
	 * @return
	 */
	private float getChord() {
		double deltaX = Math.pow((A.x - B.x), 2);
		double deltaY = Math.pow((A.y - B.y), 2);
		return (float) Math.sqrt(deltaX + deltaY);
	}

	/**
	 * Returns the angle described by two points (the source and target
	 * PVectors). The angle is measured in radians being 0 or TWO_PI at three
	 * o'clock. The angle increments clockwise.
	 * 
	 * @param app
	 * @return
	 */
	private double getDirection(PApplet app) {
		double angle = 0;
		app.pushMatrix();
		app.translate(A.x, A.y);
		angle = PApplet.atan2(B.y - A.y, B.x - A.x);
		app.popMatrix();
		if (angle < 0) {
			// This is to complete TWO_PI radians over because after PI it
			// diminishes its value
			angle = PApplet.TWO_PI + angle;
		}
		return angle;
	}

	public void setControlInclination(float radius) {
		double angle;
		if (radius == 0) {
			angle = Math.PI/2;
		} else {
			angle = Math.asin((getChord() / 2) / radius);
		}
		System.out.println(this.getClass().getName() +  " ANGLE: " + angle + ", RADIUS "+ radius + ", Chord:"+ (getChord() / 2) );
		this.inclination = angle;
	}

	/**
	 * Returns a PVector corresponding to the control point of a origin PVector.
	 * The control point is off the origin of the same magnitude as the sagitta,
	 * and describes a square angle with the chord. The angle between the
	 * control point and the chord could be modified changing the value of
	 * "inclination", which if initially parameterized at HALF_PI.
	 * 
	 * @param origin
	 * @param angle
	 * @return
	 */
	private PVector getControlPoint(PVector origin, double angle, double inc) {
		PVector rtn = null;
		double X = Math.cos(angle + inc);
		double Y = Math.sin(angle + inc);
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
	 * Draws all the components of the bezier. It is useful to draft new shapes
	 * 
	 * @param app
	 */
	public void drawBezierAndControls(PApplet app, float thickness) {
		app.noFill();
		app.strokeWeight(thickness);
		cA = getControlPoint(A, getDirection(app), inclination);
		cB = getControlPoint(B, getDirection(app), 2*inclination);
		app.stroke(255, 0, 0, 50);
		app.line(A.x, A.y, cA.x, cA.y);
		app.ellipse(A.x, A.y, 3, 3);
		app.line(B.x, B.y, cB.x, cB.y);
		app.ellipse(B.x, B.y, 3, 3);
		app.stroke(0, 255, 0, 50);
		app.line(A.x, A.y, B.x, B.y);
		app.text((float)inclination,cA.x, cA.y);
		app.stroke(currentColor.getRGB(), localAlpha);
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
	}

	/**
	 * Draws just the bezier with the currentColor() and localAlpha values.
	 * 
	 * @param app
	 * @param thickness
	 *            The thickness does not belong to this class but to VEdge,
	 *            therefore it is received as a parameter. If the thickness is
	 *            less that 1 it is set to 1.
	 */
	public void drawBezier2D(PApplet app, float thickness) {
//		cA = getControlPoint(A, getDirection(app), inclination + Math.PI / 2);
//		cB = getControlPoint(B, getDirection(app), inclination - Math.PI / 2);
		app.noFill();
		app.stroke(currentColor.getRGB(), localAlpha);
		if (thickness < 1)
			thickness = 1;
		app.strokeWeight(thickness);
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
	}

	/**
	 * Used to draw edge's tails and heads
	 * 
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

	/**
	 * Draws the first third of the bezier curve starting from the source
	 * PVector
	 * 
	 * @param app
	 * @param thickness
	 * @param alpha
	 */
	public void drawHeadBezier2D(PApplet app, float thickness, int alpha) {
		getBezierThird(A, cA, cB, B).drawBezier2D(app, headColor, thickness, alpha);
		// System.out.println("Bezier> drawHeadBezier2D:"+ alpha);
	}

	/**
	 * Draws the last third of the bezier curve starting from the source PVector
	 * 
	 * @param app
	 * @param thickness
	 * @param alpha
	 */
	public void drawTailBezier2D(PApplet app, float thickness, int alpha) {
		getBezierThird(B, cB, cA, A).drawBezier2D(app, tailColor, thickness, alpha);
	}

	/**
	 * Draws both first and third parts of the bezier curve starting from the
	 * source PVector
	 * 
	 * @param app
	 * @param thicknessHead
	 * @param thicknessTail
	 * @param alpha
	 */
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
	private Bezier getBezierThird(PVector A, PVector cA, PVector cB, PVector B) {

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
	public void setSourceAndTarget(PVector source, PVector target) {
		A.set(source);
		B.set(target);
	}

	/**
	 * Returns a PVector corresponding to the first third of the line connecting
	 * two points. The starting point is the first parameterized Vector
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
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
		this.currentColor = new Color(color);
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

	/**
	 * Sets all the bezier colors darker using java.awt.Color darker() method
	 */
	public void darker() {
		bodyColor.darker();
		headColor.darker();
		tailColor.darker();
		propagated.darker();
	}

	/**
	 * Sets all the bezier colors darker using java.awt.Color brighter() method
	 */
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

	/**
	 * Sets the bezier's body color to PROPAGATE color (red) or to NORMAL
	 * bodyColor.
	 * 
	 * @param val
	 */
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
