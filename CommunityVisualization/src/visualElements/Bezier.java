package visualElements;

import processing.core.*;

public class Bezier {
	public PVector A, B, cA, cB;
	int alpha = 80;


	/**
	 * Bezier 2D
	 */
	public Bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		A = new PVector(x1, y1);
		cA = new PVector(x2, y2);
		cB = new PVector(x3, y3);
		B = new PVector(x4, y4);
	}

	/**
	 * Bezier 3D
	 */
	public Bezier(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3,
			float x4, float y4, float z4) {
		A = new PVector(x1, y1, z1);
		cA = new PVector(x2, y2, z2);
		cB = new PVector(x3, y3, z3);
		B = new PVector(x4, y4, z4);

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

	public void drawBezier2D(PApplet app) {
		app.noFill();
		app.bezier(A.x, A.y, cA.x, cA.y, cB.x, cB.y, B.x, B.y);
		app.stroke(150,alpha);
	}

	public void drawBezier3D(PApplet app) {
		app.noFill();
		app.bezier(A.x, A.y, A.z, cA.x, cA.y, cA.z, cB.x, cB.y, cB.z, B.x, B.y, cB.z);
		app.stroke(200,alpha/2);
	}

	public void drawHeadBezier2D(PApplet app) {
		app.stroke(255, 158, 15, alpha);
		getBezierThird(A, cA, cB, B).drawBezier2D(app);
	}

	public void drawTailBezier2D(PApplet app) {
		app.stroke(115,33,86,alpha);
		getBezierThird(B, cB, cA, A).drawBezier2D(app);
	}

	public void drawHeadAndTailBezier2D(PApplet app) {
		app.stroke(255, 158, 15, alpha);
		getBezierThird(A, cA, cB, B).drawBezier2D(app);
		app.stroke(115,33,86,alpha);
		getBezierThird(B, cB, cA, A).drawBezier2D(app);
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
}
