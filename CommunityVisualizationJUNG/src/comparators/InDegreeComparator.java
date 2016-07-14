package comparators;

import java.util.Comparator;
import graphElements.Node;

public class InDegreeComparator implements Comparator<Node> {

	int key;
	
	public void setKey(int key){
		this.key = key;
	}
	@Override
	public int compare(Node a, Node b) {
		return a.getInDegree(key) - b.getInDegree(key);
	}

}
