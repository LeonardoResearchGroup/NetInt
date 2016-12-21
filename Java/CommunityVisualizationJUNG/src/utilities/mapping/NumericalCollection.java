package utilities.mapping;

import java.util.Set;
import java.util.TreeMap;

import graphElements.GraphElement;

/**
 * This collection stores pairs of attribute names and associated float values.
 * The structure is a TreeMap whose key is a String and value is a Float.
 * 
 * @author jsalam
 *
 */
public class NumericalCollection {
	private TreeMap<String, Float> attributes;

	public NumericalCollection() {
		attributes = new TreeMap<String, Float>();
	}

	public void initialize(GraphElement graphElement) {
		// Go over all the attributes of this element
		for (int i = 0; i < graphElement.getAttributeKeys().length; i++) {
			// For each attribute key get its value
			String key = (String) graphElement.getAttributeKeys()[i];
			// set the attribute as Float in the attribute collections
			Object value = graphElement.getAttribute(key);
			if (isNumerical(value)) {
				Float valueFloat = graphElement.getFloatAttribute(key);
				if (valueFloat != Float.NEGATIVE_INFINITY) {
					attributes.put(key, graphElement.getFloatAttribute(key));
				}
			}
		}
	}

	private boolean isNumerical(Object value) {
		if (value instanceof Double) {
			return true;
		} else if (value instanceof Integer) {
			return true;
		} else if (value instanceof Float) {
			return true;
		}
		return false;
	}

	public void addLowerValue(String key, Float value) {
		if (attributes.get(key) > value && isNumerical(value)) {
			attributes.put(key, value);
		}
	}

	public void addHigherValue(String key, Float value) {
		if (attributes.get(key) < value && isNumerical(value)) {
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
