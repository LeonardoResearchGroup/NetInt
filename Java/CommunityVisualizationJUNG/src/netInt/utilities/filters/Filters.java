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
package netInt.utilities.filters;

import java.util.TreeMap;

import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.GraphLoader;
import netInt.utilities.predicates.Predicates;

/**
 * @author jsalam
 *
 */
public class Filters {

	// This graph contains all the nodes that have not been removed after
	// filtering operations using the predicate
	// filterAndRemoveCommunityLinks()
	private static Graph<Node, Edge> remainingGraph;
	// Collection of results
	private static TreeMap<String, Graph<Node, Edge>> filteredResults = new TreeMap<String, Graph<Node, Edge>>();

	private static Filters filtersInstance = null;

	public static Filters getInstance() {
		if (filtersInstance == null) {
			filtersInstance = new Filters();
		}
		return filtersInstance;
	}

	protected Filters() {
	}

	public Graph<Node, Edge> getRemainingGraph() {
		return remainingGraph;
	}

	public void setRootGraph() {
		remainingGraph = new DirectedSparseMultigraph<Node, Edge>();
		// Copying the graph
		for (Node n : GraphLoader.theGraph.getVertices())
			remainingGraph.addVertex(n);
		for (Edge e : GraphLoader.theGraph.getEdges())
			remainingGraph.addEdge(e, GraphLoader.theGraph.getIncidentVertices(e));
		System.out.println(getInstance().getClass().getName() + " Total edges in Filters' remainingGraph: "
				+ remainingGraph.getEdgeCount());
	}

	/**
	 * Returns a subgraph of jungGraph whose nodes belong to the specified
	 * community. It uses JUNG predicates, that are not fast enough for large datasets
	 * @param community community name
	 * 
	 * @return a subgraph in form of DirectedSparseMultigraph
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterNodeInCommunity(final String community) {

		// Check if this filter was used with this community before. If so,
		// return the stored result
		String key = makeKey(community, "edgeInterCommunities");
		
		if (filteredResults.containsKey(key)) {
			return (DirectedSparseMultigraph<Node, Edge>) filteredResults.get(key);
		} else {

			// Make the predicate
			VertexPredicateFilter<Node, Edge> filter = new VertexPredicateFilter<Node, Edge>(Predicates.nodeInCommunity(community));
			
			// Filter the graph
			DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter.transform(GraphLoader.theGraph);
			
			// Set In and Out Degree
			for (Node n : problemGraph.getVertices()) {
				n.setOutDegree(n.getMetadataSize() - 1, problemGraph.getSuccessorCount(n));
				n.setInDegree(n.getMetadataSize() - 1, problemGraph.getPredecessorCount(n));
			}
			
			// System.out.println(getInstance().getClass().getName() + "
			// filter:"+ key);
			// filterResults.put(key, problemGraph);
			return problemGraph;
		}
	}
//	
//	/**
//	 * Returns a subgraph of jungGraph whose nodes belong to the specified
//	 * community. It uses CUSTOM MADE filtering that outperforms JUNG's predicates
//	 * @param community community name
//	 * 
//	 * @return a subgraph in form of DirectedSparseMultigraph
//	 */
//	public static DirectedSparseMultigraph<Node, Edge> filterNodeInCommunity(final String community) {
//
//		// Check if this filter was used with this community before. If so,
//		// return the stored result
//		String key = makeKey(community, "edgeInterCommunities");
//		
//		if (filteredResults.containsKey(key)) {
//			return (DirectedSparseMultigraph<Node, Edge>) filteredResults.get(key);
//		} else {
//
//		
//		}
//	}

