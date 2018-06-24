package netInt.utilities.geometry;

import geomerative.RPath;
import geomerative.RPoint;
import geomerative.RShape;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class CircleCircleTangent {

	/**
	 * Gets the tangent points of both circles. Implements Wikipedia solution *outer
	 * tangent* https://en.wikipedia.org/wiki/Tangent_lines_to_circles Steps:
	 * 
	 * 1 Find the angle gamma between centers Find the angle aG that complements the
	 * perpendicular angle described by the center of the circles and the tangential
	 * point
	 * 
	 * 2 Adjust the sign of gamma according to the position of the first circle
	 * relative to the second circle
	 * 
	 * 3 Find the angle beta described by the line connecting the centers and a line
	 * connecting the tangential points projected to the center of the smallest one
	 * 
	 * 4 Find alpha above and below gamma
	 * 
	 * 5 Estimate final angles
	 * 
	 * 6 Find points
	 * 
	 * @param circle1
	 *            PVector x,y,radius
	 * @param circle2
	 *            PVector x,y,radius
	 * @return Array with 4 positions:
	 * 
	 *         [0] PVector A of first point of circle 1,
	 * 
	 *         [1] PVector B of second point of circle 1,
	 * 
	 *         [2] PVector A of first point of circle 2,
	 * 
	 *         [3] PVector B of second point of circle 2
	 */
	public static PVector[] getTangetPoints(PVector circle1, PVector circle2) {
		// 1 gamma
		float gamma = -PApplet.atan((circle2.y - circle1.y) / (circle2.x - circle1.x));

		// 2
		if (circle2.x < circle1.x) {
			gamma = PApplet.PI + gamma;
		}

		// 3 beta
		float numerator = circle2.z / 2 - circle1.z / 2;

		float beta = PApplet.asin(numerator / PApplet.dist(circle1.x, circle1.y, circle2.x, circle2.y));

		// 4 alpha
		float alphaA = PApplet.HALF_PI - gamma - beta;
		float alphaB = PApplet.PI + PApplet.HALF_PI - gamma + beta;

		// 5 angles for vertices of circle 1
		float[] anglesC1 = new float[2];
		anglesC1[0] = alphaA - PApplet.PI;
		anglesC1[1] = alphaB + PApplet.PI;

		// angles for vertices of circle 2
		float[] anglesC2 = new float[2];
		anglesC2[0] = alphaA - PApplet.PI;
		anglesC2[1] = alphaB + PApplet.PI;

		// 6 points circle 1
		PVector[] pointsC = new PVector[4];
		pointsC[0] = new PVector(PApplet.cos(anglesC1[0]) * circle1.z / 2, PApplet.sin(anglesC1[0]) * circle1.z / 2);
		pointsC[1] = new PVector(PApplet.cos(anglesC1[1]) * circle1.z / 2, PApplet.sin(anglesC1[1]) * circle1.z / 2);
		pointsC[0].add(circle1);
		pointsC[1].add(circle1);

		// points circle 2
		pointsC[2] = new PVector(PApplet.cos(anglesC2[0]) * circle2.z / 2, PApplet.sin(anglesC2[0]) * circle2.z / 2);
		pointsC[3] = new PVector(PApplet.cos(anglesC2[1]) * circle2.z / 2, PApplet.sin(anglesC2[1]) * circle2.z / 2);
		pointsC[2].add(circle2);
		pointsC[3].add(circle2);

		return pointsC;
	}

	/**
	 * Gets a parallelepiped which corners are the tangent points of both circle.
	 * 
	 * @param circle1
	 *            PVector x,y,radius
	 * @param circle2
	 *            PVector x,y,radius
	 * @return RShape from library geomerative
	 */
	public static RShape getTangentRBox(PVector circle1, PVector circle2) {

		if (CircleCircleIntersection.validateInclusion(circle1, circle2)
				|| CircleCircleIntersection.validateInclusion(circle2, circle1)) {
			return null;
		} else {
			PVector[] points = getTangetPoints(circle1, circle2);
			RPoint[] rPoints = new RPoint[points.length];

			rPoints[0] = new RPoint(points[0].x, points[0].y);
			rPoints[1] = new RPoint(points[2].x, points[2].y);
			rPoints[2] = new RPoint(points[3].x, points[3].y);
			rPoints[3] = new RPoint(points[1].x, points[1].y);

			try {
				RPath path = new RPath(rPoints);
				path.addClose();
				return new RShape(path);
			} catch (NullPointerException np) {
				return null;
			}
		}
	}

	/**
	 * Gets a parallelepiped which corners are the tangent points of both circle.
	 * 
	 * @param circle1
	 *            RShape from library geomerative
	 * @param circle2
	 *            RShape from library geomerative
	 * @return RShape from library geomerative
	 */
	public static RShape getTangentRBox(RShape rCircle1, RShape rCircle2) {

		// convert RShape to PVector if they are circles
		float radius1, radius2;
		if (rCircle1.getHeight() == rCircle1.getWidth()) {
			radius1 = rCircle1.getHeight();
		} else {
			return null;
		}

		if (rCircle2.getHeight() == rCircle2.getWidth()) {
			radius2 = rCircle2.getHeight();
		} else {
			return null;
		}

		PVector circle1 = new PVector(rCircle1.getCenter().x, rCircle1.getCenter().y, radius1);
		PVector circle2 = new PVector(rCircle2.getCenter().x, rCircle2.getCenter().y, radius2);

		return getTangentRBox(circle1, circle2);
	}
}
