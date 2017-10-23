package netInt.utilities;

import java.util.ArrayList;
import java.util.Collections;

import netInt.comparators.NodeComparatorByAttribute;
import netInt.graphElements.Node;

/**
 * Utility class made to sort nodes by one of their attributes. It does not
 * accept nodes with N.A. attributes
 * 
 * @author jsalam
 *
 */
public class SortedNodeList {
	private static ArrayList<Node> nodeList;
	private static float[] percentileValues;

	private static void makeSortedNodeList(String attributeName) {

		// Get the node List
		nodeList = new ArrayList<Node>(GraphLoader.theGraph.getVertices());

		// Sort the list with the comparator
		Collections.sort(nodeList, new NodeComparatorByAttribute(attributeName));
	}

	/**
	 * Sort a node list by the parameterized attribute
	 * 
	 * @param attributeName
	 *            the attribute to sort the list
	 * @return sorted list of nodes
	 */
	public static ArrayList<Node> getSortedNodeList(String attributeName) {
		makeSortedNodeList(attributeName);
		return nodeList;

	}

	public static float[] getPercentileValues(int numberOfPercentiles, String attributeName) {

		// Sort the list
		makeSortedNodeList(attributeName);

		// Store the percentiles
		percentileValues = calculatePercentiles(numberOfPercentiles, attributeName);

		return percentileValues;
	}

	private static float[] calculatePercentiles(int p, String attributeName) {
		float[] vals = new float[p];

		if (p>= 0 && p <= 100) {
			for (int i = p; i <= 100; i += p) {

				// Get percentile range
				double range = nodeList.size() / 100f;
				// Get rank index
				int rank = (int) (i * range);

				if (rank > nodeList.size() - 1) {
					rank = nodeList.size() - 1;
				}
				// get Value
				vals[(i / p) - 1] = nodeList.get((int) rank).getFloatAttribute(attributeName);

			}
		} else {
			vals = new float[1];
			vals[0] = nodeList.get(nodeList.size() - 1).getFloatAttribute(attributeName);
		}
		return vals;
	}
}