	/**
	 * Returns a subgraph of jungGraph whose edges connect the specified
	 * communities in either direction
	 * 
	 * @param communityNameA
	 *            The name of one community
	 * @param communityNameB
	 *            The name of a second community
	 * @return DirectedSparseMultigraph
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterEdgeLinkingCommunities(final String communityNameA,
			final String communityNameB) {

		// Check if this filter was used in these communities before. If so,
		// return the stored result
		String[] keys = makeDoubleKey(communityNameA, communityNameB, "edgeInterCommunities");
		if (filteredResults.containsKey(keys[0])) {
			return (DirectedSparseMultigraph<Node, Edge>) filteredResults.get(keys[0]);
		} else if (filteredResults.containsKey(keys[1])) {
			return (DirectedSparseMultigraph<Node, Edge>) filteredResults.get(keys[1]);
		} else {

			// System.out.println(getInstance().getClass().getName() + " filter:
			// " + keys[0]);
			// Filter and extractor
			EdgePredicateFilter<Node, Edge> filter = new EdgePredicateFilter<Node, Edge>(
					Predicates.edgeLinkingCommunities(communityNameA, communityNameB));
			DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
					.transform(GraphLoader.theGraph);
			// filterResults.put(keys[0], problemGraph);
			return problemGraph;
		}
	}

	/**
	 * Returns a subgraph of jungGraph whose edges connect the specified
	 * communities in either direction AND REMOVES edges from
	 * Filter.remainingGraph
	 * 
	 * @param communityNameA
	 *            The name of one community
	 * @param communityNameB
	 *            The name of a second community
	 * @return DirectedSparseMultigraph
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterAndRemoveEdgeLinkingCommunity(final String communityNameA,
			final String communityNameB) {

		// Check if this filter was used in these communities before. If so,
		// return the stored result
		String[] keys = makeDoubleKey(communityNameA, communityNameB, "edgeInterCommunities");
		if (filteredResults.containsKey(keys[0])) {
			return (DirectedSparseMultigraph<Node, Edge>) filteredResults.get(keys[0]);
		} else if (filteredResults.containsKey(keys[1])) {
			return (DirectedSparseMultigraph<Node, Edge>) filteredResults.get(keys[1]);
		} else {

			// System.out.println(getInstance().getClass().getName() + " filter:
			// " + keys[0]);
			// Filter and extractor
			FilterAndExtractorEdges<Node, Edge> filter = new FilterAndExtractorEdges<Node, Edge>(
					Predicates.edgeLinkingCommunities(communityNameA, communityNameB),
					Predicates.edgeInCommunity(communityNameA));
			DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
					.transform(remainingGraph);
			// filterResults.put(keys[0], problemGraph);
			return problemGraph;
		}
	}

	/**
	 * Returns a subgraph of jungGraph whose edges link nodes inside a specified
	 * community
	 * 
	 * @param community community name
	 * @return DirectedSparseMultigraph
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterEdgeInCommunity(final String community) {

		// Check if this filter was used with this community before. If so,
		// return the stored result
		String key = makeKey(community, "edgeInterCommunities");
		if (filteredResults.containsKey(key)) {
			return (DirectedSparseMultigraph<Node, Edge>) filteredResults.get(key);
		} else {

			// Instantiate the predicate
			EdgePredicateFilter<Node, Edge> filter = new EdgePredicateFilter<Node, Edge>(
					Predicates.edgeInCommunity(community));
			// Filter the graph
			DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
					.transform(GraphLoader.theGraph);
			// Set In and Out Degree
			for (Node n : problemGraph.getVertices()) {
				n.setOutDegree(n.getMetadataSize() - 1, problemGraph.getSuccessorCount(n));
				n.setInDegree(n.getMetadataSize() - 1, problemGraph.getPredecessorCount(n));
			}
			// System.out.println(getInstance().getClass().getName() + " filter:
			// "+ key);
			// filterResults.put(key, problemGraph);
			return problemGraph;
		}
	}

	private static String[] makeDoubleKey(String communityNameA, String communityNameB, String filter) {
		String[] rtn = new String[2];
		rtn[0] = communityNameA + "_" + communityNameB + "_" + filter;
		rtn[1] = communityNameB + "_" + communityNameA + "_" + filter;
		return rtn;
	}

	private static String makeKey(String communityNameA, String filter) {
		String rtn = communityNameA + "_" + filter;
		return rtn;
	}

}
