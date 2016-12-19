package utilities.mapping;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import graphElements.Edge;
import graphElements.Node;

public class CategoricalCollection {
	private TreeMap<String, TreeSet<String>> attributes;

	public CategoricalCollection() {
		attributes = new TreeMap<String, TreeSet<String>>();
	}

	public void initialize(Object graphElement) {
		// Go over all the attributes of this edge
		TreeSet<String> valueSet = new TreeSet<String>();
		if (graphElement instanceof Edge) {
			Edge edge = (Edge) graphElement;
			for (int i = 0; i < edge.getAttributeKeys().length; i++) {
				// For each attribute key get its value
				String key = (String) edge.getAttributeKeys()[i];
				// set the attribute as Float in the attribute collections
				valueSet.add(key);
				attributes.put(key, valueSet);
			}

			// }else if (graphElement instanceof Node){
			// Node node = (Node) graphElement;
			// for (int i = 0; i < node.getAttributeKeys().length; i++) {
			// // For each attribute key get its value
			// //String key = (String) edge.getAttributeKeys()[i];
			// DO HERE WHAT IS NEDED FOR NODES
			// // set the attribute as Float in the attribute collections
			// valueSet.add(key);
			// attributes.put(key, valueSet);
			// }
			//
		}
	}

	public void addValue(String key, String value) {
		// Get the corresponding treeSet
		attributes.get(key).add(value);
	}

	public TreeSet<String> getValuesOfAttribute(String key) {
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
