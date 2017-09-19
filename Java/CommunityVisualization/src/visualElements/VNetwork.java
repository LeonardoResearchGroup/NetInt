package visualElements;

import processing.core.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import comparators.DegreeComparator;
import comparators.InDegreeComparator;
import comparators.OutDegreeComparator;
import graphElements.*;

public class VNetwork {
	private Graph graph;
	private ArrayList<VNode> VNodes;
	private ArrayList<VEdge> VEdges;
	public PApplet app;

	public VNetwork(PApplet app, Graph graph) {
		this.graph = graph;
		this.app = app;
		VNodes = new ArrayList<VNode>();
		VEdges = new ArrayList<VEdge>();
		for (int i = 0; i < graph.getVertices().size(); i++) {
			Node n = graph.getVertices().get(i);
			VNode tmp = new VNode(app, n, 0, 0, 0);
			VNodes.add(tmp);
		}
		for (int i = 0; i < graph.getEdges().size(); i++) {
			Edge e = graph.getEdges().get(i);
			VEdge tmp = new VEdge(e);
			tmp.layout(VNodes, graph.getVertices());
			tmp.makeBezier();
			VEdges.add(tmp);
		}
	}

	/**
	 * Clears the ArrayList of VNodes and recreates all the VNodes and VEdges.
	 * It is used to update the positions after invoking a comparator. Sort
	 * methods invoke updateNetwork() by default
	 */
	private void updateVNetwork() {
		VNodes.clear();

		for (int i = 0; i < graph.getVertices().size(); i++) {
			Node n = graph.getVertices().get(i);
			VNode tmp = new VNode(app, n, 0, 0, 0);
			VNodes.add(tmp);
		}
		for (int i = 0; i < graph.getEdges().size(); i++) {
			Edge e = graph.getEdges().get(i);
			VEdge tmp = new VEdge(e);
			tmp.layout(VNodes, graph.getVertices());
			tmp.makeBezier();
			VEdges.add(tmp);
		}
	}

	// Sorters
	public void sortInDegree() {
		Collections.sort(graph.getVertices(), new InDegreeComparator());
		updateVNetwork();
	}

	public void sortOutDegree() {
		Collections.sort(graph.getVertices(), new OutDegreeComparator());
		updateVNetwork();
	}

	public void sortDegree() {
		Collections.sort(graph.getVertices(), new DegreeComparator());
		updateVNetwork();
	}

	/**
	 * Assigns coordinates to each VNode on an horizontal axis
	 */
	public void linearLayout(PApplet app, PVector orig, PVector end) {
		int count = 0;
		float dist = orig.dist(end);
		float xStep = (float) dist / (graph.getVertices().size());

		// Organize nodes on a line
		Iterator<VNode> itrVNode = VNodes.iterator();
		while (itrVNode.hasNext()) {
			VNode tmp = itrVNode.next();
			tmp.setX(orig.x + xStep + (xStep * count));
			tmp.setY(orig.y);
			count++;
		}
		// Draw bezier curves
		Iterator<VEdge> itrVEdge = VEdges.iterator();
		while (itrVEdge.hasNext()) {
			VEdge tmp = itrVEdge.next();
			tmp.layout(VNodes, graph.getVertices());
			tmp.makeBezier();
		}
	}

	public void circularLayout(PApplet app, PVector center, float radius) {

		int count = 0;
		// Organize nodes on a circle
		Iterator<VNode> itrVNode = VNodes.iterator();
		while (itrVNode.hasNext()) {
			VNode tmp = itrVNode.next();
			tmp.setIndex(count);
			tmp.setArcSections(graph.getVertices().size());
			tmp.setNetworkRadius(radius);
			tmp.setCenter(center);
			tmp.setup();
			count++;
		}
		// Draw bezier curves
		Iterator<VEdge> itrVEdge = VEdges.iterator();
		while (itrVEdge.hasNext()) {
			VEdge tmp = itrVEdge.next();
			tmp.layout(VNodes, graph.getVertices());
			tmp.makeBezier();
		}

	}

	public void setNodeXY(int index, PVector pos) {
		VNodes.get(index).setX(pos.x);
		VNodes.get(index).setY(pos.y);
	}

	// app, visualizeNodes, visualizeEdges, showInvolute
	public void show(PApplet app, boolean showNodes, boolean showEdges, boolean networkVisible) {

		if (showEdges || networkVisible) {
			for (VEdge e : VEdges) {
				e.show(app);
			}
		}
		if (showNodes || networkVisible) {
			for (VNode n : VNodes) {
				n.setDiam(n.getVertex().getOutDegree() + 5);
				n.show(showNodes, networkVisible);
			}
		}
	}

	// getters and setters
	public Graph getGraph() {
		return graph;
	}

	public int size() {
		return graph.size();
	}

}