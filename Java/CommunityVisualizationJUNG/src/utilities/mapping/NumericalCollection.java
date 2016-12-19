package utilities.mapping;

import java.util.Set;
import java.util.TreeMap;

import graphElements.Edge;
import graphElements.Node;
//import graphElements.GraphElement;

public class NumericalCollection {
	private TreeMap<String, Float> attributes;

	public NumericalCollection() {
		attributes = new TreeMap<String, Float>();
	}

	public void initialize(Object graphElement) {
		if (graphElement instanceof Edge) {
			Edge edge = (Edge) graphElement;
			// Go over all the attributes of this edge
			for (int i = 0; i < edge.getAttributeKeys().length; i++) {
				// For each attribute key get its value
				String key = (String) edge.getAttributeKeys()[i];
				// set the attribute as Float in the attribute collections
				attributes.put(key, edge.getAttribute(key, new Float(0)));
			}
		}
		if (graphElement instanceof Node) {
			Node node = (Node) graphElement;
			// Go over all the attributes of this node
			for (int i = 0; i < node.getAttributeKeys().length; i++) {
				// For each attribute key get its value
				String key = (String) node.getAttributeKeys()[i];
				// set the attribute as Float in the attribute collections
				attributes.put(key, node.getAttribute(key, new Float(0)));
			}
		}
	}

	public void addLowerValue(String key, Float value) {
		if (attributes.get(key) > value) {
			attributes.put(key, value);
		}
	}

	public void addHigherValue(String key, Float value) {
		if (attributes.get(key) < value) {
			attributes.put(key, value);
		}
	}

	public Float getValueofAttribute(String key) {
		return attributes.get(key);
	}

	public Object[] getAttributeKeys() {
		return attributes.keySet().toArray();
	}

	public boolean collectionInitialized() {
		return attributes.size() > 0;
	}
	
	public int getSize(){
		return attributes.size();
	}
	
	public void printAttributes() {
		System.out.println(this.getClass().getName() +" printAttributes():");
		Set<String> s = attributes.keySet();
		for (String keyName : s) {
			System.out.println("   Key: " + keyName + ", Value: " + attributes.get(keyName));
		}
	}
}
