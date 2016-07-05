package utilities.visualArrangements;

import processing.core.*;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.interactive.VisualAtom;

import java.awt.Dimension;
import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.layout.Layout;
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

	public Layout<Node, Edge> layout;
	public CircleLayout<Node, Edge> circle;

	/**
	 * Constructor to be used with instances of Graph. Specially intended for
	 * the rootGraph
	 * 
	 * @param app
	 * @param graph
	 *            the root graph
	 */
	public Container(PApplet app, Graph<Node, Edge> graph, Dimension dimension) {
		this.graph = graph;
		this.app = app;
		// Layout
		layout = new CircleLayout<Node, Edge>(graph);
		layout.setSize(dimension);
		circle = (CircleLayout<Node, Edge>) layout;
		circle.setRadius(100);
		// VFactories
		vNodes = vNodeFactory();
		vEdges = vEdgeFactory();
	}

	// *** Visual Nodes factory (For rootGraph)
	private ArrayList<VNode> vNodeFactory() {
		ArrayList<VNode> theNodes = new ArrayList<VNode>();
		for (Node n : circle.getGraph().getVertices()) {
			VNode tmp = new VNode(app, n, (float) circle.getX(n), (float) circle.getY(n), 10);
			theNodes.add(tmp);
		}
		return theNodes;
	}

	// *** Visual Edges factory (For rootGraph)
	private ArrayList<VEdge> vEdgeFactory() {
		ArrayList<VEdge> theEdges = new ArrayList<VEdge>();
		for (Edge e : graph.getEdges()) {
			VEdge vEdge = new VEdge(e);
			vEdge.setSourceAndTarget(vNodes);
			vEdge.makeBezier();
			theEdges.add(vEdge);
		}
		return theEdges;
	}

	// *** Nodes And Edges retriever (For SubGraphs)
	/**
	 * Get the visual elements associated to the SubGraph nodes and edges
	 * 
	 * @param rootContainer
	 */
	public void retrieveVisualElements(Container rootContainer) {

		// For each node of subGraph
		for (Node n : graph.getVertices()) {

			// Look for the corresponding VNode in the collection of VAtoms
			for (VisualAtom vAtm : rootContainer.getVNodes()) {

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
						vEdgeRetriever(vN, rootContainer.getVEdges());
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
	public void updateContainer() {
		vNodes.clear();
		vEdges.clear();
		vNodes = vNodeFactory();
		vEdges = vEdgeFactory();
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

	// public void setVNode(VNode atom) {
	// vAtoms.add(atom);
	// if (!graph.getVertices().contains(atom)) {
	// graph.addVertex(atom.getNode());
	// }
	// }
	//
	// public void setVEdge(VEdge vEdge) {
	// vEdges.add(vEdge);
	// if (!graph.getEdges().contains(vEdge.getEdge()))
	// graph.addEdge(vEdge.getEdge());
	// }

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

	public void show(boolean showNodes, boolean showEdges, boolean networkVisible) {

		if (showNodes || networkVisible) {
			for (VisualAtom vA : vNodes) {
				VNode n = (VNode) vA;
				n.setDiam(n.getNode().getOutDegree() + 5);
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
