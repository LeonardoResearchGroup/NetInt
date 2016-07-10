package utilities.visualArrangements;

import processing.core.*;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.interactive.VisualAtom;

import java.awt.Dimension;
import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import graphElements.*;

/**
 * This class contains two collections, one for the visualNodes and one for the
 * visualEdges.
 * 
 * @author jsalam
 * 
 */
public class Container {
	private Graph<Node, Edge> graph;
	private ArrayList<VNode> vNodes;
	private ArrayList<VEdge> vEdges;
	private ArrayList<Arrangement> arrangements;
	private String name = "n-n";
	public PApplet app;

	public AbstractLayout<Node, Edge> layout;

	/**
	 * Constructor to be used with instances of edu.uci.ics.jung.graph
	 * 
	 * @param app
	 * @param graph
	 *            The graph
	 * @param kindOfLayout
	 *            Integer defining the kind of layout
	 * @param dimension
	 *            The Dimension of the component that contain the visualElements
	 */
	public Container(PApplet app, Graph<Node, Edge> graph, int kindOfLayout, Dimension dimension) {
		this.graph = graph;
		this.app = app;

		switch (kindOfLayout) {
		// Circular layout
		case (0):
			CircleLayout<Node, Edge> circle = new CircleLayout<Node, Edge>(graph);
			circle.setSize(dimension);
			circle.setRadius(100);
			layout = circle;
			break;
		// SpringLayout
		case (1):
			SpringLayout<Node, Edge> spring = new SpringLayout<Node, Edge>(graph);
			spring.setSize(dimension);
			break;
		}

		// VFactories
		vNodes = visualNodeFactory();
		vEdges = visualEdgeFactory();
	}

	// *** Visual Nodes factory (For rootGraph)
	private ArrayList<VNode> visualNodeFactory() {
		ArrayList<VNode> theNodes = new ArrayList<VNode>();
		// For translation to canvas origin
		float xDimensionCenter = 0;
		float yDimensionCenter = 0;
//		float xDimensionCenter = (float) layout.getSize().getWidth()/2;
//		float yDimensionCenter = (float) layout.getSize().getHeight()/2;
		// Instantiate vNodes
		for (Node n : layout.getGraph().getVertices()) {
			VNode tmp = new VNode(app, n, (float) layout.getX(n) - xDimensionCenter,
					(float) layout.getY(n) - yDimensionCenter, 10);
			theNodes.add(tmp);
		}
		return theNodes;
	}

	// *** Visual Edges factory (For rootGraph)
	private ArrayList<VEdge> visualEdgeFactory() {
		ArrayList<VEdge> theEdges = new ArrayList<VEdge>();
		for (Edge e : graph.getEdges()) {
			VEdge vEdge = new VEdge(e);
			vEdge.setSourceAndTarget(vNodes);
			vEdge.makeBezier();
			theEdges.add(vEdge);
		}
		return theEdges;
	}

	// *** Regenerates Visual Nodes relative to a given position

	
	// *** Nodes And Edges retriever (For SubGraphs)
	/**
	 * Get the visual elements (visualElements package) associated to the
	 * SubGraph nodes and edges
	 * 
	 * @param container
	 */
	public void retrieveVisualElements(Container container) {

		// For each node of subGraph
		for (Node n : graph.getVertices()) {

			// Look for the corresponding VNode in the collection of VAtoms
			for (VisualAtom vAtm : container.getVNodes()) {

				// Get only the visualAtoms that are visual Nodes
				if (VNode.class.isInstance(vAtm)) {
					VNode vN = (VNode) vAtm;

					// If the current node of the subGraph matches the node of
					// the visual node retrieved from the collection of visual
					// atoms
					if (n.equals(vN.getNode())) {
						// Add the VNode to the collection of vAtoms of this
						// container
						vNodes.add(vN);
						// Look for all the edges of that VNode and add them all
						// to the collection of vEdges of this container
						vEdgeRetriever(vN, container.getVEdges());
					}
				}
			}
		}
	}

	// *** Edges retriever (For SubGraphs)
	private void vEdgeRetriever(VNode vNode, ArrayList<VEdge> rootEdgeList) {
		for (VEdge vEdg : rootEdgeList) {
			// Check if the VNode source matches any of the VEdge sources in the
			// rootGraph
			if (vEdg.getSource().equals(vNode))
				vEdges.add(vEdg);
		}
	}

	/**
	 * Clears the ArrayList of VNodes and VEdges and recreates all the VNodes
	 * and VEdges. It is used to update the positions after invoking a
	 * comparator. Sort methods invoke updateNetwork() by default
	 */
	public void remakeVisualElements() {
		vNodes.clear();
		vEdges.clear();
		vNodes = visualNodeFactory();
		vEdges = visualEdgeFactory();
	}

	public void setArrangement(Arrangement arg) {
		arrangements.add(arg);
	}

	// *** Getters and setters
	public Graph<Node, Edge> getGraph() {
		return graph;
	}

	public int size() {
		return graph.getVertexCount();
	}

	public ArrayList<VNode> getVNodes() {
		return vNodes;
	}

	public ArrayList<VEdge> getVEdges() {
		return vEdges;
	}

	// show
	public void showVNode() {
		for (VisualAtom vA : vNodes) {
			VNode n = (VNode) vA;
			n.setDiam(n.getNode().getOutDegree() + 5);
			n.show(true, true);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<VEdge> getvEdges() {
		return vEdges;
	}

}
