package containers;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import processing.core.PVector;
import utilities.GraphLoader;
import utilities.mapping.Mapper;
import visualElements.Canvas;
import visualElements.VCommunity;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.primitives.VisualAtom;

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
	public static final int FRUCHTERMAN_REINGOLD = 2;
	// JUNG graph
	protected Graph<Node, Edge> graph;
	public DirectedSparseMultigraph<Node, Edge> rootGraph;
	// Visual Elements
	// All VNodes including VCommunities
	protected ArrayList<VNode> vNodes;
	protected ArrayList<VEdge> vEdges;
	protected ArrayList<VEdge> vExtEdges;

	// Custom Layouts
	protected String name = "no name";
	public PVector layoutCenter;
	public AbstractLayout<Node, Edge> layout;
	public int currentLayout;
	private Dimension dimension;

	// Iteration gate control
	private boolean initializationComplete = false;
	public ArrayList<Container> betweenEdgeGates;
	protected boolean done = false;

	protected Color color;
	protected int iterations;
	protected final int MAX_ITERATIONS = 100;

	// *** Constructor
	public Container() {
	}

	public Container(Graph<Node, Edge> graph) {
		this.graph = graph;

		// Instantiate empty collections
		vNodes = new ArrayList<VNode>();
		vEdges = new ArrayList<VEdge>();
		vExtEdges = new ArrayList<VEdge>();
		betweenEdgeGates = new ArrayList<Container>();
	}

	/**
	 * This method is used to distribute nodes in layout only once after the
	 * user clicks (opens) on the vCommunity cover
	 * 
	 */
	public boolean initialize() {
		if (!initializationComplete) {
			System.out.println(this.getClass().getName() + " Initializing nodes in: " + getName());
			distributeNodesInLayout(currentLayout, dimension);
			if (vNodes.size() == 0) {
				// Generate Visual Elements
				System.out.println(this.getClass().getName() + " Building visual nodes");
				runNodeFactory();
				System.out.println(this.getClass().getName() + " Building visual edges");
				runEdgeFactory();
				System.out.println(this.getClass().getName() + " Retrieving VNode successors");
				retrieveVNodeSuccessors(layout.getGraph());
			} else {
				setVElementCoordinates();
			}
			initializationComplete = true;
		}
		return initializationComplete;
	}

	// *** Factories
	/**
	 * Visual Nodes factory (For rootGraph)
	 * 
	 * @return
	 */
	protected void runNodeFactory() {
		// Instantiate vNodes
		for (Node n : layout.getGraph().getVertices()) {
			VNode tmp = new VNode(n, (float) layout.getX(n), (float) layout.getY(n)); // key
			// Compute and set the diameter
			float diameter = Mapper.getInstance().convert(Mapper.LINEAR, n.getOutDegree(1), 10, "Node", "outDegree");
			if (diameter > tmp.getDiameter()) {
				tmp.setDiameter(diameter);
			}
			tmp.absoluteToRelative(layoutCenter);
			tmp.setColor(color);
			vNodes.add(tmp);
		}
	}

	/**
	 * Visual Edges factory (For rootGraph)
	 * 
	 * @return
	 */
	public void runEdgeFactory() {
		for (Edge e : graph.getEdges()) {
			VEdge vEdge = new VEdge(e);
			vEdge.setSourceAndTarget(vNodes);
			vEdge.makeBezier();
			vEdges.add(vEdge);
		}
	}

	// *** Other methods

	/**
	 * Build all the external edges of vCommunities contained in a deployed
	 * community
	 */
	public void buildExternalEdges(ArrayList<VCommunity> otherVCommunities) {
		// For all otherCommunities
		for (VCommunity vC : otherVCommunities) {
			// See if this community's container has created betweenEdges with
			// any of them
			if (!betweenEdgeGates.contains(vC.container)) {
				Container otherContainer = vC.container;
				// See if otherCommunity's container has created betweenEdges
				// with this container
				if (!otherContainer.betweenEdgeGates.contains(this)) {
					// If the containers are not the same and are initialized
					if (!otherContainer.equals(this) && otherContainer.initializationComplete) {
						System.out.println(this.getClass().getName() + " " + this.getName()
								+ " is building External Edges for Vnodes with:" + otherContainer.getName());
						this.runExternalEdgeFactory(this.rootGraph, otherContainer.getName(), otherContainer);
						this.retrieveExternalVNodeSuccessors(this.rootGraph, otherContainer);
						otherContainer.retrieveExternalVNodeSuccessors(this.rootGraph, this);
						// Mark gates as closed for this community
						betweenEdgeGates.add(otherContainer);
						// Mark gates as closed for otherCommunity
						otherContainer.betweenEdgeGates.add(this);
					}
				}
			}
		}
	}

	/**
	 * Build all the external edges of this container with the one of a deployed
	 * community community
	 */
	public void buildExternalEdges(VCommunity otherVCommunity) {	
		if (!betweenEdgeGates.contains(otherVCommunity.container)) {
			Container otherContainer = otherVCommunity.container;
			// See if otherCommunity's container has created betweenEdges
			// with this container
			if (!otherContainer.betweenEdgeGates.contains(this)) {
				// If the containers are not the same and are initialized
				if (!otherContainer.equals(this) && otherContainer.initializationComplete) {
					System.out.println(this.getClass().getName() + " " + this.getName()
							+ " is building External Edges for Vnodes with:" + otherContainer.getName());
					this.runExternalEdgeFactory(this.rootGraph, otherContainer.getName(), otherContainer);
					this.retrieveExternalVNodeSuccessors(this.rootGraph, otherContainer);
					otherContainer.retrieveExternalVNodeSuccessors(this.rootGraph, this);
					// Mark gates as closed for this community
					betweenEdgeGates.add(otherContainer);
					// Mark gates as closed for otherCommunity
					otherContainer.betweenEdgeGates.add(this);
				}
			}
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
	public IterativeContext stepIterativeLayout(PVector vCommunityCenter) {
		// Step iteration as many times as parameterized
		IterativeContext itrContext = (IterativeContext) layout;
		// If node distribution not completed
		// if (!itrContext.done()) {
		if (!done && !itrContext.done()) {
			// Run one step
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
			iterations++;
			done = iterations == MAX_ITERATIONS;
		}
		return itrContext;
	}

	/**
	 * True if the container's layout implements IterativeContext. This means
	 * that the layout needs to iterate over several times to achieve the
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
		betweenEdgeGates.clear();
		runNodeFactory();
		runEdgeFactory();
	}

	/**
	 * Retrieves all the VNode successors for all VNodes of the container from a
	 * given graph
	 * 
	 * @param graph
	 * @return
	 */
	public void retrieveVNodeSuccessors(Graph<Node, Edge> graph) {
		for (VNode tmp : vNodes) {
			Collection<Node> succesorNodes = graph.getSuccessors(tmp.getNode());
			tmp.setVNodeSuccessors(getVNodes(succesorNodes));
		}
	}

	/**
	 * Retrieves all the external VNode successors (taken from extContainer) for
	 * all VNodes of the container from a given graph
	 * 
	 * @param graph
	 * @param extContainer
	 */
	public void retrieveExternalVNodeSuccessors(Graph<Node, Edge> graph, Container extContainer) {
		for (VNode tmp : vNodes) {
			Collection<Node> nodes = graph.getSuccessors(tmp.getNode());
			tmp.setVNodeSuccessors(extContainer.getVNodes(nodes));
		}
	}

	// *** Layouts
	protected void distributeNodesInLayout(int kindOfLayout, Dimension dimension) {
		switch (kindOfLayout) {
		// Circular layout
		case (Container.CIRCULAR):
			layout = circle(dimension);
			layoutCenter = new PVector(0, 0);
			break;
		// SpringLayout
		case (Container.SPRING):
			layout = spring(dimension);
			layoutCenter = new PVector((float) (layout.getSize().getWidth() / 2),
					(float) (layout.getSize().getHeight() / 2));
			break;
		// LinearLayout
		case (Container.FRUCHTERMAN_REINGOLD):
			layout = fruchtermanReingold(dimension);
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

	/**
	 * https://github.com/gephi/gephi/wiki/Fruchterman-Reingold
	 * http://jung.sourceforge.net/doc/api/index.html?edu/uci/ics/jung/
	 * algorithms/layout/CircleLayout.CircleVertexData.html
	 * 
	 * @return
	 */
	protected AbstractLayout<Node, Edge> fruchtermanReingold(Dimension dimension) {
		FRLayout<Node, Edge> frLayout = new FRLayout<Node, Edge>(graph, dimension);
		frLayout.setAttractionMultiplier(0.5);
		frLayout.setRepulsionMultiplier(0.5);
		frLayout.setMaxIterations(100);
		return frLayout;
	}

	public boolean isInitializationComplete() {
		return initializationComplete;
	}

	// *** Getters and setters
	public Graph<Node, Edge> getGraph() {
		return graph;
	}

	public int size() {
		return graph.getVertexCount();
	}

	/**
	 * Returns a the entire set of this cvollection's VNodes containing both
	 * VNodes and VCommunity instances
	 * 
	 * @return
	 */
	public ArrayList<VNode> getVNodes() {
		return vNodes;
	}

	/**
	 * Returns a subset of this cvollection's VNodes that are also VCommunity
	 * instances
	 * 
	 * @return
	 */
	public ArrayList<VCommunity> getVCommunities() {
		ArrayList<VCommunity> vCommunities = new ArrayList<VCommunity>();
		// This process is made one once
		if (vCommunities.size() == 0) {
			for (VNode vN : vNodes) {
				if (vN instanceof VCommunity) {
					vCommunities.add((VCommunity) vN);
				}
			}
		}
		return vCommunities;
	}

	/**
	 * Returns a subset of this collection's VNodes that are not VCommunity
	 * instances
	 * 
	 * @return
	 */
	public ArrayList<VNode> getJustVNodes() {
		ArrayList<VNode> justVNodes = new ArrayList<VNode>();
		// This process is made one once
		if (justVNodes.size() == 0) {
			justVNodes = new ArrayList<VNode>(getVNodes());
			justVNodes.removeAll(getVCommunities());
		}
		return justVNodes;
	}

	/**
	 * Returns a subset of VNodes of this container from a set of Nodes given
	 * 
	 * @param c
	 * @return
	 */
	public Collection<VNode> getVNodes(Collection<Node> c) {
		Collection<VNode> rtn = new ArrayList<VNode>();
		for (VNode vN : vNodes) {
			if (c.contains(vN.getNode())) {
				rtn.add(vN);
			}
		}
		return rtn;
	}

	public ArrayList<VEdge> getVEdges() {
		return vEdges;
	}

	public ArrayList<VEdge> getVExtEdges() {
		return vExtEdges;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set coordinates to Visual Elements
	 */
	private void setVElementCoordinates() {
		for (VNode vN : vNodes) {
			vN.setX((float) layout.getX(vN.getNode()));
			vN.setY((float) layout.getY(vN.getNode()));
		}
	}

	/**
	 * Set coordinates to a Visual Element translated to the specified position
	 * 
	 * @param newPosition
	 */
	public void translateVElementCoordinates(VNode vN, PVector newPosition) {
		float coordX = (float) layout.getX(vN.getNode()) + newPosition.x;
		float coordY = (float) layout.getY(vN.getNode()) + newPosition.y;
		vN.pos.set(coordX, coordY);
	}

	public void setRootGraph(DirectedSparseMultigraph<Node, Edge> rootGraph) {
		this.rootGraph = rootGraph;
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

	/**
	 * Build the vEdges between the nodes of this container and the container
	 * from another community.
	 * 
	 * @param completeGraph
	 * @param externalCommunityName
	 * @param externalContainer
	 * @return
	 */
	public void runExternalEdgeFactory(DirectedSparseMultigraph<Node, Edge> completeGraph, String externalCommunityName,
			Container externalContainer) {
		// Put all the VNodes from this container and the external container in
		// a single collection
		ArrayList<VNode> vNodesBothCommunities = new ArrayList<VNode>(this.vNodes);
		vNodesBothCommunities.addAll(externalContainer.getVNodes());
		// Here, we get a copy of all edges between the two containers.
		Graph<Node, Edge> filteredGraph = GraphLoader.filterByInterCommunities(completeGraph, this.getName(),
				externalCommunityName);
		Collection<Edge> edgesBetweenCommunities = filteredGraph.getEdges();
		// For each edge between containers
		for (Edge edgeBetweenCommunities : edgesBetweenCommunities) {
			// Make a VEdge
			VEdge vEdge = new VEdge(edgeBetweenCommunities);
			// Set source and target nodes
			vEdge.setSourceAndTarget(vNodesBothCommunities);
			// Make the linking curve
			vEdge.makeBezier();
			// Add vEdge to externalEdges of this container if the source node
			// belongs to this container
			if (this.graph.containsVertex(edgeBetweenCommunities.getSource())) {
				vExtEdges.add(vEdge);
			} else {
				// Otherwise, add it to externalEdges of the external container
				if (!externalContainer.vExtEdges.contains(vEdge)) {
					externalContainer.vExtEdges.add(vEdge);
				}
			}
		}
	}

	public void setvExtEdges(ArrayList<VEdge> vExtEdges) {
		this.vExtEdges = vExtEdges;
	}

	/**
	 * 
	 * @param graph
	 * @param seekNode
	 * @return
	 */
	protected Node getEqualNode(Graph<Node, Edge> graph, Node seekNode) {
		Node nodo = null;
		for (Node node : graph.getVertices()) {
			if (seekNode.equals(node)) {
				nodo = node;
				return nodo;
			}
		}
		return nodo;
	}

	public boolean isDone() {
		return done;
	}

	/**
	 * Hide or show the incident VEdges of a node.
	 * 
	 * @param node
	 * @param visibility
	 */
	public void setIncidentEdgesVisibility(Node node, boolean visibility) {
		for (Edge e : graph.getIncidentEdges(node)) {
			for (VEdge vE : this.vEdges) {
				if (vE.getEdge().equals(e)) {
					vE.setHidden(!visibility);
				}
			}
		}
	}

	/**
	 * Draws the rectangular boundaries of the container starting from the
	 * origin. Draws a cross hair at the center of the Dimension rectangle
	 * 
	 * @param origin
	 */
	public void showBoundaries(PVector origin) {
		PVector originShifted = new PVector(origin.x - (dimension.width / 2), origin.y - (dimension.height / 2));
		Canvas.app.stroke(255, 0, 0);
		Canvas.app.strokeWeight(2);
		Canvas.app.noFill();
		Canvas.app.rect(originShifted.x, originShifted.y, dimension.width, dimension.height);
		Canvas.app.stroke(0, 255, 0);
		Canvas.app.line(originShifted.x + (dimension.width / 2), originShifted.y,
				originShifted.x + (dimension.width / 2), originShifted.y + dimension.height);
		Canvas.app.line(originShifted.x, originShifted.y + (dimension.height / 2), originShifted.x + +(dimension.width),
				originShifted.y + +(dimension.height / 2));
	}
}
