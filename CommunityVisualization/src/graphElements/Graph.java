package graphElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import visualElements.VCommunity;

public class Graph {

	protected ArrayList<Node> nodes;
	protected ArrayList<Edge> edges;
	private int ID;
	private String name;

	// Constructors
	public Graph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}

	public Graph(ArrayList<Node> vertices, ArrayList<Edge> edges) {
		this.nodes = vertices;
		this.edges = edges;
		// Set Degree
		setDegree();
	}

	// Graphs of Communities
	public void addNode(Object obj) {
		if (obj instanceof Node) {
			nodes.add((Node) obj);
		} else if (obj instanceof VCommunity) {
			VCommunity tmpCommunity = (VCommunity) obj;
			Node tmpNode = new Node(tmpCommunity.getID());
			nodes.add(tmpNode);
			tmpCommunity.setNode(tmpNode);
			// Here additional method for edge compression are needed
		}
	}

	public void addEdge(Edge edge) {
		// this might result in several edges from and to the same nodes
		edges.add(edge);
	}

	// Degree
	private void setDegree() {
		for (Node n : nodes) {
			setNodeDegree(n);
		}
	}

	private void setNodeInDegree(Node n) {
		int count = 0;
		for (Edge e : edges) {
			if (e.getTarget().equals(n)) {
				count++;
			}
		}
		n.setInDegree(count);
	}

	private void setNodeOutDegree(Node n) {
		int count = 0;
		for (Edge e : edges) {
			if (e.getSource().equals(n)) {
				count++;
			}
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

	// Sorters
	public void sort(Comparator<Node> comp) {
		Collections.sort(getNodes(), comp);
	}

	// Getters and setters
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public ArrayList<Edge> getDirectedEdgesFrom(Node source) {
		ArrayList<Edge> edgeSubset = new ArrayList<Edge>();
		try {
			for (Edge e : edges) {
				if (e.getSource().equals(source)) {
					edgeSubset.add(e);
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return edgeSubset;
	}

	public int size() {
		return nodes.size();
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Printers
	public void printNodes() {
		System.out.println("Graph>printNodes Graph: " + ID + " total Nodes: " + nodes.size());
		for (Node n : nodes) {
			String node = String.valueOf(n.getId());
			System.out.println("  Node Id: " + node);
		}
	}

	public void printEdges() {
		System.out.println("Graph>printEdges Graph: " + ID + " total Edges: " + edges.size());
		for (Edge e : edges) {
			String source = String.valueOf(e.getSource().getId());
			String target = String.valueOf(e.getTarget().getId());
			System.out.println("  Edge Source: " + source + " target: " + target);
		}
	}

	public String getBasicStats() {
		String graphSize = String.valueOf(size());
		String totalEdges = String.valueOf(getEdges().size());
		String rtn = "Total nodes: " + graphSize + " Total edges: " + totalEdges;
		return rtn;
	}
}
