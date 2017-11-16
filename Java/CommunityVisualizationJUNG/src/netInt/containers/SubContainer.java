/*******************************************************************************
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 *  
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
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
 
package netInt.containers;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import edu.uci.ics.jung.graph.Graph;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.visualElements.VCommunity;
import netInt.visualElements.VEdge;
import netInt.visualElements.VNode;
import netInt.visualElements.primitives.VisualAtom;

/**
 * Instances of SubContainer are Containers of references to Nodes and Edges and
 * their visual representations, i.e. the corresponding VNodes and VEdges. Thus
 * all visual elements included in instances of SubContainer are the visual
 * representations of Nodes and Edges instantiated in the root graph.
 * 
 * @author juan salamanca
 */
public class SubContainer extends Container implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container sourceContainer;

	public SubContainer(Graph<Node, Edge> subGraph, Container sourceContainer, int kindOfLayout, Dimension dimension) {
		super(subGraph);
		this.sourceContainer = sourceContainer;
		this.currentLayout = kindOfLayout;
		setDimension(dimension);
	}

	public SubContainer(Graph<Node, Edge> subGraph, int kindOfLayout, Dimension dimension) {
		super(subGraph);
		this.currentLayout = kindOfLayout;
		setDimension(dimension);
	}

	public SubContainer(Graph<Node, Edge> subGraph, int kindOfLayout, Dimension dimension, Color color) {
		super(subGraph);
		this.currentLayout = kindOfLayout;
		setDimension(dimension);
		this.color = color;
	}

	/**
	 * Get instances of the visual elements from a given list whose Nodes are
	 * included in the Container's graph
	 * 
	 * @param vCommunities
	 *            Visual Communities
	 */
	public void assignVisualElements(ArrayList<VCommunity> vCommunities) {
		// For each node of this container's Graph
		for (Node n : graph.getVertices()) {
			
			// Look for the corresponding VNode in the collection of VCommunities
			for (VCommunity vC : vCommunities) {
				/*
				 * If the current node of the subGraph matches the node inside
				 * the VisualAtom retrieved from the collection of visual atoms
				 */
				if (n.equals(vC.getNode())) {
					vNodes.put(n.getId(),vC);
					
					// Changes the vCommunity node for the corresponding node in the graph
					vC.setNode(n);
				}
			}
		}
	}

	/**
	 * Get instances of the visual elements from a given graph (usually
	 * rootGraph) that are included in the Container's subGraph and set them the
	 * new coordinates according to the current layout.
	 * 
	 * @param sourceContainer
	 *            sourceContainer
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
						vNodes.put(n.getId(),vN);
						// Look for all the edges of that VNode and add them
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
	 * @param sourceContainer
	 *            sourceContainer
	 */
	public void retrieveVisualElements2(Container sourceContainer) {
		// For each node of Graph
		for (Node n : graph.getVertices()) {
			// Look for the corresponding VNode in the collection of VAtoms

			Iterator<VNode> iterator = sourceContainer.getVNodes().iterator();
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
					vNodes.put(n.getId(),vN);
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
	 * Edges retriever (For SubGraphs). Invoked by retrieveVisualElements()
	 * 
	 * @param vNode
	 * @param edgeList
	 */
	private void vEdgeRetriever(VNode vNode, ArrayList<VEdge> edgeList) {
		for (VEdge vEdg : edgeList) {
			// Check if the VNode source matches any of the VEdge sources in the
			// rootGraph
			if (vEdg.getSource().equals(vNode))
				vEdges.add(vEdg);
		}
	}
}
