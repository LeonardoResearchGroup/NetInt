package graphElements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Abstract class that handles the attribute collections of nodes and edges
 * 
 * @author jsalam
 *
 */
public abstract class GraphElement implements Serializable {
	protected HashMap<String, Object> attributes;

	public GraphElement() {
		attributes = new HashMap<String, Object>();
	}

	// *** Getters
	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public String getStringAttribute(String key) {
		String rtn = "void";
		try {
			rtn = (String) attributes.get(key);
		} catch (Exception e) {
			System.out
					.println(this.getClass().getName() + " Attribute named: " + key + " couldn't be casted as String");
		}
		return rtn;
	}

	public float getFloatAttribute(String key) {
		Float rtn = Float.NEGATIVE_INFINITY;
		try {
			// If Double
			if (attributes.get(key) instanceof Double) {
				Double rtnObj = (Double) attributes.get(key);
				rtn = rtnObj.floatValue();
				// If Integer
			} else if (attributes.get(key) instanceof Integer) {
				Integer rtnObj = (Integer) attributes.get(key);
				rtn = rtnObj.floatValue();
				// If float
			} else {
				rtn = (float) attributes.get(key);
			}
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + " Attribute key: " + key + " value: "
					+ attributes.get(key).toString() + " couldn't be casted as Float");
		}
		return rtn;
	}

	public int getIntegerAttribute(String key) {
		Integer rtn = Integer.MIN_VALUE;
		try {
			// If Double
			if (attributes.get(key) instanceof Double) {
				Double rtnObj = (Double) attributes.get(key);
				rtn = rtnObj.intValue();
				// If Float
			} else if (attributes.get(key) instanceof Float) {
				Float rtnObj = (Float) attributes.get(key);
				rtn = rtnObj.intValue();
				// If Integer
			} else {
				rtn = (Integer) attributes.get(key);
			}
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + " Attribute named: " + key + " couldn't be casted as int");
		}
		return rtn;
	}

	public int getAttributeSize() {
		return attributes.size();
	}

	public Object[] getAttributeKeys() {
		Set<String> keys = attributes.keySet();
		return keys.toArray();
	}

	public void printAttributes() {
		System.out.println("GRAPH ELEMENT> printAttributes():");
		Set<String> s = attributes.keySet();
		for (String keyName : s) {
			System.out.println("   Key: " + keyName + ", Value: " + attributes.get(keyName));
		}
	}

	// *** Setters
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
}
