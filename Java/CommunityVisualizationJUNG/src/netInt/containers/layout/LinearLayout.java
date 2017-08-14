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

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import processing.core.PVector;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import edu.uci.ics.jung.graph.Graph;

/**
 * A {@code Layout} implementation that positions vertices equally spaced on a
 * horizontal line.
 * 
 * Adapted code from CircleLayout by Masanori Harada.
 * 
 * https://github.com/jrtom/jung/blob/master/jung-algorithms/src/main/java/edu/
 * uci/ics/jung/algorithms/layout/CircleLayout.java
 *
 * @author Juan Salamanca
 */
public class LinearLayout<V, E> extends AbstractLayout<V, E> {

	private double radius;
	private List<V> vertex_ordered_list;
	protected HashMap<V, LineVertexData> pairs = new HashMap<V,LineVertexData>();

	public LinearLayout(Graph<V, E> g) {
		super(g);
	}

	/**
	 * Sets the order of the vertices in the layout according to the ordering
	 * specified by {@code comparator}.
	 * 
	 * @param comparator
	 *            the comparator to use to order the vertices
	 */
	public void setVertexOrder(Comparator<V> comparator) {
		if (vertex_ordered_list == null)
			vertex_ordered_list = new ArrayList<V>(getGraph().getVertices());
		Collections.sort(vertex_ordered_list, comparator);
	}

	/**
	 * Sets the order of the vertices in the layout according to the ordering of
	 * {@code vertex_list}.
	 * 
	 * @param vertex_list
	 *            a list specifying the ordering of the vertices
	 */
	public void setVertexOrder(List<V> vertex_list) {
		if (!vertex_list.containsAll(getGraph().getVertices()))
			throw new IllegalArgumentException("Supplied list must include " + "all vertices of the graph");
		this.vertex_ordered_list = vertex_list;
	}

	public void reset() {
		initialize();
	}

	public void initialize() {
		Dimension d = getSize();

		if (d != null) {
			if (vertex_ordered_list == null)
				setVertexOrder(new ArrayList<V>(getGraph().getVertices()));

			double width = d.getWidth();
			double height = d.getHeight();

			System.out.println(
					this.getClass().getName() + " items in list: " + vertex_ordered_list.size() + " width: " + width);

			int i = 0;
			for (V v : vertex_ordered_list) {
				double pos = i * (width / vertex_ordered_list.size());
				
				pairs.put(v, new PVector(pos, height / 2));

				
				// Point2D coordA = apply(v);

				Point2D.Double coord = new Point2D.Double();
				coord.setLocation();

				LineVertexData data = getLineData(v);
				data.setHorizontalPos(pos);
				i++;
			}
		}
	}

	protected LineVertexData getLineData(V v) {
		return pairs.getUnchecked(v);
	}

	// *** Internal class ****
	protected static class LineVertexData {
		private double horizontalPos;

		protected double getHorizontalPos() {
			return horizontalPos;
		}

		protected void setHorizontalPos(double horizontalPos) {
			this.horizontalPos = horizontalPos;
		}

		@Override
		public String toString() {
			return "LineVertexData: pos=" + horizontalPos;
		}
	}
}