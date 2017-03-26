/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 *
 * It makes extensive use of free libraries such as Processing, Jung, ControlP5, JOGL, 
 * Tinkerpop and many others. For details see the copyrights folder. 
 *
 * Contributors:
 * 	Juan Salamanca, Cesar Loaiza, Luis Felipe Rivera, Javier Diaz
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *
 * version alpha
 *******************************************************************************/
package netInt.containers;

import java.util.ArrayList;
import java.util.Iterator;

import netInt.visualElements.primitives.VisualAtom;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class CircularArrangement extends Arrangement {
	public PApplet app;

	public CircularArrangement(PApplet app, String name) {
		super();
		this.app = app;
	}

	public void layout(PVector center, float radius, ArrayList<VisualAtom> visualElements) {
		// clear previous layout
		clearLayout(visualElements);
		// Organize nodes on a circle
		float step = PConstants.TWO_PI / visualElements.size();
		Iterator<VisualAtom> itrVNode = visualElements.iterator();
		int count = 0;
		while (itrVNode.hasNext()) {
			VisualAtom tmp = itrVNode.next();
			// calculated the angle rotated to the top quadrant
			float angle = calcAngle(step, count) - PConstants.HALF_PI;
			// get XY
			PVector XY = getXY(angle);
			// multiply by radius
			XY.mult(radius);
			// center XY
			XY.add(center);
			// set new XY
			tmp.getPos().set(XY);
			count++;
		}
	}

	private PVector getXY(float angle) {
		PVector rtn = new PVector(PApplet.cos(angle), PApplet.sin(angle));
		return rtn;
	}

	public float calcAngle(float step, int index) {
		float rtnAngle = (step * index);
		return rtnAngle;
	}
}
