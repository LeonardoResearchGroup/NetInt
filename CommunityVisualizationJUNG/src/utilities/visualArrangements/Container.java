package utilities.visualArrangements;

import java.awt.Dimension;
import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import processing.core.PApplet;
import processing.core.PVector;
import visualElements.VEdge;
import visualElements.VNode;

/**
 * This abstract class contains two collections, one for the visualNodes and one
 * for the visualEdges.
 * 
 * @author jsalam
 * 
 */
public abstract class Container {
	// Kinds of layouts
	public static final int CIRCULAR = 0;
	public static final int SPRING = 1;
	// JUNG graph
	protected Graph<Node, Edge> graph;
	// Visual Elements
	protected ArrayList<VNode> vNodes;
	protected ArrayList<VEdge> vEdges;
	// Custom Layouts
	public ArrayList<Arrangement> customLayouts;
	protected String name = "no name";
	public PApplet app;
	public PVector layoutCenter;

	public AbstractLayout<Node, Edge> layout;

	// *** Constructor
	public Container(PApplet app, Graph<Node, Edge> graph) {
		this.app = app;
		this.graph = graph;

		// Instantiate empty collections
		vNodes = new ArrayList<VNode>();
		vEdges = new ArrayList<VEdge>();
	}

	/**
	 * Visual Nodes factory (For rootGraph)
	 * 
	 * @return
	 */
	protected void runNodeFactory() {
		// Instantiate vNodes
		for (Node n : layout.getGraph().getVertices()) {
			VNode tmp = new VNode(app, n, (float) layout.getX(n), (float) layout.getY(n), 10);
			tmp.absoluteToRelative(layoutCenter);
			vNodes.add(tmp);
		}
	}

	/**
	 * Visual Edges factory (For rootGraph)
	 * 
	 * @return
	 */
	protected void runEdgeFactory() {
		for (Edge e : graph.getEdges()) {
			VEdge vEdge = new VEdge(e);
			vEdge.setSourceAndTarget(vNodes);
			vEdge.makeBezier();
			vEdges.add(vEdge);
		}
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
	 * Check if the current layout is an IterativeContext and runs one layout
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
		runNodeFactory();
		runEdgeFactory();
	}

	public void setArrangement(Arrangement arg) {
		customLayouts.add(arg);
	}

	// *** Layouts
	
	protected void distributeNodesInLayout(int kindOfLayout, Dimension dimension){
		switch (kindOfLayout) {
		// Circular layout
		case (0):
			layout = circle(dimension);
			layoutCenter = new PVector(0, 0);
			break;
		// SpringLayout
		case (1):

			layout = spring(dimension);
			layoutCenter = new PVector((float) (layout.getSize().getWidth() / 2),
					(float) (layout.getSize().getHeight() / 2));
			break;
		}
	}
	
	protected AbstractLayout<Node, Edge> circle(Dimension dimension) {
		CircleLayout<Node, Edge> circle = new CircleLayout<Node, Edge>(graph);
		circle.setSize(dimension);
		return circle;
	}

	protected AbstractLayout<Node, Edge> spring(Dimension dimension) {
		SpringLayout<Node, Edge> spring = new SpringLayout<Node, Edge>(graph);
		spring.setSize(dimension);
		return spring;
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
	public String getName() {
		return name;
	}

	public AbstractLayout<Node, Edge> getLayout() {
		return layout;
	}

	public void setLayout(AbstractLayout<Node, Edge> layout) {
		this.layout = layout;
	}
}
