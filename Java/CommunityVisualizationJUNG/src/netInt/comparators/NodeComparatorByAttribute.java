package netInt.comparators;

import java.util.Comparator;

import netInt.graphElements.Node;

public class NodeComparatorByAttribute implements Comparator<Node> {
	String attributeName;

	public NodeComparatorByAttribute(String attributeName) {
		this.attributeName = attributeName;
	}

	public int compare(Node o1, Node o2) {
		try {
			float value = o1.getFloatAttribute(attributeName) - o2.getFloatAttribute(attributeName);

			if (value < 0) {
				return -1;
			} else if (value > 0) {
				return 1;
			} else if (value == 0) {
				return 0;
			} else {
				return 0;
			}
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + " " + e.toString() + " \n" + "    Attribute name - "
					+ attributeName + " - not found in nodes. CHECK COMPARATOR RESULTS");

			return 0;
		}
	}

}
