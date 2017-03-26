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
package netInt.containers;

import java.util.ArrayList;
import java.util.Iterator;

import netInt.visualElements.primitives.VisualAtom;
import processing.core.PApplet;
import processing.core.PVector;

public class LinearLayout extends Arrangement  {
	public PApplet app;

	public LinearLayout(PApplet app) {
		super();
		this.app = app;
	}

	/**
	 * Assigns coordinates to each VisualAtom on an horizontal axis. The length
	 * of the axis is the width of the Applet minus 2* margin. Center is at
	 * (0,0)
	 * 
	 * @param margin
	 *            the gap between the Applet margin and each extreme of the axis
	 */
	public void linearLayout(float margin, ArrayList<VisualAtom> visualElements) {
		clearLayout(visualElements);
		float dist = app.width - (2 * margin);
		float xStep = (float) dist / (visualElements.size()-1);
		PVector left = new PVector(dist/-2, 0);
		int count = 0;
		
		// Organize nodes on a line
		Iterator<VisualAtom> itr = visualElements.iterator();
		while (itr.hasNext()) {
			VisualAtom tmp = itr.next();
			tmp.setX(left.x + (xStep * count));
			tmp.setY(left.y);
			count++;
		}
	}

}