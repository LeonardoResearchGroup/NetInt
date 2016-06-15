package graphElements;

/**
 * @author jsalam
 *
 */
public class Edge {

	private Node Source;
	private Node Target;
	private boolean directed;
	private float weight;
	private String name;
	private boolean loop;

	public Edge() {

	}

	public Edge(Node source, Node target, boolean directed) {
		setSource(source);
		setTarget(target);
		setDirected(directed);
		if (Source.equals(Target))
			loop = true;
			
	}

	// **** Getters and Setters

	public Node getSource() {
		return Source;
	}

	public Node getTarget() {
		return Target;
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
		Source = source;
	}

	public void setTarget(Node target) {
		Target = target;
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

}
