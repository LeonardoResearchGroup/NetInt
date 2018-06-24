package netInt.utilities.geometry;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * The main goal of this class is to find the intersecting points of two
 * circles.
 * 
 * @author juan salamanca
 *
 */
public abstract class CircleCircleIntersection {
	/*
	 * Implementation of
	 * http://www.ambrsoft.com/TrigoCalc/Circles2/circle2intersection/
	 * CircleCircleIntersection.htm
	 */

	/**
	 * True is there is a strict intersection.
	 * 
	 * @param circleA
	 *            first circle
	 * @param circleB
	 *            second circle
	 * @return true if circles intersect
	 */
	public static boolean validateIntersection(PVector circleA, PVector circleB) {
		if (circleA.z + circleB.z > dist(circleA, circleB) && dist(circleA, circleB) > PApplet.abs(circleA.z - circleB.z)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Validates that one circle is inside another
	 * 
	 * @param circleA
	 *            first circle
	 * @param circleB
	 *            second circle
	 * @return True if the first circle is totally included inside the second circle
	 */
	public static boolean validateInclusion(PVector circleA, PVector circleB) {
		if (dist(circleA, circleB) + circleA.z/2 <= circleB.z/2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Finds the intersecting points between two overlapping circles. IMPORTANT: The
	 *         order of vectors depends of the order of circles.
	 * 
	 * @param circleA
	 *            first circle
	 * @param circleB
	 *            second circle
	 * @return two vectors with x and y positions for each point. IMPORTANT: The
	 *         order of vectors depends of the order of circles. If no intersection
	 *         returns null
	 */
	public static PVector[] twoIntersectionPoints(PVector circleA, PVector circleB) {
		PVector[] rtn = new PVector[2];
		if (validateIntersection(circleA, circleB)) {
			float w = getW(circleA, circleB);
			float dist2 = PApplet.pow(dist(circleA, circleB), 2);

			float term1 = (circleA.x + circleB.x) / 2;
			float term2 = ((circleB.x - circleA.x) * (PApplet.pow(circleA.z, 2) - PApplet.pow(circleB.z, 2))) / (2 * dist2);
			float term3 = 2 * ((circleA.y - circleB.y) / dist2) * w;

			float x0 = term1 + term2 + term3;
			float x1 = term1 + term2 - term3;

			term1 = (circleA.y + circleB.y) / 2;
			term2 = ((circleB.y - circleA.y) * (PApplet.pow(circleA.z, 2) - PApplet.pow(circleB.z, 2))) / (2 * dist2);
			term3 = 2 * ((circleA.x - circleB.x) / dist2) * w;

			float y0 = term1 + term2 - term3;
			float y1 = term1 + term2 + term3;

			// For circle circleA
			rtn[0] = new PVector(x0, y0);
			rtn[1] = new PVector(x1, y1);

		}
		return rtn;
	}

	/**
	 * Finds the middle point between two centers
	 * 
	 * @param circleA
	 *            first circle
	 * @param circleB
	 *            second circle
	 * @return x and y of middle point
	 */
	public static PVector middlePoint(PVector circleA, PVector circleB) {
		float midX = (circleA.x + circleB.x) / 2;
		float midY = (circleA.y + circleB.y) / 2;
		return new PVector(midX, midY);
	}

	public static float dist(PVector circleA, PVector circleB) {
		return PApplet.dist(circleA.x, circleA.y, circleB.x, circleB.y);
	}

	/**
	 * W is the area of the triangle formed by the two circle centers and one of the
	 * intersection point. Calculated with Heron's formula
	 * https://en.wikipedia.org/wiki/Heron%27s_formula
	 */
	private static float getW(PVector circleA, PVector circleB) {
		float term1 = dist(circleA, circleB) + circleA.z + circleB.z;
		float term2 = dist(circleA, circleB) + circleA.z - circleB.z;
		float term3 = dist(circleA, circleB) - circleA.z + circleB.z;
		float term4 = -dist(circleA, circleB) + circleA.z + circleB.z;
		return (1f / 4f) * PApplet.sqrt(term1 * term2 * term3 * term4);
	}

	/**
	 * Returns the start and end angle in radians described by the intersecting
	 * points and the center of the first circle
	 * 
	 * @param circleA
	 *            first circle
	 * @param circleB
	 *            second circle
	 * @return angle in radians
	 */
	public static float[] startEndAngleIntersection(PVector circleA, PVector startPoint, PVector endPoint) {
		float[] rtn = new float[2]; // 0 start, 1 end

		try {
			// Start
			rtn[0] = PApplet.atan2(endPoint.y - circleA.y, endPoint.x - circleA.x);

			// End angleBetweenCenters(circleA, circleB) -
			rtn[1] = PApplet.atan2(startPoint.y - circleA.y, startPoint.x - circleA.x);

			if (rtn[0] <= 0)
				rtn[0] += PApplet.TWO_PI;

			if (rtn[1] <= 0)
				rtn[1] += PApplet.TWO_PI;
			// System.out.println(circleA.id + " start: " + rtn[0] + " end: " + rtn[1]);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rtn;
	}

	/**
	 * Gets the chord between to intersection circles Implementation of
	 * http://mathworld.wolfram.com/Circle-CircleIntersection.html
	 * 
	 * @param circleA
	 *            first circle
	 * @param circleB
	 *            second circle
	 * @return length of chord
	 */
	public static float findIntersectionChord(PVector circleA, PVector circleB) {

		if (validateIntersection(circleA, circleB)) {
			float gapD = PApplet.sqrt(PApplet.pow(circleA.x - circleB.x, 2) + PApplet.pow(circleA.y - circleB.y, 2));
			float term1 = -gapD + circleB.z - circleA.z;
			float term2 = -gapD - circleB.z + circleA.z;
			float term3 = -gapD + circleB.z + circleA.z;
			float term4 = gapD + circleB.z + circleA.z;
			return (1 / gapD) * PApplet.sqrt(term1 * term2 * term3 * term4);
		} else
			return 0;
	}

	public static float angleBetweenCenters(PVector circleA, PVector circleB) {
		return PApplet.atan2(circleB.y - circleA.y, circleB.x - circleA.x);
	}

	/**
	 * Used to determine the midpoint between to intersecting circles. This midpoint
	 * is used to set the relative angles of nodes to this centroid.
	 * 
	 * @param circleA
	 * @param circleB
	 * @return
	 */
	public static PVector getMidIntersection(PVector circleA, PVector circleB) {
		// find the distance to midpoint
		float d = circleA.z - ((circleB.z + circleA.z) - PApplet.dist(circleA.x, circleA.y, circleB.x, circleB.y)) / 2;

		// find the angle between centers
		float angle = PApplet.atan2(circleA.y - circleB.y, circleA.x - circleB.x);

		// find the coordinates of midpoint on the direction of the angle
		float x = circleA.x - PApplet.cos(angle) * d;
		float y = circleA.y - PApplet.sin(angle) * d;

		// return the coordinates
		return new PVector(x, y);
	}
}
