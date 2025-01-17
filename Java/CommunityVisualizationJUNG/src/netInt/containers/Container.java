/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.containers;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import netInt.canvas.Canvas;
import netInt.comparators.InDegreeComparator;
import netInt.containers.layout.ConcentricLayout;
import netInt.containers.layout.LinearLayout;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.GraphLoader;
import netInt.utilities.filters.Filters;
import netInt.utilities.mapping.Mapper;
import netInt.visualElements.VCommunity;
import netInt.visualElements.VEdge;
import netInt.visualElements.VNode;
import processing.core.PVector;

/**
 * This abstract class contains two collections, one for the visualNodes and one
 * for the visualEdges.
 * 
 * @author jsalam
 * 
 */
public abstract class Container {
	// Kinds of layouts
	// public static final int CIRCULAR = 0;
	public static final int SPRING = 1;
	public static final int FRUCHTERMAN_REINGOLD = 2;
	public static final int LINEAR = 3;
	public static final int CONCENTRIC = 4;
	// JUNG graph
	protected Graph<Node, Edge> graph;
	// Visual Elements
	// All VNodes including VCommunities
	protected HashMap<String, VNode> vNodes;
	protected ArrayList<VEdge> vEdges;
	protected ArrayList<VEdge> vExtEdges;

	// Custom Layouts
	protected String name = "no name";
	private PVector layoutCenter;
	public AbstractLayout<Node, Edge> layout;
	public int currentLayout;
	private boolean iterativeLayout;
	private Dimension dimension;

	// Iteration gate control
	private boolean initializationComplete = false;
	public ArrayList<Container> betweenEdgeGates;
	private boolean done = false;

	protected Color color;
	private int iterations = 0;
	protected final int MAX_ITERATIONS = 70;

	protected ArrayList<VCommunity> vCommunities = new ArrayList<VCommunity>();

	// Visibility
	public double degreeThreshold;
	public int[] degrees;

	// *** Constructor
	public Container(Graph<Node, Edge> graph) {
		this.graph = graph;
		// Instantiate empty collections
		vNodes = new HashMap<String, VNode>();
		vEdges = new ArrayList<VEdge>();
		vExtEdges = new ArrayList<VEdge>();
		betweenEdgeGates = new ArrayList<Container>();
	}

	/**
	 * This method is used to distribute nodes in layout only once after the
	 * user clicks (opens) on the vCommunity cover
	 * 
	 * @return true if initialization completed
	 */
	public boolean initialize() {
		if (!initializationComplete) {

			System.out.println(this.getClass().getName() + " Initializing nodes in container of " + getName());

			distributeNodesInLayout(currentLayout, dimension);

			if (vNodes.size() == 0) {

				// Generate Visual Elements
				System.out.println(
						this.getClass().getName() + " Building " + graph.getVertices().size() + " visual nodes");
				runVNodeFactory();

				System.out
						.println(this.getClass().getName() + " Building " + graph.getEdges().size() + " visual edges");
				runVEdgeFactory();

				System.out.println(this.getClass().getName() + " Retrieving VNode successors");

				retrieveVNodeSuccessors(layout.getGraph());

			} else {
				System.out
						.println(this.getClass().getName() + " Building " + graph.getEdges().size() + " visual edges");
				runVEdgeFactory();

				setVElementCoordinates();
			}

			iterativeLayout = isCurrentLayoutIterative();

			initializationComplete = true;
		}
		return initializationComplete;
	}

	// *** Factories
	/**
	 * Visual Nodes factory (For rootGraph)
	 * 
	 */
	protected void runVNodeFactory() {
		// Instantiate vNodes
		for (Node n : layout.getGraph().getVertices()) {
			VNode tmp = new VNode(n, (float) layout.getX(n), (float) layout.getY(n)); // key

			tmp.absoluteToRelative(layoutCenter);

			tmp.setColor(color);

			vNodes.put(n.getId(), tmp);
		}
	}

