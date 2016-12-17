package graphElements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import visualElements.VEdge;

/**
 * @author jsalam
 *
 */
public class Edge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node source;
	private Node target;
	private HashMap<String, Object> attributes;
	private boolean directed;
	private boolean loop;
	/**
	 * @deprecated
	 */
	private float weight;
	/**
	 * @deprecated
	 */
	private String name;
	/**
	 * @deprecated
	 */
	private int ID;
	/**
	 * @deprecated
	 */
	private int frequency;

	public Edge(Node source, Node target, boolean directed) {
		this.source = source;
		this.target = target;
		this.directed = directed;
		if (source.equals(target))
			loop = true;
		attributes = new HashMap<String, Object>();
	}

	public Edge() {

	}

	// **** Getters and Setters

	public Node getSource() {
		return source;
	}

	public Node getTarget() {
		return target;
	}

	public boolean isDirected() {
		return directed;
	}

	// *** Getters

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public String getAttribute(String key, String rtn) {
		try {
			rtn = (String) attributes.get(key);
		} catch (Exception e) {
			System.out.println("Edge Attribute named:" + key + " couldn't be casted as String");
		}
		return rtn;
	}

	public float getAttribute(String key, Float rtn) {
		try {
			if (attributes.get(key) instanceof Double) {
				Double rtnObj = (Double) attributes.get(key);
				rtn = rtnObj.floatValue();
			} else {
				rtn = (Float) attributes.get(key);
			}
		} catch (Exception e) {
			System.out.println("EDGE > Edge Attribute named:" + key + " couldn't be casted as float. Attribute size:"
					+ attributes.size());
			System.out.println("     > source: " + source.getName() + " target: " + target.getName());
		}
		return rtn;
	}

	public int getAttribute(String key, Integer rtn) {
		try {
			if (attributes.get(key) instanceof Double) {
				Double rtnObj = (Double) attributes.get(key);
				rtn = rtnObj.intValue();
			} else {
				rtn = (Integer) attributes.get(key);
			}
		} catch (Exception e) {
			System.out.println("EDGE > Edge Attribute named:" + key + " couldn't be casted as int. Attribute size:"
					+ attributes.size());
			System.out.println("     > source: " + source.getName() + " target: " + target.getName());
		}
		return rtn;
	}

	public String getName() {
		return (String) attributes.get("label");
	}

	public int getAttributeSize() {
		return attributes.size();
	}

	public Object[] getAttributeKeys() {
		Set<String> keys = attributes.keySet();
		return keys.toArray();
	}

	public void printAttributes() {
		System.out.println("EDGE> printAttributes():");
		Set<String> s = attributes.keySet();
		for (String keyName : s) {
			System.out.println("   Key: " + keyName + ", Value: " + attributes.get(keyName));
		}
	}

	// *** Setters
	public void setSource(Node source) {
		this.source = source;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	/**
	 * @deprecated
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @deprecated
	 */
	public void setFrequency(int freq) {
		this.frequency = freq;
	}

	/**
	 * @deprecated
	 */
	public void setName(String name) {
		this.name = name;
	}

	public boolean isLoop() {
		return loop;
	}

	/**
	 * @deprecated
	 */
	public void setID(int property) {
		this.ID = property;
	}

	/**
	 * @deprecated
	 */
	public int getID() {
		return ID;
	}

	public boolean equals(Object obj) {
		Edge edge = (Edge) obj;
		boolean sourceIsEqual = edge.getSource().equals(this.getSource());
		boolean targetIsEqual = edge.getTarget().equals(this.getTarget());
		return sourceIsEqual && targetIsEqual;
	}

}
