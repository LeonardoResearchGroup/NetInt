package comparators;

import java.util.Comparator;
import graphElements.Node;

public class OutDegreeComparator implements Comparator <Node> {
	
	int key;
	
	public void setKey(int key){
		this.key = key;
	}
		public int compare(Node a, Node b) {
			return a.getOutDegree(key) - b.getOutDegree(key);
		}
}
