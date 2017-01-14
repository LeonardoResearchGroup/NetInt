package containers;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;

import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import visualElements.VCommunity;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.primitives.VisualAtom;

public class SubContainer extends Container implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container sourceContainer;
	
	public SubContainer(){
		super();
	}

	public SubContainer(Graph<Node, Edge> subGraph, Container sourceContainer, int kindOfLayout, Dimension dimension) {
		super(subGraph);
		this.sourceContainer = sourceContainer;
		this.kindOfLayout = kindOfLayout;
		setDimension(dimension);
	}
	
	public SubContainer(Graph<Node, Edge> subGraph, int kindOfLayout, Dimension dimension) {
		super(subGraph);
		this.kindOfLayout = kindOfLayout;
		setDimension(dimension);
	}

	public SubContainer(Graph<Node, Edge> subGraph, int kindOfLayout, Dimension dimension, Color color) {
		super(subGraph);
		this.kindOfLayout = kindOfLayout;
		setDimension(dimension);
		this.color = color;
	}

	/**
	 * Get instances of the visual elements from a given graph (usually
	 * rootGraph) that are included in the Container's subGraph and set them the
	 * new coordinates according to the current layout.
	 * 
	 * @param container
	 */
	public void retrieveVisualElements(Container sourceContainer) {
		// For each node of Graph
		for (Node n : graph.getVertices()) {
			// Look for the corresponding VNode in the collection of VAtoms
			for (VisualAtom vAtm : sourceContainer.getVNodes()) {

				// Get only the visualAtoms that are visual Nodes
				if (VNode.class.isInstance(vAtm)) {
					VNode vN = (VNode) vAtm;

					/*
					 * If the current node of the subGraph matches the node
					 * inside the visual node retrieved from the collection of
					 * visual atoms
					 */
					if (n.equals(vN.getNode())) {
						// Add the VNode to the collection of vAtoms of this
						// container
						vN.setX((float) layout.getX(n));
						vN.setY((float) layout.getY(n));
						vNodes.add(vN);
						// Look for all the edges of that VNode and add them all
						// to the collection of vEdges of this container
						vEdgeRetriever(vN, sourceContainer.getVEdges());
					}
				}
			}
		}
	}

	/**
	 * Get instances of the visual elements from a given graph that are included
	 * in the Container's subGraph and set them the new coordinates according to
	 * the current layout. The VNodes added to this container are removed from
	 * the sourceContainer.
	 * 
	 * @param container
	 */
	public void retrieveVisualElements2(Container sourceContainer) {
		// For each node of Graph
		for (Node n : graph.getVertices()) {

			// Look for the corresponding VNode in the collection of VAtoms
			ListIterator<VNode> iterator = sourceContainer.getVNodes().listIterator();
			while (iterator.hasNext()) {
				// Get each visual Nodes
				VNode vN = (VNode) iterator.next();

				/*
				 * If the current node of the subGraph matches the node inside
				 * the visual node retrieved from the collection of visual atoms
				 */
				if (n.equals(vN.getNode())) {
					// Add the VNode to the collection of vAtoms of this
					// container
					vN.setX((float) layout.getX(n));
					vN.setY((float) layout.getY(n));
					vNodes.add(vN);
					// Look for all the edges of that VNode and add them all
					// to the collection of vEdges of this container
					vEdgeRetriever(vN, sourceContainer.getVEdges());
					// Remove to node from the other container
					iterator.remove();
				}
			}
		}
	}

	/**
	 * Get instances of the visual elements from a given list whose Nodes are
	 * included in the Container's graph
	 * 
	 * @param container
	 */
	public void assignVisualElements(ArrayList<VCommunity> vCommunities) {
		// For each node of Graph
		for (Node n : graph.getVertices()) {
			// Look for the corresponding VNode in the collection of VAtoms
			for (VCommunity vC : vCommunities) {
				/*
				 * If the current node of the subGraph matches the node inside
				 * the VisualAtom retrieved from the collection of visual atoms
				 */
				if (n.equals(vC.getNode())) {
					vNodes.add(vC);
					// Look for all the edges of that VNode and add them all
					// to the collection of vEdges of this container
					// vEdgeRetriever(vC, sourceContainer.getVEdges());
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
}
