package graphElements;

import java.io.Serializable;
import java.util.HashMap;

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
//			System.out.println("Node Attribute couldn't be casted att");
		}
		return rtn;
	}

	public float getAttribute(String key, Float rtn) {
		try {
			rtn = (Float) attributes.get(key);
		} catch (Exception e) {
//			System.out.println("Node Attribute couldn't be casted att");
		}
		return rtn;
	}

	public int getAttribute(String key, Integer rtn) {
		try {
			rtn = (Integer) attributes.get(key);
		} catch (Exception e) {
//			System.out.println("Node Attribute couldn't be casted att");
		}
		return rtn;
	}

	public String getName() {
		return (String) attributes.get("label");
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
