package utilities.visualArrangements;

import processing.core.*;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.interactive.VisualAtom;

import java.awt.Dimension;
import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
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
	public static final int CIRCULAR = 0;
	public static final int SPRING = 1;
	private Graph<Node, Edge> graph;
	private ArrayList<VNode> vNodes;
	private ArrayList<VEdge> vEdges;
	private ArrayList<Arrangement> arrangements;
	private String name = "no name";
	public PApplet app;
	public PVector layoutCenter;

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
			layoutCenter = new PVector(0, 0);
			break;
		// SpringLayout
		case (1):
			SpringLayout<Node, Edge> spring = new SpringLayout<Node, Edge>(graph);
			spring.setSize(dimension);
			layout = spring;
			layoutCenter = new PVector((float) (layout.getSize().getWidth() / 2),
					(float) (layout.getSize().getHeight() / 2));
			break;
		}

		// VFactories
		vNodes = visualNodeFactory();
		vEdges = visualEdgeFactory();
	}

	/**
	 * Visual Nodes factory (For rootGraph)
	 * 
	 * @return
	 */
	private ArrayList<VNode> visualNodeFactory() {
		ArrayList<VNode> theNodes = new ArrayList<VNode>();
		// Instantiate vNodes
		for (Node n : layout.getGraph().getVertices()) {
			VNode tmp = new VNode(app, n, (float) layout.getX(n), (float) layout.getY(n), 10);
			tmp.absoluteToRelative(layoutCenter);
			theNodes.add(tmp);
		}
		return theNodes;
	}

	/**
	 * Visual Edges factory (For rootGraph)
	 * 
	 * @return
	 */
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

	/**
	 * Update Visual Nodes relative to a given position
	 * 
	 * @param diffPos
	 */
	public void updateVNodesCoordinates(PVector diffPos) {
		for (VNode vN : getVNodes()) {
			vN.pos.sub(diffPos);
		}
	}

	/**
	 * Check if the current layout is an IterativeContext and makes one layout
	 * step
	 * 
	 */
	public void stepIterativeLayout(PVector vCommunityCenter) {
		// Step iteration as many times as parameterized
		if (isCurrentLayoutIterative()) {
			IterativeContext itrContext = (IterativeContext) layout;
			itrContext.step();
			// get nodes in layout positions
			for (Node n : layout.getGraph().getVertices()) {
				PVector nPos = new PVector((float) layout.getX(n), (float) layout.getY(n));
				// Get all vNodes
				for (VNode vN : vNodes) {
					if (vN.getNode().equals(n)) {
						// set new position
						vN.pos.set(nPos);
						vN.absoluteToRelative(layoutCenter);
						vN.pos.add(vCommunityCenter);
					}
				}
			}
		}
	}

	/**
	 * Get instances of the visual elements from a given graph (usually
	 * rootGraph) that are included in the Container's subGraph
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

	/**
	 * Edges retriever (For SubGraphs). Invoked by retrieveVisualElements()
	 * 
	 * @param vNode
	 * @param rootEdgeList
	 */
	private void vEdgeRetriever(VNode vNode, ArrayList<VEdge> rootEdgeList) {
		for (VEdge vEdg : rootEdgeList) {
			// Check if the VNode source matches any of the VEdge sources in the
			// rootGraph
			if (vEdg.getSource().equals(vNode))
				vEdges.add(vEdg);
		}
	}

	/**
	 * True if the container's layout implements IterativeContext. This means
	 * that the layout need to iterate over several times to achieve the
	 * distribution of vNodes
	 * 
	 * @return
	 */
	public boolean isCurrentLayoutIterative() {
		boolean currentLayoutIsIterativeInterface = false;
		// check if the layout implements IterativeContext
		for (int i = 0; i < layout.getClass().getGenericInterfaces().length; i++) {
			if (layout.getClass().getGenericInterfaces()[i].toString()
					.equals("interface edu.uci.ics.jung.algorithms.util.IterativeContext")) {
				currentLayoutIsIterativeInterface = true;
			}
		}
		return currentLayoutIsIterativeInterface;
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

	public void setName(String name) {
		this.name = name;
	}

	// *** Show
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

}
