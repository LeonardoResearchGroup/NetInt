package executable;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections15.Predicate;

import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import utilities.GraphmlReader;

public class GraphLoader {

	public DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph;
	GraphmlReader reader;

	public GraphLoader(String file, String coomunityFilter, String nodeLabel) {
		reader = new GraphmlReader(file);
		jungGraph = reader.getJungDirectedGraph(coomunityFilter, nodeLabel);
		System.out.println("GraphLoader> " + reader.getCommunities().size() + "communities loaded");
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
	 * Uses 0 as index because this method is only ussed for root Graphs
	 * 
	 * @param graph
	 */
	public static void setNodesDegrees(Graph<Node, Edge> graph) {
		// Degree
		for (Node n : graph.getVertices()) {
			n.setDegree(0, graph.degree(n));
			n.setOutDegree(0, graph.getSuccessorCount(n));
			n.setInDegree(0, graph.getPredecessorCount(n));
		}
		
		System.out.println("GraphLoader> degrees assigned to " + graph.getVertices().size() + " nodes");
	}

	/**
	 * Uses 0 as index because this method is only ussed for root Graphs
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
	 * Uses 0 as index because this method is only ussed for root Graphs
	 * 
	 * @param graph
	 */
	public static void setNodesInDegree(Graph<Node, Edge> graph) {
		// Degree
		for (Node n : graph.getVertices()) {
			n.setInDegree(0, graph.getPredecessorCount(n));
		}
	}

	public void printJungGraph(boolean printDetails) {
		System.out.println("Nodes: " + jungGraph.getVertexCount());
		System.out.println("Edges: " + jungGraph.getEdgeCount());
		if (printDetails) {
//			Collection<graphElements.Edge> edges = jungGraph.getEdges();
//			for (graphElements.Edge e : edges) {
//				System.out.println("from: " + e.getSource().getName() + " " + e.getSource().getId() + " to: "
//						+ e.getTarget().getName() + " " + e.getTarget().getId());
//			}
//
//			Collection<Node> nodes = jungGraph.getVertices();
//			for (Node n : nodes) {
//				System.out.print(n.getName() + " has ID: " + n.getId());
//				System.out.println("  Predecessors count: " + jungGraph.getPredecessorCount(n));
//			}
			
			for(Object ob: jungGraph.getVertices()){
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
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
				.transform(jungGraph);
		// Set In and Out Degree
		for (Node n : problemGraph.getVertices()) {
			n.setOutDegree(n.getMetadataSize() - 1, problemGraph.getSuccessorCount(n));
			n.setInDegree(n.getMetadataSize() - 1, problemGraph.getPredecessorCount(n));
		}
		return problemGraph;

	}

	public static void main(String[] args) {
		GraphLoader gL = new GraphLoader("./data/graphs/Risk.graphml", "Continent", "label");
		gL.printJungGraph(true);
	}
}
