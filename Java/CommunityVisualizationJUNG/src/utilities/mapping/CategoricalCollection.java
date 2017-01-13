package utilities.mapping;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import graphElements.Edge;
import graphElements.GraphElement;
import graphElements.Node;

/**
 * This collection stores subsets of categorical values as strings. The
 * structure is a TreeMap whose key is a String and value is a TreeSet that
 * accepts only Strings.
 * 
 * @author jsalam
 *
 */
public class CategoricalCollection {
	private TreeMap<String, TreeSet<String>> attributes;

	public CategoricalCollection() {
		attributes = new TreeMap<String, TreeSet<String>>();
	}

	public void initialize(GraphElement graphElement) {

		// Go over all the attributes of this edge
		for (int i = 0; i < graphElement.getAttributeKeys().length; i++) {
			TreeSet<String> valueSet = new TreeSet<String>();
			// For each attribute key get its value
			String key = (String) graphElement.getAttributeKeys()[i];
			// set the attribute as TreeSet in the attribute collections
			Object value = graphElement.getAttribute(key);
			if (isCategorical(value)) {
				String stringValue = graphElement.getStringAttribute(key);
				if (!stringValue.equals("void")) {
					valueSet.add(graphElement.getStringAttribute(key));
					attributes.put(key, valueSet);
				}
			}
		}
	}

	private boolean isCategorical(Object value) {
		if (value instanceof String) {
			return true;
		}
		return false;
	}

	public void addValue(String key, String value) {
		if (isCategorical(value)) {
			// Get the corresponding treeSet
			attributes.get(key).add(value);
		}
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

	public int getSize() {
		return attributes.size();
	}

	public void printAttributes() {
		System.out.println(this.getClass().getName() + " printAttributes():");
		Set<String> s = attributes.keySet();
		for (String keyName : s) {
			System.out.println("   Key: " + keyName + ", Value: " + attributes.get(keyName));
		}
	}
}
