package graphElements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Abstract class that handles the attribute collections of nodes and edges
 * @author jsalam
 *
 */
abstract class GraphElement implements Serializable {
	private HashMap<String, Object> attributes;

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

	public String getAttribute(String key, String rtn) {
		try {
			rtn = (String) attributes.get(key);
		} catch (Exception e) {
			System.out.println(
					this.getClass().getName() + " Attribute named: " + key + " couldn't be casted as String");
		}
		return rtn;
	}

	public float getAttribute(String key, Float rtn) {
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
			System.out.println(this.getClass().getName() + " Attribute named: " + key
					+ " couldn't be casted as float. Attribute size:" + attributes.size());
		}
		return rtn;
	}

	public int getAttribute(String key, Integer rtn) {
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
			System.out.println(this.getClass().getName() + " Attribute named: " + key
					+ " couldn't be casted as int. Attribute size:" + attributes.size());
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
