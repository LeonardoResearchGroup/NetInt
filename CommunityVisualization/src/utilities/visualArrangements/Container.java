package utilities.visualArrangements;

import processing.core.*;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.interactive.VisualAtom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import comparators.DegreeComparator;
import comparators.InDegreeComparator;
import comparators.OutDegreeComparator;
import graphElements.*;

/**
 * This class contains two collections, one for the visualNodes and one for the
 * visualEdges. The contents of both collections are emptied and re-populated
 * every time the internal graph is modified, e.g., after every sort. See
 * updateContainer()
 * 
 * @author jsalam
 * 
 */
public class Container {
	private Graph graph, rootGraph;
	private ArrayList<VisualAtom> vAtoms;
	private ArrayList<VEdge> vEdges;
	private ArrayList<Arrangement> arrangements;
	public PApplet app;

	public Container(PApplet app, Graph graph) {
		this.graph = graph;
		this.app = app;
		this.rootGraph = graph;
		// If graph is the rootGraph generate all the visual elements
		if (!SubGraph.class.isInstance(graph)) {
			vAtoms = vNodeFactory();
			vEdges = vEdgeFactory();
		}
		// Graph Visual Layouts
		addVisualArrangements();
	}

	public Container(PApplet app, SubGraph graph) {
		this.graph = graph;
		this.app = app;
		// If graph is a subGraph just instantiate empty collections for visual
		// elements
		if (SubGraph.class.isInstance(graph)) {
			vAtoms = new ArrayList<VisualAtom>();
			vEdges = new ArrayList<VEdge>();
		}
		// Graph Visual Layouts
		addVisualArrangements();
	}

	/**
	 * These arrangements serve to layout the visual elements on screen
	 */
	public void addVisualArrangements() {
		arrangements = new ArrayList<Arrangement>();
		arrangements.add(new LinearArrangement(app, "linear"));
		arrangements.add(new CircularArrangement(app, "circular"));
	}

	// *** Visual Nodes factory
	private ArrayList<VisualAtom> vNodeFactory() {
		ArrayList<VisualAtom> theNodes = new ArrayList<VisualAtom>();
		for (Node n : graph.getNodes()) {
			VNode tmp = new VNode(app, n, 0, 0, 0);
			theNodes.add(tmp);
		}
		return theNodes;
	}

	// *** Visual Edges factory
	private ArrayList<VEdge> vEdgeFactory() {
		ArrayList<VEdge> theEdges = new ArrayList<VEdge>();
		System.out.println("Container> vEdgesFactory:  edges count:" + graph.getEdges().size());
		for (Edge e : graph.getEdges()) {
			VEdge vEdge = new VEdge(e);
			vEdge.setSourceAndTarget(vAtoms, graph.getNodes());
			vEdge.makeBezier();
			theEdges.add(vEdge);
		}
		return theEdges;
	}

	// *** Nodes And Edges retriever
	public void retrieveVisualElements(Container rootContainer) {
		for (Node n : graph.getNodes()) {
			for (VisualAtom vAtm : rootContainer.getVAtoms()) {
				// If a VNode
				if (VNode.class.isInstance(vAtm)) {
					VNode vN = (VNode) vAtm;
					// get the VNode for each Node
					if (n.equals(vN.getNode())) {
						vAtoms.add(vN);
						vEdgeRetriever(vN, rootContainer);
					}
				}
			}
		}
	}

	// *** Edges retriever
	private void vEdgeRetriever(VNode vNode, Container rootContainer) {
		for (VEdge vEdg : rootContainer.getVEdges()) {
			// get the VNode for each Node
			if (vEdg.getSource().equals(vNode))
				vEdges.add(vEdg);
		}
	}

	// *** Arrangements
	public void arrangeBy(String str) {
		if (str.equals("linear")) {
			for (Arrangement a : arrangements) {
				if (a.getName().equals("linear")) {
					LinearArrangement lA = (LinearArrangement) a;
					lA.linearLayout(60, vAtoms);
					break;
				}
			}
		} else if (str.equals("circular")) {
			for (Arrangement a : arrangements) {
				if (a.getName().equals("circular")) {
					CircularArrangement lA = (CircularArrangement) a;
					lA.circularLayout(containerDiameter() / 2, vAtoms);
					break;
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

	// Sort Container graph
	public void sort(Comparator<Node> comp) {
		graph.sort(comp);
		updateContainer();
	}

	/**
	 * Clears the ArrayList of VNodes and VEdges and recreates all the VNodes
	 * and VEdges. It is used to update the positions after invoking a
	 * comparator. Sort methods invoke updateNetwork() by default
	 */
	public void updateContainer() {
		vAtoms.clear();
		vEdges.clear();
		vAtoms = vNodeFactory();
		vEdges = vEdgeFactory();
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

	public int getID() {
		return graph.getID();
	}

	public ArrayList<VisualAtom> getVAtoms() {
		return vAtoms;
	}

	public ArrayList<VEdge> getVEdges() {
		return vEdges;
	}

	public Graph getRootGraph() {
		return rootGraph;
	}

	public void setRootGraph(Graph rootGraph) {
		this.rootGraph = rootGraph;
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
