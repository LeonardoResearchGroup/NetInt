package comparators;

import java.util.Comparator;
import graphElements.Node;

public class OutDegreeComparator implements Comparator <Node> {
	
		public int compare(Node a, Node b) {
			return a.getOutDegree() - b.getOutDegree();
		}
}
