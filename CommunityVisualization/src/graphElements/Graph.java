package graphElements;

import java.util.ArrayList;

public class Graph {

	protected ArrayList<Node> nodes;
	protected ArrayList<Edge> edges;

	// Constructors
	public Graph() {

	}
	
	public Graph(ArrayList<Node> vertices, ArrayList<Edge> edges) {
		this.nodes = vertices;
		this.edges = edges;
		// Set Degree
		setDegree();
	}

	// Degree
	private void setInDegree() {
		for (Node n : nodes) {
			setNodeInDegree(n);
		}
	}

	private void setOutDegree() {
		for (Node n : nodes) {
			setNodeOutDegree(n);
		}
	}

	private void setDegree() {
		for (Node n : nodes) {
			setNodeDegree(n);
		}
	}

	private void setNodeInDegree(Node n) {
		int count = 0;
		for (Edge e : edges) {
			if (e.getTarget().equals(n))
				count++;
		}
		n.setInDegree(count);
	}

	private void setNodeOutDegree(Node n) {
		int count = 0;
		for (Edge e : edges) {
			if (e.getSource().equals(n))
				count++;
		}
		n.setOutDegree(count);
	}

	private void setNodeDegree(Node n) {
		if (n.getInDegree() == 0) {
			setNodeInDegree(n);
		}
		if (n.getOutDegree() == 0) {
			setNodeOutDegree(n);
		}
		n.setDegree(n.getInDegree() + n.getOutDegree());
	}

	// Getters and setters
	public ArrayList<Node> getVertices() {
		return nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public ArrayList<Edge> getDirectedEdgesFrom(Node source) {
		ArrayList<Edge> edgeSubset = new ArrayList<Edge>();
		try {
			for(Edge e: edges){
				if(e.getSource().equals(source)){
					edgeSubset.add(e);
					System.out.println("Graph getDirectedEdges() :"+ e.getSource().getId() + " "+ e.getTarget().getId());
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return edgeSubset;
	}

	private void setVertices(ArrayList<Node> vertices) {
		this.nodes = vertices;
	}

	private void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	public int size() {
		return nodes.size();
	}

}
