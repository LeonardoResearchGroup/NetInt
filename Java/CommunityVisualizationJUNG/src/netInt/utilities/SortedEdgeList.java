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
package netInt.utilities;

import java.util.ArrayList;
import java.util.Collections;

import netInt.comparators.EdgeComparatorByAttribute;
import netInt.graphElements.Edge;

/**
 * Utility class made to sort nodes by one of their attributes. It does not
 * accept nodes with N.A. attributes
 * 
 * @author jsalam
 *
 */
public class SortedEdgeList {

	private static ArrayList<Edge> edgeList;
	private static float[] percentileValues;

	private static void makeSortedEdgeList(String attributeName) {
		// Get the node List
		edgeList = new ArrayList<Edge>(GraphLoader.theGraph.getEdges());

		// Sort the list with the comparator
		Collections.sort(edgeList, new EdgeComparatorByAttribute(attributeName));
	}

	/**
	 * Sort a node list by the parameterized attribute
	 * 
	 * @param attributeName
	 *            the attribute to sort the list
	 * @return sorted list of nodes
	 */
	public static ArrayList<Edge> getSortedEdgeList(String attributeName) {
		makeSortedEdgeList(attributeName);
		return edgeList;

	}

	/**
	 * This method sorts
	 * 
	 * @param numberOfPercentiles
	 *            Usually 10
	 * @param attributeName
	 *            name of graph element attribute
	 * @return array of percentile values
	 */
	public static float[] getPercentileValues(int numberOfPercentiles, String attributeName) {

		// Sort the list
		makeSortedEdgeList(attributeName);

		// Store the percentiles
		percentileValues = calculatePercentiles(numberOfPercentiles, attributeName);

		return percentileValues;
	}

	private static float[] calculatePercentiles(int p, String attributeName) {
		float[] vals = new float[p];

		if (p >= 0 && p <= 100) {
			for (int i = p; i <= 100; i += p) {

				// Get percentile range
				double range = edgeList.size() / 100f;
				// Get rank index
				int rank = (int) (i * range);

				if (rank > edgeList.size() - 1) {
					rank = edgeList.size() - 1;
				}

				// get Value
				if (edgeList.get((int) rank).getAttributes().containsKey(attributeName))
					vals[(i / p) - 1] = edgeList.get((int) rank).getFloatAttribute(attributeName);

			}
		} else {
			vals = new float[1];
			if (edgeList.get((int) -1).getAttributes().containsKey(attributeName))
				vals[0] = edgeList.get(edgeList.size() - 1).getFloatAttribute(attributeName);
		}

		return vals;
	}
}
