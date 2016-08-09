package graphElements;

import java.util.ArrayList;

public class Graph {

	private ArrayList<Node> vertices;
	private ArrayList<Edge> edges;

	public Graph(ArrayList<Node> vertices, ArrayList<Edge> edges) {
		this.vertices = vertices;
		this.edges = edges;
	}

	// Degree
	public void setInDegree() {
		for (Node n : vertices) {
			setNodeInDegree(n);
		}
	}

	public void setOutDegree() {
		for (Node n : vertices) {
			setNodeOutDegree(n);
		}
	}

	public void setDegree() {
		for (Node n : vertices) {
			setNodeDegree(n);
		}
	}

	public void setNodeInDegree(Node n) {
		int count = 0;
		for (Edge e : edges) {
			if (e.getTarget().equals(n))
				count++;
		}
		n.setInDegree(count);
	}

	public void setNodeOutDegree(Node n) {
		int count = 0;
		for (Edge e : edges) {
			if (e.getSource().equals(n))
				count++;
		}
		n.setOutDegree(count);
	}

	public void setNodeDegree(Node n) {
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
		return vertices;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setVertices(ArrayList<Node> vertices) {
		this.vertices = vertices;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	public int size() {
		return vertices.size();
	}

}