	/**
	 * Visual Edges factory (For rootGraph)
	 * 
	 */
	public void runVEdgeFactory() {
		for (Edge e : graph.getEdges()) {
			VEdge vEdge = new VEdge(e);
			vEdge.setSourceAndTarget(vNodes);
			vEdge.makeBezier();
			vEdges.add(vEdge);

		}
	}

	// *** Other methods

	/**
	 * Sets the degrees of this container graph. It is useful because the graph
	 * of higher tier containers is populated during the loading time. This
	 * graph usually contains nodes that represent communities and edges linking
	 * communities
	 */
	public void setGraphDegrees() {
		for (Node n : this.getGraph().getVertices()) {
			n.setInDegree(0, this.getGraph().getPredecessorCount(n));
			n.setOutDegree(0, this.getGraph().getSuccessorCount(n));
			n.setDegree(0, this.getGraph().degree(n));
		}
	}
	
	/**
	 * This method is used only for the adaptive performance function
	 */
	public void sortNodesDegrees() {
		int numberNodes = this.getGraph().getVertices().size();
		int i = 0;
		degrees = new int[numberNodes];

		// For all the nodes in the graph
		degreeThreshold = 0;

		for (Node n : this.getGraph().getVertices()) {
			int degree = this.getGraph().degree(n);
			degrees[i] = degree;
			i++;
		}
		// Nodes sorted for adaptive performance
		Arrays.sort(degrees);
	}

	/**
	 * This method is intended to populate a container's empty graph with
	 * GraphElements from the non-empty subGraphs of edges between nodes marked
	 * a members of this community and those marked as members of other
	 * communities.
	 * 
	 * It creates an edge between the VCommunity to which this container belongs
	 * and VCommunities received as parameter if there is an actual edge whose
	 * source or target are marked as members of either communities.
	 * 
	 * Each edge has a weight equals to the number of edges linking both
	 * communities
	 * 
	 * 
	 * @param edgeList
	 *            list of edges
	 */
	public void populateGraphfromEdgeList(ArrayList<Edge> edgeList, ArrayList<VCommunity> communities) {
		
		for (VCommunity vC : communities) {
			this.getGraph().addVertex(vC.getNode());
		}

		for (Edge e : edgeList) {

			this.getGraph().addEdge(e, e.getSource(), e.getTarget());
		}

		System.out.println(this.getClass().getName() + " Container's Graph Population Completed: " + this.getGraph().getVertexCount() + " total nodes.");
	}

	/**
	 * This method is intended to populate a container's empty graph with
	 * GraphElements from the non-empty subGraphs of edges between nodes marked
	 * a members of this community and those marked as members of other
	 * communities.
	 * 
	 * It creates an edge between the VCommunity to which this container belongs
	 * and VCommunities received as parameter if there is an actual edge whose
	 * source or target are marked as members of either communities.
	 * 
	 * Each edge has a weight equals to the number of edges linking both
	 * communities
	 * 
	 * @param intVComm
	 *            the list of VCommunities
	 */
	public void populateGraphfromVCommunities(ArrayList<VCommunity> intVComm) {
		System.out.println(this.getClass().getName() + " Building between edges for: " + getVCommunities().size()
				+ " communities");

		// Detect linked communities and build edges
		for (int i = 0; i < intVComm.size(); i++) {
			VCommunity vCA = intVComm.get(i);
			System.out.println("     Building edges linking : " + vCA.getNode().getName());

			for (int j = i + 1; j < intVComm.size(); j++) {
				VCommunity vCB = intVComm.get(j);

				// Detect linking edges
				DirectedSparseMultigraph<Node, Edge> linkingEdges = vCA.detectLinkedCommunities(vCB);

				// If communities are linked
				if (linkingEdges.getEdgeCount() > 0) {
					System.out.println("           ...  with: " + vCB.getNode().getName());

					// Build edge
					Edge tempEdge = new Edge(vCA.getNode(), vCB.getNode(), false);
					tempEdge.setAbsoluteAttribute("BtwnComm_weight", linkingEdges.getEdgeCount());

					// Add edge
					this.getGraph().addEdge(tempEdge, vCA.getNode(), vCB.getNode());
				} else {
					
					// Add unlinked communities
					this.getGraph().addVertex(vCA.getNode());
				}
			}
		}
	}

