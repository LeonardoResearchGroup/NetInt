package utilities.visualArrangements;

import processing.core.*;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.interactive.VisualAtom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import comparators.DegreeComparator;
import comparators.InDegreeComparator;
import comparators.OutDegreeComparator;
import graphElements.*;

public class Container {
	private Graph graph;
	private ArrayList<VisualAtom> vAtoms;
	private ArrayList<VEdge> vEdges;
	private ArrayList<Arrangement> arrangements;
	public PApplet app;

	public Container(PApplet app, Graph graph) {
		this.graph = graph;
		this.app = app;
		vAtoms = vNodeFactory(graph);
		vEdges = vEdgeFactory(graph);
		arrangements = new ArrayList<Arrangement>();
		arrangements.add(new LinearArrangement(app, "linear"));
		arrangements.add(new CircularArrangement(app, "circular"));
	}

	// *** Nodes factory
	private ArrayList<VisualAtom> vNodeFactory(Graph graph) {
		ArrayList<VisualAtom> theNodes = new ArrayList<VisualAtom>();
		for (int i = 0; i < graph.getVertices().size(); i++) {
			Node n = graph.getVertices().get(i);
			VNode tmp = new VNode(app, n, 0, 0, 0);
			theNodes.add(tmp);
		}
		return theNodes;
	}

	// *** Edges factory
	private ArrayList<VEdge> vEdgeFactory(Graph graph) {
		ArrayList<VEdge> theEdges = new ArrayList<VEdge>();
		Iterator<Edge> itr = graph.getEdges().iterator();
		while (itr.hasNext()) {
			VEdge vEdge = new VEdge(itr.next());
			vEdge.setCoordinates(vAtoms, graph.getVertices());
			vEdge.makeBezier();
			theEdges.add(vEdge);
		}
		return theEdges;
	}

	// *** Arrangements
	public void arrangeBy(String str) {
		if (str.equals("linear")) {
			for (Arrangement a : arrangements) {
				if (a.getName().equals("linear")) {
					LinearArrangement lA = (LinearArrangement) a;
					lA.linearLayout(60, vAtoms);
				}
			}
		} else if (str.equals("circular")) {
			for (Arrangement a : arrangements) {
				if (a.getName().equals("circular")) {
					CircularArrangement lA = (CircularArrangement) a;
					lA.circularLayout(containerDiameter()/2, vAtoms);
				}
			}
		}
	}

	private int containerDiameter() {
		// Calculate the community diameter
		int minCommunityDiam = 70;
		int maxCommunityDiam = 200;
		int minCommunitySize = 1;
		int maxCommunitySize = 1000;
		int diam = (int) PApplet.map(vAtoms.size(), minCommunitySize,
				maxCommunitySize, minCommunityDiam, maxCommunityDiam);
		return diam;
	}

	/**
	 * Clears the ArrayList of VNodes and recreates all the VNodes and VEdges.
	 * It is used to update the positions after invoking a comparator. Sort
	 * methods invoke updateNetwork() by default
	 */
	private void updateContainer(Graph graph) {
		vAtoms.clear();

		for (int i = 0; i < graph.getVertices().size(); i++) {
			Node n = graph.getVertices().get(i);
			VNode tmp = new VNode(app, n, 0, 0, 0);
			vAtoms.add(tmp);
		}
		for (int i = 0; i < graph.getEdges().size(); i++) {
			Edge e = graph.getEdges().get(i);
			VEdge tmp = new VEdge(e);
			tmp.setCoordinates(vAtoms, graph.getVertices());
			tmp.makeBezier();
			vEdges.add(tmp);
		}
	}

	// Sorters
	public void sortInDegree() {
		Collections.sort(graph.getVertices(), new InDegreeComparator());
		updateContainer(graph);
	}

	public void sortOutDegree() {
		Collections.sort(graph.getVertices(), new OutDegreeComparator());
		updateContainer(graph);
	}

	public void sortDegree() {
		Collections.sort(graph.getVertices(), new DegreeComparator());
		updateContainer(graph);
	}

	public void setNodeXY(int index, PVector pos) {
		vAtoms.get(index).setX(pos.x);
		vAtoms.get(index).setY(pos.y);
	}

	public void setArrangement(Arrangement arg) {
		arrangements.add(arg);
	}

	// getters and setters
	public Graph getGraph() {
		return graph;
	}

	public int size() {
		return graph.size();
	}

	public void recenter(PVector pos) {
		for (VisualAtom vA : vAtoms) {
			VNode n = (VNode) vA;
			n.pos.add(pos);
		}
	}

	// show
	public void show(PVector pos) {
		for (VisualAtom vA : vAtoms) {
			VNode n = (VNode) vA;
			n.setDiam(n.getVertex().getOutDegree() + 5);
			n.show(true, true);
		}
	}

	public void show(boolean showNodes, boolean showEdges,
			boolean networkVisible) {

		if (showNodes || networkVisible) {
			for (VisualAtom vA : vAtoms) {
				VNode n = (VNode) vA;
				n.setDiam(n.getVertex().getOutDegree() + 5);
				n.show(showNodes, networkVisible);
			}
		}
		if (showEdges || networkVisible) {
			for (VEdge e : vEdges) {
				e.show(app);
			}
		}
	}

}
