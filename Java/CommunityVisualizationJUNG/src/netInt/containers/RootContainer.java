/*******************************************************************************
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package netInt.containers;

import java.awt.Dimension;

import edu.uci.ics.jung.graph.Graph;
import netInt.graphElements.*;

public class RootContainer extends Container {

	/**
	 * Constructor to be used with instances of edu.uci.ics.jung.graph
	 * 
	 * @param graph
	 *            The graph
	 * @param kindOfLayout
	 *            Integer defining the kind of layout
	 * @param dimension
	 *            The Dimension of the component that contain the visualElements
	 */
	public RootContainer(Graph<Node, Edge> graph, int kindOfLayout, Dimension dimension) {
		super(graph);
		this.currentLayout = kindOfLayout;
		setDimension(dimension);
	}
}
