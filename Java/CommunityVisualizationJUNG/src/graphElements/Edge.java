package graphElements;

import java.io.Serializable;

/**
 * @author jsalam
 *
 */
public class Edge extends GraphElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node source;
	private Node target;
	//private HashMap<String, Object> attributes;
	private boolean directed;
	private boolean loop;

	public Edge(Node source, Node target, boolean directed) {
		super();
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
		return absoluetAttributes.get(key);
	}

	public String getAttribute(String key, String rtn) {
		try {
			rtn = (String) absoluetAttributes.get(key);
		} catch (Exception e) {
//			System.out.println("Node Attribute couldn't be casted att");
		}
		return rtn;
	}

	public float getAttribute(String key, Float rtn) {
		try {
			rtn = (Float) absoluetAttributes.get(key);
		} catch (Exception e) {
//			System.out.println("Node Attribute couldn't be casted att");
		}
		return rtn;
	}

	public int getAttribute(String key, Integer rtn) {
		try {
			rtn = (Integer) absoluetAttributes.get(key);
		} catch (Exception e) {
//			System.out.println("Node Attribute couldn't be casted att");
		}
		return rtn;
	}

	public String getName() {
		return (String) absoluetAttributes.get("label");
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

	public boolean isLoop() {
		return loop;
	}

	public boolean equals(Object obj) {
		Edge edge = (Edge) obj;
		boolean sourceIsEqual = edge.getSource().equals(this.getSource());
		boolean targetIsEqual = edge.getTarget().equals(this.getTarget());
		return sourceIsEqual && targetIsEqual;
	}

}
