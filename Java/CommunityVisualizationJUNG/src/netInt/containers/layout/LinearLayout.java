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
package netInt.containers.layout;

import java.util.Iterator;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import processing.core.PVector;

public class LinearLayout extends AbstractLayout<Node, Edge> {
	float margin = 50;

	public LinearLayout(Graph<Node, Edge> graph) {
		super(graph);
	}

	/**
	 * Assigns coordinates to each Node on an horizontal axis. The length
	 * of the axis is the width of the Dimension minus 2* margin. Center is at
	 * (0,0)
	 * 
	 * @param margin
	 *            the gap between the Applet margin and each extreme of the axis
	 * @param visualElements
	 *            list of VisualAtom
	 */
	public void linearLayout() {
		
		// size is the Dimension size
		float dist = (float) (size.getWidth() - (2 * margin));
		
		// graph is the passed graph in the construction time
		float xStep = (float) dist / (graph.getVertexCount() - 1);
		
		PVector left = new PVector(dist / -2, 0);
		
		int count = 0;

		// Organize nodes on a line
		Iterator<Node> itr = graph.getVertices().iterator();
//		while (itr.hasNext()) {
//			Node tmp = itr.next();
//			tmp.setX(left.x + (xStep * count));
//			tmp.setY(left.y);
//			count++;
//		}
	}

	@Override
	public void initialize() {
		// DO HERE THE ALLOCATION OF COORDINATES FOR EACH NODE
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}