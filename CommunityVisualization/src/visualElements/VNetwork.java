package visualElements;

import processing.core.*;

import java.util.ArrayList;
import java.util.Collections;

import graphElements.*;

public class VNetwork {
	private Graph graph;
	private ArrayList<VNode> VNodes;
	private ArrayList<VEdge> VEdges;

	public VNetwork(Graph graph) {
		this.graph = graph;
		VNodes = new ArrayList<VNode>();
		VEdges = new ArrayList<VEdge>();
	}

	// Sorters
	public void sortInDegree() {
		Collections.sort(graph.getVertices(), new InDegreeComparator());
	}

	public void sortOutDegree() {
		Collections.sort(graph.getVertices(), new OutDegreeComparator());
	}

	public void sortDegree() {
		Collections.sort(graph.getVertices(), new DegreeComparator());
	}

	/**
	 * Assigns coordinates to each VNode
	 */
	public void linearLayout(PApplet app, PVector orig, PVector end) {
		float dist = orig.dist(end);
		float xStep = (float) dist / (graph.getVertices().size());

		for (int i = 0; i < graph.getVertices().size(); i++) {
			Node n = graph.getVertices().get(i);
			VNode tmp = new VNode(app,n,0,0,0);
			tmp.setX(orig.x + xStep + (xStep * i));
			tmp.setY(orig.y);
			VNodes.add(tmp);
		}
		for (Edge e : graph.getEdges()) {
			VEdge tmp = new VEdge(e);
			tmp.layout(VNodes, graph.getVertices());
			tmp.makeBezier();
			VEdges.add(tmp);
		}
	}

	public void circularLayout(PApplet app, PVector center, float radius) {
		float step = PConstants.TWO_PI / (graph.getVertices().size());
		System.out.println(step);

		for (int i = 0; i < graph.getVertices().size(); i++) {
			Node n = graph.getVertices().get(i);
			VNode tmp = new VNode(app,n,0,0,0);
			tmp.setX((float) (center.x + (Math.cos(step * i) * radius)));
			tmp.setY((float) (center.y + (Math.sin(step * i) * radius)));
			VNodes.add(tmp);
		}
		for (Edge e : graph.getEdges()) {
			VEdge tmp = new VEdge(e);
			tmp.layout(VNodes, graph.getVertices());
			tmp.makeBezier();
			VEdges.add(tmp);
		}

	}
	
	public void setNodeXY(int index, PVector pos){
		VNodes.get(index).setX(pos.x);
		VNodes.get(index).setY(pos.y);
	}

	public void show(PApplet app, boolean nodes, boolean edges) {
		if (edges) {
			for (VEdge e : VEdges) {
				e.show(app);
			}
		}
		if (nodes) {
			for (VNode n : VNodes) {
				n.setDiam(n.getVertex().getOutDegree() + 3);
				n.show();
			}
		}
	}

	// getters and setters
	public Graph getGraph() {
		return graph;
	}
	
	public int size(){
		return graph.size();
	}

}