	/**
	 * Builds all the external edges of vCommunities contained in a deployed
	 * community
	 * 
	 * @param otherVCommunities
	 *            The VCommunities different than this one
	 */
	public void buildExternalEdges(ArrayList<VCommunity> otherVCommunities) {
		// For all otherCommunities
		for (VCommunity vC : otherVCommunities) {

			// See if this community's container has created betweenEdges with
			// any of them
			if (!betweenEdgeGates.contains(vC.container) && vC.getComCover().isDeployed()) {
				Container otherContainer = vC.container;

				// See if otherCommunity's container has created betweenEdges
				// with this container
				if (!otherContainer.betweenEdgeGates.contains(this)) {

					// If the containers are not the same and are initialized
					if (!otherContainer.equals(this) && otherContainer.initializationComplete) {

						System.out.println(this.getClass().getName() + " " + this.getName()
								+ " is building External Edges for Vnodes with:" + otherContainer.getName());

						this.runExternalEdgeFactory(otherContainer.getName(), otherContainer);

						this.retrieveExternalVNodeSuccessors(GraphLoader.theGraph, otherContainer);

						otherContainer.retrieveExternalVNodeSuccessors(GraphLoader.theGraph, this);

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
	 * Builds all the external edges of this container with the one of a
	 * deployed community community
	 * 
	 * @param otherVCommunity
	 *            VCommunity
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

					this.runExternalEdgeFactory(otherContainer.getName(), otherContainer);

					this.retrieveExternalVNodeSuccessors(GraphLoader.theGraph, otherContainer);

					otherContainer.retrieveExternalVNodeSuccessors(GraphLoader.theGraph, this);

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
	 *            Position
	 */
	public void updateVNodesCoordinates(PVector diffPos) {
		for (VNode vN : getVNodes()) {
			vN.getPos().sub(diffPos);
		}
	}

	/**
	 * Check if the current layout is an IterativeContext and runs one layout
	 * step
	 * 
	 * @param vCommunityCenter
	 *            The new center
	 * 
	 * @return the IterativeContext
	 */
	public IterativeContext stepIterativeLayout(PVector vCommunityCenter) {

		// Step iteration as many times as parameterized
		IterativeContext itrContext = (IterativeContext) layout;

		// If node distribution not completed
		if (!done && !itrContext.done()) {

			// Run one step
			itrContext.step();

			// get nodes in layout positions
			for (Node n : layout.getGraph().getVertices()) {

				PVector nPos = new PVector((float) layout.getX(n), (float) layout.getY(n));

				// Get all vNodes
				for (VNode vN : vNodes.values()) {

					if (vN.getNode().equals(n)) {

						// set new position
						vN.getPos().set(nPos);
						vN.absoluteToRelative(layoutCenter);
						vN.getPos().add(vCommunityCenter);
					}
				}
			}
			iterations++;

			done = iterations == MAX_ITERATIONS || itrContext.done();
			if (done) {
				Canvas.setAdaptiveDegreeThresholdPercentage(100);
			}
		}
		return itrContext;
	}

	/**
	 * True if the container's layout implements IterativeContext. This means
	 * that the layout needs to iterate over several times to achieve the
	 * distribution of vNodes
	 * 
	 * @return true if iterative
	 */
	private boolean isCurrentLayoutIterative() {

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
		runVNodeFactory();
		runVEdgeFactory();
	}

	/**
	 * Retrieves all the VNode successors for all VNodes of the container from a
	 * given graph
	 * 
	 * @param graph
	 *            The graph
	 */
	public void retrieveVNodeSuccessors(Graph<Node, Edge> graph) {

		for (VNode tmp : vNodes.values()) {
			Collection<Node> succesorNodes = graph.getSuccessors(tmp.getNode());
			tmp.setVNodeSuccessors(getVNodes(succesorNodes));
		}
	}

	/**
	 * Retrieves all the external VNode successors (taken from extContainer) for
	 * all VNodes of the container from a given graph
	 * 
	 * @param graph
	 *            The graph
	 * @param extContainer
	 *            The external container
	 */
	public void retrieveExternalVNodeSuccessors(Graph<Node, Edge> graph, Container extContainer) {
		for (VNode tmp : vNodes.values()) {
			Collection<Node> nodes = graph.getSuccessors(tmp.getNode());
			tmp.setVNodeSuccessors(extContainer.getVNodes(nodes));
		}
	}

	// *** Layouts
	/**
	 * Distributes nodes within the boundaries of a two dimensional space
	 * following the given algorithm
	 * 
	 * @param kindOfLayout
	 *            Defined as static attribute
	 * @param dimension
	 *            The boundaries of the two dimensional space
	 */
	protected void distributeNodesInLayout(int kindOfLayout, Dimension dimension) {

		switch (kindOfLayout) {

		// // Circular layout
		// case (Container.CIRCULAR):
		// layout = circle(dimension);
		// layoutCenter = new PVector(0, 0);
		// break;

		// SpringLayout
		case (Container.SPRING):
			layout = spring(dimension);
			layoutCenter = new PVector((float) (layout.getSize().getWidth() / 2),
					(float) (layout.getSize().getHeight() / 2));
			break;

		// FRLayout
		case (Container.FRUCHTERMAN_REINGOLD):
			layout = fruchtermanReingold(dimension);
			layoutCenter = new PVector((float) (layout.getSize().getWidth() / 2),
					(float) (layout.getSize().getHeight() / 2));
			break;

		// LinearLayout
		case (Container.LINEAR):
			layout = linear(dimension);
			layoutCenter = new PVector(0, (float) (layout.getSize().getHeight() / 2));
			break;

		// CircularConcentricLayout
		case (Container.CONCENTRIC):
			layout = concentric(dimension);
			layoutCenter = new PVector(0, 0);
			break;
		}
	}

	protected AbstractLayout<Node, Edge> circle(Dimension dimension) {
		CircleLayout<Node, Edge> circle = new CircleLayout<Node, Edge>(graph);
		circle.setSize(dimension);
		return circle;
	}

	protected AbstractLayout<Node, Edge> concentric(Dimension dimension) {
		ConcentricLayout<Node, Edge> circle = new ConcentricLayout<Node, Edge>(graph);
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
	 * @param dimension An instance of Dimension that defines the boundaries of the
	 *            layout
	 * @return the Fruchterman Reingold layout set to a given Dimension
	 */
	protected AbstractLayout<Node, Edge> fruchtermanReingold(Dimension dimension) {
		FRLayout<Node, Edge> frLayout = new FRLayout<Node, Edge>(graph, dimension);

		// attraction multiplier: how much edges try to keep their vertices
		// together
		// frLayout.setAttractionMultiplier(0.5);

		// repulsion multiplier: how much vertices try to push each other apart
		frLayout.setRepulsionMultiplier(0.5);

		frLayout.setMaxIterations(100);
		return frLayout;
	}

	protected AbstractLayout<Node, Edge> linear(Dimension dimension) {
		LinearLayout<Node, Edge> line = new LinearLayout<Node, Edge>(graph);
		// Order the vertex with the given comparator
		line.setVertexOrder(new InDegreeComparator());
		line.setSize(dimension);
		return line;
	}

	public boolean isInitializationComplete() {
		return initializationComplete;
	}

	public boolean isLayoutIterative() {
		return iterativeLayout;
	}

	// ****** Getters and setters *****
	public Graph<Node, Edge> getGraph() {
		return graph;
	}

	/**
	 * The number of nodes in this container
	 * 
	 * @return number of nodes in this container
	 */
	public int size() {
		return graph.getVertexCount();
	}

	/**
	 * Returns a the entire set of Nodes in this container
	 * 
	 * @return List of nodes
	 */
	public Collection<Node> getNodes() {
		return graph.getVertices();
	}

	/**
	 * Returns a the entire set of Edges in this container
	 * 
	 * @return List of edges
	 */
	public Collection<Edge> getEdges() {
		return graph.getEdges();
	}

	/**
	 * Returns a the entire set of this collection's VNodes containing both
	 * VNodes and VCommunity instances
	 * 
	 * @return List of vNodes
	 */
	public Collection<VNode> getVNodes() {
		return vNodes.values();
	}

	/**
	 * Returns a the entire set of this collection's VNodes containing both
	 * VNodes and VCommunity instances in a HashMap which includes Ids for every
	 * node
	 * 
	 * @return List of vNodes
	 */
	public HashMap<String, VNode> getVNodesById() {
		return vNodes;
	}

	/**
	 * Returns a subset of this collection's VNodes that are also VCommunity
	 * instances
	 * 
	 * @return List of VCommunities
	 */
	public ArrayList<VCommunity> getVCommunities() {
		// ArrayList<VCommunity> vCommunities = new ArrayList<VCommunity>();
		// This process is made one once
		if (vCommunities.size() == 0) {
			for (VNode vN : vNodes.values()) {
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
	 * @return List of VNodes
	 */
	public ArrayList<VNode> getJustVNodes() {
		ArrayList<VNode> justVNodes = new ArrayList<VNode>();
		// This process is made only once
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
	 *            Collection of nodes
	 * @return subset of collection of nodes
	 */
	public Collection<VNode> getVNodes(Collection<Node> c) {
		Collection<VNode> rtn = new ArrayList<VNode>();
		for (VNode vN : vNodes.values()) {

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

	public void addVNode(String id, VNode vN) {
		vNodes.put(id, vN);
	}

	// public void setVCommunitie(ArrayList<VCommunity> otherVNodes) {
	// vNodes.addAll(otherVNodes);
	// }

	// public void setVNodes(ArrayList<VCommunity> communities) {
	// vNodes.addAll(communities);
	// }

	/**
	 * Set coordinates to Visual Elements
	 */
	private void setVElementCoordinates() {
		for (VNode vN : vNodes.values()) {
			vN.setX((float) layout.getX(vN.getNode()));
			vN.setY((float) layout.getY(vN.getNode()));
		}
	}

	/**
	 * Translates the coordinates of a Visual Element relative to the specified
	 * position
	 * 
	 * @param vN
	 *            VNode
	 * @param newPosition
	 *            destination
	 */
	public void translateVElementCoordinates(VNode vN, PVector newPosition) {
		float coordX = (float) layout.getX(vN.getNode()) + newPosition.x;
		float coordY = (float) layout.getY(vN.getNode()) + newPosition.y;
		vN.getPos().set(coordX, coordY);
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
	 * @param externalCommunityName
	 *            Name of external community
	 * @param externalContainer
	 *            External container
	 */
	public void runExternalEdgeFactory(String externalCommunityName, Container externalContainer) {
		// Put all the VNodes from this container and the external container in
		// a single collection
		HashMap<String, VNode> vNodesBothCommunities = new HashMap<String, VNode>(this.vNodes);
		vNodesBothCommunities.putAll(externalContainer.getVNodesById());
		// Here, we get a copy of all edges between the two containers.
		Graph<Node, Edge> filteredGraph = Filters.filterEdgeLinkingCommunities(this.getName(), externalCommunityName);
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
	 *            Graph
	 * @param seekNode
	 *            node searched
	 * @return The node equal to the given node
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
	 *            Node
	 * @param visibility
	 *            true if visible
	 * @deprecated
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
	 *            rectangle origin
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
