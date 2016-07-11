package graphElements;

/**
 * @author jsalam
 *
 */
public class Edge {

	private Node source;
	private Node target;
	private boolean directed;
	private float weight;
	private String name;
	private boolean loop;
	private int ID;
	// Observer
	private int mark;

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

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	//
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

}
