package visualElements;

import processing.core.*;
import utilities.containers.Container;
import visualElements.interactive.VisualAtom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import comparators.DegreeComparator;
import comparators.InDegreeComparator;
import comparators.OutDegreeComparator;
import graphElements.*;

public class Arrangement {
	private Graph graph;
	private ArrayList<VisualAtom> VAtoms;
	private ArrayList<Container> containers;
	public PApplet app;

	public Arrangement(PApplet app, Graph graph) {
		this.graph = graph;
		this.app = app;
		VAtoms = new ArrayList<VisualAtom>();
		for (int i = 0; i < graph.getVertices().size(); i++) {
			Node n = graph.getVertices().get(i);
			VNode tmp = new VNode(app, n, 0, 0, 0);
			VAtoms.add(tmp);
		}
	}

	public Arrangement(PApplet app) {
		this.app = app;
		VAtoms = new ArrayList<VisualAtom>();
		VEdges = new ArrayList<VEdge>();
	}

	/**
	 * Clears the ArrayList of VNodes and recreates all the VNodes and VEdges.
	 * It is used to update the positions after invoking a comparator. Sort
	 * methods invoke updateNetwork() by default
	 */
	private void updateContainer(Graph graph) {
		VAtoms.clear();

		for (int i = 0; i < graph.getVertices().size(); i++) {
			Node n = graph.getVertices().get(i);
			VNode tmp = new VNode(app, n, 0, 0, 0);
			VAtoms.add(tmp);
		}
		for (int i = 0; i < graph.getEdges().size(); i++) {
			Edge e = graph.getEdges().get(i);
			VEdge tmp = new VEdge(e);
			tmp.setCoordinates(VAtoms, graph.getVertices());
			tmp.makeBezier();
			VEdges.add(tmp);
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

	public void circularLayout(PApplet app, PVector center, float radius) {

		int count = 0;
		// Organize nodes on a circle
		Iterator<VisualAtom> itrVNode = VAtoms.iterator();
		while (itrVNode.hasNext()) {
			VNode tmp = (VNode) itrVNode.next();
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
			tmp.setCoordinates(VAtoms, graph.getVertices());
			tmp.makeBezier();
		}

	}

	public void setNodeXY(int index, PVector pos) {
		VAtoms.get(index).setX(pos.x);
		VAtoms.get(index).setY(pos.y);
	}

	// app, visualizeNodes, visualizeEdges, showInvolute

	// getters and setters
	public Graph getGraph() {
		return graph;
	}

	public int size() {
		return graph.size();
	}

}
