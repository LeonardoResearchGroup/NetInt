package visualElements;

import java.util.Comparator;
import graphElements.Node;

public class InDegreeComparator implements Comparator<Node> {

	@Override
	public int compare(Node a, Node b) {
		return a.getInDegree() - b.getInDegree();
	}

}
