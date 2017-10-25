/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package netInt.utilities;

import java.util.ArrayList;
import java.util.Collection;

import edu.uci.ics.jung.graph.Graph;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.customCollections.NestedNodeMap;
import netInt.utilities.mapping.Mapper;

/**
 * PAJEK import changes made on feb 3. It has not ben tested.
 * 
 * @author jsalam
 *
 */
public class GraphLoader {

	// The original graph available across classes
	public static Graph<Node, Edge> theGraph;

	// The nested map of nodes avilable across classes
	// public static NestedNodeMap<String, Character> nestedMap;

	private GraphmlReader GMLreader;
	private PajekReader PJKreader;
	public static final int PAJEK = 1;
	public static final int GRAPHML = 0;

	private int fileFormat;

	/**
	 * @param file
	 *            The path to the source file
	 * @param nestedAttributesOrder
	 *            the vector of ordered node categorical attributes
	 * @param nodeLabelAttributes
	 *            List of node attributes to be retrieved from the source file
	 * @param edgeImportAttributes
	 *            List of edge attributes to be retrieved from the source file
	 * @param format
	 *            PAJEK = 1, GRAPHML = 0;
	 */
	public GraphLoader(String file, String[] nestedAttributesOrder, String[] nodeLabelAttributes,
			String[] edgeImportAttributes, int format) {
		int totalCommunities = 0;
		System.out.println(this.getClass().getName() + " GraphLoader");
		fileFormat = format;
		if (format == GraphLoader.PAJEK) {
			PJKreader = new PajekReader();
			theGraph = PJKreader.getGraph();
			totalCommunities = PJKreader.getCommunities().size();
		} else if (format == GraphLoader.GRAPHML) {
			GMLreader = new GraphmlReader(file);
			theGraph = GMLreader.getJungDirectedGraph(nestedAttributesOrder, nodeLabelAttributes, edgeImportAttributes,
					false);
			totalCommunities = GMLreader.getCommunities().size();
		}
		System.out.println("     Jung Graph Created from file:" + file);
		System.out.println("        Total Nodes in the graph: " + theGraph.getVertexCount());
		System.out.println("        Total Edges in the graph: " + theGraph.getEdgeCount());
		System.out.println("     " + totalCommunities + " communities names identified");

		// Iterate over elements to set attributes of nodes in the
		// GraphElements and Mapping
		for (Node n : theGraph.getVertices()) {
			setNodesDegrees(theGraph, n);
			Mapper.getInstance().setMaxMinNodeAttributes(n);
		}

		System.out.println("     Degrees assigned to nodes and attributes to Mapper Class");
		// ***** EDGE ATRIBUTES ADDED IN METHOD getJungDirectedGraph AROUND LINE
		// 183 OF GRAPHMLREADER CLASS
		System.out.println("     Edge attributes assigned to edges and to Mapper Class");

//		// Instantiation of nested map. Start() invoked in nestedMap contructor
//		nestedMap = new NestedNodeMap<String, Character>(theGraph.getVertices(), nestedAttributesOrder[0]);
//
//		// Initialization of nested map in a new thread
//		nestedMap.start();
//
//		System.out.println(
//				this.getClass().getName() + " Waiting for initialization completion of nested node structure ...");

	}

	public ArrayList<String> getCommunityNames() {
		if (fileFormat == GraphLoader.PAJEK) {
			return PJKreader.getCommunities();
		} else if (fileFormat == GraphLoader.GRAPHML) {
			return GMLreader.getCommunities();
		} else {
			return null;
		}
	}

	/**
	 * Uses 0 as index because this method is only used for root Graphs
	 * 
	 * @param graph
	 */
	private static void setNodesDegrees(Graph<Node, Edge> graph, Node n) {
		// Degree. 0 because it is the root community
		// Degree
		n.setDegree(0, graph.degree(n));
		// Out degree
		n.setOutDegree(0, graph.getSuccessorCount(n));
		// In Degree
		n.setInDegree(0, graph.getPredecessorCount(n));
	}

	public void printJungGraph(boolean printDetails) {
		System.out.println("Nodes: " + theGraph.getVertexCount());
		System.out.println("Edges: " + theGraph.getEdgeCount());
		if (printDetails) {
			Collection<netInt.graphElements.Edge> edges = theGraph.getEdges();
			for (netInt.graphElements.Edge e : edges) {
				System.out.println("from: " + e.getSource().getName() + " " + e.getSource().getId() + " to: "
						+ e.getTarget().getName() + " " + e.getTarget().getId());
			}

			Collection<Node> nodes = theGraph.getVertices();
			for (Node n : nodes) {
				System.out.print(n.getName() + " has ID: " + n.getId());
				System.out.println("  Predecessors count: " + theGraph.getPredecessorCount(n));
			}

			for (Object ob : theGraph.getVertices()) {
				Node n = (Node) ob;
				System.out.println(n.getName() + " has ID: " + n.getId());
			}
		}
	}

	public ArrayList<Edge> getFirstOrderEdgeList() {
		if (fileFormat == GraphLoader.GRAPHML)
			return GMLreader.getEdgesBetweenCommunities();
		else if (fileFormat == GraphLoader.PAJEK)
			return PJKreader.getEdgesBetweenCommunities();
		else
			return null;
	}

}
