package visualElements;

import java.util.Comparator;
import graphElements.Node;

public class DegreeComparator implements Comparator<Node> {

	@Override
	public int compare(Node a, Node b) {
		return a.getDegree() - b.getDegree();
	}
}
