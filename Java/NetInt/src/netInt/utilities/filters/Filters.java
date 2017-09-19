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

public class Filters {

	// This graph contains all the nodes that have not been removed after
	// filtering operations using the predicate
	// filterAndRemoveCommunityLinks()
	private static Graph<Node, Edge> remainingGraph;
	// Collection of results
	private static TreeMap<String, Graph<Node, Edge>> filterResults = new TreeMap<String, Graph<Node, Edge>>();

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
	 * community
	 * 
	 * @param comunidad
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterNodeInCommunity(final String community) {

		// Check if this filter was used with this community before. If so,
		// return the stored result
		String key = makeKey(community, "edgeInterCommunities");
		if (filterResults.containsKey(key)) {
			return (DirectedSparseMultigraph<Node, Edge>) filterResults.get(key);
		} else {

			// Make the predicate
			VertexPredicateFilter<Node, Edge> filter = new VertexPredicateFilter<Node, Edge>(
					Predicates.nodeInCommunity(community));
			// Filter the graph
			DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
					.transform(GraphLoader.theGraph);
			// Set In and Out Degree
			for (Node n : problemGraph.getVertices()) {
				n.setOutDegree(n.getMetadataSize() - 1, problemGraph.getSuccessorCount(n));
				n.setInDegree(n.getMetadataSize() - 1, problemGraph.getPredecessorCount(n));
			}
		//	System.out.println(getInstance().getClass().getName() + " filter:"+ key);
			//filterResults.put(key, problemGraph);
			return problemGraph;
		}
	}

	/**
	 * Returns a subgraph of jungGraph whose edges connect the specified
	 * communities in either direction
	 * 
	 * @param comumnity1
	 * @param comumnity2
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterEdgeLinkingCommunities(final String communityNameA,
			final String communityNameB) {

		// Check if this filter was used in these communities before. If so,
		// return the stored result
		String[] keys = makeDoubleKey(communityNameA, communityNameB, "edgeInterCommunities");
		if (filterResults.containsKey(keys[0])) {
			return (DirectedSparseMultigraph<Node, Edge>) filterResults.get(keys[0]);
		} else if (filterResults.containsKey(keys[1])) {
			return (DirectedSparseMultigraph<Node, Edge>) filterResults.get(keys[1]);
		} else {

		//	System.out.println(getInstance().getClass().getName() + " filter: " + keys[0]);
			// Filter and extractor
			EdgePredicateFilter<Node, Edge> filter = new EdgePredicateFilter<Node, Edge>(
					Predicates.edgeLinkingCommunities(communityNameA, communityNameB));
			DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
					.transform(GraphLoader.theGraph);
		//	filterResults.put(keys[0], problemGraph);
			return problemGraph;
		}
	}

	/**
	 * Returns a subgraph of jungGraph whose edges connect the specified
	 * communities in either direction AND REMOVES edges from
	 * Filter.remainingGraph
	 * 
	 * @param comumnity1
	 * @param comumnity2
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterAndRemoveEdgeLinkingCommunity(final String communityNameA,
			final String communityNameB) {

		// Check if this filter was used in these communities before. If so,
		// return the stored result
		String[] keys = makeDoubleKey(communityNameA, communityNameB, "edgeInterCommunities");
		if (filterResults.containsKey(keys[0])) {
			return (DirectedSparseMultigraph<Node, Edge>) filterResults.get(keys[0]);
		} else if (filterResults.containsKey(keys[1])) {
			return (DirectedSparseMultigraph<Node, Edge>) filterResults.get(keys[1]);
		} else {

		//	System.out.println(getInstance().getClass().getName() + " filter: " + keys[0]);
			// Filter and extractor
			FilterAndExtractorEdges<Node, Edge> filter = new FilterAndExtractorEdges<Node, Edge>(
					Predicates.edgeLinkingCommunities(communityNameA, communityNameB),
					Predicates.edgeInCommunity(communityNameA));
			DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
					.transform(remainingGraph);
		//	filterResults.put(keys[0], problemGraph);
			return problemGraph;
		}
	}

	/**
	 * Returns a subgraph of jungGraph whose edges link nodes inside a specified
	 * community
	 * 
	 * @param community
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterEdgeInCommunity(final String community) {

		// Check if this filter was used with this community before. If so,
		// return the stored result
		String key = makeKey(community, "edgeInterCommunities");
		if (filterResults.containsKey(key)) {
			return (DirectedSparseMultigraph<Node, Edge>) filterResults.get(key);
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
		//	System.out.println(getInstance().getClass().getName() + " filter: "+ key);
		//	filterResults.put(key, problemGraph);
			return problemGraph;
		}
	}

	private static String[] makeDoubleKey(String communityNameA, String communityNameB, String filter) {
		String[] rtn = new String[2];
		rtn[0] = communityNameA + "_" + communityNameB + "_" + filter;
		rtn[1] = communityNameB + "_" + communityNameA + "_"+filter;
		return rtn;
	}

	private static String makeKey(String communityNameA, String filter) {
		String rtn = communityNameA + "_" + filter;
		return rtn;
	}

}