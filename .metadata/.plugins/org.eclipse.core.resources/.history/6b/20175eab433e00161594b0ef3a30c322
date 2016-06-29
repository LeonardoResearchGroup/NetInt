package utilities.visualArrangements;

import processing.core.*;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.interactive.VisualAtom;

import java.util.ArrayList;
import java.util.Comparator;

import graphElements.*;

/**
 * This class contains two collections, one for the visualNodes and one for the
 * visualEdges.
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

	/**
	 * Constructor to be used with instances of Graph. Specially intended for
	 * the rootGraph
	 * 
	 * @param app
	 * @param graph
	 *            the root graph
	 */
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

	/**
	 * Constructor to be used with instances of subGraph
	 * 
	 * @param app
	 * @param graph
	 */
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

	// *** Visual Nodes factory (For rootGraph)
	private ArrayList<VisualAtom> vNodeFactory() {
		ArrayList<VisualAtom> theNodes = new ArrayList<VisualAtom>();
		for (Node n : graph.getNodes()) {
			VNode tmp = new VNode(app, n, 0, 0, 0);
			theNodes.add(tmp);
		}
		return theNodes;
	}

	// *** Visual Edges factory (For rootGraph)
	private ArrayList<VEdge> vEdgeFactory() {
		ArrayList<VEdge> theEdges = new ArrayList<VEdge>();
		for (Edge e : graph.getEdges()) {
			VEdge vEdge = new VEdge(e);
			vEdge.setSourceAndTarget(vAtoms, graph.getNodes());
			vEdge.makeBezier();
			theEdges.add(vEdge);
		}
		return theEdges;
	}

	// *** Nodes And Edges retriever (For SubGraphs)
	/**
	 * Get the visual elements associated to the subGraph nodes and edges
	 * 
	 * @param rootContainer
	 */
	public void retrieveVisualElements(Container rootContainer) {

		// For each node of subGraph
		for (Node n : graph.getNodes()) {

			// Look for the corresponding VNode in the collection of VAtoms
			for (VisualAtom vAtm : rootContainer.getVAtoms()) {

				// Get only the visualAtoms that are visual Nodes
				if (VNode.class.isInstance(vAtm)) {
					VNode vN = (VNode) vAtm;

					// If the current node of the subGraph matches the node of
					// the visual node retrieved from the collection of visual
					// atoms
					if (n.equals(vN.getNode())) {
						// Add the VNode to the collection of vAtoms of this
						// container
						vAtoms.add(vN);
						// Look for all the edges of that VNode and add them all
						// to the collection of vEdges of this container
						vEdgeRetriever(vN, rootContainer.getVEdges());
					}
				}
			}
		}
	}

	// *** Edges retriever (For SubGraphs)
	private void vEdgeRetriever(VNode vNode, ArrayList<VEdge> rootEdgeList) {
		for (VEdge vEdg : rootEdgeList) {
			// Check if the VNode source matches any of the VEdge sources in the rootGraph
			if (vEdg.getSource().equals(vNode))
				vEdges.add(vEdg);
		}
	}

	// *** Sort container graph
	public void sort(Comparator<Node> comp){
		graph.sort(comp);
	}
	
	// *** Arrangements
	public void arrangement(String str) {
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
					lA.circularLayout(containerDiameter()/2, vAtoms);
					break;
				}
			}
		} else {
			System.out.println("Container>arrangeBy: ARRANGEMENT NOT AVAILABLE. Check spelling");
		}
	}

	private int containerDiameter() {
		// Calculate the community diameter
		int minCommunityDiam = 70;
		int maxCommunityDiam = 200;
		int minCommunitySize = 1;
		int maxCommunitySize = 1000;
		int diam = (int) PApplet.map(vAtoms.size(), minCommunitySize, maxCommunitySize, minCommunityDiam,
				maxCommunityDiam);
		return diam;
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
	public void showVNode() {
		for (VisualAtom vA : vAtoms) {
			VNode n = (VNode) vA;
			n.setDiam(n.getVertex().getOutDegree() + 5);
			n.show(true, true);
		}
	}

	public void show(boolean showNodes, boolean showEdges, boolean networkVisible) {

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
