package graphElements;

import java.io.Serializable;

import visualElements.VEdge;

/**
 * @author jsalam
 *
 */
public class Edge implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node source;
	private Node target;
	private boolean directed;
	private float weight;
	private String name;
	private boolean loop;
	private int ID;
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

	public float getWeight() {
		return weight;
	}

	public int getFrequency() {
		return frequency;
	}
	public String getName() {
		return name;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setFrequency(int freq) {
		this.frequency = freq;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setID(int property) {
		this.ID = property;
	}

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
