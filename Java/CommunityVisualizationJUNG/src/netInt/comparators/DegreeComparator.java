package netInt.comparators;

import java.util.Comparator;

import netInt.graphElements.Node;

public class DegreeComparator implements Comparator<Node> {
	
	int key;
	
	public void setKey(int key){
		this.key = key;
	}
	@Override
	public int compare(Node a, Node b) {
		return a.getDegree(key) - b.getDegree(key);
	}
}
