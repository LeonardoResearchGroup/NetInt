/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.containers.layout;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A implementation that positions vertices equally spaced on a horizontal line.
 *
 * @author Juan Salamanca
 */
public class LinearLayout<V, E> extends AbstractLayout<V, E> {

	private List<V> vertex_ordered_list;

	public LinearLayout(Graph<V, E> g) {
		super(g);
		vertex_ordered_list = new ArrayList<V>(graph.getVertices());
	}

	public LinearLayout(Graph<V, E> g, Dimension size) {
		super(g, size);
		vertex_ordered_list = (List<V>) graph.getVertices();
		initialize();
	}

	public void setSize(Dimension size) {
		this.size = size;
		initialize();
	}

	public void setVertexOrder(Comparator<V> comparator) {
		Collections.sort(vertex_ordered_list, comparator);
	}

	public void initialize() {

		if (size != null) {

			double width = size.getWidth();
			double height = size.getHeight();

			int i = 0;

			for (V v : vertex_ordered_list) {

				float posX = (float) (i * (width / vertex_ordered_list.size()));
				float posY = (float) (height / 2);
				Point2D pos = new Point2D.Double(posX, posY);
				locations.put(v, pos);
				i++;
			}
		}
	}

	public void reset() {
		initialize();

	}
}
