package utilities;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections15.Predicate;

import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;

public class GraphLoader {

	public DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph;
	public GraphmlReader reader;
	public static final int PAJEK = 1;
	public static final int GRAPHML = 0;
	
	public GraphLoader(String file, String communityFilter, String graphmlLabel, String sector, String edgeWeight,
			int format) {
		
		if(format == GraphLoader.PAJEK){
			reader = new GraphmlReader();
			jungGraph = reader.readFromPajek(file);
		}else if(format == GraphLoader.GRAPHML){
			reader = new GraphmlReader(file);
			jungGraph = reader.getJungDirectedGraph(communityFilter, graphmlLabel, sector, edgeWeight, "CANTIDAD_TRNS");
			
		}
		System.out.println("GraphLoader> " + reader.getCommunities().size() + " communities loaded");
		// TODO Improve degree assigning with JUNG library method
		setNodesDegrees(jungGraph);
		System.out.println("GraphLoader> Graph Created from file:" + file);
		System.out.println(" Total Nodes: " + jungGraph.getVertexCount());
		System.out.println(" Total Edges: " + jungGraph.getEdgeCount());
	}

	public ArrayList<String> getCommunityNames() {
		return reader.getCommunities();
	}

	/**
	 * Uses 0 as index because this method is only used for root Graphs
	 * 
	 * @param graph
	 */
	public static void setNodesDegrees(Graph<Node, Edge> graph) {
		// Degree. 0 because it is the root community
		for (Node n : graph.getVertices()) {
			n.setDegree(0, graph.degree(n));
			n.setOutDegree(0, graph.getSuccessorCount(n));
			n.setInDegree(0, graph.getPredecessorCount(n));
		}

		System.out.println("GraphLoader> degrees assigned to " + graph.getVertices().size() + " nodes");
	}

	/**
	 * Uses 0 as index because this method is only used for root Graphs
	 * 
	 * @param graph
	 */
	public static void setNodesOutDegree(Graph<Node, Edge> graph) {
		// Degree
		for (Node n : graph.getVertices()) {
			n.setOutDegree(0, graph.getSuccessorCount(n));
		}
	}

	/**
	 * Uses 0 as index because this method is only used for root Graphs
	 * 
	 * @param graph
	 */
	public static void setNodesInDegree(Graph<Node, Edge> graph) {
		// Degree, 0 because it is the root community
		for (Node n : graph.getVertices()) {
			n.setInDegree(0, graph.getPredecessorCount(n));
		}
	}

	public void printJungGraph(boolean printDetails) {
		System.out.println("Nodes: " + jungGraph.getVertexCount());
		System.out.println("Edges: " + jungGraph.getEdgeCount());
		if (printDetails) {
			Collection<graphElements.Edge> edges = jungGraph.getEdges();
			for (graphElements.Edge e : edges) {
				System.out.println("from: " + e.getSource().getName() + " " + e.getSource().getId() + " to: "
						+ e.getTarget().getName() + " " + e.getTarget().getId());
			}

			Collection<Node> nodes = jungGraph.getVertices();
			for (Node n : nodes) {
				System.out.print(n.getName() + " has ID: " + n.getId());
				System.out.println("  Predecessors count: " + jungGraph.getPredecessorCount(n));
			}

			for (Object ob : jungGraph.getVertices()) {
				Node n = (Node) ob;
				System.out.println(n.getName() + " has ID: " + n.getId());
			}
		}
	}

	/**
	 * Returns a subgraph of jungGraph whose nodes belong to the specified
	 * community
	 * 
	 * @param jungGraph
	 * @param comunidad
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterByCommunity(
			DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph, final String community) {

		Predicate<Node> inSubgraph = new Predicate<Node>() {
			@Override
			public boolean evaluate(Node nodo) {
				return nodo.belongsTo(community);
			}
		};
		VertexPredicateFilter<Node, Edge> filter = new VertexPredicateFilter<Node, Edge>(inSubgraph);
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter.transform(jungGraph);
		// Set In and Out Degree
		for (Node n : problemGraph.getVertices()) {
			n.setOutDegree(n.getMetadataSize() - 1, problemGraph.getSuccessorCount(n));
			n.setInDegree(n.getMetadataSize() - 1, problemGraph.getPredecessorCount(n));
		}
		return problemGraph;

	}

	/**
	 * Returns a subgraph of jungGraph whose edges connect the specified
	 * communities community
	 * 
	 * @param jungGraph
	 * @param comumnity1	
	 * @param comumnity2
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterByInterCommunities(
			DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph, final String community1,
			final String community2) {

		Predicate<Edge> inSubgraph = new Predicate<Edge>() {
			@Override
			public boolean evaluate(Edge edge) {
				boolean connectsOneWay = edge.getSource().belongsTo(community1)
						&& edge.getTarget().belongsTo(community2);
				boolean connectsOtherWay = edge.getSource().belongsTo(community2)
						&& edge.getTarget().belongsTo(community1);
				boolean connectsCommunities = connectsOneWay || connectsOtherWay;
				return connectsCommunities;
			}
		};
		EdgePredicateFilter<Node, Edge> filter = new EdgePredicateFilter<Node, Edge>(inSubgraph);
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter.transform(jungGraph);
		return problemGraph;

	}

	public static void main(String[] args) {
		GraphLoader rootGraph = new GraphLoader("./data/graphs/Risk.graphml", "Continent", "label", "Continent", "weight", GraphLoader.GRAPHML);
		DirectedSparseMultigraph<Node, Edge> graphTemp = GraphLoader.filterByInterCommunities(rootGraph.jungGraph, "AF",
				"EU");
		System.out.println(graphTemp.getEdgeCount());
	}

}
