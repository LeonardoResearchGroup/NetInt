/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package netInt.utilities.geometry;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Involute {
	private boolean clockwise = true;
	private PVector anchor, ending;
	public PApplet app;
	private float radius, anchorAngle, anchorAngleStart, step;
	private float cordBase, cordLength;
	private int index;

	/**
	 * @param app
	 * @param radius
	 *            the radius of the circumference
	 * @param sections
	 *            the total number of anchors to be accommodated in the
	 *            circumference
	 * @param index
	 *            the index of the VisualAtom in the sequence around the
	 *            circumference
	 */
	public Involute(PApplet app, float radius, int sections, int index) {
		this.radius = radius;
		this.app = app;
		this.index = index;
		// this determines if the unfolding goes clockwise(true) or
		// counterclockwise (false)
		if (index > sections / 2) {
			clockwise = false;
		}

		// Calculate the step for the total number of anchors to be accommodated
		// in the total circumference.
		step = PConstants.TWO_PI / sections;

		// calculate the startAngle for that index. Save a copy in
		// AnchorAngleStart
		anchorAngle = calcAngle(index);
		// anchorAngleStart = anchorAngle;

		// calculate the cord base (never changes) and the cordLength (extended
		// cord for a given angle)
		cordBase = radius * anchorAngle;
		cordLength = radius * anchorAngle;

		// rotate angle to top quadrant
		anchorAngle = anchorAngle - PConstants.HALF_PI;
		anchorAngleStart = anchorAngle;

		// get the PVector for that anchor
		anchor = getXY(anchorAngle);
		anchor.mult(radius);

		// get the PVector for that ending
		ending = getInvoluteCoords(anchorAngle);
		ending.mult(cordLength - cordBase);
		// ending.add(anchor);
	}

	public float calcAngle(int index) {
		float angle = (step * index);
		return angle;
	}

	/**
	 * Gets the XY PVector for a given angle for a circumference of radius 1
	 * 
	 * @param angle
	 * @return
	 */
	private PVector getXY(float angle) {
		PVector rtn = new PVector(PApplet.cos(angle), PApplet.sin(angle));
		return rtn;
	}

	private PVector getInvoluteCoords(float angle) {
		PVector involute = getXY(PConstants.PI + (angle + PConstants.HALF_PI));
		return involute;
	}

	private void getPositiveAngle() {
		if (anchorAngle < -PConstants.HALF_PI) {
			anchorAngle = PConstants.PI + (PConstants.PI + anchorAngle);
		}
	}

	public boolean rotateAnchor(float limit, float increment,
			boolean rollingDown) {
		boolean completed = false;
		// If the unfolding direction is not clockwise
		if (!clockwise) {
			increment *= -1;
		}

		// If rolling up invert the increment direction
		if (!rollingDown) {
			increment *= -1;
		}

		// Convert degrees to radians. If rolling up reset the limit
		// direction to startAngle
		float incrementRadians = PApplet.radians(increment);
		float limitRadians;
		if (!rollingDown) {
			limitRadians = anchorAngleStart;
		} else {
			limitRadians = PApplet.radians(limit);
		}

		// Rotate clockwise
		if (incrementRadians > 0) {
			if (anchorAngle < limitRadians) {
				anchor.rotate(incrementRadians);
				anchorAngle = PApplet.atan2(anchor.y, anchor.x);
			} else {
				completed = true;
			}
		} else {
			// Rotate counterclockwise
			getPositiveAngle();
			if (anchorAngle > limitRadians) {
				anchor.rotate(incrementRadians);
				anchorAngle = PApplet.atan2(anchor.y, anchor.x);
			} else {
				completed = true;
			}
		}
		return completed;
	}

	public void rotateInvolute() {
		// ending.sub(anchor);
		getPositiveAngle();
		ending = getInvoluteCoords(anchorAngle);
		cordLength = radius * (anchorAngle + PConstants.HALF_PI);
		ending.mult(cordLength - cordBase);
		// ending.add(anchor);
		if (ending.y > radius) {
			ending.y = radius;
		}
	}

	/**
	 * Runs the involute from the top quadrant down to the limit.
	 * 
	 * @param limit
	 *            The angle in DEGREES that limits the displacement of nodes.
	 *            Angle 0 is the right quadrant, 90 the bottom quadrant and so
	 *            on
	 * @param increment
	 *            The displacement of each node on the circumference
	 */
	public void runInvolute(float limit, float increment, boolean unfolding) {
		if (!rotateAnchor(limit, increment, unfolding)) {
			rotateInvolute();
		}
	}

	/**
	 * Draws the involute on the canvas around the given PVector
	 * 
	 * @param center
	 */
	public void show(PVector center) {
		PVector vAnchor = PVector.add(anchor, center);
		PVector vEnding = PVector.add(ending, anchor);
		vEnding.add(center);
		app.noFill();
		app.stroke(255, 90);
		app.fill(255, 0, 0);
		app.line(center.x, center.y, vAnchor.x, vAnchor.y);
		app.line(vAnchor.x, vAnchor.y, vEnding.x, vEnding.y);
		app.ellipse(vAnchor.x, vAnchor.y, 3, 3);
		app.text(index, vEnding.x, vEnding.y);
	}

	// GETTERS & SETTERS

	public PVector getInvoluteCoords(PVector center) {
		// PVector vEnding = PVector.add(ending, center);
		PVector vEnding = PVector.add(ending, anchor);
		vEnding.add(center);
		return vEnding;
	}

	public PVector getEnding() {
		return ending;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

}
