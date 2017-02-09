package utilities.filters;

import org.apache.commons.collections15.Predicate;

import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import utilities.predicates.Predicates;

public class Filters {

	// this graph contains all the nodes that have not been removed after
	// filtering operations using the predicate
	// filterAndRemoveCommunitiesBridges()
	private static Graph<Node, Edge> remainingGraph;

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

	public void setGraph(Graph<Node, Edge> rootGraph) {
		remainingGraph = new DirectedSparseMultigraph<Node, Edge>();
		// Copying the graph
		for (Node n : rootGraph.getVertices())
			remainingGraph.addVertex(n);
		for (Edge e : rootGraph.getEdges())
			remainingGraph.addEdge(e, rootGraph.getIncidentVertices(e));
		System.out.println(getInstance().getClass().getName() + " Total edges in Filters' remainingGraph: "
				+ remainingGraph.getEdgeCount());
	}

	/**
	 * Returns a subgraph of jungGraph whose nodes belong to the specified
	 * community
	 * 
	 * @param jungGraph
	 * @param comunidad
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterByCommunity(Graph<Node, graphElements.Edge> jungGraph,
			final String community) {
		VertexPredicateFilter<Node, Edge> filter = new VertexPredicateFilter<Node, Edge>(
				Predicates.nodeInCommunity(community));
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
				.transform(jungGraph);
		// Set In and Out Degree
		for (Node n : problemGraph.getVertices()) {
			n.setOutDegree(n.getMetadataSize() - 1, problemGraph.getSuccessorCount(n));
			n.setInDegree(n.getMetadataSize() - 1, problemGraph.getPredecessorCount(n));
		}
		return problemGraph;
	}

	/**
	 * Returns a subgraph of jungGraph whose edges connect the specified
	 * communities in either direction jungGraph
	 * 
	 * @param jungGraph
	 * @param comumnity1
	 * @param comumnity2
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterByInterCommunities(
			Graph<Node, graphElements.Edge> jungGraph, final String communityNameA, final String communityNameB) {
		// Filter and extractor
		EdgePredicateFilter<Node, Edge> filter = new EdgePredicateFilter<Node, Edge>(
				Predicates.edgeLinkingCommunities(communityNameA, communityNameB));
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
				.transform(jungGraph);
		return problemGraph;

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
	public static DirectedSparseMultigraph<Node, Edge> filterAndRemoveCommunityLinks(final String communityNameA,
			final String communityNameB) {
		// Filter and extractor
		System.out.println("           ...  and community: " + communityNameB);
		FilterAndExtractorEdges<Node, Edge> filter = new FilterAndExtractorEdges<Node, Edge>(
				Predicates.edgeLinkingCommunities(communityNameA, communityNameB),
				Predicates.edgeInCommunity(communityNameA));
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
				.transform(remainingGraph);
		return problemGraph;
	}

	public static DirectedSparseMultigraph<Node, Edge> filterByEdgesBelonging(
			DirectedSparseMultigraph<Node, Edge> jungGraph, final String community) {
		EdgePredicateFilter<Node, Edge> filter = new EdgePredicateFilter<Node, Edge>(
				Predicates.edgeInCommunity(community));
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
				.transform(jungGraph);
		// Set In and Out Degree
		for (Node n : problemGraph.getVertices()) {
			n.setOutDegree(n.getMetadataSize() - 1, problemGraph.getSuccessorCount(n));
			n.setInDegree(n.getMetadataSize() - 1, problemGraph.getPredecessorCount(n));
		}
		return problemGraph;
	}

}
